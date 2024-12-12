package front.inyecmotor.modelos;
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
<<<<<<< HEAD
import androidx.appcompat.widget.Toolbar;
=======
<<<<<<< HEAD
=======
import androidx.appcompat.widget.Toolbar;
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)
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

<<<<<<< HEAD
    private static final String BASE_URL = "http://192.168.56.1:8080"; // Cambia esto según tu configuración
    private ApiService apiService;

    private EditText etNombre;
=======
    private static final String BASE_URL = "http://192.168.0.8:8080"; // Cambia esto según tu configuración
    private ApiService apiService;

    private EditText etNombre;
<<<<<<< HEAD
>>>>>>> c331e9f (Nuevas features)
    private Spinner spinnerMarca;
    private Spinner spinnerMotorLitros;
    private Spinner spinnerMotorTipo;
    private EditText etAnio;
    private Button btnGuardarModelo;
<<<<<<< HEAD
=======
=======
    private EditText etAnio;
    private EditText etMotorLitros;
    private Spinner spinnerMarca;
    private Spinner spinnerMotorTipo;
    private Button btnCrearModelo;
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_modelo);

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> c331e9f (Nuevas features)
        // Inicializa los campos y botones
        etNombre = findViewById(R.id.etNombre);
        spinnerMarca = findViewById(R.id.spinnerMarca);
        spinnerMotorLitros = findViewById(R.id.spinnerMotorLitros);
        spinnerMotorTipo = findViewById(R.id.spinnerMotorTipo);
        etAnio = findViewById(R.id.etAnio);
        btnGuardarModelo = findViewById(R.id.btnGuardarModelo);


        // Adaptadores para los spinners
<<<<<<< HEAD
=======
=======
        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre);
        etAnio = findViewById(R.id.etAnio);
        etMotorLitros = findViewById(R.id.etMotorLitros);
        spinnerMarca = findViewById(R.id.spinnerMarca);
        spinnerMotorTipo = findViewById(R.id.spinnerMotorTipo);
        btnCrearModelo = findViewById(R.id.btnGuardarModelo);

        // Configurar adaptadores para los spinners
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)
        ArrayAdapter<CharSequence> marcaAdapter = ArrayAdapter.createFromResource(this,
                R.array.marcas_array, android.R.layout.simple_spinner_item);
        marcaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarca.setAdapter(marcaAdapter);

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> c331e9f (Nuevas features)
        ArrayAdapter<CharSequence> motorLitrosAdapter = ArrayAdapter.createFromResource(this,
                R.array.motor_litros_array, android.R.layout.simple_spinner_item);
        motorLitrosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotorLitros.setAdapter(motorLitrosAdapter);

<<<<<<< HEAD
=======
=======
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)
        ArrayAdapter<CharSequence> motorTipoAdapter = ArrayAdapter.createFromResource(this,
                R.array.motor_tipo_array, android.R.layout.simple_spinner_item);
        motorTipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotorTipo.setAdapter(motorTipoAdapter);

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> c331e9f (Nuevas features)




<<<<<<< HEAD
=======
=======
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)
        // Configura Retrofit y ApiService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Configura el botón de guardar
<<<<<<< HEAD
        btnGuardarModelo.setOnClickListener(new View.OnClickListener() {
=======
<<<<<<< HEAD
        btnGuardarModelo.setOnClickListener(new View.OnClickListener() {
=======
        btnCrearModelo.setOnClickListener(new View.OnClickListener() {
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)
            @Override
            public void onClick(View v) {
                crearModelo();
            }
<<<<<<< HEAD

        });

        // Configura el botón de "Volver" en la Toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
=======
<<<<<<< HEAD
=======

>>>>>>> 6c95963 (Features)
        });
>>>>>>> c331e9f (Nuevas features)
    }

    private boolean validateInputs() {
        String marca = spinnerMarca.getSelectedItem().toString();
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> c331e9f (Nuevas features)
        String motorLitros = spinnerMotorLitros.getSelectedItem().toString();
        String motorTipo = spinnerMotorTipo.getSelectedItem().toString();

        if (etNombre.getText().toString().isEmpty()) {
            Toast.makeText(this, "El campo Nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar que no se haya seleccionado el valor por defecto
<<<<<<< HEAD
=======
=======
        String motorTipo = spinnerMotorTipo.getSelectedItem().toString();

>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)
        if (marca.equals("Seleccione Marca")) {
            Toast.makeText(this, "Debe seleccionar una marca", Toast.LENGTH_SHORT).show();
            return false;
        }

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> c331e9f (Nuevas features)
        if (motorLitros.equals("Seleccione Motor Litros")) {
            Toast.makeText(this, "Debe seleccionar los litros del motor", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (motorTipo.equals("Seleccione Tipo de Motor")) {
            Toast.makeText(this, "Debe seleccionar el tipo de motor", Toast.LENGTH_SHORT).show();
<<<<<<< HEAD
=======
=======
        if (etNombre.getText().toString().isEmpty()) {
            Toast.makeText(this, "El campo Modelo es obligatorio", Toast.LENGTH_SHORT).show();
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)
            return false;
        }

        if (etAnio.getText().toString().isEmpty()) {
            Toast.makeText(this, "El campo Año es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }

<<<<<<< HEAD
        return true;
    }

=======
<<<<<<< HEAD
        return true;
    }

=======
        if (motorTipo.equals("Seleccione Tipo de Motor")) {
            Toast.makeText(this, "Debe seleccionar el tipo de motor", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidMotorLitros(String motorLitros) {
        try {
            // Check format n.n
            if (!motorLitros.matches("\\d+\\.\\d+")) {
                Toast.makeText(this, "Formato de litros inválido. Use el formato n.n (ej: 1.6)", Toast.LENGTH_SHORT).show();
                return false;
            }
            double value = Double.parseDouble(motorLitros);
            return value > 0;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor ingrese un número válido", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)

    private void crearModelo() {

        if (!validateInputs()) {
            return;
        }

        try {
            String nombre = etNombre.getText().toString();
            String marca = spinnerMarca.getSelectedItem().toString();
<<<<<<< HEAD
            Double motorLitros = Double.parseDouble(spinnerMotorLitros.getSelectedItem().toString().trim());
=======
<<<<<<< HEAD
            Double motorLitros = Double.parseDouble(spinnerMotorLitros.getSelectedItem().toString().trim());
=======
            Double motorLitros = Double.parseDouble(etMotorLitros.getText().toString().trim());
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)
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
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
>>>>>>> c331e9f (Nuevas features)

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
<<<<<<< HEAD
=======
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)
}
