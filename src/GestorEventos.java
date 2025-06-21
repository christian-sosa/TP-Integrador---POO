import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class GestorEventos {
    private List<Evento> listaEventos;

    // Constructor
    public GestorEventos() {
        this.listaEventos = new ArrayList<>();
    }

    // Agregar un evento nuevo
    public void agregarEvento(Evento evento) {
        if (evento != null && !listaEventos.contains(evento)) {
            listaEventos.add(evento);
            System.out.println("Evento '" + evento.getTitulo() + "' agregado exitosamente.");
        } 
        else {
            System.out.println("El evento ya existe o es nulo.");
        }
    }

    // Editar un evento por titulo
    public void editarEvento(String titulo) {
        Evento evento = buscarEventoPorTitulo(titulo);
        if (evento != null) {
            System.out.println("Evento encontrado: " + evento);
            // Aquí podrías implementar la edición específica
            System.out.println("Función de edición implementada para: " + titulo);
        } 
        else {
            System.out.println("Evento con titulo '" + titulo + "' no encontrado.");
        }
    }

    // Editar evento con nuevos datos
    public void editarEvento(String titulo, Date nuevaFecha, String nuevaUbicacion, String nuevaDescripcion) {
        Evento evento = buscarEventoPorTitulo(titulo);
        if (evento != null) {
            if (nuevaFecha != null) evento.setFecha(nuevaFecha);
            if (nuevaUbicacion != null) evento.setUbicacion(nuevaUbicacion);
            if (nuevaDescripcion != null) evento.setDescripcion(nuevaDescripcion);
            System.out.println("Evento '" + titulo + "' editado exitosamente.");
        } 
        else {
            System.out.println("Evento con titulo '" + titulo + "' no encontrado.");
        }
    }

    // Listar eventos futuros
    public void listarEventosFuturos() {
        Date ahora = new Date();
        System.out.println("=== EVENTOS FUTUROS ===");
        boolean hayEventosFuturos = false;
        
        for (Evento evento : listaEventos) {
            if (evento.getFecha().after(ahora)) {
                System.out.println(evento);
                hayEventosFuturos = true;
            }
        }
        
        if (!hayEventosFuturos) {
            System.out.println("No hay eventos futuros programados.");
        }
    }

    // Listar eventos pasados
    public void listarEventosPasados() {
        Date ahora = new Date();
        System.out.println("=== EVENTOS PASADOS ===");
        boolean hayEventosPasados = false;
        
        for (Evento evento : listaEventos) {
            if (evento.getFecha().before(ahora)) {
                System.out.println(evento);
                hayEventosPasados = true;
            }
        }
        
        if (!hayEventosPasados) {
            System.out.println("No hay eventos pasados.");
        }
    }

    // Listar todos los eventos
    public void listarTodosLosEventos() {
        System.out.println("=== TODOS LOS EVENTOS ===");
        if (listaEventos.isEmpty()) {
            System.out.println("No hay eventos registrados.");
        } 
        else {
            for (Evento evento : listaEventos) {
                System.out.println(evento);
            }
        }
    }

    // Buscar evento por titulo
    public Evento buscarEventoPorTitulo(String titulo) {
        for (Evento evento : listaEventos) {
            if (evento.getTitulo().equalsIgnoreCase(titulo)) {
                return evento;
            }
        }
        return null;
    }

    // Eliminar evento
    public boolean eliminarEvento(String titulo) {
        Evento evento = buscarEventoPorTitulo(titulo);
        if (evento != null) {
            listaEventos.remove(evento);
            System.out.println("Evento '" + titulo + "' eliminado exitosamente.");
            return true;
        } 
        else {
            System.out.println("Evento con titulo '" + titulo + "' no encontrado.");
            return false;
        }
    }

    // Agregar asistente a un evento
    public void agregarAsistenteAEvento(String tituloEvento, Asistente asistente) {
        Evento evento = buscarEventoPorTitulo(tituloEvento);
        if (evento != null) {
            evento.agregarAsistente(asistente);
            System.out.println("Asistente " + asistente.getNombre() + " agregado al evento '" + tituloEvento + "'.");
        } 
        else {
            System.out.println("Evento '" + tituloEvento + "' no encontrado.");
        }
    }

    // Getters
    public List<Evento> getListaEventos() {
        return new ArrayList<>(listaEventos); // Retorna una copia para proteger la lista original
    }

    public int getNumeroDeEventos() {
        return listaEventos.size();
    }
}
