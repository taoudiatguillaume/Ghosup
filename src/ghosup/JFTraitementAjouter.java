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
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class JFTraitementAjouter extends JFrame implements ActionListener
{
	JFMenu			jfMenu;
	String dateDébut 	= "2013-01-01";
	JTextField 				jtfDateDébut					= new JTextField();
	JComboBox<String> jcbnombreSéancesJour	= new JComboBox<>();
	JTextField 				jtfnumSSPatient				= new JTextField();
	JTextField 				jtfSoin0 = new JTextField(),jtfSoin1 = new JTextField(),jtfSoin2 = new JTextField(),jtfSoin3 = new JTextField(),jtfSoin4 = new JTextField();

	JButton						jbAjouter 						= new JButton("Ajouter le traitement");
	public JFTraitementAjouter(JFMenu jfMenu)
	{
		this.jfMenu = jfMenu;
		BDD.connecter("root", "");
		for (int i = 1; i <=3; i++)	this.jcbnombreSéancesJour.addItem(String.valueOf(i));
		Placeur p = new Placeur(Paramètres.marge,Paramètres.largeur,Paramètres.hauteur,this);
		p.placer(new JLabel("Date de début?"), 40);
		p.placer(this.jtfDateDébut, 60);
		p.allerALaLigne();
		p.placer(new JLabel("Nombre de séances par jour?"), 40);
		p.placer(this.jcbnombreSéancesJour, 60);
		p.allerALaLigne();
		p.placer(new JLabel("Numéro SS du patient?"), 40);
		p.placer(this.jtfnumSSPatient, 60);
		p.allerALaLigne();
		p.placer(new JLabel("Codes des soins?"), 100);
		p.allerALaLigne();
		p.placer(this.jtfSoin0, 20);p.placer(this.jtfSoin1, 20);p.placer(this.jtfSoin2, 20);p.placer(this.jtfSoin3, 20);p.placer(this.jtfSoin4, 20);
		p.allerALaLigne();
		p.placer(this.jbAjouter, 100);
		p.allerALaLigne();
		p.tracerFormulaire(100, 100);

		this.jbAjouter.addActionListener(this);
		this.setTitle("Ajouter un traitement");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args)
	{
		//		new JFTraitementAjouter();
	}

	public void actionPerformed(ActionEvent e)
	{
		String uneDateDébut = this.jtfDateDébut.getText();
		int unNombreSéances = new Integer((String)this.jcbnombreSéancesJour.getSelectedItem()).intValue();
		String unNumSS  = this.jtfnumSSPatient.getText();
		int numéroTraitement = BDD.nouveauNuméroTraitement();

		/*!! ne pas supprimer!!*/
		//		BDD.insérerDansTraitement(numéroTraitement,uneDateDébut,unNombreSéances, unNumSS);
		//		if(!this.jtfSoin0.getText().isEmpty())BDD.insérerDansPrescrire(numéroTraitement, this.jtfSoin0.getText());
		//		if(!this.jtfSoin1.getText().isEmpty())BDD.insérerDansPrescrire(numéroTraitement, this.jtfSoin1.getText());
		//		if(!this.jtfSoin2.getText().isEmpty())BDD.insérerDansPrescrire(numéroTraitement, this.jtfSoin2.getText());
		//		if(!this.jtfSoin3.getText().isEmpty())BDD.insérerDansPrescrire(numéroTraitement, this.jtfSoin3.getText());
		//		if(!this.jtfSoin4.getText().isEmpty())BDD.insérerDansPrescrire(numéroTraitement, this.jtfSoin4.getText());
		/*!! ne pas supprimer!!*/

		/*Jeu d'essai. A détruire au moment de la mise en exploitation*/
		boolean fait = true;
		if(!BDD.insérerDansTraitement(numéroTraitement,"2013-04-12",3, "1234"))fait = false;
		if(fait&&!BDD.insérerDansPrescrire(numéroTraitement, "01"))fait = false;
		if(fait&&!BDD.insérerDansPrescrire(numéroTraitement, "02"))fait = false;
		if(fait&&!BDD.insérerDansPrescrire(numéroTraitement, "03"))fait = false;
		if(fait) 
		{
			JOptionPane.showMessageDialog(this, "L'ajout a été pris en compte");
			this.jfMenu.actualiserLApplication();
		}
		else 
		{
			JOptionPane.showMessageDialog(this, "erreur lors de l'ajout du traitement");
		};
		/*
		 * Pour remettre la bdd en état
		 * 
		 		DELETE FROM `patient` WHERE `numSS`=1234;
		 		DELETE FROM `prescrire` WHERE `numTraitement`=229;
				DELETE FROM `traitement` WHERE `numTraitement`=229;
				DELETE FROM `rdv` WHERE `numTraitement`=229;
		 */
	}
}
