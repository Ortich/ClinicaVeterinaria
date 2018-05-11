/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Usuario
 */
public class Login extends javax.swing.JFrame {

    /* ----------------------------- Variables ------------------------------ */
    // Variables de conexion a la base de datos
    private Statement estado;
    private ResultSet resultadoConsulta;
    private Connection conexion;
    ArrayList<Veterinario> listaVeterinario = new ArrayList();
    boolean usuarioExiste = false;

    private void iniciaPrograma() {
	labelUserError.setText("");
	labelPassError.setText("");
	usuarioExiste = false;
	String pass = String.valueOf(passLogin.getPassword());
	for (int i = 0; i < listaVeterinario.size() && !usuarioExiste; i++) {
	    if (listaVeterinario.get(i).dni.equals(usuarioLogin.getText())) {
		if (listaVeterinario.get(i).pass.equals(pass)) {
		    VentanaPrincipal m = new VentanaPrincipal();
		    m.setVisible(true);
		    this.setVisible(false);
		} else {
		    labelPassError.setText("Contraseña incorrecta");
		}
		usuarioExiste = true;
	    } else if (!usuarioExiste) {
		labelUserError.setText("Usuario incorrecto");
	    }
	}
    }

    /**
     * Creates new form Login
     */
    public Login() {
	initComponents();

	try {
	    this.setIconImage(new ImageIcon(ImageIO.read(getClass().getResource("/imagenes/Auxiliares/logo.png"))).getImage());
	} catch (Exception e) {
	    this.setIconImage(null);
	}

	this.setTitle("Clinica UFVet - Login");

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/clinicaufvet", "root", "root");
	    estado = conexion.createStatement();
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
	} catch (Exception e) {
	    e.getMessage();
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

        labelUserError = new javax.swing.JLabel();
        usuarioLogin = new javax.swing.JTextField();
        labelPassError = new javax.swing.JLabel();
        login = new javax.swing.JButton();
        passLogin = new javax.swing.JPasswordField();
        fondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelUserError.setForeground(new java.awt.Color(255, 51, 51));
        getContentPane().add(labelUserError, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 350, 240, 20));

        usuarioLogin.setBackground(new java.awt.Color(204, 204, 255));
        usuarioLogin.setText("Usuario");
        usuarioLogin.setPreferredSize(new java.awt.Dimension(240, 33));
        usuarioLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                usuarioLoginMousePressed(evt);
            }
        });
        getContentPane().add(usuarioLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 313, -1, -1));

        labelPassError.setForeground(new java.awt.Color(255, 51, 51));
        getContentPane().add(labelPassError, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 420, 240, 20));

        login.setText("Login");
        login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                loginMousePressed(evt);
            }
        });
        getContentPane().add(login, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 480, 150, 40));

        passLogin.setText("Contraseña");
        passLogin.setPreferredSize(new java.awt.Dimension(240, 33));
        passLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                passLoginMousePressed(evt);
            }
        });
        passLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passLoginKeyPressed(evt);
            }
        });
        getContentPane().add(passLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 380, -1, -1));

        fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Login/pgLogin.png"))); // NOI18N
        getContentPane().add(fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginMousePressed
	iniciaPrograma();
    }//GEN-LAST:event_loginMousePressed

    private void usuarioLoginMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usuarioLoginMousePressed
	if (usuarioLogin.getText().equals("Usuario")) {
	    usuarioLogin.setText("");
	}
    }//GEN-LAST:event_usuarioLoginMousePressed

    private void passLoginMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_passLoginMousePressed
	String pass = String.valueOf(passLogin.getPassword());
	if (pass.equals("Contraseña")) {
	    passLogin.setText("");
	}
    }//GEN-LAST:event_passLoginMousePressed

    private void passLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passLoginKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            iniciaPrograma();
        }
    }//GEN-LAST:event_passLoginKeyPressed

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
	    java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		new Login().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fondo;
    private javax.swing.JLabel labelPassError;
    private javax.swing.JLabel labelUserError;
    private javax.swing.JButton login;
    private javax.swing.JPasswordField passLogin;
    private javax.swing.JTextField usuarioLogin;
    // End of variables declaration//GEN-END:variables
}
