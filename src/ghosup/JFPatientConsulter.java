package ghosup;
/*
 * On suppose que
 * 	le patient est créé
 * 	la date de début n'appartient pas à un traitement en cours du patient
 * 	saisie du détail du traitement ( 5 soins maximum)
 * 
 * évolutions :
 * 	Bouton "Nouveau Patient"
 * 	Bouton "Prolonger le traitement d'un patient existant"
 * 	Saisie du détail sous la forme d'une liste déroulante
 * 	
 * 
 *	Exemple : 
 *		le traitement 179 pour le patient 2320375112008, 3 séances par jour, démarré le 2013-03-18 se termine 2013-04-16 
 * 		On peut donc créer un nouveau traitement pour le patient 2320375112008 à partir du 2013-04-17
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
	JComboBox<String>	jcbNomPrénPat = BDD.laJCombo("SELECT CONCAT(`nompat`, ' ', `prenpat`) FROM patient ORDER BY `nompat`,`prenpat`");
	JTextField				jtfdatenaiss  = new JTextField();
	JTextField				jtfadrpat     = new JTextField();
	JTextField				jtfcppat      = new JTextField();
	JTextField				jtfnumfixepat = new JTextField();
	JTextField				jtfnumportpat = new JTextField();
	JTextField				jtfnumordre   = new JTextField();

	public JFPatientConsulter(JFMenu jfMenu)
	{
		this.jfMenu = jfMenu;
		Placeur p = new Placeur(Paramètres.marge,Paramètres.largeur,Paramètres.hauteur,this);
		p.placer(new JLabel("numéro de sécurité sociale"), 40);	p.placer(this.jtfnumss, 60);p.allerALaLigne();
		p.placer(new JLabel("nom prénom?"), 40);	p.placer(this.jcbNomPrénPat, 60);p.allerALaLigne();
		p.placer(new JLabel("date de naissance"), 40);	p.placer(this.jtfdatenaiss, 60);p.allerALaLigne();
		p.placer(new JLabel("adresse"), 40);	p.placer(this.jtfadrpat, 60);p.allerALaLigne();
		p.placer(new JLabel("code postal"), 40);	p.placer(this.jtfcppat, 60);p.allerALaLigne();
		p.placer(new JLabel("téléphone fixe"), 40);	p.placer(this.jtfnumfixepat, 60);p.allerALaLigne();
		p.placer(new JLabel("téléphone portable"), 40);	p.placer(this.jtfnumportpat, 60);p.allerALaLigne();
		p.placer(new JLabel("numéro du médecin"), 40);	p.placer(this.jtfnumordre, 60);p.allerALaLigne();
		p.tracerFormulaire(100, 100);

		this.jtfnumss.setEditable(false);
		this.jtfdatenaiss.setEditable(false);
		this.jtfadrpat.setEditable(false);
		this.jtfcppat.setEditable(false);
		this.jtfnumfixepat.setEditable(false);
		this.jtfnumportpat.setEditable(false);
		this.jtfnumordre.setEditable(false);

		this.jcbNomPrénPat.addActionListener(this);

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
		if (ae.getSource() == this.jcbNomPrénPat)
		{
			this.rafraîchir((String)this.jcbNomPrénPat.getSelectedItem());
		}
	}

	public void rafraîchir(String unNomPrénom)
	{
		try
		{
			ResultSet rs = BDD.leRésultat("SELECT * FROM patient WHERE CONCAT(`nompat`, ' ', `prenpat`) = '"+unNomPrénom+"'");
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
		String numSSAffiché = this.jtfnumss.getText();
		try
		{
			this.jcbNomPrénPat.removeAllItems();
			String requête = "SELECT CONCAT(`nompat`, ' ', `prenpat`) ,numss, datenaiss, adrpat, cppat, numfixepat, numportpat, numordre FROM patient ORDER BY CONCAT(`nompat`, ' ', `prenpat`)";
			ResultSet rs = BDD.leRésultat(requête);
			int i = 0, numItemSélectionné = 0;
			while(rs.next())
			{
				this.jcbNomPrénPat.addItem(rs.getString(1));
				if(rs.getString(2).compareTo(numSSAffiché)==0)
				{
					numItemSélectionné = i;
					this.jtfdatenaiss.setText( rs.getString("datenaiss"));
					this.jtfadrpat.setText( rs.getString("adrpat"));
					this.jtfcppat.setText( rs.getString("cppat"));
					this.jtfnumfixepat.setText( rs.getString("numfixepat"));
					this.jtfnumportpat.setText( rs.getString("numportpat"));
					this.jtfnumordre.setText( rs.getString("numordre"));
				}
				i++;
			}
			this.jcbNomPrénPat.revalidate();
			this.jcbNomPrénPat.setSelectedIndex(numItemSélectionné);
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
