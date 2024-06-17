package front.inyecmotor.marcas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarcasFragment extends Fragment {

    private RecyclerView recyclerView;
    private MarcaAdapter adapter;
    private ApiService apiService;
    private Button btnGetMarcas;

    private static final String BASE_URL = "http://192.168.0.106:8080"; // Cambia a la URL de tu servidor

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marcas_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        btnGetMarcas = view.findViewById(R.id.getMarcas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Configurar el botón para obtener marcas
        btnGetMarcas.setOnClickListener(v -> obtenerMarcas());

        return view;
    }

    private void obtenerMarcas() {
        Call<List<Marca>> call = apiService.getMarcas();

        call.enqueue(new Callback<List<Marca>>() {
            @Override
            public void onResponse(Call<List<Marca>> call, Response<List<Marca>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Marca> marcas = response.body();
                    abrirMarcasActivity(marcas);
                } else {
                    Toast.makeText(getContext(), "Error al obtener la lista de marcas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Marca>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirMarcasActivity(List<Marca> marcas) {
        Intent intent = new Intent(getContext(), MarcasActivity.class);
        intent.putParcelableArrayListExtra("marcas", new ArrayList<>(marcas));
        startActivity(intent);
    }
}
