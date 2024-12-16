package front.inyecmotor.modelos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import front.inyecmotor.crearProducto.ModeloDTO;
import front.inyecmotor.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ModeloAdapter adapter;
    private List<Modelo> modelos = new ArrayList<>();
    private List<Modelo> modelosFiltrados = new ArrayList<>();
    private MultiAutoCompleteTextView actvModelos;
    private static final String BASE_URL = "http://192.168.0.8:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modelos_activity);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa el MultiAutoCompleteTextView
        actvModelos = findViewById(R.id.actvModelos);
        actvModelos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    buscarModelos(s.toString());
                } else {
                    // Si no hay texto o es menos de 2 caracteres, muestra la lista completa
                    modelosFiltrados.clear();
                    modelosFiltrados.addAll(modelos);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Configura el adaptador del RecyclerView
        adapter = new ModeloAdapter(modelos, this);
        recyclerView.setAdapter(adapter);

        // Configura la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Cargar todos los modelos al iniciar
        obtenerTodosLosModelos();
    }

    private void obtenerTodosLosModelos() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(this);
        if (hashedPassword == null) {
            Toast.makeText(this, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "Bearer " + hashedPassword;

        Call<List<ModeloDTO>> call = apiService.getModelosDTO(token);
        call.enqueue(new Callback<List<ModeloDTO>>() {
            @Override
            public void onResponse(Call<List<ModeloDTO>> call, Response<List<ModeloDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    modelos.clear();
                    for (ModeloDTO modeloDTO : response.body()) {
                        Modelo modelo = new Modelo();
                        modelo.setId(modeloDTO.getId());
                        modelo.setNombre(modeloDTO.getNombre());
                        modelo.setAnio(modeloDTO.getAnio());
                        modelo.setMotorLitros(modeloDTO.getMotorLitros());
                        modelos.add(modelo);
                    }
                    // Actualiza la lista filtrada para mostrar todos
                    modelosFiltrados.clear();
                    modelosFiltrados.addAll(modelos);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ModelosActivity.this, "No se encontraron modelos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ModeloDTO>> call, Throwable t) {
                Toast.makeText(ModelosActivity.this, "Error al obtener modelos: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarModelos(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(this);
        if (hashedPassword == null) {
            Toast.makeText(this, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "Bearer " + hashedPassword;

        Call<List<ModeloDTO>> call = apiService.getModeloByName(token, query);
        call.enqueue(new Callback<List<ModeloDTO>>() {
            @Override
            public void onResponse(Call<List<ModeloDTO>> call, Response<List<ModeloDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    modelos.clear();
                    for (ModeloDTO modeloDTO : response.body()) {
                        Modelo modelo = new Modelo();
                        modelo.setId(modeloDTO.getId());
                        modelo.setNombre(modeloDTO.getNombre());
                        modelo.setAnio(modeloDTO.getAnio()); // Asegúrate de que estos métodos existan en Modelo
                        modelo.setMotorLitros(modeloDTO.getMotorLitros());
                        modelos.add(modelo);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ModelosActivity.this, "No se encontraron modelos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ModeloDTO>> call, Throwable t) {
                Toast.makeText(ModelosActivity.this, "Error al buscar modelos: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
