package front.inyecmotor.crearProducto;

import java.util.List;

import front.inyecmotor.productos.Producto;
import front.inyecmotor.proveedores.Proveedor;

public class ProveedorDTO {

    private int id;
    private String cuit;
    private String tel;
    private String direccion;
    private String nombre;
    private String email;
    private List<Producto> productos;

    public ProveedorDTO(int id, String cuit, String tel, String direccion, String nombre, String email, List<Producto> productos) {
        this.id = id;
        this.cuit = cuit;
        this.tel = tel;
        this.direccion = direccion;
        this.nombre = nombre;
        this.email = email;
        this.productos = productos;
    }

    public Proveedor toProveedor() {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(this.getId());
        proveedor.setCuit(this.getCuit());
        proveedor.setTel(this.getTel());
        proveedor.setDireccion(this.getDireccion());
        proveedor.setNombre(this.getNombre());
        proveedor.setEmail(this.getEmail());
        return proveedor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

}
