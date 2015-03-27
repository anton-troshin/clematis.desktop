package jworkspace.ui.runtime;
/* ----------------------------------------------------------------------------
   Java Workspace
   Copyright (C) 1999-2000 Anton Troshin

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
import com.hyperrealm.kiwi.ui.KPanel;

import javax.swing.*;
import java.awt.*;
import java.net.*;
/**
 * Shows machine name and ip address.
 */
public class IPAddressPanel extends KPanel
{

  public IPAddressPanel()
  {
    super();
    JTextField tf = new JTextField()
    {
       public void updateUI()
       {
         super.updateUI();
         setFont( getFont().deriveFont(Font.BOLD) );
         setBackground(Color.black);
         setForeground(Color.green);
       }
    };
    setLayout(new BorderLayout());
    add(tf, BorderLayout.CENTER);
    tf.setEditable(false);
    tf.setHorizontalAlignment(JTextField.CENTER);
    try
    {
      tf.setText(java.net.InetAddress.getLocalHost().toString());
    }
    catch(UnknownHostException ex)
    {
       tf.setText(LangResource.getString("message#239"));
    }
  }
  public Dimension getPreferredSize()
  {
    return new Dimension(200,60);
  }
}