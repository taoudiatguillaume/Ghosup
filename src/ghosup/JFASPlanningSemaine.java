package ghosup;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class JFASPlanningSemaine extends JFrame implements MouseListener
{
	String dateD�but 	= "2013-01-01";

	public JFASPlanningSemaine(String dateD�but)
	{
		this.dateD�but = dateD�but;
		BDD.connecter();
		Placeur p = new Placeur(2,1000,24,this);
		p.placer(new JLabel(), 12.5);
		String d = ""+dateD�but;
		for (int i  = 0; i <7; i++)
		{
			p.placer(new JLabel(d), 12.5);
			d = Utilitaires.incr�menter(d);
		}
		p.allerALaLigne();
		try
		{
			ResultSet rs = BDD.leR�sultat("SELECT * FROM personnel");
			while(rs.next())
			{
				p.placer(new JLabel(rs.getString("nompers")), 12);
				d = ""+dateD�but;
				for (int i  = 0; i <7; i++)
				{
					if(BDD.estEnCong�(rs.getString("matricule"), d))
					{
						p.placer(new JTextField("absente"), 12.5);
					}
					else
					{
						//On affiche l'infobulle dans les jtf
						String s = BDD.emploiDuTemps(dateD�but, rs.getString("matricule"));
						JTextField jtf = new JTextField(s);
						//On rend les �tiquettes transparentes
						jtf.setForeground(Color.WHITE);
						BDD.d�connecter();
						//Apr�s avoir importer le MouseListener on configure ces derniers en fonction de la position
						//de la souris pour que les cases change de couleur au survol
						jtf.addMouseListener(new MouseListener() {

							@Override
							public void mouseReleased(MouseEvent e) {
								// TODO Auto-generated method stub

							}

							@Override
							public void mousePressed(MouseEvent e)
							{
							}

							@Override
							public void mouseExited(MouseEvent e)
							{
								// TODO Auto-generated method stub
								//Modification pour ne pas apercevoir les �critures au survol
								jtf.setBackground(Color.WHITE);
								jtf.setForeground(Color.WHITE);
							}

							@Override
							public void mouseEntered(MouseEvent e)
							{
								// TODO Auto-generated method stub
								jtf.setBackground(Color.CYAN);
								jtf.setForeground(Color.CYAN);
							}

							@Override
							public void mouseClicked(MouseEvent e)
							{
								//Affichage de l'infobulle dans une fen�tre
								JOptionPane.showMessageDialog(null,((JTextField)e.getSource()).getText());
							}
						});
						//Affichage de l'infobulle
						if(Param�tres.affichageInfoBulle == true)
						{
						String s1 = BDD.emploiDuTemps(dateD�but, rs.getString("matricule"));
						jtf.setToolTipText(s1);
						p.placer(jtf, 12.5);
						}
					}
					d = Utilitaires.incr�menter(d);
				}
				p.allerALaLigne();
			}
		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		p.tracerFormulaire(100, 100);
		this.setTitle("Emploi du temps");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		BDD.d�connecter();
	}

	public static void main(String[] args)
	{
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}