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
import java.util.Properties;

import com.tecnicsassociats.gvsig.inpcom.gui.AboutDialog;
import com.tecnicsassociats.gvsig.inpcom.gui.MainFrame;
import com.tecnicsassociats.gvsig.inpcom.gui.WelcomeDialog;
import com.tecnicsassociats.gvsig.inpcom.model.ModelPostgis;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;

public class MenuController {

	private MainFrame view;
	private Properties prop;

	// private ResourceBundle bundleText;

	public MenuController(MainFrame mainFrame) {

		this.view = mainFrame;
		this.prop = ModelPostgis.getPropertiesFile();
		view.setControl(this);

		// userHomeFolder = System.getProperty("user.home");
		// this.bundleForm = Utils.getBundleForm();
		// this.bundleText = Utils.getBundleText();

	}

	public void action(String actionCommand) {

		Method method;
		try {
			if (Utils.getLogger() != null) {
				Utils.getLogger().info(actionCommand);
			}
			method = this.getClass().getMethod(actionCommand);
			method.invoke(this);
		} catch (Exception e) {
			if (Utils.getLogger() != null) {
				Utils.logError(e, actionCommand);
			} else {
				Utils.showError(e, actionCommand);
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

	public void showWelcome() {
		String title = "Welcome";
		String info = "Welcome to INPcom, the EPANET & EPASWMM comunication tool";
		String info2 = "Please read the documentation and enjoy using the software";
		WelcomeDialog about = new WelcomeDialog(title, info, info2);
		about.setModal(true);
		about.setLocationRelativeTo(null);
		about.setVisible(true);
	}

	public void showAuthor() {
		String version = "INPcom version " + prop.getProperty("VERSION_CODE");
		AboutDialog about = new AboutDialog("About", version);
		about.setModal(true);
		// about.setSize(300, 200);
		about.setLocationRelativeTo(null);
		about.setVisible(true);
	}

	public void showLicense() {
		String title = "License";
		String info = "This product as a whole is distributed under the GNU General Public License version 3";
		String info2 = "<html><font size='2'>This tool has been funded wholly or in part by TECNICSASSOCIATS, TALLER D'ARQUITECTURA I ENGINYERIA, SL. (hereafter TECNICSASSOCIATS). Mention of trade names or commercial products does not constitute endorsement or recommendation for use. Although It has been subjected to technical review before being released and although it has made a considerable effort to assure that the results obtained are correct, the computer programs are experimental. Therefore the author and TECNICSASSOCIATS are not responsible and assume no liability whatsoever for any results or any use made of the results obtained from these programs, nor for any damages or litigation that result from the use of these programs for any purpose.</font></html>";
		String info3 = "View license file";		
		WelcomeDialog about = new WelcomeDialog(title, info, info2, info3);
		about.setModal(true);
		about.setLocationRelativeTo(null);
		about.setVisible(true);
	}

	public void showAgreements() {
		String title = "Agreements";
		String info = "Special thanks for his contribution to the project to:";
		String info2 = "Carlos López Quintanilla, Andreu Rodríguez and Gemma Garcia";
		WelcomeDialog about = new WelcomeDialog(title, info, info2);
		about.setModal(true);
		about.setLocationRelativeTo(null);
		about.setVisible(true);
	}

}
