package front.inyecmotor.marcas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import front.inyecmotor.R;

public class MarcasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marcas_activity);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtén la lista de marcas de la intención
        List<Marca> marcas = getIntent().getParcelableArrayListExtra("marcas");

        // Configura el adaptador
        MarcaAdapter adapter = new MarcaAdapter(marcas, this);

        recyclerView.setAdapter(adapter);

        // Configura el botón de "Volver" en la Toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
