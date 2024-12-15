package front.inyecmotor.ordenes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import front.inyecmotor.modelos.Modelo;
import front.inyecmotor.productos.Producto;

public class Orden implements Parcelable {
    private Long id;
    private Modelo modelo;
    private String patente;
    private String nombreCompleto;
    private double manoDeObra;
    private double otros;
    private String descripcionOtros;
    private double precioTotal;
    private ArrayList<Producto> productos;

    public Orden() {
    }

    public Orden(Long id, Modelo modelo, String patente,String nombreCompleto, double manoDeObra, double otros, String descripcionOtros, double precioTotal, ArrayList<Producto> productos) {
        this.id = id;
        this.modelo = modelo;
        this.patente = patente;
        this.nombreCompleto = nombreCompleto;
        this.manoDeObra = manoDeObra;
        this.otros = otros;
        this.descripcionOtros = descripcionOtros;
        this.precioTotal = precioTotal;
        this.productos = productos;
    }

    // Constructor para Parcel
    protected Orden(Parcel in) {
        id = in.readLong();
        modelo = in.readParcelable(Modelo.class.getClassLoader());
        patente = in.readString();
        nombreCompleto = in.readString();
        manoDeObra = in.readDouble();
        otros = in.readDouble();
        descripcionOtros = in.readString();
        precioTotal = in.readDouble();
        productos = in.createTypedArrayList(Producto.CREATOR);

    }

    public static final Creator<Orden> CREATOR = new Creator<Orden>() {
        @Override
        public Orden createFromParcel(Parcel in) {
            return new Orden(in);
        }

        @Override
        public Orden[] newArray(int size) {
            return new Orden[size];
        }
    };

    // Getters y setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public double getManoDeObra() {
        return manoDeObra;
    }

    public void setManoDeObra(double manoDeObra) {
        this.manoDeObra = manoDeObra;
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

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    // Métodos de Parcelable
    @Override
    public String toString() {
        return "Orden{" +
                "id=" + id +
                ", modelo=" + modelo +
                ", patente='" + patente + '\'' +
                ", manoDeObra=" + manoDeObra +
                ", otros=" + otros +
                ", descripcionOtros='" + descripcionOtros + '\'' +
                ", precioTotal=" + precioTotal +'\'' +
                ", productos=" + productos +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(modelo, flags); // Escribir el objeto Modelo en el Parcel
        dest.writeString(patente);
        dest.writeDouble(manoDeObra);
        dest.writeDouble(otros);
        dest.writeString(descripcionOtros);
        dest.writeDouble(precioTotal);
        dest.writeTypedList(productos);
    }
}
