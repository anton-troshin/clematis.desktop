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

package com.hyperrealm.kiwi.ui;

import java.awt.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.metal.*;

import com.hyperrealm.kiwi.event.*;
import com.hyperrealm.kiwi.io.*;
import com.hyperrealm.kiwi.util.*;

/** This class functions as a clearinghouse for global appearance and look &
 * feel changes for an application. <code>UIChangeManager</code> manages
 * two lists of objects.
 * <p>
 * The first is a list of JFC components that need to be redrawn when the
 * application's Look & Feel changes. Generally only top-level components,
 * like descendants of <code>JDialog</code> and <code>JFrame</code>, need to
 * be registered, as the <code>UIChangeManager</code> updates the Look & Feel
 * on the entire component hierarchy of each registered component.
 * The Kiwi <code>KFrame</code> and <code>KDialog</code>
 * superclasses automatically register themselves with the change manager, so
 * any class that extends either of these will inherit this behavior.
 * <p>
 * The second is a list of classes that wish to be notified of other, more
 * fine-grained appearance changes that take place in the application: for
 * example, a change in the default background texture. Potentially hundreds
 * of components could be affected by a change in one of these appearance
 * properties, so it would be wasteful to register all of them for
 * notification by <code>UIChangeManager</code>. The preferred alternative is
 * for each component to consult <code>UIChangeManager</code> for the current
 * settings of the appropriate properties when it is constructing itself, so
 * that its appearance is in accordance with the current settings. Obviously,
 * only subsequently-created components will inherit appearance property
 * changes. For this reason, interface components that employ the singleton
 * pattern will need to register themselves as
 * <code>PropertyChangeListener</code>s of <code>UIChangeManager</code> and
 * redraw themselves when a property changes. <code>DialogSet</code> is an
 * example of a class that implements this behavior.
 *
 * @author Mark Lindner
 */

public final class UIChangeManager implements PropertyChangeSource
{
  private static UIChangeManager instance = new UIChangeManager();
  private static ArrayList<JComponent> components;
  private static Properties props;
  private static PropertyChangeSupport support;
  /** Button opacity property. */
  public static final String BUTTON_OPACITY_PROPERTY = "button.opacity";
  /** Default texture property. */
  public static final String TEXTURE_PROPERTY = "texture";
  public static final String FRAME_ICON_PROPERTY = "frame.icon";
  private static final String DEFAULT_TEXTURE = "clouds.jpg";
  private static final String METAL_PLAF
    = "javax.swing.plaf.metal.MetalLookAndFeel";
  
  /* constructor -- singleton pattern  */

  private UIChangeManager()
  {
    components = new ArrayList<JComponent>();
    props = new Config();
    support = new PropertyChangeSupport(this);

    props.put(BUTTON_OPACITY_PROPERTY, Boolean.FALSE);
    Image texture = KiwiUtils.getResourceManager().getTexture(DEFAULT_TEXTURE);
    props.put(TEXTURE_PROPERTY, texture);

    // MetalLookAndFeel.setCurrentTheme(new KiwiTheme());
  }

  /** Get a reference to the <code>UIChangeManager</code> singleton. */

  public static UIChangeManager getInstance()
  {
    return(instance);
  }

