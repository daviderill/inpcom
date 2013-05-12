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
package com.tecnicsassociats.gvsig.inpcom.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;


public class LicenseDialog extends JDialog {

	private static final long serialVersionUID = 2829254148112384387L;
	public URI uri = null;
	public File file = null;


	public static void main(String[] args) {
		try {
			LicenseDialog dialog = new LicenseDialog("Welcome", "Welcome to INPcom, the EPANET & EPASWMM comunication tool", 
					"Please read the documentation and enjoy using the software");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public LicenseDialog(String title, String info, String info2) {
		this(title, info, info2, "");
	}
	
	
	/**
	 * @wbp.parser.constructor
	 */
	public LicenseDialog(String title, String info, String info2, String info3) {
		getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 8));

		ImageIcon image = new ImageIcon("images/imago.png");
		setIconImage(image.getImage());
		setTitle(title);		
		setSize(550, 230);
		getContentPane().setLayout(new MigLayout("", "[150px,grow]", "[10px][30px][][30.00px][30.00]"));
		
		JLabel lblInfo = new JLabel(info);
		lblInfo.setFont(new Font("Tahoma", Font.BOLD, 12));
		getContentPane().add(lblInfo, "cell 0 1,alignx center");	
		
		JLabel lblInfo12 = new JLabel("<html>\"This product is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details\u201D</html>");
		getContentPane().add(lblInfo12, "cell 0 2");
		
		JLabel lblInfo2 = new JLabel(info2);
		lblInfo2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		getContentPane().add(lblInfo2, "cell 0 3,alignx center");
		
		class OpenUrlAction implements ActionListener {
		    @Override public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		    }
		}		

    	String folderRoot;
		try {
			folderRoot = new File(".").getCanonicalPath() + File.separator;
			file = new File(folderRoot + "licensing.txt");			
		} catch (IOException e) {
			e.printStackTrace();
		}   			
		
		JButton btngsdf = new JButton();
		btngsdf.setFont(new Font("Tahoma", Font.BOLD, 12));
		String text = "<HTML><FONT color=\"#000099\"><U>" + info3 + "</U></FONT></HTML>";
		btngsdf.setText(text);
		btngsdf.setHorizontalAlignment(SwingConstants.LEFT);
		btngsdf.setBorderPainted(false);
		btngsdf.setOpaque(false);
		btngsdf.setBackground(Color.WHITE);
		btngsdf.setToolTipText(file.toString());
		btngsdf.addActionListener(new OpenUrlAction());		
		getContentPane().add(btngsdf, "flowx,cell 0 4,alignx center");
		
		JLabel lblNewLabel = new JLabel("Copyright 2013 T\u00E8cnicsassociats");
		getContentPane().add(lblNewLabel, "cell 0 4");
		
	}

}