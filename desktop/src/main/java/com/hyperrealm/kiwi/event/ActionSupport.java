/* ----------------------------------------------------------------------------
   The Kiwi Toolkit - A Java Class Library
   Copyright (C) 1998-2008 Mark A. Lindner

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of the
   License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this library; if not, see <http://www.gnu.org/licenses/>.
   ----------------------------------------------------------------------------
*/

package com.hyperrealm.kiwi.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.EventListenerList;

/**
 * A support object for generating <code>ActionEvent</code>s.
 *
 * @author Mark Lindner
 * @see java.awt.event.ActionEvent
 * @see java.awt.event.ActionListener
 */
@SuppressWarnings("unused")
public class ActionSupport {

    private EventListenerList listeners;
    private Object source;

    /**
     * Construct a new <code>ActionSupport</code> object.
     *
     * @param source The owner of this object (and the source of the events that
     *               will be generated by it).
     */

    public ActionSupport(Object source) {
        this.source = source;
        listeners = new EventListenerList();
    }

    /**
     * Add an <code>ActionListener</code> to this object's list of listeners.
     *
     * @param listener The listener to add.
     */

    public void addActionListener(ActionListener listener) {
        listeners.add(ActionListener.class, listener);
    }

    /**
     * Remove an <code>ActionListener</code> from this object's list of
     * listeners.
     *
     * @param listener The listener to remove.
     */

    public void removeActionListener(ActionListener listener) {
        listeners.remove(ActionListener.class, listener);
    }

    /**
     * Fire an <code>ActionEvent</code> with an ID of
     * <code>ACTION_PERFORMED</code>, a null command string, and an empty
     * modifier mask.
     */

    public void fireActionEvent() {
        fireActionEvent(ActionEvent.ACTION_PERFORMED, null, 0);
    }

    /**
     * Fire an <code>ActionEvent</code> with an ID of
     * <code>ACTION_PERFORMED</code>, the given command string, and an empty
     * modifier mask.
     *
     * @param command The command string for the event.
     */

    public void fireActionEvent(String command) {
        fireActionEvent(ActionEvent.ACTION_PERFORMED, command, 0);
    }

    /**
     * Fire an <code>ActionEvent</code> with the given ID and command string,
     * and an empty modifier mask.
     *
     * @param id      The ID for the event.
     * @param command The command string for the event.
     */

    public void fireActionEvent(int id, String command) {
        fireActionEvent(id, command, 0);
    }

    /**
     * Fire an <code>ActionEvent</code> with the given ID, command string, and
     * modifier mask.
     *
     * @param id        The ID for the event.
     * @param command   The command string for the event.
     * @param modifiers The modifier mask for the event.
     */
    public void fireActionEvent(int id, String command, int modifiers) {

        ActionEvent evt = null;

        Object[] list = listeners.getListenerList();

        for (int i = list.length - 2; i >= 0; i -= 2) {
            if (list[i] == ActionListener.class) {
                // Lazily create the event:
                if (evt == null) {
                    evt = new ActionEvent(source, id, command, modifiers);
                }
                ((ActionListener) list[i + 1]).actionPerformed(evt);
            }
        }
    }

}
