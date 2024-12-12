package front.inyecmotor.modelos;

public class ModeloCreate {
    private Long id;

    private String nombre;

    private String marca;

    private double motorLitros;

    private String motorTipo;

    private int anio;


    public ModeloCreate(Long id, String nombre, String marca, double motorLitros, String motorTipo, int anio) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.motorLitros = motorLitros;
        this.motorTipo = motorTipo;
        this.anio = anio;

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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
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
}
