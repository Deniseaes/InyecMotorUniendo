package front.inyecmotor.modelos;

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

public class ModeloAdapter extends RecyclerView.Adapter<ModeloAdapter.ModeloViewHolder> {
    private List<Modelo> modelos;
    private Context context;
    private static final String BASE_URL = "http://192.168.0.106:8080"; // Cambia a la URL de tu servidor
    private static final String TAG = "ModeloAdapter"; // Tag para los logs

    public ModeloAdapter(List<Modelo> modelos, Context context) {
        this.modelos = modelos;
        this.context = context;
    }

    @NonNull
    @Override
    public ModeloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelos_item, parent, false);
        return new ModeloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModeloViewHolder holder, int position) {
        Modelo modelo = modelos.get(position);
        holder.tvModeloNombre.setText(modelo.getNombre());
        holder.tvModeloAnio.setText("Año: " + modelo.getAnio());
        holder.tvModeloMotor.setText("Motor: " + modelo.getMotorLitros() + "L");

        holder.btnVerDetalle.setOnClickListener(v -> mostrarDialogoDetalle(modelo));
    }

    @Override
    public int getItemCount() {
        return modelos.size();
    }

    public class ModeloViewHolder extends RecyclerView.ViewHolder {
        TextView tvModeloNombre;
        TextView tvModeloAnio;
        TextView tvModeloMotor;
        Button btnVerDetalle;

        public ModeloViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModeloNombre = itemView.findViewById(R.id.tvModeloNombre);
            tvModeloAnio = itemView.findViewById(R.id.tvModeloAnio);
            tvModeloMotor = itemView.findViewById(R.id.tvModeloMotor);
            btnVerDetalle = itemView.findViewById(R.id.btnVerDetalle);
        }
    }

    private void mostrarDialogoDetalle(Modelo modelo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles del Modelo");

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.modelos_detalle, null);
        builder.setView(viewInflated);

        EditText etNombre = viewInflated.findViewById(R.id.etNombre);
        EditText etAnio = viewInflated.findViewById(R.id.etAnio);
        EditText etMotorLitros = viewInflated.findViewById(R.id.etMotorLitros);
        EditText etMotorTipo = viewInflated.findViewById(R.id.etMotorTipo);
        EditText etMarcaId = viewInflated.findViewById(R.id.etMarcaId);

        // Setear valores iniciales del modelo
        etNombre.setText(modelo.getNombre());
        etAnio.setText(String.valueOf(modelo.getAnio()));
        etMotorLitros.setText(String.valueOf(modelo.getMotorLitros()));
        etMotorTipo.setText(modelo.getMotorTipo());
        etMarcaId.setText(String.valueOf(modelo.getMarcaId()));

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            try {
                // Actualizar el objeto modelo con los valores del EditText
                modelo.setNombre(etNombre.getText().toString());
                modelo.setAnio(Integer.parseInt(etAnio.getText().toString()));
                modelo.setMotorLitros(Double.parseDouble(etMotorLitros.getText().toString()));
                modelo.setMotorTipo(etMotorTipo.getText().toString());
                modelo.setMarcaId(Long.parseLong(etMarcaId.getText().toString()));

                // Enviar los datos editados al servidor
                enviarDatosModelo(modelo);

                // Notificar al adaptador que los datos han cambiado
                notifyDataSetChanged();
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Por favor, ingrese valores válidos", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error al convertir datos: ", e);
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void enviarDatosModelo(Modelo modelo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<Modelo> call = apiService.editarModelo(modelo); // Supón que tienes este endpoint

        call.enqueue(new Callback<Modelo>() {
            @Override
            public void onResponse(@NonNull Call<Modelo> call, @NonNull Response<Modelo> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Modelo actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al actualizar el modelo", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Response Code: " + response.code());
                    Log.d(TAG, "Response Message: " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.d(TAG, "Error Body: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e(TAG, "Error al leer el cuerpo de error: ", e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Modelo> call, @NonNull Throwable t) {
                Toast.makeText(context, "Fallo al actualizar el modelo", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Fallo en la solicitud: ", t);
            }
        });
    }
}
