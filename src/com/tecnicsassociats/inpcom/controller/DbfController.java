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

import java.io.File;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.tecnicsassociats.inpcom.gui.Form;
import com.tecnicsassociats.inpcom.model.MainDao;
import com.tecnicsassociats.inpcom.model.Model;
import com.tecnicsassociats.inpcom.model.ModelDbf;
import com.tecnicsassociats.inpcom.util.Utils;


//Controller of DBF Panel only
public class DbfController {

	private Form view;
	private Properties prop;

	private boolean readyShp = false;
	private boolean readyOut = false;
	private File dirShp;
	private File dirOut;

	private String userHomeFolder;
	private ResourceBundle bundleText;

	
	public DbfController(Form view) {

		this.view = view;
		this.prop = MainDao.getPropertiesFile();
		view.setControl(this);
		view.setSoftwareDbf(MainDao.getAvailableVersions("dbf"));		

		userHomeFolder = System.getProperty("user.home");
		this.bundleText = Utils.getBundleText();

		dirShp = new File(prop.getProperty("FOLDER_SHP", userHomeFolder));
		if (dirShp.exists()) {
			view.setFolderShp(dirShp.getAbsolutePath());
			readyShp = true;
		}
		dirOut = new File(prop.getProperty("FOLDER_INP", userHomeFolder));
		if (dirOut.exists()) {
			view.setFolderOut(dirOut.getAbsolutePath());
			readyOut = true;
		}

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
	

	public void chooseFolderShp() {

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle(bundleText.getString("folder_shp"));
		File file = new File(prop.getProperty("FOLDER_SHP", userHomeFolder));
		chooser.setCurrentDirectory(file);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dirShp = chooser.getSelectedFile();
			view.setFolderShp(dirShp.getAbsolutePath());
			prop.put("FOLDER_SHP", dirShp.getAbsolutePath());
			MainDao.savePropertiesFile();
			readyShp = true;
		}

	}

	public void chooseFolderOut() {

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle(bundleText.getString("folder_out"));
		File file = new File(prop.getProperty("FOLDER_INP", userHomeFolder));
		chooser.setCurrentDirectory(file);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dirOut = chooser.getSelectedFile();
			view.setFolderOut(dirOut.getAbsolutePath());
			prop.put("FOLDER_INP", dirOut.getAbsolutePath());
			MainDao.savePropertiesFile();
			readyOut = true;
		}

	}


	public void executeDbf() {

		if (!readyShp) {
			Utils.showError("dir_shp_not_selected", "", "inp_descr");
			return;
		}
		if (!readyOut) {
			Utils.showError("dir_out_not_selected", "", "inp_descr");
			return;
		}

        // Get software version from view
		String id = view.getSoftwareDbf();
        if (id.equals("")){
            Utils.showError("Any software version selected", "", "inp_descr");
            return;
        }
        String version = MainDao.getSoftwareVersion("dbf", id);
        Model.setSoftwareVersion(version);
		
		// Get Sqlite Database			
		String sqlitePath = version + ".sqlite";
		if (!Model.setConnectionDrivers(sqlitePath)){
			return;
		}
		
		// Get INP template file
		String templatePath = MainDao.folderConfig + version + ".inp";
		File fileTemplate = new File(templatePath);
		if (!fileTemplate.exists()) {
			Utils.showError("inp_error_notfound", templatePath, "inp_descr");				
			return;
		}

		// Check if all necessary files exist
		if (!ModelDbf.checkFiles(dirShp.getAbsolutePath(), dirOut.getAbsolutePath())) {
			return;
		}

		// Process all shapes and output to INP file
		String sFileOut = view.getTxtFileOut();
		ModelDbf.processAll(dirOut.getAbsolutePath(), sFileOut);

	}
	

}