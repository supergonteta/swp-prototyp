package de.unibremen.swp.stundenplan.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.unibremen.swp.stundenplan.Stundenplan;

public class StartFrame extends JFrame {
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton buttonS1;
	private JPanel panelButton;
	private JPanel panel2Button;
	private JLabel oben;

	
	public StartFrame() {
		super("Start");
		setLocation(100, 300);
		this.setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(5, 5));

		// Buttons erzeugen
		button1 = new JButton("Add Lehrer");
		
		button2 = new JButton("Add Fach");
	
		button3 = new JButton("Add Klasse");
		
		buttonS1 = new JButton("Stundenplan anzeigen");
		

		// Panels erzeugen
		panelButton = new JPanel(new GridLayout(1, 3));
		panel2Button = new JPanel(new GridLayout(1, 2));

		// Auf Panel Buttons packen
		panelButton.add(button1);
		panelButton.add(button2);
		panelButton.add(button3);
		panel2Button.add(buttonS1);

		
		addLehrer(button1);
		addFach(button2);
		addKlasse(button3);
		showStundenplan(buttonS1);

		// Labels erzeugen
		oben = new JLabel("StartPanel");
		// Label zentrieren
		oben.setHorizontalAlignment(JLabel.CENTER);

		// Labels auf Frame packen
		getContentPane().add(BorderLayout.NORTH, oben);

		// Panels auf Frame packen
		getContentPane().add(BorderLayout.WEST, panelButton);
		getContentPane().add(BorderLayout.SOUTH, panel2Button);

		pack();
		setVisible(true);
 
	}

	public static void main(String[] args) {
		
		StartFrame g = new StartFrame();
		
		
		
	}

	private void showStundenplan(JButton b) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String[] args = new String[8];
				Stundenplan.main(args);
			}
		});
	}
	
	private void addLehrer(JButton b) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				AddNewTeacher a = new AddNewTeacher();
				JFrame f = new JFrame("neue Lehrer hinzufügen");
				f.add(a, BorderLayout.CENTER);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.pack();
				f.setVisible(true);
				
			}
		});
	}
	private void addKlasse(JButton b) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Klasse");
			}
		});
	}
	private void addFach(JButton b) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Fach");
			}
		});
	}
	
	

}