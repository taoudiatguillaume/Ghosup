package ghosup;
/*pbs :
 *  trop d'accès à la base => durée!!
 *  ppe minimiser ces accès : plus de fonction estAbsente..voir JFEmploiDuTemps.1
 *
 *
 * */

//
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


public class JFASPlanningJournée extends JFrame implements ActionListener
{
	String 						datePlanning 	= "";
	JComboBox<String> jcbAidesSoignantes = BDD.laJCombo("SELECT CONCAT(`matricule`,' : ',`nompers`)FROM personnel ");
	JTextField[] 			jtfCréneaux = new JTextField[8];

	public JFASPlanningJournée(String datePlanning)
	{
		this.datePlanning = datePlanning;
		BDD.connecter();
		Placeur p = new Placeur(2,600,24,this);
		p.placer(this.jcbAidesSoignantes,100);p.allerALaLigne();
		for (int i  = 0; i <8; i++)
		{
			this.jtfCréneaux[i]= new JTextField(String.valueOf(i+1));
			p.placer(this.jtfCréneaux[i], 100);p.allerALaLigne();
		}
		this.jcbAidesSoignantes.addActionListener(this);
		p.tracerFormulaire(100, 100);
		this.setTitle("Journée d'une aide-soignante");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		BDD.déconnecter();
	}

	public static void main(String[] args)
	{

	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		String matricule =(String) this.jcbAidesSoignantes.getSelectedItem();
		matricule = matricule.substring(0,4);
		for (int i = 0; i < 8; i++)
		{
			this.jtfCréneaux[i].setText(String.valueOf(i+1));
		}
		try
		{
			String requête = "SELECT `heureRdv`,`nompat` ,`adrpat` "+
												"FROM `rdv`, traitement, patient "+
												"WHERE `rdv`.`numTraitement`= traitement.`numTraitement`"+
												"AND traitement.numSSPatient = patient.`numss`"+
												"AND `matricule`='"+matricule+"'"+
												"AND `dateRdv`='"+this.datePlanning+"'"+
												"ORDER BY `heureRdv`";
//			System.out.println(requête);
			ResultSet rs = BDD.leRésultat(requête);

			while(rs.next())
			{
				int index = rs.getInt(1)-1;
//				System.out.println(index);
				this.jtfCréneaux[index].setText("patient :"+ rs.getString(2)+"\t, adresse : "+rs.getString(3));
//				System.out.println(rs.getString(1)+rs.getString(2)+rs.getString(3));
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
}
