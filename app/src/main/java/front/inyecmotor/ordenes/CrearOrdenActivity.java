package front.inyecmotor.ordenes;

import front.inyecmotor.BuildConfig;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.text.TextWatcher;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import front.inyecmotor.crearProducto.CrearProductoActivity;
import front.inyecmotor.crearProducto.ModeloDTO;
import front.inyecmotor.crearProducto.ProductoCreate;
import front.inyecmotor.crearProducto.ProveedorDTO;
import front.inyecmotor.crearProducto.Tipo;
import front.inyecmotor.crearProducto.TipoDTO;
import front.inyecmotor.login.LoginActivity;
import front.inyecmotor.modelos.Modelo;
import front.inyecmotor.productos.Producto;
import front.inyecmotor.proveedores.Proveedor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import okhttp3.ResponseBody;

public class CrearOrdenActivity extends AppCompatActivity {
    private EditText etnombreCompleto, etPatente, etDescripcionOtros, etManoDeObra, etOtros;
    private Button btnCrearOrden;
    private TextView tvTotal;
    private AutoCompleteTextView actvModelo;
    private Spinner  spinnerProductos;
    private LinearLayout listaProductos ;

    // Listas de datos
    private List<Producto> productos;
    private List<Modelo> modelos;

    private List<Producto> selectedOrderProductos = new ArrayList<>();
    private List<Modelo> selectedOrderModelo = new ArrayList<>();
    private List<Producto> selectedProducts;
    private Modelo selectedModel;

    private static final String BASE_URL = BuildConfig.BASE_URL; // Cambia esto según tu configuración
    private ApiService apiService;

