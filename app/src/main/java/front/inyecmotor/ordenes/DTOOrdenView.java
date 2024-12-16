package front.inyecmotor.ordenes;
import android.os.Parcel;
import android.os.Parcelable;

public class DTOOrdenView implements Parcelable {
    private Long id;
    private String nombreCompleto;
    private String descripcionOtros;
    private String modelo;
    private String patente;
    private double total;

    // Constructor vacío
    public DTOOrdenView() {
    }

    // Constructor con parámetros
    public DTOOrdenView(Long id, String nombreCompleto,String descripcionOtros, String modelo, String patente, double total) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.descripcionOtros = descripcionOtros;
        this.modelo = modelo;
        this.patente = patente;
        this.total = total;
    }

    // Constructor para Parcelable
    protected DTOOrdenView(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        nombreCompleto = in.readString();
        descripcionOtros = in.readString();
        modelo = in.readString();
        patente = in.readString();
        total = in.readDouble();
    }

    public static final Creator<DTOOrdenView> CREATOR = new Creator<DTOOrdenView>() {
        @Override
        public DTOOrdenView createFromParcel(Parcel in) {
            return new DTOOrdenView(in);
        }

        @Override
        public DTOOrdenView[] newArray(int size) {
            return new DTOOrdenView[size];
        }
    };

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "DTOOrdenView{" +
                "id=" + id +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", descripcionOtros='" + descripcionOtros + '\'' +
                ", modelo='" + modelo + '\'' +
                ", patente='" + patente + '\'' +
                ", total=" + total +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(nombreCompleto);
        dest.writeString(descripcionOtros);
        dest.writeString(modelo);
        dest.writeString(patente);
        dest.writeDouble(total);
    }
}