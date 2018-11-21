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
		Placeur p = new Placeur(Param�tres.marge,Param�tres.largeur,Param�tres.hauteur,this);
		p.placer(new JLabel("num�ro de s�curit� sociale?"), 40);	p.placer(this.jtfnumss, 60);p.allerALaLigne();
		p.placer(new JLabel("nom?"), 40);	p.placer(this.jtfnompat, 60);p.allerALaLigne();
		p.placer(new JLabel("pr�nom?"), 40);	p.placer(this.jtfprenpat, 60);p.allerALaLigne();
		p.placer(new JLabel("date de naissance?"), 40);	p.placer(this.jtfdatenaiss, 60);p.allerALaLigne();
		p.placer(new JLabel("adresse?"), 40);	p.placer(this.jtfadrpat, 60);p.allerALaLigne();
		p.placer(new JLabel("code postal?"), 40);	p.placer(this.jtfcppat, 60);p.allerALaLigne();
		p.placer(new JLabel("t�l�phone fixe?"), 40);	p.placer(this.jtfnumfixepat, 60);p.allerALaLigne();
		p.placer(new JLabel("t�l�phone portable?"), 40);	p.placer(this.jtfnumportpat, 60);p.allerALaLigne();
		p.placer(new JLabel("num�ro du m�decin?"), 40);	p.placer(this.jtfnumordre, 60);p.allerALaLigne();
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

		String requ�te = "";
//		requ�te+= "INSERT INTO patient VALUES(";
//		requ�te+= "'"+this.jtfnumss.getText()+"',";
//		requ�te+= "'"+this.jtfnompat.getText()+"',";
//		requ�te+= "'"+this.jtfprenpat.getText()+"',";
//		requ�te+= "'"+this.jtfdatenaiss.getText()+"',";
//		requ�te+= "'"+this.jtfadrpat.getText()+"',";
//		requ�te+= "'"+this.jtfcppat.getText()+"',";
//		requ�te+= "'"+this.jtfnumfixepat.getText()+"',";
//		requ�te+= "'"+this.jtfnumportpat.getText()+"',";
//		requ�te+= "'"+this.jtfnumordre.getText()+"'";
//		requ�te+=	")";
//		JOptionPane.showMessageDialog(null, requ�te);
		/*pour le test de la jf*/
		requ�te = "INSERT INTO patient VALUES('1234','LEROY','Pierre','2013-12-27','8 rue des clos','94230','111111','22222','0123')";
		if(BDD.maj(requ�te))
		{
			this.jfMenu.actualiserLApplication();
			JOptionPane.showMessageDialog(this, "L'ajout a �t� pris en compte");
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
