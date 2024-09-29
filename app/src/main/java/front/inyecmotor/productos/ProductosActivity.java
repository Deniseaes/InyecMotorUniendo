package front.inyecmotor.productos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import front.inyecmotor.crearProducto.TipoDTO;
import front.inyecmotor.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import okhttp3.ResponseBody;

public class ProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> productos;
    private List<Producto> allProductos;
    private ApiService apiService;
    private String authToken;

    private SearchView searchViewProduct;
    private Spinner spinnerProductType;

    private static final String TAG = "ProductosActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos_activity);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchViewProduct = findViewById(R.id.searchViewProduct);
        spinnerProductType = findViewById(R.id.spinnerProductType);

        productos = getIntent().getParcelableArrayListExtra("productos");
        allProductos = new ArrayList<>(productos); // Guarda todos los productos
        adapter = new ProductoAdapter(productos, this);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupRetrofit();
        setupSearchView();
        setupSpinner();
    }

    private void setupRetrofit() {
        String baseUrl = "http://192.168.0.8:8080"; // Asegúrate de que esta URL sea correcta
        Log.d(TAG, "URL base: " + baseUrl);
        
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        authToken = "Bearer " + LoginActivity.PreferenceManager.getHashedPassword(this);
        Log.d(TAG, "Token de autenticación: " + authToken);
    }

    private void setupSearchView() {
        searchViewProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProductsByName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProductsByName(newText);
                return true;
            }
        });
    }

    private void setupSpinner() {
        apiService.getTipos(authToken).enqueue(new Callback<List<TipoDTO>>() {
            @Override
            public void onResponse(Call<List<TipoDTO>> call, Response<List<TipoDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TipoDTO> tipos = response.body();
                    tipos.add(0, new TipoDTO(-1, "Todos los tipos", null));
                    TipoSpinnerAdapter adapter = new TipoSpinnerAdapter(ProductosActivity.this, tipos);
                    spinnerProductType.setAdapter(adapter);
    
                    spinnerProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            TipoDTO selectedTipo = (TipoDTO) parent.getItemAtPosition(position);
                            filterProductsByType(selectedTipo.getId());
                        }
    
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
    
            @Override
            public void onFailure(Call<List<TipoDTO>> call, Throwable t) {
                Toast.makeText(ProductosActivity.this, "Error al cargar tipos de productos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterProductsByName(String query) {
        List<Producto> filteredList = new ArrayList<>();
        for (Producto producto : allProductos) {
            if (producto.getNombre().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(producto);
            }
        }
        adapter.setProductos(filteredList);
        adapter.notifyDataSetChanged();
    }

    private void filterProductsByType(int tipoId) {
        if (tipoId == -1) {
            // Si se selecciona "Todos los tipos", mostrar todos los productos
            adapter.setProductos(allProductos);
            adapter.notifyDataSetChanged();
        } else {
            Log.d(TAG, "Filtrando productos por tipo: " + tipoId);
            apiService.getProductosByTipo(authToken, tipoId).enqueue(new Callback<List<Producto>>() {
                @Override
                public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        handleSuccessfulResponse(response.body());
                    } else if (response.code() == 302 && response.errorBody() != null) {
                        handleRedirectResponse(response.errorBody());
                    } else {
                        handleErrorResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<List<Producto>> call, Throwable t) {
                    Log.e(TAG, "Error de conexión al filtrar por tipo", t);
                    Toast.makeText(ProductosActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void handleSuccessfulResponse(List<Producto> productos) {
        Log.d(TAG, "Productos filtrados recibidos: " + productos.size());
        adapter.setProductos(productos);
        adapter.notifyDataSetChanged();
    }

    private void handleRedirectResponse(ResponseBody errorBody) {
        try {
            String responseString = errorBody.string();
            Log.d(TAG, "Respuesta de redirección: " + responseString);
            List<Producto> productos = new Gson().fromJson(responseString, new TypeToken<List<Producto>>(){}.getType());
            handleSuccessfulResponse(productos);
        } catch (IOException | JsonSyntaxException e) {
            Log.e(TAG, "Error al procesar la respuesta de redirección", e);
            Toast.makeText(ProductosActivity.this, "Error al procesar la respuesta del servidor", Toast.LENGTH_LONG).show();
        }
    }

    private void handleErrorResponse(Response<List<Producto>> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Cuerpo de error vacío";
            Log.e(TAG, "Error al obtener productos por tipo. Código: " + response.code() + ", Cuerpo: " + errorBody);
            Toast.makeText(ProductosActivity.this, "Error al obtener productos por tipo. Código: " + response.code(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "Error al leer el cuerpo del error", e);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}