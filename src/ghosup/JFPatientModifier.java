package ghosup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class JFPatientModifier extends JFrame implements ActionListener, WindowListener
{
	JFMenu			jfMenu;
	JComboBox<String> jcbNumSS	= BDD.laJCombo("SELECT `numss` FROM `patient` ORDER BY `numss`");
	JTextField	jtfnompat       = new JTextField();
	JTextField	jtfprenpat      = new JTextField();
	JTextField	jtfdatenaiss    = new JTextField();
	JTextField	jtfadrpat       = new JTextField();
	JTextField	jtfcppat        = new JTextField();
	JTextField	jtfnumfixepat   = new JTextField();
	JTextField	jtfnumportpat   = new JTextField();
	JTextField	jtfnumordre     = new JTextField();
	JButton			jbValider 			= new JButton("Valider");
	JButton			jbAbandonner		= new JButton("Abandonner");

	public JFPatientModifier(JFMenu jfMenu)
	{
		this.jfMenu = jfMenu;
		BDD.connecter("root", "");
		Placeur p = new Placeur(Paramètres.marge,Paramètres.largeur,Paramètres.hauteur,this);
		p.placer(new JLabel("numéro de sécurité sociale?"), 40);	p.placer(this.jcbNumSS, 60);p.allerALaLigne();
		p.placer(new JLabel("nom"), 40);	p.placer(this.jtfnompat, 60);p.allerALaLigne();
		p.placer(new JLabel("prénom"), 40);	p.placer(this.jtfprenpat, 60);p.allerALaLigne();
		p.placer(new JLabel("date de naissance"), 40);	p.placer(this.jtfdatenaiss, 60);p.allerALaLigne();
		p.placer(new JLabel("adresse"), 40);	p.placer(this.jtfadrpat, 60);p.allerALaLigne();
		p.placer(new JLabel("code postal"), 40);	p.placer(this.jtfcppat, 60);p.allerALaLigne();
		p.placer(new JLabel("téléphone fixe"), 40);	p.placer(this.jtfnumfixepat, 60);p.allerALaLigne();
		p.placer(new JLabel("téléphone portable"), 40);	p.placer(this.jtfnumportpat, 60);p.allerALaLigne();
		p.placer(new JLabel("numéro du médecin"), 40);	p.placer(this.jtfnumordre, 60);p.allerALaLigne();
		p.placer(this.jbValider, 50);	p.placer(this.jbAbandonner, 50);p.allerALaLigne();
		p.tracerFormulaire(100, 100);

		this.jtfnompat.setEditable(false);
		this.jtfprenpat.setEditable(false);
		this.jtfdatenaiss.setEditable(false);

		this.jcbNumSS.addActionListener(this);
		this.jbValider.addActionListener(this);
		this.jbAbandonner.addActionListener(this);

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Modifier un patient");
		this.addWindowListener(this);
	}

	public static void main(String[] args)
	{
//		BDD.connecter("root","");
//		new JFPatientModifier();
	}

	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == this.jcbNumSS)
		{
			this.rafraîchir((String)this.jcbNumSS.getSelectedItem());
		}
		if (ae.getSource() == this.jbValider)
		{
			String requête = "";
			requête+= "UPDATE patient SET ";
			requête+= "adrpat = '"+this.jtfadrpat.getText()+"',";
			requête+= "numfixepat = '"+this.jtfnumfixepat.getText()+"',";
			requête+= "numportpat = '"+this.jtfnumportpat.getText()+"',";
			requête+= "numordre = '"+this.jtfnumordre.getText()+"'";
			requête+=	"WHERE numSS = '"+(String)this.jcbNumSS.getSelectedItem()+"'";
			BDD.maj(requête);
			this.jfMenu.actualiserLApplication();
			JOptionPane.showMessageDialog(this, "La modification a été prise en compte");
		}
		if (ae.getSource() == this.jbAbandonner)
		{
			this.dispose();
		}
	}

	public void rafraîchir(String unNumSS)
	{
		try
		{
			ResultSet rs = BDD.leRésultat("SELECT * FROM patient WHERE numSS = '"+unNumSS+"'");
			rs.first();
			this.jtfnompat.setText( rs.getString("nompat"));
			this.jtfprenpat.setText( rs.getString("prenpat"));
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
		String numSSAffiché = (String) this.jcbNumSS.getSelectedItem();
		try
		{
			this.jcbNumSS.removeAllItems();
			String requête = "SELECT * FROM patient ORDER BY numss";
			ResultSet rs = BDD.leRésultat(requête);
			int i = 0, numItemSélectionné = 0;
			while(rs.next())
			{
				this.jcbNumSS.addItem(rs.getString("numss"));
				if(rs.getString("numss").compareTo(numSSAffiché)==0)
				{
					numItemSélectionné = i;	
					this.jtfnompat.setText( rs.getString("nompat"));
					this.jtfprenpat.setText( rs.getString("prenpat"));
					this.jtfdatenaiss.setText( rs.getString("datenaiss"));
					this.jtfadrpat.setText( rs.getString("adrpat"));
					this.jtfcppat.setText( rs.getString("cppat"));
					this.jtfnumfixepat.setText( rs.getString("numfixepat"));
					this.jtfnumportpat.setText( rs.getString("numportpat"));
					this.jtfnumordre.setText( rs.getString("numordre"));
				}
				i++;
			}
			this.jcbNumSS.revalidate();
			this.jcbNumSS.setSelectedIndex(numItemSélectionné);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "erreur dans JFPatientModifier : "+e.getMessage());
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
