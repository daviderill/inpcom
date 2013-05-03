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

import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.Method;

import com.tecnicsassociats.gvsig.inpcom.gui.MainFrame;
import com.tecnicsassociats.gvsig.inpcom.model.ModelPostgis;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;

public class MenuController {

	
	private MainFrame view;
    //private Properties prop;
    
	//private ResourceBundle bundleText;
	
	
	public MenuController(MainFrame mainFrame) {
		
		this.view = mainFrame;
//        this.prop = ModelPostgis.getPropertiesFile();
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
			Utils.logError(e, actionCommand);
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
	
	
	public void showAuthor(){
//		AboutDialog about = new AboutDialog();
//		about.setModal(true);
//		about.setSize(300, 200);
//		about.setLocationRelativeTo(null);   
//		about.setVisible(true);		
	}
	
}
