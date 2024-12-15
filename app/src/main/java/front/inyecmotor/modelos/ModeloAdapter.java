package front.inyecmotor.modelos;
import front.inyecmotor.BuildConfig;


import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


public class ModeloAdapter extends RecyclerView.Adapter<ModeloAdapter.ModeloViewHolder> {
    private List<Modelo> modelos;
    private Context context;
    private static final String BASE_URL = BuildConfig.BASE_URL;
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
        holder.tvModeloMotorTipo.setText("Tipo de Motor: " + modelo.getMotorTipo());
        holder.btnVerDetalle.setOnClickListener(v -> mostrarDialogoDetalle(modelo));
        System.out.println(modelo.toFullString());
    }

    @Override
    public int getItemCount() {
        return modelos.size();
    }

    public class ModeloViewHolder extends RecyclerView.ViewHolder {
        TextView tvModeloNombre;
        TextView tvModeloAnio;
        TextView tvModeloMotor;
        TextView tvModeloMotorTipo;
        Button btnVerDetalle;

        public ModeloViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModeloNombre = itemView.findViewById(R.id.tvModeloNombre);
            tvModeloAnio = itemView.findViewById(R.id.tvModeloAnio);
            tvModeloMotor = itemView.findViewById(R.id.tvModeloMotor);
            tvModeloMotorTipo = itemView.findViewById(R.id.tvModeloMotorTipo);
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

        Spinner spinnerMotorLitros = viewInflated.findViewById(R.id.spinnerMotorLitros);
        Spinner spinnerMotorTipo = viewInflated.findViewById(R.id.spinnerMotorTipo);

        // Setear valores iniciales del modelo
        etNombre.setText(modelo.getNombre());
        etAnio.setText(String.valueOf(modelo.getAnio()));

        // Configurar adaptadores para los spinners

        ArrayAdapter<CharSequence> motorLitrosAdapter = ArrayAdapter.createFromResource(context,
                R.array.motor_litros_array, android.R.layout.simple_spinner_item);
        motorLitrosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotorLitros.setAdapter(motorLitrosAdapter);

        ArrayAdapter<CharSequence> motorTipoAdapter = ArrayAdapter.createFromResource(context,
                R.array.motor_tipo_array, android.R.layout.simple_spinner_item);
        motorTipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotorTipo.setAdapter(motorTipoAdapter);

        System.out.println(modelo.getMotorTipo());
        // Seleccionar los valores actuales del modelo en los spinners
        setSpinnerValue(spinnerMotorLitros, String.valueOf(modelo.getMotorLitros()));
        setSpinnerValue(spinnerMotorTipo, modelo.getMotorTipo());

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            try {
                // Validar y convertir el año a int
                String anioString = etAnio.getText().toString();
                int anio = Integer.parseInt(anioString);

                // Validar y convertir el motor litros a double
                String motorLitrosString = spinnerMotorLitros.getSelectedItem().toString();
                double motorLitros = Double.parseDouble(motorLitrosString);


                // Actualizar el objeto modelo con los valores seleccionados
                modelo.setNombre(etNombre.getText().toString());
                modelo.setAnio(anio);
                modelo.setMotorLitros(motorLitros);
                modelo.setMotorTipo(spinnerMotorTipo.getSelectedItem().toString());

                // Enviar los datos editados al servidor
                enviarDatosModelo(modelo);

                // Notificar al adaptador que los datos han cambiado
                notifyDataSetChanged();
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Por favor, ingrese valores válidos", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error al convertir datos: ", e);
            }
        });

        // Botón de cancelar
        builder.setNegativeButton("Cancelar", (dialogInterface, which) -> {
            dialogInterface.cancel();
        });

        // Crear y mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnEliminarModelo = viewInflated.findViewById(R.id.btnEliminarModelo);
        btnEliminarModelo.setOnClickListener(v -> {
            // Mostrar un diálogo de confirmación
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar Modelo")
                    .setMessage("¿Estás seguro de que deseas eliminar este modelo?")
                    .setPositiveButton("Sí", (dialogInterface, i) -> {
                        // Llamada al método eliminarModelo con el id del modelo
                        eliminarModelo(modelo.getId());
                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

    }

    private void enviarDatosModelo(Modelo modelo) {

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
        Call<Modelo> call = apiService.editarModelo(token, modelo);

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

    private void eliminarModelo(int modeloId) {
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
        Call<Void> call = apiService.eliminarModelo(token, modeloId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Modelo eliminado correctamente", Toast.LENGTH_SHORT).show();
                    modelos.removeIf(modelo -> modelo.getId() == modeloId); // Eliminar el modelo de la lista
                    notifyDataSetChanged(); // Actualizar la lista en el RecyclerView
                } else {
                    Toast.makeText(context, "Error al eliminar el modelo", Toast.LENGTH_SHORT).show();
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

    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

}

