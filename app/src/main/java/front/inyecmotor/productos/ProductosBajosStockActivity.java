package front.inyecmotor.productos;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import front.inyecmotor.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductosBajosStockActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
<<<<<<< HEAD
    private static final String BASE_URL = "http://192.168.56.1:8080";  // Cambia a la URL de tu servidor
=======
    private static final String BASE_URL = "http://192.168.0.8:8080";  // Cambia a la URL de tu servidor
>>>>>>> c331e9f (Nuevas features)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos_activity);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar productos con bajo stock
        cargarProductosBajosStock();

        // Configura el botón de "Volver" en la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void cargarProductosBajosStock() {
        String token = getAuthToken();
        if (token == null) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Producto>> call = apiService.getProductosAReponer(token);

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body();
                    adapter = new ProductoAdapter(productos, ProductosBajosStockActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d("ProductosBajosStock", "Error al obtener productos bajos de stock");
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Log.e("ProductosBajosStock", "Error: " + t.getMessage());
            }
        });
    }

    private String getAuthToken() {
        String token = LoginActivity.PreferenceManager.getHashedPassword(this);
        if (token == null) {
            return null;
        }
        return "Bearer " + token;
    }
}
