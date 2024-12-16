package front.inyecmotor;

import java.util.List;


import front.inyecmotor.crearProducto.ModeloDTO;
import front.inyecmotor.crearProducto.ProductoCreate;
import front.inyecmotor.crearProducto.ProveedorDTO;
import front.inyecmotor.crearProducto.TipoDTO;
import front.inyecmotor.login.AuthDTO;
import front.inyecmotor.modelos.Modelo;
import front.inyecmotor.modelos.ModeloCreate;
import front.inyecmotor.ordenes.DTOOrdenView;
import front.inyecmotor.ordenes.Orden;
import front.inyecmotor.ordenes.OrdenCreate;
import front.inyecmotor.productos.Producto;
import front.inyecmotor.productos.TipoProducto;
import front.inyecmotor.proveedores.Proveedor;
import front.inyecmotor.proveedores.ProveedorCreate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import okhttp3.ResponseBody;

public interface ApiService {

    //PRODUCTOS
    @GET("/producto/all") // La ruta de tu endpoint en el servidor Spring Boot
    Call<List<Producto>> getProductos(@Header("Authorization") String authToken);

    @PATCH("/producto/editar")
    Call<Producto> editarProducto(@Header("Authorization") String authToken, @Body Producto producto);

    @POST("/producto/crear")
    Call<ProductoCreate> crearProducto(@Header("Authorization") String authToken, @Body ProductoCreate nuevoProducto);


    //@GET("/producto/get-by-modelo/{idModelo}")
    //Call<ResponseBody> getProductosByModelo(@Header("Authorization") String token, @Path("idModelo") int idModelo);//Este usa order

    @GET("/producto/get-by-tipo/{idTipo}")
    Call<List<Producto>> getProductosByTipo(@Header("Authorization") String token, @Path("idTipo") Long tipo);

    @GET("/producto/get-productos-a-reponer")
    Call<List<Producto>> getProductosAReponer(@Header("Authorization") String authToken);

    @DELETE("/producto/eliminar/{id}")
    Call<Void> eliminarProducto(@Header("Authorization") String authorization, @Path("id") int id);

    //TIPOS
    @GET("/tipo/all")
    Call<List<TipoDTO>> getTipos(@Header("Authorization") String authToken);

    //MODELOS

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

    @DELETE("/modelo/eliminar/{id}")
    Call<Void>eliminarModelo(@Header("Authorization") String authorization, @Path("id") int id);


    //PROVEEDOR

    @GET("/proveedor/all")
    Call<List<Proveedor>> getProveedores(@Header("Authorization") String authToken);

    @POST("/proveedor/crear")
    Call<ProveedorCreate> crearProveedor(@Header("Authorization") String authToken, @Body ProveedorCreate nuevoProveedor);

    @GET("/proveedor/all")
    Call<List<ProveedorDTO>> getProveedoresDTO(@Header("Authorization") String authToken);

    @PATCH("/proveedor/editar")
    Call<Proveedor> editarProveedor(@Header("Authorization") String authToken, @Body Proveedor proveedor);

    @DELETE("/proveedor/eliminar/{id}")
    Call<Void>eliminarProveedor(@Header("Authorization") String authorization, @Path("id") int id);

    //AUTH

    @POST("/auth")
    Call<Boolean> auth(@Body AuthDTO auth);

    @GET("/producto/get-by-tipo/{id}")
    Call<List<Producto>> getProductosByTipo(@Header("Authorization") String authToken, @Path("id") int tipoId);



    @GET("/producto/get-by-id/{id}")
    Call<Producto> getProductoById(@Header("Authorization") String authToken, @Path("id") int productoId);

    @GET("/tipo/get-by-id/{id}")
    Call<TipoDTO> getTipoById(@Header("Authorization") String authToken, @Path("id") int tipoId);


    @GET("/modelo/get-by-id/{id}")
    Call<ModeloDTO> getModeloById(@Header("Authorization") String authToken, @Path("id") int modeloId);

    @GET("modelo/suggestions")
Call<List<String>> getModeloSuggestions(@Header("Authorization") String authToken, @Query("query") String query);


@GET("modelo/all")
Call<List<Modelo>> getAllModelos(@Header("Authorization") String authToken);

@GET("producto/get-by-modelo/{id}")
Call<ResponseBody> getProductosByModelo(@Header("Authorization") String authToken, @Path("id") int modeloId);


//OREDENES

    @GET("orden/all")
    Call<List<Orden>> getOrdenes(@Header("Authorization") String token);

    @POST("orden/crear")
    Call<OrdenCreate> crearOrden(@Header("Authorization") String token, @Body OrdenCreate orden);

    @DELETE("orden/eliminar/{id}")
    Call<Void> eliminarOrden(@Header("Authorization") String token, @Path("id") Long id);
    @GET("/orden/get-by-patente")
    Call<List<DTOOrdenView>> getOrdenByPatente(@Header("Authorization") String authorizationHeader,
                                               @Query("patente") String patente);

    @PATCH("orden/editar")
    Call<Orden> editarOrden(@Header("Authorization") String token, @Body Orden orden);



    Call<List<Modelo>> getModeloByName(String query);
}

