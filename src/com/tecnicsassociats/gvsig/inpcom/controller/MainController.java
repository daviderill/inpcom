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
package com.tecnicsassociats.gvsig.inpcom.controller;

import java.awt.Cursor;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tecnicsassociats.gvsig.inpcom.gui.Form;
import com.tecnicsassociats.gvsig.inpcom.gui.TableWindow;
import com.tecnicsassociats.gvsig.inpcom.model.MainDao;
import com.tecnicsassociats.gvsig.inpcom.model.ModelPostgis;
import com.tecnicsassociats.gvsig.inpcom.util.Encryption;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;


// Controller of Postgis Panel only
public class MainController{

	private Form view;
    private ModelPostgis modelPostgis;
    private Properties prop;

    // Postgis
    private File fileInp;
    private File fileRpt;
    private String projectName;
    private boolean readyFileInp = false;
    private boolean readyFileRpt = false;
    private boolean exportChecked;
    private boolean execChecked;
    private boolean importChecked;
    
    private String userHomeFolder;
    //private ResourceBundle bundleForm;
    private ResourceBundle bundleText;

    
    public MainController(ModelPostgis modelPostgis, Form view) {
    	
    	this.view = view;
        this.modelPostgis = modelPostgis;    	
        this.prop = ModelPostgis.getPropertiesFile();
	    view.setControl(this);        
    	
    	userHomeFolder = System.getProperty("user.home");
    	//this.bundleForm = Utils.getBundleForm();
    	this.bundleText = Utils.getBundleText();
    	
    	// Set default values
    	setDefaultValues();
    	    	
	}

    
    private void setDefaultValues(){
    	
    	fileInp = new File(prop.getProperty("FILE_INP", userHomeFolder));
		if (fileInp.exists()) {
			view.setFileInp(fileInp.getAbsolutePath());
			readyFileInp = true;
		}
		fileRpt = new File(prop.getProperty("FILE_RPT", userHomeFolder));
		if (fileRpt.exists()) {
			view.setFileRpt(fileRpt.getAbsolutePath());
			readyFileRpt = true;
		}    	
		
		projectName = prop.getProperty("PROJECT_NAME");
		view.setProjectName(projectName);
		
		// Get parameters connection 
		view.setHost(prop.getProperty("POSTGIS_HOST", "localhost"));
		view.setPort(prop.getProperty("POSTGIS_PORT", "5432"));
		view.setDatabase(prop.getProperty("POSTGIS_DATABASE", ""));
		view.setUser(prop.getProperty("POSTGIS_USER", ""));
		view.setPassword(Encryption.decrypt(prop.getProperty("POSTGIS_PASSWORD", "")));
	
    }
   

