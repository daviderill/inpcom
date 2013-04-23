package com.tecnicsassociats.gvsig.inpcom.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.tecnicsassociats.gvsig.inpcom.controller.DbfController;
import com.tecnicsassociats.gvsig.inpcom.controller.MainController;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;

import net.miginfocom.swing.MigLayout;


public class Form extends JPanel {

	private static final long serialVersionUID = -2576460232916596200L;
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("form"); //$NON-NLS-1$
	private JFrame f;
	private JDialog dialog;
	private MainController controller;
	private DbfController dbfController;
	private JTextField txtProject;
	private JTextField txtSchema;
	private JTextArea txtFileRpt;
	private JTextArea txtFileInp;
	private JButton btnFileInp;
	private JButton btnFileRpt;
	private JButton btnAccept;
	private JButton btnCancel;
	private JCheckBox chkExport;
	private JCheckBox chkExec;
	private JCheckBox chkImport;
	private JComboBox<String> cboSchema;
	private JTextField txtFileOut;
	private JButton btnFolderShp;
	private JButton btnFolderOut;
	private JButton btnHelp;
	private JButton btnAccept2;
	private JButton btnCancel2;
	private JTextArea txtInput;
	private JTextArea txtExportTo;
	private JButton btnFileInp3;
	private JButton btnAccept3;
	
	private String status;
	private JLabel lblStatus2;


	public Form() {
		try{
			initConfig();
		} catch (MissingResourceException e){
			Utils.showError(e.getMessage(), "", "Error");
			System.exit(ERROR);
		}
	}

	public void setControl(MainController nodeController) {
		this.controller = nodeController;
	}	
	
	public void setControl(DbfController dbfController) {
		this.dbfController = dbfController;
	}
	
	public JFrame getFrame() {
		return new JFrame();
	}

	public void setFrame(JFrame frame) {
		this.f = frame;
	}

	public JDialog getDialog() {
		return new JDialog();
	}

	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	// Add info 
//	public void logStatus(String log) {
//		this.status += log;
//		lblStatus.setText(status);
//		this.repaint();
//		this.validate();
//	}
//	
//	public void resetStatus(){
//		this.status = "";
//		lblStatus.setText(status);
//		this.repaint();
//		this.validate();
//	}
	
	// Panel DBF
	public void setFolderShp(String path) {
		txtInput.setText(path);
	}

	public void setFolderOut(String path) {
		txtExportTo.setText(path);
	}	
	
	public String getTxtFileOut() {
		return txtFileOut.getText().trim();
	}	
	
	// Panel Postgis
	public void setFileRpt(String path) {
		txtFileRpt.setText(path);
	}

	public void setFileInp(String path) {
		txtFileInp.setText(path);
	}

	public void setProjectName(String projectName) {
		txtProject.setText(projectName);
	}
	
	public String getProjectName() {
		return txtProject.getText().trim();
	}

	public void setCboSchema(List<String> elems) {
		DefaultComboBoxModel<String> theModel = (DefaultComboBoxModel<String>) cboSchema.getModel();
		theModel.removeAllElements();
		for (int i = 0; i < elems.size() ; i++) {
			cboSchema.addItem(elems.get(i));
		}
	}
	
	public String getCboSchema() {
		String elem = cboSchema.getSelectedItem().toString();
		return elem;
	}

	public boolean isExportChecked() {
		return chkExport.isSelected();
	}
	
	public boolean isExecChecked() {
		return chkExec.isSelected();
	}

	public boolean isImportChecked() {
		return chkImport.isSelected();
	}
	
	public void close() {
		f.setVisible(false);
		f.dispose();
	}

