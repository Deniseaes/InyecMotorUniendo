package front.inyecmotor.ordenes;

import front.inyecmotor.BuildConfig;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.ArrayList;
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

public class OrdenAdapter extends RecyclerView.Adapter<OrdenAdapter.OrdenViewHolder> {
    private List<DTOOrdenView> ordenes;

    private Context context;
    private static final String BASE_URL = BuildConfig.BASE_URL;
    private static final String TAG = "OrdenAdapter";


    public OrdenAdapter(List<DTOOrdenView> ordenes, Context context) {
        this.ordenes = ordenes;

        this.context = context;
    }

    @NonNull
    @Override
    public OrdenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orden, parent, false);
        return new OrdenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdenViewHolder holder, int position) {
        DTOOrdenView orden = ordenes.get(position);
        holder.tvPatente.setText("Patente: " + orden.getPatente());
        holder.tvModelo.setText("Vehículo: " + orden.getModelo());
        holder.tvNombreCompleto.setText("Nombre Cliente: " + orden.getNombreCompleto());
        holder.tvDescripcionOtros.setText("Descripcion: " + orden.getDescripcionOtros());
        holder.tvPrecioTotal.setText("Total: $" + orden.getTotal());

        holder.btnEliminarOrden.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar Orden")
                    .setMessage("¿Estás seguro de que deseas eliminar esta orden?")
                    .setPositiveButton("Sí", (dialogInterface, i) -> {
                        eliminarOrden(orden.getId(), position); // Llamar al método eliminar
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });


    }
    private void eliminarOrden(Long ordenId, int position) {
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

        Call<Void> call = apiService.eliminarOrden(token, ordenId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Orden eliminada correctamente", Toast.LENGTH_SHORT).show();
                    ordenes.remove(position); // Eliminar la orden de la lista
                    notifyItemRemoved(position); // Actualizar RecyclerView
                } else {
                    Toast.makeText(context, "Error al eliminar orden", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return ordenes.size();
    }
    public void setOrdenes(List<DTOOrdenView> ordenes) {
        this.ordenes = ordenes; // Actualiza la lista de órdenes
        notifyDataSetChanged(); // Notifica al adaptador que la lista cambió
    }


    public class OrdenViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatente;
        TextView tvModelo;
        TextView tvNombreCompleto;
        TextView tvDescripcionOtros;
        TextView tvPrecioTotal;

        Button btnEliminarOrden;

        public OrdenViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatente = itemView.findViewById(R.id.tvPatente);
            tvModelo = itemView.findViewById(R.id.tvModelo);
            tvNombreCompleto = itemView.findViewById(R.id.tvNombreCompleto);
            tvDescripcionOtros = itemView.findViewById(R.id.tvDescripcionOtros);
            tvPrecioTotal = itemView.findViewById(R.id.tvPrecioTotal);
            btnEliminarOrden = itemView.findViewById(R.id.btnEliminarOrden);

        }
    }


}
