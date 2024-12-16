package front.inyecmotor.login;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import front.inyecmotor.ApiService;
import front.inyecmotor.MainActivity;
import front.inyecmotor.R;
import front.inyecmotor.crearProducto.CrearProductoActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.SharedPreferences;
import android.content.Context;

public class LoginActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://192.168.0.8:8080"; // Cambia a la URL de tu servidor
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        // Limpiar contraseña guardada al iniciar la pantalla de login
        PreferenceManager.clearHashedPassword(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                if (!password.isEmpty()) {
                    String hashedPassword = hashPassword(password);
                    sendPasswordToServer(hashedPassword);
                } else {
                    Toast.makeText(LoginActivity.this, "Introduzca la contraseña: ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static String hashPassword(String password) {
        // Temporalmente, solo devolver la contraseña sin hashear
        return password;
    }

    // Clase para gestionar las preferencias (SharedPreferences)
    public static class PreferenceManager {

        private static final String PREF_NAME = "user_prefs";
        private static final String KEY_HASHED_PASSWORD = "hashed_password";

        // Guardar contraseña hasheada
        public static void saveHashedPassword(Context context, String hashedPassword) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_HASHED_PASSWORD, hashedPassword);
            editor.apply();
        }

        // Recuperar la contraseña hasheada para enviar en peticiones
        public static String getHashedPassword(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_HASHED_PASSWORD, null);
        }

        // Eliminar la contraseña hasheada guardada
        public static void clearHashedPassword(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(KEY_HASHED_PASSWORD);
            editor.apply();
        }
    }

    private void sendPasswordToServer(String hashedPassword) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        AuthDTO auth = new AuthDTO(hashedPassword);

        Call<Boolean> call = apiService.auth(auth);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (response.isSuccessful()) {
                    // Guardar la contraseña hasheada si el login esta ok.
                    PreferenceManager.saveHashedPassword(LoginActivity.this, hashedPassword);

                    Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                    // Si el login es exitoso, redirigir a la pantalla de inicio
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    System.out.println("Respuesta error login: "+ response.body());
                    Toast.makeText(LoginActivity.this, "Login falló. Contraseña inválida.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error de la llamada", "Error: " + t);
            }
        });
    }
}
