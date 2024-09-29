package front.inyecmotor.proveedores;

import java.util.ArrayList;

public class ProveedorCreate {
    private Long id;

    private String nombre;

    private String direccion;

    private int cuit;

    private int tel;

    private String email;


    public ProveedorCreate(Long id, String nombre, String direccion, int cuit, int tel, String email) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.cuit = cuit;
        this.tel = tel;
        this.email = email;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getCuit() {
        return cuit;
    }

    public void setCuit(int cuit) {
        this.cuit = cuit;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
