public class Asistente {
    private String nombre;
    private String email;

    // Constructor
    public Asistente(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Asistente: " + nombre + " | Email: " + email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Asistente asistente = (Asistente) obj;
        return email.equals(asistente.email); // El email es unico
    }
}
