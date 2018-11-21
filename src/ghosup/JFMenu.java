package ghosup;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.tools.Tool;

public class JFMenu extends JFrame implements ActionListener
{
	JPanel 		jpMenu = new JPanel();
	JPanel 		jpTitre = new JPanel();
	JButton1 	jbPatientAjouter 			= new JButton1("Ajouter");
	JButton1 	jbPatientModifier 			= new JButton1("Modifier");
	JButton1 	jbPatientConsulter			= new JButton1("Consulter");
	JButton1 	jbTraitementAjouter 		= new JButton1("Ajouter");
	JButton1 	jbTraitementConsulter 		= new JButton1("Consulter");
	JButton1 	jbTraitementEnCours	 		= new JButton1("En cours");
	JButton1 	jbPlanningSemaine 			= new JButton1("Planning semaine");// Pr�sence/Absence des Aides-Soignantes pour une semaine
	JButton1 	jbPlanningJourn�e 			= new JButton1("Planning journ�e");// Planning pour une AS � une date donn�e
	JButton1 	jbQuitter 					= new JButton1("Quitter l'application");
	JTextArea 	jtaInfos					= new JTextArea();
	//Cr�ation du radio bouton
	JRadioButton jrb0	= new JRadioButton("Infobulle", true);
	JPanel 		jpAppli= new JPanel();

	/*les fen�tres g�r�es par l'appli. Elles sont invisibles � la cr�ation*/
	JFPatientAjouter 			jfPatientAjouter 			= new JFPatientAjouter(this);
	JFPatientModifier			jfPatientModifier 			= new JFPatientModifier(this);
	JFPatientConsulter 			jfPatientConsulter			=	new JFPatientConsulter(this);
	JFTraitementAjouter			jfTraitementAJouter			=	new JFTraitementAjouter(this);
	JFTraitementConsulter		jfTraitementConsulter		=	new JFTraitementConsulter(this);


