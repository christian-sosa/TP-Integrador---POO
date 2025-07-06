package gestor;

import modelo.Evento;
import modelo.Asistente;
import java.util.List;
import java.util.ArrayList;

public class GestorEventos {
    private List<Evento> eventos;
    
    public GestorEventos() {
        eventos = new ArrayList<>();
    }
    
    public void agregarEvento(Evento evento) {
        eventos.add(evento);
    }
    
    public void eliminarEvento(String titulo) {
        boolean encontrado = false;
        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getTitulo().equals(titulo)) {
                eventos.remove(i);
                encontrado = true;
                break;
            }
        }
    }
    
    public Evento buscarEventoPorTitulo(String titulo) {
        for (Evento evento : eventos) {
            if (evento.getTitulo().equals(titulo)) {
                return evento;
            }
        }
        return null;
    }
    
    public void agregarAsistenteAEvento(String tituloEvento, Asistente asistente) {
        Evento evento = buscarEventoPorTitulo(tituloEvento);
        if (evento != null) {
            evento.agregarAsistente(asistente);
        }
    }
    
    public List<Evento> getListaEventos() {
        return eventos;
    }
    
    public void mostrarEventos() {
        if (eventos.isEmpty()) {
            System.out.println("No hay eventos registrados.");
            return;
        }
        
        System.out.println("\n=== LISTA DE EVENTOS ===");
        for (int i = 0; i < eventos.size(); i++) {
            Evento evento = eventos.get(i);
            System.out.println((i + 1) + ". " + evento.getTitulo() + 
                             " - " + evento.getFecha() + 
                             " - " + evento.getUbicacion());
        }
    }
} 