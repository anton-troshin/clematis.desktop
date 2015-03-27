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
import java.util.Date;
import javax.swing.*;
import javax.swing.text.*;

import com.hyperrealm.kiwi.util.*;

/** A GUI console panel. This class implements the 
 * <code>LoggingEndpoint</code> interface and as such can be used as the
 * destination of log messages sent using that interface.
 *
 * @author Mark Lindner
 * @since Kiwi 2.0
 * @see com.hyperrealm.kiwi.ui.ConsoleFrame
 */

public class ConsolePanel extends KPanel implements LoggingEndpoint
{
  public static final int DEFAULT_BUFFER_SIZE = 4096; // 4K
  
  private Color colors[] = { Color.green, Color.yellow, Color.orange,
                             Color.red };
  private JTextPane t_buffer;  
  private SimpleAttributeSet attrs[];
  private DefaultStyledDocument doc;
  private int bufSize = DEFAULT_BUFFER_SIZE; 
  private Segment segment;
  private boolean timestamps = false;
  private LocaleManager lm = LocaleManager.getDefault();

  /** Construct a new <code>ConsolePanel</code>.
   */
  
  public ConsolePanel()
  {
    setLayout(new GridLayout(1, 0));

    t_buffer = new JTextPane();
    t_buffer.setEditable(false);
    t_buffer.setBackground(Color.black);
    
    KScrollPane sp = new KScrollPane(t_buffer);

    add(sp);

    StyleContext sc = new StyleContext();
    Style def = sc.getStyle(StyleContext.DEFAULT_STYLE);
    StyleConstants.setFontFamily(def, "Serif");
    StyleConstants.setFontSize(def, 14);
    doc = new DefaultStyledDocument(sc);
    t_buffer.setDocument(doc);
    
    attrs = new SimpleAttributeSet[colors.length];
    for(int i = 0; i < colors.length; i++)
    {
      attrs[i] = new SimpleAttributeSet();
      StyleConstants.setBold(attrs[i], true);
      StyleConstants.setBackground(attrs[i], Color.black);
      StyleConstants.setForeground(attrs[i], colors[i]);
    }

    segment = new Segment();
    segment.setPartialReturn(true);
  }

  /** Set the background color for the component.
   *
   * @param color The new background color.
   */
  
  public void setBackground(Color color)
  {
    if(t_buffer != null)
      t_buffer.setBackground(color);
    
    super.setBackground(color);
  }

  /** Get all of the text currently in the console's buffer.
   */

  public String getText()
  {
    String s = null;

    try
    {
      s = doc.getText(0, doc.getLength());
    }
    catch(BadLocationException ex) { }
    
    return(s);
  }

  /** Clear the console panel. All messages displayed in the panel are removed.
   */
  
  public void clear()
  {
    try
    {
      doc.remove(0, doc.getLength());
    }
    catch(BadLocationException ex) { }
  }

  /** Set the console's maximum buffer size.
   *
   * @param bufSize The buffer size, in characters.
   */

  public void setBufferSize(int bufSize)
  {
    this.bufSize = bufSize;
  }

  /** Get the console's buffer size.
   *
   * @return The buffer size, in characters.
   */

  public int getBufferSize()
  {
    return(bufSize);
  }

  /** Enable or disable message timestamps.
   *
   * @param flag A flag indicating whether timestamps should be enabled.
   *
   * @since Kiwi 2.1.1
   */
  
  public void setTimestamps(boolean flag)
  {
    timestamps = flag;
  }

  
  /** Log a message to the console.
   *
   * @param type The message type
   * @param message The message proper.
   *
   * @see com.hyperrealm.kiwi.util.LoggingEndpoint
   */

  public void logMessage(int type, String message)
  {
    if(type < 0 || type >= colors.length)
      type = 0;
    
    try
    {
      int len = doc.getLength();
      int slen = message.length();
      int newlinePos = -1;
      
      int over = (slen + len) - bufSize;
      if(over > 0)
      {
        // we need to remove at least _over_ bytes from the beginning of the
        // buffer...up until the next newline. If we can't find a newline, then
        // remove everything.
        
        int offset = 0, left = len;
        
      SEARCH:
        while(left > 0)
        {
          doc.getText(offset, left, segment);
          
          int i = 0;
          
          for(char c = segment.first(); i < segment.count;
              c = segment.next(), i++)
          {
            if(c == '\n')
            {
              newlinePos = i;
              break SEARCH;
            }
          }
          
          left -= segment.count;
          offset += segment.count;
        }
        
        if(newlinePos >= 0)
          doc.remove(0, newlinePos + 1);
        else
          doc.remove(0, len);
      }

      if(timestamps)
      {
        String timestamp = ('[' + lm.formatDateTime(new Date(), lm.SHORT)
                            + "] ");
        doc.insertString(doc.getLength(), timestamp, attrs[type]);
      }

      doc.insertString(doc.getLength(), message, attrs[type]);
      if(! message.endsWith("\n"))
        doc.insertString(doc.getLength(), "\n", null);
    }
    catch(BadLocationException ex) { }
  }

  /** Close the console.
   *
   * @see com.hyperrealm.kiwi.util.LoggingEndpoint
   */

  public void close()
  {
    // no-op
  }

}

/* end of source file */
