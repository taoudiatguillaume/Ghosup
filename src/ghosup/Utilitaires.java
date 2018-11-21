package ghosup;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Utilitaires
{
	public static String doublerLesApostrophes(String s)
	{
		String résultat = "";
		for (int i = 0; i < s.length(); i++)
		{
			if(s.charAt(i)=='\'')résultat+=s.charAt(i);
			résultat+=s.charAt(i);
		}
		return résultat;
	}
	public static String incrémenter(String uneDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try
		{
			c.setTime(sdf.parse(uneDate));
			c.add(Calendar.DATE, 1);  
			return sdf.format(c.getTime()); 
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}
	public static void main(String[] args)
	{
//		System.out.println(lesColonnes("Tailles"));
//		System.out.println(lesColonnes("Clients"));
//		System.out.println(lesAttributs("Clients"));
//		System.out.println(déclarerLesAttributs("Clients"));
//		System.out.println(placerLesAttributs("Clients"));

//		System.out.println(System.getProperty("user.dir") );
//		System.out.println(normaliser("absencE"));
//		String d = "2013-03-14";
//		incrémenter(d);
//		System.out.println(d);
//		System.out.println(leParamètre("couleurFond1"));
//		System.out.println(leParamètre("ww"));
//		System.out.println(remplacer("	this.setBackground(!!couleurFond1!!);","!!couleurFond1!!",Utilitaires.leParamètre("couleurFond1")));
		
	}
}
