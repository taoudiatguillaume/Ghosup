package ghosup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class JFConnexion extends JFrame implements ActionListener
{
	JTextField 		jtfNomUtilisateur 	= new JTextField("root");
	JPasswordField 	jpwUtilisateur 		= new JPasswordField();
	JButton			jbConnecter 		= new JButton("Connexion");

	public JFConnexion()
	{
		Placeur p = new Placeur(Paramètres.marge, Paramètres.largeur, Paramètres.hauteur, this);
		p.placer(new JLabel("nom d'utilisateur?"),50);p.placer(this.jtfNomUtilisateur, 50);p.allerALaLigne();
		p.placer(new JLabel("mot de passe?"),50); p.placer(this.jpwUtilisateur, 50);p.allerALaLigne();
		p.placer(this.jbConnecter,100);p.allerALaLigne();
		p.tracerFormulaire(100, 100);
		this.jbConnecter.addActionListener(this);
		this.setVisible(true);
		this.setTitle("-- GHOSUP --");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args)
	{
		new JFConnexion();
	}

	public void actionPerformed(ActionEvent e)
	{
		if(BDD.connecter(this.jtfNomUtilisateur.getText(), new String(this.jpwUtilisateur.getPassword())))
		{
			JOptionPane.showMessageDialog(this, "Vous êtes connecté");
			this.setVisible(false);
			new JFMenu();
		}
		else
		{
			JOptionPane.showMessageDialog(this, "nom d'utilisateur ou mot de passe incorrect");
		}
	}
}
