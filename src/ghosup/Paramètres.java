package ghosup;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Paramètres
{
	public static int marge = 2;
	public static int hauteur = 24;
	public static int largeur = 500;

	public static Color couleur1 = new Color(100,255,255);
	public static Color couleur2 = new Color(200,255,255);
	public static Color couleur3 = Color.blue;

	public static Font f1 = new Font("TimesRoman",Font.BOLD+Font.ITALIC,18);
	public static boolean affichageInfoBulle = true;
}

class JButton1 extends JButton implements MouseListener
{
	public JButton1(String texte)
	{
		super(texte);
		this.setFont( new Font("TimesRoman",Font.BOLD+Font.ITALIC,16));
		this.setForeground(Color.blue);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.addMouseListener(this);
	}

	public void mouseClicked(MouseEvent arg0){}
	public void mouseEntered(MouseEvent arg0){this.setForeground(Color.red);}
	public void mouseExited(MouseEvent arg0){	this.setForeground(Color.blue);}
	public void mousePressed(MouseEvent arg0){}
	public void mouseReleased(MouseEvent arg0){}
}

class JLabel1 extends JLabel
{
	public JLabel1(String texte)
	{
		super(texte);
		this.setForeground(Paramètres.couleur3);
		this.setFont(Paramètres.f1);
	}
}
