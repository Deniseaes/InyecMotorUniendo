package front.inyecmotor.crearProducto;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;

import front.inyecmotor.marcas.Marca;

import front.inyecmotor.modelos.Modelo;

import front.inyecmotor.proveedores.Proveedor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearProductoActivity extends AppCompatActivity {
    private EditText etCodigo, etNombre, etPrecioCosto, etPrecioVenta, etStockActual, etStockMax, etStockMin;
    private Button btnCrearProducto,  btnSelectTipo, btnRegresarCrearProduct, btnSelectProveedor, btnSelectMarcas, btnSelectModelos;
    //son los que nos traemos en la llamada al fetch son todos los all
    private List<Tipo> tipos;
    private List<Proveedor>proveedores;

    private List<Marca> marcas;
    private List<Modelo> modelos;
    //son los que se seleccionaron
    private boolean[] selectedTipos;
    private boolean[] selectedProveedores;
    private boolean[] selectedMarcas;
    private boolean[] selectedModelos;
    private List<Tipo> selectedProductTipos;
    private List<Proveedor> selectedProductProveedores;
    private List<Marca> selectedProductMarcas;
    private List<Modelo> selectedProductModelos;
    private static final String BASE_URL = "http://192.168.1.3:8080"; // Cambia a la URL de tu servidor
    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etPrecioCosto = findViewById(R.id.etPrecioCosto);
        etPrecioVenta = findViewById(R.id.etPrecioVenta);
        etStockActual = findViewById(R.id.etStockActual);
        etStockMax = findViewById(R.id.etStockMax);
        etStockMin = findViewById(R.id.etStockMin);
        btnCrearProducto = findViewById(R.id.btnCrearProducto);
        btnSelectTipo = findViewById(R.id.btnSelectTipo);
        btnRegresarCrearProduct = findViewById(R.id.btnRegresarCrearProduct);
        btnSelectProveedor = findViewById(R.id.btnSelectProveedor);
        btnSelectMarcas = findViewById(R.id.btnSelectMarcas);
        btnSelectModelos = findViewById(R.id.btnSelectModelos);


        tipos = new LinkedList<Tipo>();
        selectedTipos = new boolean[tipos.size()];
        selectedProductTipos = new ArrayList<Tipo>();

        proveedores = new LinkedList<Proveedor>();
        selectedProveedores = new boolean[proveedores.size()];
        selectedProductProveedores = new ArrayList<Proveedor>();

        marcas = new LinkedList<Marca>();
        selectedMarcas = new boolean[marcas.size()];
        selectedProductMarcas = new ArrayList<Marca>();

        modelos = new LinkedList<Modelo>();
        selectedModelos = new boolean[modelos.size()];
        selectedProductModelos = new ArrayList<Modelo>();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Obtener tipos de productos al crear la vista
        fetchProductTipos();
        //Obtener los proveedores tambien
        fetchProductProveedores();
        //Obtener los Marcas tambien
        fetchProductMarcas();
        //Obtener los Modelos tambien
        fetchProductModelos();


        btnSelectTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMultiSelectDialogTipos();
            }
        });

        btnSelectProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMultiSelectDialogProveedores();
            }
        });

        btnSelectMarcas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMultiSelectDialogMarcas();
            }
        });

        btnSelectModelos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMultiSelectDialogModelos();
            }
        });

        btnCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearProducto();
            }
        });

        btnRegresarCrearProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atrasClick(v);
            }
        });
    }

    // Método para manejar el clic del botón de regresar
    public void atrasClick(View view) {
        // Esto hará que la actividad actual termine y vuelva a la anterior en la pila
        finish();
    }

    private void crearProducto() {
        String codigo = etCodigo.getText().toString();
        String nombre = etNombre.getText().toString();
        double precioCosto = Double.parseDouble(etPrecioCosto.getText().toString());
        double precioVenta = Double.parseDouble(etPrecioVenta.getText().toString());
        int stockActual = Integer.parseInt(etStockActual.getText().toString());
        int stockMax = Integer.parseInt(etStockMax.getText().toString());
        int stockMin = Integer.parseInt(etStockMin.getText().toString());

        // Aquí puedes ajustar cómo se envían los modelos seleccionados al backend
        ArrayList <Integer> modelosIds = new ArrayList<>();
        for (Modelo modelo : selectedProductModelos) {
            modelosIds.add(modelo.getId());
        }
        // Aquí puedes ajustar cómo se envían los marcas seleccionados al backend
        ArrayList <Integer> marcasIds = new ArrayList<>();
        for (Marca marca : selectedProductMarcas) {
            marcasIds.add(marca.getId());
        }
        // Aquí puedes ajustar cómo se envían los proveedores seleccionados al backend
        ArrayList <Integer> proveedoresIds = new ArrayList<>();
        for (Proveedor proveedor : selectedProductProveedores) {
            proveedoresIds.add(proveedor.getId());
        }
        // Aquí puedes ajustar cómo se envían los tipos seleccionados al backend
        ArrayList<Integer> tipoIds = new ArrayList<>();
        for (Tipo tipo : selectedProductTipos) {
            tipoIds.add(tipo.getId());
        }



        Long id = Long.valueOf(99999);
        ProductoCreate nuevoProducto = new ProductoCreate(id, codigo, nombre,stockMin, stockMax,stockActual , precioVenta, precioCosto,proveedoresIds,tipoIds,marcasIds,modelosIds);


        Call<ProductoCreate> call = apiService.crearProducto(nuevoProducto);
        call.enqueue(new Callback<ProductoCreate>() {
            @Override
            public void onResponse(Call<ProductoCreate> call, Response<ProductoCreate> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearProductoActivity.this, "Producto creado exitosamente", Toast.LENGTH_LONG).show();
                    Log.i("respuesta", response.toString());
                    finish();

                } else {
                    Toast.makeText(CrearProductoActivity.this, "Error al crear el producto", Toast.LENGTH_LONG).show();
                    Log.i("respuesta no", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ProductoCreate> call, Throwable t) {
                Toast.makeText(CrearProductoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                Log.e("Error de la llamada", "Error: " + t);
            }
        });
    }

    private void fetchProductTipos() {
        Call<List<TipoDTO>> call = apiService.getTipos();
        call.enqueue(new Callback<List<TipoDTO>>() {
            @Override
            public void onResponse(Call<List<TipoDTO>> call, Response<List<TipoDTO>> response) {
                if (response.isSuccessful()) {
                    List<TipoDTO> tipoDtoList = response.body();
                    tipoDtoList.forEach(tipoDto -> {
                        Tipo newTipo =tipoDto.toTipo();
                        tipos.add(newTipo);});

                    selectedTipos = new boolean[tipos.size()];
                } else {
                    Toast.makeText(CrearProductoActivity.this, "Failed to fetch product types", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoDTO>> call, Throwable t) {
                Toast.makeText(CrearProductoActivity.this, "Failed to fetch product types", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMultiSelectDialogTipos() {

        String[] tipoNames = new String[tipos.size()];
        for (int i = 0; i < tipos.size(); i++) {
            tipoNames[i] = tipos.get(i).getNombre();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona los tipos de producto")
                .setMultiChoiceItems(tipoNames, selectedTipos, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selectedTipos[which] = isChecked;
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Implementa la lógica después de seleccionar los tipos y hacer clic en Aceptar
                        // Puedes recorrer selectedTipos para obtener los tipos seleccionados
                        selectedProductTipos.clear(); // Limpiar la lista de tipos seleccionados antes de agregar los nuevos
                        for (int i = 0; i < selectedTipos.length; i++) {
                            if (selectedTipos[i]) {
                                selectedProductTipos.add(tipos.get(i));
                            }
                        }
                        // Actualiza la interfaz de usuario si es necesario
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al hacer clic en Cancelar, si es necesario
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void fetchProductProveedores() {
        Call<List<ProveedorDTO>> call = apiService.getProveedoresDTO();
        call.enqueue(new Callback<List<ProveedorDTO>>() {
            @Override
            public void onResponse(Call<List<ProveedorDTO>> call, Response<List<ProveedorDTO>> response) {
                if (response.isSuccessful()) {
                    List<ProveedorDTO> proveedoresDtoList = response.body();
                    proveedoresDtoList.forEach(proveedorDto -> {
                        Proveedor newProveedor =proveedorDto.toProveedor();
                        proveedores.add(newProveedor);});

                    selectedProveedores = new boolean[proveedores.size()];
                } else {
                    Toast.makeText(CrearProductoActivity.this, "Failed to fetch prodcuts proveedores", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProveedorDTO>> call, Throwable t) {
                Toast.makeText(CrearProductoActivity.this, "Failed to fetch prodcuts proveedores", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //proveedores
    private void showMultiSelectDialogProveedores() {

        String[] tipoNames = new String[proveedores.size()];
        for (int i = 0; i < proveedores.size(); i++) {
            tipoNames[i] = proveedores.get(i).getNombre();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona el/los proveedores del producto")
                .setMultiChoiceItems(tipoNames, selectedProveedores, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selectedProveedores[which] = isChecked;
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Implementa la lógica después de seleccionar los tipos y hacer clic en Aceptar
                        // Puedes recorrer selectedProveedores para obtener los tipos seleccionados
                        selectedProductProveedores.clear(); // Limpiar la lista de tipos seleccionados antes de agregar los nuevos
                        for (int i = 0; i < selectedProveedores.length; i++) {
                            if (selectedProveedores[i]) {
                                selectedProductProveedores.add(proveedores.get(i));
                            }
                        }
                        // Actualiza la interfaz de usuario si es necesario
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al hacer clic en Cancelar, si es necesario
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    //Marcas
    private void fetchProductMarcas() {
        Call<List<MarcaDTO>> call = apiService.getMarcasDTO();
        call.enqueue(new Callback<List<MarcaDTO>>() {
            @Override
            public void onResponse(Call<List<MarcaDTO>> call, Response<List<MarcaDTO>> response) {
                if (response.isSuccessful()) {
                    List<MarcaDTO> marcaDtoList = response.body();
                    marcaDtoList.forEach(marcaDto -> {
                        Marca newMarca =marcaDto.toMarca();
                        marcas.add(newMarca);});

                    selectedMarcas = new boolean[marcas.size()];
                } else {
                    Toast.makeText(CrearProductoActivity.this, "Failed to fetch product on response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MarcaDTO>> call, Throwable t) {
                Toast.makeText(CrearProductoActivity.this, "Failed to fetch product marcas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMultiSelectDialogMarcas() {

        String[] marcaNames = new String[marcas.size()];
        for (int i = 0; i < marcas.size(); i++) {
            marcaNames[i] = marcas.get(i).getNombre();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona los tipos de marcas")
                .setMultiChoiceItems(marcaNames, selectedMarcas, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selectedMarcas[which] = isChecked;
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Implementa la lógica después de seleccionar las marcas y hacer clic en Aceptar
                        // Puedes recorrer selectedMarcas para obtener las marcas seleccionados
                        selectedProductMarcas.clear(); // Limpiar la lista de marcas seleccionados antes de agregar los nuevos
                        for (int i = 0; i < selectedMarcas.length; i++) {
                            if (selectedMarcas[i]) {
                                selectedProductMarcas.add(marcas.get(i));
                            }
                        }
                        // Actualiza la interfaz de usuario si es necesario
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al hacer clic en Cancelar, si es necesario
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    //Modelos
    private void fetchProductModelos() {
        Call<List<ModeloDTO>> call = apiService.getModelosDTO();
        call.enqueue(new Callback<List<ModeloDTO>>() {
            @Override
            public void onResponse(Call<List<ModeloDTO>> call, Response<List<ModeloDTO>> response) {
                if (response.isSuccessful()) {
                    List<ModeloDTO> modeloDtoList = response.body();
                    modeloDtoList.forEach(modeloDto -> {
                        Modelo newModelo =modeloDto.toModelo();
                        modelos.add(newModelo);});

                    selectedModelos = new boolean[modelos.size()];
                } else {
                    Toast.makeText(CrearProductoActivity.this, "Failed to fetch product modelos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ModeloDTO>> call, Throwable t) {
                Toast.makeText(CrearProductoActivity.this, "Failed to fetch product modelos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMultiSelectDialogModelos() {

        String[] modeloNames = new String[modelos.size()];
        for (int i = 0; i < modelos.size(); i++) {
            modeloNames[i] = modelos.get(i).getNombre();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona los modelos de autos")
                .setMultiChoiceItems(modeloNames, selectedModelos, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selectedModelos[which] = isChecked;
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Implementa la lógica después de seleccionar los modelos y hacer clic en Aceptar
                        // Puedes recorrer selectedModelos para obtener los modelos seleccionados
                        selectedProductModelos.clear(); // Limpiar la lista de modelos seleccionados antes de agregar los nuevos
                        for (int i = 0; i < selectedModelos.length; i++) {
                            if (selectedModelos[i]) {
                                selectedProductModelos.add(modelos.get(i));
                            }
                        }
                        // Actualiza la interfaz de usuario si es necesario
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al hacer clic en Cancelar, si es necesario
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
