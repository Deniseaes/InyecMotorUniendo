package front.inyecmotor.marcas;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarcaAdapter extends RecyclerView.Adapter<MarcaAdapter.MarcaViewHolder> {
    private List<Marca> marcas;
    private Context context;
    private static final String BASE_URL = "http://192.168.1.3:8080"; // Cambia a la URL de tu servidor
    private static final String TAG = "MarcaAdapter"; // Tag para los logs

    public MarcaAdapter(List<Marca> marcas, Context context) {
        this.marcas = marcas;
        this.context = context;
    }

    @NonNull
    @Override
    public MarcaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marcas_item, parent, false);
        return new MarcaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarcaViewHolder holder, int position) {
        Marca marca = marcas.get(position);
        holder.tvMarcaNombre.setText(marca.getNombre());

        holder.btnVerDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoDetalle(marca);
            }
        });
    }

    @Override
    public int getItemCount() {
        return marcas.size();
    }

    public class MarcaViewHolder extends RecyclerView.ViewHolder {
        TextView tvMarcaNombre;
        Button btnVerDetalle;

        public MarcaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMarcaNombre = itemView.findViewById(R.id.tvMarcaNombre);
            btnVerDetalle = itemView.findViewById(R.id.btnVerDetalle);
        }
    }

    private void mostrarDialogoDetalle(Marca marca) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles de la Marca");

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.marcas_detalle, null);
        builder.setView(viewInflated);

        EditText etNombre = viewInflated.findViewById(R.id.etNombre);

        // Setear valores iniciales de la marca
        etNombre.setText(marca.getNombre());

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            // Actualizar el objeto marca con los valores del EditText
            marca.setNombre(etNombre.getText().toString());

            // Enviar los datos editados al servidor
            enviarDatosMarca(marca);

            // Notificar al adaptador que los datos han cambiado
            notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void enviarDatosMarca(Marca marca) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<Marca> call = apiService.editarMarca(marca);

        call.enqueue(new Callback<Marca>() {
            @Override
            public void onResponse(Call<Marca> call, Response<Marca> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Marca actualizada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al actualizar la marca", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<Marca> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
