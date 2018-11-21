package ghosup;


import java.awt.*;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Placeur
{
	private int x, y;
	private int margeExterne;
	public int largeurFormulaire;
	private int hauteurComposant;
	private int hauteurFormulaire;
	private	Container conteneur;
	private ArrayList<Component> lesComposantsDeLargeurIndeterminee;

	
	public Placeur(int uneMargeExterne, int uneLargeurFormulaire,int uneHauteurComposant, Container unConteneur)
	{
		this.margeExterne = uneMargeExterne;
		this.largeurFormulaire = uneLargeurFormulaire;
		this.hauteurComposant = uneHauteurComposant;
		this.conteneur = unConteneur;
		this.x = 0;
		this.y = 0;
		this.hauteurFormulaire = 0;
		this.conteneur.setLayout(null);
		this.lesComposantsDeLargeurIndeterminee = new ArrayList<Component>();
	}
//	
	public int getX()
	{
		return this.x;
	}
	public int getY()
	{
		return this.y;
	}
	public int getHauteurFormulaire()
	{
		return this.hauteurFormulaire;
	}
	public int getLargeurFormulaire()
	{
		return this.largeurFormulaire;
	}
	public void setHauteurFormulaire(int uneHauteurFormulaire)
	{
		 this.hauteurFormulaire = uneHauteurFormulaire;
	}
	public void setLargeurFormulaire(int uneLargeurFormulaire)
	{
		this.largeurFormulaire = uneLargeurFormulaire;
	}
	public void augmenterHauteurFormulaire(int i)
  {
  	this.hauteurFormulaire += i;
  }
	
	public void augmenterLargeurFormulaire(int i)
	{
		this.largeurFormulaire += i;
	}
	
	public void setHauteurComposant(int hauteurComposant)
	{
		this.hauteurComposant = hauteurComposant;
	}

	public void placer(Component unComposant, double unPourcentage)
	{
		
		int largeurBoîte =( this.largeurFormulaire*(int)(unPourcentage*100))/10000;
		int largeurComposant = largeurBoîte - 2*this.margeExterne;
		this.conteneur.add(unComposant);
		unComposant.setBounds(x + this.margeExterne, y + this.margeExterne, largeurComposant, this.hauteurComposant);
		this.x += unComposant.getWidth() + 2 * this.margeExterne;
	}
	public void placer(Component unComposant, int uneLargeur, int uneHauteur)
	{
		this.conteneur.add(unComposant);
		unComposant.setBounds(x + this.margeExterne, y + this.margeExterne, uneLargeur, uneHauteur);
		this.x += uneLargeur + 2 * this.margeExterne;
		if(y+uneHauteur+2*this.margeExterne> this.hauteurFormulaire)this.hauteurFormulaire = y+uneHauteur+2*this.margeExterne;
	}

	public void allerALaLigne()
	{
		this.x = 0;
		this.y += hauteurComposant + 2 * margeExterne;
		if (this.y > this.hauteurFormulaire)this.hauteurFormulaire = y;
	}
	
	public void allerALaLigne(int unSaut)
	{
		this.x = 0;
		this.y += unSaut+this.margeExterne;
		if (this.y > this.hauteurFormulaire)this.hauteurFormulaire = y;
		
	}

	public void tracerFormulaire(int x, int y )// pour une JFrame
	{
		this.largeurFormulaire += 8;
		this.hauteurFormulaire += this.margeExterne+24;
		this.conteneur.setSize(this.largeurFormulaire, this.hauteurFormulaire);
		this.conteneur.setLocation(x, y);
	}
	public void pointer(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public static void main(String[] args)
  {
	  JFrame jf = new JFrame();
	  Placeur p = new Placeur(5, 300, 20,jf);
//	  JTextField jt = new JTextField("wcvn");
//	  jf.add(jt);
	  p.placer(new JTextField("azert"), 25);
	  p.placer(new JTextField("qsdfg"), 75);
	  p.allerALaLigne();
//	  p.tracerFormulaire();
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

