package front.inyecmotor.proveedores;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import front.inyecmotor.productos.Producto;

public class Proveedor implements Parcelable {
    private int id;
    private String cuit;
    private String tel;
    private String direccion;
    private String nombre;
    private String email;
    private List<Producto> productos; // Dependiendo de la estructura de Producto

    // Constructor vacío
    public Proveedor() {
    }

    // Constructor con parámetros
    public Proveedor(int id, String cuit, String tel, String direccion, String nombre, String email, List<Producto> productos) {
        this.id = id;
        this.cuit = cuit;
        this.tel = tel;
        this.direccion = direccion;
        this.nombre = nombre;
        this.email = email;
        this.productos = productos;
    }

    protected Proveedor(Parcel in) {
        id = in.readInt();
        cuit = in.readString();
        tel = in.readString();
        direccion = in.readString();
        nombre = in.readString();
        email = in.readString();
        productos = in.createTypedArrayList(Producto.CREATOR);
    }

    public static final Creator<Proveedor> CREATOR = new Creator<Proveedor>() {
        @Override
        public Proveedor createFromParcel(Parcel in) {
            return new Proveedor(in);
        }

        @Override
        public Proveedor[] newArray(int size) {
            return new Proveedor[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "id=" + id +
                ", cuit='" + cuit + '\'' +
                ", tel='" + tel + '\'' +
                ", direccion='" + direccion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", productos=" + productos +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(cuit);
        dest.writeString(tel);
        dest.writeString(direccion);
        dest.writeString(nombre);
        dest.writeString(email);
        dest.writeTypedList(productos);
    }
}
