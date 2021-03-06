/*
 * Copyright 2014 AG Softwaretechnik, University of Bremen, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package de.unibremen.swp.stundenplan.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;

import de.unibremen.swp.stundenplan.config.Messages;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Teacher;
import de.unibremen.swp.stundenplan.data.Timeslot;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;
import de.unibremen.swp.stundenplan.logic.TimetableManager;
import de.unibremen.swp.stundenplan.persistence.Data;


/**
 * Das Hauptfenster, in dem die GUI dargestellt wird.
 * 
 * @author D. Lüdemann, K. Hölscher
 * @version 0.1
 * 
 */
public class TeacherFrame extends JFrame {
	/**
     * Ein eigener Maushorcher für die Tabelle.
     * 
     * @author D. Lüdemann
     * @version 0.1
     * 
     */
    protected class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(final MouseEvent event) {
            final int row = table.rowAtPoint(event.getPoint());
            final int col = table.columnAtPoint(event.getPoint());
            if (row >= 0 && row < table.getRowCount() && col >= 1 && col < table.getColumnCount()) {
                table.changeSelection(row, col, false, false);
            } else {
                table.clearSelection();
            }
            checkPopup(event);
            event.consume();
        }

        @Override
        public void mouseReleased(final MouseEvent event) {
            checkPopup(event);
        }

