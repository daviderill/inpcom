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

import javax.swing.JDialog;

import com.iver.andami.plugins.Extension;
import com.tecnicsassociats.gvsig.inpcom.controller.DbfController;
import com.tecnicsassociats.gvsig.inpcom.controller.MainController;
import com.tecnicsassociats.gvsig.inpcom.gui.Form;
import com.tecnicsassociats.gvsig.inpcom.model.ModelDbf;
import com.tecnicsassociats.gvsig.inpcom.model.ModelPostgis;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;


public class InpExtension2 extends Extension {

	public void execute(String action) {
		
		//ModelPostgis modelPostgis = new ModelPostgis(action);
		//modelPostgis.execute(Constants.EXEC_GVSIG, true);

        // Create models
        ModelDbf modelDbf = new ModelDbf(action, Constants.EXEC_GVSIG);
        ModelPostgis modelPostgis = new ModelPostgis(action, Constants.EXEC_GVSIG);        

        // Create form
    	Form cmWindow = new Form();
    	
    	// Create controllers
        new MainController(modelPostgis, cmWindow);
        new DbfController(modelDbf, cmWindow);
        
        // Open form in a dialog box
        JDialog dialog = Utils.openDialogForm(cmWindow, cmWindow.getDialog());
        cmWindow.setDialog(dialog);  
        
	}

	public void initialize() {
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		return true;
	}

}