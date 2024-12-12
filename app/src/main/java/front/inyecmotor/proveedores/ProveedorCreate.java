package front.inyecmotor.proveedores;

import java.util.ArrayList;

public class ProveedorCreate {
    private Long id;

    private String nombre;

    private String direccion;

<<<<<<< HEAD
    private String cuit;

    private String tel;
=======
<<<<<<< HEAD
    private int cuit;

    private int tel;
=======
    private String cuit;

    private String tel;
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)

    private String email;


<<<<<<< HEAD
    public ProveedorCreate(Long id, String nombre, String direccion, String cuit, String tel, String email) {
=======
<<<<<<< HEAD
    public ProveedorCreate(Long id, String nombre, String direccion, int cuit, int tel, String email) {
=======
    public ProveedorCreate(Long id, String nombre, String direccion, String cuit, String tel, String email) {
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)
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

<<<<<<< HEAD
=======
<<<<<<< HEAD
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
=======
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
>>>>>>> c331e9f (Nuevas features)

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
<<<<<<< HEAD
=======
>>>>>>> 6c95963 (Features)
>>>>>>> c331e9f (Nuevas features)
}
