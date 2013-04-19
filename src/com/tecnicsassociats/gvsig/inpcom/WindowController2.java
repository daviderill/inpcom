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

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jeta.forms.components.panel.FormPanel;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;


public class WindowController2 implements ActionListener {

	private MainWindow view;
    private ModelDbf model;
    private ModelPostgis modelPostgis;
    private Properties prop;
    private FormPanel form;
    // Dbf
    private boolean readyShp = false;
    private boolean readyOut = false;
    private File dirShp;
    private File dirOut;
    // Postgis
    private File fileInp;
    private File fileRpt;
    private String projectName;
    private boolean readyFileInp = false;
    private boolean readyFileRpt = false;
    private String netType = "ACTUAL";
    private boolean exportChecked;
    private boolean execChecked;
    private boolean importChecked;
    
    private String userHomeFolder;
    private ResourceBundle bundle;

    
    public WindowController2(ModelDbf modelDbf, ModelPostgis modelPostgis, MainWindow view) {

    	this.view = view;
        this.model = modelDbf;
        this.modelPostgis = modelPostgis;
        this.form = view.getForm();
        this.prop = ModelPostgis.getPropertiesFile();
        String sVersion = ""; 
        String schemaProp = this.modelPostgis.sExport + "SCHEMA_" + netType;       
    	this.modelPostgis.schema = prop.getProperty(schemaProp);
    	
    	userHomeFolder = System.getProperty("user.home");
    	this.bundle = Utils.getBundle();

        try {

            // DBF panel
            form.getLabel(Constants.LBL_TITLE).setText(bundle.getString("lbl_title"));
            form.getLabel(Constants.LBL_DIR_SHP).setText(bundle.getString("lbl_dir_shp"));
            form.getLabel(Constants.LBL_DIR_OUT).setText(bundle.getString("lbl_dir_out"));
            form.getLabel(Constants.LBL_FILE_OUT).setText(bundle.getString("lbl_file_out"));
            sVersion = bundle.getString("lbl_version") + " " + prop.getProperty("VERSION_CODE");
            form.getLabel(Constants.LBL_VERSION).setText(sVersion);
            form.getButton(Constants.BTN_ACCEPT).setText(bundle.getString("btn_accept"));
            form.getButton(Constants.BTN_CANCEL).setText(bundle.getString("btn_cancel"));
            form.getButton(Constants.BTN_HELP_TEMPLATE).setText(bundle.getString("btn_help_template"));

            form.getButton(Constants.BTN_FOLDER_SHP).setActionCommand("shp");
            form.getButton(Constants.BTN_FOLDER_INP).setActionCommand("out");
            form.getButton(Constants.BTN_ACCEPT).setActionCommand("accept");
            form.getButton(Constants.BTN_CANCEL).setActionCommand("cancel");
            form.getButton(Constants.BTN_HELP_TEMPLATE).setActionCommand("help");

            form.getButton(Constants.BTN_FOLDER_SHP).addActionListener(this);
            form.getButton(Constants.BTN_FOLDER_INP).addActionListener(this);
            form.getButton(Constants.BTN_ACCEPT).addActionListener(this);
            form.getButton(Constants.BTN_CANCEL).addActionListener(this);
            form.getButton(Constants.BTN_HELP_TEMPLATE).addActionListener(this);

            dirShp = new File(prop.getProperty("FOLDER_SHP2", userHomeFolder));
            if (dirShp.exists()) {
                form.getTextComponent(Constants.TXT_DIR_SHP).setText(dirShp.getAbsolutePath());
                readyShp = true;
            }
            dirOut = new File(prop.getProperty("FOLDER_INP", userHomeFolder));
            if (dirOut.exists()) {
                form.getTextComponent(Constants.TXT_DIR_OUT).setText(dirOut.getAbsolutePath());
                readyOut = true;
            }

            // Postgis panel
            String export = this.modelPostgis.sExport.toLowerCase();
            form.getLabel("lbl_title").setText(bundle.getString(export + "lbl_title"));            
            form.getLabel("lbl_type").setText(bundle.getString("lbl_type"));
            form.getLabel("lbl_file_inp").setText(bundle.getString("lbl_file_inp"));  
            form.getLabel("lbl_file_rpt").setText(bundle.getString("lbl_file_rpt"));  
            form.getLabel("lbl_project").setText(bundle.getString("lbl_project"));              
            form.getCheckBox("chk_export_inp").setText(bundle.getString("chk_export_inp"));
            form.getCheckBox("chk_exec").setText(bundle.getString("chk_" + export + "exec"));
            form.getCheckBox("chk_import").setText(bundle.getString("chk_import"));
            form.getCheckBox("chk_import").setText(bundle.getString("chk_import"));            
            form.getButton("btn_file_inp").setActionCommand("file_inp");
            form.getButton("btn_file_rpt").setActionCommand("file_rpt");
            form.getRadioButton("opt_now").setActionCommand("ACTUAL");
            form.getRadioButton("opt_future").setActionCommand("FUTURE");
            form.getRadioButton("opt_now").setText(bundle.getString("opt_now"));
            form.getRadioButton("opt_future").setText(bundle.getString("opt_future"));
            form.getButton("btn_accept_postgis").setText(bundle.getString("btn_accept"));
            form.getButton("btn_cancel_postgis").setText(bundle.getString("btn_cancel"));
            form.getButton("btn_accept_postgis").setActionCommand("accept_postgis");
            form.getButton("btn_cancel_postgis").setActionCommand("cancel_postgis");

            form.getRadioButton("opt_future").setSelected(true);
            form.getRadioButton("opt_now").addActionListener(this);
            form.getRadioButton("opt_future").addActionListener(this);
            form.getButton("btn_accept_postgis").addActionListener(this);
            form.getButton("btn_cancel_postgis").addActionListener(this);
            form.getButton("btn_file_inp").addActionListener(this);
            form.getButton("btn_file_rpt").addActionListener(this);

            fileInp = new File(prop.getProperty("FILE_INP", userHomeFolder));
            if (fileInp.getParentFile() != null && fileInp.getParentFile().exists()) {
                form.getTextComponent("txt_file_inp").setText(fileInp.getAbsolutePath());
                readyFileInp = true;
            }

            fileRpt = new File(prop.getProperty("FILE_RPT", userHomeFolder));
            if (fileRpt.getParentFile() != null && fileRpt.getParentFile().exists()) {
                form.getTextComponent("txt_file_rpt").setText(fileRpt.getAbsolutePath());
                readyFileRpt = true;
            }
            projectName = prop.getProperty("PROJECT_NAME", "result");
            form.getTextComponent("txt_project").setText(projectName);     
            
            // Select Postgis panel by default
            form.getTabbedPane("panel").setSelectedIndex(1);

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        System.out.println(command);

        // DBF actions
        if (command.equalsIgnoreCase("shp")) {
            chooseFolderShp();
        } else if (command.equalsIgnoreCase("out")) {
            chooseFolderOut();
        } else if (command.equalsIgnoreCase("accept")) {
            executeDbf();
        } else if (command.equalsIgnoreCase("cancel")) {
        	view.close();
        } else if (command.equalsIgnoreCase("help")) {
            openPDF();
        } // Postgis actions
        else if (command.equalsIgnoreCase("file_inp")) {
            chooseFileInp();
        } else if (command.equalsIgnoreCase("file_rpt")) {
            chooseFileRpt();
        } else if (command.equalsIgnoreCase("ACTUAL") || command.equalsIgnoreCase("FUTURE")) {
            netType = command;
            String schemaProp = this.modelPostgis.sExport + "SCHEMA_" + netType;              
        	this.modelPostgis.schema = prop.getProperty(schemaProp);     
        } else if (command.equalsIgnoreCase("accept_postgis")) {
            exportChecked = form.getCheckBox("chk_export_inp").isSelected();
            execChecked = form.getCheckBox("chk_exec").isSelected();
            importChecked = form.getCheckBox("chk_import").isSelected();
            executePostgis();
        } else if (command.equalsIgnoreCase("cancel_postgis")) {
        	view.close();
        }

    }


    // Begin DBF Actions
    private void openPDF() {
        if (this.model.fileHelp != null) {
            try {
                Desktop.getDesktop().open(this.model.fileHelp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void executeDbf() {

        if (!readyShp) {
            Utils.showError("dir_shp_not_selected", "", "inp_descr");
            return;
        }
        if (!readyOut) {
            Utils.showError("dir_out_not_selected", "", "inp_descr");
            return;
        }

        // Connect to sqlite database
        String sqliteFile = prop.getProperty(this.model.sExport + "DB_DBF");
        if (!this.model.connectDB(sqliteFile)) {
            return;
        }

        // Check if all necessary files exist
        if (!this.model.checkFiles(dirShp.getAbsolutePath(), dirOut.getAbsolutePath())) {
            return;
        }

//        fileInp = null;
        String sFileOut = form.getTextField(Constants.TXT_FILE_OUT).getText().trim();
//        if (!sFileOut.equals("")) {
//            sFileOut = dirOut.getAbsolutePath() + File.separator + sFileOut;
//            fileInp = new File(sFileOut);
//        }
        
        // Process all shapes and output to INP file
        this.model.processALL(dirOut.getAbsolutePath(), sFileOut);

    }


    private void chooseFolderShp() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle(bundle.getString("folder_shp"));
        File file = new File(prop.getProperty("FOLDER_SHP", userHomeFolder));
        chooser.setCurrentDirectory(file);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dirShp = chooser.getSelectedFile();
            form.getTextComponent(Constants.TXT_DIR_SHP).setText(dirShp.getAbsolutePath());
            prop.put("FOLDER_SHP", dirShp.getAbsolutePath());
            ModelDbf.savePropertiesFile();
            readyShp = true;
        }

    }


    private void chooseFolderOut() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle(bundle.getString("folder_out"));
        File file = new File(prop.getProperty("FOLDER_INP", userHomeFolder));
        chooser.setCurrentDirectory(file);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dirOut = chooser.getSelectedFile();
            form.getTextComponent(Constants.TXT_DIR_OUT).setText(dirOut.getAbsolutePath());
            prop.put("FOLDER_INP", dirOut.getAbsolutePath());
            ModelDbf.savePropertiesFile();
            readyOut = true;
        }

    }
    // End DBF Actions


    // Begin Postgis Actions
    private void chooseFileInp() {

        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("INP extension file", "inp");
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle(bundle.getString("file_inp"));
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
            form.getTextComponent("txt_file_inp").setText(fileInp.getAbsolutePath());
            prop.put("FILE_INP", fileInp.getAbsolutePath());
            ModelPostgis.savePropertiesFile();
            readyFileInp = true;
        }

    }


