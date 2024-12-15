package front.inyecmotor.crearProducto;
import front.inyecmotor.BuildConfig;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.text.TextWatcher;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import front.inyecmotor.login.LoginActivity;
import front.inyecmotor.modelos.Modelo;
import front.inyecmotor.proveedores.Proveedor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class CrearProductoActivity extends AppCompatActivity {
    private EditText etCodigo, etNombre, etPrecioCosto, etPrecioVenta, etStockActual, etStockMax, etStockMin;
    private Button btnCrearProducto;
    private Button btnIncrementStockActual, btnDecrementStockActual, btnIncrementStockMax, btnDecrementStockMax, btnIncrementStockMin, btnDecrementStockMin;
    private MultiAutoCompleteTextView actvModelos;
    private Spinner spinnerTipos, spinnerProveedores;
    private LinearLayout listaModelos, listaProveedores ; //listaTipos

    // Listas de datos
    private List<Tipo> tipos;
    private List<Proveedor> proveedores;
    private List<Modelo> modelos;

    private List<Proveedor> selectedProveedores = new ArrayList<>();
    private List<String> selectedTipos = new ArrayList<>();
    private List<String> selectedModelos = new ArrayList<>();
    private List<Tipo> selectedProductTipos; //este
    private List<Proveedor> selectedProductProveedores; //Este agrega a la lista de seleccionados visible debajo del selector
    private List<Modelo> selectedProductModelos;

    private static final String BASE_URL = BuildConfig.BASE_URL; // Cambia esto según tu configuración
    private ApiService apiService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        // Inicialización de elementos de la interfaz
        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etPrecioCosto = findViewById(R.id.etPrecioCosto);
        etPrecioVenta = findViewById(R.id.etPrecioVenta);
        etStockActual = findViewById(R.id.etStockActual);
        etStockMax = findViewById(R.id.etStockMax);
        etStockMin = findViewById(R.id.etStockMin);

        // Botones de incrementar y decrementar
        btnIncrementStockActual = findViewById(R.id.btnIncrementStockActual);
        btnDecrementStockActual = findViewById(R.id.btnDecrementStockActual);
        btnIncrementStockMax = findViewById(R.id.btnIncrementStockMax);
        btnDecrementStockMax = findViewById(R.id.btnDecrementStockMax);
        btnIncrementStockMin = findViewById(R.id.btnIncrementStockMin);
        btnDecrementStockMin = findViewById(R.id.btnDecrementStockMin);
        // Botones de crear
        btnCrearProducto = findViewById(R.id.btnCrearProducto);



        // MultiAutoCompleteTextView y Spinner
        spinnerProveedores = findViewById(R.id.spinnerProveedores);
        actvModelos = findViewById(R.id.actvModelos);
        spinnerTipos = findViewById(R.id.spinnerTipos);

        tipos = new LinkedList<>();
        proveedores = new LinkedList<>();
        modelos = new LinkedList<>();

        listaModelos = findViewById(R.id.listaModelos);
        listaProveedores = findViewById(R.id.listaProveedores);
        //listaTipos = findViewById(R.id.listaTipos);
        // Listas para las selecciones
        selectedProductTipos = new ArrayList<>();
        selectedProductProveedores = new ArrayList<>();
        selectedProductModelos = new ArrayList<>();




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Obtener datos de la base de datos
        fetchProductTipos();
        fetchProductProveedores();


        // Configuración de botones de incremento/decremento
        setupStockButtons();

        // Configuración de los adaptadores para los campos desplegables
        setupMultiAutoCompleteTextView();
        setupSpinners();
        btnCrearProducto.setOnClickListener(v -> crearProducto());


        // Configura el botón de "Volver" en la Toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void cargarTipos() {
        String token = "Bearer " + LoginActivity.PreferenceManager.getHashedPassword(this);
        Call<List<TipoDTO>> call = apiService.getTipos(token);
    }



    private void crearProducto() {
        String codigo = etCodigo.getText().toString();
        String nombre = etNombre.getText().toString();

        // Verificaciones para asegurar que los campos no estén vacíos
        if (codigo.isEmpty() || nombre.isEmpty() || etPrecioCosto.getText().toString().isEmpty() ||
                etPrecioVenta.getText().toString().isEmpty() || etStockActual.getText().toString().isEmpty() ||
                etStockMax.getText().toString().isEmpty() || etStockMin.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precioCosto = Double.parseDouble(etPrecioCosto.getText().toString());
        double precioVenta = Double.parseDouble(etPrecioVenta.getText().toString());
        int stockActual = Integer.parseInt(etStockActual.getText().toString());
        int stockMax = Integer.parseInt(etStockMax.getText().toString());
        int stockMin = Integer.parseInt(etStockMin.getText().toString());

        // Obtener los IDs de los modelos seleccionados
        ArrayList<Integer> modelosIds = new ArrayList<>();
        for (Modelo modelo : selectedProductModelos) {
            modelosIds.add(modelo.getId());
        }

        // Obtener los IDs de los proveedores seleccionados
        ArrayList<Integer> proveedoresIds = new ArrayList<>();
        for (Proveedor proveedor : selectedProductProveedores) {
            proveedoresIds.add(proveedor.getId());
        }

        ArrayList<Integer> tipoIds = new ArrayList<>();
        for (Tipo tipo : selectedProductTipos) {
            tipoIds.add(tipo.getId());
        }

        Long id = Long.valueOf(99999);
        ProductoCreate nuevoProducto = new ProductoCreate(id, codigo, nombre, stockMin, stockMax, stockActual, precioVenta, precioCosto, proveedoresIds, tipoIds, modelosIds);
        System.out.println(nuevoProducto);

        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(CrearProductoActivity.this);
        if (hashedPassword == null) {
            Toast.makeText(CrearProductoActivity.this, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }
        String token = "Bearer " + hashedPassword;
        Log.d("Token", token);

        Call<ProductoCreate> call = apiService.crearProducto(token, nuevoProducto);
        call.enqueue(new Callback<ProductoCreate>() {
            @Override
            public void onResponse(Call<ProductoCreate> call, Response<ProductoCreate> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearProductoActivity.this, "Producto creado exitosamente", Toast.LENGTH_LONG).show();

                    // Limpiar listas y campos
                    selectedProductModelos.clear();
                    selectedProductProveedores.clear();
                    selectedProductTipos.clear();

                    actvModelos.setText("");


                    // Opcionalmente también podrías vaciar los campos de texto de los productos
                    etCodigo.setText("");
                    etNombre.setText("");
                    etPrecioCosto.setText("");
                    etPrecioVenta.setText("");
                    etStockActual.setText("");
                    etStockMax.setText("");
                    etStockMin.setText("");


                    finish();
                } else {
                    Toast.makeText(CrearProductoActivity.this, "Error al crear el producto", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Código de error: " + response.code() + " - Mensaje: " + response.message());
                    Toast.makeText(CrearProductoActivity.this, "Error al crear el producto. Código: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProductoCreate> call, Throwable t) {
                Toast.makeText(CrearProductoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Métodos para obtener datos de tipos, proveedores y modelos
    private void fetchProductTipos() {
        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(CrearProductoActivity.this);
        if (hashedPassword == null) {
            Toast.makeText(CrearProductoActivity.this, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "Bearer " + hashedPassword;

        Call<List<TipoDTO>> call = apiService.getTipos(token);
        call.enqueue(new Callback<List<TipoDTO>>() {
            @Override
            public void onResponse(Call<List<TipoDTO>> call, Response<List<TipoDTO>> response) {
                if (response.isSuccessful()) {
                    List<TipoDTO> tipoDtoList = response.body();
                    tipos.clear();
                    for (TipoDTO tipoDTO : tipoDtoList) {
                        tipos.add(tipoDTO.toTipo());
                    }

                    ArrayAdapter<String> tiposAdapter = new ArrayAdapter<>(CrearProductoActivity.this, android.R.layout.simple_spinner_item, getNombresTipos());
                    tiposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tiposAdapter.insert("Seleccione tipo", 0);
                    spinnerTipos.setAdapter(tiposAdapter);
                } else {
                    Toast.makeText(CrearProductoActivity.this, "Error al obtener tipos de producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoDTO>> call, Throwable t) {
                Toast.makeText(CrearProductoActivity.this, "Error de conexión al obtener tipos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProductProveedores() {
        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(CrearProductoActivity.this);
        if (hashedPassword == null) {
            Toast.makeText(CrearProductoActivity.this, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "Bearer " + hashedPassword;

        Call<List<ProveedorDTO>> call = apiService.getProveedoresDTO(token);
        call.enqueue(new Callback<List<ProveedorDTO>>() {
            @Override
            public void onResponse(Call<List<ProveedorDTO>> call, Response<List<ProveedorDTO>> response) {
                if (response.isSuccessful()) {
                    List<ProveedorDTO> proveedoresDtoList = response.body();
                    proveedores.clear();
                    for (ProveedorDTO proveedorDTO : proveedoresDtoList) {
                        proveedores.add(proveedorDTO.toProveedor());
                    }

                    ArrayAdapter<String> proveedoresAdapter = new ArrayAdapter<>(CrearProductoActivity.this, android.R.layout.simple_dropdown_item_1line, getNombresProveedores());
                    proveedoresAdapter.insert("Seleccione proveedor", 0);
                    spinnerProveedores.setAdapter(proveedoresAdapter);


                } else {
                    Toast.makeText(CrearProductoActivity.this, "Error al obtener proveedores", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProveedorDTO>> call, Throwable t) {
                Toast.makeText(CrearProductoActivity.this, "Error de conexión al obtener proveedores", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProductModelos(String query) {
        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(CrearProductoActivity.this);
        if (hashedPassword == null) {
            Toast.makeText(CrearProductoActivity.this, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "Bearer " + hashedPassword;

        // Llamada a la API para obtener los modelos que coinciden con el query
        Call<List<ModeloDTO>> call = apiService.getModeloByName(token, query);
        call.enqueue(new Callback<List<ModeloDTO>>() {
            @Override
            public void onResponse(Call<List<ModeloDTO>> call, Response<List<ModeloDTO>> response) {
                if (response.isSuccessful()) {
                    List<ModeloDTO> modeloDtoList = response.body();
                    modelos.clear();  // Limpiar la lista de modelos anteriores

                    // Convertir los DTOs en objetos de Modelo y agregarlos a la lista
                    for (ModeloDTO modeloDTO : modeloDtoList) {
                        Modelo newModelo = modeloDTO.toModelo();
                        modelos.add(newModelo);  // Agregar el modelo a la lista
                    }

                    // Actualizar el adaptador con los nombres de los modelos
                    ArrayAdapter<String> modelosAdapter = new ArrayAdapter<>(CrearProductoActivity.this, android.R.layout.simple_dropdown_item_1line, getNombresModelos());
                    actvModelos.setAdapter(modelosAdapter);
                    actvModelos.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                    // Mostrar el dropdown con las coincidencias
                    actvModelos.showDropDown();

                    // Agregar modelos seleccionados a la lista visual debajo del campo
                    actvModelos.setOnItemClickListener((parent, view, position, id) -> {
                        String modeloSeleccionado = (String) parent.getItemAtPosition(position);
                        agregarModeloALista(modeloSeleccionado);
                        actvModelos.setText("");  // Limpiar el campo después de seleccionar
                    });
                } else {
                    Toast.makeText(CrearProductoActivity.this, "Error al OBTENER modelos", Toast.LENGTH_SHORT).show();
                    Log.e("ErrorModelo", "Código de error: " + response.code() + " - Mensaje: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ModeloDTO>> call, Throwable t) {
                Toast.makeText(CrearProductoActivity.this, "Error de CONEXION al obtener modelos", Toast.LENGTH_SHORT).show();
                Log.e("ErrorModelo", "Error de conexión: " + t.getMessage(), t);

                if (t instanceof IOException) {
                    Log.e("ErrorModelo", "Problema de red: " + t.getMessage());
                } else {
                    Log.e("ErrorModelo", "Error inesperado: " + t.getMessage());
                }
            }
        });
    }

    private void setupMultiAutoCompleteTextView() {
        actvModelos.setOnItemClickListener((parent, view, position, id) -> {
            String modeloSeleccionado = (String) parent.getItemAtPosition(position);
            agregarModeloALista(modeloSeleccionado);
            actvModelos.setText("");
        });

        actvModelos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 2) {
                    fetchProductModelos(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void setupStockButtons() {
        btnIncrementStockActual.setOnClickListener(v -> updateStock(etStockActual, 1));
        btnDecrementStockActual.setOnClickListener(v -> updateStock(etStockActual, -1));

        btnIncrementStockMax.setOnClickListener(v -> updateStock(etStockMax, 1));
        btnDecrementStockMax.setOnClickListener(v -> updateStock(etStockMax, -1));

        btnIncrementStockMin.setOnClickListener(v -> updateStock(etStockMin, 1));
        btnDecrementStockMin.setOnClickListener(v -> updateStock(etStockMin, -1));
    }

    private void updateStock(EditText editText, int amount) {
        try {
            int stock = Integer.parseInt(editText.getText().toString());
            stock = Math.max(0, stock + amount); // Evita números negativos
            editText.setText(String.valueOf(stock));
        } catch (NumberFormatException e) {
            editText.setText("0");
        }
    }


    private List<String> getNombresTipos() {
        List<String> nombres = new ArrayList<>();
        for (Tipo tipo : tipos) {
            nombres.add(tipo.getNombre());
        }
        return nombres;
    }

    private List<String> getNombresProveedores() {
        List<String> nombres = new ArrayList<>();
        for (Proveedor proveedor : proveedores) {
            nombres.add(proveedor.getNombre());
        }
        return nombres;
    }

    private List<String> getNombresModelos() {
        List<String> nombres = new ArrayList<>();
        for (Modelo modelo : modelos) {
            nombres.add(modelo.getNombre());
        }
        return nombres;
    }

    private void setupSpinners() {
        // Configurar Spinner de Proveedores
        spinnerProveedores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Convertir el item seleccionado a String
                String proveedorSeleccionado = (String) parent.getItemAtPosition(position);
                agregarProveedorALista(proveedorSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Configurar Spinner de Tipos (similar al de proveedores)
        spinnerTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoSeleccionado = (String) parent.getItemAtPosition(position);
                agregarTipoALista(tipoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void agregarProveedorALista(String proveedorSeleccionado) {
        // Buscar el proveedor correspondiente en la lista de objetos `Proveedor`
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getNombre().equals(proveedorSeleccionado)) {
                // Verificar si ya está en la lista de proveedores seleccionados
                if (!selectedProductProveedores.contains(proveedor)) {
                    selectedProductProveedores.add(proveedor);

                    // Crear un LinearLayout horizontal para la x
                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.HORIZONTAL);

                    // Crear una vista de texto para mostrar el proveedor seleccionado
                    TextView textView = new TextView(this);
                    textView.setText(proveedor.getNombre());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                    // Crear el botón "X" para eliminar el proveedor
                    Button removeButton = new Button(this);
                    removeButton.setText("x");
                    // Esto es para el tamano del botón "X"
                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                            110, 100);
                    removeButton.setLayoutParams(buttonParams);

                    //removeButton.setTextSize(30);


                    removeButton.setOnClickListener(v -> {
                        selectedProductProveedores.remove(proveedor);
                        listaProveedores.removeView(layout); // Eliminar el LinearLayout de la lista visual
                    });

                    // Añadir el TextView y el botón al LinearLayout
                    layout.addView(textView);
                    layout.addView(removeButton);

                    // Añadir el LinearLayout a la vista de listaProveedores
                    listaProveedores.addView(layout);
                }
                break;
            }
        }
    }



    private void agregarTipoALista(String tipoSeleccionado) {
        // Buscar el tipo correspondiente en la lista de objetos `Tipo`
        for (Tipo tipo : tipos) {
            if (tipo.getNombre().equals(tipoSeleccionado)) {
                // Verificar si ya está en la lista de tipos seleccionados
                if (!selectedProductTipos.contains(tipo)) {
                    selectedProductTipos.add(tipo);

                    // Crear una vista de texto para mostrar el tipo seleccionado
                   /* TextView textView = new TextView(this);
                    textView.setText(tipo.getNombre());
                    listaTipos.addView(textView); // Agregar a la vista de tipos seleccionados*/
                }
                break;
            }
        }
    }

    private void agregarModeloALista(String modeloSeleccionado) {
        for (Modelo modelo : modelos) {
            if (modelo.getNombre().equals(modeloSeleccionado)) {
                // Verificar si el modelo ya está en la lista de modelos seleccionados
                if (!selectedProductModelos.contains(modelo)) {
                    selectedProductModelos.add(modelo);  // Agregar el modelo seleccionado a la lista

                    // Crear un LinearLayout horizontal para el modelo y el botón "X"
                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.HORIZONTAL);

                    // Crear una nueva vista de texto para mostrar el modelo seleccionado
                    TextView textView = new TextView(this);
                    textView.setText(modelo.getNombre());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));  // Ocupar espacio restante

                    // Crear el botón "X" para eliminar el modelo
                    Button removeButton = new Button(this);
                    removeButton.setText("x");

                    // Establecer los parámetros de diseño para el botón "X"
                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                            110, 100);  // Ancho y alto del botón
                    removeButton.setLayoutParams(buttonParams);

                    // Acción para eliminar el modelo cuando se presiona "X"
                    removeButton.setOnClickListener(v -> {
                        selectedProductModelos.remove(modelo);
                        listaModelos.removeView(layout); // Eliminar el LinearLayout de la lista visual
                    });

                    // Añadir el TextView y el botón al LinearLayout
                    layout.addView(textView);
                    layout.addView(removeButton);

                    // Añadir el LinearLayout a la vista de listaModelos
                    listaModelos.addView(layout);
                }
                break;
            }
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




}














