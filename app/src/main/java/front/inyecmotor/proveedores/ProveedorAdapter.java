package front.inyecmotor.proveedores;

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

public class ProveedorAdapter extends RecyclerView.Adapter<ProveedorAdapter.ProveedorViewHolder> {
    private List<Proveedor> proveedores;
    private Context context;
    private static final String BASE_URL = "http://192.168.0.8:8080"; // Cambia esto según tu configuración
    private static final String TAG = "ProveedorAdapter";

    public ProveedorAdapter(List<Proveedor> proveedores, Context context) {
        this.proveedores = proveedores;
        this.context = context;
    }

    @NonNull
    @Override
    public ProveedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proveedores_item, parent, false);
        return new ProveedorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProveedorViewHolder holder, int position) {
        Proveedor proveedor = proveedores.get(position);
        holder.tvProveedorNombre.setText(proveedor.getNombre());
        holder.tvProveedorDireccion.setText("Dirección: " +proveedor.getDireccion());
        holder.tvProveedorTel.setText("Tel: " +proveedor.getTel());
        holder.tvProveedorEmail.setText("E-Mail: " +proveedor.getEmail());

        holder.btnVerDetalle.setOnClickListener(v -> mostrarDialogoDetalle(proveedor));
    }

    @Override
    public int getItemCount() {
        return proveedores.size();
    }

    public class ProveedorViewHolder extends RecyclerView.ViewHolder {
        TextView tvProveedorNombre;
        TextView tvProveedorDireccion;
        TextView tvProveedorTel;
        TextView tvProveedorEmail;
        Button btnVerDetalle;

        public ProveedorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProveedorNombre = itemView.findViewById(R.id.tvProveedorNombre);
            tvProveedorDireccion = itemView.findViewById(R.id.tvProveedorDireccion);
            tvProveedorTel = itemView.findViewById(R.id.tvProveedorTel);
            tvProveedorEmail = itemView.findViewById(R.id.tvProveedorEmail);
            btnVerDetalle = itemView.findViewById(R.id.btnVerDetalle);
        }
    }

    private void mostrarDialogoDetalle(Proveedor proveedor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles del Proveedor");

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.proveedores_detalle, null);
        builder.setView(viewInflated);

        EditText etNombre = viewInflated.findViewById(R.id.etNombre);
        EditText etDireccion = viewInflated.findViewById(R.id.etDireccion);
        EditText etCuit = viewInflated.findViewById(R.id.etCuit);
        EditText etTel = viewInflated.findViewById(R.id.etTel);
        EditText etEmail = viewInflated.findViewById(R.id.etEmail);

        etNombre.setText(proveedor.getNombre());
        etDireccion.setText(proveedor.getDireccion());
        etCuit.setText(proveedor.getCuit());
        etTel.setText(proveedor.getTel());
        etEmail.setText(proveedor.getEmail());

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            proveedor.setNombre(etNombre.getText().toString());
            proveedor.setDireccion(etDireccion.getText().toString());
            proveedor.setCuit(etCuit.getText().toString());
            proveedor.setTel(etTel.getText().toString());
            proveedor.setEmail(etEmail.getText().toString());

            actualizarProveedor(proveedor);

            notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void actualizarProveedor(Proveedor proveedor) {

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
        Call<Proveedor> call = apiService.editarProveedor(token, proveedor);

        call.enqueue(new Callback<Proveedor>() {
            @Override
            public void onResponse(Call<Proveedor> call, Response<Proveedor> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Proveedor actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Toast.makeText(context, "Error al actualizar: " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Proveedor> call, Throwable t) {
                Toast.makeText(context, "Error en la comunicación con el servidor", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error al actualizar proveedor", t);
            }
        });
    }
}
