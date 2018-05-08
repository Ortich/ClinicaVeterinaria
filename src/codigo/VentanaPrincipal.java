/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Practica Clinica Veterinaria 3EV - DAM.
 *
 * @author Grupo DAM - Marco Girbau, Alejandro Luna, Marta Marquez, Daniel Ortiz
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    /* ----------------------------- Variables ------------------------------ */
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
    String nacimiento;
    String cliente;

    //Variables de datos citas
    int id;
    String fechaCita;
    String descripcion;
    int mascota;
    String veterinario;

    /* ------------------------------ Metodos ------------------------------- */
    ////////////////////////////////////////////////////////////////////////////
    /////                          Otros Metodos                           /////
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Devuelve, a partir de un parametro de entrada numerico, un String con el
     * sexo de la mascota.
     *
     * @param n - int
     * @return - String
     */
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

    ////////////////////////////////////////////////////////////////////////////
    /////                  Metodos de Escritura de Datos                   /////
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Escribe los datos de la mascota con el chip pasado como parametro.
     *
     * @param _chip - int
     */
    private void escribeDatosMascota(int _chip) {
	Mascota m = null;
	for (int i = 0; i < listaMascota.size(); i++) {
	    if (listaMascota.get(i).chip == _chip) {
		m = listaMascota.get(i);
		break;
	    }
	}
	if (m != null) {
	    cuadroChip.setText(String.format("%015d", m.chip));
	    cuadroNombre.setText(m.nombre);
	    cuadroSexo.setText(devuelveSexo(m.sexo));
	    cuadroEspecie.setText(m.especie);
	    cuadroRaza.setText(m.raza);
	    cuadroNacimiento.setText(m.fecha_nacimiento);
	    cuadroCliente.setText(m.cliente);
	    escribeCitas(_chip);
	}
	escribeCitas(_chip);
    }

    /**
     * Escribe los datos del cliente con el dni pasado como parametro.
     *
     * @param dni - String
     */
    private void escribeDatosCliente(String _dni) {
	Cliente c = null;
	for (int i = 0; i < listaCliente.size(); i++) {
	    if (listaCliente.get(i).dni.equals(_dni)) {
		c = listaCliente.get(i);
		break;
	    }
	}

	if (c != null) {
	    cuadroNombreApellidos.setText(c.nombre + " " + c.apellido);
	    cuadroDNI.setText(c.dni);
	    cuadroTelefono.setText(String.valueOf(c.telefono));
	    cuadroDireccion.setText(c.direccion);
	    cuadroPoblacion.setText(c.poblacion);
	    cuadroCPostal.setText(String.valueOf(c.cp));
	    cuadroEmail.setText(c.email);
	}
    }

    // Escribe los datos de las citas de la mascota seleccionada en una tabla de citas.
    private void escribeCitas(int _chip) {
	DefaultTableModel model = (DefaultTableModel) tablaCitas.getModel();
	model.getDataVector().removeAllElements();
	for (int i = 0; i < listaCita.size(); i++) {
	    if (listaCita.get(i).mascota == _chip) {
		Veterinario vet = null;
		for (int j = 0; j < listaVeterinario.size(); j++) {
		    if (listaVeterinario.get(j).dni.equals(listaCita.get(i).veterinario)) {
			vet = listaVeterinario.get(j);
			break;
		    }
		}
		model.addRow(new Object[]{listaCita.get(i).fecha_cita, listaCita.get(i).descripcion, vet.nombre + " " + vet.apellido});
	    }
	}
    }

    ////////////////////////////////////////////////////////////////////////////
    /////               Metodos de Busqueda en los ArrayList               /////
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Busca y muestra las mascotas con el mismo nombre junto con su chip y el
     * nombre y apellido de su dueño en una tabla.
     *
     * @param busqueda - String
     */
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

    ////////////////////////////////////////////////////////////////////////////
    /////              Metodos de Conexion a la Base de Datos              /////
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Conecta con la Base de Datos y almacena todos los datos en distintos
     * ArrayList.
     */
    public void conexionBBDD() {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
	    estado = conexion.createStatement();

	    /* ·························· Mascotas ·························· */
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.mascota");
	    while (resultadoConsulta.next()) {
		Mascota m = new Mascota();
		m.chip = resultadoConsulta.getInt(1);
		m.nombre = resultadoConsulta.getString(2);
		m.sexo = resultadoConsulta.getInt(3);
		m.especie = resultadoConsulta.getString(4);
		m.raza = resultadoConsulta.getString(5);
		m.fecha_nacimiento = resultadoConsulta.getString(6);
		m.cliente = resultadoConsulta.getString(7);
		listaMascota.add(m);
	    }

	    /* ·························· Clientes ·························· */
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cliente");
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

	    /* ························ Veterinarios ························ */
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.veterinario");
	    while (resultadoConsulta.next()) {
		Veterinario v = new Veterinario();
		v.dni = resultadoConsulta.getString(1);
		v.nombre = resultadoConsulta.getString(2);
		v.apellido = resultadoConsulta.getString(3);
		v.pass = resultadoConsulta.getString(4);
		listaVeterinario.add(v);
	    }

	    /* ··························· Citas ···························· */
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cita");
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
     * Inserta datos de mascota en la Base de Datos.
     *
     * @param _mascota - Mascota(Objeto)
     */
    public void insertaDatosMascota(Mascota _mascota) {
	if (_mascota != null) {
	    try {
		Class.forName("com.mysql.jdbc.Driver");
		conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
		estado = conexion.createStatement();
		resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.mascota");
		estado.executeUpdate("INSERT INTO clinicaufvet.mascota VALUES(" + _mascota.chip + ",'" + _mascota.nombre + "', " + _mascota.sexo + ", '" + _mascota.especie + "','" + _mascota.raza + "','" + _mascota.fecha_nacimiento + "','" + _mascota.cliente + "')");
		listaMascota.add(_mascota);
	    } catch (Exception e) {
		e.getMessage();
	    }
	}
    }

    /**
     * Inserta datos de cita en la Base de Datos.
     *
     * @param _cita - Cita(Objeto)
     */
    public void insertaDatosCita(Cita _cita) {
	if (_cita != null) {
	    try {
		Class.forName("com.mysql.jdbc.Driver");
		conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
		estado = conexion.createStatement();
		resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cita");
		estado.executeUpdate("INSERT INTO clinicaufvet.cita(2,3,4,5) VALUES('" + _cita.fecha_cita + "', '" + _cita.descripcion + "', " + _cita.mascota + ",'" + _cita.veterinario + "')");
		listaCita.add(_cita);
	    } catch (Exception e) {
		e.getMessage();
	    }
	}
    }

    /**
     * Inserta datos de cliente en la Base de Datos.
     *
     * @param _cliente - Cliente(Objeto)
     */
    private void insertaDatosCliente(Cliente _cliente) {
	if (_cliente != null) {
	    try {
		Class.forName("com.mysql.jdbc.Driver");
		conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
		estado = conexion.createStatement();
		resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cliente");
		estado.executeUpdate("INSERT INTO clinicaufvet.cliente VALUES('" + _cliente.dni + "','" + _cliente.nombre + "', '" + _cliente.apellido + "', '" + _cliente.direccion + "'," + _cliente.cp + "," + _cliente.telefono + ",'" + _cliente.poblacion + "','" + _cliente.email + "')");
		listaCliente.add(_cliente);
	    } catch (Exception e) {
		e.getMessage();
	    }
	}
    }

    // Método para modificar los datos ya insertados en la BBDD. 
    public void cambiaDatosMascota(int chip, String nombreM, int sexo, String especie, String raza, String nacimiento, String cliente) {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
	    estado = conexion.createStatement();
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cita");

	    String sql = "UPDATE clinicaufvet.mascota "
		    + " SET nombre='" + nombreM + "',sexo=" + sexo + ",especie='" + especie + "',raza='" + raza + "',fecha_nacimiento='" + nacimiento + "',cliente='" + cliente + "'"
		    + " WHERE chip=" + chip + "";

	    estado.executeUpdate(sql);
	    System.out.println("funciona");
	} catch (Exception e) {
	    e.getMessage();
	    System.out.println("no funciona");
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

	ventanaBusquedaMascota.setSize(480, 390);
	ventanaBusquedaMascota.setResizable(false);

	conexionBBDD();
	escribeDatosMascota(0);
	escribeCitas(1);

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
        cuadroNombreLNC = new javax.swing.JTextField();
        cuadroNombreTNC = new javax.swing.JLabel();
        cuadroApellidosLNC = new javax.swing.JTextField();
        cuadroApellidosTNC = new javax.swing.JLabel();
        cuadroDNILNC = new javax.swing.JTextField();
        cuadroDNITNC = new javax.swing.JLabel();
        cuadroTelefonoLNC = new javax.swing.JTextField();
        cuadroTelefonoTNC = new javax.swing.JLabel();
        cuadroDireccionLNC = new javax.swing.JTextField();
        cuadroDireccionTNC = new javax.swing.JLabel();
        cuadroPoblacionLNC = new javax.swing.JTextField();
        cuadroPoblacionTNC = new javax.swing.JLabel();
        cudroCPostalLNC = new javax.swing.JTextField();
        cuadroCPostalTNC = new javax.swing.JLabel();
        cuadroEmailLNC = new javax.swing.JTextField();
        cuadroEmailTNC = new javax.swing.JLabel();
        botonGuardarGNC = new javax.swing.JLabel();
        botonEditarGNC = new javax.swing.JLabel();
        fondoNC = new javax.swing.JLabel();
        ventanaBusquedaMascota = new javax.swing.JDialog();
        botonBuscarBM = new javax.swing.JButton();
        cuadroTextoBM = new javax.swing.JTextField();
        scrollTablaBusquedaMascota = new javax.swing.JScrollPane();
        tablaBusquedaMascota = new javax.swing.JTable();
        botonAceptarBM = new javax.swing.JButton();
        fondoBM = new javax.swing.JLabel();
        ventanaInsercionCitas = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        mascotaCitasTexto = new javax.swing.JLabel();
        labelMascotaCitas = new javax.swing.JTextField();
        cajaMascotas = new javax.swing.JLabel();
        descripcionCitasTexto = new javax.swing.JLabel();
        labelDescripcionCitas = new javax.swing.JTextField();
        cajaDescripcion = new javax.swing.JLabel();
        fechaCitasTexto = new javax.swing.JLabel();
        labelFechasCitas = new javax.swing.JTextField();
        cajaFechas = new javax.swing.JLabel();
        veterinarioCitasTexto = new javax.swing.JLabel();
        labelVeterinarioCitas = new javax.swing.JTextField();
        cajaVeterinario = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        fondoCitas = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCitas = new javax.swing.JTable();
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
        borrarCita = new javax.swing.JLabel();
        buscarMascota = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
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
        jButton2 = new javax.swing.JButton();
        fondoClientes = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        ventanaMascotaNueva.setSize(new java.awt.Dimension(1020, 850));
        ventanaMascotaNueva.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setOpaque(false);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(1040, 600));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cuadroFNacimientoNM.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.add(cuadroFNacimientoNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 547, 440, 35));

        cuadroFNAciemientoTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaNacimientoLetras.png"))); // NOI18N
        cuadroFNAciemientoTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroFNAciemientoTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroFNAciemientoTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 550, -1, -1));

        cuadroFotoNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroFotoNM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuadroFotoNMActionPerformed(evt);
            }
        });
        jPanel4.add(cuadroFotoNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 760, 441, 30));

        cuadroFotoTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaFotoLetras.png"))); // NOI18N
        cuadroFotoTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroFotoTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroFotoTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 760, -1, -1));

        cuadroChipNM.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.add(cuadroChipNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 625, 440, 35));

        cuadroChipTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaChipLetras.png"))); // NOI18N
        cuadroChipTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroChipTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroChipTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 630, -1, -1));

        cuadroSexoNM.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.add(cuadroSexoNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 472, 440, 35));

        cuadroSexoTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaSexoLetras.png"))); // NOI18N
        cuadroSexoTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroSexoTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroSexoTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 480, -1, -1));

        cuadroNombreNM.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.add(cuadroNombreNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 254, 440, 35));

        cuadroNombreTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaNombreLetras.png"))); // NOI18N
        cuadroNombreTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroNombreTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroNombreTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 260, -1, -1));

        cuadroEspecieNM.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.add(cuadroEspecieNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 323, 440, 35));

        cuadroEspecieTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaEspecieLetras.png"))); // NOI18N
        cuadroEspecieTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroEspecieTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroEspecieTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, -1, -1));

        cuadroPropietarioNM.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.add(cuadroPropietarioNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(369, 691, 440, 35));

        cuadroPropietarioTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaPopietarioLetras.png"))); // NOI18N
        cuadroPropietarioTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroPropietarioTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroPropietarioTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 690, -1, -1));

        cuadroRazaNM.setBackground(new java.awt.Color(204, 204, 255));
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

        ventanaMascotaNueva.getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        ventanaClienteNuevo.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane4.setPreferredSize(new java.awt.Dimension(1040, 600));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cuadroNombreLNC.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.add(cuadroNombreLNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(367, 256, 440, 35));

        cuadroNombreTNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaNombreLetras.png"))); // NOI18N
        jPanel5.add(cuadroNombreTNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(246, 250, 120, 40));

        cuadroApellidosLNC.setBackground(new java.awt.Color(204, 204, 255));
        cuadroApellidosLNC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuadroApellidosLNCActionPerformed(evt);
            }
        });
        jPanel5.add(cuadroApellidosLNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 323, 440, 35));

        cuadroApellidosTNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteApellido.png"))); // NOI18N
        jPanel5.add(cuadroApellidosTNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 318, 140, 40));

        cuadroDNILNC.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.add(cuadroDNILNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 399, 440, 35));

        cuadroDNITNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgClienteDNILetras.png"))); // NOI18N
        jPanel5.add(cuadroDNITNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 398, 60, 30));

        cuadroTelefonoLNC.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.add(cuadroTelefonoLNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 473, 440, 35));

        cuadroTelefonoTNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgClienteTelefonoLetras.png"))); // NOI18N
        jPanel5.add(cuadroTelefonoTNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 468, 140, 40));

        cuadroDireccionLNC.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.add(cuadroDireccionLNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 548, 440, 35));

        cuadroDireccionTNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgClienteDireccionLetras.png"))); // NOI18N
        jPanel5.add(cuadroDireccionTNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 544, 140, 40));

        cuadroPoblacionLNC.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.add(cuadroPoblacionLNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 624, 440, 35));

        cuadroPoblacionTNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgClientePoblacionLetras.png"))); // NOI18N
        jPanel5.add(cuadroPoblacionTNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(214, 621, -1, 40));

        cudroCPostalLNC.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.add(cudroCPostalLNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 690, 440, 35));

        cuadroCPostalTNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgClienteCPostalLetras.png"))); // NOI18N
        jPanel5.add(cuadroCPostalTNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(218, 686, 150, 40));

        cuadroEmailLNC.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.add(cuadroEmailLNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 760, 440, 35));

        cuadroEmailTNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgClienteEmailLetras.png"))); // NOI18N
        jPanel5.add(cuadroEmailTNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 756, 100, 40));

        botonGuardarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaGuardar.png"))); // NOI18N
        botonGuardarGNC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonGuardarGNCMousePressed(evt);
            }
        });
        jPanel5.add(botonGuardarGNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 860, -1, -1));

        botonEditarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaEditar.png"))); // NOI18N
        jPanel5.add(botonEditarGNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 860, 240, 60));

        fondoNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosCliente.png"))); // NOI18N
        jPanel5.add(fondoNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2, 1000, 1000));

        jScrollPane4.setViewportView(jPanel5);

        ventanaClienteNuevo.getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        ventanaBusquedaMascota.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botonBuscarBM.setText("Buscar");
        botonBuscarBM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonBuscarBMMouseClicked(evt);
            }
        });
        ventanaBusquedaMascota.getContentPane().add(botonBuscarBM, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, -1, -1));

        cuadroTextoBM.setText("Nombre Mascota");
        cuadroTextoBM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cuadroTextoBMKeyPressed(evt);
            }
        });
        ventanaBusquedaMascota.getContentPane().add(cuadroTextoBM, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 130, 30));

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

        ventanaBusquedaMascota.getContentPane().add(scrollTablaBusquedaMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 370, 170));

        botonAceptarBM.setText("Aceptar");
        botonAceptarBM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonAceptarBMMousePressed(evt);
            }
        });
        ventanaBusquedaMascota.getContentPane().add(botonAceptarBM, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 300, -1, -1));

        fondoBM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Auxiliares/pgBusqueda2.png"))); // NOI18N
        ventanaBusquedaMascota.getContentPane().add(fondoBM, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        ventanaInsercionCitas.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mascotaCitasTexto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Auxiliares/mascotaCita.png"))); // NOI18N
        jPanel6.add(mascotaCitasTexto, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 100, 40));

        labelMascotaCitas.setBackground(new java.awt.Color(204, 204, 255));
        jPanel6.add(labelMascotaCitas, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 84, 186, 33));

        cajaMascotas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteCPostal.png"))); // NOI18N
        cajaMascotas.setText("jLabel1");
        jPanel6.add(cajaMascotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 200, -1));

        descripcionCitasTexto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Auxiliares/descripcionCitas.png"))); // NOI18N
        jPanel6.add(descripcionCitasTexto, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 130, 40));

        labelDescripcionCitas.setBackground(new java.awt.Color(204, 204, 255));
        jPanel6.add(labelDescripcionCitas, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 144, 186, 33));

        cajaDescripcion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteCPostal.png"))); // NOI18N
        cajaDescripcion.setText("jLabel1");
        jPanel6.add(cajaDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 140, 200, -1));

        fechaCitasTexto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Auxiliares/fechasCitas.png"))); // NOI18N
        jPanel6.add(fechaCitasTexto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 200, 70, 40));

        labelFechasCitas.setBackground(new java.awt.Color(204, 204, 255));
        jPanel6.add(labelFechasCitas, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 204, 186, 33));

        cajaFechas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteCPostal.png"))); // NOI18N
        cajaFechas.setText("jLabel1");
        jPanel6.add(cajaFechas, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 200, 200, -1));

        veterinarioCitasTexto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Auxiliares/veterinarioCitas.png"))); // NOI18N
        jPanel6.add(veterinarioCitasTexto, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 130, 40));

        labelVeterinarioCitas.setBackground(new java.awt.Color(204, 204, 255));
        labelVeterinarioCitas.setText("jTextField1");
        jPanel6.add(labelVeterinarioCitas, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 264, 186, 33));

        cajaVeterinario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteCPostal.png"))); // NOI18N
        cajaVeterinario.setText("jLabel1");
        jPanel6.add(cajaVeterinario, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, 200, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasInsertar.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
        });
        jPanel6.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, 170, 40));

        fondoCitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Auxiliares/pgBusquedaCitas.png"))); // NOI18N
        jPanel6.add(fondoCitas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 410));

        ventanaInsercionCitas.getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 930, 520));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1010, 700));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setPreferredSize(new java.awt.Dimension(1030, 680));

        jTabbedPane2.setMinimumSize(new java.awt.Dimension(1012, 700));

        jPanel1.setMinimumSize(new java.awt.Dimension(1002, 700));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaCitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Fecha", "Detalles", "Veterinario"
            }
        ));
        jScrollPane1.setViewportView(tablaCitas);

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
        jPanel1.add(cuadroNuevaMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 220, -1, -1));

        cuadroNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNombre.setText("NOMBRE MASCOTA");
        cuadroNombre.setPreferredSize(new java.awt.Dimension(384, 114));
        jPanel1.add(cuadroNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 360, -1, -1));

        cuadroNombre1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNombre1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro2.png"))); // NOI18N
        jPanel1.add(cuadroNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 360, -1, -1));

        cuadroRaza.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroRaza.setText("RAZA");
        cuadroRaza.setPreferredSize(new java.awt.Dimension(169, 45));
        jPanel1.add(cuadroRaza, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 590, -1, -1));

        cuadroRazaF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        jPanel1.add(cuadroRazaF, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 590, -1, -1));

        cuadroRazaT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasRaza.png"))); // NOI18N
        jPanel1.add(cuadroRazaT, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 600, -1, -1));

        cuadroEspecie.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroEspecie.setText("ESPECIE");
        cuadroEspecie.setPreferredSize(new java.awt.Dimension(169, 45));
        jPanel1.add(cuadroEspecie, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 530, -1, -1));

        cuadroEspecieF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        jPanel1.add(cuadroEspecieF, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 530, -1, -1));

        cuadroEspecieT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasEspecie.png"))); // NOI18N
        jPanel1.add(cuadroEspecieT, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 540, -1, -1));

        cuadroSexo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroSexo.setText("SEXO");
        cuadroSexo.setPreferredSize(new java.awt.Dimension(169, 45));
        jPanel1.add(cuadroSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 650, -1, -1));

        cuadroSexoF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        jPanel1.add(cuadroSexoF, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 650, -1, -1));

        cuadroSexoT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasSexo.png"))); // NOI18N
        jPanel1.add(cuadroSexoT, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 660, -1, -1));

        cuadroNacimiento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNacimiento.setText("FECHA NACIMIENTO");
        cuadroNacimiento.setPreferredSize(new java.awt.Dimension(169, 45));
        jPanel1.add(cuadroNacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 530, -1, -1));

        cuadroNacimientoF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNacimientoF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        cuadroNacimientoF.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(cuadroNacimientoF, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 530, -1, -1));

        cuadroNacimientoT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasFNacimiento.png"))); // NOI18N
        jPanel1.add(cuadroNacimientoT, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 540, -1, -1));

        cuadroChip.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroChip.setText("Nº CHIP");
        cuadroChip.setPreferredSize(new java.awt.Dimension(169, 45));
        jPanel1.add(cuadroChip, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 590, -1, -1));

        cuadroChipF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        jPanel1.add(cuadroChipF, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 590, -1, -1));

        cuadroChipT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasChip.png"))); // NOI18N
        jPanel1.add(cuadroChipT, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 600, -1, -1));

        cuadroCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroCliente.setText("PROPIETARIO");
        cuadroCliente.setPreferredSize(new java.awt.Dimension(169, 45));
        jPanel1.add(cuadroCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 650, -1, -1));

        cuadroClienteF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro3.png"))); // NOI18N
        jPanel1.add(cuadroClienteF, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 650, -1, -1));

        cuadroClienteT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasPopietario.png"))); // NOI18N
        jPanel1.add(cuadroClienteT, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 656, -1, -1));

        cuadroFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasFoto.png"))); // NOI18N
        jPanel1.add(cuadroFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, -1, -1));

        insertaCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasInsertar.png"))); // NOI18N
        insertaCita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                insertaCitaMousePressed(evt);
            }
        });
        jPanel1.add(insertaCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 744, 170, -1));

        borrarCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasBorrar.png"))); // NOI18N
        jPanel1.add(borrarCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 744, 170, -1));

        buscarMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasEditarMascota.png"))); // NOI18N
        buscarMascota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                buscarMascotaMousePressed(evt);
            }
        });
        jPanel1.add(buscarMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, -1, -1));

        jButton1.setText("boton secreto de marta");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 240, -1, -1));

        fondoMascotas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotas.png"))); // NOI18N
        fondoMascotas.setMaximumSize(new java.awt.Dimension(400, 400));
        jPanel1.add(fondoMascotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 970));

        jTabbedPane2.addTab("Mascota", jPanel1);

        jPanel2.setMinimumSize(new java.awt.Dimension(1010, 700));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cuadroBusquedaCliente.setBackground(new java.awt.Color(204, 204, 255));
        cuadroBusquedaCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cuadroBusquedaCliente.setText("BUSCAR CLIENTE");
        cuadroBusquedaCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cuadroBusquedaClienteKeyPressed(evt);
            }
        });
        jPanel2.add(cuadroBusquedaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 220, 50));

        cuadroNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonNuevoCliente.png"))); // NOI18N
        cuadroNuevoCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroNuevoClienteMousePressed(evt);
            }
        });
        jPanel2.add(cuadroNuevoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 220, -1, -1));

        cuadroNombreApellidos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNombreApellidos.setText("NOMBRE Y APELLIDOS");
        cuadroNombreApellidos.setPreferredSize(new java.awt.Dimension(634, 114));
        jPanel2.add(cuadroNombreApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 350, -1, -1));

        cuadroNombreApellidosF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteNombreApellidos.png"))); // NOI18N
        jPanel2.add(cuadroNombreApellidosF, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 350, -1, -1));

        cuadroDNI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroDNI.setText("DNI");
        cuadroDNI.setPreferredSize(new java.awt.Dimension(189, 43));
        jPanel2.add(cuadroDNI, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 505, -1, -1));

        cuadroDNIF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteDNI.png"))); // NOI18N
        jPanel2.add(cuadroDNIF, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 505, -1, -1));

        cuadroDNIT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroDNIT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteDNILetras.png"))); // NOI18N
        cuadroDNIT.setPreferredSize(new java.awt.Dimension(150, 30));
        jPanel2.add(cuadroDNIT, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 510, -1, -1));

        cuadroTelefono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroTelefono.setText("Nº Teléfono");
        cuadroTelefono.setPreferredSize(new java.awt.Dimension(189, 43));
        jPanel2.add(cuadroTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 505, -1, -1));

        cuadroTelefonoF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroTelefonoF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteDNI.png"))); // NOI18N
        jPanel2.add(cuadroTelefonoF, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 505, -1, -1));

        cuadroTelefonoT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroTelefonoT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteTelefonoLetras.png"))); // NOI18N
        cuadroTelefonoT.setPreferredSize(new java.awt.Dimension(150, 30));
        jPanel2.add(cuadroTelefonoT, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 510, -1, -1));

        cuadroDireccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroDireccion.setText("DIRECCIÓN (CALLE)");
        cuadroDireccion.setPreferredSize(new java.awt.Dimension(551, 43));
        jPanel2.add(cuadroDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 585, -1, -1));

        cuadroDireccionF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroDireccionF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteDireccion.png"))); // NOI18N
        jPanel2.add(cuadroDireccionF, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 585, -1, -1));

        cuadroDireccionT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroDireccionT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteDireccionLetras.png"))); // NOI18N
        cuadroDireccionT.setPreferredSize(new java.awt.Dimension(150, 30));
        jPanel2.add(cuadroDireccionT, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 590, -1, -1));

        cuadroPoblacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroPoblacion.setText("POBLACIÓN");
        cuadroPoblacion.setPreferredSize(new java.awt.Dimension(189, 43));
        jPanel2.add(cuadroPoblacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 665, -1, -1));

        cuadroPoblacionF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroPoblacionF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClientePoblacion.png"))); // NOI18N
        jPanel2.add(cuadroPoblacionF, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 665, -1, -1));

        cuadroPoblacionT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroPoblacionT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClientePoblacionLetras.png"))); // NOI18N
        cuadroPoblacionT.setPreferredSize(new java.awt.Dimension(150, 30));
        jPanel2.add(cuadroPoblacionT, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 670, -1, 30));

        cuadroCPostal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroCPostal.setText("CÓDIGO POSTAL");
        cuadroCPostal.setPreferredSize(new java.awt.Dimension(198, 43));
        jPanel2.add(cuadroCPostal, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 665, -1, -1));

        cuadroCPostalF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroCPostalF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteCPostal.png"))); // NOI18N
        jPanel2.add(cuadroCPostalF, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 665, -1, -1));

        cuadroCPostalT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroCPostalT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteCPostalLetras.png"))); // NOI18N
        cuadroCPostalT.setPreferredSize(new java.awt.Dimension(150, 30));
        jPanel2.add(cuadroCPostalT, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 670, -1, -1));

        cuadroEmail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroEmail.setText("EMAIL");
        cuadroEmail.setPreferredSize(new java.awt.Dimension(553, 43));
        jPanel2.add(cuadroEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 745, -1, -1));

        cuadroEmailF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroEmailF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteEmail.png"))); // NOI18N
        jPanel2.add(cuadroEmailF, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 745, -1, -1));

        cuadroEmailT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroEmailT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteEmailLetras.png"))); // NOI18N
        cuadroEmailT.setPreferredSize(new java.awt.Dimension(150, 30));
        jPanel2.add(cuadroEmailT, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 750, -1, -1));

        jButton2.setText("boton secreto de marta");
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 240, -1, -1));

        fondoClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgCliente.png"))); // NOI18N
        jPanel2.add(fondoClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -15, 1010, 1000));

        jTabbedPane2.addTab("Cliente", jPanel2);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jTabbedPane2.addTab("Tienda?", jPanel3);

        jScrollPane2.setViewportView(jTabbedPane2);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cuadroNuevaMascotaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevaMascotaMousePressed
	ventanaMascotaNueva.setVisible(true);
    }//GEN-LAST:event_cuadroNuevaMascotaMousePressed

    private void cuadroNuevoClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevoClienteMousePressed
	ventanaClienteNuevo.setVisible(true);
    }//GEN-LAST:event_cuadroNuevoClienteMousePressed

    private void botonGuardarNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGuardarNMMousePressed
	Mascota mascota = new Mascota();
	
	mascota.chip = Integer.valueOf(cuadroChipNM.getText());
	mascota.nombre = cuadroNombreNM.getText();
	mascota.sexo = Integer.valueOf(cuadroSexoNM.getText());
	mascota.especie = cuadroEspecieNM.getText();
	mascota.raza = cuadroRazaNM.getText();
	mascota.fecha_nacimiento = cuadroFNacimientoNM.getText();
	mascota.cliente = cuadroPropietarioNM.getText();

	//System.out.println(nacimiento);
	insertaDatosMascota(mascota);
    }//GEN-LAST:event_botonGuardarNMMousePressed

    private void insertaCitaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertaCitaMousePressed
        ventanaInsercionCitas.setVisible(true);

