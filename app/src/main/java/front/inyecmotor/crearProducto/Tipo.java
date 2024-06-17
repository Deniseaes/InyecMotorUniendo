package front.inyecmotor.crearProducto;

public class Tipo {

        private int id; // Variable id
        private String nombre;

        // Constructor vacío (puede ser útil para algunas operaciones)
        public Tipo() {
        }

        // Constructor con parámetro para inicializar el campo nombre
        public Tipo(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        // Getter y setter para el campo id
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        // Getter y setter para el campo nombre
        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }


}
