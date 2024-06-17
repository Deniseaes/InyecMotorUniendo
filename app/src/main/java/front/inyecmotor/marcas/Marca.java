package front.inyecmotor.marcas;

import android.os.Parcel;
import android.os.Parcelable;

public class Marca implements Parcelable {
    private int id;
    private String nombre;

    // Constructor vacío (puede ser útil para algunas operaciones)
    public Marca() {
    }

    // Constructor con parámetros para inicializar todos los campos
    public Marca(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Constructor para Parcel
    protected Marca(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
    }

    // Getters y setters para todos los campos
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

    // Otros getters y setters si es necesario para otros campos

    @Override
    public String toString() {
        return "Marca{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    // Métodos de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(nombre);
    }

    public static final Creator<Marca> CREATOR = new Creator<Marca>() {
        @Override
        public Marca createFromParcel(Parcel in) {
            return new Marca(in);
        }

        @Override
        public Marca[] newArray(int size) {
            return new Marca[size];
        }
    };
}
