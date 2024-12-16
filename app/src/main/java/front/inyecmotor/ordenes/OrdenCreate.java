package front.inyecmotor.ordenes;


import java.util.ArrayList;
import java.util.List;

public class OrdenCreate {
    private Long id;
    private String nombreCompleto;
    private String patente;
    private int modelo;
    private double manoDeObra;
    private double otros;
    private String descripcionOtros;
    private double precioTotal;
    private ArrayList<Long> productos; // IDs de los repuestos seleccionados

    // Constructor vacío


    // Constructor con parámetros
    public OrdenCreate(Long id,String nombreCompleto, String patente, int modelo,
                       double manoDeObra, double otros,String descripcionOtros, ArrayList<Long> productos) {
        this.id=id;
        this.nombreCompleto = nombreCompleto;
        this.patente = patente;
        this.modelo = modelo;
        this.manoDeObra = manoDeObra;
        this.otros = otros;
        this.descripcionOtros = descripcionOtros;
        this.productos = productos;
        //this.precioTotal = precioTotal;

    }



    // Getters y setters

    public Long getId() {
        return id;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getnombreCompleto() {
        return nombreCompleto;
    }

    public void setnombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }



    public double getManoDeObra() {
        return manoDeObra;
    }

    public void setManoDeObra(double manoDeObra) {
        this.manoDeObra = manoDeObra;
    }


    public int getModelo() {
        return modelo;
    }

    public void setModelo(int modelo) {
        this.modelo = modelo;
    }

    public double getOtros() {
        return otros;
    }

    public void setOtros(double otros) {
        this.otros = otros;
    }

    public String getDescripcionOtros() {
        return descripcionOtros;
    }

    public void setDescripcionOtros(String descripcionOtros) {
        this.descripcionOtros = descripcionOtros;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public ArrayList<Long> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Long> productos) {
        this.productos = productos;
    }




    @Override
    public String toString() {
        return "OrdenCreate{" +
                "id=" + id +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", patente='" + patente + '\'' +
                ", modelo=" + modelo +
                ", manoDeObra=" + manoDeObra +
                ", otros=" + otros +
                ", descripcionOtros='" + descripcionOtros + '\'' +
                ", productos=" + productos +
               // ", precioTotal=" + precioTotal +
                '}';
    }

}