	public JFMenu()
	{
		// r�cup�ration des dimensions de l'�cran. La fen�tre doit occuper la totalit� de l'�cran
		Toolkit leKit = this.getToolkit();Dimension tailleFenetre = leKit.getScreenSize();
		this.setBounds(0, 0, tailleFenetre.width, tailleFenetre.height);

		this.setLayout(null);

		// Cr�ation du panel jpTitre
		this.add(this.jpTitre);
		this.jpTitre.setBackground(Param�tres.couleur1);
		this.jpTitre.setBounds(0, 0, tailleFenetre.width, tailleFenetre.height*5/100);
		JLabel jlTitre = new JLabel("-- GHOSUP --");
		jlTitre.setFont(new Font("TimesRoman",Font.BOLD+Font.ITALIC,30));
		jlTitre.setForeground(Param�tres.couleur3);
		this.jpTitre.add(jlTitre);


		// Cr�ation du panel jpMenu
		this.add(this.jpMenu);
		this.jpMenu.setBackground(Param�tres.couleur1);
		this.jpMenu.setBounds(0, this.jpTitre.getHeight(), tailleFenetre.width*15/100, tailleFenetre.height);
		Placeur p = new Placeur( Param�tres.marge, this.jpMenu.getWidth(), Param�tres.hauteur, this.jpMenu);
		JLabel1 jl1;
		jl1 = new JLabel1("Patients");p.placer(jl1, 100);p.allerALaLigne();
		p.placer(new JLabel(), 10);	p.placer(this.jbPatientAjouter, 80);	this.jbPatientAjouter.addActionListener(this);p.allerALaLigne();
		p.placer(new JLabel(), 10);	p.placer(this.jbPatientModifier, 80);	this.jbPatientModifier.addActionListener(this);p.allerALaLigne();
		p.placer(new JLabel(), 10);	p.placer(this.jbPatientConsulter, 80);	this.jbPatientConsulter.addActionListener(this);p.allerALaLigne();

		jl1 = new JLabel1("Traitements");p.placer(jl1, 100);p.allerALaLigne();
		p.placer(new JLabel(), 10);	p.placer(this.jbTraitementAjouter, 80);	this.jbTraitementAjouter.addActionListener(this);p.allerALaLigne();
		p.placer(new JLabel(), 10);	p.placer(this.jbTraitementConsulter, 80);	this.jbTraitementConsulter.addActionListener(this);p.allerALaLigne();
		p.placer(new JLabel(), 10);	p.placer(this.jbTraitementEnCours, 80);	this.jbTraitementEnCours.addActionListener(this);p.allerALaLigne();

		jl1 = new JLabel1("Aides-soignantes");p.placer(jl1, 100);p.allerALaLigne();
		p.placer(new JLabel(), 10);	p.placer(this.jbPlanningSemaine, 80);	this.jbPlanningSemaine.addActionListener(this);p.allerALaLigne();
		p.placer(new JLabel(), 10);	p.placer(this.jbPlanningJourn�e, 80);	this.jbPlanningJourn�e.addActionListener(this);p.allerALaLigne();

		p.allerALaLigne();
		p.placer(this.jbQuitter, 100);	this.jbQuitter.addActionListener(this);p.allerALaLigne();
		p.allerALaLigne();
		
		//Cr�ation des boutons radios
		p.placer(this.jrb0, 100); this.jrb0.addActionListener(this);
		p.allerALaLigne();

		jl1 = new JLabel1("infos");p.placer(jl1, 100);

		p.allerALaLigne();

		p.setHauteurComposant(8*Param�tres.hauteur);
		p.placer(this.jtaInfos, 100);

		this.jtaInfos.setBackground(Param�tres.couleur1);
		this.jtaInfos.setBorder(BorderFactory.createLineBorder(Color.blue));
		p.setHauteurComposant(Param�tres.hauteur);
		p.allerALaLigne();
		// Cr�ation du panel jpAppli
		this.add(this.jpAppli);
		this.jpAppli.setBackground(Param�tres.couleur2);
		this.jpAppli.setBounds(this.jpMenu.getWidth(),  this.jpTitre.getHeight(), tailleFenetre.width*85/100, tailleFenetre.height);

		this.setUndecorated(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.jbPlanningSemaine.setToolTipText("Pr�sence/Absence des Aides-Soignantes pour une semaine");
		this.jbPlanningJourn�e.setToolTipText("Planning pour une AS � une date donn�e");
	}

	public static void main(String[] args)
	{
		//ID de connexion au serveur
		//BDD.connecter("root", "");
		BDD.connecter("root", "");
		new JFMenu();
	}

	public void actionPerformed(ActionEvent e)
	{
		int x = this.jpMenu.getWidth(),y = this.jpTitre.getHeight();
		if (e.getSource() == this.jbPatientAjouter)
		{
			this.jfPatientAjouter.setVisible(true);
			this.jfPatientAjouter.setLocation(x,y);
			this.jfPatientAjouter.setAlwaysOnTop(true);
			return;
		}
		if (e.getSource() == this.jbPatientModifier)
		{
			this.jfPatientModifier.setVisible(true);
			this.jfPatientModifier.setLocation(x,y);
			this.jfPatientModifier.setAlwaysOnTop(true);

			return;
		}
		if (e.getSource() == this.jbPatientConsulter)
		{
			this.jfPatientConsulter.setVisible(true);
			this.jfPatientConsulter.setLocation(x,y);
			this.jfPatientConsulter.setAlwaysOnTop(true);
			return;
		}
		if (e.getSource() == this.jbTraitementAjouter)
		{
			this.jfTraitementAJouter.setVisible(true);
			this.jfTraitementAJouter.setLocation(x,y);
			this.jfTraitementAJouter.setAlwaysOnTop(true);
			return;
		}
		if (e.getSource() == this.jbTraitementConsulter)
		{
			this.jfTraitementConsulter.setVisible(true);
			this.jfTraitementConsulter.setLocation(x,y);
			this.jfTraitementConsulter.setAlwaysOnTop(true);
			return;
		}
		if (e.getSource() == this.jbPlanningSemaine)
		{
			//Insertion du JOptionPane au clique de JbPlanningSemaine
			int confirm = JOptionPane.showConfirmDialog(null, "Date du jour ?");
			//si oui
			if (confirm==0)
			{
				Calendar c = Calendar.getInstance();
				String jtfJour 	= String.valueOf((c.get(Calendar.DAY_OF_MONTH)));
				String jtfMois 	= String.valueOf(c.get(Calendar.MONTH) + 1);
				String jtfAnn�e = String.valueOf(c.get(Calendar.YEAR));
				JFASPlanningSemaine jfEmploiDuTemps0 = new JFASPlanningSemaine(jtfAnn�e+"-"+jtfMois+"-"+jtfJour);
				jfEmploiDuTemps0.setLocation(x,y);
			}
			//si non
			if (confirm==1)
			{
				JDSaisieDate jdSaisieDate = new JDSaisieDate(null,true,"Date de d�but?",x,y);
				jdSaisieDate.setVisible(true);
				if(jdSaisieDate.getAnswer())
				{
					JFASPlanningSemaine jfEmploiDuTemps0 = new JFASPlanningSemaine(jdSaisieDate.getDate());
					jfEmploiDuTemps0.setLocation(x,y);
					//jfEmploiDuTemps0.setAlwaysOnTop(true);
					return;
				}
				return;
			}
			return;
		}
		if (e.getSource() == this.jbPlanningJourn�e)
		{
			JDSaisieDate jdSaisieDate = new JDSaisieDate(null,true,"Date de d�but?",x,y);
			jdSaisieDate.setVisible(true);
			if(jdSaisieDate.getAnswer())
			{
				JFASPlanningJourn�e jfasPlanningJourn�e = new JFASPlanningJourn�e(	jdSaisieDate.getDate());
				jfasPlanningJourn�e.setLocation(x,y);
				jfasPlanningJourn�e.setAlwaysOnTop(true);
				return;
			}
			return;
		}
		if(e.getSource() == this.jrb0)
		{
			if(this.jrb0.isSelected())
			{
				Param�tres.affichageInfoBulle = true;
			}
			else
			{
				Param�tres.affichageInfoBulle = false;
			}
		}
		if (e.getSource() == this.jbQuitter)
		{
			System.exit(0);
		}
	}
	public void actualiserLApplication()
	{
			/*System.out.println("JFMenu : "+172);*/jfPatientAjouter.actualiser();
			/*System.out.println("JFMenu : "+173);*/jfPatientModifier.actualiser();
			/*System.out.println("JFMenu : "+174);*/jfPatientConsulter.actualiser();
			/*System.out.println("JFMenu : "+174);*/jfTraitementConsulter.actualiser();
	}
}
