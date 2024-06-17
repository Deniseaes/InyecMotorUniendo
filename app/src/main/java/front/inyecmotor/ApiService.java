package front.inyecmotor;

import java.util.List;

import front.inyecmotor.crearProducto.MarcaDTO;
import front.inyecmotor.crearProducto.ModeloDTO;
import front.inyecmotor.crearProducto.ProductoCreate;
import front.inyecmotor.crearProducto.ProveedorDTO;
import front.inyecmotor.crearProducto.TipoDTO;
import front.inyecmotor.marcas.Marca;
import front.inyecmotor.modelos.Modelo;
import front.inyecmotor.productos.Producto;
import front.inyecmotor.proveedores.Proveedor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/producto/all") // La ruta de tu endpoint en el servidor Spring Boot
    Call<List<Producto>> getProductos();

    @PATCH("/producto/editar")
    Call<Producto> editarProducto(@Body Producto producto);

    @POST("/producto/crear")
    Call<ProductoCreate> crearProducto(@Body ProductoCreate nuevoProducto);

    @GET("/tipo/all")
    Call<List<TipoDTO>> getTipos(); // Define el nuevo endpoint para obtener las tipos

    /* para ver las marcas*/
    @GET("/marca/all")
    Call<List<Marca>> getMarcas();

    /* para crear producto*/
    @GET("/marca/all")
    Call<List<MarcaDTO>> getMarcasDTO();

    @PATCH("/marca/editar")
    Call<Marca> editarMarca(@Body Marca marca);

    @GET("/modelo/all")
    Call<List<Modelo>> getModelos();

    @GET("/modelo/all")
    Call<List<ModeloDTO>> getModelosDTO();

    @PATCH("/modelo/editar")
    Call<Modelo> editarModelo(@Body Modelo modelo);

    @GET("/proveedor/all")
    Call<List<Proveedor>> getProveedores();

    @GET("/proveedor/all")
    Call<List<ProveedorDTO>> getProveedoresDTO();

    @PATCH("/proveedor/editar")
    Call<Proveedor> editarProveedor(@Body Proveedor proveedor);
}