        /**
         * Prüft, ob es sich bei dem gegebenen Mausereignis um einen Rechtsklick handelt. Falls das so ist, wird ein
         * entsprechendes Popup-Menu an den durch das Mausereignis übermittelten Koordinaten geöffnet.
         * 
         * Vermeidet Redundanz in den Listenern für mouse-pressed und mouse-released-Ereignisse. Beide Listener sind
         * nötig, da Windoof den Popup-Trigger erst auf Loslassen der Maustaste meldet, Linux und MacOs aber bereits
         * beim Klicken der Maus.
         * 
         * @param event
         *            das zu prüfende Mausevent
         */
        private void checkPopup(final MouseEvent event) {
            final int row = table.rowAtPoint(event.getPoint());
            final int col = table.columnAtPoint(event.getPoint());
            if (event.isPopupTrigger()) {
                /*
                 * Verhindert den nochmaligen Aufruf unter Linux und MacOs.
                 */
                event.consume();
                if (event.getComponent() instanceof JTable && row >= 0 && col >= 1) {
                    final JPopupMenu popup = createPopup(row, col - 1);
                    popup.show(table, event.getX(), event.getY());
                }
            }
        }
    }
	/**
     * Zur Darstellung der Aktivitäten in einer Tabelle, wird die JTable benutzt.
     */
    protected JTable table;
    
    /**
     * Der Dialog, der aufpopt, um ein Fach hinzuzufügen.
     */
    protected AddSubjectDialog addSubjectDialog;

   private EditSubjectDialog editSubjectDialog;
   /**
     * Die Zeilenhöhe einer Tabellenzeile.
     */
    protected static final int ROW_HEIGHT = 40;
    

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2176074090940828921L;
	
	private final Teacher teacher;

	/**
     * Der Dialog, der aufpopt, um eine Schulklasse hinzuzufügen.
     */
    protected final AddSchoolclassDialog addSchoolclassDialog;


    public TeacherFrame(Teacher pTeacher) {
        super();
        teacher = pTeacher;
        addSubjectDialog = new AddSubjectDialog(this);
        addSchoolclassDialog = new AddSchoolclassDialog(this);
        editSubjectDialog = new EditSubjectDialog(this);
        setTitle("Stundenplan vom Lehrer: "+teacher.getName());
        table = new JTable(new TeachertableModel(pTeacher));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        table.addMouseListener(new MyMouseListener());
        table.setDefaultRenderer(Timeslot.class, new TimetableRenderer());
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPane = new JScrollPane(table);

        table.setFillsViewportHeight(true);
        table.setRowHeight(ROW_HEIGHT);
        table.setGridColor(Color.LIGHT_GRAY);
      	table.setBackground(Color.LIGHT_GRAY);
        
	        Timer t = new Timer(500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
           	Random r = new Random();
           	table.setGridColor(new Color(r.nextInt(200), r.nextInt(2), r.nextInt(2)));
          	table.setBackground(Color.LIGHT_GRAY);
            }
        });
        t.start();

        add(scrollPane);
        pack();
        setVisible(true);

        
    }

    /**
     * Erzeugt ein Popup zum Hinzufügen eines Lehrers.
     * 
     * @param row
     *            Die Zeile.
     * @param col
     *            Die Spalte.
     * @return das neue Popup-Menu
     */
    protected JPopupMenu createPopup(final int row, final int col) {
    	final JPopupMenu popmen = new JPopupMenu();
        final JMenuItem menu1 = new JMenuItem(Messages.getString("MainFrame.AddSchoolclass"));
        final JMenuItem menu2 = new JMenuItem(Messages.getString("MainFrame.RemoveSchoolclass"));
        final JMenuItem menu3 = new JMenuItem(Messages.getString("MainFrame.AddSubject"));
        final JMenuItem menu4 = new JMenuItem(Messages.getString("MainFrame.RemoveSubject"));
        final JMenuItem menu5 = new JMenuItem(Messages.getString("MainFrame.EditSubject"));
        menu1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent event) {
                addSchoolclassDialog.setTimeslot(Weekday.values()[col], row, teacher);
                addSchoolclassDialog.setVisible(true);
            }
        });
        menu2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
            	teacher.addWorkingHours(-Timeslot.LENGTH/60);
            	try {
					Timeslot timeslot = TimetableManager.getTimeslotAt(Weekday.values()[col], row, teacher);
					Boolean deleted= false;
					for(Schoolclass sc : timeslot.getSchoolclasses()){
						Timeslot ts =Data.getDayTableForWeekday(Weekday.values()[col], sc).getTimeslot(row);
						for(Teacher t : ts.getTeachers()){
							if(t.getName()==teacher.getName())
								ts.getTeachers().clear();
								timeslot.getSchoolclasses().clear();
								deleted=true;
								break;
						}
						if(deleted==true){
							break;
						}
					}
				} catch (DatasetException e) {
					e.printStackTrace();
				}
            }
        });
        menu3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
            	try {
					Timeslot timeslot = TimetableManager.getTimeslotAt(Weekday.values()[col], row, teacher);
					if(!timeslot.getSubjectAcronymList().equals("")) {
						JOptionPane.showMessageDialog(menu1, "Dort ist bereits ein Fach eingetragen!", "Fehler", JOptionPane.PLAIN_MESSAGE);
					}else {
		                addSubjectDialog.setTimeslot(Weekday.values()[col], row, teacher);
		                addSubjectDialog.setVisible(true);
					}
				} catch (DatasetException e) {
					e.printStackTrace();
				}
            }
        });
        menu4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
            	try {
            		Timeslot timeslot = TimetableManager.getTimeslotAt(Weekday.values()[col], row, teacher);
					Boolean deleted= false;
					for(Schoolclass sc : timeslot.getSchoolclasses()){
						Timeslot ts =Data.getDayTableForWeekday(Weekday.values()[col], sc).getTimeslot(row);
						for(Teacher t : ts.getTeachers()){
							if(t.getName()==teacher.getName())
								ts.getSubjects().clear();
								timeslot.getSubjects().clear();
								deleted=true;
								break;
						}
						if(deleted==true){
							break;
						}
					}				
				} catch (DatasetException e) {
					e.printStackTrace();
				}
            }
        });
        menu5.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(final ActionEvent event) {
        		try {
        			Timeslot timeslot = TimetableManager.getTimeslotAt(Weekday.values()[col], row, teacher);
        			if(timeslot.getSubjectAcronymList() == null){
        				JOptionPane.showMessageDialog(menu4, "Dort ist kein Fach zum editieren!", "Fehler", JOptionPane.PLAIN_MESSAGE);
        			}else {
        				editSubjectDialog.setTimeslot(Weekday.values()[col], row , teacher);
        				editSubjectDialog.setVisible(true);
        			}
        		}catch (DatasetException e){
        			e.printStackTrace();
        		} 
        	}          
        });
        popmen.add(menu1);
        popmen.add(menu2);
        popmen.add(menu3);
        popmen.add(menu4);
        popmen.add(menu5);
        popmen.setVisible(true);
        return popmen;
    }

}
