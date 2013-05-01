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

import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.tecnicsassociats.gvsig.inpcom.controller.DbfController;
import com.tecnicsassociats.gvsig.inpcom.controller.MainController;
import com.tecnicsassociats.gvsig.inpcom.controller.OptionsController;
import com.tecnicsassociats.gvsig.inpcom.gui.Form;
import com.tecnicsassociats.gvsig.inpcom.model.ModelDbf;
import com.tecnicsassociats.gvsig.inpcom.model.ModelPostgis;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;


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
    	
        // Create models
        ModelDbf modelDbf = new ModelDbf("SWMM_", Constants.EXEC_CONSOLE);
        ModelPostgis modelPostgis = new ModelPostgis("SWMM_", Constants.EXEC_CONSOLE);        

        // Create form
    	Form cmWindow = new Form();
    	
    	// Create controllers
        @SuppressWarnings("unused")
		MainController mc = new MainController(modelPostgis, cmWindow);
        new DbfController(modelDbf, cmWindow);
        OptionsController op = new OptionsController(modelPostgis, cmWindow);
        
        // TODO: Borrar
        op.testConnection(false);
        //mc.setSoftware();
        
        // Open form in a dialog box
        //JDialog dialog = Utils.openDialogForm(cmWindow, cmWindow.getDialog(), 510, 400);
        //cmWindow.setDialog(dialog);  

        JFrame frame = Utils.openForm(cmWindow, cmWindow.getFrame(), 550, 400);
        cmWindow.setFrame(frame);
		
        //modelPostgis.execute(Constants.EXEC_CONSOLE, true);
        //modelPostgis.execute(Constants.EXEC_CONSOLE, args[0], args[1], args[2]);

    }
    
}