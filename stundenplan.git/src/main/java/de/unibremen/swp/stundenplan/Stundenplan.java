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
package de.unibremen.swp.stundenplan;

import java.io.IOException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;
import de.unibremen.swp.stundenplan.gui.ErrorHandler;
import de.unibremen.swp.stundenplan.gui.PlanList;
import de.unibremen.swp.stundenplan.gui.StartFrame;
import de.unibremen.swp.stundenplan.logic.SchoolclassManager;
import de.unibremen.swp.stundenplan.logic.SubjectManager;
import de.unibremen.swp.stundenplan.logic.TeacherManager;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

/**
 * Hauptklasse für den Stundenplan mit main-Methode. Erzeugt eine Konfiguration und initialisiert die Logik-Komponenten
 * und die GUI und zeigt dann das Hauptfenster an.
 * 
 * @author D. Lüdemann, K. Hölscher
 * @version 0.1
 */
public final class Stundenplan {

    /**
     * Der Logger dieser Klasse.
     */
    private static final Logger LOGGER = Logger.getLogger(Stundenplan.class.getName());
   
    public static PlanList pList;
    
    /**
     * Privater Konstruktor, der eine Instanziierung dieser Utility-Klasse verhindert.
     */
    private Stundenplan() {
    }
    
    /**
     * Startet die Anwendung. Erzeugt dazu einen neuen Stundenplaner und dann das Hauptanzeigefenster und macht dieses
     * sichtbar. Der Pfad zur Konfigurationsdatei kann als Parameter übergeben werden.
     * 
     * Falls die Konfiguration nicht erzeugt werden kann, wird eine {@link IllegalStateException} ausgelöst.
     * 
     * @param args
     *            als erstes Argument kann der Pfad zur Konfigurationsdatei angegeben werden
     */
    public static void main(final String[] args) {
        LOGGER.info("Initialize configuration");
        try {
            if (args.length > 0) {
                Config.init(args[0]);
            } else {
                Config.init(null);
            }
        } catch (IOException e) {
            LOGGER.error("Exception while initializing configuration!", e);
            throw new IllegalStateException("Could not create configuration." + e.getMessage());
        }
        try {
            TeacherManager.init();
            SubjectManager.init();
            SchoolclassManager.init();
            TimetableManager.init();
        } catch (DatasetException e) {
            LOGGER.error("Exception while initializing logic!", e);
            throw new IllegalStateException("Could not initialize logic components: " + e.getMessage());
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	final StartFrame startFrame = new StartFrame();
            	pList = new PlanList();
                ErrorHandler.setMainFrame(startFrame);
                startFrame.setLocation(50, 50);
                pList.setLocation(50, 225);
            }
        });
    }
}
