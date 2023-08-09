/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import controlador.DAO.AeropuertoDAO;
import controlador.DAO.VueloDAO;
import controlador.DAO.grafo.VueloGrafo;
import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.VacioException;
import javax.swing.JOptionPane;
import modelo.Aeropuerto;
import modelo.Vuelo;
import vista.grafos.FrmGrafo;
import vista.modeloTabla.ModeloTablaAeropuerto;
import vista.modeloTabla.ModeloTablaVuelo;
import vista.utilidades.UtilidadesVistaGrafo;

/**
 *
 * @author Bravo
 */
public class FrmAeropuerto extends javax.swing.JDialog {

    AeropuertoDAO ad = new AeropuertoDAO();
    VueloDAO vd = new VueloDAO();
    VueloGrafo vg = new VueloGrafo();
    ModeloTablaAeropuerto modeloA = new ModeloTablaAeropuerto();
    ModeloTablaVuelo modeloV = new ModeloTablaVuelo();

    /**
     * Creates new form FrmAeropuerto
     */
    public FrmAeropuerto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(this);
        cargarTabla();
        UtilidadesVistaGrafo.cargarComboAeropuertos(vg.getLista(), cbxo);
        UtilidadesVistaGrafo.cargarComboAeropuertos(vg.getLista(), cbxd);
        UtilidadesVistaGrafo.cargarComboAeropuertos(vg.getLista(), cbxOv);
        UtilidadesVistaGrafo.cargarComboAeropuertos(vg.getLista(), cbxDv);
        txtArea.setEditable(false);
        

    }

    private void cargarCombos() {
        try {
            UtilidadesVistaGrafo.cargarComboAeropuertos(vg.getLista(), cbxo);
            UtilidadesVistaGrafo.cargarComboAeropuertos(vg.getLista(), cbxd);
            UtilidadesVistaGrafo.cargarComboAeropuertos(vg.getLista(), cbxOv);
            UtilidadesVistaGrafo.cargarComboAeropuertos(vg.getLista(), cbxDv);
        } catch (Exception e) {
            // Manejo de excepciones
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtDuracion.setText("");
        txtCiudad.setText("");

    }

    private void cargarTabla() {
        modeloA.setLista(ad.listar());
        tblAeropuerto.setModel(modeloA);
        tblAeropuerto.updateUI();
        tblVuelos.setModel(modeloV);
        tblVuelos.updateUI();

    }

    private void mostrarAeropuertoTabla() {
        int fila = tblAeropuerto.getSelectedRow();
        if (fila >= 0) {
            try {
//                Aeropuerto aeropuertoSeleccionado = modeloA.getLista().get(fila);
//
//                ListaEnlazada<Vuelo> vuelosRelacionados = vd.listaPorAeropuerto(aeropuertoSeleccionado.getNombre());
//                modeloV.setLista(vuelosRelacionados);
                ad.setAeropuerto(modeloA.getLista().get(fila));
                modeloV.setLista(vd.listaPorAeropuerto(ad.getAeropuerto().getNombre()));
                tblVuelos.setModel(modeloV);
                tblVuelos.updateUI();
            } catch (Exception e) {
                System.out.println("Error Mejorar");
            }
        }
    }

    private void guardarDatos() {
        if (txtNombre.getText().isEmpty() || txtCiudad.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Llene los campos", "INFORMACION", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                ad.getAeropuerto().setNombre(txtNombre.getText());
                ad.getAeropuerto().setCiudad(txtCiudad.getText());
                ad.guardar();

                cargarTabla();
                cargarCombos();
                limpiar();
                JOptionPane.showMessageDialog(null, "Gracias por Preferirnos", "INFORMACION", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                // Manejo de excepciones
            }
        }
    }

    private void UnirDatos() {
        if (txtDuracion.getText().isEmpty() || cbxOv.getSelectedItem() == null || cbxDv.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Llene todos los campos", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            try {

                vd.getVuelo().setDuracion(Integer.parseInt(txtDuracion.getText()));
                vd.getVuelo().setOrigen(cbxOv.getSelectedItem().toString());
                vd.getVuelo().setDestino(cbxDv.getSelectedItem().toString());
                vd.guardar();
                cargarTabla();
                cargarCombos();
                limpiar();
                JOptionPane.showMessageDialog(null, "Se ha realizado la unión", "OK", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                // Manejo de excepciones
            }
        }
    }

    private void ejecutarAdyacencia() {
        try {
            Aeropuerto origen = UtilidadesVistaGrafo.obtenerComboAeropuerto(vg.getLista(), cbxo);
            Aeropuerto destino = UtilidadesVistaGrafo.obtenerComboAeropuerto(vg.getLista(), cbxd);

            if (origen == destino || destino == null) {
                txtArea.setText("Seleccione un origen y un destino válidos");
                return;
            }

            int indiceOrigen = vg.getGrafo().getVerticeNum(origen);
            int indiceDestino = vg.getGrafo().getVerticeNum(destino);

            long startTime = System.currentTimeMillis(); // Iniciar medición de tiempo

            ListaEnlazada<Integer> listaIndices = vg.getGrafo().camino(indiceOrigen, indiceDestino);

            long endTime = System.currentTimeMillis(); // Finalizar medición de tiempo
            long totalTime = endTime - startTime; // Calcular tiempo total en milisegundos

            ListaEnlazada<Aeropuerto> caminoAeropuertos = new ListaEnlazada<>();
            for (int i = 0; i < listaIndices.size(); i++) {
                int indice = listaIndices.get(i);
                caminoAeropuertos.insertar(vg.getGrafo().getEtiqueta(indice));
            }

            if (caminoAeropuertos.isEmpty()) {
                txtArea.setText("No existe camino");
            } else {
                StringBuilder caminoStr = new StringBuilder();
                for (int i = 0; i < caminoAeropuertos.size(); i++) {
                    Aeropuerto aeropuerto = caminoAeropuertos.get(i);
                    caminoStr.append(aeropuerto.getNombre());
                    if (i < caminoAeropuertos.size() - 1) {
                        caminoStr.append(" -> ");
                    }
                }
                txtArea.setText("Camino mínimo Adyacencia:\n" + caminoStr.toString());
                txtArea.append("\nTiempo de ejecución: " + totalTime + " ms");
            }
        } catch (Exception e) {
            txtArea.setText("Error al encontrar el camino");
            e.printStackTrace();
        }
    }

    private void ejecutarFloyd() {
        try {
            Aeropuerto origen = UtilidadesVistaGrafo.obtenerComboAeropuerto(vg.getLista(), cbxo);
            Aeropuerto destino = UtilidadesVistaGrafo.obtenerComboAeropuerto(vg.getLista(), cbxd);

            long startTime = System.currentTimeMillis(); // Iniciar medición de tiempo

            ListaEnlazada<Integer> caminoIndices = vg.getGrafo().obtenerCaminoFloyd(vg.getGrafo().getVerticeNum(origen), vg.getGrafo().getVerticeNum(destino));

            long endTime = System.currentTimeMillis(); // Finalizar medición de tiempo
            long totalTime = endTime - startTime; // Calcular tiempo total en milisegundos

            ListaEnlazada<Aeropuerto> caminoAeropuertos = new ListaEnlazada<>();
            for (int i = 0; i < caminoIndices.size(); i++) {
                int indice = caminoIndices.get(i);
                caminoAeropuertos.insertar(vg.getGrafo().getEtiqueta(indice));
            }

            StringBuilder caminoStr = new StringBuilder();
            for (int i = 0; i < caminoAeropuertos.size(); i++) {
                Aeropuerto aeropuerto = caminoAeropuertos.get(i);
                caminoStr.append(aeropuerto.getNombre());
                if (i < caminoAeropuertos.size() - 1) {
                    caminoStr.append(" -> ");
                }
            }

            txtArea.setText("Camino mínimo (Floyd):\n" + caminoStr.toString());
            txtArea.append("\nTiempo de ejecución: " + totalTime + " ms");
        } catch (Exception e) {
            txtArea.setText("No se pudo encontrar un camino mínimo");
            e.printStackTrace();
        }
    }

    private void ejecutarBellmanFord() {

        try {
            Aeropuerto origen = UtilidadesVistaGrafo.obtenerComboAeropuerto(vg.getLista(), cbxo);
            Aeropuerto destino = UtilidadesVistaGrafo.obtenerComboAeropuerto(vg.getLista(), cbxd);

            int indiceOrigen = vg.getGrafo().getVerticeNum(origen);
            int indiceDestino = vg.getGrafo().getVerticeNum(destino);

            long startTime = System.currentTimeMillis(); // Iniciar medición de tiempo

            ListaEnlazada<Integer> caminoIndices = vg.getGrafo().bellmanFord(indiceOrigen, indiceDestino);

            long endTime = System.currentTimeMillis(); // Finalizar medición de tiempo
            long totalTime = endTime - startTime; // Calcular tiempo total en milisegundos

            ListaEnlazada<Aeropuerto> caminoAeropuertos = new ListaEnlazada<>();
            for (int i = 0; i < caminoIndices.size(); i++) {
                int indice = caminoIndices.get(i);
                caminoAeropuertos.insertar(vg.getGrafo().getEtiqueta(indice));
            }

            StringBuilder caminoStr = new StringBuilder();
            for (int i = 0; i < caminoAeropuertos.size(); i++) {
                Aeropuerto aeropuerto = caminoAeropuertos.get(i);
                caminoStr.append(aeropuerto.getNombre());
                if (i < caminoAeropuertos.size() - 1) {
                    caminoStr.append(" -> ");
                }
            }
            txtArea.setText("Camino mínimo (Bellman-Ford):\n" + caminoStr.toString());
            txtArea.append("\nTiempo de ejecución: " + totalTime + " ms");

        } catch (Exception e) {
            txtArea.setText("No se pudo encontrar un camino mínimo");
            e.printStackTrace();
        }

    }

    private void mostrarGrafo() {
        new FrmGrafo(null, true, vg.getGrafo()).setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtCiudad = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtDuracion = new javax.swing.JTextField();
        cbxOv = new javax.swing.JComboBox<>();
        cbxDv = new javax.swing.JComboBox<>();
        btnUnir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnBellman = new javax.swing.JButton();
        btnFloyd = new javax.swing.JButton();
        btnAdyacente = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        cbxo = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cbxd = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAeropuerto = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVuelos = new javax.swing.JTable();
        btnVer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(131, 175, 155));

        jPanel1.setBackground(new java.awt.Color(200, 200, 169));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Aeropuerto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel3.setText("Nombre de Aeropuerto:");

        txtNombre.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        btnGuardar.setBackground(new java.awt.Color(249, 205, 173));
        btnGuardar.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel7.setText("Ciudad:");

        txtCiudad.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtCiudad.setFocusTraversalPolicyProvider(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtNombre))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCiudad)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(136, Short.MAX_VALUE)
                .addComponent(btnGuardar)
                .addGap(109, 109, 109))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuardar)
                .addGap(60, 60, 60))
        );

        jLabel3.getAccessibleContext().setAccessibleName("Nombre Aeropuerto:");

        jPanel4.setBackground(new java.awt.Color(200, 200, 169));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Vuelos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel8.setText("Origen:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel9.setText("Destino:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel10.setText("Duracion:");

        txtDuracion.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        cbxOv.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        cbxDv.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        btnUnir.setBackground(new java.awt.Color(249, 205, 173));
        btnUnir.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnUnir.setText("Unir");
        btnUnir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxDv, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUnir)
                        .addContainerGap(68, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(cbxOv, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbxOv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(cbxDv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnUnir)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(200, 200, 169));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Caminos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        btnBellman.setBackground(new java.awt.Color(249, 205, 173));
        btnBellman.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnBellman.setText("Bellman");
        btnBellman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBellmanActionPerformed(evt);
            }
        });

        btnFloyd.setBackground(new java.awt.Color(249, 205, 173));
        btnFloyd.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnFloyd.setText("Floyd");
        btnFloyd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFloydActionPerformed(evt);
            }
        });

        btnAdyacente.setBackground(new java.awt.Color(249, 205, 173));
        btnAdyacente.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnAdyacente.setText("Adyacente");
        btnAdyacente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdyacenteActionPerformed(evt);
            }
        });

        txtArea.setColumns(20);
        txtArea.setRows(5);
        jScrollPane3.setViewportView(txtArea);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel1.setText("Origen");

        cbxo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel2.setText("Destino");

        cbxd.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBellman, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btnAdyacente))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cbxd, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnFloyd, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(cbxo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBellman)
                            .addComponent(btnFloyd)
                            .addComponent(btnAdyacente))
                        .addGap(19, 19, 19))))
        );

        tblAeropuerto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblAeropuerto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblAeropuerto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAeropuertoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblAeropuerto);

        tblVuelos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblVuelos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblVuelos.setFocusTraversalPolicyProvider(true);
        jScrollPane1.setViewportView(tblVuelos);

        btnVer.setBackground(new java.awt.Color(249, 205, 173));
        btnVer.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnVer.setText("Ver");
        btnVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnVer))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(31, 31, 31)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVer)
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 600));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerActionPerformed
        mostrarGrafo();
    }//GEN-LAST:event_btnVerActionPerformed

    private void btnFloydActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFloydActionPerformed
        ejecutarFloyd();
    }//GEN-LAST:event_btnFloydActionPerformed

    private void btnBellmanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBellmanActionPerformed
        ejecutarBellmanFord();
    }//GEN-LAST:event_btnBellmanActionPerformed

    private void btnAdyacenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdyacenteActionPerformed
        ejecutarAdyacencia();
    }//GEN-LAST:event_btnAdyacenteActionPerformed

    private void tblAeropuertoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAeropuertoMouseClicked
        mostrarAeropuertoTabla();
    }//GEN-LAST:event_tblAeropuertoMouseClicked

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarDatos();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnUnirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnirActionPerformed
        UnirDatos();
    }//GEN-LAST:event_btnUnirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmAeropuerto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmAeropuerto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmAeropuerto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmAeropuerto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmAeropuerto dialog = new FrmAeropuerto(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdyacente;
    private javax.swing.JButton btnBellman;
    private javax.swing.JButton btnFloyd;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnUnir;
    private javax.swing.JButton btnVer;
    private javax.swing.JComboBox<String> cbxDv;
    private javax.swing.JComboBox<String> cbxOv;
    private javax.swing.JComboBox<String> cbxd;
    private javax.swing.JComboBox<String> cbxo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblAeropuerto;
    private javax.swing.JTable tblVuelos;
    private javax.swing.JTextArea txtArea;
    private javax.swing.JTextField txtCiudad;
    private javax.swing.JTextField txtDuracion;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
