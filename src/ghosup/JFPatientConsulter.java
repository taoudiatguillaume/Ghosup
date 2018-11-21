package ghosup;
/*
 * On suppose que
 * 	le patient est cr��
 * 	la date de d�but n'appartient pas � un traitement en cours du patient
 * 	saisie du d�tail du traitement ( 5 soins maximum)
 * 
 * �volutions :
 * 	Bouton "Nouveau Patient"
 * 	Bouton "Prolonger le traitement d'un patient existant"
 * 	Saisie du d�tail sous la forme d'une liste d�roulante
 * 	
 * 
 *	Exemple : 
 *		le traitement 179 pour le patient 2320375112008, 3 s�ances par jour, d�marr� le 2013-03-18 se termine 2013-04-16 
 * 		On peut donc cr�er un nouveau traitement pour le patient 2320375112008 � partir du 2013-04-17
 * */
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


public class JFPatientConsulter extends JFrame implements ActionListener, WindowListener
{

	JFMenu						jfMenu;
	JTextField				jtfnumss      = new JTextField();
	JComboBox<String>	jcbNomPr�nPat = BDD.laJCombo("SELECT CONCAT(`nompat`, ' ', `prenpat`) FROM patient ORDER BY `nompat`,`prenpat`");
	JTextField				jtfdatenaiss  = new JTextField();
	JTextField				jtfadrpat     = new JTextField();
	JTextField				jtfcppat      = new JTextField();
	JTextField				jtfnumfixepat = new JTextField();
	JTextField				jtfnumportpat = new JTextField();
	JTextField				jtfnumordre   = new JTextField();

	public JFPatientConsulter(JFMenu jfMenu)
	{
		this.jfMenu = jfMenu;
		Placeur p = new Placeur(Param�tres.marge,Param�tres.largeur,Param�tres.hauteur,this);
		p.placer(new JLabel("num�ro de s�curit� sociale"), 40);	p.placer(this.jtfnumss, 60);p.allerALaLigne();
		p.placer(new JLabel("nom pr�nom?"), 40);	p.placer(this.jcbNomPr�nPat, 60);p.allerALaLigne();
		p.placer(new JLabel("date de naissance"), 40);	p.placer(this.jtfdatenaiss, 60);p.allerALaLigne();
		p.placer(new JLabel("adresse"), 40);	p.placer(this.jtfadrpat, 60);p.allerALaLigne();
		p.placer(new JLabel("code postal"), 40);	p.placer(this.jtfcppat, 60);p.allerALaLigne();
		p.placer(new JLabel("t�l�phone fixe"), 40);	p.placer(this.jtfnumfixepat, 60);p.allerALaLigne();
		p.placer(new JLabel("t�l�phone portable"), 40);	p.placer(this.jtfnumportpat, 60);p.allerALaLigne();
		p.placer(new JLabel("num�ro du m�decin"), 40);	p.placer(this.jtfnumordre, 60);p.allerALaLigne();
		p.tracerFormulaire(100, 100);

		this.jtfnumss.setEditable(false);
		this.jtfdatenaiss.setEditable(false);
		this.jtfadrpat.setEditable(false);
		this.jtfcppat.setEditable(false);
		this.jtfnumfixepat.setEditable(false);
		this.jtfnumportpat.setEditable(false);
		this.jtfnumordre.setEditable(false);

		this.jcbNomPr�nPat.addActionListener(this);

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Consulter les patients");
	}

	public static void main(String[] args)
	{
		//		BDD.connecter("root","");
		//		new JFPatientConsulter();
	}

	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == this.jcbNomPr�nPat)
		{
			this.rafra�chir((String)this.jcbNomPr�nPat.getSelectedItem());
		}
	}

	public void rafra�chir(String unNomPr�nom)
	{
		try
		{
			ResultSet rs = BDD.leR�sultat("SELECT * FROM patient WHERE CONCAT(`nompat`, ' ', `prenpat`) = '"+unNomPr�nom+"'");
			rs.first();
			this.jtfnumss.setText( rs.getString("numss"));
			this.jtfdatenaiss.setText( rs.getString("datenaiss"));
			this.jtfadrpat.setText( rs.getString("adrpat"));
			this.jtfcppat.setText( rs.getString("cppat"));
			this.jtfnumfixepat.setText( rs.getString("numfixepat"));
			this.jtfnumportpat.setText( rs.getString("numportpat"));
			this.jtfnumordre.setText( rs.getString("numordre"));
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	public void actualiser()
	{
		String numSSAffich� = this.jtfnumss.getText();
		try
		{
			this.jcbNomPr�nPat.removeAllItems();
			String requ�te = "SELECT CONCAT(`nompat`, ' ', `prenpat`) ,numss, datenaiss, adrpat, cppat, numfixepat, numportpat, numordre FROM patient ORDER BY CONCAT(`nompat`, ' ', `prenpat`)";
			ResultSet rs = BDD.leR�sultat(requ�te);
			int i = 0, numItemS�lectionn� = 0;
			while(rs.next())
			{
				this.jcbNomPr�nPat.addItem(rs.getString(1));
				if(rs.getString(2).compareTo(numSSAffich�)==0)
				{
					numItemS�lectionn� = i;
					this.jtfdatenaiss.setText( rs.getString("datenaiss"));
					this.jtfadrpat.setText( rs.getString("adrpat"));
					this.jtfcppat.setText( rs.getString("cppat"));
					this.jtfnumfixepat.setText( rs.getString("numfixepat"));
					this.jtfnumportpat.setText( rs.getString("numportpat"));
					this.jtfnumordre.setText( rs.getString("numordre"));
				}
				i++;
			}
			this.jcbNomPr�nPat.revalidate();
			this.jcbNomPr�nPat.setSelectedIndex(numItemS�lectionn�);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "erreur dans JFPatientConsulter : "+e.getMessage());
		}
	}
	public void windowActivated(WindowEvent we){}
	public void windowClosed(WindowEvent we){}
	public void windowClosing(WindowEvent we){this.jfMenu.actualiserLApplication();}
	public void windowDeactivated(WindowEvent we){}
	public void windowDeiconified(WindowEvent we){}
	public void windowIconified(WindowEvent we){}
	public void windowOpened(WindowEvent we){}
}