	public void action(String actionCommand) {
		Method method;
		try {
			Utils.getLogger().info(actionCommand);
			method = this.getClass().getMethod(actionCommand);
			method.invoke(this);	
			view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));			
		} catch (Exception e) {
			view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));			
			Utils.logError(e, actionCommand);
		}
	}	
	
	
	public void schemaChanged(){
		MainDao.setSchema(view.getSchema());
	}
	
	
	
	public void showOptions(){
		ResultSet rs = MainDao.getTableResultset("inp_options");
		TableWindow tableWindow = new TableWindow(rs, "inp_options");
        Utils.openDialogForm(tableWindow, 900, 250);
	}
	

	public void showCatchment(){
		ResultSet rs = MainDao.getTableResultset("catch_selection");
		TableWindow tableWindow = new TableWindow(rs, "catch_selection");
        Utils.openDialogForm(tableWindow, 500, 300);
	}	
	
	
	public void showRaingage(){
		ResultSet rs = MainDao.getTableResultset("raingage");
		TableWindow tableWindow = new TableWindow(rs, "raingage");
        Utils.openDialogForm(tableWindow, 700, 300);
	}	
		
		

    public void chooseFileInp() {

        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("INP extension file", "inp");
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle(bundleText.getString("file_inp"));
        File file = new File(prop.getProperty("FILE_INP", userHomeFolder));	
        chooser.setCurrentDirectory(file.getParentFile());
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileInp = chooser.getSelectedFile();
            String path = fileInp.getAbsolutePath();
            if (path.lastIndexOf(".") == -1) {
                path += ".inp";
                fileInp = new File(path);
            }
            view.setFileInp(fileInp.getAbsolutePath());            
            prop.put("FILE_INP", fileInp.getAbsolutePath());
            ModelPostgis.savePropertiesFile();
            readyFileInp = true;
        }

    }


    public void chooseFileRpt() {

        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("RPT extension file", "rpt");
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle(bundleText.getString("file_rpt"));
        File file = new File(prop.getProperty("FILE_RPT", userHomeFolder));	
        chooser.setCurrentDirectory(file.getParentFile());
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileRpt = chooser.getSelectedFile();
            String path = fileRpt.getAbsolutePath();
            System.out.println(path.lastIndexOf("."));
            if (path.lastIndexOf(".") == -1) {
                path += ".rpt";
                fileRpt = new File(path);
            }
            view.setFileRpt(fileRpt.getAbsolutePath());
            prop.put("FILE_RPT", fileRpt.getAbsolutePath());
            ModelPostgis.savePropertiesFile();
            readyFileRpt = true;
        }

    }


    public void executePostgis() {

        boolean continueExec = true;

        //view.resetStatus();
        
        // Get schema from view
        String schema = view.getSchema();
        if (schema.equals("")){
            Utils.showError("Any schema selected", "", "inp_descr");
            return;
        }
        MainDao.setSchema(schema);
        
        // Which checks are selected?
        exportChecked = view.isExportChecked();
        execChecked = view.isExecChecked();
        importChecked = view.isImportChecked();        
        
        if (!exportChecked && !execChecked && !importChecked){
            Utils.showError("select_option", "", "inp_descr");
        }
        
        view.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        // Export to INP
        if (exportChecked) {
            if (!readyFileInp) {
                Utils.showError("file_inp_not_selected", "", "inp_descr");
                return;
            }            
            //String status = "Export to INP\n" + bundleText.getString("processing") + "\n";            
            continueExec = this.modelPostgis.processAll(fileInp);
            //status = bundleText.getString("completed") + ": " + fileInp.getAbsolutePath();
        }

        // Run SWMM
        if (execChecked && continueExec) {
            if (!readyFileInp) {
                Utils.showError("file_inp_not_selected", "", "inp_descr");
                return;
            }            
            if (!readyFileRpt) {
                Utils.showError("file_rpt_not_selected", "", "inp_descr");
                return;
            }            
            //String status = "Run SWMM\n" + bundleText.getString("processing") + "\n";          
            continueExec = this.modelPostgis.execSWMM(fileInp, fileRpt);
        }

        // Import RPT to Postgis
        if (importChecked && continueExec) {
            if (!readyFileRpt) {
                Utils.showError("file_rpt_not_selected", "", "inp_descr");
                return;
            }            
            projectName = view.getProjectName();
            if (projectName.equals("")){
                Utils.showError("project_name", "", "inp_descr");
            } else{
                //String status = "Import RPT to Postgis\n" + bundleText.getString("processing") + "\n";
            	continueExec = this.modelPostgis.importRpt(fileRpt, projectName);
            	if (!continueExec){
            		try {
						ModelPostgis.connectionPostgis.rollback();
					} catch (SQLException e) {
	    	            Utils.showError(e);
					}
            	}
            }
        }
        
    }
    
    
	public void openHelp() {
		if (ModelPostgis.fileHelp != null) {
			try {
				Desktop.getDesktop().open(ModelPostgis.fileHelp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}    
	
	
    public void closeView(){
		view.close();
	}


	public void setSoftware() {
		List<String> list = MainDao.getSoftware();
		view.setSoftware(list);
	}

	
}