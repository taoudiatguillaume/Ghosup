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
	String dateDébut 	= "2013-01-01";

	public JFASPlanningSemaine(String dateDébut)
	{
		this.dateDébut = dateDébut;
		BDD.connecter();
		Placeur p = new Placeur(2,1000,24,this);
		p.placer(new JLabel(), 12.5);
		String d = ""+dateDébut;
		for (int i  = 0; i <7; i++)
		{
			p.placer(new JLabel(d), 12.5);
			d = Utilitaires.incrémenter(d);
		}
		p.allerALaLigne();
		try
		{
			ResultSet rs = BDD.leRésultat("SELECT * FROM personnel");
			while(rs.next())
			{
				p.placer(new JLabel(rs.getString("nompers")), 12);
				d = ""+dateDébut;
				for (int i  = 0; i <7; i++)
				{
					if(BDD.estEnCongé(rs.getString("matricule"), d))
					{
						p.placer(new JTextField("absente"), 12.5);
					}
					else
					{
						//On affiche l'infobulle dans les jtf
						String s = BDD.emploiDuTemps(dateDébut, rs.getString("matricule"));
						JTextField jtf = new JTextField(s);
						//On rend les étiquettes transparentes
						jtf.setForeground(Color.WHITE);
						BDD.déconnecter();
						//Après avoir importer le MouseListener on configure ces derniers en fonction de la position
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
								//Modification pour ne pas apercevoir les écritures au survol
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
								//Affichage de l'infobulle dans une fenêtre
								JOptionPane.showMessageDialog(null,((JTextField)e.getSource()).getText());
							}
						});
						//Affichage de l'infobulle
						if(Paramètres.affichageInfoBulle == true)
						{
						String s1 = BDD.emploiDuTemps(dateDébut, rs.getString("matricule"));
						jtf.setToolTipText(s1);
						p.placer(jtf, 12.5);
						}
					}
					d = Utilitaires.incrémenter(d);
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
		BDD.déconnecter();
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