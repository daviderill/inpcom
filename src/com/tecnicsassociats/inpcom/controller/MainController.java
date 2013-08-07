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
import java.io.File;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tecnicsassociats.inpcom.gui.Form;
import com.tecnicsassociats.inpcom.gui.OptionsDialog;
import com.tecnicsassociats.inpcom.gui.RaingageDialog;
import com.tecnicsassociats.inpcom.gui.TableWindow;
import com.tecnicsassociats.inpcom.model.MainDao;
import com.tecnicsassociats.inpcom.model.Model;
import com.tecnicsassociats.inpcom.model.ModelPostgis;
import com.tecnicsassociats.inpcom.util.Encryption;
import com.tecnicsassociats.inpcom.util.Utils;


// Controller of Postgis Panel only
public class MainController{

	private Form view;
    private Properties prop;
    private File fileInp;
    private File fileRpt;
    private String projectName;
    private boolean exportChecked;
    private boolean execChecked;
    private boolean importChecked;
    
    private String userHomeFolder;
    private ResourceBundle bundleText;

    
    public MainController(Form view) {
    	
    	this.view = view;	
        this.prop = MainDao.getPropertiesFile();
	    view.setControl(this);        
    	
    	userHomeFolder = System.getProperty("user.home");
    	this.bundleText = Utils.getBundleText();
    	
    	// Set default values
    	setDefaultValues();
    	    	
	}

    
    private void setDefaultValues(){
    	
    	fileInp = new File(prop.getProperty("FILE_INP", userHomeFolder));
		if (fileInp.exists()) {
			view.setFileInp(fileInp.getAbsolutePath());
		}
		fileRpt = new File(prop.getProperty("FILE_RPT", userHomeFolder));
		if (fileRpt.exists()) {
			view.setFileRpt(fileRpt.getAbsolutePath());
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
			if (Utils.getLogger() != null){
				Utils.getLogger().info(actionCommand);
			}
			method = this.getClass().getMethod(actionCommand);
			method.invoke(this);	
			view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));			
		} catch (Exception e) {
			view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			if (Utils.getLogger() != null){			
				Utils.logError(e, actionCommand);
			} else{
				Utils.showError(e, actionCommand);
			}
		}
		
	}	
	
	
	public void schemaChanged(){
		MainDao.setSchema(view.getSchema());
	}
	
	
	public void showOptions(){
		ResultSet rs = MainDao.getTableResultset("inp_options");
		OptionsDialog dialog = new OptionsDialog();
		InpOptionsController inp = new InpOptionsController(dialog, rs);
		inp.setComponents();
		dialog.setModal(true);
		dialog.setLocationRelativeTo(null);   
		dialog.setVisible(true);		
	}
	

	public void showCatchment(){
		TableWindow tableWindow = new TableWindow();
        JDialog dialog = Utils.openDialogForm(tableWindow, 350, 280);
		ImageIcon image = new ImageIcon("images/imago.png");        
        dialog.setIconImage(image.getImage());
        dialog.setVisible(true);
	}	
	
	
	public void showRaingage(){
		ResultSet rs = MainDao.getTableResultset("raingage");
		RaingageDialog dialog = new RaingageDialog();
		RaingageController inp = new RaingageController(dialog, rs);
		inp.setComponents();
		dialog.setModal(true);
		dialog.setLocationRelativeTo(null);   
		dialog.setVisible(true);		        
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
//            prop.put("FILE_INP", fileInp.getAbsolutePath());
//            MainDao.savePropertiesFile();
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
//            prop.put("FILE_RPT", fileRpt.getAbsolutePath());
//            MainDao.savePropertiesFile();
//            readyFileRpt = true;
        }

    }
    
    
    private boolean getFileInp(){
    	
        String path = view.getFileInp();
        if (path.equals("")){
            return false;        	
        }
        if (path.lastIndexOf(".") == -1) {
            path += ".inp";
        }
        fileInp = new File(path);        
        prop.put("FILE_INP", fileInp.getAbsolutePath());
        MainDao.savePropertiesFile();
        return true;    
        
    }

    
    private boolean getFileRpt(){
    	
        String path = view.getFileRpt();
        if (path.equals("")){
            return false;        	
        }
        if (path.lastIndexOf(".") == -1) {
            path += ".rpt";
        }
        fileRpt = new File(path);        
        prop.put("FILE_RPT", fileRpt.getAbsolutePath());
        MainDao.savePropertiesFile();
        return true;    
        
    }
    

    public void executePostgis() {

        boolean continueExec = true;
        
        // Which checks are selected?
        exportChecked = view.isExportChecked();
        execChecked = view.isExecChecked();
        importChecked = view.isImportChecked();        
        
        if (!exportChecked && !execChecked && !importChecked){
            Utils.showError("select_option", "", "inp_descr");
        }

        // Get schema from view
        String schema = view.getSchema();
        if (schema.equals("")){
            Utils.showError("Any schema selected", "", "inp_descr");
            return;
        }
        MainDao.setSchema(schema);
        
        // Get software version from view
        String id = view.getSoftwarePostgis();
        if (id.equals("")){
            Utils.showError("Any software version selected", "", "inp_descr");
            return;
        }
        String version = MainDao.getSoftwareVersion("postgis", id);
        Model.setSoftware(version);
        
		// Get Sqlite Database			
		String sqlitePath = version + ".sqlite";
		if (!Model.setConnectionDrivers(sqlitePath)){
			return;
		}        
        
        view.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        // Export to INP
        if (exportChecked) {
            if (!getFileInp()) {
                Utils.showError("file_inp_not_selected", "", "inp_descr");
                return;
            }                
            continueExec = ModelPostgis.processAll(fileInp);
        }

        // Run SWMM
        if (execChecked && continueExec) {
            if (!getFileInp()) {
                Utils.showError("file_inp_not_selected", "", "inp_descr");
                return;
            }            
            if (!getFileRpt()) {
                Utils.showError("file_rpt_not_selected", "", "inp_descr");
                return;
            }                  
            continueExec = ModelPostgis.execSWMM(fileInp, fileRpt);
        }

        // Import RPT to Postgis
        if (importChecked && continueExec) {
            if (!getFileRpt()) {
                Utils.showError("file_rpt_not_selected", "", "inp_descr");
                return;
            }            
            projectName = view.getProjectName();
            if (projectName.equals("")){
                Utils.showError("project_name", "", "inp_descr");
            } else{
            	continueExec = ModelPostgis.importRpt(fileRpt, projectName);
            	Model.closeFile();
            	if (!continueExec){
            		try {
						ModelPostgis.rollback();
					} catch (SQLException e) {
	    	            Utils.showError(e);
					}
            	}
            }
        }
        
    }
    
    	
	public void setSoftware() {
		view.setSoftwarePostgis(MainDao.getAvailableVersions("postgis"));
	}
	
}