    private double precioTotal;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_orden);

        // Inicialización de elementos de la interfaz
        etnombreCompleto = findViewById(R.id.etnombreCompleto);
        etPatente= findViewById(R.id.etPatente);
        etDescripcionOtros = findViewById(R.id.etDescripcionOtros);
        etOtros = findViewById(R.id.etOtros);
        etManoDeObra = findViewById(R.id.etManoDeObra);
        tvTotal = findViewById(R.id.tvTotal);



        // Botones de crear
        btnCrearOrden = findViewById(R.id.btnCrearOrden);



        // MultiAutoCompleteTextView y Spinner
        spinnerProductos = findViewById(R.id.spinnerProductos);
        actvModelo = findViewById(R.id.actvModelo);

        // Configurar adaptador para el AutoCompleteTextView
        if (modelos != null && !modelos.isEmpty()) {
            ArrayAdapter<String> modelosAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    getNombresModelos()
            );
            actvModelo.setAdapter(modelosAdapter);
        }

        listaProductos = findViewById(R.id.listaProductos);
        //listaTipos = findViewById(R.id.listaTipos);
        // Listas para las selecciones
        selectedOrderProductos = new ArrayList<>();
        selectedProducts = new ArrayList<>();
        productos = new LinkedList<>();
        modelos = new LinkedList<>();
        selectedOrderModelo = new ArrayList<>();

        setupMultiSelectSpinner();
        setupRealTimeCalculations();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);


        btnCrearOrden.setOnClickListener(v -> crearOrden());
        // Configura el botón de "Volver" en la Toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Configuración del Spinner
        spinnerProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean isInitialSelection = true; // Bandera para ignorar la primera selección automática

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInitialSelection) {
                    isInitialSelection = false; // Ignorar la primera selección automática
                    return;
                }

                if (position >= 0 && position < productos.size()) {
                    Producto productoSeleccionado = productos.get(position);
                    agregarProductoALista(productoSeleccionado);

                    // (Opcional) Si quieres permitir seleccionar el mismo producto nuevamente:
                    spinnerProductos.setSelection(-1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        // Configurar el TextWatcher para actvModelo
        actvModelo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    fetchOrderModels(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        actvModelo.setOnItemClickListener((parent, view, position, id) -> {
            String modeloSeleccionado = (String) parent.getItemAtPosition(position);
            selectedModel = obtenerModeloPorNombre(modeloSeleccionado);

            if (selectedModel != null) {
                Toast.makeText(CrearOrdenActivity.this, "Modelo seleccionado: " + modeloSeleccionado, Toast.LENGTH_SHORT).show();
                // Carga productos asociados
                long idModel =selectedModel.getId();
                fetchOrderProductsByModel(idModel);
                actvModelo.dismissDropDown();
            } else {
                Toast.makeText(CrearOrdenActivity.this, "Por favor selecciona un modelo válido", Toast.LENGTH_SHORT).show();
            }
        });



        // Configura el botón de "Volver" en la Toolbar


    }


    private void crearOrden() {
        String nombreCompleto = etnombreCompleto.getText().toString();
        String patente = etPatente.getText().toString();
        String descripcionOtros = etDescripcionOtros.getText().toString();

        if (nombreCompleto.isEmpty() || patente.isEmpty() || etManoDeObra.getText().toString().isEmpty() ||
                etOtros.getText().toString().isEmpty() || descripcionOtros.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double manoDeObra = Double.parseDouble(etManoDeObra.getText().toString());
        double otros = Double.parseDouble(etOtros.getText().toString());


        ArrayList<Long> productos = new ArrayList<>();
        for (Producto producto : selectedProducts) {
            productos.add(producto.getId());
        }
        if (productos.isEmpty()) {
            Toast.makeText(this, "Debes seleccionar al menos un producto", Toast.LENGTH_SHORT).show();
            return;
        }


        System.out.println("Productos: " + productos);

        int modelIdInt = (int) selectedModel.getId();
        Long id = Long.valueOf(99999);
        OrdenCreate nuevaOrden = new OrdenCreate(
                id,
                nombreCompleto,
                patente,
                modelIdInt,
                manoDeObra,
                otros,
                descripcionOtros,
                productos

        );
        System.out.println(nuevaOrden);
        System.err.println("Error al procesar la orden: " + nuevaOrden);

        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(this);
        if (hashedPassword == null) {
            Toast.makeText(this, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "Bearer " + hashedPassword;

        Call<OrdenCreate> call = apiService.crearOrden(token, nuevaOrden);
        call.enqueue(new Callback<OrdenCreate>() {
            @Override
            public void onResponse(Call<OrdenCreate> call, Response<OrdenCreate> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearOrdenActivity.this, "Orden creada exitosamente", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(CrearOrdenActivity.this, "Error al crear la orden", Toast.LENGTH_LONG).show();
                    Log.e("ErrorOrden", "Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<OrdenCreate> call, Throwable t) {
                Toast.makeText(CrearOrdenActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Métodos para obtener datos de modelos y productos relacionados

    private void fetchOrderModels(String query) {
        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(CrearOrdenActivity.this);
        if (hashedPassword == null) {
            Toast.makeText(CrearOrdenActivity.this, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
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
                    ArrayAdapter<String> modelosAdapter = new ArrayAdapter<>(CrearOrdenActivity.this, android.R.layout.simple_dropdown_item_1line, getNombresModelos());
                    actvModelo.setAdapter(modelosAdapter);

                    // Mostrar el dropdown con las coincidencias
                    actvModelo.showDropDown();

                    // Agregar modelos seleccionados a la lista visual debajo del campo
                    actvModelo.setOnItemClickListener((parent, view, position, id) -> {
                        String modeloSeleccionado = (String) parent.getItemAtPosition(position);
                        selectedModel = obtenerModeloPorNombre(modeloSeleccionado);

                        // Confirmar la selección en la interfaz
                        if (selectedModel != null) {
                            actvModelo.setText(modeloSeleccionado); // Actualizar visualmente el campo con el modelo seleccionado
                            actvModelo.dismissDropDown(); // Ocultar el desplegable


                            // Realizar cualquier acción necesaria con el modelo seleccionado
                            long idModel =selectedModel.getId();
                            fetchOrderProductsByModel(idModel);


                        } else {
                            Toast.makeText(CrearOrdenActivity.this, "Por favor selecciona un modelo", Toast.LENGTH_SHORT).show();
                        }
                        actvModelo.dismissDropDown();
                        actvModelo.setText(modeloSeleccionado);
                        actvModelo.dismissDropDown();
                       // Toast.makeText(CrearOrdenActivity.this, modeloSeleccionado, Toast.LENGTH_SHORT).show();

                    });
                } else {
                    Toast.makeText(CrearOrdenActivity.this, "Error al OBTENER modelos orden", Toast.LENGTH_SHORT).show();
                    Log.e("ErrorModelo", "Código de error: " + response.code() + " - Mensaje: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<List<ModeloDTO>> call, Throwable t) {
                Toast.makeText(CrearOrdenActivity.this, "Error de CONEXION al obtener modelos orden", Toast.LENGTH_SHORT).show();
                Log.e("ErrorModelo", "Error de conexión: " + t.getMessage(), t);

                if (t instanceof IOException) {
                    Log.e("ErrorModelo", "Problema de red: " + t.getMessage());
                } else {
                    Log.e("ErrorModelo", "Error inesperado: " + t.getMessage());
                }
            }
        });
    }


    private void fetchOrderProductsByModel(Long idModelo) {
        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(this);
        if (hashedPassword == null) {
            Toast.makeText(this, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "Bearer " + hashedPassword;

        Call<ResponseBody> call = apiService.getProductosByModelo(token, idModelo.intValue());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Producto>>() {}.getType();
                        List<Producto> productosList = gson.fromJson(jsonString, listType);

                        productos.clear();
                        productos.addAll(productosList);

                        // Configurar adaptador para selección múltiple
                        ArrayAdapter<String> productosAdapter = new ArrayAdapter<>(
                                CrearOrdenActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                getNombresProductos(productos)
                        );
                        spinnerProductos.setAdapter(productosAdapter);

                        // Configurar el listener del Spinner
                        /* .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            private boolean isInitialSelection = true; // Bandera para manejar la selección inicial

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (isInitialSelection) {
                                    isInitialSelection = false; // Ignorar la primera llamada
                                    return;
                                }

                                if (position >= 0 && position < productos.size()) {
                                    Producto productoSeleccionado = productos.get(position);

                                    // Agrega el producto si no está en la lista
                                  //  if (!selectedProducts.contains(productoSeleccionado)) {
                                        agregarProductoALista(productoSeleccionado);
                                    //} else {
                                     //   Toast.makeText(CrearOrdenActivity.this, "El producto ya fue seleccionado.", Toast.LENGTH_SHORT).show();
                                   // }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // No hacer nada
                            }
                        });*/

                    } catch (IOException e) {
                        Toast.makeText(CrearOrdenActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        Log.e("ErrorProductos", "Excepción al procesar JSON", e);
                    }
                } else {
                    Toast.makeText(CrearOrdenActivity.this, "Error al obtener productos", Toast.LENGTH_SHORT).show();
                    Log.e("ErrorProductos", "Código de error: " + response.code() + " - Mensaje: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CrearOrdenActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                Log.e("ErrorProductos", "Excepción: " + t.getMessage(), t);
            }
        });
    }





    //Metodos Auxiliares
    private List<String> getNombresProductos(List<Producto> productos) {
        List<String> nombres = new ArrayList<>();
        for (Producto producto : productos) {
            nombres.add(producto.getNombre());
        }
        return nombres;
    }

    private void calcularPrecioTotal() {
        double manoDeObra = etManoDeObra.getText().toString().isEmpty() ? 0 : Double.parseDouble(etManoDeObra.getText().toString());
        double otros = etOtros.getText().toString().isEmpty() ? 0 : Double.parseDouble(etOtros.getText().toString());
        double totalProductos = 0;

        for (Producto producto : selectedProducts) {
            totalProductos += producto.getPrecioVenta(); // Asume que Producto tiene un método getPrecioVenta()
        }

        double total = manoDeObra + otros + totalProductos;
        tvTotal.setText(String.format("Total: $%.2f", total));
        precioTotal = total;
    }



    private Modelo obtenerModeloPorNombre(String nombre) {
        for (Modelo modelo : modelos) {
            if (modelo.getNombre().equals(nombre)) {
                return modelo;
            }
        }
        return null;
    }


    // Obtener nombres de los modelos
    private List<String> getNombresModelos() {
        List<String> nombres = new ArrayList<>();
        if (modelos != null) {
            for (Modelo modelo : modelos) {
                nombres.add(modelo.getNombre());
            }
        }
        return nombres;
    }


    private void setupMultiSelectSpinner() {
        // Validar si la lista de productos está vacía
        if (productos == null || productos.isEmpty()) {
            return; // Salir del método si no hay productos
        }

        // Crear una lista temporal con un encabezado para el adaptador
        List<String> nombresConEncabezado = new ArrayList<>();
        nombresConEncabezado.add("Seleccionar un producto"); // Encabezado
        nombresConEncabezado.addAll(getNombresProductos(productos));

        // Configurar el adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombresConEncabezado
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductos.setAdapter(adapter);

        // Configurar el listener del Spinner
        /*spinnerProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Validar que no sea el encabezado (posición 0)
                if (position == 0) {
                    // No hacer nada, es el encabezado
                    return;
                }

                // Ajustar el índice para obtener el producto correcto
                Producto productoSeleccionado = productos.get(position - 1);

                // Agregar el producto si no está en la lista
                agregarProductoALista(productoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });*/
    }




    private void agregarProductoALista(Producto productoSeleccionado) {

            selectedProducts.add(productoSeleccionado);

            // Crear un contenedor para el producto
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            // Crear un TextView para mostrar el nombre y precio del producto
            TextView textView = new TextView(this);
            String productoInfo = productoSeleccionado.getNombre() + " - $" + productoSeleccionado.getPrecioVenta();
            textView.setText(productoInfo);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

            // Crear un botón "X" para eliminar el producto
            Button removeButton = new Button(this);
            removeButton.setText("x");
            removeButton.setOnClickListener(v -> {
                selectedProducts.remove(productoSeleccionado); // Quitar de la lista seleccionada
                listaProductos.removeView(layout); // Quitar de la vista
                calcularPrecioTotal(); // Actualizar el total al eliminar
            });

            // Añadir los elementos al contenedor
            layout.addView(textView);
            layout.addView(removeButton);

            // Agregar el contenedor a la vista de lista
            listaProductos.addView(layout);

            // Actualizar el total después de agregar
            calcularPrecioTotal();

    }


    private void setupRealTimeCalculations() {
        TextWatcher totalWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcularPrecioTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etManoDeObra.addTextChangedListener(totalWatcher);
        etOtros.addTextChangedListener(totalWatcher);
    }








    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
