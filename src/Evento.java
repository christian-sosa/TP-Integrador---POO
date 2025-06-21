import java.util.Date;
import java.util.ArrayList;

public class Evento {
    private String titulo;
    private Date fecha;
    private String ubicacion;
    private String descripcion;
    private ArrayList<Asistente> asistentes;

    // Constructor
    public Evento(String titulo, Date fecha, String ubicacion, String descripcion) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.asistentes = new ArrayList<>();
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public ArrayList<Asistente> getAsistentes() {
        return asistentes;
    }

    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Métodos para manejar asistentes
    public void agregarAsistente(Asistente asistente) {
        if (!asistentes.contains(asistente)) {
            asistentes.add(asistente);
        }
    }

    public void eliminarAsistente(Asistente asistente) {
        asistentes.remove(asistente);
    }

    @Override
    public String toString() {
        return "Evento: " + titulo + " | Fecha: " + fecha + " | Ubicacion: " + ubicacion + " | Descripcion: " + descripcion + " | Cantidad de asistentes: " + asistentes.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Evento evento = (Evento) obj;
        return titulo.equals(evento.titulo);
    }
}