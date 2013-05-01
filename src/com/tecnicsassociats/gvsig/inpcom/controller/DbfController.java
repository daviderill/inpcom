package com.tecnicsassociats.gvsig.inpcom.controller;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.tecnicsassociats.gvsig.inpcom.gui.Form;
import com.tecnicsassociats.gvsig.inpcom.model.Model;
import com.tecnicsassociats.gvsig.inpcom.model.ModelDbf;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;

//Controller of DBF Panel only
public class DbfController {

	private Form view;
	private ModelDbf modelDbf;
	private Properties prop;

	private boolean readyShp = false;
	private boolean readyOut = false;
	private File dirShp;
	private File dirOut;

	private String userHomeFolder;
	//private ResourceBundle bundleForm;
	private ResourceBundle bundleText;

	
	public DbfController(ModelDbf modelDbf, Form view) {

		this.view = view;
		this.modelDbf = modelDbf;
		this.prop = Model.getPropertiesFile();
		view.setControl(this);
		view.setSoftwareDbf(null);

		userHomeFolder = System.getProperty("user.home");
		//this.bundleForm = Utils.getBundleForm();
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
		Utils.getLogger().info(actionCommand);
		try {
			method = this.getClass().getMethod(actionCommand);
			method.invoke(this);
		} catch (NoSuchMethodException e) {
			Utils.getLogger().warning(e.getMessage());
		} catch (SecurityException e) {
			Utils.getLogger().warning(e.getMessage());
		} catch (IllegalAccessException e) {
			Utils.getLogger().warning(e.getMessage());
		} catch (IllegalArgumentException e) {
			Utils.getLogger().warning(e.getMessage());
		} catch (InvocationTargetException e) {
			Utils.getLogger().warning(e.getMessage());
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
			// form.getTextComponent(Constants.TXT_DIR_SHP).setText(dirShp.getAbsolutePath());
			view.setFolderShp(dirShp.getAbsolutePath());
			prop.put("FOLDER_SHP", dirShp.getAbsolutePath());
			ModelDbf.savePropertiesFile();
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
			ModelDbf.savePropertiesFile();
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

		// Connect to sqlite database
		String sqliteFile = prop.getProperty(this.modelDbf.sExport + "DB_DBF");
		if (!this.modelDbf.connectDB(sqliteFile)) {
			return;
		}

		// Check if all necessary files exist
		if (!this.modelDbf.checkFiles(dirShp.getAbsolutePath(),
				dirOut.getAbsolutePath())) {
			return;
		}

		// Process all shapes and output to INP file
		String sFileOut = view.getTxtFileOut();
		this.modelDbf.processAll(dirOut.getAbsolutePath(), sFileOut);

	}
	
	
    public void closeView(){
		view.close();
	}
    

}
