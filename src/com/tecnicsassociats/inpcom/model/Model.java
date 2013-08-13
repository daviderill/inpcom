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
package com.tecnicsassociats.inpcom.model;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;


public class Model {

	protected static Logger logger;	
	protected static Properties iniProperties;
    protected static Connection connectionDrivers;
    protected static String softwareVersion = "";
    protected static String softwareName = "";    
    protected static File fileTemplate;
    protected static ArrayList<LinkedHashMap<String, String>> lMapDades;
	protected static Map<String, Integer> mHeader;
    protected static RandomAccessFile rat;
    protected static RandomAccessFile raf;
    
    
    public static boolean setConnectionDrivers(String sqlitePath) {
		if (MainDao.setConnectionDrivers(sqlitePath)){
			connectionDrivers = MainDao.connectionDrivers;
			return true;
		} else{
			return false;
		}
    }    
    
    public static void setSoftwareVersion(String softwareVersion){
    	Model.softwareVersion = softwareVersion;
    	if (softwareVersion.substring(0, 4).toLowerCase().equals("swmm")){
    		softwareName = "swmm";
    	} else{
    		softwareName = "epanet";
    	}
    }
    
    public static void closeFile(){
    	try {
			Model.rat.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}