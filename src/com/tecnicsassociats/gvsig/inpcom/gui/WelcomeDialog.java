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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;


public class WelcomeDialog extends JDialog {

	private static final long serialVersionUID = 2829254148112384387L;
	public URI uri = null;
	public File file = null;


	public static void main(String[] args) {
		try {
			WelcomeDialog dialog = new WelcomeDialog("Welcome", "Welcome to INPcom, the EPANET & EPASWMM comunication tool", 
					"Please read the documentation and enjoy using the software");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	/**
	 * @wbp.parser.constructor
	 */
	public WelcomeDialog(String title, String info, String info2) {
		getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 8));
	
		ImageIcon image = new ImageIcon("images/imago.png");
		setIconImage(image.getImage());
		setTitle(title);		
		setSize(549, 231);
		getContentPane().setLayout(new MigLayout("", "[150px,grow]", "[10px][grow][30px][30.00px][30.00]"));

		
		final ImageIcon backgroundImage = new ImageIcon("images/imago.png");
		
        JPanel panelLogo = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 3096090575648819722L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, 90, 90, this);
            }

            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width = Math.max(backgroundImage.getIconWidth(), size.width);
                size.height = Math.max(backgroundImage.getIconHeight(), size.height);
                size.width = 90;
                size.height = 90;  
                return size;
            }
        };
		getContentPane().add(panelLogo, "cell 0 1,alignx center,growy");
		panelLogo.setLayout(new BorderLayout());
		
		JLabel lblInfo = new JLabel(info);
		lblInfo.setFont(new Font("Tahoma", Font.BOLD, 12));
		getContentPane().add(lblInfo, "cell 0 2,alignx center");	
		
		JLabel lblInfo2 = new JLabel(info2);
		lblInfo2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		getContentPane().add(lblInfo2, "cell 0 3,alignx center");
		
	}

}