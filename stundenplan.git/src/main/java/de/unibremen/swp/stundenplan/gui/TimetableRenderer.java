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
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

import de.unibremen.swp.stundenplan.config.Messages;
import de.unibremen.swp.stundenplan.data.Timeslot;

/**
 * Der TimetableRenderer bestimmt, wie die Tabelle dargestellt wird.
 * 
 * @author D. Lüdemann
 * @version 0.1
 * 
 */
public final class TimetableRenderer extends JLabel implements TableCellRenderer {

    /**
     * Logger dieser Klasse zum Protokollieren von Ereignissen und Informationen.
     */
    private static final Logger LOGGER = Logger.getLogger(TimetableRenderer.class.getName());

    /**
     * Border für die Tabellendarstellung. Wird in getTableCellRendererComponent initialisiert.
     */
    private Border unselectedBorder;

    /**
     * Border für die Tabellendarstellung. Wird in getTableCellRendererComponent initialisiert.
     */
    private Border selectedBorder;

    /**
     * Boolscher Wert, der definiert, ob eine Celle eine Border hat.
     */
    private static final boolean IS_BORDERED = true;

    /**
     * Breite des Rahmens in Pixel bei selektierten Zellen.
     */
    private static final int MATTE_BORDER_WIDTH = 5;

    /**
     * Höhe des Rahmens in Pixel bei selektierten Zellen.
     */
    private static final int MATTE_BORDER_HEIGHT = 2;

    /**
     * Die eineindeutige Kennzeichnung für Serialisierung.
     */
    private static final long serialVersionUID = -1699503640667039767L;

    /**
     * Der Konstruktor für den TimetableRenderer.
     */
    public TimetableRenderer() {
        super();
        setOpaque(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object,
     * boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        final String timetableRenderError = Messages.getString("TimetableRenderer.Error");
        if (IS_BORDERED) {
            if (isSelected) {
                if (selectedBorder == null) {
                    selectedBorder = BorderFactory.createMatteBorder(MATTE_BORDER_HEIGHT, MATTE_BORDER_WIDTH,
                            MATTE_BORDER_HEIGHT, MATTE_BORDER_WIDTH, table.getSelectionBackground());
                }
                setBorder(selectedBorder);
            } else {
                if (unselectedBorder == null) {
                    unselectedBorder = BorderFactory.createMatteBorder(MATTE_BORDER_HEIGHT, MATTE_BORDER_WIDTH,
                            MATTE_BORDER_HEIGHT, MATTE_BORDER_WIDTH, Color.GRAY);
                }
                setBorder(unselectedBorder);
            }
        }
        if (value == null) {
            LOGGER.warn("null-Objekt in schedule!");
            this.setText(timetableRenderError);
            return this;
        }
        if (!(value instanceof Timeslot)) {
            LOGGER.warn("Object of Typ " + value.getClass().getSimpleName() + " in schedule!");
            this.setText(timetableRenderError);
            return this;
        }
        final Timeslot timeslot = (Timeslot) value;
        final String text = timeslot.getTeacherAcronymList();
        final String subjectText = timeslot.getSubjectAcronym();
        String ausgabeText = "<html>"+subjectText+" <br/> "+text+"</html>";
        setText(ausgabeText);
        return this;
    }

}
