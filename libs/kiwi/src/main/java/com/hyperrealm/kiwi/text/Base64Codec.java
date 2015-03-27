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

/** This class implements a codec for the Base-64 encoding scheme. For a
 * high-level interface, see
 * {@link com.hyperrealm.kiwi.io.Base64InputStream Base64InputStream}
 * and {@link com.hyperrealm.kiwi.io.Base64OutputStream Base64OutputStream}.
 *
 * @author Mark Lindner
 * @since Kiwi 2.1.1
 */

public class Base64Codec
{
  private static final String base64
    = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  private static final byte pad = (byte)'=';

  /** The number of 4-byte tuples on each line of base-64 output. */
  public static final int TUPLES_PER_LINE = 18;

  private Base64Codec() {}

  /** Determine if a byte is a valid Base-64 encoding character; one of the
   * characters: '+', '/', '=', '0' - '9', 'A' - 'Z', 'a' - 'z'.
   *
   * @param c The byte to test.
   *
   * @return <code>true</code> if <code>c</code> is a Base-64 character,
   * <code>false</code>
   * otherwise.
   */

  public static final boolean isBase64Character(byte c)
  {
    return((base64.indexOf((char)c) != -1) || (c == pad));
  }

  /** Encode up to 3 bytes of binary data as 4 bytes of printable ASCII text.
   *
   * @param input The input array.
   * @param inpos The starting offset in the input array.
   * @param output The output array.
   * @param outpos The starting offset in the output array.
   * @param len The number of bytes (1, 2, or 3) of input to encode.
   */

  public static final void encode(byte output[], int outpos, byte input[],
                                  int inpos, int len)
  {
    byte out[] = new byte[4];

    // 3 bytes --> 4 bytes
    // the highest order byte becomes the first radix-64 'digit'

    int x = 0;

    for(int i = 0; i < 3; i++)
    {
      if(i > 0)
        x <<= 8;
      
      if(i < len)
        x |= (input[inpos++] & 0xFF);
    }
    
    for(int i = 0, n = 18; i < len + 1; i++, n -= 6)
      output[outpos++] = (byte)(base64.charAt((x >> n) & 0x3F));
    
    if(len < 3)
      output[outpos++] = pad;
    
    if(len < 2)
      output[outpos++] = pad;
  }

  /** Decode 4 bytes of printable ASCII text into up to 3 bytes of binary data.
   *
   * @param input The input array.
   * @param inpos The starting offset in the input array.
   * @param output The output array.
   * @param outpos The starting offset in the output array.
   * @return The number of bytes (1, 2, or 3) that were decoded.
   */

  public static final int decode(byte output[], int outpos, byte input[],
                                 int inpos)
  {
    int x = 0;
    int len = 3;
    
    for(int i = 0; i < 4; i++)
    {
      if(i > 0)
        x <<= 6;
      
      byte c = input[inpos++];
      if(c != pad)
        x |= (byte)(base64.indexOf(c));
      else
        len--;
    }
    
    for(int i = 0, n = 16; i < len; i++, n -= 8)
      output[outpos++] = (byte)((x >> n) & 0xFF);
    
    return(len);
  }
  
}

/* end of source file */
