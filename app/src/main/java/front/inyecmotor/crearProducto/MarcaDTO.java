package front.inyecmotor.crearProducto;

import java.util.List;

import front.inyecmotor.marcas.Marca;
import front.inyecmotor.modelos.Modelo;
import front.inyecmotor.productos.Producto;

public class MarcaDTO {

    private int id; // Variable id
    private String nombre;
    private List<Producto> productos;
    private List<Modelo> modelos;


    // Constructor vacío (puede ser útil para algunas operaciones)
    public MarcaDTO() {
    }

    // Constructor con parámetro para inicializar el campo nombre


    public MarcaDTO(int id, String nombre, List<Producto> productos, List<Modelo> modelos) {
        this.id = id;
        this.nombre = nombre;
        this.productos = productos;
        this.modelos = modelos;
    }

    // Getter y setter para el campo id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter y setter para el campo nombre
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

    public List<Modelo> getModelo() {
        return modelos;
    }

    public void setModelo(List<Modelo> modelo) {
        this.modelos = modelo;
    }

    public Marca toMarca() {
        Marca marca = new Marca();
        marca.setId(this.id);
        marca.setNombre(this.nombre);
        return marca;
    }

}
