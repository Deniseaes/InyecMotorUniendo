package front.inyecmotor.productos;
import front.inyecmotor.BuildConfig;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import front.inyecmotor.crearProducto.CrearProductoActivity;
import front.inyecmotor.crearProducto.TipoDTO;
import front.inyecmotor.login.LoginActivity;
import front.inyecmotor.modelos.Modelo;
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

    private AutoCompleteTextView autoCompleteModelos;

    private List<Modelo> allModelos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos_activity);
        productos = getIntent().getParcelableArrayListExtra("productos");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductoAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        searchViewProduct = findViewById(R.id.searchViewProduct);
        spinnerProductType = findViewById(R.id.spinnerProductType);
        autoCompleteModelos = findViewById(R.id.autoCompleteModelos);

        productos = getIntent().getParcelableArrayListExtra("productos");
        allProductos = new ArrayList<>(productos); // Guarda todos los productos
        adapter = new ProductoAdapter(productos, this);
        recyclerView.setAdapter(adapter);
        setupSearchView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupRetrofit();
        setupSearchView();
        setupSpinner();
        setupAutoCompleteModelos();
    }

    private void setupRetrofit() {
        String baseUrl = BuildConfig.BASE_URL; // Asegúrate de que esta URL sea correcta
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
                    tipos.add(0, new TipoDTO(-1, "Todos los tipos", null)); // Opción para mostrar todos los productos

                    // Adaptador personalizado
                    ArrayAdapter<TipoDTO> tiposAdapter = new ArrayAdapter<TipoDTO>(ProductosActivity.this, android.R.layout.simple_spinner_item, tipos) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            TextView label = (TextView) super.getView(position, convertView, parent);
                            label.setText(getItem(position).getNombre()); // Mostrar nombre del tipo
                            return label;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
                            label.setText(getItem(position).getNombre()); // Mostrar nombre en el dropdown
                            return label;
                        }
                    };

                    tiposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProductType.setAdapter(tiposAdapter);

                    spinnerProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            TipoDTO selectedTipo = (TipoDTO) parent.getItemAtPosition(position);
                            filterProductsByType(selectedTipo.getId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // No hacer nada si no se selecciona ningún tipo
                        }
                    });
                } else {
                    Toast.makeText(ProductosActivity.this, "Error al cargar tipos de productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoDTO>> call, Throwable t) {
                Toast.makeText(ProductosActivity.this, "Error de conexión al cargar tipos de productos", Toast.LENGTH_SHORT).show();
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

            // Convertimos tipoId a Long antes de enviarlo a la API
            apiService.getProductosByTipo(authToken, (long) tipoId).enqueue(new Callback<List<Producto>>() {
                @Override
                public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Producto> productosFiltrados = response.body();
                        Log.d(TAG, "Productos filtrados recibidos: " + productosFiltrados.size());
                        adapter.setProductos(productosFiltrados);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error al filtrar productos, código: " + response.code());
                        Toast.makeText(ProductosActivity.this, "Error al filtrar productos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Producto>> call, Throwable t) {
                    Log.e(TAG, "Error de conexión al filtrar por tipo", t);
                    Toast.makeText(ProductosActivity.this, "Error de conexión", Toast.LENGTH_LONG).show();
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

    private void setupAutoCompleteModelos() {
        autoCompleteModelos.setThreshold(1);
        autoCompleteModelos.setOnItemClickListener((parent, view, position, id) -> {
            if (parent.getAdapter().getCount() > position) {
                Modelo selectedModelo = (Modelo) parent.getItemAtPosition(position);
                if (selectedModelo != null) {
                    autoCompleteModelos.setText(selectedModelo.toString()); // Usa toString() para mostrar solo el nombre
                    filterProductsByModelo(selectedModelo.getId());
                    logModeloDetails(selectedModelo); // Usa toFullString() internamente para loggear todos los detalles
                }
            } else {
                Log.e(TAG, "Invalid position selected: " + position);
            }
        });
    
        autoCompleteModelos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterModeloSuggestions(s.toString());
            }
    
            @Override
            public void afterTextChanged(Editable s) {}
        });
    
        fetchAllModelos();
    }
    
    private void fetchAllModelos() {
        apiService.getModelos(authToken).enqueue(new Callback<List<Modelo>>() {
            @Override
            public void onResponse(Call<List<Modelo>> call, Response<List<Modelo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allModelos.clear();
                    allModelos.addAll(response.body());
                    Log.d(TAG, "Received " + allModelos.size() + " modelos");
                    if (!allModelos.isEmpty()) {
                        filterModeloSuggestions("");  // Update suggestions after fetching
                    } else {
                        Log.w(TAG, "No modelos received from API");
                    }
                } else {
                    Log.e(TAG, "Error al obtener modelos: " + response.code());
                }
            }
    
            @Override
            public void onFailure(Call<List<Modelo>> call, Throwable t) {
                Log.e(TAG, "Error al obtener modelos", t);
            }
        });
    }
    
    private void filterModeloSuggestions(String query) {
        Log.d(TAG, "Filtering modelo suggestions for query: " + query);
        Log.d(TAG, "Total modelos: " + allModelos.size());
        
        if (allModelos.isEmpty()) {
            Log.w(TAG, "No hay modelos para filtrar");
            return;
        }
        
        List<Modelo> filteredModelos = new ArrayList<>();
        for (Modelo modelo : allModelos) {
            if (modelo.getNombre().toLowerCase().contains(query.toLowerCase())) {
                filteredModelos.add(modelo);
            }
        }
        
        Log.d(TAG, "Filtered modelos: " + filteredModelos.size());
        
        if (!filteredModelos.isEmpty()) {
            ModeloAutoCompleteAdapter adapter = new ModeloAutoCompleteAdapter(this, filteredModelos);
            autoCompleteModelos.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Log.w(TAG, "No se encontraron modelos que coincidan con la consulta: " + query);
        }
    }
    
    private void filterProductsByModelo(int modeloId) {
        Log.d(TAG, "Filtrando productos por modelo ID: " + modeloId);
        apiService.getProductosByModelo(authToken, modeloId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() || response.code() == 302) {
                        ResponseBody responseBody = response.body();
                        if (responseBody == null) {
                            responseBody = response.errorBody();
                        }
                        
                        if (responseBody != null) {
                            String jsonString = responseBody.string();
                            Log.d(TAG, "Respuesta recibida: " + jsonString);
                            
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Producto>>(){}.getType();
                            List<Producto> filteredProductos = gson.fromJson(jsonString, listType);
                            
                            Log.d(TAG, "Productos filtrados recibidos: " + filteredProductos.size());
                            adapter.setProductos(filteredProductos);
                            adapter.notifyDataSetChanged();
                            
                            if (filteredProductos.isEmpty()) {
                                Toast.makeText(ProductosActivity.this, "No se encontraron productos para este modelo", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Respuesta vacía del servidor");
                            Toast.makeText(ProductosActivity.this, "No se recibieron datos del servidor", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Error al filtrar por modelo. Código: " + response.code());
                        Toast.makeText(ProductosActivity.this, "Error al filtrar por modelo", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error al leer la respuesta", e);
                    Toast.makeText(ProductosActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "Error al parsear JSON", e);
                    Toast.makeText(ProductosActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                }
            }
    
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Error de conexión al filtrar por modelo", t);
                Toast.makeText(ProductosActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logModeloDetails(Modelo modelo) {
        Log.d(TAG, "Modelo seleccionado: " + modelo.toFullString());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}