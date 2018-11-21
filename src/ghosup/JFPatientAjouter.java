package ghosup;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class JFPatientAjouter extends JFrame implements ActionListener, WindowListener
{
	JFMenu			jfMenu;
	JTextField 	jtfnumss        = new JTextField();
	JTextField	jtfnompat       = new JTextField();
	JTextField	jtfprenpat      = new JTextField();
	JTextField	jtfdatenaiss    = new JTextField();
	JTextField	jtfadrpat       = new JTextField();
	JTextField	jtfcppat        = new JTextField();
	JTextField	jtfnumfixepat   = new JTextField();
	JTextField	jtfnumportpat   = new JTextField();
	JTextField	jtfnumordre     = new JTextField();
	JButton			jbAjouter 			= new JButton("Ajouter le patient");

	public JFPatientAjouter(JFMenu jfMenu)
	{
		this.jfMenu = jfMenu;
		Placeur p = new Placeur(Paramètres.marge,Paramètres.largeur,Paramètres.hauteur,this);
		p.placer(new JLabel("numéro de sécurité sociale?"), 40);	p.placer(this.jtfnumss, 60);p.allerALaLigne();
		p.placer(new JLabel("nom?"), 40);	p.placer(this.jtfnompat, 60);p.allerALaLigne();
		p.placer(new JLabel("prénom?"), 40);	p.placer(this.jtfprenpat, 60);p.allerALaLigne();
		p.placer(new JLabel("date de naissance?"), 40);	p.placer(this.jtfdatenaiss, 60);p.allerALaLigne();
		p.placer(new JLabel("adresse?"), 40);	p.placer(this.jtfadrpat, 60);p.allerALaLigne();
		p.placer(new JLabel("code postal?"), 40);	p.placer(this.jtfcppat, 60);p.allerALaLigne();
		p.placer(new JLabel("téléphone fixe?"), 40);	p.placer(this.jtfnumfixepat, 60);p.allerALaLigne();
		p.placer(new JLabel("téléphone portable?"), 40);	p.placer(this.jtfnumportpat, 60);p.allerALaLigne();
		p.placer(new JLabel("numéro du médecin?"), 40);	p.placer(this.jtfnumordre, 60);p.allerALaLigne();
		p.placer(new JLabel(), 40);	p.placer(this.jbAjouter, 60);p.allerALaLigne();
		p.tracerFormulaire(100, 100);
		this.jbAjouter.addActionListener(this);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Ajouter un patient");
		this.addWindowListener(this);
	}

	public static void main(String[] args)
	{
	}

	public void actionPerformed(ActionEvent e)
	{

		String requête = "";
//		requête+= "INSERT INTO patient VALUES(";
//		requête+= "'"+this.jtfnumss.getText()+"',";
//		requête+= "'"+this.jtfnompat.getText()+"',";
//		requête+= "'"+this.jtfprenpat.getText()+"',";
//		requête+= "'"+this.jtfdatenaiss.getText()+"',";
//		requête+= "'"+this.jtfadrpat.getText()+"',";
//		requête+= "'"+this.jtfcppat.getText()+"',";
//		requête+= "'"+this.jtfnumfixepat.getText()+"',";
//		requête+= "'"+this.jtfnumportpat.getText()+"',";
//		requête+= "'"+this.jtfnumordre.getText()+"'";
//		requête+=	")";
//		JOptionPane.showMessageDialog(null, requête);
		/*pour le test de la jf*/
		requête = "INSERT INTO patient VALUES('1234','LEROY','Pierre','2013-12-27','8 rue des clos','94230','111111','22222','0123')";
		if(BDD.maj(requête))
		{
			this.jfMenu.actualiserLApplication();
			JOptionPane.showMessageDialog(this, "L'ajout a été pris en compte");
		}
	}
	public void actualiser()
	{
		/**/
	}

	public void windowActivated(WindowEvent we){}
	public void windowClosed(WindowEvent we){}
	public void windowClosing(WindowEvent we){this.jfMenu.actualiserLApplication();}
	public void windowDeactivated(WindowEvent we){}
	public void windowDeiconified(WindowEvent we){}
	public void windowIconified(WindowEvent we){}
	public void windowOpened(WindowEvent we){}
}
