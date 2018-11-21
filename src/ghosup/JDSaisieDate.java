package ghosup;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


class JDSaisieDate extends JDialog implements ActionListener
{
	int dayPlus = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK)-1;
	int day1 = java.util.Calendar.getInstance().get(java.util.Calendar.DATE);
	int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
	int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
	JLabel l = new JLabel("", JLabel.CENTER);
	JLabel ld = new JLabel("", JLabel.LEFT);
	String day = "";
	String strmonth = "";
	String strday = "";
	JDialog d;
	JButton[] button = new JButton[49];
	JTextField jtfdate = new JTextField();
	private boolean answer         = false;
	private String date    = "?";
	public boolean getAnswer() { return answer; }
	public String getDate()          { return date; }


	public JDSaisieDate(JDialog frame, boolean modal,String titre, int x, int y)
	{
		d = new JDialog();
		d.setModal(true);
		String[] header = {"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
		JPanel p1 = new JPanel(new GridLayout(7, 7));
		p1.setPreferredSize(new Dimension(800, 120));
		for (int x1 = 0; x1 < button.length; x1++)
		{
			final int selection = x1;
			button[x1] = new JButton();
			button[x1].setFocusPainted(false);
			button[x1].setBackground(Color.white);
			//// met en évidence le jour sur le calendrier
			if(x1 == Integer.parseInt(formatDay(day1))+dayPlus-1+6)
			{
				button[x1].setBackground(Color.CYAN);
			}
			if (x1 > 6) // Concerne les boutons avec le jour
				//// BONUS ////
				// Permet de gérer les action de la sourie sur le bouton
				button[x1].addMouseListener(new MouseListener()
				{
					public void mouseClicked(MouseEvent e) {}
					public void mouseEntered(MouseEvent e) {
						// remet la couleur de fond du jour actuel en blanc
						button[Integer.parseInt(formatDay(day1))+dayPlus-1+6].setBackground(Color.white);
						// Si le bouton est un jour alors on affiche le jour qui est survolé par la souris dans la zone de texte
						if(!button[selection].getText().isEmpty()){
							jtfdate.setText(year+"-"+formatMonth(month)+"-"+formatDay(Integer.parseInt(button[selection].getText())));
						}
						// Modification de la couleur de fond lors du passage de la sourie
						button[selection].setBackground(Color.CYAN);
					}
					public void mouseExited(MouseEvent e) {
						// Modification de la couleur lors de la sortie de la sourie de la case
						button[selection].setBackground(Color.white);
					}
					public void mousePressed(MouseEvent e) {}
					public void mouseReleased(MouseEvent e) {}
				}); //// FIN BONUS ////
			//Permet de gerer le clique sur un bouton jour
			button[x1].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if(!button[selection].getText().isEmpty())
					{
						day = button[selection].getActionCommand();
						// on teste la validité de la date saisie
						String s = year+"-"+formatMonth(month)+"-"+formatDay(Integer.parseInt(day));
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						sdf.setLenient(false);
					if(sdf.parse(s, new ParsePosition(0)) == null)
					{
					//JOptionPane.showMessageDialog(this, "La date saisie n'est pas correcte");
							return;
					}
					else
					{
							date = s;
							answer = true;
							d.setVisible(false);
							d.dispose();
							d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
							return;
					}
				}
				}
			});
			if (x1 < 7)
			{
				// Correspond aux boutons des jours de la semaines
				// ces bouton n'ont aucune action en cas de clique
				button[x1].setText(header[x1]);
				// Modification de la couleur du texte
				button[x1].setForeground(Color.BLACK);
				// Modification de la couleur du fond
				button[x1].setBackground(Color.CYAN);
			}
			// Mise en place du bouton sur le calendrier
			p1.add(button[x1]);
		}
		JPanel p2 = new JPanel(new GridLayout(1, 3));
		JButton previous = new JButton("<< Précédent");
		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				month--;
				displayDate();
			}
		});
		p2.add(previous);
		p2.add(l);
		JButton next = new JButton("Suivant >>");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				month++;
				displayDate();
			}
		});
		p2.add(next);
		JPanel p3 = new JPanel(new GridLayout(1, 2));
		jtfdate.setText(year+"-"+formatMonth(month)+"-"+formatDay(day1));
		JButton valide = new JButton("valider");
		valide.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae)
		{
				String sd = jtfdate.getText();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setLenient(false);
				if(sdf.parse(sd, new ParsePosition(0)) == null)
				{
					//JOptionPane.showMessageDialog(this, "La date saisie n'est pas correcte");
					return;
				}
				else
				{
					date = sd;
					answer = true;
					d.setVisible(false);
					d.dispose();
					d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					return;
				}
			}
		});
		ld.setText("Choisir une date au format YYYY-MMM-DD : ");
		p3.add(ld);
		p3.add(jtfdate);
		p3.add(valide);
		d.add(p1, BorderLayout.CENTER);
		d.add(p2, BorderLayout.SOUTH);
		d.add(p3, BorderLayout.NORTH);
		d.pack();
		d.setLocationRelativeTo(getParent());
		displayDate();
		d.setVisible(true);
		d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// Permet de faire disparaitre this
		//this.setTitle("");
		this.setLocation(-1000,-1000);
	}
	// formatage du mois et de l'année
	public String formatMonth(int m){
		if(m < 10){
			strmonth = "0"+m;
		}else{
			strmonth = ""+m;
		}
		return strmonth;
	}
	public String formatDay(int d){
		if(d < 10){
			strday = "0"+d;
		}else{
			strday = ""+d;
		}
		return strday;
	}
	public void displayDate() {
		for (int x = 7; x < button.length; x++)
			button[x].setText("");
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM yyyy");
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(year, month, 1);
		int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
		int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
		for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
			button[x].setText("" + day);
		l.setText(sdf.format(cal.getTime()));
		d.setTitle("Choix de la date");
	}
	public String setPickedDate() {
		if (day.equals(""))
			return day;
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(year, month, Integer.parseInt(day));
		return sdf.format(cal.getTime());
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
