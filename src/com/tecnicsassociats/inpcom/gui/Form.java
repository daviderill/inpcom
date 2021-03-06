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
package com.tecnicsassociats.inpcom.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;

import com.tecnicsassociats.inpcom.controller.DatabaseController;
import com.tecnicsassociats.inpcom.controller.DbfController;
import com.tecnicsassociats.inpcom.controller.MainController;
import com.tecnicsassociats.inpcom.util.Utils;


public class Form extends JPanel implements ActionListener {

	private static final long serialVersionUID = -2576460232916596200L;
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("form"); //$NON-NLS-1$

	private JFrame f;
	private MainController controller;
	private DbfController dbfController;
	private DatabaseController databaseController;

	private JTextField txtProject;
	private JTextField txtSchema;
	private JTextArea txtFileRpt;
	private JTextArea txtFileInp;
	private JButton btnFileInp;
	private JButton btnFileRpt;
	private JButton btnAccept;
	private JCheckBox chkExport;
	private JCheckBox chkExec;
	private JCheckBox chkImport;
	private JComboBox<String> cboSchema;
	private JTextField txtFileOut;
	private JButton btnFolderShp;
	private JButton btnFolderOut;
	private JButton btnAccept2;
	private JTextArea txtExportTo;
	private JButton btnFileInp3;
	private JButton btnAccept3;
	private JButton btnCatchSelection;
	private JButton btnOptions;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JComboBox<String> cboDriver;
	private JLabel lblPort;
	private JLabel lblIp;
	private JTextField txtIP;
	private JTextField txtPort;
	private JLabel lblDatabase;
	private JTextField txtDatabase;
	private JLabel lblUser;
	private JTextField txtUser;
	private JLabel lblPassword;
	private JPasswordField txtPassword;
	private JPanel panel_5;
	private JLabel lblNewLabel_1;
	private JLabel lblResultId;
	private JComboBox<String> cboResultId;
	private JScrollPane scrollPane;
	private JTextArea txtInput;
	private JButton btnTest;
	private JCheckBox chkRemember;
	private JButton btnRaingage;
	private JComboBox<String> cboSchema1;
	private JPanel panel_4;
	private JTabbedPane tabbedPane;
	private JComboBox<String> cboSoftware;
	private JLabel lblEpaSoftware;
	private JComboBox<String> cboSoftwareDbf;
	private JLabel lblEpaSoftware_1;
	private JPanel panel_2;
	private JButton btnCreateSchema;
	private JButton btnDeleteSchema;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_3;

	
	public Form() {
		try {
			initConfig();
			enableButtons(false);
		} catch (MissingResourceException e) {
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

	public void setControl(DatabaseController databaseController) {
		this.databaseController = databaseController;
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


	// Panel Database Options
	public void enableButtons(boolean isEnabled) {
		btnAccept.setEnabled(isEnabled);
		btnCreateSchema.setEnabled(isEnabled);
		btnDeleteSchema.setEnabled(isEnabled);
		btnOptions.setEnabled(isEnabled);
		btnCatchSelection.setEnabled(isEnabled);
		btnRaingage.setEnabled(isEnabled);
	}
	
	public Integer getDriver() {
		return cboDriver.getSelectedIndex();
	}
	
	public String getHost() {
		return txtIP.getText().trim();
	}

	public void setHost(String text) {
		txtIP.setText(text);
	}

	public String getPort() {
		return txtPort.getText().trim();
	}

	public void setPort(String text) {
		txtPort.setText(text);
	}

	public String getDatabase() {
		return txtDatabase.getText().trim();
	}

	public void setDatabase(String text) {
		txtDatabase.setText(text);
	}

	public String getUser() {
		return txtUser.getText().trim();
	}

	public void setUser(String text) {
		txtUser.setText(text);
	}

	@SuppressWarnings("deprecation")
	public String getPassword() {
		return txtPassword.getText();
	}

	public void setPassword(String path) {
		txtPassword.setText(path);
	}

	public boolean getRemember() {
		return chkRemember.isSelected();
	}
	
	public String getSchemaResult() {
		String elem = "";
		if (cboSchema1.getSelectedIndex() != -1) {
			elem = cboSchema1.getSelectedItem().toString();
		}
		return elem;
	}	
	
	public void setResultCombo(Vector<String> v) {
		ComboBoxModel<String> cbm = new DefaultComboBoxModel<String>(v);
		cboResultId.setModel(cbm);
	}	

	public String getResult() {
		String elem = "";
		if (cboResultId.getSelectedIndex() != -1) {
			elem = cboResultId.getSelectedItem().toString();
		}
		return elem;		
	}
	
	
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

	public String getSoftwareDbf() {
		String elem = "";
		if (cboSoftwareDbf.getSelectedIndex() != -1) {
			elem = cboSoftwareDbf.getSelectedItem().toString();
		}
		return elem;
	}	
	
	public void setSoftwareDbf(Vector<String> v) {
		ComboBoxModel<String> cbm = new DefaultComboBoxModel<String>(v);
		cboSoftwareDbf.setModel(cbm);		
	}
	
	
	// Panel Postgis
	public void setSoftwarePostgis(Vector<String> v) {
		ComboBoxModel<String> cbm = new DefaultComboBoxModel<String>(v);
		cboSoftware.setModel(cbm);		
	}
	
	public String getSoftwarePostgis() {
		String elem = "";
		if (cboSoftware.getSelectedIndex() != -1) {
			elem = cboSoftware.getSelectedItem().toString();
		}
		return elem;
	}	
	
	public void setSchemas(Vector<String> v) {
		ComboBoxModel<String> cbm = new DefaultComboBoxModel<String>(v);
		cboSchema.setModel(cbm);
		cboSchema1.setModel(cbm);		
	}

	public String getSchema1() {
		String elem = "";
		if (cboSchema1.getSelectedIndex() != -1) {
			elem = cboSchema1.getSelectedItem().toString();
		}
		return elem;
	}
	
	public String getSchema() {
		String elem = "";
		if (cboSchema.getSelectedIndex() != -1) {
			elem = cboSchema.getSelectedItem().toString();
		}
		return elem;
	}
	
	public String getFileRpt() {
		String fileRpt = txtFileRpt.getText().trim();
		return fileRpt;
	}
	
	public void setFileRpt(String path) {
		txtFileRpt.setText(path);
	}
	
	public String getFileInp() {
		String fileInp = txtFileInp.getText().trim();
		return fileInp;
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

//	public void setDefaultSchema() {
//		if (cboSchema.getItemCount() > 0) {
//			cboSchema.setSelectedIndex(cboSchema.getItemCount() - 1);
//		}
//	}
	
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

	
	private void initConfig() throws MissingResourceException {

		setLayout(new MigLayout("", "[8.00][:531px:531px][40.00]", "[10px][410.00][12]"));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(tabbedPane, "cell 1 1,grow");

		// Panel 1
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab(
				BUNDLE.getString("Form.panel_1.title"), null, panel_1, null); //$NON-NLS-1$
		panel_1.setLayout(new MigLayout("", "[10][][380]", "[5][208.00][10][80][]"));

		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.add(panel, "cell 1 1 2 1,grow");
		panel.setLayout(new MigLayout("", "[5][60][15][140][11.00][125.00]",
				"[4][24][24][24][24][24][24][]"));

		lblNewLabel = new JLabel(BUNDLE.getString("Form.lblNewLabel.text_2")); //$NON-NLS-1$
		panel.add(lblNewLabel, "cell 1 1");

		cboDriver = new JComboBox<String>();
		cboDriver.setPreferredSize(new Dimension(24, 20));
		cboDriver.setMinimumSize(new Dimension(24, 20));
		cboDriver.setModel(new DefaultComboBoxModel<String>(new String[] {"PG-9.1+PostGIS-1.5", "PG-9.2+PostGIS-2.0"}));
		panel.add(cboDriver, "cell 3 1,growx");

		lblIp = new JLabel(BUNDLE.getString("Form.lblIp.text")); //$NON-NLS-1$
		panel.add(lblIp, "cell 1 2");

		txtIP = new JTextField();
		txtIP.setText(BUNDLE.getString("Form.textField.text")); //$NON-NLS-1$
		panel.add(txtIP, "cell 3 2,growx");
		txtIP.setColumns(10);

		lblPort = new JLabel(BUNDLE.getString("Form.lblPort.text")); //$NON-NLS-1$
		panel.add(lblPort, "cell 1 3,alignx left");

		txtPort = new JTextField();
		txtPort.setText(BUNDLE.getString("Form.txtPort.text")); //$NON-NLS-1$
		txtPort.setColumns(10);
		panel.add(txtPort, "cell 3 3,growx");

		lblDatabase = new JLabel(BUNDLE.getString("Form.lblDatabase.text")); //$NON-NLS-1$
		panel.add(lblDatabase, "cell 1 4");

		txtDatabase = new JTextField();
		txtDatabase.setText("");
		txtDatabase.setColumns(10);
		panel.add(txtDatabase, "cell 3 4,growx");

		lblUser = new JLabel(BUNDLE.getString("Form.lblUser.text")); //$NON-NLS-1$
		panel.add(lblUser, "cell 1 5");

		txtUser = new JTextField();
		txtUser.setText("postgres");
		txtUser.setColumns(10);
		panel.add(txtUser, "cell 3 5,growx");

		lblPassword = new JLabel(BUNDLE.getString("Form.lblPassword.text")); //$NON-NLS-1$
		panel.add(lblPassword, "cell 1 6");

		txtPassword = new JPasswordField();
		txtPassword.setText("");
		panel.add(txtPassword, "cell 3 6,growx");

		chkRemember = new JCheckBox(
				BUNDLE.getString("Form.chckbxNewCheckBox.text")); //$NON-NLS-1$
		chkRemember.setSelected(true);
		panel.add(chkRemember, "cell 3 7,aligny baseline");

		btnTest = new JButton(BUNDLE.getString("Form.btnTest.text")); //$NON-NLS-1$
		btnTest.setMinimumSize(new Dimension(107, 23));
		btnTest.setActionCommand(BUNDLE.getString("Form.btnTest.actionCommand")); //$NON-NLS-1$
		panel.add(btnTest, "cell 5 7,alignx right");
		// panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new
		// Component[]{lblNewLabel, comboBox, lblIp, txtIP, lblPort, txtPort,
		// lblDatabase}));

		panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.add(panel_5, "cell 1 3 2 1,grow");
		panel_5.setLayout(new MigLayout("", "[10][60][15][140][136]", "[2][25][25]"));

		lblNewLabel_1 = new JLabel(BUNDLE.getString("Form.lblNewLabel_1.text")); //$NON-NLS-1$
		panel_5.add(lblNewLabel_1, "cell 1 1");

		cboSchema1 = new JComboBox<String>();
		cboSchema1.setActionCommand(BUNDLE.getString("Form.cboSchema1.actionCommand")); //$NON-NLS-1$
		cboSchema1.setPreferredSize(new Dimension(140, 20));
		cboSchema1.setMinimumSize(new Dimension(29, 20));
		panel_5.add(cboSchema1, "cell 3 1,growx");
		
		btnCreateSchema = new JButton(BUNDLE.getString("Form.btnCreateSchema.text"));
		btnCreateSchema.setEnabled(false);
		btnCreateSchema.setPreferredSize(new Dimension(105, 23));
		btnCreateSchema.setActionCommand(BUNDLE.getString("Form.btnCreateSchema.actionCommand")); //$NON-NLS-1$
		panel_5.add(btnCreateSchema, "cell 4 1,alignx right");

		lblResultId = new JLabel(BUNDLE.getString("Form.lblResultId.text")); //$NON-NLS-1$
		panel_5.add(lblResultId, "cell 1 2");

		cboResultId = new JComboBox<String>();
		cboResultId.setActionCommand(BUNDLE.getString("Form.cboResultId.actionCommand")); //$NON-NLS-1$
		cboResultId.setMinimumSize(new Dimension(29, 20));
		cboResultId.setPreferredSize(new Dimension(24, 20));
		panel_5.add(cboResultId, "cell 3 2,growx");
		
		btnDeleteSchema = new JButton(BUNDLE.getString("Form.btnDeleteSchema.text")); //$NON-NLS-1$
		btnDeleteSchema.setEnabled(false);
		btnDeleteSchema.setMinimumSize(new Dimension(107, 23));
		btnDeleteSchema.setMaximumSize(new Dimension(107, 23));
		btnDeleteSchema.setPreferredSize(new Dimension(107, 23));
		btnDeleteSchema.setActionCommand(BUNDLE.getString("Form.btnDeleteSchema.actionCommand")); //$NON-NLS-1$
		panel_5.add(btnDeleteSchema, "cell 4 2,alignx right");

		// Panel 2
		panel_2 = new JPanel();
		tabbedPane.addTab(
				BUNDLE.getString("Form.panel_2.title"), null, panel_2, null); //$NON-NLS-1$
		panel_2.setLayout(new MigLayout("", "[10px][80][308.00px][]", "[5px][24px][24][50][15][50][5px][][5px][]"));
		
		lblEpaSoftware_1 = new JLabel(BUNDLE.getString("Form.lblEpaSoftware_1.text")); //$NON-NLS-1$
		panel_2.add(lblEpaSoftware_1, "cell 1 1,alignx left,aligny center");
		
		cboSoftwareDbf = new JComboBox<String>();
		cboSoftwareDbf.setPreferredSize(new Dimension(24, 20));
		cboSoftwareDbf.setMinimumSize(new Dimension(150, 20));
		cboSoftwareDbf.setActionCommand("schemaChanged");
		panel_2.add(cboSoftwareDbf, "cell 2 1,alignx left,aligny center");

		JLabel lblInput = new JLabel(BUNDLE.getString("Form.lblNewLabel.text")); //$NON-NLS-1$
		panel_2.add(lblInput, "cell 1 2");

		scrollPane = new JScrollPane();
		panel_2.add(scrollPane, "cell 1 3 2 1,grow");

		txtInput = new JTextArea();
		txtInput.setText("");
		txtInput.setFont(new Font("Tahoma", Font.PLAIN, 11));
		scrollPane.setViewportView(txtInput);

		btnFolderShp = new JButton();
		btnFolderShp.setText("...");
		btnFolderShp.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnFolderShp.setActionCommand(BUNDLE.getString("Form.btnFolderShp.actionCommand")); //$NON-NLS-1$
		panel_2.add(btnFolderShp, "cell 3 3,alignx right");

		JLabel lblFolderOut = new JLabel(BUNDLE.getString("Form.lblFolderOut.text")); //$NON-NLS-1$
		panel_2.add(lblFolderOut, "cell 1 4");
		
		scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, "cell 1 5 2 1,grow");

		txtExportTo = new JTextArea();
		scrollPane_1.setViewportView(txtExportTo);
		txtExportTo.setText("");
		txtExportTo.setFont(new Font("Tahoma", Font.PLAIN, 11));

		btnFolderOut = new JButton();
		btnFolderOut.setText("...");
		btnFolderOut.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnFolderOut.setActionCommand(BUNDLE.getString("Form.btnFolderOut.actionCommand")); //$NON-NLS-1$
		panel_2.add(btnFolderOut, "cell 3 5,alignx right");

		JLabel lblFileName = new JLabel(BUNDLE.getString("Form.lblFileName.text")); //$NON-NLS-1$
		panel_2.add(lblFileName, "cell 1 7,alignx trailing");

		txtFileOut = new JTextField();
		txtFileOut.setText("");
		panel_2.add(txtFileOut, "cell 2 7,growx");
		txtFileOut.setColumns(10);

		btnAccept2 = new JButton();
		btnAccept2.setText(BUNDLE.getString("Form.btnAccept.text")); //$NON-NLS-1$
		btnAccept2.setName("btn_accept_postgis");
		btnAccept2.setActionCommand(BUNDLE.getString("Form.btnAccept2.actionCommand")); //$NON-NLS-1$
		panel_2.add(btnAccept2, "flowx,cell 2 9,alignx right");

		// Panel 3
		JPanel panel_3 = new JPanel();
		panel_3.setVisible(false);
		//tabbedPane.addTab(BUNDLE.getString("Form.panel_4.title"), null, panel_3, null); //$NON-NLS-1$
		panel_3.setLayout(new MigLayout("",	"[10.00][][10.00][300.00][10.00][]", "[10][][10][45][15][]"));

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
		panel_3.add(btnFileInp3, "cell 5 3,alignx right");

		btnAccept3 = new JButton();
		btnAccept3.setText(BUNDLE.getString("Form.btnAccept.text")); //$NON-NLS-1$
		btnAccept3.setName("btn_accept_postgis");
		btnAccept3.setActionCommand("...");
		panel_3.add(btnAccept3, "cell 3 5,alignx right");

		// Panel 4
		panel_4 = new JPanel();
		tabbedPane.addTab(BUNDLE.getString("Form.panel_3.title"), null, panel_4, null); //$NON-NLS-1$
		panel_4.setLayout(new MigLayout("", "[5px][22][][320.00px][110]", "[5px][24px][24px][30px][44.00][24][44px][24][][5px][]"));

		lblEpaSoftware = new JLabel(BUNDLE.getString("Form.lblEpaSoftware.text")); //$NON-NLS-1$
		panel_4.add(lblEpaSoftware, "cell 2 1");

		cboSoftware = new JComboBox<String>();
		cboSoftware.setPreferredSize(new Dimension(24, 20));
		cboSoftware.setMinimumSize(new Dimension(150, 20));
		cboSoftware.setActionCommand(BUNDLE.getString("Form.cboSoftware.actionCommand")); //$NON-NLS-1$
		panel_4.add(cboSoftware, "cell 3 1,alignx left");

		JLabel lblSelectSchema = new JLabel(BUNDLE.getString("Form.lblSelectSchema.text")); //$NON-NLS-1$
		panel_4.add(lblSelectSchema, "cell 2 2");

		cboSchema = new JComboBox<String>();
		cboSchema.setPreferredSize(new Dimension(24, 20));
		cboSchema.setActionCommand(BUNDLE.getString("Form.cboSchema.actionCommand")); //$NON-NLS-1$
		cboSchema.setMinimumSize(new Dimension(150, 20));
		panel_4.add(cboSchema, "cell 3 2,alignx left");

		chkExport = new JCheckBox();
		chkExport.setText(BUNDLE.getString("Form.checkBox.text")); //$NON-NLS-1$
		panel_4.add(chkExport, "cell 1 3 2 1");

		btnOptions = new JButton(BUNDLE.getString("Form.btnOptions.text")); //$NON-NLS-1$
		btnOptions.setEnabled(false);
		btnOptions.setMinimumSize(new Dimension(110, 23));
		btnOptions.setPreferredSize(new Dimension(110, 23));
		btnOptions.setActionCommand(BUNDLE.getString("Form.btnOptions.actionCommand")); //$NON-NLS-1$
		panel_4.add(btnOptions, "flowx,cell 3 3");

		JLabel label = new JLabel();
		label.setText(BUNDLE.getString("Form.label.text")); //$NON-NLS-1$
		panel_4.add(label, "cell 2 4");
		
		scrollPane_2 = new JScrollPane();
		panel_4.add(scrollPane_2, "cell 3 4,grow");

		txtFileInp = new JTextArea();
		scrollPane_2.setViewportView(txtFileInp);
		txtFileInp.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtFileInp.setLineWrap(true);

		btnFileInp = new JButton();
		btnFileInp.setActionCommand(BUNDLE.getString("Form.btnFileInp.actionCommand")); //$NON-NLS-1$
		btnFileInp.setText("...");
		btnFileInp.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_4.add(btnFileInp, "cell 4 4,alignx left");

		chkExec = new JCheckBox();
		chkExec.setText(BUNDLE.getString("Form.checkBox_1.text")); //$NON-NLS-1$
		chkExec.setName("chk_exec");
		chkExec.setActionCommand("Exportaci\u00F3n a INP");
		panel_4.add(chkExec, "cell 1 5 3 1,alignx left");

		JLabel label_1 = new JLabel();
		label_1.setText(BUNDLE.getString("Form.label_1.text")); //$NON-NLS-1$
		panel_4.add(label_1, "cell 2 6");
		
		scrollPane_3 = new JScrollPane();
		panel_4.add(scrollPane_3, "cell 3 6,grow");

		txtFileRpt = new JTextArea();
		scrollPane_3.setViewportView(txtFileRpt);
		txtFileRpt.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtFileRpt.setLineWrap(true);

		btnFileRpt = new JButton();
		btnFileRpt.setActionCommand(BUNDLE.getString("Form.btnFileRpt.actionCommand")); //$NON-NLS-1$
		btnFileRpt.setText("...");
		btnFileRpt.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_4.add(btnFileRpt, "cell 4 6,alignx left");

		chkImport = new JCheckBox();
		chkImport.setText(BUNDLE.getString("Form.chkImport.text")); //$NON-NLS-1$
		chkImport.setName("chk_import");
		chkImport.setActionCommand("Exportaci\u00F3n a INP");
		panel_4.add(chkImport, "cell 1 7 2 1");

		JLabel label_2 = new JLabel();
		label_2.setText(BUNDLE.getString("Form.label_2.text")); //$NON-NLS-1$
		label_2.setName("lbl_project");
		panel_4.add(label_2, "cell 2 8");

		txtProject = new JTextField();
		txtProject.setName("txt_project");
		panel_4.add(txtProject, "cell 3 8,growx,aligny top");

		btnAccept = new JButton();
		btnAccept.setEnabled(false);
		btnAccept.setText(BUNDLE.getString("Form.btnAccept.text")); //$NON-NLS-1$
		btnAccept.setName("btn_accept_postgis");
		btnAccept.setActionCommand(BUNDLE.getString("Form.btnAccept.actionCommand")); //$NON-NLS-1$
		panel_4.add(btnAccept, "flowx,cell 3 10,alignx right");

		btnCatchSelection = new JButton(BUNDLE.getString("Form.btnCatchSelection.text")); //$NON-NLS-1$
		btnCatchSelection.setEnabled(false);
		btnCatchSelection.setMaximumSize(new Dimension(110, 23));
		btnCatchSelection.setActionCommand(BUNDLE
				.getString("Form.btnCatchSelection.actionCommand")); //$NON-NLS-1$
		panel_4.add(btnCatchSelection, "cell 3 3");

		btnRaingage = new JButton(BUNDLE.getString("Form.btnNewButton.text")); //$NON-NLS-1$
		btnRaingage.setEnabled(false);
		btnRaingage.setPreferredSize(new Dimension(110, 23));
		btnRaingage.setMaximumSize(new Dimension(110, 23));
		btnRaingage.setActionCommand(BUNDLE.getString("Form.btnRaingage.actionCommand")); //$NON-NLS-1$
		panel_4.add(btnRaingage, "cell 3 3,aligny baseline");

		// Select Database Options by default
		tabbedPane.setSelectedIndex(0);
		panel_4.setVisible(false);

		setupListeners();

	}

	
	// Setup component's listener
	private void setupListeners() {

		// Panel Database options
		cboSchema1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseController.action(e.getActionCommand());				
			}
		});

		cboResultId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseController.action(e.getActionCommand());					
			}
		});
		
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseController.action(e.getActionCommand());
			}
		});
				
		btnCreateSchema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseController.action(e.getActionCommand());				
			}
		});
		
		btnDeleteSchema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseController.action(e.getActionCommand());				
			}
		});		

		// Panel Dbf
//		panel_2.addFocusListener(new FocusAdapter() {
//			@Override
//			public void focusGained(FocusEvent arg0) {
//				setSoftwareDbf(null);
//			}
//		});
		
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
		btnAccept2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbfController.action(e.getActionCommand());
			}
		});

		// Panel INP to Database
		btnFileInp3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// controller.action(e.getActionCommand());
				Utils.showMessage("Not implemented", "", "");
			}
		});

		btnAccept3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// controller.action(e.getActionCommand());
				Utils.showMessage("Not implemented", "", "");
			}
		});

		// Panel Postgis
		cboSchema.addActionListener(this);
		btnFileInp.addActionListener(this);
		btnFileRpt.addActionListener(this);
		btnAccept.addActionListener(this);
		btnOptions.addActionListener(this);
		btnCatchSelection.addActionListener(this);
		btnRaingage.addActionListener(this);

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.action(e.getActionCommand());
	}
	
}