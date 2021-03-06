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

package com.hyperrealm.kiwi.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * This class represents a filesystem traverser. A depth-first traversal of a
 * directory tree rooted at a specified node is performed, with each file and
 * directory encountered being passed to a <code>FileConsumer</code>.
 *
 * @author Mark Lindner
 * @see com.hyperrealm.kiwi.io.FileConsumer
 */
@SuppressWarnings("unused")
public class FilesystemTraverser {

    private File root;

    private FilenameFilter filter;

    private FileConsumer consumer;

    /**
     * Construct a new <code>FilesystemTraverser</code>.
     *
     * @param root     The root of the directory tree to traverse.
     * @param consumer The <code>FileConsumer</code> class to pass each
     *                 encountered file and directory to.
     * @param filter   The <code>FilenameFilter</code> to filter files through
     *                 before passing them to the consumer.
     */

    public FilesystemTraverser(File root, FilenameFilter filter,
                               FileConsumer consumer) {
        this.root = root;
        this.filter = filter;
        this.consumer = consumer;
    }

    /**
     * Construct a new <code>FilesystemTraverser</code>.
     *
     * @param root     The root of the directory tree to traverse.
     * @param consumer The <code>FileConsumer</code> class to pass each
     *                 encountered file and directory to.
     */

    public FilesystemTraverser(File root, FileConsumer consumer) {
        this(root, null, consumer);
    }

    /**
     * Traverse the filesystem. If the root node does not exist or is
     * not a directory, this method returns immediately with a value of
     * <code>false</code>.  Each file encountered during the traversal
     * is passed to the <code>FileConsumer</code>'s
     * <code>accept()</code> method; if that method returns
     * <code>false</code>, the traversal is interrupted and this method
     * returns <code>false</code>; otherwise the traversal continues
     * through the end and the method returns <code>true</code>.
     *
     * @see com.hyperrealm.kiwi.io.FileConsumer#accept
     */

    public boolean traverse() {

        if (!root.exists()) {
            return (false);
        }

        return !root.isDirectory() || traverse(root);
    }

    /* one iteration of traversal */

    private boolean traverse(File dir) {

        boolean ret = true;
        String[] files = ((filter == null) ? dir.list() : dir.list(filter));

        if (files == null) {
            return false;
        }

        for (String file : files) {
            File f = new File(dir, file);

            if (f.isDirectory()) {
                if (!f.canRead() && !consumer.accessError(f)) {
                    ret = false;
                    break;
                }

                if (!consumer.accept(f)) {
                    ret = false;
                    break;
                } else if (!traverse(f)) {
                    ret = false;
                    break;
                }
            } else {
                if (!consumer.accept(f)) {
                    ret = false;
                    break;
                }
            }
        }

        return ret;
    }

}
