package front.inyecmotor.ordenes;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.BuildConfig;
import front.inyecmotor.R;
import front.inyecmotor.crearProducto.ModeloDTO;
import front.inyecmotor.login.LoginActivity;
import front.inyecmotor.productos.Producto;
import front.inyecmotor.productos.ProductoAdapter;
import front.inyecmotor.proveedores.ProveedorAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrdenesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrdenAdapter adapter;


    private ApiService apiService;
    private static final String BASE_URL = BuildConfig.BASE_URL;;

    private SearchView searchViewOrden;
    private List<DTOOrdenView> ordenesFilter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchViewOrden = findViewById(R.id.searchViewOrden);
        ordenesFilter = new ArrayList<>();



        List<DTOOrdenView> ordenes = getIntent().getParcelableArrayListExtra("ordenes");
        for (DTOOrdenView orden : ordenes) {
            System.err.println(" ERROR ORDENES: " + orden.toString());
        }

        ordenesFilter.addAll(ordenes);

        adapter = new OrdenAdapter(ordenes, this);
        recyclerView.setAdapter(adapter);

       /* ordenes = getIntent().getParcelableArrayListExtra("ordenes");
        if (ordenes == null) {
            ordenes = new ArrayList<>();
        }*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupSearchView();

    }


    private void setupSearchView() {
        searchViewOrden.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterByPatente(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterByPatente(newText);
                return true;
            }
        });
    }

    private void filterByPatente(String query) {
        List<DTOOrdenView> filteredList= new ArrayList<>();
        for (DTOOrdenView ordenView : ordenesFilter) {
            if (ordenView.getPatente().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(ordenView);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
        }else {
            // Ordenar las órdenes por ID descendente (suponiendo que ID más alto = orden más nueva)
            filteredList.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));
        }
        adapter.setOrdenes(filteredList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
