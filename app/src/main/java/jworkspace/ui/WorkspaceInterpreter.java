package jworkspace.ui;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsh.EvalError;
import bsh.Interpreter;
/**
 * @author Anton Troshin
 */
public final class WorkspaceInterpreter {

    /**
     * Default logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(WorkspaceInterpreter.class);
    /**
     *
     */
    private static final String SEMICOLON = ";";
    /**
     *
     */
    private static final String CANNOT_INTERPRET = "Cannot interpret ";
    /**
     * Single instance
     */
    private static WorkspaceInterpreter ourInstance;
    /**
     * Bean Shell interpreter for scripted methods invoked from different command sources like desktop shortcuts.
     */
    private Interpreter interpreter = null;
    /**
     * Console is being executed in the separate thread
     */
    private Thread interpreterThread = null;
    /**
     * Stream for interpreter
     */
    private PipedOutputStream outPipe;

    public static synchronized WorkspaceInterpreter getInstance() {

        if (ourInstance == null) {
            ourInstance = new WorkspaceInterpreter();
            ourInstance.startInterpreter();
        }
        return ourInstance;
    }

    public synchronized void stop() throws InterruptedException {
        getInstance().interpreterThread.join();
    }
    /**
     * Re/starts interpreter thread
     */
    private synchronized void startInterpreter() {

        LOG.info("> Starting Bean Shell Interpreter");
        outPipe = new PipedOutputStream();
        /*
         * Stream for interpreter
         */
        InputStream in = System.in;
        interpreter = new Interpreter(new InputStreamReader(in, StandardCharsets.UTF_8),
            System.out, System.err, true, null);

        interpreterThread = new Thread(interpreter);
        interpreterThread.start();
        LOG.info("> Bean Shell Interpreter is successfully started");
    }

    /**
     * Executes script file
     */
    public void sourceScriptFile(String fileName) {
        try {
            if (!isAlive()) {
                startInterpreter();
            }
            interpreter.source(fileName);
        } catch (EvalError | IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Executes script
     */
    public synchronized void executeScript(String commandLine) {
        String commandLineInt = commandLine;
        try {
            if (!isAlive()) {
                startInterpreter();
            }
            if (!commandLineInt.endsWith(SEMICOLON)) {
                commandLineInt = commandLineInt + SEMICOLON;
            }
            outPipe.write(commandLineInt.getBytes(StandardCharsets.UTF_8));
            outPipe.flush();
        } catch (IOException e) {
            LOG.error(CANNOT_INTERPRET + commandLineInt, e);
        }
    }

    private synchronized boolean isAlive() {
        return interpreterThread.isAlive();
    }
}

