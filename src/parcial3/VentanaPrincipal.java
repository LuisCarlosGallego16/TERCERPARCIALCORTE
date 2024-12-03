/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package parcial3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author luisc
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form VentanaPrincipal
     */
    DefaultTableModel modelo;

    public VentanaPrincipal() {
        initComponents();
        modelo = getModeloTabla();
    }

    public JTable getTabla() {
        return jTable1;
    }

    public DefaultTableModel getModeloTabla() {
        return (DefaultTableModel) getTabla().getModel();
    }

    public String getCampoIdentificacion() {
        return campoIdentificacion.getText();
    }

    public String getCampoNombre() {
        return campoNombre.getText();
    }

    public String getCampoCorreo() {
        return campoCorreo.getText();
    }

//METODO PARA GUARDAR ARCHIVO TXT
    public void guardarArchivoTXT(String nombreArchivo) {
        BufferedWriter bufferedWriter = null;

        try {
            File file = new File(nombreArchivo);
            bufferedWriter = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < modelo.getRowCount(); i++) {
                String identificacion = modelo.getValueAt(i, 0).toString();
                String nombre = modelo.getValueAt(i, 1).toString();
                String correo = modelo.getValueAt(i, 2).toString();

                bufferedWriter.write(identificacion + "," + nombre + "," + correo);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    //METODO PARA GUARDAR ARCHIVOS TXT 
    public void cargarArchivosTXT() {
        BufferedReader bufferedReader = null;

        try {
            File file = new File("C:\\Users\\luisc\\OneDrive\\Documentos\\NetBeansProjects\\Parcial3\\personas.txt");
            bufferedReader = new BufferedReader(new FileReader(file));
            String linea;

            modelo.setRowCount(0);

            while ((linea = bufferedReader.readLine()) != null) {
                String[] lista = linea.split(",");

                // Agregar la fila a la tabla (cada elemento de la lista corresponde a una columna)
                Object[] nuevaFila = {lista[0], lista[1], lista[2]};
                modelo.addRow(nuevaFila);
            }

        } catch (Exception e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception ex) {
                System.out.println("Error al cerrar el archivo: " + ex.getMessage());
            }
        }
    }

    //METODO PARA GUARDAR XML
    public void guardarXML(String nombreArchivo) {
        try {
            modelo = getModeloTabla();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = (Document) builder.newDocument();

            Element raiz = documento.createElement("personas");
            documento.appendChild(raiz);

            for (int i = 0; i < modelo.getRowCount(); i++) {
                Element persona = documento.createElement("persona");

                Element identificacion = documento.createElement("identificacion");
                identificacion.appendChild(documento.createTextNode(modelo.getValueAt(i, 0).toString()));
                persona.appendChild(identificacion);

                Element nombre = documento.createElement("nombre");
                nombre.appendChild(documento.createTextNode(modelo.getValueAt(i, 1).toString()));
                persona.appendChild(nombre);

                Element correo = documento.createElement("correo");
                correo.appendChild(documento.createTextNode(modelo.getValueAt(i, 2).toString()));
                persona.appendChild(correo);

                raiz.appendChild(persona);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource((Node) documento);
            StreamResult streamResult = new StreamResult(new File(nombreArchivo));
            transformer.transform(domSource, streamResult);

            System.out.println("Archivo XML generado correctamente: " + nombreArchivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //METODO PARA CARGAR DESDE ARCHIVO XML
    public void cargarDesdeXML(String nombreArchivo) {
        try {
            File archivo = new File(nombreArchivo);
            if (!archivo.exists()) {
                System.out.println("El archivo XML no existe. Se creará uno nuevo cuando se guarde.");
                return;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(archivo);
            NodeList listaPersonas = documento.getElementsByTagName("personas");
            modelo.setRowCount(0);

            for (int i = 0; i < listaPersonas.getLength(); i++) {
                Node personaNode = listaPersonas.item(i);

                if (personaNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element personas = (Element) personaNode;

                    String identificacion = personas.getElementsByTagName("identificacion").item(0).getTextContent();
                    String nombre = personas.getElementsByTagName("nombre").item(0).getTextContent();
                    String correo = personas.getElementsByTagName("correo").item(0).getTextContent();

                    modelo.addRow(new Object[]{identificacion, nombre, correo});
                }
            }

            System.out.println("Datos cargados desde el archivo XML.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los datos desde el archivo XML");
        }
    }

    
    //
    public void guardarArchivoJSON() {
        List<PersonaJSON>listaPersonas = new ArrayList<>();

        //Iterar sobre cada fila de la tabla para obtener los valores
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String identificacion = modelo.getValueAt(i, 0).toString();
            String nombre = modelo.getValueAt(i, 1).toString();
            String correo = modelo.getValueAt(i, 2).toString();
            
            //Creamos un objeto factura, con los datos que obtenemos de cada fila de la tabla.
            PersonaJSON personas = new PersonaJSON(identificacion , nombre , correo);
            listaPersonas.add(personas);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter file = new FileWriter("facturas.json")) {
            gson.toJson(listaPersonas, file);
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente en el archivo JSON");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "ERROR AL GUARDAR LOS DATOS EN EL ARCHIVO JSON ");
        }
    }
    
    public void cargarDatosJSON() {
    try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\luisc\\OneDrive\\Documentos\\NetBeansProjects\\TalleresPOO\\facturas.json"))) {
        Gson gson = new Gson();

        tipoPersonas = new TypeToken<List<PersonaJSON>>() {}.getType();

        List<PersonaJSON> personas = gson.fromJson(reader, tipoPersonas);

        // Limpiar el modelo de la tabla
        modelo.setRowCount(0);

        for (PersonaJSON personas : personas) {
            Object[] fila = {
                personas.getIdentificacion(),
                personas.getNombre(),
                personas.getCorreo(),

            };
            modelo.addRow(fila);
        }

        JOptionPane.showMessageDialog(this, "Datos cargados correctamente desde el archivo JSON.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar el archivo JSON: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    
    
    
    
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        campoIdentificacion = new javax.swing.JTextField();
        campoNombre = new javax.swing.JTextField();
        campoCorreo = new javax.swing.JTextField();
        botonGuardarTabla = new javax.swing.JButton();
        botonGuardarTXT = new javax.swing.JButton();
        botonCargarTXT = new javax.swing.JButton();
        botonGuardarXML = new javax.swing.JButton();
        botonCargarXML = new javax.swing.JButton();
        botonGuardarJSON = new javax.swing.JButton();
        botonCargarJSON = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IDENTIFICACION", "NOMBRE", "CORREO"
            }
        ));
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jLabel1.setText("FORMULARIO PARA REGISTRO DE PERSONAS");

        jLabel2.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel2.setText("IDENTIFICACION DE LA PERSONA;");

        jLabel3.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel3.setText("NOMBRE DE LA PERSONA;");

        jLabel4.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel4.setText("CORREO DE LA PERSONA;");

        campoCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoCorreoActionPerformed(evt);
            }
        });

        botonGuardarTabla.setText("GUARDAR");
        botonGuardarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarTablaActionPerformed(evt);
            }
        });

        botonGuardarTXT.setText("GUARDAR TXT");
        botonGuardarTXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarTXTActionPerformed(evt);
            }
        });

        botonCargarTXT.setText("CARGAR TXT");
        botonCargarTXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCargarTXTActionPerformed(evt);
            }
        });

        botonGuardarXML.setText("GUARDAR XML");
        botonGuardarXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarXMLActionPerformed(evt);
            }
        });

        botonCargarXML.setText("CARGAR XML");
        botonCargarXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCargarXMLActionPerformed(evt);
            }
        });

        botonGuardarJSON.setText("GUARDAR JSON");
        botonGuardarJSON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarJSONActionPerformed(evt);
            }
        });

        botonCargarJSON.setText("CARGAR JSON");
        botonCargarJSON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCargarJSONActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(151, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(2, 2, 2))
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(campoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(campoIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(campoCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(41, 41, 41)))
                        .addGap(145, 145, 145))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(botonGuardarTXT)
                            .addComponent(botonCargarTXT))
                        .addGap(143, 143, 143)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(botonCargarXML)
                            .addComponent(botonGuardarXML)
                            .addComponent(botonGuardarTabla))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonGuardarJSON)
                            .addComponent(botonCargarJSON))
                        .addGap(109, 109, 109))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(campoIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(campoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(campoCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(botonGuardarTabla)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonGuardarTXT)
                    .addComponent(botonGuardarXML)
                    .addComponent(botonGuardarJSON))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonCargarXML)
                    .addComponent(botonCargarTXT)
                    .addComponent(botonCargarJSON))
                .addGap(0, 20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void campoCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoCorreoActionPerformed

    private void botonGuardarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarTablaActionPerformed
        String identificacion = getCampoIdentificacion();
        String nombre = getCampoNombre();
        String correo = getCampoCorreo();
        if (identificacion.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "TODOS LOS CAMPOS TIENEN QUE ESTAR COMPLETOS!");
        } else {
            Object[] nuevaFila = {identificacion, nombre, correo};
            modelo.addRow(nuevaFila);
            campoIdentificacion.setText("");
            campoNombre.setText("");
            campoCorreo.setText("");

        }
    }//GEN-LAST:event_botonGuardarTablaActionPerformed

    private void botonGuardarTXTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarTXTActionPerformed
        String identificacion = getCampoIdentificacion();
        String nombre = getCampoNombre();
        String correo = getCampoCorreo();
        if (identificacion.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "TODOS LOS CAMPOS TIENEN QUE ESTAR COMPLETOS!");
        } else {
            Object[] nuevaFila = {identificacion, nombre, correo};
            modelo.addRow(nuevaFila);
            guardarArchivoTXT("personas.txt");
            campoIdentificacion.setText("");
            campoNombre.setText("");
            campoCorreo.setText("");

        }
    }//GEN-LAST:event_botonGuardarTXTActionPerformed

    private void botonCargarTXTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCargarTXTActionPerformed

        cargarArchivosTXT();

    }//GEN-LAST:event_botonCargarTXTActionPerformed

    private void botonGuardarXMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarXMLActionPerformed
        String identificacion = getCampoIdentificacion();
        String nombre = getCampoNombre();
        String correo = getCampoCorreo();
        if (identificacion.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "TODOS LOS CAMPOS TIENEN QUE ESTAR COMPLETOS!");
        } else {
            Object[] nuevaFila = {identificacion, nombre, correo};
            modelo.addRow(nuevaFila);
            guardarXML("personas.xml");
            campoIdentificacion.setText("");
            campoNombre.setText("");
            campoCorreo.setText("");

        }
    }//GEN-LAST:event_botonGuardarXMLActionPerformed

    private void botonCargarXMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCargarXMLActionPerformed
        cargarDesdeXML("personas.xml");





    }//GEN-LAST:event_botonCargarXMLActionPerformed

    private void botonGuardarJSONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarJSONActionPerformed
    guardarArchivoJSON();

    }//GEN-LAST:event_botonGuardarJSONActionPerformed

    private void botonCargarJSONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCargarJSONActionPerformed

        cargarDatosJSON();

    }//GEN-LAST:event_botonCargarJSONActionPerformed

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
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCargarJSON;
    private javax.swing.JButton botonCargarTXT;
    private javax.swing.JButton botonCargarXML;
    private javax.swing.JButton botonGuardarJSON;
    private javax.swing.JButton botonGuardarTXT;
    private javax.swing.JButton botonGuardarTabla;
    private javax.swing.JButton botonGuardarXML;
    private javax.swing.JTextField campoCorreo;
    private javax.swing.JTextField campoIdentificacion;
    private javax.swing.JTextField campoNombre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

 