	private void initConfig() throws MissingResourceException{

		setLayout(new MigLayout("", "[8.00][371.00,grow][]", "[5][][12.00][390.00][12][88.00][40]"));

		JLabel lbl_title = new JLabel();
		lbl_title.setText(BUNDLE.getString("Form.lbl_title.text")); //$NON-NLS-1$
		lbl_title.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(lbl_title, "cell 1 1,alignx center");

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(tabbedPane, "cell 1 3,grow");

		// Panel 1
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab(BUNDLE.getString("Form.panel_1.title"), null, panel_1, null); //$NON-NLS-1$

		// Panel 2		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab(BUNDLE.getString("Form.panel_2.title"), null, panel_2, null); //$NON-NLS-1$
		panel_2.setLayout(new MigLayout("", "[10][40][300px][10][]", "[10][15][50][15][15][50][15][][10][]"));

		JLabel lblInput = new JLabel(BUNDLE.getString("Form.lblNewLabel.text")); //$NON-NLS-1$
		panel_2.add(lblInput, "cell 1 1");

		txtInput = new JTextArea();
		txtInput.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtInput.setText("");
		panel_2.add(txtInput, "cell 1 2 2 1,grow");
		
		btnFolderShp = new JButton();
		btnFolderShp.setText("...");
		btnFolderShp.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnFolderShp.setActionCommand(BUNDLE.getString("Form.btnFolderShp.actionCommand")); //$NON-NLS-1$
		panel_2.add(btnFolderShp, "cell 4 2");
		
		JLabel lblFolderOut = new JLabel(BUNDLE.getString("Form.lblFolderOut.text")); //$NON-NLS-1$
		panel_2.add(lblFolderOut, "cell 1 4");
		
		txtExportTo = new JTextArea();
		txtExportTo.setText("");
		txtExportTo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel_2.add(txtExportTo, "cell 1 5 2 1,grow");
		
		btnFolderOut = new JButton();
		btnFolderOut.setText("...");
		btnFolderOut.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnFolderOut.setActionCommand(BUNDLE.getString("Form.btnFolderOut.actionCommand")); //$NON-NLS-1$
		panel_2.add(btnFolderOut, "cell 4 5");
		
		JLabel lblFileName = new JLabel(BUNDLE.getString("Form.lblFileName.text")); //$NON-NLS-1$
		panel_2.add(lblFileName, "cell 1 7,alignx trailing");
		
		txtFileOut = new JTextField();
		txtFileOut.setText("");
		panel_2.add(txtFileOut, "cell 2 7,growx");
		txtFileOut.setColumns(10);
		
		btnHelp = new JButton();
		btnHelp.setText(BUNDLE.getString("Form.btnHelp.text")); //$NON-NLS-1$
		btnHelp.setName("btn_accept_postgis");
		btnHelp.setActionCommand(BUNDLE.getString("Form.btnHelp.actionCommand")); //$NON-NLS-1$
		panel_2.add(btnHelp, "cell 4 7");
		
		btnAccept2 = new JButton();
		btnAccept2.setText(BUNDLE.getString("Form.btnAccept.text")); //$NON-NLS-1$
		btnAccept2.setName("btn_accept_postgis");
		btnAccept2.setActionCommand(BUNDLE.getString("Form.btnAccept2.actionCommand")); //$NON-NLS-1$
		panel_2.add(btnAccept2, "flowx,cell 2 9,alignx right");
		
		btnCancel2 = new JButton();
		btnCancel2.setText(BUNDLE.getString("Form.btnCancel.text")); //$NON-NLS-1$
		btnCancel2.setName("Cancel");
		btnCancel2.setActionCommand("closeView");
		panel_2.add(btnCancel2, "cell 2 9,alignx right");

		// Panel 3		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab(BUNDLE.getString("Form.panel_4.title"), null, panel_3, null); //$NON-NLS-1$
		panel_3.setLayout(new MigLayout("", "[10.00][][10.00][300.00][10.00][]", "[10][][10][45][15][]"));

		JLabel lblSchemaName = new JLabel(BUNDLE.getString("Form.lblSchemaName.text")); //$NON-NLS-1$
		panel_3.add(lblSchemaName, "cell 1 1");

		txtSchema = new JTextField();
		txtSchema.setMinimumSize(new Dimension(100, 20));
		txtSchema.setPreferredSize(new Dimension(150, 20));
		panel_3.add(txtSchema, "cell 3 1,alignx left");
		txtSchema.setColumns(10);

		JLabel label_3 = new JLabel();
		label_3.setText(BUNDLE.getString("Form.label.text")); //$NON-NLS-1$
		panel_3.add(label_3, "cell 1 3");

		JTextArea txtFileInp3 = new JTextArea();
		txtFileInp3.setLineWrap(true);
		panel_3.add(txtFileInp3, "cell 3 3,grow");

		btnFileInp3 = new JButton();
		btnFileInp3.setText("...");
		btnFileInp3.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_3.add(btnFileInp3, "cell 5 3");

		btnAccept3 = new JButton();
		btnAccept3.setText(BUNDLE.getString("Form.btnAccept.text")); //$NON-NLS-1$
		btnAccept3.setName("btn_accept_postgis");
		btnAccept3.setActionCommand("...");
		panel_3.add(btnAccept3, "cell 3 5,alignx right");

		// Panel 4
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab(BUNDLE.getString("Form.panel_3.title"), null, panel_4, null); //$NON-NLS-1$
		panel_4.setLayout(new MigLayout("", "[10.00][20][][5][100px:300.00px][10][]", "[10][][10][][44.00][10][][44px][10][][][15][]"));

		JLabel lblSelectSchema = new JLabel(BUNDLE.getString("Form.lblSelectSchema.text")); //$NON-NLS-1$
		panel_4.add(lblSelectSchema, "cell 2 1");

		cboSchema = new JComboBox<String>();
		cboSchema.setMinimumSize(new Dimension(150, 22));
		panel_4.add(cboSchema, "cell 4 1,alignx left");

		chkExport = new JCheckBox();
		chkExport.setText(BUNDLE.getString("Form.checkBox.text")); //$NON-NLS-1$
		panel_4.add(chkExport, "cell 1 3 2 1");

		JLabel label = new JLabel();
		label.setText(BUNDLE.getString("Form.label.text")); //$NON-NLS-1$
		panel_4.add(label, "cell 2 4");

		txtFileInp = new JTextArea();
		txtFileInp.setLineWrap(true);
		panel_4.add(txtFileInp, "cell 4 4,grow");

		btnFileInp = new JButton();
		btnFileInp.setActionCommand(BUNDLE.getString("Form.btnFileInp.actionCommand")); //$NON-NLS-1$
		btnFileInp.setText("...");
		btnFileInp.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_4.add(btnFileInp, "cell 6 4");

		chkExec = new JCheckBox();
		chkExec.setText(BUNDLE.getString("Form.checkBox_1.text")); //$NON-NLS-1$
		chkExec.setName("chk_exec");
		chkExec.setActionCommand("Exportaci\u00F3n a INP");
		panel_4.add(chkExec, "cell 1 6 4 1,alignx left");

		JLabel label_1 = new JLabel();
		label_1.setText(BUNDLE.getString("Form.label_1.text")); //$NON-NLS-1$
		panel_4.add(label_1, "cell 2 7");

		txtFileRpt = new JTextArea();
		txtFileRpt.setLineWrap(true);
		panel_4.add(txtFileRpt, "cell 4 7,grow");

		btnFileRpt = new JButton();
		btnFileRpt.setActionCommand(BUNDLE.getString("Form.btnFileRpt.actionCommand")); //$NON-NLS-1$
		btnFileRpt.setText("...");
		btnFileRpt.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_4.add(btnFileRpt, "cell 6 7");

		chkImport = new JCheckBox();
		chkImport.setText(BUNDLE.getString("Form.chkImport.text")); //$NON-NLS-1$
		chkImport.setName("chk_import");
		chkImport.setActionCommand("Exportaci\u00F3n a INP");
		panel_4.add(chkImport, "cell 1 9 2 1");

		JLabel label_2 = new JLabel();
		label_2.setText(BUNDLE.getString("Form.label_2.text")); //$NON-NLS-1$
		label_2.setName("lbl_project");
		panel_4.add(label_2, "cell 2 10");

		txtProject = new JTextField();
		txtProject.setName("txt_project");
		panel_4.add(txtProject, "cell 4 10,growx,aligny top");

		btnAccept = new JButton();
		btnAccept.setText(BUNDLE.getString("Form.btnAccept.text")); //$NON-NLS-1$
		btnAccept.setName("btn_accept_postgis");
		btnAccept.setActionCommand(BUNDLE.getString("Form.btnAccept.actionCommand")); //$NON-NLS-1$
		panel_4.add(btnAccept, "flowx,cell 4 12,alignx right");

		btnCancel = new JButton();
		btnCancel.setText(BUNDLE.getString("Form.btnCancel.text")); //$NON-NLS-1$
		btnCancel.setName(BUNDLE.getString("Form.btnCancel.text")); //$NON-NLS-1$
		btnCancel.setActionCommand(BUNDLE.getString("Form.btnCancel.actionCommand")); //$NON-NLS-1$
		panel_4.add(btnCancel, "cell 4 12");
		
		// Status info
		lblStatus2 = new JLabel("");
		lblStatus2.setIgnoreRepaint(true);
		add(lblStatus2, "cell 1 5");

        // Select Postgis panel by default
		tabbedPane.setSelectedIndex(3);

		setupListeners();
		
	}
	
	
	// Setup component's listener
	private void setupListeners(){

		// Panel Dbf		
		btnFolderShp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbfController.action(e.getActionCommand());				
			}
		});

		btnFolderOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbfController.action(e.getActionCommand());					
			}
		});
		
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbfController.action(e.getActionCommand());					
			}
		});		
		
		btnAccept2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbfController.action(e.getActionCommand());	
			}
		});
		
		btnCancel2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.action(e.getActionCommand());	
			}
		});		
		
		// Panel INP to Database
		btnFileInp3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//controller.action(e.getActionCommand());
				Utils.showMessage("Not implemented", "", "");
			}
		});

		btnAccept3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//controller.action(e.getActionCommand());
				Utils.showMessage("Not implemented", "", "");				
			}
		});
		
		// Panel Postgis
		btnFileInp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.action(e.getActionCommand());				
			}
		});
		
		btnFileRpt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.action(e.getActionCommand());					
			}
		});		

		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.action(e.getActionCommand());					
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.action(e.getActionCommand());					
			}
		});
		
	}


}