  /** Add a <code>PropertyChangeListener</code> to this object's list of
   * listeners. Listeners are notified whenever a property of this object
   * is changed.
   *
   * @param listener The listener to add.
   * @see #removePropertyChangeListener
   */
  
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  /** Remove a <code>PropertyChangeListener</code> from this object's list of
   * listeners.
   *
   * @param listener The listener to remove.
   * @see #addPropertyChangeListener
   */
  
  
  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
  }
  
  /** Register a component with the manager. If the given component is already
   * registered, the method call has no effect. The registered component will
   * have its UI updated whenever the Look & Feel of the application changes.
   *
   * @param c The component to register.
   * @see #unregisterComponent
   */

  public void registerComponent(JComponent c)
  {
    synchronized(components)
    {
      if(!components.contains(c))
        components.add(c);
    }
  }

  /** Unregister a component from the manager.
   *
   * @param c The component to unregister.
   * @see #registerComponent
   */

  public void unregisterComponent(JComponent c)
  {
    synchronized(components)
    {
      components.remove(c);
    }
  }

  /** Change the Look & Feel globally. Each registered component (and every
   * child component thereof) will be updated to use the new Look & Feel.
   *
   * @param lf The new Look & Feel.
   * @exception javax.swing.UnsupportedLookAndFeelException If the specified Look & Feel
   * is not available.
   */

  public void changeLookAndFeel(LookAndFeel lf)
    throws UnsupportedLookAndFeelException
  {
    UIManager.setLookAndFeel(lf);
    _update();
  }

  /** Change the Look & Feel globally. Each registered component (and every
   * child component thereof) will be updated to use the new Look & Feel.
   *
   * @param className The fully-qualified class name for the new Look & Feel.
   * @exception javax.swing.UnsupportedLookAndFeelException If the specified Look & Feel
   * is not available.
   * @exception IllegalAccessException If the specified class could not be
   * accessed.
   * @exception InstantiationException If the specified class could not be
   * instantiated.
   * @exception ClassNotFoundException If the specified class could not be
   * found.
   */
  
  public void changeLookAndFeel(String className)
    throws UnsupportedLookAndFeelException, IllegalAccessException,
    InstantiationException, ClassNotFoundException
  {
    UIManager.setLookAndFeel(className);
    _update();
  }

  /** Set the <i>transparent buttons</i> flag.
   *
   * @param flag The new state of the flag; if <code>true</code>,
   * <code>KButton</code>s will be transparent by default.
   * @see #getButtonsAreTransparent
   * @see com.hyperrealm.kiwi.ui.KButton
   */

  public static void setButtonsAreTransparent(boolean flag)
  {
    props.put(BUTTON_OPACITY_PROPERTY, new Boolean(!flag));
    support.firePropertyChange(BUTTON_OPACITY_PROPERTY, null,
                               props.get(BUTTON_OPACITY_PROPERTY));
  }

  /** Get the state of the <i>transparent buttons</i> flag.
   *
   * @return The current state of the flag.
   * @see #setButtonsAreTransparent
   * @see com.hyperrealm.kiwi.ui.KButton
   */

  public static boolean getButtonsAreTransparent()
  {
    boolean flag = ((Boolean)props.get(BUTTON_OPACITY_PROPERTY))
      .booleanValue();
    return(!flag);
  }
  
  /** Get the default texture used for tiling the backgrounds of
   * <code>KPanel</code>s.
   *
   * @return The current texture, or <code>null</code> if there is no default
   * texture.
   * @see #setDefaultTexture
   * @see com.hyperrealm.kiwi.ui.KPanel
   */
  
  public static Image getDefaultTexture()
  {
    Object o = props.get(TEXTURE_PROPERTY);

    if(o == Void.class)
      return(null);
    else
      return((Image)o);
  }

  /** Set the default texture used for tiling backgrounds of
   * <code>KPanel</code>s.
   *
   * @param texture The new texture, or <code>null</code> if no textures
   * should be used.
   * @see #getDefaultTexture
   * @see com.hyperrealm.kiwi.ui.KPanel
   */
  
  public static void setDefaultTexture(Image texture)
  {
    Object o = texture;
    
    props.put(TEXTURE_PROPERTY, (o == null) ? Void.class : o);
    
    support.firePropertyChange(TEXTURE_PROPERTY, null, o);
  }

  /**
   * Set the default frame icon to be used for <code>KFrame</code>s.
   *
   * @param icon The new default frame icon, or <code>null</code> if a
   * generic frame icon should be used.
   *
   * @since Kiwi 1.4.2
   */

  public static void setDefaultFrameIcon(Image icon)
  {
    Object o = icon;

    props.put(FRAME_ICON_PROPERTY, (o == null) ? Void.class : o);

    support.firePropertyChange(FRAME_ICON_PROPERTY, null, o);
  }

  /**
   * Get the default frame icon used for <code>KFrame</code>s.
   *
   * @return The current frame icon, or <code>null</code> if there is no
   * default frame icon.
   *
   * @since Kiwi 1.4.2
   */

  public static Image getDefaultFrameIcon()
  {
    Object o = props.get(FRAME_ICON_PROPERTY);

    if(o == Void.class)
      return(null);
    else
      return((Image)o);
  }
  
  /** Set the color theme.
   *
   * @param theme The color theme.
   */

  public static void setColorTheme(ColorTheme theme)
  {
    MetalLookAndFeel.setCurrentTheme(theme);
    try
    {
      UIManager.setLookAndFeel(METAL_PLAF);
    }
    catch(Exception ex) {}
  }
  
  /* update look&feel on all components */

  private void _update()
  {
    synchronized(components)
    {
      Iterator<JComponent> iter = components.iterator();
      while(iter.hasNext())
      {
        JComponent c = iter.next();
        SwingUtilities.updateComponentTreeUI(c);
      }
    }
  }

}

/* end of source file */