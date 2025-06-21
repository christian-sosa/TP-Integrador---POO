import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class ArchivoUtils {
    private static final String ARCHIVO_EVENTOS = "eventos.txt";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    // Guardar eventos en archivo
    public static void guardarEventos(List<Evento> eventos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_EVENTOS))) {
            for (Evento evento : eventos) {
                // Formato: titulo|fecha|ubicacion|descripcion|asistentes
                StringBuilder linea = new StringBuilder();
                linea.append(evento.getTitulo()).append("|");
                linea.append(dateFormat.format(evento.getFecha())).append("|");
                linea.append(evento.getUbicacion()).append("|");
                linea.append(evento.getDescripcion()).append("|");
                
                // Agregar asistentes separados por comas
                for (int i = 0; i < evento.getAsistentes().size(); i++) {
                    Asistente asistente = evento.getAsistentes().get(i);
                    linea.append(asistente.getNombre()).append(":").append(asistente.getEmail());
                    if (i < evento.getAsistentes().size() - 1) {
                        linea.append(",");
                    }
                }
                
                writer.println(linea.toString());
            }
            System.out.println("Eventos guardados exitosamente en " + ARCHIVO_EVENTOS);
        } 
        catch (IOException e) {
            System.err.println("Error al guardar eventos: " + e.getMessage());
        }
    }

    // Cargar eventos desde archivo
    public static List<Evento> cargarEventos() {
        List<Evento> eventos = new ArrayList<>();
        File archivo = new File(ARCHIVO_EVENTOS);
        
        if (!archivo.exists()) {
            System.out.println("Archivo de eventos no existe. Se creara uno nuevo al guardar.");
            return eventos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                try {
                    String[] partes = linea.split("\\|");
                    if (partes.length >= 4) {
                        String titulo = partes[0];
                        Date fecha = dateFormat.parse(partes[1]);
                        String ubicacion = partes[2];
                        String descripcion = partes[3];
                        
                        Evento evento = new Evento(titulo, fecha, ubicacion, descripcion);
                        
                        // Cargar asistentes si existen
                        if (partes.length > 4 && !partes[4].isEmpty()) {
                            String[] asistentesData = partes[4].split(",");
                            for (String asistenteData : asistentesData) {
                                String[] asistenteInfo = asistenteData.split(":");
                                if (asistenteInfo.length == 2) {
                                    Asistente asistente = new Asistente(asistenteInfo[0], asistenteInfo[1]);
                                    evento.agregarAsistente(asistente);
                                }
                            }
                        }
                        
                        eventos.add(evento);
                    }
                } 
                catch (ParseException e) {
                    System.err.println("Error al parsear fecha en linea: " + linea);
                }
            }
            System.out.println("Eventos cargados exitosamente: " + eventos.size() + " eventos.");
        } 
        catch (IOException e) {
            System.err.println("Error al cargar eventos: " + e.getMessage());
        }
        
        return eventos;
    }

    // Verificar si existe el archivo de eventos
    public static boolean existeArchivoEventos() {
        return new File(ARCHIVO_EVENTOS).exists();
    }

    // Eliminar archivo de eventos
    public static boolean eliminarArchivoEventos() {
        File archivo = new File(ARCHIVO_EVENTOS);
        if (archivo.exists()) {
            return archivo.delete();
        }
        return false;
    }
}
