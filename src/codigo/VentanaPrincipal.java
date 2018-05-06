/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
    ArrayList<Mascota> listaMascota = new ArrayList();
    ArrayList<Cliente> listaCliente = new ArrayList();
    ArrayList<Veterinario> listaVeterinario = new ArrayList();
    ArrayList<Cita> listaCita = new ArrayList();

    //Variables de datos cliente
    String dni;
    String nombre;
    String apellido;
    int telefono;
    String direccion;
    int postal;
    String poblacion;
    String email;

    //Variables de datos mascota
    int chip;
    String nombreM;
    int sexo;
    String especie;
    String raza;
    Date nacimiento;
    String cliente;

    //Variables de datos citas
    int id;
    Date fechaCita;
    String descripcion;
    int mascota;
    String veterinario;

    // Metodos
    private void escribeDatosMascota(int _chip) {
	Mascota m = listaMascota.get(0);
	for(int i = 0; i < listaMascota.size(); i++){
	    if(listaMascota.get(i).chip == _chip){
		m = listaMascota.get(i);
	    }
	}
	if (m != null) {
	    cuadroChip.setText(String.valueOf(m.chip));
	    cuadroNombre.setText(m.nombre);
	    cuadroSexo.setText(devuelveSexo(m.Sexo));
	    cuadroEspecie.setText(m.especie);
	    cuadroRaza.setText(m.raza);
	    cuadroNacimiento.setText(m.fecha_nacimiento);
	    cuadroCliente.setText(m.cliente);
	}
    }

    private String devuelveSexo(int n) {
	switch (n) {
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
    public void insertaDatos(String dni, String nombre, String apellido, int telefono, String direccion, int postal, String poblacion, String email) {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
	    estado = conexion.createStatement();
	    // Clientes
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cliente");

	    estado.executeUpdate("INSERT INTO clinicaufvet.cliente VALUES('" + dni + "','" + nombre + "', '" + apellido + "', '" + direccion + "'," + postal + "," + telefono + ",'" + poblacion + "','" + email + "')");
	} catch (Exception e) {
	    e.getMessage();
	}

    }

    //Inserción de datos de mascota en la BBDD.
    public void insertaDatosM(int chip, String nombreM, int sexo, String especie, String raza, Date nacimiento, String cliente) {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
	    estado = conexion.createStatement();
	    // Mascotas
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.mascota");

	    estado.executeUpdate("INSERT INTO clinicaufvet.mascota VALUES(" + chip + ",'" + nombreM + "', " + sexo + ", '" + especie + "','" + raza + "','" + nacimiento + "','" + cliente + "')");
	} catch (Exception e) {
	    e.getMessage();
	}

    }

    public void insertaDatosCita(int id, Date fechaCita, String descripcion, int mascota, String veterinario) {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
	    estado = conexion.createStatement();
	    // Mascotas
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.mascota");

	    estado.executeUpdate("INSERT INTO clinicaufvet.cita VALUES(" + id + ",'" + fechaCita + "', '" + descripcion + "', " + mascota + ",'" + veterinario + "')");
	} catch (Exception e) {
	    e.getMessage();
	}

    }

    public void buscaMascota(String busqueda) {
	DefaultTableModel model = (DefaultTableModel) tablaBusquedaMascota.getModel();
	model.getDataVector().removeAllElements();
	for (int i = 0; i < listaMascota.size(); i++) {
	    if (listaMascota.get(i).nombre.contains(busqueda)) {
		try {
		    String nombreCliente = "";
		    String apellidoCliente = "";
		    String dniCliente = listaMascota.get(i).cliente.toString();
		    for (int j = 0; j < listaCliente.size(); j++) {
			if (listaCliente.get(j).dni.toString().equals(dniCliente)) {
			    nombreCliente = listaCliente.get(j).nombre;
			    apellidoCliente = listaCliente.get(j).apellido;
			}
		    }
		    model.addRow(new Object[]{listaMascota.get(i).chip, listaMascota.get(i).nombre, nombreCliente + " " + apellidoCliente});
		} catch (Exception e) {
		    System.err.println(e.getMessage());
		}
	    }
	}
    }

    public void conexionBBDD() {
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
		listaMascota.add(m);
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
		c.poblacion = resultadoConsulta.getString(7);
		c.email = resultadoConsulta.getString(8);
		listaCliente.add(c);
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
		listaVeterinario.add(v);
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
		listaCita.add(cita);
	    }
	} catch (Exception e) {
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

	ventanaBusquedaMascota.setSize(500, 500);
	ventanaBusquedaMascota.setResizable(false);

	conexionBBDD();
	escribeDatosMascota(0);

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
        cuadroFNacimientoNM = new javax.swing.JTextField();
        cuadroFNAciemientoTNM = new javax.swing.JLabel();
        cuadroFotoNM = new javax.swing.JTextField();
        cuadroFotoTNM = new javax.swing.JLabel();
        cuadroChipNM = new javax.swing.JTextField();
        cuadroChipTNM = new javax.swing.JLabel();
        cuadroSexoNM = new javax.swing.JTextField();
        cuadroSexoTNM = new javax.swing.JLabel();
        cuadroNombreNM = new javax.swing.JTextField();
        cuadroNombreTNM = new javax.swing.JLabel();
        cuadroEspecieNM = new javax.swing.JTextField();
        cuadroEspecieTNM = new javax.swing.JLabel();
        cuadroPropietarioNM = new javax.swing.JTextField();
        cuadroPropietarioTNM = new javax.swing.JLabel();
        cuadroRazaNM = new javax.swing.JTextField();
        cuadroRazaTNM = new javax.swing.JLabel();
        botonGuardarNM = new javax.swing.JLabel();
        botonEditarNM = new javax.swing.JLabel();
        fondoNuevaMascota = new javax.swing.JLabel();
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
        fondoNC = new javax.swing.JLabel();
        ventanaBusquedaMascota = new javax.swing.JDialog();
        buttonBusquedaMascota = new javax.swing.JButton();
        fieldBusquedaMascota = new javax.swing.JTextField();
        scrollTablaBusquedaMascota = new javax.swing.JScrollPane();
        tablaBusquedaMascota = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        huella = new javax.swing.JLabel();
        cuadroNuevaMascota = new javax.swing.JLabel();
        cuadroNombre = new javax.swing.JLabel();
        cuadroNombre1 = new javax.swing.JLabel();
        cuadroRaza = new javax.swing.JLabel();
        cuadroRazaF = new javax.swing.JLabel();
        cuadroRazaT = new javax.swing.JLabel();
        cuadroEspecie = new javax.swing.JLabel();
        cuadroEspecieF = new javax.swing.JLabel();
        cuadroEspecieT = new javax.swing.JLabel();
        cuadroSexo = new javax.swing.JLabel();
        cuadroSexoF = new javax.swing.JLabel();
        cuadroSexoT = new javax.swing.JLabel();
        cuadroNacimiento = new javax.swing.JLabel();
        cuadroNacimientoF = new javax.swing.JLabel();
        cuadroNacimientoT = new javax.swing.JLabel();
        cuadroChip = new javax.swing.JLabel();
        cuadroChipF = new javax.swing.JLabel();
        cuadroChipT = new javax.swing.JLabel();
        cuadroCliente = new javax.swing.JLabel();
        cuadroClienteF = new javax.swing.JLabel();
        cuadroClienteT = new javax.swing.JLabel();
        cuadroFoto = new javax.swing.JLabel();
        insertaCita = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        fondoMascotas = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cuadroBusquedaCliente = new javax.swing.JTextField();
        cuadroNuevoCliente = new javax.swing.JLabel();
        cuadroNombreApellidos = new javax.swing.JLabel();
        cuadroNombreApellidosF = new javax.swing.JLabel();
        cuadroDNI = new javax.swing.JLabel();
        cuadroDNIF = new javax.swing.JLabel();
        cuadroDNIT = new javax.swing.JLabel();
        cuadroTelefono = new javax.swing.JLabel();
        cuadroTelefonoF = new javax.swing.JLabel();
        cuadroTelefonoT = new javax.swing.JLabel();
        cuadroDireccion = new javax.swing.JLabel();
        cuadroDireccionF = new javax.swing.JLabel();
        cuadroDireccionT = new javax.swing.JLabel();
        cuadroPoblacion = new javax.swing.JLabel();
        cuadroPoblacionF = new javax.swing.JLabel();
        cuadroPoblacionT = new javax.swing.JLabel();
        cuadroCPostal = new javax.swing.JLabel();
        cuadroCPostalF = new javax.swing.JLabel();
        cuadroCPostalT = new javax.swing.JLabel();
        cuadroEmail = new javax.swing.JLabel();
        cuadroEmailF = new javax.swing.JLabel();
        cuadroEmailT = new javax.swing.JLabel();
        fondoClientes = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        ventanaMascotaNueva.setSize(new java.awt.Dimension(1020, 850));
        ventanaMascotaNueva.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setOpaque(false);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(1030, 700));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cuadroFNacimientoNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroFNacimientoNM.setText("jTextField7");
        jPanel4.add(cuadroFNacimientoNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 547, 440, 35));

        cuadroFNAciemientoTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaNacimientoLetras.png"))); // NOI18N
        cuadroFNAciemientoTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroFNAciemientoTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroFNAciemientoTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 550, -1, -1));

        cuadroFotoNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroFotoNM.setText("jTextField10");
        jPanel4.add(cuadroFotoNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 760, 441, 30));

        cuadroFotoTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaFotoLetras.png"))); // NOI18N
        cuadroFotoTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroFotoTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroFotoTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 760, -1, -1));

        cuadroChipNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroChipNM.setText("jTextField8");
        jPanel4.add(cuadroChipNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 625, 440, 35));

        cuadroChipTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaChipLetras.png"))); // NOI18N
        cuadroChipTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroChipTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroChipTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 640, -1, -1));

        cuadroSexoNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroSexoNM.setText("jTextField6");
        jPanel4.add(cuadroSexoNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 472, 440, 35));

        cuadroSexoTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaSexoLetras.png"))); // NOI18N
        cuadroSexoTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroSexoTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroSexoTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 480, -1, -1));

        cuadroNombreNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroNombreNM.setText("jTextField3");
        jPanel4.add(cuadroNombreNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 254, 440, 35));

        cuadroNombreTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaNombreLetras.png"))); // NOI18N
        cuadroNombreTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroNombreTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroNombreTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 260, -1, -1));

        cuadroEspecieNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroEspecieNM.setText("jTextField4");
        jPanel4.add(cuadroEspecieNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 323, 440, 35));

        cuadroEspecieTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaEspecieLetras.png"))); // NOI18N
        cuadroEspecieTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroEspecieTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroEspecieTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, -1, -1));

        cuadroPropietarioNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroPropietarioNM.setText("jTextField9");
        jPanel4.add(cuadroPropietarioNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(369, 691, 440, 35));

        cuadroPropietarioTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaPopietarioLetras.png"))); // NOI18N
        cuadroPropietarioTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroPropietarioTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroPropietarioTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 690, -1, -1));

        cuadroRazaNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroRazaNM.setText("jTextField5");
        jPanel4.add(cuadroRazaNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 397, 440, 35));

        cuadroRazaTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaRazaLetras.png"))); // NOI18N
        cuadroRazaTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroRazaTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroRazaTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 400, -1, -1));

        botonGuardarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaGuardar.png"))); // NOI18N
        botonGuardarNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonGuardarNMMousePressed(evt);
            }
        });
        jPanel4.add(botonGuardarNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 860, -1, -1));

        botonEditarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaEditar.png"))); // NOI18N
        botonEditarNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonEditarNMMousePressed(evt);
            }
        });
        jPanel4.add(botonEditarNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 860, -1, -1));

        fondoNuevaMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascota.png"))); // NOI18N
        jPanel4.add(fondoNuevaMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1004, 1006));

        jScrollPane3.setViewportView(jPanel4);

        ventanaMascotaNueva.getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, 610));

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

        fondoNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosCliente.png"))); // NOI18N
        jPanel5.add(fondoNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2, 1000, 1000));

        jScrollPane4.setViewportView(jPanel5);

        ventanaClienteNuevo.getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 460));

        ventanaBusquedaMascota.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonBusquedaMascota.setText("Buscar");
        buttonBusquedaMascota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonBusquedaMascotaMouseClicked(evt);
            }
        });
        ventanaBusquedaMascota.getContentPane().add(buttonBusquedaMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, -1, -1));

        fieldBusquedaMascota.setText("Nombre Mascota");
        fieldBusquedaMascota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldBusquedaMascotaKeyPressed(evt);
            }
        });
        ventanaBusquedaMascota.getContentPane().add(fieldBusquedaMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 130, 30));

        tablaBusquedaMascota.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Chip", "Nombre", "Cliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollTablaBusquedaMascota.setViewportView(tablaBusquedaMascota);

        ventanaBusquedaMascota.getContentPane().add(scrollTablaBusquedaMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 370, 170));

        jButton1.setText("Aceptar");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });
        ventanaBusquedaMascota.getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

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

        huella.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/huella.png"))); // NOI18N
        huella.setText("jLabel22");
        jPanel1.add(huella, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, 90, 90));

        cuadroNuevaMascota.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNuevaMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasNuevaMascota.png"))); // NOI18N
        cuadroNuevaMascota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroNuevaMascotaMousePressed(evt);
            }
        });
        jPanel1.add(cuadroNuevaMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 220, -1, -1));

        cuadroNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNombre.setText("NOMBRE MASCOTA");
        jPanel1.add(cuadroNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 350, 350, 80));

        cuadroNombre1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNombre1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro2.png"))); // NOI18N
        jPanel1.add(cuadroNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 330, -1, -1));

        cuadroRaza.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroRaza.setText("RAZA");
        jPanel1.add(cuadroRaza, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 600, 150, 30));

        cuadroRazaF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        jPanel1.add(cuadroRazaF, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 590, -1, -1));

        cuadroRazaT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasRaza.png"))); // NOI18N
        jPanel1.add(cuadroRazaT, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 600, -1, -1));

        cuadroEspecie.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroEspecie.setText("ESPECIE");
        jPanel1.add(cuadroEspecie, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 540, 150, 30));

        cuadroEspecieF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        jPanel1.add(cuadroEspecieF, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 530, -1, -1));

        cuadroEspecieT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasEspecie.png"))); // NOI18N
        jPanel1.add(cuadroEspecieT, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 540, -1, -1));

        cuadroSexo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroSexo.setText("SEXO");
        jPanel1.add(cuadroSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 660, 150, 30));

        cuadroSexoF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        jPanel1.add(cuadroSexoF, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 650, -1, -1));

        cuadroSexoT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasSexo.png"))); // NOI18N
        jPanel1.add(cuadroSexoT, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 660, -1, -1));

        cuadroNacimiento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNacimiento.setText("FECHA NACIMIENTO");
        jPanel1.add(cuadroNacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 540, 160, 30));

        cuadroNacimientoF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        jPanel1.add(cuadroNacimientoF, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 530, -1, -1));

        cuadroNacimientoT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasFNacimiento.png"))); // NOI18N
        jPanel1.add(cuadroNacimientoT, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 540, -1, -1));

        cuadroChip.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroChip.setText("Nº CHIP");
        jPanel1.add(cuadroChip, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 610, 150, 30));

        cuadroChipF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        jPanel1.add(cuadroChipF, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 600, -1, -1));

        cuadroChipT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasChip.png"))); // NOI18N
        jPanel1.add(cuadroChipT, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 610, -1, -1));

        cuadroCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroCliente.setText("PROPIETARIO");
        jPanel1.add(cuadroCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 670, 150, 30));

        cuadroClienteF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        jPanel1.add(cuadroClienteF, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 660, -1, -1));

        cuadroClienteT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasPopietario.png"))); // NOI18N
        jPanel1.add(cuadroClienteT, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 660, -1, -1));

        cuadroFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasFoto.png"))); // NOI18N
        jPanel1.add(cuadroFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, -1, -1));

        insertaCita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                insertaCitaMousePressed(evt);
            }
        });
        jPanel1.add(insertaCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 744, 160, 40));

        jTextField1.setBackground(new java.awt.Color(204, 204, 255));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("BUSCAR MASCOTA");
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField1MousePressed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, 220, 50));

        fondoMascotas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotas.png"))); // NOI18N
        fondoMascotas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fondoMascotas.setMaximumSize(new java.awt.Dimension(400, 400));
        jPanel1.add(fondoMascotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 970));

        jTabbedPane2.addTab("tab1", jPanel1);

        jPanel2.setMinimumSize(new java.awt.Dimension(1010, 700));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cuadroBusquedaCliente.setBackground(new java.awt.Color(204, 204, 255));
        cuadroBusquedaCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cuadroBusquedaCliente.setText("BUSCAR CLIENTE");
        jPanel2.add(cuadroBusquedaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 260, 220, 50));

        cuadroNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonNuevoCliente.png"))); // NOI18N
        cuadroNuevoCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroNuevoClienteMousePressed(evt);
            }
        });
        jPanel2.add(cuadroNuevoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 240, -1, -1));

        cuadroNombreApellidos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNombreApellidos.setText("NOMBRE Y APELLIDOS");
        jPanel2.add(cuadroNombreApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 590, 80));

        cuadroNombreApellidosF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteNombreApellidos.png"))); // NOI18N
        jPanel2.add(cuadroNombreApellidosF, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 400, -1, -1));

        cuadroDNI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroDNI.setText("DNI");
        jPanel2.add(cuadroDNI, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 540, 180, 40));

        cuadroDNIF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteDNI.png"))); // NOI18N
        jPanel2.add(cuadroDNIF, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 540, -1, -1));

        cuadroDNIT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteDNILetras.png"))); // NOI18N
        jPanel2.add(cuadroDNIT, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 540, -1, -1));

        cuadroTelefono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroTelefono.setText("Nº Teléfono");
        jPanel2.add(cuadroTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 530, 190, 40));

        cuadroTelefonoF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroTelefonoF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteDNI.png"))); // NOI18N
        jPanel2.add(cuadroTelefonoF, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 530, -1, -1));

        cuadroTelefonoT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroTelefonoT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteTelefonoLetras.png"))); // NOI18N
        jPanel2.add(cuadroTelefonoT, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 530, -1, -1));

        cuadroDireccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroDireccion.setText("DIRECCIÓN (CALLE)");
        jPanel2.add(cuadroDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 610, 520, 30));

        cuadroDireccionF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroDireccionF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteDireccion.png"))); // NOI18N
        jPanel2.add(cuadroDireccionF, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 600, -1, -1));

        cuadroDireccionT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroDireccionT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteDireccionLetras.png"))); // NOI18N
        jPanel2.add(cuadroDireccionT, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 610, -1, -1));

        cuadroPoblacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroPoblacion.setText("POBLACIÓN");
        jPanel2.add(cuadroPoblacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 670, 160, 30));

        cuadroPoblacionF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroPoblacionF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClientePoblacion.png"))); // NOI18N
        jPanel2.add(cuadroPoblacionF, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 660, -1, -1));

        cuadroPoblacionT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroPoblacionT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClientePoblacionLetras.png"))); // NOI18N
        jPanel2.add(cuadroPoblacionT, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 670, -1, -1));

        cuadroCPostal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroCPostal.setText("CÓDIGO POSTAL");
        jPanel2.add(cuadroCPostal, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 660, 190, 30));

        cuadroCPostalF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroCPostalF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteCPostal.png"))); // NOI18N
        jPanel2.add(cuadroCPostalF, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 650, -1, -1));

        cuadroCPostalT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroCPostalT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteCPostalLetras.png"))); // NOI18N
        jPanel2.add(cuadroCPostalT, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 660, -1, -1));

        cuadroEmail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroEmail.setText("EMAIL");
        jPanel2.add(cuadroEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 740, 520, 40));

        cuadroEmailF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroEmailF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteEmail.png"))); // NOI18N
        jPanel2.add(cuadroEmailF, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 740, -1, -1));

        cuadroEmailT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroEmailT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteEmailLetras.png"))); // NOI18N
        jPanel2.add(cuadroEmailT, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 760, -1, -1));

        fondoClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgCliente.png"))); // NOI18N
        jPanel2.add(fondoClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 4, 1010, 1000));

        jTabbedPane2.addTab("tab2", jPanel2);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jTabbedPane2.addTab("tab2", jPanel3);

        jScrollPane2.setViewportView(jTabbedPane2);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 1030, 680));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cuadroNuevaMascotaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevaMascotaMousePressed
	ventanaMascotaNueva.setVisible(true);
    }//GEN-LAST:event_cuadroNuevaMascotaMousePressed

    private void cuadroNuevoClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevoClienteMousePressed
	ventanaClienteNuevo.setVisible(true);
    }//GEN-LAST:event_cuadroNuevoClienteMousePressed

    private void jLabel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MousePressed

	dni = jTextField13.getText();
	nombre = jTextField11.getText();
	apellido = jTextField12.getText();
	telefono = Integer.valueOf(jTextField14.getText());
	direccion = jTextField15.getText();
	postal = Integer.valueOf(jTextField17.getText());
	email = jTextField18.getText();
	poblacion = jTextField16.getText();

	insertaDatos(dni, nombre, apellido, telefono, direccion, postal, poblacion, email);

    }//GEN-LAST:event_jLabel5MousePressed

    private void botonGuardarNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGuardarNMMousePressed
	chip = Integer.valueOf(cuadroChipNM.getText());
	nombreM = cuadroNombreNM.getText();
	sexo = Integer.valueOf(cuadroSexoNM.getText());
	especie = cuadroEspecieNM.getText();
	raza = cuadroRazaNM.getText();
	nacimiento = Date.valueOf(cuadroFNacimientoNM.getText());
	cliente = cuadroPropietarioNM.getText();

	System.out.println(nacimiento);

	insertaDatosM(chip, nombreM, sexo, especie, raza, nacimiento, cliente);
    }//GEN-LAST:event_botonGuardarNMMousePressed

    private void insertaCitaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertaCitaMousePressed
	id = 0;
	//fechaCita=fechaCita.getTime();
	descripcion = "Descripción de prueba";
	mascota = 2;
	veterinario = "00000004V";

	insertaDatosCita(id, fechaCita, descripcion, mascota, veterinario);
    }//GEN-LAST:event_insertaCitaMousePressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
	if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
	    escribeDatosMascota(0);
	}
    }//GEN-LAST:event_jTextField1KeyPressed

    private void buttonBusquedaMascotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonBusquedaMascotaMouseClicked
	buscaMascota(fieldBusquedaMascota.getText());
    }//GEN-LAST:event_buttonBusquedaMascotaMouseClicked

    private void jTextField1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MousePressed
	ventanaBusquedaMascota.setVisible(true);
    }//GEN-LAST:event_jTextField1MousePressed

    private void fieldBusquedaMascotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldBusquedaMascotaKeyPressed
	if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
	    buscaMascota(fieldBusquedaMascota.getText());
	}
    }//GEN-LAST:event_fieldBusquedaMascotaKeyPressed

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        TableModel model = (TableModel)tablaBusquedaMascota.getModel();
	int chip = Integer.valueOf(model.getValueAt((tablaBusquedaMascota.getSelectedRow()), 0).toString());
	escribeDatosMascota(chip);
    }//GEN-LAST:event_jButton1MousePressed

    private void botonEditarNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEditarNMMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonEditarNMMousePressed

    private void cuadroFotoTNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroFotoTNMMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuadroFotoTNMMousePressed

    private void cuadroPropietarioTNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroPropietarioTNMMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuadroPropietarioTNMMousePressed

    private void cuadroEspecieTNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroEspecieTNMMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuadroEspecieTNMMousePressed

    private void cuadroNombreTNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNombreTNMMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuadroNombreTNMMousePressed

    private void cuadroSexoTNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroSexoTNMMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuadroSexoTNMMousePressed

    private void cuadroChipTNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroChipTNMMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuadroChipTNMMousePressed

    private void cuadroRazaTNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroRazaTNMMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuadroRazaTNMMousePressed

    private void cuadroFNAciemientoTNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroFNAciemientoTNMMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuadroFNAciemientoTNMMousePressed

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
    private javax.swing.JLabel botonEditarNM;
    private javax.swing.JLabel botonGuardarNM;
    private javax.swing.JButton buttonBusquedaMascota;
    private javax.swing.JTextField cuadroBusquedaCliente;
    private javax.swing.JLabel cuadroCPostal;
    private javax.swing.JLabel cuadroCPostalF;
    private javax.swing.JLabel cuadroCPostalT;
    private javax.swing.JLabel cuadroChip;
    private javax.swing.JLabel cuadroChipF;
    private javax.swing.JTextField cuadroChipNM;
    private javax.swing.JLabel cuadroChipT;
    private javax.swing.JLabel cuadroChipTNM;
    private javax.swing.JLabel cuadroCliente;
    private javax.swing.JLabel cuadroClienteF;
    private javax.swing.JLabel cuadroClienteT;
    private javax.swing.JLabel cuadroDNI;
    private javax.swing.JLabel cuadroDNIF;
    private javax.swing.JLabel cuadroDNIT;
    private javax.swing.JLabel cuadroDireccion;
    private javax.swing.JLabel cuadroDireccionF;
    private javax.swing.JLabel cuadroDireccionT;
    private javax.swing.JLabel cuadroEmail;
    private javax.swing.JLabel cuadroEmailF;
    private javax.swing.JLabel cuadroEmailT;
    private javax.swing.JLabel cuadroEspecie;
    private javax.swing.JLabel cuadroEspecieF;
    private javax.swing.JTextField cuadroEspecieNM;
    private javax.swing.JLabel cuadroEspecieT;
    private javax.swing.JLabel cuadroEspecieTNM;
    private javax.swing.JLabel cuadroFNAciemientoTNM;
    private javax.swing.JTextField cuadroFNacimientoNM;
    private javax.swing.JLabel cuadroFoto;
    private javax.swing.JTextField cuadroFotoNM;
    private javax.swing.JLabel cuadroFotoTNM;
    private javax.swing.JLabel cuadroNacimiento;
    private javax.swing.JLabel cuadroNacimientoF;
    private javax.swing.JLabel cuadroNacimientoT;
    private javax.swing.JLabel cuadroNombre;
    private javax.swing.JLabel cuadroNombre1;
    private javax.swing.JLabel cuadroNombreApellidos;
    private javax.swing.JLabel cuadroNombreApellidosF;
    private javax.swing.JTextField cuadroNombreNM;
    private javax.swing.JLabel cuadroNombreTNM;
    private javax.swing.JLabel cuadroNuevaMascota;
    private javax.swing.JLabel cuadroNuevoCliente;
    private javax.swing.JLabel cuadroPoblacion;
    private javax.swing.JLabel cuadroPoblacionF;
    private javax.swing.JLabel cuadroPoblacionT;
    private javax.swing.JTextField cuadroPropietarioNM;
    private javax.swing.JLabel cuadroPropietarioTNM;
    private javax.swing.JLabel cuadroRaza;
    private javax.swing.JLabel cuadroRazaF;
    private javax.swing.JTextField cuadroRazaNM;
    private javax.swing.JLabel cuadroRazaT;
    private javax.swing.JLabel cuadroRazaTNM;
    private javax.swing.JLabel cuadroSexo;
    private javax.swing.JLabel cuadroSexoF;
    private javax.swing.JTextField cuadroSexoNM;
    private javax.swing.JLabel cuadroSexoT;
    private javax.swing.JLabel cuadroSexoTNM;
    private javax.swing.JLabel cuadroTelefono;
    private javax.swing.JLabel cuadroTelefonoF;
    private javax.swing.JLabel cuadroTelefonoT;
    private javax.swing.JTextField fieldBusquedaMascota;
    private javax.swing.JLabel fondoClientes;
    private javax.swing.JLabel fondoMascotas;
    private javax.swing.JLabel fondoNC;
    private javax.swing.JLabel fondoNuevaMascota;
    private javax.swing.JLabel huella;
    private javax.swing.JLabel insertaCita;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JScrollPane scrollTablaBusquedaMascota;
    private javax.swing.JTable tablaBusquedaMascota;
    private javax.swing.JDialog ventanaBusquedaMascota;
    private javax.swing.JDialog ventanaClienteNuevo;
    private javax.swing.JDialog ventanaMascotaNueva;
    // End of variables declaration//GEN-END:variables
}
