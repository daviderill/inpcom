/*
 * This file is part of INPcom
 * Copyright (C) 2012  Tecnics Associats
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Author:
 *   David Erill <daviderill79@gmail.com>
 */

package com.tecnicsassociats.gvsig.inpcom.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.MDIManager;
import com.tecnicsassociats.gvsig.inpcom.Constants;
import com.tecnicsassociats.gvsig.inpcom.MainWindow;
import com.tecnicsassociats.gvsig.inpcom.ModelDbf;
import com.tecnicsassociats.gvsig.inpcom.ModelPostgis;
import com.tecnicsassociats.gvsig.inpcom.WindowController2;

public class Utils {

    private static Logger logger;
    private static final String LOG_FOLDER = "log/";

    // Open main form
    public static void openForm(ModelDbf modelDbf, ModelPostgis modelPostgis) {
        MainWindow cmWindow = new MainWindow();
        new WindowController2(modelDbf, modelPostgis, cmWindow);
        MDIManager mdi = PluginServices.getMDIManager();
        mdi.addCentredWindow(cmWindow);
    }
    
    
    public static Logger getLogger(String folderRoot) {
        if (logger == null) {
            try {
                String folder = folderRoot + LOG_FOLDER;
                (new File(folder)).mkdirs();
                String logFile = folder + "log_" + getCurrentTimeStamp() + ".log";
                FileHandler fh = new FileHandler(logFile, true);
                LogFormatter lf = new LogFormatter();
                fh.setFormatter(lf);
                logger = Logger.getLogger(logFile);
                logger.addHandler(fh);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error al crear el fitxer de log", JOptionPane.ERROR_MESSAGE);
            }
        }
        return logger;

    }


    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        String date = sdfDate.format(now);
        return date;
    }


    public static String dateToString(Date date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        String parsedDate = sdfDate.format(date);
        return parsedDate;
    }


    public static void copyFile(String srFile, String dtFile) {

        try {

            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);

            // For Overwrite the file.
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    public static void showMessage(String msg, String param, String title, String execType) {
        if (execType.equals(Constants.EXEC_GVSIG)) {             
            try {
                JOptionPane.showMessageDialog(null, PluginServices.getText(Constants.CONFIG_PLUGIN, msg) + "\n" + param,
                    PluginServices.getText(Constants.CONFIG_PLUGIN, title), JOptionPane.PLAIN_MESSAGE);
                if (logger != null) {
                    logger.info(PluginServices.getText(Constants.CONFIG_PLUGIN, msg) + "\n" + param);
                }
            }  catch (NoClassDefFoundError e) {
                JOptionPane.showMessageDialog(null, msg + param, title, JOptionPane.PLAIN_MESSAGE);
                if (logger != null) {
                    logger.info(msg + "\n" + param);
                }
            }    
        }
    }


    public static void showError(String msg, String param, String title) {
        try {
            JOptionPane.showMessageDialog(null, PluginServices.getText(Constants.CONFIG_PLUGIN, msg) + "\n" + param,
                PluginServices.getText(Constants.CONFIG_PLUGIN, title), JOptionPane.WARNING_MESSAGE);
            if (logger != null) {
                logger.warning(PluginServices.getText(Constants.CONFIG_PLUGIN, msg) + "\n" + param);
            }
        } catch (NoClassDefFoundError e) {
            JOptionPane.showMessageDialog(null, msg + "\n" + param, title, JOptionPane.WARNING_MESSAGE);
            if (logger != null) {
                logger.warning(msg + "\n" + param);
            }
        }
    }

    
    public static int confirmDialog(String msg, String title) {
    	int reply;
        try {
            reply = JOptionPane.showConfirmDialog(null, PluginServices.getText(Constants.CONFIG_PLUGIN, msg),
                PluginServices.getText(Constants.CONFIG_PLUGIN, title), JOptionPane.YES_NO_OPTION);
            if (logger != null) {
                logger.warning(PluginServices.getText(Constants.CONFIG_PLUGIN, msg));
            }
        } catch (NoClassDefFoundError e) {
            reply = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION);
            if (logger != null) {
                logger.warning(msg);
            }
        }
        return reply;
    }
    

    /**
     * From geotools Adds quotes to an object for storage in postgis. The object
     * should be a string or a number. To perform an insert strings need quotes
     * around them, and numbers work fine with quotes, so this method can be
     * called on unknown objects.
     *
     * @param value The object to add quotes to.
     *
     * @return a string representation of the object with quotes.
     */
    protected String addQuotes(Object value) {
        String retString;

        if (value != null) {
            retString = "'" + doubleQuote(value) + "'";
        } else {
            retString = "null";
        }

        return retString;
    }


    private String doubleQuote(Object obj) {
        String aux = obj.toString().replaceAll("'", "''");
        StringBuffer strBuf = new StringBuffer(aux);
        ByteArrayOutputStream out = new ByteArrayOutputStream(strBuf.length());
        PrintStream printStream = new PrintStream(out);
        printStream.print(aux);
        String aux2 = "ERROR";
        //aux2 = out.toString(toEncode);
        System.out.println(aux + " " + aux2);
//		catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}

        return aux2;
    }
    
}