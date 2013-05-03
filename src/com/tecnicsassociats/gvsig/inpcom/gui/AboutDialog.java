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
			AboutDialog dialog = new AboutDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutDialog() {

		//setBounds(100, 100, 450, 300);
		setTitle("About");
		setSize(300, 200);
		getContentPane().setLayout(new MigLayout("", "[150px,grow]", "[100][][][50px][50px]"));

		final ImageIcon backgroundImage = new ImageIcon("images/INPcom_eng3.jpg");
		
        JPanel panelLogo = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 3096090575648819722L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                //g.drawImage(backgroundImage.getImage(), 0, 0, 20, 18, this);
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
		
		getContentPane().add(panelLogo, "cell 0 0,grow");

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
	    button.setFont(new Font("Tahoma", Font.BOLD, 12));
	    button.setText("<HTML><FONT color=\"#000099\"><U>www.tecnicsassociats.com</U></FONT></HTML>");
	    button.setHorizontalAlignment(SwingConstants.LEFT);
	    button.setBorderPainted(false);
	    button.setOpaque(false);
	    button.setBackground(Color.WHITE);
	    button.setToolTipText(uri.toString());
	    button.addActionListener(new OpenUrlAction());		
	    
	    JLabel lblTcnicsassociats = new JLabel();
	   // lblTcnicsassociats.setFont(new Font("Tahoma", Font.PLAIN, 16));
	    lblTcnicsassociats.setText("<HTML><FONT color=\"#000000\">T\u00E8cnics</FONT><FONT color=\"#000000\">associats</br></br>\n engineering & geospatial solutions</FONT></HTML>");
	    getContentPane().add(lblTcnicsassociats, "cell 0 1,alignx center");
	    getContentPane().add(button, "cell 0 2,alignx center");
		
		lblInfo = new JLabel("Developer: David Erill Carrera");
		lblInfo.setFont(new Font("Tahoma", Font.BOLD, 12));
		getContentPane().add(lblInfo, "cell 0 3,alignx center");	
		
		JLabel lblNewLabel = new JLabel("INPcom version: 2.0.035");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		getContentPane().add(lblNewLabel, "cell 0 4,alignx center");
	
	}

}

