package front.inyecmotor.modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class Modelo implements Parcelable {
    private int id;
    private int anio;
    private double motorLitros;
    private String motorTipo;
    private String nombre;
    private long marcaId;

    // Constructor vacío
    public Modelo() {
    }

    // Constructor con parámetros
    public Modelo(int id, int anio, double motorLitros, String motorTipo, String nombre, long marcaId) {
        this.id = id;
        this.anio = anio;
        this.motorLitros = motorLitros;
        this.motorTipo = motorTipo;
        this.nombre = nombre;
        this.marcaId = marcaId;
    }

    protected Modelo(Parcel in) {
        id = in.readInt();
        anio = in.readInt();
        motorLitros = in.readDouble();
        motorTipo = in.readString();
        nombre = in.readString();
        marcaId = in.readLong();
    }

    public static final Creator<Modelo> CREATOR = new Creator<Modelo>() {
        @Override
        public Modelo createFromParcel(Parcel in) {
            return new Modelo(in);
        }

        @Override
        public Modelo[] newArray(int size) {
            return new Modelo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public double getMotorLitros() {
        return motorLitros;
    }

    public void setMotorLitros(double motorLitros) {
        this.motorLitros = motorLitros;
    }

    public String getMotorTipo() {
        return motorTipo;
    }

    public void setMotorTipo(String motorTipo) {
        this.motorTipo = motorTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(long marcaId) {
        this.marcaId = marcaId;
    }

    @Override
    public String toString() {
        return "Modelo{" +
                "id=" + id +
                ", anio=" + anio +
                ", motorLitros=" + motorLitros +
                ", motorTipo='" + motorTipo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", marcaId=" + marcaId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(anio);
        dest.writeDouble(motorLitros);
        dest.writeString(motorTipo);
        dest.writeString(nombre);
        dest.writeLong(marcaId);
    }
}
