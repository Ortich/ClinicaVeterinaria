/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Usuario
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    // Variables de conexion a la base de datos
    private Statement estado;
    private ResultSet resultadoConsulta;
    private Connection conexion;
    
    //Variables para almacenar datos
    HashMap<String, Mascota> listaMascota = new HashMap();
    HashMap<String, Cliente> listaCliente = new HashMap();
    HashMap<String, Veterinario> listaVeterinario = new HashMap();
    HashMap<String, Cita> listaCita = new HashMap();
    
    //Variables de datos cliente
    
    String dni;
    String nombre;
    String apellido;
    int telefono;
    String direccion;
    int postal;
    String email;
    String poblacion;
    
   //Variables de datos mascota
    
    int chip;
    String nombreM;
    int sexo;
    String especie;
    String raza;
    Date nacimiento;
    String cliente;
    
    
    // Metodos
    private void escribeDatos(){
	Mascota m = listaMascota.get(String.valueOf(1));
	if(m != null){
	    cuadroChip.setText(String.valueOf(m.chip));
	    cuadroNombre.setText(m.nombre);
	    cuadroSexo.setText(devuelveSexo(m.Sexo));
	    cuadroEspecie.setText(m.especie);
	    cuadroRaza.setText(m.raza);
	    cuadroNacimiento.setText(m.fecha_nacimiento);
	    cuadroCliente.setText(m.cliente);
	}
    }
    
    private String devuelveSexo(int n){
	switch(n){
	    case 0:
		return "Hembra";
	    case 1:
		return "Macho";
	    case 2:
		return "Hermafrodita";
	    default:
		return "No determinado";
	}
    }
    
    //Inserción de datos de cliente en la BBDD.
    
    public void insertaDatos(String dni, String nombre, String apellido, int telefono, String direccion, int postal){
      try {
	    Class.forName("com.mysql.jdbc.Driver");
	    conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
	    estado = conexion.createStatement();
	    // Clientes
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cliente");
            
            
            estado.executeUpdate("INSERT INTO clinicaufvet.cliente VALUES('"+dni+"','"+nombre+"', '"+apellido+"', '"+direccion+"',"+postal+","+telefono+")");
      }
      catch (Exception e) {
	    e.getMessage();
	}
    
    }
    
    //Inserción de datos de mascota en la BBDD.
    
    public void insertaDatosM(int chip, String nombreM, int sexo, String especie, String raza, Date nacimiento, String cliente){
         try {
	    Class.forName("com.mysql.jdbc.Driver");
	    conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
	    estado = conexion.createStatement();
	    // Mascotas
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.mascota");
            
            
            estado.executeUpdate("INSERT INTO clinicaufvet.mascota VALUES("+chip+",'"+nombreM+"', "+sexo+", '"+especie+"','"+raza+"','"+nacimiento+"','"+cliente+"')");
      }
      catch (Exception e) {
	    e.getMessage();
	}
    
    }
    
    
    
    
    
    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
	initComponents();
	this.setSize(1035, 700);
	this.setResizable(false);
	
	ventanaMascotaNueva.setSize(1030, 700);
	ventanaMascotaNueva.setResizable(false);
	
	ventanaClienteNuevo.setSize(1035, 700);
	ventanaClienteNuevo.setResizable(false);
	
	/* ---------------- Conexiones a la base de datos ------------------- */
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
	    estado = conexion.createStatement();
	    // Mascotas
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.mascota");
            
            
           // estado.executeUpdate("INSERT INTO clinicaufvet.cliente VALUES('"+dni+"','"+nombre+"', '"+apellido+"', '"+direccion+"',"+postal+","+telefono+")");
            
              
            
            
	    // Almacena la consulta en un HashMap
	    while (resultadoConsulta.next()) {
		Mascota m = new Mascota();
		m.chip = resultadoConsulta.getInt(1);
		m.nombre = resultadoConsulta.getString(2);
		m.Sexo = resultadoConsulta.getInt(3);
		m.especie = resultadoConsulta.getString(4);
		m.raza = resultadoConsulta.getString(5);
		m.fecha_nacimiento = resultadoConsulta.getString(6);
		m.cliente = resultadoConsulta.getString(7);
		listaMascota.put(resultadoConsulta.getString(1), m);
	    }

	    // Clientes
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cliente");
	    // Almacena la consulta en un HashMap
	    while (resultadoConsulta.next()) {
		Cliente c = new Cliente();
		c.dni = resultadoConsulta.getString(1);
		c.nombre = resultadoConsulta.getString(2);
		c.apellido = resultadoConsulta.getString(3);
		c.direccion = resultadoConsulta.getString(4);
		c.cp = resultadoConsulta.getInt(5);
		c.telefono = resultadoConsulta.getInt(6);
		listaCliente.put(resultadoConsulta.getString(1), c);
	    }

	    // Veterinarios
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.veterinario");
	    // Almacena la consulta en un HashMap
	    while (resultadoConsulta.next()) {
		Veterinario v = new Veterinario();
		v.dni = resultadoConsulta.getString(1);
		v.nombre = resultadoConsulta.getString(2);
		v.apellido = resultadoConsulta.getString(3);
		v.pass = resultadoConsulta.getString(4);
		listaVeterinario.put(resultadoConsulta.getString(1), v);
	    }

	    // Citas
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cita");
	    // Almacena la consulta en un HashMap
	    while (resultadoConsulta.next()) {
		Cita cita = new Cita();
		cita.id = resultadoConsulta.getInt(1);
		cita.fecha_cita = resultadoConsulta.getString(2);
		cita.descripcion = resultadoConsulta.getString(3);
		cita.mascota = resultadoConsulta.getInt(4);
		cita.veterinario = resultadoConsulta.getString(5);
		listaCita.put(resultadoConsulta.getString(1), cita);
	    }
	} catch (Exception e) {
	    e.getMessage();
	}
	escribeDatos();
	
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ventanaMascotaNueva = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jTextField7 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        ventanaClienteNuevo = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        huella = new javax.swing.JLabel();
        cuadroNuevaMascota = new javax.swing.JLabel();
        cuadroNombre = new javax.swing.JLabel();
        cuadroRaza = new javax.swing.JLabel();
        cuadroEspecie = new javax.swing.JLabel();
        cuadroSexo = new javax.swing.JLabel();
        cuadroNacimiento = new javax.swing.JLabel();
        cuadroChip = new javax.swing.JLabel();
        cuadroCliente = new javax.swing.JLabel();
        cuadroFoto = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        fondo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        ventanaMascotaNueva.setSize(new java.awt.Dimension(1020, 850));
        ventanaMascotaNueva.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setOpaque(false);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(1030, 700));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField7.setBackground(new java.awt.Color(204, 204, 255));
        jTextField7.setText("jTextField7");
        jPanel4.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 547, 440, 35));

        jTextField10.setBackground(new java.awt.Color(204, 204, 255));
        jTextField10.setText("jTextField10");
        jPanel4.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(369, 758, 441, 35));

        jTextField8.setBackground(new java.awt.Color(204, 204, 255));
        jTextField8.setText("jTextField8");
        jPanel4.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 625, 440, 35));

        jTextField6.setBackground(new java.awt.Color(204, 204, 255));
        jTextField6.setText("jTextField6");
        jPanel4.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 472, 440, 35));

        jTextField3.setBackground(new java.awt.Color(204, 204, 255));
        jTextField3.setText("jTextField3");
        jPanel4.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 254, 440, 35));

        jTextField4.setBackground(new java.awt.Color(204, 204, 255));
        jTextField4.setText("jTextField4");
        jPanel4.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 323, 440, 35));

        jTextField9.setBackground(new java.awt.Color(204, 204, 255));
        jTextField9.setText("jTextField9");
        jPanel4.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(369, 691, 440, 35));

        jTextField5.setBackground(new java.awt.Color(204, 204, 255));
        jTextField5.setText("jTextField5");
        jPanel4.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 397, 440, 35));

        jLabel6.setText("jLabel6");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel6MousePressed(evt);
            }
        });
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 860, 220, 60));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pgDatosMascota.png"))); // NOI18N
        jPanel4.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1004, 1006));

        jScrollPane3.setViewportView(jPanel4);

        ventanaMascotaNueva.getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 610));

        ventanaClienteNuevo.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane4.setPreferredSize(new java.awt.Dimension(1024, 800));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField11.setBackground(new java.awt.Color(204, 204, 255));
        jTextField11.setText("jTextField3");
        jPanel5.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(367, 256, 440, 35));

        jTextField12.setBackground(new java.awt.Color(204, 204, 255));
        jTextField12.setText("jTextField4");
        jPanel5.add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 323, 440, 35));

        jTextField13.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.add(jTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 399, 440, 35));

        jTextField14.setBackground(new java.awt.Color(204, 204, 255));
        jTextField14.setText("jTextField6");
        jPanel5.add(jTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 473, 440, 35));

        jTextField15.setBackground(new java.awt.Color(204, 204, 255));
        jTextField15.setText("jTextField7");
        jPanel5.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 548, 440, 35));

        jTextField16.setBackground(new java.awt.Color(204, 204, 255));
        jTextField16.setText("jTextField8");
        jPanel5.add(jTextField16, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 624, 440, 35));

        jTextField17.setBackground(new java.awt.Color(204, 204, 255));
        jTextField17.setText("jTextField9");
        jPanel5.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 690, 440, 35));

        jTextField18.setBackground(new java.awt.Color(204, 204, 255));
        jTextField18.setText("jTextField10");
        jPanel5.add(jTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 760, 440, 35));

        jLabel5.setText("jLabel5");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel5MousePressed(evt);
            }
        });
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 864, 220, 50));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pgDatosCliente.png"))); // NOI18N
        jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2, 1000, 1000));

        jScrollPane4.setViewportView(jPanel5);

        ventanaClienteNuevo.getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 460));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1010, 700));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane2.setMinimumSize(new java.awt.Dimension(1012, 700));

        jPanel1.setMinimumSize(new java.awt.Dimension(1002, 700));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 800, 510, 110));

        huella.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/huella.png"))); // NOI18N
        huella.setText("jLabel22");
        jPanel1.add(huella, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 320, 90, 90));

        cuadroNuevaMascota.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNuevaMascota.setText("NUEVA MASCOTA");
        cuadroNuevaMascota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cuadroNuevaMascota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroNuevaMascotaMousePressed(evt);
            }
        });
        jPanel1.add(cuadroNuevaMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 240, 230, 50));

        cuadroNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNombre.setText("NOMBRE MASCOTA");
        cuadroNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(cuadroNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 380, 350, 80));

        cuadroRaza.setText("RAZA");
        cuadroRaza.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(cuadroRaza, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 600, 150, 30));

        cuadroEspecie.setText("ESPECIE");
        cuadroEspecie.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(cuadroEspecie, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 540, 150, 30));

        cuadroSexo.setText("SEXO");
        cuadroSexo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(cuadroSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 660, 160, 30));

        cuadroNacimiento.setText("FECHA NACIMIENTO");
        cuadroNacimiento.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(cuadroNacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 530, 160, 40));

        cuadroChip.setText("Nº CHIP");
        cuadroChip.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(cuadroChip, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 590, 160, 40));

        cuadroCliente.setText("PROPIETARIO");
        cuadroCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(cuadroCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 660, 160, 30));

        cuadroFoto.setText("FOTO (¿?)");
        cuadroFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(cuadroFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 350, 140, 140));

        jTextField1.setBackground(new java.awt.Color(204, 204, 255));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("BUSCAR MASCOTA");
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, 220, 50));

        fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pgMascotas.png"))); // NOI18N
        fondo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fondo.setMaximumSize(new java.awt.Dimension(400, 400));
        jPanel1.add(fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 970));

        jTabbedPane2.addTab("tab1", jPanel1);

        jPanel2.setMinimumSize(new java.awt.Dimension(1010, 700));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField2.setBackground(new java.awt.Color(204, 204, 255));
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("BUSCAR CLIENTE");
        jPanel2.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 260, 220, 50));

        jLabel1.setText("NUEVO CLIENTE");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
        });
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 260, 210, 50));

        jLabel2.setText("NOMBRE Y APELLIDOS");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 400, 590, 80));

        jLabel3.setText("DNI");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 550, 180, 40));

        jLabel4.setText("Nº Teléfono");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 550, 190, 40));

        jLabel16.setText("DIRECCIÓN (CALLE)");
        jLabel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 630, 520, 30));

        jLabel17.setText("POBLACIÓN");
        jLabel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 710, 190, 40));

        jLabel18.setText("CÓDIGO POSTAL");
        jLabel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 710, 190, 30));

        jLabel19.setText("EMAIL");
        jLabel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 780, 520, 40));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pgCliente.png"))); // NOI18N
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 4, 1010, 1000));

        jTabbedPane2.addTab("tab2", jPanel2);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jTabbedPane2.addTab("tab2", jPanel3);

        jScrollPane2.setViewportView(jTabbedPane2);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 1030, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cuadroNuevaMascotaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevaMascotaMousePressed
        ventanaMascotaNueva.setVisible(true);
    }//GEN-LAST:event_cuadroNuevaMascotaMousePressed

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        ventanaClienteNuevo.setVisible(true);
    }//GEN-LAST:event_jLabel1MousePressed

    private void jLabel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MousePressed
        
        dni=jTextField13.getText();
        nombre=jTextField11.getText();
        apellido=jTextField12.getText();
        telefono=Integer.valueOf(jTextField14.getText());
        direccion=jTextField15.getText();
        postal=Integer.valueOf(jTextField17.getText());
        email=jTextField18.getText();
        poblacion=jTextField16.getText();
        
      insertaDatos(dni,nombre,apellido,telefono,direccion,postal);

    }//GEN-LAST:event_jLabel5MousePressed

    private void jLabel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MousePressed
         chip=Integer.valueOf(jTextField8.getText());
         nombreM=jTextField3.getText();
         sexo=Integer.valueOf(jTextField6.getText());
         especie=jTextField4.getText();
         raza=jTextField5.getText();
         nacimiento=Date.valueOf(jTextField7.getText());
         cliente=jTextField9.getText();
         
         System.out.println(nacimiento);
         
         insertaDatosM(chip,nombreM,sexo,especie,raza,nacimiento,cliente);
    }//GEN-LAST:event_jLabel6MousePressed

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
    private javax.swing.JLabel cuadroChip;
    private javax.swing.JLabel cuadroCliente;
    private javax.swing.JLabel cuadroEspecie;
    private javax.swing.JLabel cuadroFoto;
    private javax.swing.JLabel cuadroNacimiento;
    private javax.swing.JLabel cuadroNombre;
    private javax.swing.JLabel cuadroNuevaMascota;
    private javax.swing.JLabel cuadroRaza;
    private javax.swing.JLabel cuadroSexo;
    private javax.swing.JLabel fondo;
    private javax.swing.JLabel huella;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JDialog ventanaClienteNuevo;
    private javax.swing.JDialog ventanaMascotaNueva;
    // End of variables declaration//GEN-END:variables
}
