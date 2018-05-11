/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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

    int contadorCita = 0;

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

    /**
     * Cambia el titulo de la ventana principal dependiendo de la pestaña
     * seleccionada.
     */
    private void cambiaTitulo() {
	if (jTabbedPane2.getSelectedIndex() == 0) {
	    this.setTitle("Clinica UFVet - Mascota");
	} else if (jTabbedPane2.getSelectedIndex() == 1) {
	    this.setTitle("Clinica UFVet - Cliente");
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
	    try {
		if (m.img > 0 && m.img < 9) {
		    fotoAnimal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fotosAnimales/" + m.img + ".png")));
		} else {
		    fotoAnimal.setIcon(null);
		}
	    } catch (Exception e) {
	    }
	    escribeCitas(_chip);
	}
    }

    /**
     * Escribe los datos del cliente con el dni pasado como parametro.
     *
     * @param dni - String
     */
    private void escribeDatosCliente(String _dni) {
	Cliente c = null;
	for (int i = 0; i < listaCliente.size(); i++) {
	    if (listaCliente.get(i).dni.toUpperCase().equals(_dni.toUpperCase())) {
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

	    //
	    cuadroNombreLNC.setText(c.nombre);
	    cuadroApellidosLNC.setText(c.apellido);
	    cuadroDNILNC.setText(c.dni);
	    cuadroTelefonoLNC.setText(String.valueOf(c.telefono));
	    cuadroDireccionLNC.setText(c.direccion);
	    cuadroCPostalLNC.setText(String.valueOf(c.cp));
	    cuadroPoblacionLNC.setText(c.poblacion);
	    cuadroEmailLNC.setText(c.email);

	}
    }

    /**
     * Escribe los datos de las citas de la mascota con el chip pasado como
     * parametro.
     *
     * @param _chip - int
     */
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
	    if (listaMascota.get(i).nombre.toUpperCase().contains(busqueda.toUpperCase())) {
		try {
		    String nombreCliente = "";
		    String apellidoCliente = "";
		    String dniCliente = listaMascota.get(i).cliente.toString().toUpperCase();
		    for (int j = 0; j < listaCliente.size(); j++) {
			if (listaCliente.get(j).dni.toString().toUpperCase().equals(dniCliente)) {
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
		m.img = resultadoConsulta.getInt(8);
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
		if (contadorCita < cita.id) {
		    contadorCita = cita.id;
		}
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
		resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.mascota");
		estado.executeUpdate("INSERT INTO clinicaufvet.mascota VALUES(" + _mascota.chip + ",'" + _mascota.nombre + "', " + _mascota.sexo + ", '" + _mascota.especie + "','" + _mascota.raza + "','" + _mascota.fecha_nacimiento + "','" + _mascota.cliente + "','" + _mascota.img + "')");
		listaMascota.add(_mascota);

		datosCorrectosVNM.setVisible(true);
		datosCorrectosLMN.setVisible(true);
		datosCorrectosLMN.setText("Los datos se han insertado correctamente");

	    } catch (Exception e) {
		e.getMessage();
		datosCorrectosVNM.setVisible(true);
		datosCorrectosLMN.setVisible(true);
		datosCorrectosLMN.setText("No se han podido insertar los datos");
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
		contadorCita++;
		resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cita");
		estado.executeUpdate("INSERT INTO clinicaufvet.cita VALUES(" + contadorCita + ",'" + _cita.fecha_cita + "', '" + _cita.descripcion + "', " + _cita.mascota + ",'" + _cita.veterinario + "')");
		listaCita.add(_cita);

		datosCitasV.setVisible(true);
		datosCitasL.setVisible(true);
		datosCitasL.setText("La cita se ha insertado correctamente");

	    } catch (Exception e) {
		e.getMessage();
		datosCitasV.setVisible(true);
		datosCitasL.setVisible(true);
		datosCitasL.setText("No se ha podido insertar la cita");
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
		resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cliente");
		estado.executeUpdate("INSERT INTO clinicaufvet.cliente VALUES('" + _cliente.dni + "','" + _cliente.nombre + "', '" + _cliente.apellido + "', '" + _cliente.direccion + "'," + _cliente.cp + "," + _cliente.telefono + ",'" + _cliente.poblacion + "','" + _cliente.email + "')");
		listaCliente.add(_cliente);

		mensajeDatosVNC.setVisible(true);
		mensajeDatosLNC.setVisible(true);
		mensajeDatosLNC.setText("Los datos se han insertado correctamente");
	    } catch (Exception e) {
		e.getMessage();
		mensajeDatosVNC.setVisible(true);
		mensajeDatosLNC.setVisible(true);
		mensajeDatosLNC.setText("No se han podido insertar los datos");
	    }
	}
    }

    /**
     * Modifica los datos de una mascota.
     *
     * @param m - Mascota(Objeto)
     */
    public void cambiaDatosMascota(Mascota m) {
	try {
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cita");

	    String sql = "UPDATE clinicaufvet.mascota "
		    + " SET nombre='" + m.nombre + "',sexo=" + m.sexo + ",especie='" + m.especie + "',raza='" + m.raza + "',fecha_nacimiento='" + m.fecha_nacimiento + "',cliente='" + m.cliente + "',img='" + m.img + "'"
		    + " WHERE chip=" + m.chip + "";

	    estado.executeUpdate(sql);
	    System.out.println("funciona");

	    datosCorrectosVNM.setVisible(true);
	    datosCorrectosLMN.setVisible(true);
	    datosCorrectosLMN.setText("Los datos se han cambiado correctamente");
	} catch (Exception e) {
	    e.getMessage();
	    System.out.println("no funciona");

	    datosCorrectosVNM.setVisible(true);
	    datosCorrectosLMN.setVisible(true);
	    datosCorrectosLMN.setText("No se han podido cambiar los datos");
	}

    }

    /**
     * Modifica los datos de un cliente.
     *
     * @param c - Cliente(Objeto)
     */
    public void cambiaDatosCliente(Cliente c) {
	try {
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cita");

	    String sql = "UPDATE clinicaufvet.cliente "
		    + " SET nombre='" + c.nombre + "',apellido='" + c.apellido + "',dni='" + c.dni + "',direccion='" + c.direccion + "',cp=" + c.cp + ",telefono=" + c.telefono + ",poblacion='" + c.poblacion + "',email='" + c.email + "'"
		    + " WHERE dni='" + c.dni + "'";

	    estado.executeUpdate(sql);
	    System.out.println("funciona");

	    mensajeDatosVNC.setVisible(true);
	    mensajeDatosLNC.setVisible(true);
	    mensajeDatosLNC.setText("Los datos se han cambiado correctamente");
	} catch (Exception e) {
	    e.getMessage();
	    System.out.println("no funciona");

	    mensajeDatosVNC.setVisible(true);
	    mensajeDatosLNC.setVisible(true);
	    mensajeDatosLNC.setText("No se han podido cambiar los datos");
	}

    }

    /**
     * Borra una cita.
     *
     * @param _cita - Cita(Objeto)
     */
    public void borraCitas(Cita _cita) {
	try {
	    resultadoConsulta = estado.executeQuery("SELECT * FROM clinicaufvet.cita");

	    String sql = "DELETE FROM clinicaufvet.cita "
		    + " WHERE fecha_cita='" + _cita.fecha_cita + "' AND mascota=" + _cita.mascota + "";

	    estado.executeUpdate(sql);
	    listaCita.remove(_cita);
	    System.out.println("funciona" + _cita.descripcion);

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

	datosCorrectosVNM.setVisible(false);
	datosCorrectosLMN.setVisible(false);

	mensajeDatosVNC.setVisible(false);
	mensajeDatosLNC.setVisible(false);

	datosCitasV.setVisible(false);
	datosCitasL.setVisible(false);
	try {
	    this.setIconImage(new ImageIcon(ImageIO.read(getClass().getResource("/imagenes/Auxiliares/logo.png"))).getImage());
	} catch (Exception e) {
	    this.setIconImage(null);
	}
	cambiaTitulo();
	ventanaBusquedaMascota.setTitle("Clinica UFVet - Busqueda Mascota");
	ventanaClienteNuevo.setTitle("Clinica UFVet - Añadir o Editar Cliente");
	ventanaMascotaNueva.setTitle("Clinica UFVet - Añadir o Editar Mascota");
	ventanaInsercionCitas.setTitle("Clinica UFVet - Nueva Cita");

	conexionBBDD();

    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
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
        datosCorrectosLMN = new javax.swing.JLabel();
        datosCorrectosVNM = new javax.swing.JLabel();
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
        mensajeDatosLNC = new javax.swing.JLabel();
        mensajeDatosVNC = new javax.swing.JLabel();
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
        cuadroCPostalLNC = new javax.swing.JTextField();
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
        datosCitasL = new javax.swing.JLabel();
        datosCitasV = new javax.swing.JLabel();
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
        insertarCitaIC = new javax.swing.JLabel();
        fondoCitas = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCitas = new javax.swing.JTable();
        huella = new javax.swing.JLabel();
        fotoAnimal = new javax.swing.JLabel();
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
        editarMascota = new javax.swing.JLabel();
        fondoMascotas = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
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
        cuadroEditarCliente = new javax.swing.JLabel();
        fondoClientes = new javax.swing.JLabel();

        ventanaMascotaNueva.setSize(new java.awt.Dimension(1020, 850));
        ventanaMascotaNueva.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setHorizontalScrollBar(null);
        jScrollPane3.setOpaque(false);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosCorrectosLMN.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel4.add(datosCorrectosLMN, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 744, 230, 50));

        datosCorrectosVNM.setBackground(new java.awt.Color(255, 255, 255));
        datosCorrectosVNM.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        datosCorrectosVNM.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        datosCorrectosVNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro1.png"))); // NOI18N
        jPanel4.add(datosCorrectosVNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 740, 230, 60));

        cuadroFNacimientoNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroFNacimientoNM.setMinimumSize(new java.awt.Dimension(440, 35));
        cuadroFNacimientoNM.setPreferredSize(new java.awt.Dimension(440, 35));
        jPanel4.add(cuadroFNacimientoNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 540, -1, -1));

        cuadroFNAciemientoTNM.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroFNAciemientoTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaNacimientoLetras.png"))); // NOI18N
        cuadroFNAciemientoTNM.setPreferredSize(new java.awt.Dimension(194, 35));
        cuadroFNAciemientoTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroFNAciemientoTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroFNAciemientoTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 540, -1, -1));

        cuadroFotoNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroFotoNM.setPreferredSize(new java.awt.Dimension(440, 35));
        cuadroFotoNM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuadroFotoNMActionPerformed(evt);
            }
        });
        jPanel4.add(cuadroFotoNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 750, -1, -1));

        cuadroFotoTNM.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroFotoTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaFotoLetras.png"))); // NOI18N
        cuadroFotoTNM.setPreferredSize(new java.awt.Dimension(194, 35));
        cuadroFotoTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroFotoTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroFotoTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 750, -1, -1));

        cuadroChipNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroChipNM.setPreferredSize(new java.awt.Dimension(440, 35));
        jPanel4.add(cuadroChipNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 610, -1, -1));

        cuadroChipTNM.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroChipTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaChipLetras.png"))); // NOI18N
        cuadroChipTNM.setPreferredSize(new java.awt.Dimension(194, 35));
        cuadroChipTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroChipTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroChipTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 610, -1, -1));

        cuadroSexoNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroSexoNM.setPreferredSize(new java.awt.Dimension(440, 35));
        jPanel4.add(cuadroSexoNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 470, -1, -1));

        cuadroSexoTNM.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroSexoTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaSexoLetras.png"))); // NOI18N
        cuadroSexoTNM.setPreferredSize(new java.awt.Dimension(194, 35));
        cuadroSexoTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroSexoTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroSexoTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 470, -1, -1));

        cuadroNombreNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroNombreNM.setPreferredSize(new java.awt.Dimension(440, 35));
        jPanel4.add(cuadroNombreNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 260, -1, -1));

        cuadroNombreTNM.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroNombreTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaNombreLetras.png"))); // NOI18N
        cuadroNombreTNM.setPreferredSize(new java.awt.Dimension(194, 35));
        cuadroNombreTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroNombreTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroNombreTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, -1, -1));

        cuadroEspecieNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroEspecieNM.setPreferredSize(new java.awt.Dimension(440, 35));
        jPanel4.add(cuadroEspecieNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, -1, -1));

        cuadroEspecieTNM.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroEspecieTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaEspecieLetras.png"))); // NOI18N
        cuadroEspecieTNM.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        cuadroEspecieTNM.setPreferredSize(new java.awt.Dimension(194, 35));
        cuadroEspecieTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroEspecieTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroEspecieTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 330, -1, -1));

        cuadroPropietarioNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroPropietarioNM.setPreferredSize(new java.awt.Dimension(440, 35));
        jPanel4.add(cuadroPropietarioNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 680, -1, -1));

        cuadroPropietarioTNM.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroPropietarioTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaPopietarioLetras.png"))); // NOI18N
        cuadroPropietarioTNM.setPreferredSize(new java.awt.Dimension(194, 35));
        cuadroPropietarioTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroPropietarioTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroPropietarioTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 680, -1, -1));

        cuadroRazaNM.setBackground(new java.awt.Color(204, 204, 255));
        cuadroRazaNM.setPreferredSize(new java.awt.Dimension(440, 35));
        jPanel4.add(cuadroRazaNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, -1, -1));

        cuadroRazaTNM.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cuadroRazaTNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaRazaLetras.png"))); // NOI18N
        cuadroRazaTNM.setPreferredSize(new java.awt.Dimension(194, 35));
        cuadroRazaTNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroRazaTNMMousePressed(evt);
            }
        });
        jPanel4.add(cuadroRazaTNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 400, -1, -1));

        botonGuardarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaGuardar.png"))); // NOI18N
        botonGuardarNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonGuardarNMMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonGuardarNMMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonGuardarNMMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                botonGuardarNMMouseReleased(evt);
            }
        });
        jPanel4.add(botonGuardarNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 860, -1, -1));

        botonEditarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaEditar.png"))); // NOI18N
        botonEditarNM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonEditarNMMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonEditarNMMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonEditarNMMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                botonEditarNMMouseReleased(evt);
            }
        });
        jPanel4.add(botonEditarNM, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 860, -1, -1));

        fondoNuevaMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascota.png"))); // NOI18N
        fondoNuevaMascota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fondoNuevaMascotaMousePressed(evt);
            }
        });
        jPanel4.add(fondoNuevaMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1004, 1006));

        jScrollPane3.setViewportView(jPanel4);

        ventanaMascotaNueva.getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        ventanaClienteNuevo.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane4.setPreferredSize(new java.awt.Dimension(1040, 600));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mensajeDatosLNC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(mensajeDatosLNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 750, 230, 60));

        mensajeDatosVNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro1.png"))); // NOI18N
        mensajeDatosVNC.setText("jLabel1");
        jPanel5.add(mensajeDatosVNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 750, 240, 60));

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

        cuadroCPostalLNC.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.add(cuadroCPostalLNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 690, 440, 35));

        cuadroCPostalTNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgClienteCPostalLetras.png"))); // NOI18N
        jPanel5.add(cuadroCPostalTNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(218, 686, 150, 40));

        cuadroEmailLNC.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.add(cuadroEmailLNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 760, 440, 35));

        cuadroEmailTNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgClienteEmailLetras.png"))); // NOI18N
        jPanel5.add(cuadroEmailTNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 756, 100, 40));

        botonGuardarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaGuardar.png"))); // NOI18N
        botonGuardarGNC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonGuardarGNCMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonGuardarGNCMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonGuardarGNCMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                botonGuardarGNCMouseReleased(evt);
            }
        });
        jPanel5.add(botonGuardarGNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 860, -1, -1));

        botonEditarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaEditar.png"))); // NOI18N
        botonEditarGNC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonEditarGNCMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonEditarGNCMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonEditarGNCMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                botonEditarGNCMouseReleased(evt);
            }
        });
        jPanel5.add(botonEditarGNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 860, -1, 60));

        fondoNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosCliente.png"))); // NOI18N
        fondoNC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fondoNCMousePressed(evt);
            }
        });
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
        cuadroTextoBM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroTextoBMMousePressed(evt);
            }
        });
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

        ventanaInsercionCitas.setResizable(false);
        ventanaInsercionCitas.setSize(new java.awt.Dimension(600, 431));
        ventanaInsercionCitas.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosCitasL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel6.add(datosCitasL, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, 230, 60));

        datosCitasV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasCuadro1.png"))); // NOI18N
        jPanel6.add(datosCitasV, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, -1, 60));

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
        jPanel6.add(labelVeterinarioCitas, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 264, 186, 33));

        cajaVeterinario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteCPostal.png"))); // NOI18N
        cajaVeterinario.setText("jLabel1");
        jPanel6.add(cajaVeterinario, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, 200, -1));

        insertarCitaIC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasInsertar.png"))); // NOI18N
        insertarCitaIC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                insertarCitaICMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                insertarCitaICMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                insertarCitaICMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                insertarCitaICMouseReleased(evt);
            }
        });
        jPanel6.add(insertarCitaIC, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, 170, 40));

        fondoCitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Auxiliares/pgBusquedaCitas.png"))); // NOI18N
        fondoCitas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fondoCitasMousePressed(evt);
            }
        });
        jPanel6.add(fondoCitas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 410));

        ventanaInsercionCitas.getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 930, 520));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1010, 700));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setHorizontalScrollBar(null);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(1030, 680));

        jTabbedPane2.setMinimumSize(new java.awt.Dimension(1012, 700));
        jTabbedPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane2MouseClicked(evt);
            }
        });

        jPanel1.setMinimumSize(new java.awt.Dimension(1002, 700));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaCitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Detalles", "Veterinario"
            }
        ));
        tablaCitas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaCitasMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tablaCitas);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 800, 510, 110));

        huella.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/huella.png"))); // NOI18N
        huella.setText("jLabel22");
        jPanel1.add(huella, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, 90, 90));

        fotoAnimal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fotoAnimal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fotoAnimal.setPreferredSize(new java.awt.Dimension(157, 166));
        jPanel1.add(fotoAnimal, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, -1, -1));

        cuadroNuevaMascota.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroNuevaMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasNuevaMascota.png"))); // NOI18N
        cuadroNuevaMascota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cuadroNuevaMascotaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cuadroNuevaMascotaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroNuevaMascotaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cuadroNuevaMascotaMouseReleased(evt);
            }
        });
        jPanel1.add(cuadroNuevaMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 220, -1, -1));

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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                insertaCitaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                insertaCitaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                insertaCitaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                insertaCitaMouseReleased(evt);
            }
        });
        jPanel1.add(insertaCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 744, 170, -1));

        borrarCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasBorrar.png"))); // NOI18N
        borrarCita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                borrarCitaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                borrarCitaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                borrarCitaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                borrarCitaMouseReleased(evt);
            }
        });
        jPanel1.add(borrarCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 744, 170, -1));

        buscarMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgClienteBotonBuscarMascota.png"))); // NOI18N
        buscarMascota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buscarMascotaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buscarMascotaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                buscarMascotaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                buscarMascotaMouseReleased(evt);
            }
        });
        jPanel1.add(buscarMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, -1, -1));

        editarMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasEditarMascota.png"))); // NOI18N
        editarMascota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                editarMascotaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                editarMascotaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                editarMascotaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                editarMascotaMouseReleased(evt);
            }
        });
        jPanel1.add(editarMascota, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 220, -1, -1));

        fondoMascotas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotas.png"))); // NOI18N
        fondoMascotas.setMaximumSize(new java.awt.Dimension(400, 400));
        jPanel1.add(fondoMascotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 970));

        jLabel1.setText("jLabel1");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 880, 70, 40));

        jLabel2.setText("jLabel2");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 830, 70, 40));

        jTabbedPane2.addTab("Mascota", jPanel1);

        jPanel2.setMinimumSize(new java.awt.Dimension(1010, 700));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cuadroBusquedaCliente.setBackground(new java.awt.Color(204, 204, 255));
        cuadroBusquedaCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cuadroBusquedaCliente.setText("BUSCAR CLIENTE");
        cuadroBusquedaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroBusquedaClienteMousePressed(evt);
            }
        });
        cuadroBusquedaCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cuadroBusquedaClienteKeyPressed(evt);
            }
        });
        jPanel2.add(cuadroBusquedaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 220, 50));

        cuadroNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonNuevoCliente.png"))); // NOI18N
        cuadroNuevoCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cuadroNuevoClienteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cuadroNuevoClienteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroNuevoClienteMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cuadroNuevoClienteMouseReleased(evt);
            }
        });
        jPanel2.add(cuadroNuevoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 220, -1, -1));

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
        cuadroCPostal.setPreferredSize(new java.awt.Dimension(189, 43));
        jPanel2.add(cuadroCPostal, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 665, -1, -1));

        cuadroCPostalF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cuadroCPostalF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteDNI.png"))); // NOI18N
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

        cuadroEditarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonEditarCliente.png"))); // NOI18N
        cuadroEditarCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cuadroEditarClienteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cuadroEditarClienteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cuadroEditarClienteMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cuadroEditarClienteMouseReleased(evt);
            }
        });
        jPanel2.add(cuadroEditarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 220, -1, -1));

        fondoClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgCliente.png"))); // NOI18N
        jPanel2.add(fondoClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -15, 1010, 1000));

        jTabbedPane2.addTab("Cliente", jPanel2);

        jScrollPane2.setViewportView(jTabbedPane2);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cuadroNuevaMascotaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevaMascotaMousePressed
	cuadroNuevaMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasNuevaMascotaP.png")));
	ventanaMascotaNueva.setVisible(true);
    }//GEN-LAST:event_cuadroNuevaMascotaMousePressed

    private void cuadroNuevoClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevoClienteMousePressed
	cuadroNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonNuevoClienteP.png")));
	ventanaClienteNuevo.setVisible(true);
    }//GEN-LAST:event_cuadroNuevoClienteMousePressed

    private void botonGuardarNMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGuardarNMMousePressed
	botonGuardarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaGuardarP.png")));
	Mascota mascota = new Mascota();

	mascota.chip = Integer.valueOf(cuadroChipNM.getText());
	mascota.nombre = cuadroNombreNM.getText();
	mascota.sexo = Integer.valueOf(cuadroSexoNM.getText());
	mascota.especie = cuadroEspecieNM.getText();
	mascota.raza = cuadroRazaNM.getText();
	mascota.fecha_nacimiento = cuadroFNacimientoNM.getText();
	mascota.cliente = cuadroPropietarioNM.getText();
	mascota.img = Integer.valueOf(cuadroFotoNM.getText());

	//System.out.println(nacimiento);
	insertaDatosMascota(mascota);
    }//GEN-LAST:event_botonGuardarNMMousePressed

    private void insertaCitaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertaCitaMousePressed
	insertaCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasInsertarP.png")));
	labelMascotaCitas.setText(cuadroChip.getText());
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
	botonEditarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaEditarP.png")));
	//Botón que cambia los datos en la ventana de mascotas. Obtenemos los datos que hemos sacado de la ventana principal
	// Y los guardamos en las variables que vamos a usar en el método.

	Mascota m = null;
	for (int i = 0; i < listaMascota.size(); i++) {
	    if (listaMascota.get(i).chip == Integer.valueOf(cuadroChipNM.getText())) {
		m = listaMascota.get(i);
		break;
	    }
	}

	m.nombre = cuadroNombreNM.getText();
	m.chip = Integer.valueOf(cuadroChipNM.getText());
	m.cliente = cuadroPropietarioNM.getText();
	m.especie = cuadroEspecieNM.getText();
	m.fecha_nacimiento = cuadroFNacimientoNM.getText();
	m.raza = cuadroRazaNM.getText();
	m.sexo = Integer.valueOf(cuadroSexoNM.getText());
	m.img = Integer.valueOf(cuadroFotoNM.getText());

	cambiaDatosMascota(m);
	escribeDatosMascota(m.chip);
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
	buscarMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgClienteBotonBuscarMascotaP.png")));
	ventanaBusquedaMascota.setVisible(true);
    }//GEN-LAST:event_buscarMascotaMousePressed

    private void cuadroBusquedaClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cuadroBusquedaClienteKeyPressed
	if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
	    escribeDatosCliente(cuadroBusquedaCliente.getText());
	}
    }//GEN-LAST:event_cuadroBusquedaClienteKeyPressed

    private void cuadroApellidosLNCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuadroApellidosLNCActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cuadroApellidosLNCActionPerformed

    private void botonGuardarGNCMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGuardarGNCMousePressed
	botonGuardarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaGuardarP.png")));
	Cliente cliente = new Cliente();

	cliente.dni = cuadroDNILNC.getText();
	cliente.nombre = cuadroNombreLNC.getText();
	cliente.apellido = cuadroApellidosLNC.getText();
	cliente.telefono = Integer.valueOf(cuadroTelefonoLNC.getText());
	cliente.direccion = cuadroDireccionLNC.getText();
	cliente.cp = Integer.valueOf(cuadroCPostalLNC.getText());
	cliente.email = cuadroEmailLNC.getText();
	cliente.poblacion = cuadroPoblacionLNC.getText();

	insertaDatosCliente(cliente);
    }//GEN-LAST:event_botonGuardarGNCMousePressed

    private void cuadroFotoNMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuadroFotoNMActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cuadroFotoNMActionPerformed

    private void insertarCitaICMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertarCitaICMousePressed
	insertarCitaIC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasInsertarP.png")));

	Cita cita = new Cita();
	cita.mascota = Integer.valueOf(labelMascotaCitas.getText());
	cita.descripcion = labelDescripcionCitas.getText();
	cita.fecha_cita = labelFechasCitas.getText();
	cita.veterinario = labelVeterinarioCitas.getText();
	insertaDatosCita(cita);
	escribeDatosMascota(cita.mascota);
	escribeCitas(cita.mascota);

    }//GEN-LAST:event_insertarCitaICMousePressed

    private void buscarMascotaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscarMascotaMouseReleased
	buscarMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgClienteBotonBuscarMascotaH.png")));
    }//GEN-LAST:event_buscarMascotaMouseReleased

    private void cuadroNuevaMascotaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevaMascotaMouseReleased
	cuadroNuevaMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasNuevaMascota.png")));
    }//GEN-LAST:event_cuadroNuevaMascotaMouseReleased

    private void insertaCitaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertaCitaMouseReleased
	insertaCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasInsertarH.png")));
    }//GEN-LAST:event_insertaCitaMouseReleased

    private void borrarCitaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrarCitaMousePressed
	borrarCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasBorrarP.png")));
	//
	Cita cita = new Cita();
	DefaultTableModel model = (DefaultTableModel) tablaCitas.getModel();
	//
	int filaSeleccionada = tablaCitas.getSelectedRow();
	model.removeRow(filaSeleccionada);

	String fecha = jLabel1.getText();
	int mascota = Integer.valueOf(jLabel2.getText());

	for (int i = 0; i < listaCita.size(); i++) {
	    if (listaCita.get(i).fecha_cita.equals(jLabel1.getText()) && listaCita.get(i).mascota == Integer.valueOf((jLabel2.getText()))) {
		cita = listaCita.get(i);
	    }
	}
	borraCitas(cita);
    }//GEN-LAST:event_borrarCitaMousePressed

    private void borrarCitaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrarCitaMouseReleased
	borrarCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasBorrarH.png")));
    }//GEN-LAST:event_borrarCitaMouseReleased

    private void cuadroNuevoClienteMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevoClienteMouseReleased
	cuadroNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonNuevoClienteH.png")));
    }//GEN-LAST:event_cuadroNuevoClienteMouseReleased

    private void botonEditarNMMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEditarNMMouseReleased
	botonEditarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaEditarH.png")));
    }//GEN-LAST:event_botonEditarNMMouseReleased

    private void botonGuardarNMMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGuardarNMMouseReleased
	botonGuardarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaGuardarH.png")));
    }//GEN-LAST:event_botonGuardarNMMouseReleased

    private void botonEditarGNCMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEditarGNCMousePressed
	botonEditarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaEditarP.png")));
	//
	Cliente c = null;
	for (int i = 0; i < listaCliente.size(); i++) {
	    if (listaCliente.get(i).dni.equals(cuadroDNILNC.getText())) {
		c = listaCliente.get(i);
		break;
	    }
	}

	c.apellido = cuadroApellidosLNC.getText();
	c.cp = Integer.valueOf(cuadroCPostalLNC.getText());
	c.direccion = cuadroDireccionLNC.getText();
	c.dni = cuadroDNILNC.getText();
	c.email = cuadroEmailLNC.getText();
	c.nombre = cuadroNombreLNC.getText();
	c.poblacion = cuadroPoblacionLNC.getText();
	c.telefono = Integer.valueOf(cuadroTelefonoLNC.getText());

	cambiaDatosCliente(c);
	escribeDatosCliente(c.dni);
    }//GEN-LAST:event_botonEditarGNCMousePressed

    private void botonEditarGNCMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEditarGNCMouseReleased
	botonEditarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaEditarH.png")));
    }//GEN-LAST:event_botonEditarGNCMouseReleased

    private void botonGuardarGNCMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGuardarGNCMouseReleased
	botonGuardarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaGuardarH.png")));
    }//GEN-LAST:event_botonGuardarGNCMouseReleased

    private void insertarCitaICMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertarCitaICMouseReleased
	insertarCitaIC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasInsertarH.png")));
    }//GEN-LAST:event_insertarCitaICMouseReleased

    private void cuadroTextoBMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroTextoBMMousePressed
	if (cuadroTextoBM.getText().equals("Nombre Mascota")) {
	    cuadroTextoBM.setText("");
	}
    }//GEN-LAST:event_cuadroTextoBMMousePressed

    private void cuadroBusquedaClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroBusquedaClienteMousePressed
	if (cuadroBusquedaCliente.getText().equals("BUSCAR CLIENTE")) {
	    cuadroBusquedaCliente.setText("");
	}
    }//GEN-LAST:event_cuadroBusquedaClienteMousePressed

    private void editarMascotaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editarMascotaMousePressed
	editarMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasEditarMascotaP.png")));
	//Abrimos la ventana de inserción de datos.
	ventanaMascotaNueva.setVisible(true);

	//Método que nos va a permitir modificar datos en la BBDD.
    }//GEN-LAST:event_editarMascotaMousePressed

    private void editarMascotaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editarMascotaMouseReleased
	editarMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasEditarMascotaH.png")));
    }//GEN-LAST:event_editarMascotaMouseReleased

    private void cuadroEditarClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroEditarClienteMousePressed
	cuadroEditarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonEditarClienteP.png")));
	//Abrimos la ventana de insercion de clientes
	ventanaClienteNuevo.setVisible(true);
    }//GEN-LAST:event_cuadroEditarClienteMousePressed

    private void cuadroEditarClienteMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroEditarClienteMouseReleased
	cuadroEditarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonEditarClienteH.png")));
    }//GEN-LAST:event_cuadroEditarClienteMouseReleased

    private void buscarMascotaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscarMascotaMouseEntered
	buscarMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgClienteBotonBuscarMascotaH.png")));
    }//GEN-LAST:event_buscarMascotaMouseEntered

    private void buscarMascotaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscarMascotaMouseExited
	buscarMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgClienteBotonBuscarMascota.png")));
    }//GEN-LAST:event_buscarMascotaMouseExited

    private void editarMascotaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editarMascotaMouseEntered
	editarMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasEditarMascotaH.png")));
    }//GEN-LAST:event_editarMascotaMouseEntered

    private void editarMascotaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editarMascotaMouseExited
	editarMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasEditarMascota.png")));
    }//GEN-LAST:event_editarMascotaMouseExited

    private void cuadroNuevaMascotaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevaMascotaMouseEntered
	cuadroNuevaMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasNuevaMascotaH.png")));
    }//GEN-LAST:event_cuadroNuevaMascotaMouseEntered

    private void cuadroNuevaMascotaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevaMascotaMouseExited
	cuadroNuevaMascota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasNuevaMascota.png")));
    }//GEN-LAST:event_cuadroNuevaMascotaMouseExited

    private void borrarCitaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrarCitaMouseEntered
	borrarCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasBorrarH.png")));
    }//GEN-LAST:event_borrarCitaMouseEntered

    private void borrarCitaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrarCitaMouseExited
	borrarCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasBorrar.png")));
    }//GEN-LAST:event_borrarCitaMouseExited

    private void insertaCitaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertaCitaMouseEntered
	insertaCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasInsertarH.png")));
    }//GEN-LAST:event_insertaCitaMouseEntered

    private void insertaCitaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertaCitaMouseExited
	insertaCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasInsertar.png")));
    }//GEN-LAST:event_insertaCitaMouseExited

    private void cuadroEditarClienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroEditarClienteMouseEntered
	cuadroEditarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonEditarClienteH.png")));
    }//GEN-LAST:event_cuadroEditarClienteMouseEntered

    private void cuadroEditarClienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroEditarClienteMouseExited
	cuadroEditarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonEditarCliente.png")));
    }//GEN-LAST:event_cuadroEditarClienteMouseExited

    private void cuadroNuevoClienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevoClienteMouseEntered
	cuadroNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonNuevoClienteH.png")));
    }//GEN-LAST:event_cuadroNuevoClienteMouseEntered

    private void cuadroNuevoClienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuadroNuevoClienteMouseExited
	cuadroNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cliente/pgClienteBotonNuevoCliente.png")));
    }//GEN-LAST:event_cuadroNuevoClienteMouseExited

    private void botonEditarNMMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEditarNMMouseEntered
	botonEditarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaEditarH.png")));
    }//GEN-LAST:event_botonEditarNMMouseEntered

    private void botonEditarNMMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEditarNMMouseExited
	botonEditarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaEditar.png")));
    }//GEN-LAST:event_botonEditarNMMouseExited

    private void botonGuardarNMMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGuardarNMMouseEntered
	botonGuardarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaGuardarH.png")));
    }//GEN-LAST:event_botonGuardarNMMouseEntered

    private void botonGuardarNMMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGuardarNMMouseExited
	botonGuardarNM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevaMascota/pgDatosMascotaGuardar.png")));
    }//GEN-LAST:event_botonGuardarNMMouseExited

    private void botonEditarGNCMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEditarGNCMouseEntered
	botonEditarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaEditarH.png")));
    }//GEN-LAST:event_botonEditarGNCMouseEntered

    private void botonEditarGNCMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEditarGNCMouseExited
	botonEditarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaEditar.png")));
    }//GEN-LAST:event_botonEditarGNCMouseExited

    private void botonGuardarGNCMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGuardarGNCMouseEntered
	botonGuardarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaGuardarH.png")));
    }//GEN-LAST:event_botonGuardarGNCMouseEntered

    private void botonGuardarGNCMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGuardarGNCMouseExited
	botonGuardarGNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/NuevoCliente/pgDatosMascotaGuardar.png")));
    }//GEN-LAST:event_botonGuardarGNCMouseExited

    private void insertarCitaICMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertarCitaICMouseEntered
	insertarCitaIC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasInsertarH.png")));
    }//GEN-LAST:event_insertarCitaICMouseEntered

    private void insertarCitaICMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertarCitaICMouseExited
	insertarCitaIC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mascotas/pgMascotasInsertar.png")));
    }//GEN-LAST:event_insertarCitaICMouseExited

    private void fondoNCMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fondoNCMousePressed
	mensajeDatosLNC.setVisible(false);
	mensajeDatosVNC.setVisible(false);
    }//GEN-LAST:event_fondoNCMousePressed

    private void fondoNuevaMascotaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fondoNuevaMascotaMousePressed
	datosCorrectosLMN.setVisible(false);
	datosCorrectosVNM.setVisible(false);
    }//GEN-LAST:event_fondoNuevaMascotaMousePressed

    private void fondoCitasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fondoCitasMousePressed
	datosCitasL.setVisible(false);
	datosCitasV.setVisible(false);
    }//GEN-LAST:event_fondoCitasMousePressed

    private void tablaCitasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaCitasMousePressed
	DefaultTableModel model = (DefaultTableModel) tablaCitas.getModel();
	int filaSeleccionada = tablaCitas.getSelectedRow();

	jLabel1.setText(model.getValueAt(filaSeleccionada, 0).toString());
	jLabel2.setText(cuadroChip.getText());
    }//GEN-LAST:event_tablaCitasMousePressed

    private void jTabbedPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseClicked
	cambiaTitulo();
    }//GEN-LAST:event_jTabbedPane2MouseClicked

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
    private javax.swing.JTextField cuadroCPostalLNC;
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
    private javax.swing.JLabel cuadroEditarCliente;
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
    private javax.swing.JLabel datosCitasL;
    private javax.swing.JLabel datosCitasV;
    private javax.swing.JLabel datosCorrectosLMN;
    private javax.swing.JLabel datosCorrectosVNM;
    private javax.swing.JLabel descripcionCitasTexto;
    private javax.swing.JLabel editarMascota;
    private javax.swing.JLabel fechaCitasTexto;
    private javax.swing.JLabel fondoBM;
    private javax.swing.JLabel fondoCitas;
    private javax.swing.JLabel fondoClientes;
    private javax.swing.JLabel fondoMascotas;
    private javax.swing.JLabel fondoNC;
    private javax.swing.JLabel fondoNuevaMascota;
    private javax.swing.JLabel fotoAnimal;
    private javax.swing.JLabel huella;
    private javax.swing.JLabel insertaCita;
    private javax.swing.JLabel insertarCitaIC;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
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
    private javax.swing.JLabel mensajeDatosLNC;
    private javax.swing.JLabel mensajeDatosVNC;
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
