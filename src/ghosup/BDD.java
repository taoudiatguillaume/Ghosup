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

	public static void déconnecter()
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
			JOptionPane.showMessageDialog( null,"erreur dans Déconnecter : " +e.getMessage());
		}
	}

	//fournit le résultat de requête sous forme d'un ResultSet
	public static ResultSet leRésultat(String requête)
	{
		try
		{
			BDD.connecter();
			return connexion.createStatement().executeQuery(requête);
		}
		catch (Exception e)
		{
			System.out.println("erreur accès BDD.."+e.getMessage());
		}
		return null;
	}

	//fournit le résultat de requête sous forme d'une JCombo de chaînes. La requête doit fournir des lignes constituées d'une seule valeur.
	public static JComboBox<String> laJCombo(String requête)
	{
		JComboBox<String> résultat = new JComboBox<String>();
		try
		{
			ResultSet rs = leRésultat(requête);
			while(rs.next())
			{
				résultat.addItem(rs.getString(1));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return résultat;
	}

	//exécution d'une requête de MAJ
	public static boolean maj(String requête)
	{
		try
		{
			connecter();
			connexion.createStatement().executeUpdate(requête);
			déconnecter();
			return true;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,"erreur dans  BDD.maj(String requête)..\n"+"requête : "+requête+"\n"+e.getMessage());
			return false;
		}
	}

	/* fournit pour unMatriculeAS la charge pour la période de 30 jours à partir de uneDate
	 * Utilisée pour répartir les rdv d'un traitement
	 */
	public static int laCharge(String unMatriculeAS, String uneDate)
	{
		int résultat = -2;
		try
		{
			BDD.connecter("root", "");
			String requête = "";
			requête+="SELECT COUNT(*) ";
			requête+="FROM rdv ";
			requête+="WHERE matricule = '"+unMatriculeAS+"' ";
			requête+="AND   dateRDV BETWEEN '"+uneDate+"' AND DATE_ADD('"+uneDate+"',INTERVAL 29 DAY) ";
			ResultSet rs = BDD.leRésultat(requête);
			rs.first();
			return rs.getInt(1);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return résultat;
	}

	/*fournit pour chaque AS(matricule) les charges pour la période de 30 jours à partir de uneDate
	 * Liste classée par ordre croissant des charges*/
	public static ArrayList<Paire>lesChargesPour30Jours(String uneDate)
	{
		ArrayList<Paire> résultat = new ArrayList<Paire>();
		try
		{
			BDD.connecter("root", "");
			ResultSet rs = BDD.leRésultat("SELECT matricule FROM personnel");
			while(rs.next())
			{
				String unMatriculeAS = rs.getString(1);
				int 	laChargeAS = laCharge(unMatriculeAS, uneDate);
				int i = 0;
				while(i < résultat.size() && laChargeAS > résultat.get(i).entier)
				{
					i++;
				}
				résultat.add(i, new Paire(unMatriculeAS,laChargeAS));
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return résultat;
	}

	// indique si unMatriculeAS est en congé à la date uneDate
	public static boolean estEnCongé(String unMatriculeAS, String uneDate)
	{
		try
		{
			BDD.connecter("root", "");
			ResultSet rs = BDD.leRésultat("SELECT COUNT(*) FROM absence WHERE dateAbs = '"+uneDate+"' AND matricule = '"+unMatriculeAS+"'");
			rs.first();
			if(rs.getInt(1)==1)
			{
				BDD.déconnecter();
				return true;
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		BDD.déconnecter();
		return false;
	}

	//fournit pour unMatriculeAS les heures où elle peut intervenir à la date uneDate
	public static ArrayList<String>lesHeuresPossibles(String unMatriculeAS, String uneDate)
	{
		if(estEnCongé(unMatriculeAS, uneDate))return null;
		ArrayList<String> résultat = (ArrayList<String>)lesHeures.clone();
		try
		{
			BDD.connecter("root", "");
			ResultSet rs = BDD.leRésultat("SELECT heureRdv FROM rdv WHERE daterdv = '"+uneDate+"' AND matricule = '"+unMatriculeAS+"'");
			while(rs.next())
			{
				String heureOccupée = rs.getString(1);
				for (int i = 0; i < résultat.size(); i++)
				{
					if(résultat.get(i).compareTo(heureOccupée)==0)résultat.remove(i);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return résultat;
	}

	public static int nouveauNuméroTraitement()
	{
		int résultat = -1;
		try
		{
			BDD.connecter();
			String requête = "";
			ResultSet rs = BDD.leRésultat("SELECT MAX(numTraitement) FROM traitement");
			rs.first();
			résultat = rs.getInt(1)+1;
			BDD.déconnecter();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,"erreur dans  int nouveauNuméroTraitement()..\n"+e.getMessage());
		}
		return résultat;
	}

	/*insertion d'un traitement dans la table Traitement, de ts les rdv pour ce traitement ds la table rdv*/
	public static boolean insérerDansTraitement(int numéroTraitement,String uneDateDébut, int unNombreSéances, String unNumSS)//
	{
		String requête="";
		try
		{
			BDD.connecter("root", "");

			requête = "INSERT INTO traitement VALUES("+numéroTraitement+",'"+uneDateDébut+"','"+unNombreSéances+"','"+unNumSS+"',0);";System.out.println(requête);
			if(!BDD.maj(requête))return false;

			// insertion des rdvs
			// on recherche les AS les moins chargées pour la période de 30 jours
			ArrayList<Paire>lesCharges = lesChargesPour30Jours(uneDateDébut);System.out.println(lesCharges);
			String uneDateRDV = ""+uneDateDébut;
			String uneHeureRDV = "0";
			String unMatriculeAS = "unMatricule";
			for (int i = 0; i < 30; i++)
			{
				// sélection de l'AS la moins chargée qui peut assurer unNombreSéances
				int j = 0;
				boolean trouvée = false;
				while(!trouvée)
				{
					unMatriculeAS = lesCharges.get(j).string;//System.out.println("?"+unMatriculeAS);
					ArrayList<String>lesHeuresPossibles = lesHeuresPossibles(unMatriculeAS, uneDateRDV);//System.out.println("?lesHeuresPossibles(unMatriculeAS, uneDateRDV) : "+lesHeuresPossibles(unMatriculeAS, uneDateRDV));
					if(lesHeuresPossibles!=null&&lesHeuresPossibles.size()>=unNombreSéances)
					{
						int milieu = lesHeuresPossibles.size()/2;
						// affecter les séances
						switch(unNombreSéances)
						{
						case 1 :
							uneHeureRDV = lesHeuresPossibles.get(milieu);
							requête="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+numéroTraitement+"','"+unMatriculeAS+"','');";System.out.println(requête);
							BDD.maj(requête);
							break;
						case 2 :
							uneHeureRDV = lesHeuresPossibles.get(0);
							requête="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+numéroTraitement+"','"+unMatriculeAS+"','');";System.out.println(requête);
							BDD.maj(requête);
							uneHeureRDV = lesHeuresPossibles.get(lesHeuresPossibles.size()-1);
							requête="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+numéroTraitement+"','"+unMatriculeAS+"','');";System.out.println(requête);
							BDD.maj(requête);
							break;
						case 3 :
							uneHeureRDV = lesHeuresPossibles.get(0);
							requête="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+numéroTraitement+"','"+unMatriculeAS+"','');";System.out.println(requête);
							BDD.maj(requête);
							uneHeureRDV = lesHeuresPossibles.get(milieu);
							requête="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+numéroTraitement+"','"+unMatriculeAS+"','');";System.out.println(requête);
							BDD.maj(requête);
							uneHeureRDV = lesHeuresPossibles.get(lesHeuresPossibles.size()-1);
							requête="INSERT INTO rdv VALUES(NULL,'"+uneDateRDV+"','"+uneHeureRDV+"',0,'"+numéroTraitement+"','"+unMatriculeAS+"','');";System.out.println(requête);
							BDD.maj(requête);
							break;
						}
						trouvée = true;
					}
					j++;
				}
				uneDateRDV = Utilitaires.incrémenter(uneDateRDV);
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,"erreur dans insérerDansTraitement(int numéroTraitement,String uneDateDébut, int unNombreSéances, String unNumSS)..\n"+"requête : "+requête+e.getMessage());
			return false;
		}
		return true;
	}

	public static boolean insérerDansPrescrire(int numéroTraitement,String codeTypeSoin)//
	{
		try
		{
			BDD.connecter("root", "");

			String requête = "INSERT INTO prescrire VALUES("+numéroTraitement+",'"+codeTypeSoin+"');";System.out.println(requête);
			BDD.maj(requête);
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
			String requête = "";
			requête+="SELECT rdv.heureRdv AS heure ,CONCAT(patient.nomPat,'  ', patient.prenPat) AS patient,patient.adrpat AS adresse ";
			requête+="FROM `rdv`, traitement, patient ";
			requête+="WHERE rdv.`numTraitement` = traitement.numTraitement ";
			requête+="AND traitement.`numSSPatient` = patient.numss ";
			requête+="AND `dateRdv` = '"+uneDate+"'AND `matricule`='"+ unMatricule+"' ";
			//On classe les Rdv par heure
			requête+="ORDER BY heure";
			ResultSet rs = BDD.leRésultat(requête);
			String résultat = "<HTML>";
			//On créer la requete permettant de récupérer les informations sur l'aide soignant
			String requête2 = "";
			requête2+="SELECT nompers, prenpers FROM personnel WHERE matricule ='"+unMatricule+"'";
			ResultSet rs2 = BDD.leRésultat(requête2);
			//On créer la boucle pour récupérer les informations sur l'aide soignant
			while(rs2.next())
			{
				String s2 = "Nom de l'aide soignant : "+rs2.getString("nompers")+" "+rs2.getString("prenpers")+" - Date : "+uneDate;
				résultat += s2+"<br>";
			}
			//BDD.déconnecter();
			//init d'un int pour compter les heures libres
			int precedent =1;
			//init d'un autre compteur pour la somme des heures libres
			int libre = 0;
			while(rs.next())
			{
				//Pour que toutes les heures soit affichées heures libres incluses
				while(precedent != Integer.parseInt(rs.getString("heure")))
				{
					//détecter le saut d'heure et ajouter manuellement les heures jusqua rattrapper le retard
					if(precedent != Integer.parseInt(rs.getString("heure")))
					{
					//Affichage des heures creuses
					String s = "Heure : "+precedent;
					résultat += s+"<br>";
					precedent ++;
					//On incrémente libre a chaque heure creuse
					libre ++;
					}
				}
				String s = "Heure : "+rs.getString("heure")+", patient : "+rs.getString("patient")+", adresse : "+rs.getString("adresse");
				résultat += s+"<br>";
				precedent = Integer.parseInt(rs.getString("heure"))+1;
			}
			BDD.déconnecter();
			//affichage des heures libres a la fin de l'infobulle
			résultat +="Heures libres : "+libre;
			return résultat+"</HTML>";
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Erreur dans BDD.emploiDuTemps"+e.getMessage());
			BDD.déconnecter();
			return null;
		}
	}
	public static void main(String[] args)
	{
		//BDD.connecter("root", "");
		//		System.out.println(BDD.lesValeurs("SELECT `numss` FROM `patient` ORDER BY `numss`"));
	}
}
