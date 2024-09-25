package front.inyecmotor;

import java.util.List;


import front.inyecmotor.crearProducto.ModeloDTO;
import front.inyecmotor.crearProducto.ProductoCreate;
import front.inyecmotor.crearProducto.ProveedorDTO;
import front.inyecmotor.crearProducto.TipoDTO;
import front.inyecmotor.login.AuthDTO;
import front.inyecmotor.modelos.Modelo;
import front.inyecmotor.modelos.ModeloCreate;
import front.inyecmotor.productos.Producto;
import front.inyecmotor.proveedores.Proveedor;
import front.inyecmotor.proveedores.ProveedorCreate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/producto/all") // La ruta de tu endpoint en el servidor Spring Boot
    Call<List<Producto>> getProductos(@Header("Authorization") String authToken);

    @PATCH("/producto/editar")
    Call<Producto> editarProducto(@Header("Authorization") String authToken, @Body Producto producto);

    @POST("/producto/crear")
    Call<ProductoCreate> crearProducto(@Header("Authorization") String authToken, @Body ProductoCreate nuevoProducto);


    @GET("/producto/get-by-modelo/{idModelo}")
    Call<List<Producto>> getProductosByModelo(@Header("Authorization") String token, @Path("idModelo") String idModelo);

    @GET("/producto/get-by-tipo/{idTipo}")
    Call<List<Producto>> getProductosByTipo(@Header("Authorization") String token, @Path("idTipo") String tipo);

    @GET("/producto/get-productos-a-reponer")
    Call<List<Producto>> getProductosAReponer(@Header("Authorization") String authToken);

    @GET("/tipo/all")
    Call<List<TipoDTO>> getTipos(@Header("Authorization") String authToken);

    /* para ver las marcas*/
    @GET("/modelo/get-by-name")
    Call<List<ModeloDTO>> getModeloByName(
            @Header("Authorization") String token,
            @Query("nombre") String nombre
    );

    @GET("/modelo/all")
    Call<List<Modelo>> getModelos(@Header("Authorization") String authToken);

    @GET("/modelo/all")
    Call<List<ModeloDTO>> getModelosDTO(@Header("Authorization") String authToken);
    @POST("/modelo/crear")
    Call<ModeloCreate> crearModelo(@Header("Authorization") String authToken, @Body ModeloCreate nuevoModelo);


    @PATCH("/modelo/editar")
    Call<Modelo> editarModelo(@Header("Authorization") String authToken, @Body Modelo modelo);

    @GET("/proveedor/all")
    Call<List<Proveedor>> getProveedores(@Header("Authorization") String authToken);

    @POST("/proveedor/crear")
    Call<ProveedorCreate> crearProveedor(@Header("Authorization") String authToken, @Body ProveedorCreate nuevoProveedor);

    @GET("/proveedor/all")
    Call<List<ProveedorDTO>> getProveedoresDTO(@Header("Authorization") String authToken);

    @PATCH("/proveedor/editar")
    Call<Proveedor> editarProveedor(@Header("Authorization") String authToken, @Body Proveedor proveedor);

    @POST("/auth")
    Call<Boolean> auth(@Body AuthDTO auth);



}

