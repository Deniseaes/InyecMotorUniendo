package front.inyecmotor.proveedores;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import front.inyecmotor.crearProducto.CrearProductoActivity;
import front.inyecmotor.crearProducto.ProductoCreate;
import front.inyecmotor.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearProveedorActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://192.168.0.8:8080"; // Cambia esto según tu configuración
    private ApiService apiService;

    private EditText etNombre;
    private EditText etDireccion;
    private EditText etCuit;
    private EditText etTel;
    private EditText etEmail;
    private Button btnCrear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_proveedor);

        // Inicializa los campos y botones
        etNombre = findViewById(R.id.etNombre);
        etDireccion = findViewById(R.id.etDireccion);
        etCuit = findViewById(R.id.etCuit);
        etTel = findViewById(R.id.etTel);
        etEmail = findViewById(R.id.etEmail);
        btnCrear = findViewById(R.id.btnGuardarProveedor);

        // Configura Retrofit y ApiService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Configura el botón de guardar
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearProveedor();
            }
        });

        // Configura el botón de "Volver" en la Toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private boolean validateInputs() {
        if (etNombre.getText().toString().isEmpty() ||
                etDireccion.getText().toString().isEmpty() ||
                etCuit.getText().toString().isEmpty() ||
                etTel.getText().toString().isEmpty() ||
                etEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void crearProveedor() {

        if (!validateInputs()) {
            return;
        }

        try {
            String nombre = etNombre.getText().toString();
            String direccion = etDireccion.getText().toString();
            int cuit = Integer.parseInt(etCuit.getText().toString().trim());
            int tel = Integer.parseInt(etTel.getText().toString().trim());
            String email = etEmail.getText().toString();

            Long id = Long.valueOf(99999);
            ProveedorCreate nuevoProveedor = new ProveedorCreate(id, nombre, direccion, cuit, tel, email);

            // Obtener el token desde SharedPreferences
            String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(this);
            if (hashedPassword == null) {
                Toast.makeText(this, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
                return;
            }
            String token = "Bearer " + hashedPassword;

            Call<ProveedorCreate> call = apiService.crearProveedor(token, nuevoProveedor);
            call.enqueue(new Callback<ProveedorCreate>() {
                @Override
                public void onResponse(Call<ProveedorCreate> call, Response<ProveedorCreate> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CrearProveedorActivity.this, "Proveedor creado correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CrearProveedorActivity.this, "Error al crear proveedor: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProveedorCreate> call, Throwable t) {
                    Toast.makeText(CrearProveedorActivity.this, "Error en la comunicación con el servidor: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "CUIT y Teléfono deben ser números válidos", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
