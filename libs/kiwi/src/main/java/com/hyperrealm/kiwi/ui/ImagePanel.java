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
import javax.swing.*;

/** A component for displaying an icon image. The component's preferred and
 * minimum sizes are equal to the size of the image.
 *
 * @author Mark Lindner
 */

public class ImagePanel extends JComponent
{
  private Icon image;
  private Dimension size;

  /** Construct a new <code>ImagePanel</code> with the specified icon image.
   *
   * @param image The icon to paint in the panel.
   */
       
  public ImagePanel(Icon image)
  {
    _setImage(image);
  }

  /** Get the icon image currently displayed by this <code>ImagePanel</code>.
   *
   * @since Kiwi 1.3.2
   *
   * @return The icon.
   */

  public Icon getImage()
  {
    return(image);
  }

  private void _setImage(Icon image)
  {
    this.image = image;
    size = new Dimension(image.getIconWidth(), image.getIconHeight());
  }

  /** Set an icon image for the <code>ImagePanel</code>
   *
   * @since Kiwi 1.3.3
   *
   * @param image The icon to paint in the panel.
   */
   
  public void setImage(Icon image)
  {
    _setImage(image);
    repaint();
  }
  
  /** Paint the component.
   */
   
  public void paintComponent(Graphics gc)
  {
    image.paintIcon(this, gc, 0, 0);
  }

  /** Get the preferred size of the component.
   *
   * @return The size of the image.
   */
   
  public Dimension getPreferredSize()
  {
    return(size);
  }

  /** Get the minimum size of the component.
   *
   * @return The size of the image.
   */
   
  public Dimension getMinimumSize()
  {
    return(size);
  }

}

/* end of source file */
