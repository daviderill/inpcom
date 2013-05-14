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
package com.tecnicsassociats.inpcom;

import java.util.Locale;

import javax.swing.UIManager;

import com.tecnicsassociats.inpcom.controller.DatabaseController;
import com.tecnicsassociats.inpcom.controller.DbfController;
import com.tecnicsassociats.inpcom.controller.MainController;
import com.tecnicsassociats.inpcom.controller.MenuController;
import com.tecnicsassociats.inpcom.gui.Form;
import com.tecnicsassociats.inpcom.gui.MainFrame;
import com.tecnicsassociats.inpcom.model.MainDao;
import com.tecnicsassociats.inpcom.util.Utils;


public class MainClass {

    public static void main(String[] args) {
    	
    	// English language
    	Locale.setDefault(Locale.ENGLISH);

    	// Look&Feel
    	String className = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
    	try {
			UIManager.setLookAndFeel(className);
		} catch (Exception e) {
			e.printStackTrace();
		}  
    	
    	if (!MainDao.configIni()){
    		return;
    	}

        // Create form
    	Form cmWindow = new Form();
        MainFrame mainFrame = new MainFrame();    	
    	
    	// Create controllers
		new MainController(cmWindow);
        new DbfController(cmWindow);
        new DatabaseController(cmWindow);
        new MenuController(mainFrame);
        
        // Open Main Frame
        Utils.openForm(cmWindow, mainFrame, 535, 405);
        cmWindow.setFrame(mainFrame);        

    }
    
}