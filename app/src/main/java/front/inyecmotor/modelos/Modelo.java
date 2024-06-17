package front.inyecmotor.modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class Modelo implements Parcelable {
    private int id;
    private String nombre;
    private double motorLitros;
    private String motorTipo;
    private int anio;

    // Constructor vacío
    public Modelo() {
    }

    // Constructor con parámetros
    public Modelo(int id, String nombre, double motorLitros, String motorTipo, int anio) {
        this.id = id;
        this.nombre = nombre;
        this.motorLitros = motorLitros;
        this.motorTipo = motorTipo;
        this.anio = anio;
    }

    protected Modelo(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        motorLitros = in.readDouble();
        motorTipo = in.readString();
        anio = in.readInt();
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    @Override
    public String toString() {
        return "Modelo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", motorLitros=" + motorLitros +
                ", motorTipo='" + motorTipo + '\'' +
                ", anio=" + anio +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeDouble(motorLitros);
        dest.writeString(motorTipo);
        dest.writeInt(anio);
    }
}
