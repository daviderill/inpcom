package com.tecnicsassociats.gvsig.inpcom.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.tecnicsassociats.gvsig.inpcom.model.ModelPostgis;

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


	public WelcomeDialog(String title, String info, String info2) {
		this(title, info, info2, "");
	}
	
	
	/**
	 * @wbp.parser.constructor
	 */
	public WelcomeDialog(String title, String info, String info2, String info3) {
		getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 8));

		try{
			

		ImageIcon image = new ImageIcon("images/imago.png");
		setIconImage(image.getImage());
		//setBounds(100, 100, 450, 300);
		setSize(549, 231);
		getContentPane().setLayout(new MigLayout("", "[150px,grow]", "[][50px][80.00px,grow][30.00]"));

		setTitle(title);
		
		JLabel lblInfo = new JLabel(info);
		lblInfo.setFont(new Font("Tahoma", Font.BOLD, 12));
		getContentPane().add(lblInfo, "cell 0 1,alignx center");	
		
		JLabel lblInfo2 = new JLabel(info2);
		lblInfo2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		getContentPane().add(lblInfo2, "cell 0 2,alignx center");
		
		if (!info3.equals("")){
			
			class OpenUrlAction implements ActionListener {
			    @Override public void actionPerformed(ActionEvent e) {
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			    }
			}		
	
        	String folderRoot = new File(".").getCanonicalPath() + File.separator;   			
			file = new File(folderRoot + "licensing.txt");
			
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
			getContentPane().add(btngsdf, "cell 0 3,alignx center");
			
		}
			
		} catch (Exception e2){
			e2.printStackTrace();
		}
		
	}

}