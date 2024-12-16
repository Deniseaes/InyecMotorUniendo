package front.inyecmotor.crearProducto;

import java.util.ArrayList;

public class ProductoCreate {


    private Long id;

    private String codigo;

    private String nombre;

    private int stockMin;

    private int stockMax;

    private int stockActual;

    private double precioVenta;

    private double precioCosto;

    private ArrayList<Integer> proveedores;

    private ArrayList<Integer> tipos;

    private ArrayList<Integer> modelos;

    public ProductoCreate(Long id, String codigo, String nombre, int stockMin, int stockMax, int stockActual, double precioVenta, double precioCosto, ArrayList<Integer> proveedores, ArrayList<Integer> tipos, ArrayList<Integer> modelos) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.stockMin = stockMin;
        this.stockMax = stockMax;
        this.stockActual = stockActual;
        this.precioVenta = precioVenta;
        this.precioCosto = precioCosto;
        this.proveedores = proveedores;
        this.tipos = tipos;
        this.modelos = modelos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStockMin() {
        return stockMin;
    }

    public void setStockMin(int stockMin) {
        this.stockMin = stockMin;
    }

    public int getStockMax() {
        return stockMax;
    }

    public void setStockMax(int stockMax) {
        this.stockMax = stockMax;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(double precioCosto) {
        this.precioCosto = precioCosto;
    }

    public ArrayList<Integer> getProveedores() {
        return proveedores;
    }

    public void setProveedores(ArrayList<Integer> proveedores) {
        this.proveedores = proveedores;
    }


    public ArrayList<Integer> getTipos() {
        return tipos;
    }

    public void setTipos(ArrayList<Integer> tipos) {
        this.tipos = tipos;
    }

    public ArrayList<Integer> getModelos() {
        return modelos;
    }

    public void setModelos(ArrayList<Integer> modelos) {
        this.modelos = modelos;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", stockMin=" + stockMin +
                ", stockMax=" + stockMax +
                ", stockActual=" + stockActual +
                ", precioVenta=" + precioVenta +
                ", precioCosto=" + precioCosto +
                ", proveedores=" + proveedores +
                ", tipos=" + tipos +
                ", modelos=" + modelos +
                '}';


    }
}

