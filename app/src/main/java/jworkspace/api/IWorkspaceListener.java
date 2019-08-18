package jworkspace.api;

/* ----------------------------------------------------------------------------
   Java Workspace
   Copyright (C) 1999-2018 Anton Troshin

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


/**
 * Workspace listener dispatches events send to subscribers through workspace event mechanism.
 *
 * @author Anton Troshin
 */
public interface IWorkspaceListener {

    String VIEW_PARAMETER = "view";

    String DISPLAY_PARAMETER = "display";

    String REGISTER_PARAMETER = "register";
    /**
     * Returns the integer this listener is bound to
     */
    int getCode();
    /**
     * Processes event. Such event is send to every subscribed event listener in synchronous manner.
     */
    void processEvent(Integer event, Object lparam, Object rparam);
}