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
package com.tecnicsassociats.inpcom.controller;

import java.awt.Cursor;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.tecnicsassociats.inpcom.gui.Form;
import com.tecnicsassociats.inpcom.model.MainDao;
import com.tecnicsassociats.inpcom.model.ModelPostgis;
import com.tecnicsassociats.inpcom.util.Encryption;
import com.tecnicsassociats.inpcom.util.Utils;

public class DatabaseController {

	private Form view;
    private Properties prop;
	private boolean isConnected = false;
	private ResourceBundle bundleText;
	
	
	public DatabaseController(Form cmWindow) {
		
		this.view = cmWindow;	
        this.prop = MainDao.getPropertiesFile();
	    view.setControl(this);        
    	this.bundleText = Utils.getBundleText();
    	
	}
	
	
	public void action(String actionCommand) {
		
		Method method;
		try {
			if (Utils.getLogger() != null){
				Utils.getLogger().info(actionCommand);
			}
			method = this.getClass().getMethod(actionCommand);
			method.invoke(this);	
		} catch (Exception e) {
			if (Utils.getLogger() != null){			
				Utils.logError(e, actionCommand);
			} else{
				Utils.showError(e, actionCommand);
			}
		}
		
	}	
	
	
	public void schemaResultChanged(){
		String schema = view.getSchemaResult();
		Vector<String> v = MainDao.getTable("rpt_result_cat", schema);
		view.setResultCombo(v);
	}
	
	
	public void resultChanged(){
		String result = view.getResult();
		MainDao.setResultSelect(view.getSchemaResult(), "result_selection", result);
	}	
	
	
	public void createSchema(){
		
		Integer driver = view.getDriver();
		String schemaName = JOptionPane.showInputDialog(this.view, bundleText.getString("enter_schema_name"), "schema_name");
		if (schemaName == null){
			return;
		}
		schemaName = schemaName.trim().toLowerCase();
		if (schemaName.equals("")){
			Utils.showError("Please, provide a valid name", "", "INPcom");
			return;
		}
		
		// Get default SRID from properties
		String defaultSrid = prop.getProperty("SRID", "23030");
		String sridValue = JOptionPane.showInputDialog(this.view, bundleText.getString("enter_srid"), defaultSrid);
		if (sridValue == null){
			return;
		}		
		sridValue = sridValue.trim();
		if (!sridValue.equals("")){
			Integer srid;
			try{
				srid = Integer.parseInt(sridValue);
			} catch (NumberFormatException e){
				Utils.showError("SRID must be a numeric value", "", "INPcom");
				return;
			}
			if (!sridValue.equals(defaultSrid)){
				prop.setProperty("SRID", sridValue);
				MainDao.savePropertiesFile();
			}
			boolean isSridOk = MainDao.checkSrid(srid);
			if (!isSridOk){
				Utils.showError("SRID " + srid + " not found in spatial_ref_sys Postgis table\n" +
					"Please provide a valid value", "", "INPcom");
				return;
			}
			view.setCursor(new Cursor(Cursor.WAIT_CURSOR));		
			MainDao.createSchema(schemaName.trim(), sridValue.trim(), driver);
			view.setSchemas(MainDao.getSchemas());		
			view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
	}
	
	
	public void deleteSchema(){
		
		String schemaName = view.getSchema1();
        int res = JOptionPane.showConfirmDialog(this.view, Utils.getBundleString(bundleText, "delete_schema_name") + "\n" + schemaName, 
        		"INPCom", JOptionPane.YES_NO_OPTION);
        if (res == 0){
    		view.setCursor(new Cursor(Cursor.WAIT_CURSOR));	        	
        	MainDao.deleteSchema(schemaName);
        	view.setSchemas(MainDao.getSchemas());
    		view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    		Utils.showMessage("Schema deleted", "", "INPcom");
        }
        
	}	
	
	
	public void testConnection(){
		
		String host, port, db, user, password;
		
		// Get parameteres connection from view
		host = view.getHost();		
		port = view.getPort();
		db = view.getDatabase();
		user = view.getUser();
		password = view.getPassword();		
		isConnected  = ModelPostgis.setConnectionPostgis(host, port, db, user, password);
		
		if (isConnected){
			view.setSchemas(MainDao.getSchemas());
			view.setSoftwarePostgis(MainDao.getAvailableVersions("postgis"));
			prop.put("POSTGIS_HOST", host);
			prop.put("POSTGIS_PORT", port);
			prop.put("POSTGIS_DATABASE", db);
			prop.put("POSTGIS_USER", user);
			// Save encrypted password
			if (view.getRemember()){
				prop.put("POSTGIS_PASSWORD", Encryption.encrypt(password));
			} else{
				prop.put("POSTGIS_PASSWORD", "");
			}
			// Save properties file
			MainDao.savePropertiesFile();
			Utils.showMessage("Connection successful!", "", "INP.com");
			view.enableButtons(true);
			MainDao.setSchema(view.getSchema1());
		} else{
			view.enableButtons(false);
			view.setSchemas(null);			
		}
		
	}	
	
}