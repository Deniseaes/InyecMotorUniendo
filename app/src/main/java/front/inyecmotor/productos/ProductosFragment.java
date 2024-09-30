package front.inyecmotor.productos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.R;
import front.inyecmotor.crearProducto.CrearProductoActivity;
import front.inyecmotor.login.AuthDTO;
import front.inyecmotor.login.LoginActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//se encarga de mostrar un par de botones para interactuar con productos.Estos botones permiten obtener una lista
// de productos desde un servidor y crear un nuevo producto. Su layout XML es productos_fragment
public class ProductosFragment extends Fragment {

    private static final String BASE_URL = "http://192.168.56.1:8080"; // Cambia esto según tu configuración

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.productos_fragment, container, false);

        Button getProductosButton = view.findViewById(R.id.getProductos);
        if (getProductosButton != null) {
            getProductosButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchProductos();
                }
            });
        }

        Button btnCrearProducto = view.findViewById(R.id.btnCrearProducto);
        btnCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Crear el Intent para iniciar el Activity
                    Intent intent = new Intent(getActivity(), CrearProductoActivity.class);
                    startActivity(intent);
                }catch (Exception e){Log.e("Error", "Error: " + e.getMessage());}

            }
        });

        // Botón "Ver productos en faltante"
        Button btnVerProductosFaltante = view.findViewById(R.id.btnVerProductosFaltante);
        btnVerProductosFaltante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchProductosFaltante(); // Llamar a la nueva función
            }
        });





        return view;


    }

    private void fetchProductos() {

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
        Call<List<Producto>> call = apiService.getProductos(token); //Pasamos en la peticion el hashedPassword

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body();

                    // Abre la actividad de productos y pasa la lista de productos
                    Intent intent = new Intent(getActivity(), ProductosActivity.class);
                    intent.putParcelableArrayListExtra("productos", new ArrayList<>(productos));
                    startActivity(intent);

                } else {
                    Toast.makeText(getContext(), "Respuesta unsuccessful!", Toast.LENGTH_SHORT).show();
                    Log.d("HTTP Status Code", "Code: " + response.code());
                    Log.d("HTTP Response", "Response Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProductosFaltante() {
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
        Log.d("Token", token);
        Call<List<Producto>> call = apiService.getProductosAReponer(token); //Pasamos en la peticion el hashedPassword

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body();

                    // Abre la actividad de productos y pasa la lista de productos
                    Intent intent = new Intent(getActivity(), ProductosActivity.class);
                    intent.putParcelableArrayListExtra("productos", new ArrayList<>(productos));
                    startActivity(intent);

                } else {
                    Toast.makeText(getContext(), "Respuesta unsuccessful!", Toast.LENGTH_SHORT).show();
                    Log.d("HTTP Status Code", "Code: " + response.code());
                    Log.d("HTTP Response", "Response Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}