    private void chooseFileRpt() {

        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("RPT extension file", "rpt");
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle(bundle.getString("file_rpt"));
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
            form.getTextComponent("txt_file_rpt").setText(fileRpt.getAbsolutePath());
            prop.put("FILE_RPT", fileRpt.getAbsolutePath());
            ModelPostgis.savePropertiesFile();
            readyFileRpt = true;
        }

    }


    private void executePostgis() {

        boolean anySelected = false;

        // Connect to sqlite database
        String sqliteFile = prop.getProperty(this.modelPostgis.sExport + "DB_POSTGIS_" + netType);        
        if (!this.modelPostgis.connectDB(sqliteFile)) {
            return;
        }

        // Export to INP
        if (exportChecked) {
            anySelected = true;
            if (!readyFileInp) {
                Utils.showError("file_inp_not_selected", "", "inp_descr");
                return;
            }            
            this.modelPostgis.processALL(fileInp);
        }

        // Run SWMM
        if (execChecked) {
            anySelected = true;
            if (!readyFileInp) {
                Utils.showError("file_inp_not_selected", "", "inp_descr");
                return;
            }            
            if (!readyFileRpt) {
                Utils.showError("file_rpt_not_selected", "", "inp_descr");
                return;
            }            
            this.modelPostgis.execSWMM(fileInp, fileRpt);
        }

        // Import RPT to Postgis
        if (importChecked) {
            anySelected = true;
            if (!readyFileRpt) {
                Utils.showError("file_rpt_not_selected", "", "inp_descr");
                return;
            }            
            projectName = form.getTextComponent("txt_project").getText().trim();
            if (projectName.equals("")){
                Utils.showError("project_name", "", "inp_desc");
            } else{
            	this.modelPostgis.importRpt(fileRpt, projectName);
            }
        }

        if (!anySelected) {
            Utils.showError("select_option", "", "inp_desc");
        }

    }
}