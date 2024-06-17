package front.inyecmotor.crearProducto;

import java.util.List;

import front.inyecmotor.productos.Producto;

public class TipoDTO {
    private int id; // Variable id
    private String nombre;
    private List<Producto> productos;

    // Constructor vacío (puede ser útil para algunas operaciones)
    public TipoDTO() {
    }

    public TipoDTO(int id, String nombre, List<Producto> productos) {
        this.id = id;
        this.nombre = nombre;
        this.productos = productos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Tipo toTipo() {
        Tipo tipo = new Tipo();
        tipo.setId(this.id);
        tipo.setNombre(this.nombre);
        return tipo;
    }
}
