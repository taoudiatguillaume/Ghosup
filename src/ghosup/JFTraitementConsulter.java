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


public class JFTraitementConsulter extends JFrame implements ActionListener, WindowListener
{

	JFMenu			jfMenu;
	JTextField 	jtfnumss        = new JTextField();
	JComboBox<String> jcbNomPr�nPat = BDD.laJCombo("SELECT CONCAT(`nompat`, ' ', `prenpat`) FROM patient ORDER BY `nompat`,`prenpat`");
	JTextField	jtfdatenaiss    = new JTextField();
	Placeur p;
	int xFinal = 0; int yFinal = 0; int hauteurFinale = 0;int largeurFinale = 0;
	ArrayList<JLabel> jlTraitements = new ArrayList<JLabel>();// pour afficher les traitements

	public JFTraitementConsulter(JFMenu jfMenu)
	{
		this.jfMenu = jfMenu;
		BDD.connecter("root", "");
		p = new Placeur(Param�tres.marge,Param�tres.largeur,Param�tres.hauteur,this);
		p.placer(new JLabel("num�ro de s�curit� sociale"), 40);	p.placer(this.jtfnumss, 60);p.allerALaLigne();
		p.placer(new JLabel("nom pr�nom?"), 40);	p.placer(this.jcbNomPr�nPat, 60);p.allerALaLigne();
		p.tracerFormulaire(100, 100);

		this.jtfnumss.setEditable(false);
		this.jtfdatenaiss.setEditable(false);

		this.jcbNomPr�nPat.addActionListener(this);

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Consultation des traitements. Les traitements pour un patient");
		this.setVisible(false);
		this.xFinal = this.p.getX();
		this.yFinal = this.p.getY();
		this.hauteurFinale = this.p.getHauteurFormulaire();
		this.largeurFinale = this.p.getLargeurFormulaire();
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
			rs = BDD.leR�sultat("SELECT * FROM traitement WHERE numSSPatient = '"+this.jtfnumss.getText()+"'");
		
			p.pointer(this.xFinal, this.yFinal);
			p.setHauteurFormulaire(this.hauteurFinale);
			p.setLargeurFormulaire(this.largeurFinale);
			
			/* nettoyage du r�sultat pr�c�dent*/
			for (JLabel jl : this.jlTraitements)jl.setText(null);
			this.jlTraitements.clear();
			
			if(!rs.first())
			{
				this.jlTraitements.add(new JLabel("Pas de traitement pour ce patient"));
			}
			else
			{
				this.jlTraitements.add(new JLabel(rs.getString("dateDebut")));
				while(rs.next())
				{
					this.jlTraitements.add(new JLabel(rs.getString("dateDebut")));
				}
			}
		
			for (JLabel jl : this.jlTraitements)
			{
				p.placer(jl, 100);p.allerALaLigne();
			}
			p.tracerFormulaire(this.getX(), this.getY());

		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	// m�thode appliqu�e sur signal en provenance de JFMenu suite � la validation d'une autre fen�tre
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
				if(rs.getString("numss").compareTo(numSSAffich�)==0)
				{
					numItemS�lectionn� = i;
					this.jtfdatenaiss.setText( rs.getString("datenaiss"));
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
