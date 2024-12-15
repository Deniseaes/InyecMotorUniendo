package front.inyecmotor.modelos;
import front.inyecmotor.BuildConfig;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import front.inyecmotor.crearProducto.CrearProductoActivity;
import front.inyecmotor.crearProducto.ProductoCreate;
import front.inyecmotor.login.LoginActivity;
import androidx.appcompat.widget.Toolbar;
import front.inyecmotor.proveedores.CrearProveedorActivity;
import front.inyecmotor.proveedores.ProveedorCreate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class CrearModeloActivity extends AppCompatActivity {

    private static final String BASE_URL = BuildConfig.BASE_URL;; // Cambia esto según tu configuración
    private ApiService apiService;

    private EditText etNombre;
    private Spinner spinnerMarca;
    private Spinner spinnerMotorLitros;
    private Spinner spinnerMotorTipo;
    private EditText etAnio;
    private Button btnGuardarModelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_modelo);

        // Inicializa los campos y botones
        etNombre = findViewById(R.id.etNombre);
        spinnerMarca = findViewById(R.id.spinnerMarca);
        spinnerMotorLitros = findViewById(R.id.spinnerMotorLitros);
        spinnerMotorTipo = findViewById(R.id.spinnerMotorTipo);
        etAnio = findViewById(R.id.etAnio);
        btnGuardarModelo = findViewById(R.id.btnGuardarModelo);


        // Adaptadores para los spinners
        ArrayAdapter<CharSequence> marcaAdapter = ArrayAdapter.createFromResource(this,
                R.array.marcas_array, android.R.layout.simple_spinner_item);
        marcaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarca.setAdapter(marcaAdapter);

        ArrayAdapter<CharSequence> motorLitrosAdapter = ArrayAdapter.createFromResource(this,
                R.array.motor_litros_array, android.R.layout.simple_spinner_item);
        motorLitrosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotorLitros.setAdapter(motorLitrosAdapter);

        ArrayAdapter<CharSequence> motorTipoAdapter = ArrayAdapter.createFromResource(this,
                R.array.motor_tipo_array, android.R.layout.simple_spinner_item);
        motorTipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotorTipo.setAdapter(motorTipoAdapter);





        // Configura Retrofit y ApiService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Configura el botón de guardar
        btnGuardarModelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearModelo();
            }

        });

        // Configura el botón de "Volver" en la Toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private boolean validateInputs() {
        String marca = spinnerMarca.getSelectedItem().toString();
        String motorLitros = spinnerMotorLitros.getSelectedItem().toString();
        String motorTipo = spinnerMotorTipo.getSelectedItem().toString();

        if (etNombre.getText().toString().isEmpty()) {
            Toast.makeText(this, "El campo Nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar que no se haya seleccionado el valor por defecto
        if (marca.equals("Seleccione Marca")) {
            Toast.makeText(this, "Debe seleccionar una marca", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (motorLitros.equals("Seleccione Motor Litros")) {
            Toast.makeText(this, "Debe seleccionar los litros del motor", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (motorTipo.equals("Seleccione Tipo de Motor")) {
            Toast.makeText(this, "Debe seleccionar el tipo de motor", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAnio.getText().toString().isEmpty()) {
            Toast.makeText(this, "El campo Año es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void crearModelo() {

        if (!validateInputs()) {
            return;
        }

        try {
            String nombre = etNombre.getText().toString();
            String marca = spinnerMarca.getSelectedItem().toString();
            Double motorLitros = Double.parseDouble(spinnerMotorLitros.getSelectedItem().toString().trim());
            String motorTipo = spinnerMotorTipo.getSelectedItem().toString();
            int anio = Integer.parseInt(etAnio.getText().toString().trim());

            Long id = Long.valueOf(99999);
            ModeloCreate nuevoModelo = new ModeloCreate(id, nombre, marca, motorLitros, motorTipo, anio);

            // Obtener el token desde SharedPreferences
            String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(this);
            if (hashedPassword == null) {
                Toast.makeText(this, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
                return;
            }
            String token = "Bearer " + hashedPassword;

            Call<ModeloCreate> call = apiService.crearModelo(token, nuevoModelo);
            call.enqueue(new Callback<ModeloCreate>() {
                @Override
                public void onResponse(Call<ModeloCreate> call, Response<ModeloCreate> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CrearModeloActivity.this, "Modelo creado correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // Registro del código de respuesta y el cuerpo para más detalles
                        Log.e("CrearModelo", "Error al crear modelo: Código: " + response.code() + ", Mensaje: " + response.message());
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("CrearModelo", "Detalles del error: " + errorBody);
                            Toast.makeText(CrearModeloActivity.this, "Error al crear modelo: " + errorBody, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("CrearModelo", "No se pudo leer el cuerpo del error", e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ModeloCreate> call, Throwable t) {
                    Toast.makeText(CrearModeloActivity.this, "Error en la comunicación con el servidor: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Año debe ser un número válido", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
