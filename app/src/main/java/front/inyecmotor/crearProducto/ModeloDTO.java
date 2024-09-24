package front.inyecmotor.crearProducto;

import java.util.List;

import front.inyecmotor.modelos.Modelo;
import front.inyecmotor.productos.Producto;

public class ModeloDTO {

    private int id; // Variable id
    private String nombre;
    private String marca;
    private double motorLitros;
    private String motorTipo;
    private int anio;
    private List<Producto> productos;

    // Constructor vacío (puede ser útil para algunas operaciones)
    public ModeloDTO() {
    }

    // Constructor con parámetro para inicializar el campo nombre


    public ModeloDTO(int id, String nombre, String marca, double motorLitros, String motorTipo, int anio, List<Producto> productos) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.motorLitros = motorLitros;
        this.motorTipo = motorTipo;
        this.anio = anio;
        this.productos = productos;
    }

    public double getMotorLitros() {
        return motorLitros;
    }

    public String getMotorTipo() {
        return motorTipo;
    }

    public int getAnio() {
        return anio;
    }

    public void setMotorLitros(double motorLitros) {
        this.motorLitros = motorLitros;
    }

    public void setMotorTipo(String motorTipo) {
        this.motorTipo = motorTipo;
    }

    public void setAnio(int anio) {
        this.anio = anio;
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

    // Getter y setter para el campo marca
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.nombre = marca;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Modelo toModelo() {
        Modelo modelo = new Modelo();
        modelo.setId(this.id);
        modelo.setNombre(this.nombre);
        return modelo;
    }
}

