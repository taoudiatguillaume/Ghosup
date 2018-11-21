package ghosup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

//import dbwConnection.DbwConnection;
class Paire
{
	String 	string;
	int			entier;
	public Paire(String string, int entier)
	{
		super();
		this.string = string;
		this.entier = entier;
	}
	public String toString()
	{
		return this.string+" : "+this.entier;
	}
}
public class BDD
{
	private static Connection 				connexion;
	private static String 						nomUtilisateur;
	private static String 						motDePasseUtilisateur;
	private static ArrayList<String> 	lesHeures = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6", "7","8"));

	public static boolean connecter(String nom, String motDePasse)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			//Connexion locale
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/ghosup2018", nom, motDePasse);
			//Connexion serveur ghosup
			//Connection c = DriverManager.getConnection("jdbc:mysql://172.30.200.30/ghosup", nom, motDePasse);
			//connexion = c;
			BDD.nomUtilisateur = nom;
			BDD.motDePasseUtilisateur = motDePasse;
			return true;
		}
		catch (Exception e)
		{
			//			JOptionPane.showMessageDialog( null,"erreur dans Connecter(String nom, String motDePasse) : "+e.getMessage() );
			connexion = null;
			return false;
		}
	}

	public static void connecter()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/ghosup2018", BDD.nomUtilisateur, BDD.motDePasseUtilisateur);
			//Connexion serveur ghosup
			//Connection c = DriverManager.getConnection("jdbc:mysql://172.30.200.30/ghosup", BDD.nomUtilisateur, BDD.motDePasseUtilisateur);
			connexion = c;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog( null,"erreur dans Connecter() : "+e.getMessage() );
			connexion = null;
		}
	}

	public static void d�connecter()
	{
		try
		{
			if(connexion != null)
			{
				connexion.close();
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog( null,"erreur dans D�connecter : " +e.getMessage());
		}
	}

	//fournit le r�sultat de requ�te sous forme d'un ResultSet
	public static ResultSet leR�sultat(String requ�te)
	{
		try
		{
			BDD.connecter();
			return connexion.createStatement().executeQuery(requ�te);
		}
		catch (Exception e)
		{
			System.out.println("erreur acc�s BDD.."+e.getMessage());
		}
		return null;
	}

	//fournit le r�sultat de requ�te sous forme d'une JCombo de cha�nes. La requ�te doit fournir des lignes constitu�es d'une seule valeur.
	public static JComboBox<String> laJCombo(String requ�te)
	{
		JComboBox<String> r�sultat = new JComboBox<String>();
		try
		{
			ResultSet rs = leR�sultat(requ�te);
			while(rs.next())
			{
				r�sultat.addItem(rs.getString(1));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return r�sultat;
	}

	//ex�cution d'une requ�te de MAJ
	public static boolean maj(String requ�te)
	{
		try
		{
			connecter();
			connexion.createStatement().executeUpdate(requ�te);
			d�connecter();
			return true;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,"erreur dans  BDD.maj(String requ�te)..\n"+"requ�te : "+requ�te+"\n"+e.getMessage());
			return false;
		}
	}

	/* fournit pour unMatriculeAS la charge pour la p�riode de 30 jours � partir de uneDate
	 * Utilis�e pour r�partir les rdv d'un traitement
	 */
	public static int laCharge(String unMatriculeAS, String uneDate)
	{
		int r�sultat = -2;
		try
		{
			BDD.connecter("root", "");
			String requ�te = "";
			requ�te+="SELECT COUNT(*) ";
			requ�te+="FROM rdv ";
			requ�te+="WHERE matricule = '"+unMatriculeAS+"' ";
			requ�te+="AND   dateRDV BETWEEN '"+uneDate+"' AND DATE_ADD('"+uneDate+"',INTERVAL 29 DAY) ";
			ResultSet rs = BDD.leR�sultat(requ�te);
			rs.first();
			return rs.getInt(1);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return r�sultat;
	}

	/*fournit pour chaque AS(matricule) les charges pour la p�riode de 30 jours � partir de uneDate
	 * Liste class�e par ordre croissant des charges*/
	public static ArrayList<Paire>lesChargesPour30Jours(String uneDate)
	{
		ArrayList<Paire> r�sultat = new ArrayList<Paire>();
		try
		{
			BDD.connecter("root", "");
			ResultSet rs = BDD.leR�sultat("SELECT matricule FROM personnel");
			while(rs.next())
			{
				String unMatriculeAS = rs.getString(1);
				int 	laChargeAS = laCharge(unMatriculeAS, uneDate);
				int i = 0;
				while(i < r�sultat.size() && laChargeAS > r�sultat.get(i).entier)
				{
					i++;
				}
				r�sultat.add(i, new Paire(unMatriculeAS,laChargeAS));
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return r�sultat;
	}

	// indique si unMatriculeAS est en cong� � la date uneDate
	public static boolean estEnCong�(String unMatriculeAS, String uneDate)
	{
		try
		{
			BDD.connecter("root", "");
			ResultSet rs = BDD.leR�sultat("SELECT COUNT(*) FROM absence WHERE dateAbs = '"+uneDate+"' AND matricule = '"+unMatriculeAS+"'");
			rs.first();
			if(rs.getInt(1)==1)
			{
				BDD.d�connecter();
				return true;
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		BDD.d�connecter();
		return false;
	}

	//fournit pour unMatriculeAS les heures o� elle peut intervenir � la date uneDate
	public static ArrayList<String>lesHeuresPossibles(String unMatriculeAS, String uneDate)
	{
		if(estEnCong�(unMatriculeAS, uneDate))return null;
		ArrayList<String> r�sultat = (ArrayList<String>)lesHeures.clone();
		try
		{
			BDD.connecter("root", "");
			ResultSet rs = BDD.leR�sultat("SELECT heureRdv FROM rdv WHERE daterdv = '"+uneDate+"' AND matricule = '"+unMatriculeAS+"'");
			while(rs.next())
			{
				String heureOccup�e = rs.getString(1);
				for (int i = 0; i < r�sultat.size(); i++)
				{
					if(r�sultat.get(i).compareTo(heureOccup�e)==0)r�sultat.remove(i);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return r�sultat;
	}

	public static int nouveauNum�roTraitement()
	{
		int r�sultat = -1;
		try
		{
			BDD.connecter();
			String requ�te = "";
			ResultSet rs = BDD.leR�sultat("SELECT MAX(numTraitement) FROM traitement");
			rs.first();
			r�sultat = rs.getInt(1)+1;
			BDD.d�connecter();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,"erreur dans  int nouveauNum�roTraitement()..\n"+e.getMessage());
		}
		return r�sultat;
	}

	/*insertion d'un traitement dans la table Traitement, de ts les rdv pour ce traitement ds la table rdv*/
	public static boolean ins�rerDansTraitement(int num�roTraitement,String uneDateD�but, int unNombreS�ances, String unNumSS)//
	{
		String requ�te="";
		try
		{
			BDD.connecter("root", "");

			requ�te = "INSERT INTO traitement VALUES("+num�roTraitement+",'"+uneDateD�but+"','"+unNombreS�ances+"','"+unNumSS+"',0);";System.out.println(requ�te);
			if(!BDD.maj(requ�te))return false;

			// insertion des rdvs
			// on recherche les AS les moins charg�es pour la p�riode de 30 jours
			ArrayList<Paire>lesCharges = lesChargesPour30Jours(uneDateD�but);System.out.println(lesCharges);
			String uneDateRDV = ""+uneDateD�but;
			String uneHeureRDV = "0";
			String unMatriculeAS = "unMatricule";
			for (int i = 0; i < 30; i++)
			{
				// s�lection de l'AS la moins charg�e qui peut assurer unNombreS�ances
				int j = 0;
				boolean trouv�e = false;
				while(!trouv�e)
				{
					unMatriculeAS = lesCharges.get(j).string;//System.out.println("?"+unMatriculeAS);
					ArrayList<String>lesHeuresPossibles = lesHeuresPossibles(unMatriculeAS, uneDateRDV);//System.out.println("?lesHeuresPossibles(unMatriculeAS, uneDateRDV) : "+lesHeuresPossibles(unMatriculeAS, uneDateRDV));
					if(lesHeuresPossibles!=null&&lesHeuresPossibles.size()>=unNombreS�ances)
					{
						int milieu = lesHeuresPossibles.size()/2;
						// affecter les s�ances
						switch(unNombreS�ances)
						{
						case 1 :
							uneHeureRDV = lesHeuresPossibles.get(milieu);
							requ�te="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+num�roTraitement+"','"+unMatriculeAS+"','');";System.out.println(requ�te);
							BDD.maj(requ�te);
							break;
						case 2 :
							uneHeureRDV = lesHeuresPossibles.get(0);
							requ�te="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+num�roTraitement+"','"+unMatriculeAS+"','');";System.out.println(requ�te);
							BDD.maj(requ�te);
							uneHeureRDV = lesHeuresPossibles.get(lesHeuresPossibles.size()-1);
							requ�te="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+num�roTraitement+"','"+unMatriculeAS+"','');";System.out.println(requ�te);
							BDD.maj(requ�te);
							break;
						case 3 :
							uneHeureRDV = lesHeuresPossibles.get(0);
							requ�te="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+num�roTraitement+"','"+unMatriculeAS+"','');";System.out.println(requ�te);
							BDD.maj(requ�te);
							uneHeureRDV = lesHeuresPossibles.get(milieu);
							requ�te="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+num�roTraitement+"','"+unMatriculeAS+"','');";System.out.println(requ�te);
							BDD.maj(requ�te);
							uneHeureRDV = lesHeuresPossibles.get(lesHeuresPossibles.size()-1);
							requ�te="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+num�roTraitement+"','"+unMatriculeAS+"','');";System.out.println(requ�te);
							BDD.maj(requ�te);
							break;
						}
						trouv�e = true;
					}
					j++;
				}
				uneDateRDV = Utilitaires.incr�menter(uneDateRDV);
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,"erreur dans ins�rerDansTraitement(int num�roTraitement,String uneDateD�but, int unNombreS�ances, String unNumSS)..\n"+"requ�te : "+requ�te+e.getMessage());
			return false;
		}
		return true;
	}

	public static boolean ins�rerDansPrescrire(int num�roTraitement,String codeTypeSoin)//
	{
		try
		{
			BDD.connecter("root", "");

			String requ�te = "INSERT INTO prescrire VALUES("+num�roTraitement+",'"+codeTypeSoin+"');";System.out.println(requ�te);
			BDD.maj(requ�te);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public static String emploiDuTemps(String uneDate, String unMatricule)
	{
		try
		{
			//On modifie la requete afin d'avoir les informations manquantes sur l'aide soignant
			String requ�te = "";
			requ�te+="SELECT rdv.heureRdv AS heure ,CONCAT(patient.nomPat,'  ', patient.prenPat) AS patient,patient.adrpat AS adresse ";
			requ�te+="FROM `rdv`, traitement, patient ";
			requ�te+="WHERE rdv.`numTraitement` = traitement.numTraitement ";
			requ�te+="AND traitement.`numSSPatient` = patient.numss ";
			requ�te+="AND `dateRdv` = '"+uneDate+"'AND `matricule`='"+ unMatricule+"' ";
			//On classe les Rdv par heure
			requ�te+="ORDER BY heure";
			ResultSet rs = BDD.leR�sultat(requ�te);
			String r�sultat = "<HTML>";
			//On cr�er la requete permettant de r�cup�rer les informations sur l'aide soignant
			String requ�te2 = "";
			requ�te2+="SELECT nompers, prenpers FROM personnel WHERE matricule ='"+unMatricule+"'";
			ResultSet rs2 = BDD.leR�sultat(requ�te2);
			//On cr�er la boucle pour r�cup�rer les informations sur l'aide soignant
			while(rs2.next())
			{
				String s2 = "Nom de l'aide soignant : "+rs2.getString("nompers")+" "+rs2.getString("prenpers")+" - Date : "+uneDate;
				r�sultat += s2+"<br>";
			}
			//BDD.d�connecter();
			//init d'un int pour compter les heures libres
			int precedent =1;
			//init d'un autre compteur pour la somme des heures libres
			int libre = 0;
			while(rs.next())
			{
				//Pour que toutes les heures soit affich�es heures libres incluses
				while(precedent != Integer.parseInt(rs.getString("heure")))
				{
					//d�tecter le saut d'heure et ajouter manuellement les heures jusqua rattrapper le retard
					if(precedent != Integer.parseInt(rs.getString("heure")))
					{
					//Affichage des heures creuses
					String s = "Heure : "+precedent;
					r�sultat += s+"<br>";
					precedent ++;
					//On incr�mente libre a chaque heure creuse
					libre ++;
					}
				}
				String s = "Heure : "+rs.getString("heure")+", patient : "+rs.getString("patient")+", adresse : "+rs.getString("adresse");
				r�sultat += s+"<br>";
				precedent = Integer.parseInt(rs.getString("heure"))+1;
			}
			BDD.d�connecter();
			//affichage des heures libres a la fin de l'infobulle
			r�sultat +="Heures libres : "+libre;
			return r�sultat+"</HTML>";
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Erreur dans BDD.emploiDuTemps"+e.getMessage());
			BDD.d�connecter();
			return null;
		}
	}
	public static void main(String[] args)
	{
		//BDD.connecter("root", "");
		//		System.out.println(BDD.lesValeurs("SELECT `numss` FROM `patient` ORDER BY `numss`"));
	}
}
