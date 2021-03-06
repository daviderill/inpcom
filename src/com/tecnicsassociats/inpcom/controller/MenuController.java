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

import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import com.tecnicsassociats.inpcom.gui.AboutDialog;
import com.tecnicsassociats.inpcom.gui.LicenseDialog;
import com.tecnicsassociats.inpcom.gui.MainFrame;
import com.tecnicsassociats.inpcom.gui.WelcomeDialog;
import com.tecnicsassociats.inpcom.model.MainDao;
import com.tecnicsassociats.inpcom.util.Utils;

public class MenuController {

	private MainFrame view;
	private Properties prop;


	public MenuController(MainFrame mainFrame) {
		this.view = mainFrame;
		this.prop = MainDao.getPropertiesFile();
		view.setControl(this);
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
		if (MainDao.fileHelp != null) {
			try {
				Desktop.getDesktop().open(MainDao.fileHelp);
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

//	public void showSoftware() {
//		ResultSet rs = MainDao.getTableResultset(MainDao.connectionConfig, "postgis_software", ResultSet.TYPE_FORWARD_ONLY);
//		TableWindow tableWindow = new TableWindow(rs, "postgis_software");
//        Utils.openDialogForm(tableWindow, 500, 300);       
//	}
	
	public void showLicense() {
		String title = "License";
		String info = "This product as a whole is distributed under the GNU General Public License version 3";
		String info2 = "<html><p align=\"justify\"><font size='2'>This product has been funded wholly or in part by TECNICSASSOCIATS, TALLER D'ARQUITECTURA I ENGINYERIA, SL. (hereafter TECNICSASSOCIATS). Mention of trade names or commercial products does not constitute endorsement or recommendation for use. Although It has been subjected to technical review before being released and although it has made a considerable effort to assure that the results obtained are correct, the computer programs are experimental. Therefore the author and TECNICSASSOCIATS are not responsible and assume no liability whatsoever for any results or any use made of the results obtained from these programs, nor for any damages or litigation that result from the use of these programs for any purpose.</font></p></html>";
		String info3 = "View license file";		
		LicenseDialog about = new LicenseDialog(title, info, info2, info3);
		about.setModal(true);
		about.setLocationRelativeTo(null);
		about.setVisible(true);
	}

	public void showAgreements() {
		String title = "Agreements";
		String info = "Special thanks for his contribution to the project to:";
		String info2 = "Carlos L�pez Quintanilla, Gemma Garc�a Ribot & Andr�s Rodr�guez Valero";
		WelcomeDialog about = new WelcomeDialog(title, info, info2);
		about.setModal(true);
		about.setLocationRelativeTo(null);
		about.setVisible(true);
	}

}