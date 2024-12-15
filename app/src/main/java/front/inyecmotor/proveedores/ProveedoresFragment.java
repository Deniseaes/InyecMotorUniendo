package front.inyecmotor.proveedores;
import front.inyecmotor.BuildConfig;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import front.inyecmotor.crearProducto.CrearProductoActivity;
import front.inyecmotor.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProveedoresFragment extends Fragment {

    private static final String BASE_URL = BuildConfig.BASE_URL; // Cambia esto según tu configuración
    private static final String TAG = "ProveedoresFragment"; // Tag para los logs

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.proveedores_fragment, container, false);

        Button getProveedoresButton = view.findViewById(R.id.getProveedores);
        if (getProveedoresButton != null) {
            getProveedoresButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchProveedores();
                }
            });
        }

        Button btnCrearProveedor = view.findViewById(R.id.btnCrearProveedor);
        btnCrearProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Crear el Intent para iniciar el Activity
                    Intent intent = new Intent(getActivity(), CrearProveedorActivity.class);
                    startActivity(intent);
                }catch (Exception e){Log.e("Error", "Error: " + e.getMessage());}

            }
        });

        return view;
    }

    private void fetchProveedores() {
        // Obtener la contraseña hasheada desde SharedPreferences
        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(getContext());

        if (hashedPassword == null) {
            Toast.makeText(getContext(), "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        String token = "Bearer " + hashedPassword;
        Call<List<Proveedor>> call = apiService.getProveedores(token);

        call.enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {
                Log.d(TAG, "Response Code: " + response.code());
                Log.d(TAG, "Response Message: " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    List<Proveedor> proveedores = response.body();

                    // Abre la actividad de proveedores y pasa la lista de proveedores
                    Intent intent = new Intent(getActivity(), ProveedoresActivity.class);
                    intent.putParcelableArrayListExtra("proveedores", new ArrayList<>(proveedores));
                    startActivity(intent);

                } else {
                    Toast.makeText(getContext(), "Respuesta no exitosa! Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
