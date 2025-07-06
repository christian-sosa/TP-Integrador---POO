package modelo;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Evento {
    private String titulo;
    private Date fecha;
    private String ubicacion;
    private String descripcion;
    private List<Asistente> asistentes;
    
    public Evento(String titulo, Date fecha, String ubicacion, String descripcion) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.asistentes = new ArrayList<>();
    }
    
    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public List<Asistente> getAsistentes() {
        return asistentes;
    }
    
    public void agregarAsistente(Asistente asistente) {
        asistentes.add(asistente);
    }
    
    @Override
    public String toString() {
        return "Evento: " + titulo + " | Fecha: " + fecha + " | Ubicacion: " + ubicacion + " | Descripcion: " + descripcion + " | Cantidad de asistentes: " + asistentes.size();
    }
} 