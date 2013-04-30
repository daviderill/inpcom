package com.tecnicsassociats.gvsig.inpcom.controller;

import java.lang.reflect.Method;
import java.util.Properties;

import com.tecnicsassociats.gvsig.inpcom.gui.Form;
import com.tecnicsassociats.gvsig.inpcom.model.ModelPostgis;
import com.tecnicsassociats.gvsig.inpcom.util.Encryption;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;

public class OptionsController {

	private Form view;
	private ModelPostgis modelPostgis;
    private Properties prop;
    
	private boolean isConnected = false;
	private boolean showInfo = true;
	
	
	public OptionsController(ModelPostgis modelPostgis, Form cmWindow) {
		
		this.view = cmWindow;
		this.modelPostgis = modelPostgis;		
        this.prop = ModelPostgis.getPropertiesFile();
	    view.setControl(this);        
    	
//    	userHomeFolder = System.getProperty("user.home");
//    	this.bundleForm = Utils.getBundleForm();
//    	this.bundleText = Utils.getBundleText();
    	
	}
	
	
	public void action(String actionCommand) {
		Method method;
		try {
			Utils.getLogger().info(actionCommand);
			method = this.getClass().getMethod(actionCommand);
			method.invoke(this);	
		} catch (Exception e) {
			Utils.getLogger().warning("Method not found: " + actionCommand);
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
		//String connectionString = "jdbc:postgresql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + password;
		isConnected  = modelPostgis.setConnectionPostgis(host, port, db, user, password);
		
		if (isConnected){
			view.setSchemas(modelPostgis.getSchemas());
			view.setDefaultSchema();
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
			ModelPostgis.savePropertiesFile();

			if (showInfo){
				Utils.showMessage("Connection successful!", "", "INP.com");
			} 
			showInfo = true;			
			
		} else{
			view.setSchemas(null);
		}
		//view.enablePanels(isConnected);
		
	}	
	
}
