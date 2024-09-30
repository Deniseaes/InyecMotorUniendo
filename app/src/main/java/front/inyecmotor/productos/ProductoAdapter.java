
package front.inyecmotor.productos;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import front.inyecmotor.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//ProductoAdapter: Controla la lista de productos y gestiona la interacción del usuario con los datos de los productos.
//ProductoViewHolder: Define las vistas específicas para cada producto (nombre, precio, stock y botón de detalles).
//mostrarDialogoDetalle: Muestra un diálogo con los detalles del producto para su edición.
//enviarDatosProducto: Envia los cambios al servidor mediante una API REST.
public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private Context context;
    private static final String BASE_URL = "http://192.168.56.1:8080"; // Cambia a la URL de tu servidor CON EL PUERTO
    private static final String TAG = "ProductoAdapter"; // Tag para los logs

    public ProductoAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productos_item, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.tvProductoNombre.setText(producto.getNombre());
        holder.tvProductoPrecio.setText("Precio: $" + producto.getPrecioVenta());
        holder.tvProductoStock.setText("Stock: " + producto.getStockActual());

        holder.btnVerDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoDetalle(producto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductoNombre;
        TextView tvProductoPrecio;

        TextView tvProductoStock;
        Button btnVerDetalle;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductoNombre = itemView.findViewById(R.id.tvProductoNombre);
            tvProductoPrecio = itemView.findViewById(R.id.tvProductoPrecio);
            tvProductoStock = itemView.findViewById(R.id.tvProductoStock);
            btnVerDetalle = itemView.findViewById(R.id.btnVerDetalle);
        }
    }

    private void mostrarDialogoDetalle(Producto producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles del Producto");

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.productos_detalle, null);
        builder.setView(viewInflated);

        EditText etNombre = viewInflated.findViewById(R.id.etNombre);
        EditText etCodigo = viewInflated.findViewById(R.id.etCodigo);
        EditText etPrecioCosto = viewInflated.findViewById(R.id.etPrecioCosto);
        EditText etPrecioVenta = viewInflated.findViewById(R.id.etPrecioVenta);
        EditText etStockActual = viewInflated.findViewById(R.id.etStockActual);
        EditText etStockMax = viewInflated.findViewById(R.id.etStockMax);
        EditText etStockMin = viewInflated.findViewById(R.id.etStockMin);


        // Botones de incrementar y decrementar para StockActual
        Button btnIncrementarStockActual = viewInflated.findViewById(R.id.btnIncrementarStockActual);
        Button btnDecrementarStockActual = viewInflated.findViewById(R.id.btnDecrementarStockActual);

        // Botones de incrementar y decrementar para StockMax
        Button btnIncrementarStockMax = viewInflated.findViewById(R.id.btnIncrementarStockMax);
        Button btnDecrementarStockMax = viewInflated.findViewById(R.id.btnDecrementarStockMax);

        // Botones de incrementar y decrementar para StockMin
        Button btnIncrementarStockMin = viewInflated.findViewById(R.id.btnIncrementarStockMin);
        Button btnDecrementarStockMin = viewInflated.findViewById(R.id.btnDecrementarStockMin);

        // Setear valores iniciales del producto
        etNombre.setText(producto.getNombre());
        etCodigo.setText(producto.getCodigo());
        etPrecioCosto.setText(String.valueOf(producto.getPrecioCosto()));
        etPrecioVenta.setText(String.valueOf(producto.getPrecioVenta()));
        etStockActual.setText(String.valueOf(producto.getStockActual()));
        etStockMax.setText(String.valueOf(producto.getStockMax()));
        etStockMin.setText(String.valueOf(producto.getStockMin()));

        // Lógica para incrementar/decrementar StockActual
        btnIncrementarStockActual.setOnClickListener(v -> {
            int stockActual = Integer.parseInt(etStockActual.getText().toString());
            etStockActual.setText(String.valueOf(++stockActual));
        });

        btnDecrementarStockActual.setOnClickListener(v -> {
            int stockActual = Integer.parseInt(etStockActual.getText().toString());
            if (stockActual > 0) etStockActual.setText(String.valueOf(--stockActual));
        });

        // Lógica para incrementar/decrementar StockMax
        btnIncrementarStockMax.setOnClickListener(v -> {
            int stockMax = Integer.parseInt(etStockMax.getText().toString());
            etStockMax.setText(String.valueOf(++stockMax));
        });

        btnDecrementarStockMax.setOnClickListener(v -> {
            int stockMax = Integer.parseInt(etStockMax.getText().toString());
            if (stockMax > 0) etStockMax.setText(String.valueOf(--stockMax));
        });

        // Lógica para incrementar/decrementar StockMin
        btnIncrementarStockMin.setOnClickListener(v -> {
            int stockMin = Integer.parseInt(etStockMin.getText().toString());
            etStockMin.setText(String.valueOf(++stockMin));
        });

        btnDecrementarStockMin.setOnClickListener(v -> {
            int stockMin = Integer.parseInt(etStockMin.getText().toString());
            if (stockMin > 0) etStockMin.setText(String.valueOf(--stockMin));
        });

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            // Actualizar el objeto producto con los valores del EditText
            producto.setNombre(etNombre.getText().toString());
            producto.setCodigo(etCodigo.getText().toString());
            producto.setPrecioCosto(Double.parseDouble(etPrecioCosto.getText().toString()));
            producto.setPrecioVenta(Double.parseDouble(etPrecioVenta.getText().toString()));
            producto.setStockActual(Integer.parseInt(etStockActual.getText().toString()));
            producto.setStockMax(Integer.parseInt(etStockMax.getText().toString()));
            producto.setStockMin(Integer.parseInt(etStockMin.getText().toString()));

            // Enviar los datos editados al servidor
            enviarDatosProducto(producto);

            // Notificar al adaptador que los datos han cambiado
            notifyDataSetChanged();
        });

        Button btnEliminarProducto = viewInflated.findViewById(R.id.btnEliminarProducto);
        // Declara el AlertDialog
        AlertDialog dialog = builder.create(); // Crea el diálogo

        btnEliminarProducto.setOnClickListener(v -> {
            // Mostrar un diálogo de confirmación
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar Producto")
                    .setMessage("¿Estás seguro de que deseas eliminar este producto?")
                    .setPositiveButton("Sí", (dialogInterface, i) -> {
                        eliminarProducto(producto.getId());
                        dialog.dismiss(); // Cierra el diálogo de detalles
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, which) -> dialogInterface.cancel());

        dialog.show(); // Muestra el diálogo y asigna el AlertDialog
    }

    private void enviarDatosProducto(Producto producto) {
        // Obtener el token desde SharedPreferences
        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(context);
        if (hashedPassword == null) {
            Toast.makeText(context, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        String token = "Bearer " + hashedPassword;
        Call<Producto> call = apiService.editarProducto(token, producto);

        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Response Code: " + response.code());
                    Log.d(TAG, "Response Message: " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.d(TAG, "Error Body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void eliminarProducto(int productoId) {
        // Obtener el token desde SharedPreferences
        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(context);
        if (hashedPassword == null) {
            Toast.makeText(context, "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        String token = "Bearer " + hashedPassword;
        Call<Void> call = apiService.eliminarProducto(token, productoId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                    productos.removeIf(producto -> producto.getId()==productoId);
                    notifyDataSetChanged(); // Actualizar la lista
                } else {
                    Toast.makeText(context, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Response Code: " + response.code());
                    Log.d(TAG, "Response Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


}
