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
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 2829254148112384387L;
	private JLabel lblInfo;
	public URI uri = null;	


	public static void main(String[] args) {
		try {
			AboutDialog dialog = new AboutDialog("About", "INPcom version: 2.0.035");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutDialog(String title, String version) {

		ImageIcon image = new ImageIcon("images/imago.png");
		setIconImage(image.getImage());		
		//setBounds(100, 100, 450, 300);
		setTitle(title);
		setSize(340, 238);
		getContentPane().setLayout(new MigLayout("", "[116.00][173.00px,grow]", "[125.00px][70px][][][50px][50px]"));

		final ImageIcon backgroundImage = new ImageIcon("images/imago.png");
		
        JPanel panelLogo = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 3096090575648819722L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                g.drawImage(backgroundImage.getImage(), 0, 0, 90, 90, this);
            }

            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width = Math.max(backgroundImage.getIconWidth(), size.width);
                size.height = Math.max(backgroundImage.getIconHeight(), size.height);
                //size.width = 20;
                //size.height = 18;                
                return size;
            }
        };
		getContentPane().add(panelLogo, "cell 1 0,alignx center,growy");

		class OpenUrlAction implements ActionListener {
		    @Override public void actionPerformed(ActionEvent e) {
		        if (Desktop.isDesktopSupported()) {
		            try {
		              Desktop.getDesktop().browse(uri);
		            } catch (IOException e1) { }
		        }
		    }
		}		

		try {
			uri = new URI("http://www.tecnicsassociats.com");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}		
		JButton button = new JButton();
		button.setFont(new Font("Tahoma", Font.BOLD, 11));
		button.setText("<HTML><FONT color=\"#000099\"><U>www.tecnicsassociats.com</U></FONT></HTML>");
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setBorderPainted(false);
		button.setOpaque(false);
		button.setBackground(Color.WHITE);
		button.setToolTipText(uri.toString());
		button.addActionListener(new OpenUrlAction());		
		
		String label = "<HTML><FONT size=6 color=\"#336600\">Tècnics</FONT><FONT size=6 color=\"#000000\">Associats</FONT></HTML>";
		JLabel lblLogo = new JLabel(label);
		lblLogo.setFont(new Font("Tahoma", Font.PLAIN, 10));
		getContentPane().add(lblLogo, "cell 0 1 2 1,alignx center");
		getContentPane().add(button, "cell 0 3 2 1,alignx center");
		
		JLabel lblTcnicsassociats = new JLabel();
		lblTcnicsassociats.setFont(new Font("Tahoma", Font.BOLD, 10));
		//lblTcnicsassociats.setText("<HTML><FONT color=\"#000000\">engineering & geospatial solutions</FONT></HTML>");
		lblTcnicsassociats.setText("engineering & geospatial solutions");
		getContentPane().add(lblTcnicsassociats, "cell 0 2 2 1,alignx center");
		
		lblInfo = new JLabel("Developer: David Erill Carrera");
		lblInfo.setFont(new Font("Tahoma", Font.BOLD, 10));
		getContentPane().add(lblInfo, "cell 0 4 2 1,alignx center");	
		
		JLabel lblNewLabel = new JLabel(version);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 10));
		getContentPane().add(lblNewLabel, "cell 0 5 2 1,alignx center");
	
	}

}