//	Cita cita = new Cita();
//
//	cita.fecha_cita = "1999-11-11";
//	cita.descripcion = "Descripcion de prueba";
//	cita.mascota = 2;
//	cita.veterinario = "00000004V";
//
//	insertaDatosCita(cita);
    }//GEN-LAST:event_insertaCitaMousePressed

    private void botonBuscarBMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonBuscarBMMouseClicked
	buscaMascota(cuadroTextoBM.getText());
    }//GEN-LAST:event_botonBuscarBMMouseClicked

    private void cuadroTextoBMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cuadroTextoBMKeyPressed
	if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
	    buscaMascota(cuadroTextoBM.getText());
	}
    }//GEN-LAST:event_cuadroTextoBMKeyPressed

    private void botonAceptarBMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAceptarBMMousePressed
	TableModel model = (TableModel) tablaBusquedaMascota.getModel();
	int chip = Integer.valueOf(model.getValueAt((tablaBusquedaMascota.getSelectedRow()), 0).toString());
	escribeDatosMascota(chip);
    }//GEN-LAST:event_botonAceptarBMMousePressed

    private void botonEditarNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEditarNMMousePressed
	//Botón que cambia los datos en la ventana de mascotas. Obtenemos los datos que hemos sacado de la ventana principal
	// Y los guardamos en las variables que vamos a usar en el método.

	chip = Integer.valueOf(cuadroChipNM.getText());
	nombreM = cuadroNombreNM.getText();
	sexo = Integer.valueOf(cuadroSexoNM.getText());
	especie = cuadroEspecieNM.getText();
	raza = cuadroRazaNM.getText();
	nacimiento = cuadroFNacimientoNM.getText();
	cliente = cuadroPropietarioNM.getText();

	cambiaDatosMascota(chip, nombreM, sexo, especie, raza, nacimiento, cliente);
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

    private void buscarMascotaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscarMascotaMousePressed
	ventanaBusquedaMascota.setVisible(true);
    }//GEN-LAST:event_buscarMascotaMousePressed

    private void cuadroBusquedaClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cuadroBusquedaClienteKeyPressed
	if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
	    escribeDatosCliente(cuadroBusquedaCliente.getText());
	}
    }//GEN-LAST:event_cuadroBusquedaClienteKeyPressed

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
	//Obtenemos los datos y los guardamos en variables.
	String chip2 = cuadroChip.getText();
	nombreM = cuadroNombre.getText();

	if (cuadroSexo.getText().equals("hermafrodita")) {
	    sexo = 2;
	} else if (cuadroSexo.getText().equals("macho")) {
	    sexo = 1;
	} else if (cuadroSexo.getText().equals("hembra")) {
	    sexo = 0;
	}
	especie = cuadroEspecie.getText();
	raza = cuadroRaza.getText();
	nacimiento = cuadroNacimiento.getText();
	cliente = cuadroCliente.getText();

	//Abrimos la ventana de inserción de datos.
	ventanaMascotaNueva.setVisible(true);

	cuadroNombreNM.setText(nombreM);
	cuadroEspecieNM.setText(especie);
	cuadroRazaNM.setText(raza);
	cuadroSexoNM.setText(String.valueOf(sexo));
	cuadroFNacimientoNM.setText(nacimiento);
	cuadroPropietarioNM.setText(cliente);
	cuadroChipNM.setText(chip2);

	//Método que nos va a permitir modificar datos en la BBDD.

    }//GEN-LAST:event_jButton1MousePressed

    private void cuadroApellidosLNCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuadroApellidosLNCActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cuadroApellidosLNCActionPerformed

    private void botonGuardarGNCMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGuardarGNCMousePressed
	Cliente cliente = new Cliente();

	cliente.dni = cuadroDNILNC.getText();
	cliente.nombre = cuadroNombreLNC.getText();
	cliente.apellido = cuadroApellidosLNC.getText();
	cliente.telefono = Integer.valueOf(cuadroTelefonoLNC.getText());
	cliente.direccion = cuadroDireccionLNC.getText();
	cliente.cp = Integer.valueOf(cudroCPostalLNC.getText());
	cliente.email = cuadroEmailLNC.getText();
	cliente.poblacion = cuadroPoblacionLNC.getText();

	insertaDatosCliente(cliente);
    }//GEN-LAST:event_botonGuardarGNCMousePressed

    private void cuadroFotoNMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuadroFotoNMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuadroFotoNMActionPerformed

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        Cita cita = new Cita();
	cita.mascota = Integer.valueOf(labelMascotaCitas.getText());
	cita.descripcion = labelDescripcionCitas.getText();
	cita.fecha_cita = labelFechasCitas.getText();
	cita.veterinario = labelVeterinarioCitas.getText();
	insertaDatosCita(cita);
	
    }//GEN-LAST:event_jLabel1MousePressed

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
    private javax.swing.JLabel borrarCita;
    private javax.swing.JButton botonAceptarBM;
    private javax.swing.JButton botonBuscarBM;
    private javax.swing.JLabel botonEditarGNC;
    private javax.swing.JLabel botonEditarNM;
    private javax.swing.JLabel botonGuardarGNC;
    private javax.swing.JLabel botonGuardarNM;
    private javax.swing.JLabel buscarMascota;
    private javax.swing.JLabel cajaDescripcion;
    private javax.swing.JLabel cajaFechas;
    private javax.swing.JLabel cajaMascotas;
    private javax.swing.JLabel cajaVeterinario;
    private javax.swing.JTextField cuadroApellidosLNC;
    private javax.swing.JLabel cuadroApellidosTNC;
    private javax.swing.JTextField cuadroBusquedaCliente;
    private javax.swing.JLabel cuadroCPostal;
    private javax.swing.JLabel cuadroCPostalF;
    private javax.swing.JLabel cuadroCPostalT;
    private javax.swing.JLabel cuadroCPostalTNC;
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
    private javax.swing.JTextField cuadroDNILNC;
    private javax.swing.JLabel cuadroDNIT;
    private javax.swing.JLabel cuadroDNITNC;
    private javax.swing.JLabel cuadroDireccion;
    private javax.swing.JLabel cuadroDireccionF;
    private javax.swing.JTextField cuadroDireccionLNC;
    private javax.swing.JLabel cuadroDireccionT;
    private javax.swing.JLabel cuadroDireccionTNC;
    private javax.swing.JLabel cuadroEmail;
    private javax.swing.JLabel cuadroEmailF;
    private javax.swing.JTextField cuadroEmailLNC;
    private javax.swing.JLabel cuadroEmailT;
    private javax.swing.JLabel cuadroEmailTNC;
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
    private javax.swing.JTextField cuadroNombreLNC;
    private javax.swing.JTextField cuadroNombreNM;
    private javax.swing.JLabel cuadroNombreTNC;
    private javax.swing.JLabel cuadroNombreTNM;
    private javax.swing.JLabel cuadroNuevaMascota;
    private javax.swing.JLabel cuadroNuevoCliente;
    private javax.swing.JLabel cuadroPoblacion;
    private javax.swing.JLabel cuadroPoblacionF;
    private javax.swing.JTextField cuadroPoblacionLNC;
    private javax.swing.JLabel cuadroPoblacionT;
    private javax.swing.JLabel cuadroPoblacionTNC;
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
    private javax.swing.JTextField cuadroTelefonoLNC;
    private javax.swing.JLabel cuadroTelefonoT;
    private javax.swing.JLabel cuadroTelefonoTNC;
    private javax.swing.JTextField cuadroTextoBM;
    private javax.swing.JTextField cudroCPostalLNC;
    private javax.swing.JLabel descripcionCitasTexto;
    private javax.swing.JLabel fechaCitasTexto;
    private javax.swing.JLabel fondoBM;
    private javax.swing.JLabel fondoCitas;
    private javax.swing.JLabel fondoClientes;
    private javax.swing.JLabel fondoMascotas;
    private javax.swing.JLabel fondoNC;
    private javax.swing.JLabel fondoNuevaMascota;
    private javax.swing.JLabel huella;
    private javax.swing.JLabel insertaCita;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField labelDescripcionCitas;
    private javax.swing.JTextField labelFechasCitas;
    private javax.swing.JTextField labelMascotaCitas;
    private javax.swing.JTextField labelVeterinarioCitas;
    private javax.swing.JLabel mascotaCitasTexto;
    private javax.swing.JScrollPane scrollTablaBusquedaMascota;
    private javax.swing.JTable tablaBusquedaMascota;
    private javax.swing.JTable tablaCitas;
    private javax.swing.JDialog ventanaBusquedaMascota;
    private javax.swing.JDialog ventanaClienteNuevo;
    private javax.swing.JDialog ventanaInsercionCitas;
    private javax.swing.JDialog ventanaMascotaNueva;
    private javax.swing.JLabel veterinarioCitasTexto;
    // End of variables declaration//GEN-END:variables
}
