package front.inyecmotor.productos;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import front.inyecmotor.R;


//ProductosActivity es una pantalla que muestra una lista de productos en un RecyclerView.
//Se recibe una lista de productos a través de un Intent y se muestra utilizando un ProductoAdapter.
public class ProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos_activity);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtén la lista de productos de la intención
        List<Producto> productos = getIntent().getParcelableArrayListExtra("productos");
        if (productos == null || productos.isEmpty()) {
            // Manejar el caso de que la lista esté vacía
            Toast.makeText(this, "No se encontraron productos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Configura el adaptador
        ProductoAdapter adapter = new ProductoAdapter(productos, this); // Paso el contexto this

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