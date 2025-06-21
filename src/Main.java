import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

public class Main {
    private static GestorEventos gestor = new GestorEventos();
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        System.out.println("================================================");
        System.out.println("   Paradigma Orientado a Objetos - UADE");
        System.out.println("   GESTOR DE EVENTOS - TP Integrador");
        System.out.println("   Christian Agustin Sosa - 1202494 - 2025");
        System.out.println("================================================");
        
        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion();
            procesarOpcion(opcion);
        } while (opcion != 0);
        
        // Guardar eventos antes de salir
        guardarEventosEnArchivo();
        System.out.println("¡Gracias por usar el Gestor de Eventos!");
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Agregar evento");
        System.out.println("2. Listar todos los eventos");
        System.out.println("3. Listar eventos futuros");
        System.out.println("4. Listar eventos pasados");
        System.out.println("5. Buscar evento por título");
        System.out.println("6. Editar evento");
        System.out.println("7. Eliminar evento");
        System.out.println("8. Agregar asistente a evento");
        System.out.println("9. Guardar eventos en archivo");
        System.out.println("10. Cargar eventos desde archivo");
        System.out.println("11. Ver calendario del mes actual");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } 
        catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                agregarEvento();
                break;
            case 2:
                gestor.listarTodosLosEventos();
                break;
            case 3:
                gestor.listarEventosFuturos();
                break;
            case 4:
                gestor.listarEventosPasados();
                break;
            case 5:
                buscarEvento();
                break;
            case 6:
                editarEvento();
                break;
            case 7:
                eliminarEvento();
                break;
            case 8:
                agregarAsistenteAEvento();
                break;
            case 9:
                guardarEventosEnArchivo();
                break;
            case 10:
                cargarEventosDesdeArchivo();
                break;
            case 11:
                mostrarCalendarioMensual();
                break;
            case 0:
                System.out.println("Saliendo del gestor de eventos...");
                break;
            default:
                System.out.println("Opcion invalida. Por favor, seleccione una opcion valida.");
        }
    }

    private static void agregarEvento() {
        System.out.println("\n=== AGREGAR NUEVO EVENTO ===");
        
        System.out.print("Titulo del evento: ");
        String titulo = scanner.nextLine();
        
        Date fecha = null;
        while (fecha == null) {
            System.out.print("Fecha y hora (formato: dd/MM/yyyy HH:mm): ");
            String fechaStr = scanner.nextLine();
            fecha = parsearFecha(fechaStr);
            if (fecha == null) {
                System.out.println("Por favor ingrese una fecha valida.");
            }
        }
        
        System.out.print("Ubicacion: ");
        String ubicacion = scanner.nextLine();
        
        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine();
        
        Evento evento = new Evento(titulo, fecha, ubicacion, descripcion);
        gestor.agregarEvento(evento);
    }

    private static void buscarEvento() {
        System.out.print("Ingrese el titulo del evento a buscar: ");
        String titulo = scanner.nextLine();
        
        Evento evento = gestor.buscarEventoPorTitulo(titulo);
        if (evento != null) {
            System.out.println("Evento encontrado:");
            System.out.println(evento);
            System.out.println("Asistentes (" + evento.getAsistentes().size() + "):");
            for (Asistente asistente : evento.getAsistentes()) {
                System.out.println("  - " + asistente);
            }
        } 
        else {
            System.out.println("Evento no encontrado.");
        }
    }

    private static void editarEvento() {
        System.out.print("Ingrese el titulo del evento a editar: ");
        String titulo = scanner.nextLine();
        
        Evento evento = gestor.buscarEventoPorTitulo(titulo);
        if (evento == null) {
            System.out.println("Evento no encontrado.");
            return;
        }
        
        System.out.println("Evento actual: " + evento);
        System.out.println("Deje en blanco los campos que no desea modificar:");
        
        System.out.print("Nueva fecha (dd/MM/yyyy HH:mm): ");
        String fechaStr = scanner.nextLine();
        Date nuevaFecha = fechaStr.trim().isEmpty() ? null : parsearFecha(fechaStr);
        
        System.out.print("Nueva ubicacion: ");
        String nuevaUbicacion = scanner.nextLine();
        if (nuevaUbicacion.trim().isEmpty()) nuevaUbicacion = null;
        
        System.out.print("Nueva descripcion: ");
        String nuevaDescripcion = scanner.nextLine();
        if (nuevaDescripcion.trim().isEmpty()) nuevaDescripcion = null;
        
        gestor.editarEvento(titulo, nuevaFecha, nuevaUbicacion, nuevaDescripcion);
    }

    private static void eliminarEvento() {
        System.out.print("Ingrese el titulo del evento a eliminar: ");
        String titulo = scanner.nextLine();
        gestor.eliminarEvento(titulo);
    }

    private static void agregarAsistenteAEvento() {
        System.out.print("Ingrese el titulo del evento: ");
        String tituloEvento = scanner.nextLine();
        
        System.out.print("Nombre del asistente: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Email del asistente: ");
        String email = scanner.nextLine();
        
        Asistente asistente = new Asistente(nombre, email);
        gestor.agregarAsistenteAEvento(tituloEvento, asistente);
    }

    private static void guardarEventosEnArchivo() {
        ArchivoUtils.guardarEventos(gestor.getListaEventos());
    }

    private static void cargarEventosDesdeArchivo() {
        if (ArchivoUtils.existeArchivoEventos()) {
            // Cargar eventos del archivo y agregarlos al gestor existente
            for (Evento evento : ArchivoUtils.cargarEventos()) {
                gestor.agregarEvento(evento);
            }
        }
    }

    private static Date parsearFecha(String fechaStr) {
        try {
            return dateFormat.parse(fechaStr);
        } 
        catch (ParseException e) {
            System.out.println("Formato de fecha invalido. Use: dd/MM/yyyy HH:mm");
            return null;
        }
    }

    private static void mostrarCalendarioMensual() {
        Calendar cal = Calendar.getInstance();
        int mesActual = cal.get(Calendar.MONTH);
        int añoActual = cal.get(Calendar.YEAR);
        
        // Nombres de los meses
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        
        System.out.println("\n=== CALENDARIO - " + meses[mesActual] + " " + añoActual + " ===");
        System.out.println("  Do  Lu  Ma  Mi  Ju  Vi  Sa");
        
        // Primer día del mes
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int primerDiaSemana = cal.get(Calendar.DAY_OF_WEEK);
        int diasEnMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // Obtener eventos del mes actual
        List<Integer> diasConEventos = obtenerDiasConEventos(mesActual, añoActual);
        
        // Espacios antes del primer día
        for (int i = 1; i < primerDiaSemana; i++) {
            System.out.print("    ");
        }
        
        // Imprimir días del mes
        for (int dia = 1; dia <= diasEnMes; dia++) {
            if (diasConEventos.contains(dia)) {
                System.out.printf("[%2d]", dia);  // Días con eventos entre corchetes
            } else {
                System.out.printf(" %2d ", dia);  // Días normales
            }
            
            // Nueva línea después del sábado
            if ((dia + primerDiaSemana - 2) % 7 == 6) {
                System.out.println();
            }
        }
        
        System.out.println("\n\nLeyenda: [XX] = Días con eventos");
        
        // Mostrar resumen de eventos del mes
        mostrarEventosDelMes(mesActual, añoActual);
    }
    
    private static List<Integer> obtenerDiasConEventos(int mes, int año) {
        List<Integer> diasConEventos = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        
        for (Evento evento : gestor.getListaEventos()) {
            cal.setTime(evento.getFecha());
            int eventoMes = cal.get(Calendar.MONTH);
            int eventoAño = cal.get(Calendar.YEAR);
            
            if (eventoMes == mes && eventoAño == año) {
                int dia = cal.get(Calendar.DAY_OF_MONTH);
                if (!diasConEventos.contains(dia)) {
                    diasConEventos.add(dia);
                }
            }
        }
        
        return diasConEventos;
    }
    
    private static void mostrarEventosDelMes(int mes, int año) {
        Calendar cal = Calendar.getInstance();
        List<Evento> eventosDelMes = new ArrayList<>();
        
        for (Evento evento : gestor.getListaEventos()) {
            cal.setTime(evento.getFecha());
            int eventoMes = cal.get(Calendar.MONTH);
            int eventoAño = cal.get(Calendar.YEAR);
            
            if (eventoMes == mes && eventoAño == año) {
                eventosDelMes.add(evento);
            }
        }
        
        if (eventosDelMes.isEmpty()) {
            System.out.println("No hay eventos programados para este mes.");
        } else {
            System.out.println("\n=== EVENTOS DEL MES ===");
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            for (Evento evento : eventosDelMes) {
                System.out.println("• " + formato.format(evento.getFecha()) + " - " + evento.getTitulo());
            }
        }
    }
}