package front.inyecmotor;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import front.inyecmotor.expandirBarrita.DrawerAdapter;
import front.inyecmotor.expandirBarrita.DrawerItem;
import front.inyecmotor.expandirBarrita.SimpleItem;
import front.inyecmotor.expandirBarrita.SpaceItem;

import front.inyecmotor.modelos.ModelosFragment;
import front.inyecmotor.productos.ProductosFragment;
import front.inyecmotor.proveedores.ProveedoresFragment;
import front.inyecmotor.ordenes.OrdenFragment;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_CLOSE = 0;
    private static final int POS_PRODUCTOS = 1;
    private static final int POS_PROVEEDORES = 2;
    private static final int POS_MODELOS = 3;
    private static final int POS_ORDENES = 4;



    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {  //Configura la pantalla principal y el menú desplegable.
        super.onCreate(savedInstanceState);

        //Verificmos el inicio de sesion




        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);      // pantalla principal, se configura para interactuar con el menú desplegable.
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this) //drawer que maneja el menu desplegable
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar) //abrir y cerrar el menu
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(   //opciones o botones del menu
                createItemFor(POS_CLOSE),
                createItemFor(POS_PRODUCTOS).setChecked(true),
                createItemFor(POS_PROVEEDORES),
                createItemFor(POS_MODELOS),
                createItemFor(POS_ORDENES),

                new SpaceItem(260)

        ));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_PRODUCTOS);
    }

    private DrawerItem createItemFor(int position) {  //Define y personaliza los elementos individuales del menú
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.red))
                .withTextTint(color(R.color.black))
                .withSelectedIconTint(color(R.color.red))
                .withSelectedTextTint(color(R.color.red));
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.id_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemSelected(int position) {      //Maneja la transición y reemplazo de fragmentos en la pantalla principal según la opción seleccionada en el menú desplegable.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (position) {
            case POS_CLOSE:
                slidingRootNav.closeMenu();
                return;
            case POS_PRODUCTOS:
                ProductosFragment productosFragment = new ProductosFragment();
                transaction.replace(R.id.container, productosFragment);
                break;
            case POS_PROVEEDORES:
                ProveedoresFragment proveedoresFragment = new ProveedoresFragment();
                transaction.replace(R.id.container, proveedoresFragment);
                break;
            case POS_MODELOS:
                ModelosFragment modelosFragment = new ModelosFragment();
                transaction.replace(R.id.container, modelosFragment);
                break;
            case POS_ORDENES:
                OrdenFragment ordenFragment = new OrdenFragment();
                transaction.replace(R.id.container, ordenFragment);
                break;

                // Implementar lógica de cierre de sesión aquí
        }

        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
