package front.inyecmotor.ordenes;

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
import front.inyecmotor.login.LoginActivity;
import front.inyecmotor.proveedores.CrearProveedorActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrdenFragment extends Fragment {

    private static final String BASE_URL = BuildConfig.BASE_URL;
   // private static final String TAG = "OrdenesFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ordenes_fragment, container, false);

        // Botón para obtener la lista de órdenes
        Button getOrdenesButton = view.findViewById(R.id.getOrdenes);
        if (getOrdenesButton != null) {
            getOrdenesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchOrdenes();
                }
            });
        }

        // Botón para crear una nueva orden
        Button btnCrearOrden = view.findViewById(R.id.CrearOrden);
        btnCrearOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Crear el Intent para iniciar el Activity
                    Intent intent = new Intent(getActivity(), CrearOrdenActivity.class);
                    startActivity(intent);
                }catch (Exception e){Log.e("Error", "Error: " + e.getMessage());}

            }
        });

        return view;
    }

    private void fetchOrdenes() {
        // Obtener la contraseña hasheada desde SharedPreferences
        String hashedPassword = LoginActivity.PreferenceManager.getHashedPassword(getContext());

        if (hashedPassword == null) {
            Toast.makeText(getContext(), "Hashed password no encontrada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        String token = "Bearer " + hashedPassword;
        Call<List<Orden>> call = apiService.getOrdenes(token);

        call.enqueue(new Callback<List<Orden>>() {
            @Override
            public void onResponse(Call<List<Orden>> call, Response<List<Orden>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<Orden> ordenes = response.body();

                    // Ordenar las órdenes por ID descendente (suponiendo que ID más alto = orden más nueva)
                    ordenes.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));

                    System.out.println(ordenes);
                    System.err.println(" ERROR ORDENES: " + ordenes);

                    for (Orden orden : ordenes) {
                        System.err.println(" ERROR ORDENES: " + orden.toString());
                    }
                    List<DTOOrdenView> ordenesView = new ArrayList<>();
                    for (Orden orden : ordenes) {
                        DTOOrdenView ordenView = new DTOOrdenView();
                        ordenView.setId(orden.getId());
                        ordenView.setNombreCompleto(orden.getNombreCompleto());
                        ordenView.setModelo(orden.getModelo().getNombre());
                        ordenView.setPatente(orden.getPatente());
                        ordenView.setDescripcionOtros(orden.getDescripcionOtros());
                        ordenView.setTotal(orden.getPrecioTotal());

                        ordenesView.add(ordenView);
                    }
                    for (DTOOrdenView orden : ordenesView) {
                        System.err.println(" ERROR ORDENESDTO: " + orden.toString());
                    }


                    // Abre la actividad de órdenes y pasa la lista de órdenes
                    Intent intent = new Intent(getActivity(), OrdenesActivity.class);
                    intent.putParcelableArrayListExtra("ordenes", new ArrayList<>(ordenesView));
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Respuesta no exitosa! Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.d("HTTP Status Code", "Code: " + response.code());
                    Log.d("HTTP Response", "Response Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Orden>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
