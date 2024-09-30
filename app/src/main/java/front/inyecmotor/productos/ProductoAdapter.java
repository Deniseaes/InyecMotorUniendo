package front.inyecmotor.productos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> productos;
    private Context context;
    private static final String BASE_URL = "http://192.168.0.8:8080"; // Cambia esto según tu configuración
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
        holder.tvProductoPrecio.setText("Precio: $" + producto.getPrecioCosto());
        holder.tvProductoStock.setText("Stock: " + producto.getStockActual());

        // Set stock indicator
        ImageView stockIndicator = holder.itemView.findViewById(R.id.ivStockStatus);
        if (producto.getStockActual() <= producto.getStockMin()) {
            stockIndicator.setImageResource(R.drawable.ic_red_circle);
        } else {
            stockIndicator.setImageResource(R.drawable.ic_green_circle);
        }

        holder.btnVerDetalle.setOnClickListener(v -> mostrarDialogoDetalle(producto));
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

        // Setear valores iniciales del producto
        etNombre.setText(producto.getNombre());
        etCodigo.setText(producto.getCodigo());
        etPrecioCosto.setText(String.valueOf(producto.getPrecioCosto()));
        etPrecioVenta.setText(String.valueOf(producto.getPrecioVenta()));
        etStockActual.setText(String.valueOf(producto.getStockActual()));
        etStockMax.setText(String.valueOf(producto.getStockMax()));
        etStockMin.setText(String.valueOf(producto.getStockMin()));

        Button btnDecrementStock = viewInflated.findViewById(R.id.btnDecrementStock);
        Button btnIncrementStock = viewInflated.findViewById(R.id.btnIncrementStock);
        Button btnSaveChanges = viewInflated.findViewById(R.id.btnSaveChanges);

        AlertDialog dialog = builder.create();

        btnDecrementStock.setOnClickListener(v -> {
            int currentStock = Integer.parseInt(etStockActual.getText().toString());
            if (currentStock > 0) {
                etStockActual.setText(String.valueOf(currentStock - 1));
            }
        });

        btnIncrementStock.setOnClickListener(v -> {
            int currentStock = Integer.parseInt(etStockActual.getText().toString());
            etStockActual.setText(String.valueOf(currentStock + 1));
        });

        btnSaveChanges.setOnClickListener(v -> {
            // Update the product object and send to server
            producto.setNombre(etNombre.getText().toString());
            producto.setCodigo(etCodigo.getText().toString());
            producto.setPrecioCosto(Double.parseDouble(etPrecioCosto.getText().toString()));
            producto.setPrecioVenta(Double.parseDouble(etPrecioVenta.getText().toString()));
            producto.setStockActual(Integer.parseInt(etStockActual.getText().toString()));
            producto.setStockMax(Integer.parseInt(etStockMax.getText().toString()));
            producto.setStockMin(Integer.parseInt(etStockMin.getText().toString()));

            enviarDatosProducto(producto);
            notifyDataSetChanged();
            dialog.dismiss();
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", (dialogInterface, which) -> dialog.dismiss());

        dialog.show();
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
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


}