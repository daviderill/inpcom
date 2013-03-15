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

package com.tecnicsassociats.gvsig.inpcom;

import com.iver.andami.PluginServices;
import com.tecnicsassociats.gvsig.inpcom.util.LogFormatter;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.JOptionPane;


public class Model {

    protected static Logger logger;	
    protected static String configFile;
    protected static Properties iniProperties = new Properties();
    protected static String appPath;
    protected static Connection connectionSqlite;
    private File fSqlite;
    protected String folderConfig;
    protected File fileTemplate;
    protected ArrayList<Map<String, String>> lMapDades;
	protected Map<String, Integer> mHeader;
    protected int polygons_target_id;
    protected int default_size;
    protected RandomAccessFile rat;
    protected RandomAccessFile raf;
    public boolean bPolygons;   // True if we have to process polygons target (810)
    public String sExport;   // "EPANET_" o "SWMM_"
    public File fileHelp;
    protected String execType = Constants.EXEC_GVSIG;   // Constants.EXEC_CONSOLE, Constants.EXEC_GVSIG
    protected String schema;


    public static Properties getPropertiesFile() {
        return iniProperties;
    }


    protected static void savePropertiesFile() {

        File iniFile = new File(configFile);
        try {
            iniProperties.store(new FileOutputStream(iniFile), "");
        } catch (FileNotFoundException e) {
            Utils.showError("inp_error_notfound", iniFile.getPath(), "inp_descr");
        } catch (IOException e) {
            Utils.showError("inp_error_io", iniFile.getPath(), "inp_descr");
        }

    }

    
    // Get Properties Files
    protected boolean enabledPropertiesFile() {

        if (execType.equals(Constants.EXEC_CONSOLE)) {
            try {
                appPath = new File(".").getCanonicalPath() + File.separator;
            } catch (IOException e1) {
                Utils.showError("inp_error_io", configFile, "inp_descr");
                return false;
            }
        } else {
            appPath = PluginServices.getPluginServices(this).getPluginDirectory().getPath() + File.separator;
        }

        configFile = appPath + Constants.CONFIG_FOLDER + File.separator + Constants.CONFIG_FILE;
        File fileIni = new File(configFile);
        try {
            iniProperties.load(new FileInputStream(fileIni));
        } catch (FileNotFoundException e) {
            Utils.showError("inp_error_notfound", configFile, "inp_descr");
            return false;
        } catch (IOException e) {
            Utils.showError("inp_error_io", configFile, "inp_descr");
            return false;
        }
        return !iniProperties.isEmpty();

    }


    // Sets initial configuration files
    protected boolean configIni() {

        // Get schema name
        String schemaProp = sExport + "SCHEMA_" + "ACTUAL";       
    	this.schema = iniProperties.getProperty(schemaProp);     	

        // Get INP folder
        folderConfig = iniProperties.getProperty("FOLDER_CONFIG");
        folderConfig = appPath + folderConfig;

        // Get INP template file
        String sFile = iniProperties.getProperty(sExport + "TEMPLATE");
        sFile = folderConfig + File.separator + sFile;
        fileTemplate = new File(sFile);
        if (!fileTemplate.exists()) {
            Utils.showError("inp_error_notfound", sFile, "inp_descr");
            return false;
        }

        // Get PDF help file
        if (fileHelp == null) {
            sFile = iniProperties.getProperty("FILE_HELP");
            sFile = folderConfig + File.separator + sFile;
            fileHelp = new File(sFile);
        }

        return true;

    }


    // Connect to sqlite Database
    protected boolean connectDB(String fileName) {

        try {
            Class.forName("org.sqlite.JDBC");
            String sFile = folderConfig + File.separator + fileName;
            fSqlite = new File(sFile);
            if (fSqlite.exists()) {
                // sqliteURL = this.getClass().getClassLoader().getResource("inp.sqlite");
                connectionSqlite = DriverManager.getConnection("jdbc:sqlite:" + sFile);
                return true;
            } else {
                Utils.showError("inp_error_notfound", sFile, "inp_descr");
                return false;
            }
        } catch (SQLException e) {
            Utils.showError("inp_error_connection", e.getMessage(), "inp_descr");
            return false;
        } catch (ClassNotFoundException e) {
            Utils.showError("inp_error_connection", "ClassNotFoundException", "inp_descr");
            return false;
        }

    }
    
    
    protected static Logger getLogger(String folderRoot) {
    	
        if (logger == null) {
            try {
                String folder = folderRoot + "log/";
                (new File(folder)).mkdirs();
                //String logFile = folder + "log_" + getCurrentTimeStamp() + ".log";
                String logFile = folder + "log_console.log";
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
    
    
}