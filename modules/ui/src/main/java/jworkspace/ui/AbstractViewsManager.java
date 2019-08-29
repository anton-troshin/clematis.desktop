package jworkspace.ui;

/* ----------------------------------------------------------------------------
   Java Workspace
   Copyright (C) 1999-2003 Anton Troshin

   This file is part of Java Workspace.

   This application is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.

   This application is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.

   You should have received a copy of the GNU Library General Public
   License along with this application; if not, write to the Free
   Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

   The author may be contacted at:

   anton.troshin@gmail.com
  ----------------------------------------------------------------------------
*/

import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JPanel;

import com.hyperrealm.kiwi.ui.KPanel;

import jworkspace.ui.api.IView;
import jworkspace.ui.cpanel.CButton;
/**
 * Views switcher and manager
 *
 * @author Anton Troshin
 */
public abstract class AbstractViewsManager extends KPanel {
    /**
     * Save path. Relative to user.home
     */
    private String path = "multidesktop";

    /**
     * Adds view to collection of views.
     *
     * @param panel              javax.swing.JComponent
     * @param displayImmediately boolean
     * @param register
     */
    public abstract void addView(IView panel, boolean displayImmediately, boolean register);

    /**
     * Deletes current view.
     */
    public abstract void deleteCurrentView();

    /**
     * Returns buttons for content manager
     */
    public CButton[] getButtons() {
        return null;
    }

    /**
     * Returns current view.
     */
    public abstract IView getCurrentView();

    /**
     * Get list of all views
     */
    public abstract IView[] getAllViews();

    /**
     * Create content manager
     */
    public abstract void create();

    /**
     * Returns menu for content manager
     */
    public abstract JMenu[] getMenu();

    /**
     * Returns true if any of views claimed it is modified
     * by user.
     */
    public abstract boolean isModified();

    /**
     * Returns option panels for content manager
     */
    public abstract JPanel[] getOptionPanels();

    /**
     * Returns relative path for saving component data.
     */
    public java.lang.String getPath() {
        return path;
    }

    /**
     * Sets path for saving component data.
     * This path is relative to user.home path.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Loads profile data.
     */
    public abstract void load() throws IOException;

    /**
     * Resets profile data.
     */
    public abstract void reset();

    /**
     * Writes down configuration on disk.
     */
    public abstract void save() throws IOException;

    /**
     * Update controls for content manager
     */
    public abstract void update();
}