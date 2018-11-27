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

package com.hyperrealm.kiwi.text;

/** This class provides methods for converting primitive numeric and boolean
 * values to and from string representations.
 *
 * @author Mark Lindner
 */

public class ValueFormatter
{
  private ValueFormatter() {}

  /** Format a byte value as a string.
   *
   * @param v The value to format.
   * @return A string representation of the value.
   */
  
  public String format(byte v)
  {
    return(String.valueOf(v));
  }

  /** Parse a byte representation.
   *
   * @param s The string to parse.
   * @return The parsed value.
   * @exception com.hyperrealm.kiwi.text.ParsingException If parsing failed.
   */
  
  public byte parseByte(String s) throws ParsingException
  {
    byte v = 0;
    
    try
    {
      v = (new Byte(s)).byteValue();
    }
    catch(NumberFormatException ex)
    {
      throw(new ParsingException(ex.getMessage()));
    }

    return(v);
  }

  /** Format an integer value as a string.
   *
   * @param v The value to format.
   * @return A string representation of the value.
   */
  
  public String format(int v)
  {
    return(String.valueOf(v));
  }

  /** Parse an integer representation.
   *
   * @param s The string to parse.
   * @return The parsed value.
   * @exception com.hyperrealm.kiwi.text.ParsingException If parsing failed.
   */
  
  public int parseInt(String s) throws ParsingException
  {
    int v = 0;
    
    try
    {
      v = Integer.parseInt(s);
    }
    catch(NumberFormatException ex)
    {
      throw(new ParsingException(ex.getMessage()));
    }
    
    return(v);
  }

  /** Format a long integer value as a string.
   *
   * @param v The value to format.
   * @return A string representation of the value.
   */
  
  public String format(long v)
  {
    return(String.valueOf(v));
  }

  /** Parse a long integer representation.
   *
   * @param s The string to parse.
   * @return The parsed value.
   * @exception com.hyperrealm.kiwi.text.ParsingException If parsing failed.
   */
  
  public long parseLong(String s) throws ParsingException
  {
    long v = 0;
    
    try
    {
      v = (new Long(s)).longValue();
    }
    catch(NumberFormatException ex)
    {
      throw(new ParsingException(ex.getMessage()));
    }

    return(v);
  }

  /** Format a float value as a string.
   *
   * @param v The value to format.
   * @return A string representation of the value.
   */
  
  public String format(float v)
  {
    return(String.valueOf(v));
  }

  /** Parse a float representation.
   *
   * @param s The string to parse.
   * @return The parsed value.
   * @exception com.hyperrealm.kiwi.text.ParsingException If parsing failed.
   */
  
  public float parseFloat(String s) throws ParsingException
  {
    float v = 0;
    
    try
    {
      v = (new Float(s)).floatValue();
    }
    catch(NumberFormatException ex)
    {
      throw(new ParsingException(ex.getMessage()));
    }

    return(v);
  }

  /** Format a double precision value as a string.
   *
   * @param v The value to format.
   * @return A string representation of the value.
   */
  
  public String format(double v)
  {
    return(String.valueOf(v));
  }

  /** Parse a double precision representation.
   *
   * @param s The string to parse.
   * @return The parsed value.
   * @exception com.hyperrealm.kiwi.text.ParsingException If parsing failed.
   */
  
  public double parseDouble(String s) throws ParsingException
  {
    float v = 0;
    
    try
    {
      v = (new Float(s)).floatValue();
    }
    catch(NumberFormatException ex)
    {
      throw(new ParsingException(ex.getMessage()));
    }

    return(v);
  }

  /** Format a boolean value as a string.
   *
   * @param v The value to format.
   * @return A string representation of the value.
   */
  
  public String format(boolean v)
  {
    return(String.valueOf(v));
  }

  /** Parse a boolean representation.
   *
   * @param s The string to parse.
   * @return The parsed value.
   */
  
  public boolean parseBoolean(String s)
  {
    return((new Boolean(s)).booleanValue());
  }

}

/* end of source file */