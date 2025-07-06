package ui;

import modelo.Evento;
import modelo.Asistente;
import gestor.GestorEventos;
import gestor.ArchivoUtils;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class GestorEventosGUI {
    private GestorEventos gestor;
    private SimpleDateFormat dateFormat;
    private JFrame ventanaGUI;
    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;
    
    public GestorEventosGUI() {
        gestor = new GestorEventos();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }
    
    public void iniciar() {
        cargarEventosDesdeArchivo();
        
        crearInterfazVisual();
    }
    
    private void crearInterfazVisual() {
        ventanaGUI = new JFrame("TP POO - Gestor de Eventos - Christian Sosa");
        ventanaGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaGUI.setSize(900, 600);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        JLabel titulo = new JLabel("GESTOR DE EVENTOS", SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new BorderLayout());
        
        String[] columnas = {"Título", "Fecha", "Ubicación", "Descripción", "Asistentes"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEventos = new JTable(modeloTabla);
        tablaEventos.setRowHeight(25);

        tablaEventos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    mostrarDetallesEvento();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaEventos);
        scrollPane.setPreferredSize(new Dimension(800, 350));
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        
        JButton btnAgregar = new JButton("Agregar Evento");
        JButton btnEliminar = new JButton("Eliminar Evento");
        JButton btnBuscar = new JButton("Buscar Evento");
        JButton btnAgregarAsistente = new JButton("Agregar Asistente");
        JButton btnCalendario = new JButton("Ver Calendario");
        JButton btnGuardar = new JButton("Guardar Todos");
        JButton btnCargar = new JButton("Cargar Todos");
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnAgregarAsistente);
        panelBotones.add(btnCalendario);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCargar);
        
        panelCentral.add(panelBotones, BorderLayout.SOUTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        
        JLabel labelInfo = new JLabel("Christian Agustin Sosa - 1202494 - Paradigma Orientado a Objetos - UADE - 2025", SwingConstants.CENTER);

        panelInfo.add(labelInfo);
        panelPrincipal.add(panelInfo, BorderLayout.SOUTH);
        
        ventanaGUI.add(panelPrincipal);
        
        btnAgregar.addActionListener(e -> agregarEvento());
        btnEliminar.addActionListener(e -> eliminarEvento());
        btnBuscar.addActionListener(e -> buscarEvento());
        btnAgregarAsistente.addActionListener(e -> agregarAsistente());
        btnCalendario.addActionListener(e -> mostrarCalendario());
        btnGuardar.addActionListener(e -> guardarEventos());
        btnCargar.addActionListener(e -> cargarEventos());
        
        actualizarTabla();
        ventanaGUI.setVisible(true);
    }
    
    private void agregarEvento() {
        JDialog dialog = new JDialog(ventanaGUI, "Agregar Nuevo Evento", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(ventanaGUI);
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Título:"));
        JTextField campoTitulo = new JTextField();
        panel.add(campoTitulo);
        
        panel.add(new JLabel("Fecha (dd/MM/yyyy HH:mm):"));
        JTextField campoFecha = new JTextField();
        panel.add(campoFecha);
        
        panel.add(new JLabel("Ubicación:"));
        JTextField campoUbicacion = new JTextField();
        panel.add(campoUbicacion);
        
        panel.add(new JLabel("Descripción:"));
        JTextField campoDescripcion = new JTextField();
        panel.add(campoDescripcion);
        
        JButton btnAgregar = new JButton("Agregar");
        JButton btnCancelar = new JButton("Cancelar");
        
        panel.add(btnAgregar);
        panel.add(btnCancelar);
        
        dialog.add(panel);
        
        btnAgregar.addActionListener(e -> {
            String titulo = campoTitulo.getText().trim();
            String fechaStr = campoFecha.getText().trim();
            String ubicacion = campoUbicacion.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            
            if (titulo.isEmpty() || fechaStr.isEmpty() || ubicacion.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Faltan campos por completar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validacion de titulos iguales
            if (gestor.buscarEventoPorTitulo(titulo) != null) {
                JOptionPane.showMessageDialog(dialog, 
                    "Ya existe un evento con el titulo '" + titulo + "'. Por favor elija un titulo diferente.", 
                    "Evento Duplicado", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                Date fecha = dateFormat.parse(fechaStr);
                Evento evento = new Evento(titulo, fecha, ubicacion, descripcion);
                gestor.agregarEvento(evento);
                
                actualizarTabla();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(ventanaGUI, "Evento agregado correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Formato de fecha incorrecto. Use dd/MM/yyyy HH:mm", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void eliminarEvento() {
        int filaSeleccionada = tablaEventos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(ventanaGUI, "Por favor seleccione un evento de la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String titulo = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        
        int respuesta = JOptionPane.showConfirmDialog(ventanaGUI, 
            "¿Esta seguro de eliminar el evento '" + titulo + "'?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            gestor.eliminarEvento(titulo);
            actualizarTabla();
            JOptionPane.showMessageDialog(ventanaGUI, "Evento eliminado correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void buscarEvento() {
        String titulo = JOptionPane.showInputDialog(ventanaGUI, "Ingrese el titulo del evento a buscar:");
        if (titulo != null && !titulo.trim().isEmpty()) {
            Evento evento = gestor.buscarEventoPorTitulo(titulo.trim());
            if (evento != null) {
                StringBuilder info = new StringBuilder();
                info.append("EVENTO ENCONTRADO:\n\n");
                info.append("Titulo: ").append(evento.getTitulo()).append("\n");
                info.append("Fecha: ").append(dateFormat.format(evento.getFecha())).append("\n");
                info.append("Ubicacion: ").append(evento.getUbicacion()).append("\n");
                info.append("Descripcion: ").append(evento.getDescripcion()).append("\n");
                info.append("Cantidad de Asistentes: ").append(evento.getAsistentes().size()).append("\n\n");
                
                if (!evento.getAsistentes().isEmpty()) {
                    info.append("LISTA DE ASISTENTES:\n");
                    for (Asistente asistente : evento.getAsistentes()) {
                        info.append("• ").append(asistente.getNombre())
                           .append(" (").append(asistente.getEmail()).append(")\n");
                    }
                } 
                else {
                    info.append("No hay asistentes registrados para este evento.");
                }
                
                JOptionPane.showMessageDialog(ventanaGUI, info.toString(), "Evento Encontrado", JOptionPane.INFORMATION_MESSAGE);
            } 
            else {
                JOptionPane.showMessageDialog(ventanaGUI, "No se encontro el evento", "No encontrado", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void agregarAsistente() {
        int filaSeleccionada = tablaEventos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(ventanaGUI, "Por favor seleccione un evento de la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String tituloEvento = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        
        JDialog dialog = new JDialog(ventanaGUI, "Agregar Asistente", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(ventanaGUI);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Evento:"));
        JLabel labelEvento = new JLabel(tituloEvento);
        panel.add(labelEvento);
        
        panel.add(new JLabel("Nombre del Asistente:"));
        JTextField campoNombre = new JTextField();
        panel.add(campoNombre);
        
        panel.add(new JLabel("Email del Asistente:"));
        JTextField campoEmail = new JTextField();
        panel.add(campoEmail);
        
        JButton btnAgregar = new JButton("Agregar");
        JButton btnCancelar = new JButton("Cancelar");
        
        panel.add(btnAgregar);
        panel.add(btnCancelar);
        
        dialog.add(panel);
        
        btnAgregar.addActionListener(e -> {
            String nombre = campoNombre.getText().trim();
            String email = campoEmail.getText().trim();
            
            if (nombre.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
                
            Asistente asistente = new Asistente(nombre, email);
            gestor.agregarAsistenteAEvento(tituloEvento, asistente);
            
            actualizarTabla();
            dialog.dispose();
            
            JOptionPane.showMessageDialog(ventanaGUI, "Asistente agregado!", "Listo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void guardarEventos() {
        ArchivoUtils.guardarEventos(gestor.getListaEventos());
        JOptionPane.showMessageDialog(ventanaGUI, "Eventos guardados en archivo", "Exito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cargarEventos() {
        if (ArchivoUtils.hayArchivo()) {
            List<Evento> eventos = ArchivoUtils.cargarEventos();
            for (Evento evento : eventos) {
                gestor.agregarEvento(evento);
            }
            actualizarTabla();
            JOptionPane.showMessageDialog(ventanaGUI, "Eventos cargados desde archivo", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } 
        else {
            JOptionPane.showMessageDialog(ventanaGUI, "No se encontró archivo de eventos", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void cargarEventosDesdeArchivo() {
        if (ArchivoUtils.hayArchivo()) {
            List<Evento> eventos = ArchivoUtils.cargarEventos();
            for (Evento evento : eventos) {
                gestor.agregarEvento(evento);
            }
        }
    }
    
    private void mostrarCalendario() {
        Calendar cal = Calendar.getInstance();
        int mes = cal.get(Calendar.MONTH);
        int año = cal.get(Calendar.YEAR);
        
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        
        JDialog calendarioDialog = new JDialog(ventanaGUI, "Calendario - " + meses[mes] + " " + año, true);
        calendarioDialog.setSize(600, 450);
        calendarioDialog.setLocationRelativeTo(ventanaGUI);
        
        JPanel panelCalendario = new JPanel(new BorderLayout());
        
        JLabel tituloCalendario = new JLabel("Calendario - " + meses[mes] + " " + año, SwingConstants.CENTER);
        tituloCalendario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCalendario.add(tituloCalendario, BorderLayout.NORTH);
        
        JPanel calendarioVisual = crearCalendarioVisual(mes, año);
        panelCalendario.add(calendarioVisual, BorderLayout.CENTER);
        
        JPanel panelEventos = new JPanel(new BorderLayout());
        panelEventos.setBorder(BorderFactory.createTitledBorder("Eventos del Mes"));
        
        JTextArea areaEventos = new JTextArea(6, 40);
        areaEventos.setEditable(false);
        areaEventos.setBackground(new Color(248, 249, 250));
        
        StringBuilder eventosTexto = new StringBuilder();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (Evento evento : gestor.getListaEventos()) {
            Calendar calEvento = Calendar.getInstance();
            calEvento.setTime(evento.getFecha());
            
            if (calEvento.get(Calendar.MONTH) == mes && calEvento.get(Calendar.YEAR) == año) {
                eventosTexto.append("• ").append(formato.format(evento.getFecha()))
                          .append(" - ").append(evento.getTitulo())
                          .append(" (").append(evento.getUbicacion()).append(")\n");
            }
        }
        
        if (eventosTexto.length() == 0) {
            eventosTexto.append("No hay eventos programados para este mes.");
        }
        
        areaEventos.setText(eventosTexto.toString());
        panelEventos.add(new JScrollPane(areaEventos), BorderLayout.CENTER);
        
        panelCalendario.add(panelEventos, BorderLayout.SOUTH);
        
        calendarioDialog.add(panelCalendario);
        calendarioDialog.setVisible(true);
    }
    
    private JPanel crearCalendarioVisual(int mes, int año) {
        JPanel panel = new JPanel(new GridLayout(0, 7, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] diasSemana = {"Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab"};
        for (String dia : diasSemana) {
            JLabel label = new JLabel(dia, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setBackground(new Color(70, 130, 180));
            label.setForeground(Color.WHITE);
            label.setOpaque(true);
            label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            label.setPreferredSize(new Dimension(80, 40));
            panel.add(label);
        }
           
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, año);
        cal.set(Calendar.MONTH, mes);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        int primerDiaSemana = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int diasEnMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        List<Integer> diasConEventos = obtenerDiasConEventos(mes, año);

        for (int i = 0; i < primerDiaSemana; i++) {
            JLabel labelVacio = new JLabel("", SwingConstants.CENTER);
            labelVacio.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            labelVacio.setOpaque(true);
            labelVacio.setBackground(Color.WHITE);
            labelVacio.setPreferredSize(new Dimension(80, 40));
            panel.add(labelVacio);
        }
        
        for (int dia = 1; dia <= diasEnMes; dia++) {
            JLabel labelDia = new JLabel(String.valueOf(dia), SwingConstants.CENTER);
            labelDia.setFont(new Font("Arial", Font.PLAIN, 14));
            labelDia.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            labelDia.setOpaque(true);
            labelDia.setPreferredSize(new Dimension(80, 40));
            
            if (diasConEventos.contains(dia)) {
                labelDia.setBackground(new Color(144, 238, 144));
                labelDia.setForeground(Color.BLACK);
                labelDia.setToolTipText("Hay eventos en este dia");
            } 
            else {
                labelDia.setBackground(Color.WHITE);
                labelDia.setForeground(Color.BLACK);
            }
            
            panel.add(labelDia);
        }
        
        return panel;
    }
    
    private List<Integer> obtenerDiasConEventos(int mes, int año) {
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
    
    private void mostrarDetallesEvento() {
        int filaSeleccionada = tablaEventos.getSelectedRow();
        if (filaSeleccionada == -1) {
            return;
        }
        
        String titulo = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        Evento evento = gestor.buscarEventoPorTitulo(titulo);
        
        if (evento != null) {
            JDialog detallesDialog = new JDialog(ventanaGUI, "Detalles del Evento", true);
            detallesDialog.setSize(500, 400);
            detallesDialog.setLocationRelativeTo(ventanaGUI);
            
            JPanel panel = new JPanel(new BorderLayout());
            
            JPanel panelInfo = new JPanel();
            panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
            panelInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
            
            JLabel labelTitulo = new JLabel("Título: " + evento.getTitulo());
            panelInfo.add(labelTitulo);
            
            panelInfo.add(Box.createVerticalStrut(10));
            panelInfo.add(new JLabel("Fecha: " + dateFormat.format(evento.getFecha())));
            panelInfo.add(new JLabel("Ubicación: " + evento.getUbicacion()));
            panelInfo.add(new JLabel("Descripción: " + evento.getDescripcion()));
            panelInfo.add(new JLabel("Cantidad de Asistentes: " + evento.getAsistentes().size()));
            
            panel.add(panelInfo, BorderLayout.NORTH);
            
            JPanel panelAsistentes = new JPanel(new BorderLayout());
            panelAsistentes.setBorder(BorderFactory.createTitledBorder("Lista de Asistentes"));
            
            if (!evento.getAsistentes().isEmpty()) {
                JTextArea areaAsistentes = new JTextArea(10, 40);
                areaAsistentes.setEditable(false);
                areaAsistentes.setBackground(new Color(248, 249, 250));
                
                StringBuilder asistentesTexto = new StringBuilder();
                for (int i = 0; i < evento.getAsistentes().size(); i++) {
                    Asistente asistente = evento.getAsistentes().get(i);
                    asistentesTexto.append((i + 1)).append(". ")
                                  .append(asistente.getNombre())
                                  .append(" - ").append(asistente.getEmail()).append("\n");
                }
                
                areaAsistentes.setText(asistentesTexto.toString());
                panelAsistentes.add(new JScrollPane(areaAsistentes), BorderLayout.CENTER);
            } 
            else {
                JLabel labelSinAsistentes = new JLabel("No hay asistentes registrados para este evento.", SwingConstants.CENTER);
                panelAsistentes.add(labelSinAsistentes, BorderLayout.CENTER);
            }
            
            panel.add(panelAsistentes, BorderLayout.CENTER);
            
            JPanel panelBoton = new JPanel(new FlowLayout());
            JButton btnCerrar = new JButton("Cerrar");
            btnCerrar.addActionListener(e -> detallesDialog.dispose());
            panelBoton.add(btnCerrar);
            
            panel.add(panelBoton, BorderLayout.SOUTH);
            
            detallesDialog.add(panel);
            detallesDialog.setVisible(true);
        }
    }
    
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        
        for (Evento evento : gestor.getListaEventos()) {
            Object[] fila = {
                evento.getTitulo(),
                dateFormat.format(evento.getFecha()),
                evento.getUbicacion(),
                evento.getDescripcion(),
                evento.getAsistentes().size() + " asistente(s)"
            };
            modeloTabla.addRow(fila);
        }

    }
} 