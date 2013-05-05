package com.tecnicsassociats.gvsig.inpcom.gui;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import com.tecnicsassociats.gvsig.inpcom.controller.InpOptionsController;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OptionsDialog extends JDialog {

	private static final long serialVersionUID = -6349825417550216902L;
	private InpOptionsController controller;
	public HashMap<String, JComboBox> componentMap;
	public HashMap<String, JTextField> textMap;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_16;
	private JTextField textField_18;
	
	public OptionsDialog() {
		initConfig();
		createComponentMap();
	}
	
	
	public void setControl(InpOptionsController inpOptionsController) {
		this.controller = inpOptionsController;
	}		

	
	public void setTextField(JTextField textField, Object value) {
		if (value!=null){
			//JTextField component = (JTextField) getComponentByName(textName);
			textField.setText(value.toString());
		}
	}	
	
	
	public void setComboModel(JComboBox combo, Vector<String> items) {
		if (items != null){
			ComboBoxModel<String> cbm = new DefaultComboBoxModel<String>(items);
			//JComboBox combo = (JComboBox) getComponentByName(comboName);
			combo.setModel(cbm);
		}
	}	
	
	
	private Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
        	return (Component) componentMap.get(name);
        }
        else if (textMap.containsKey(name)) {
        	return (Component) textMap.get(name);
        }        
        else return null;
	}	
	
	
	public void setComboSelectedItem(JComboBox combo, String item){
		combo.setSelectedItem(item);
	}
	
	
	private void createComponentMap() {
		
        componentMap = new HashMap<String, JComboBox>();
        textMap = new HashMap<String, JTextField>();
        Component[] components = getContentPane().getComponents();
        for (int i=0; i < components.length; i++) {
			if (components[i] instanceof JComboBox) {         	
				//System.out.println("ComboBox: " + components[i].getName());
				componentMap.put(components[i].getName(), (JComboBox) components[i]);
			}
			else if (components[i] instanceof JTextField) {      
				//System.out.println("TextField: " + components[i].getName());				
				textMap.put(components[i].getName(), (JTextField) components[i]);
			}			
        }
        
	}

	

	
	
	private void initConfig(){

		setTitle("Options Table");
		setBounds(100, 100, 682, 515);
		getContentPane().setLayout(new MigLayout("", "[5px][][200.00][10px][][200px]", "[10px][][][][][][][][5px][][][][][10px][][][][][][5px][]"));

		JLabel lblFlowUnits = new JLabel("Flow Units:");
		getContentPane().add(lblFlowUnits, "cell 1 1,alignx trailing");

		JComboBox flow_units = new JComboBox();
		flow_units.setName("flow_units");
		getContentPane().add(flow_units, "cell 2 1,growx");
		
		JLabel lblIgnoreRainfall = new JLabel("Ignore Rainfall:");
		getContentPane().add(lblIgnoreRainfall, "cell 4 1,alignx trailing");
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setName("ignore_rainfall");
		getContentPane().add(comboBox_2, "cell 5 1,growx");

		JLabel lblInfiltration = new JLabel("Infiltration:");
		getContentPane().add(lblInfiltration, "cell 1 2,alignx trailing");

		JComboBox infiltration = new JComboBox();
		infiltration.setName("infiltration");
		getContentPane().add(infiltration, "cell 2 2,growx");
		
		JLabel lblIgnoreSnowmelt = new JLabel("Ignore Snowmelt:");
		getContentPane().add(lblIgnoreSnowmelt, "cell 4 2,alignx trailing");
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setName("ignore_snowmelt");
		getContentPane().add(comboBox_3, "cell 5 2,growx");

		JLabel lblFlowRouting = new JLabel("Flow Routing:");
		getContentPane().add(lblFlowRouting, "cell 1 3,alignx trailing");

		JComboBox flow_routing = new JComboBox();
		flow_routing.setName("flow_routing");
		getContentPane().add(flow_routing, "cell 2 3,growx");
		
		JLabel lblIgnoreGroundwater = new JLabel("Ignore Groundwater:");
		getContentPane().add(lblIgnoreGroundwater, "cell 4 3,alignx trailing");
		
		JComboBox comboBox_4 = new JComboBox();
		comboBox_4.setName("ignore_groundwater");
		getContentPane().add(comboBox_4, "cell 5 3,growx");
		
		JLabel lblLinkOffsets = new JLabel("Link Offsets");
		lblLinkOffsets.setName("link_offsets");
		getContentPane().add(lblLinkOffsets, "cell 1 4,alignx trailing");
		
		JComboBox comboBox = new JComboBox();
		comboBox.setName("link_offsets");
		getContentPane().add(comboBox, "cell 2 4,growx");
		
		JLabel lblIgnoreRouting = new JLabel("Ignore Routing:");
		getContentPane().add(lblIgnoreRouting, "cell 4 4,alignx trailing");
		
		JComboBox comboBox_5 = new JComboBox();
		comboBox_5.setName("ignore_routing");
		getContentPane().add(comboBox_5, "cell 5 4,growx");
		
		JLabel lblNewLabel = new JLabel("Force Main Equation");
		getContentPane().add(lblNewLabel, "cell 1 5,alignx trailing");
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setName("force_main_equation");
		getContentPane().add(comboBox_1, "cell 2 5,growx");
		
		JLabel lblIgnoreQuality = new JLabel("Ignore Quality:");
		getContentPane().add(lblIgnoreQuality, "cell 4 5,alignx trailing");
		
		JComboBox comboBox_6 = new JComboBox();
		comboBox_6.setName("ignore_quality");
		getContentPane().add(comboBox_6, "cell 5 5,growx");
		
		JLabel label = new JLabel("Dry days:");
		label.setName("");
		getContentPane().add(label, "cell 1 6,alignx trailing");
		
		textField_9 = new JTextField();
		textField_9.setName("dry_days");
		textField_9.setColumns(10);
		getContentPane().add(textField_9, "cell 2 6,growx");
		
		JLabel lblNewLabel_1 = new JLabel("Skip Steady State:");
		getContentPane().add(lblNewLabel_1, "cell 4 6,alignx trailing");
		
		JComboBox comboBox_7 = new JComboBox();
		comboBox_7.setName("skip_steady_state");
		getContentPane().add(comboBox_7, "cell 5 6,growx,aligny top");
		
		JLabel lblAllowPonding = new JLabel("Allow Ponding:");
		getContentPane().add(lblAllowPonding, "cell 4 7,alignx trailing");
		
		JComboBox comboBox_10 = new JComboBox();
		comboBox_10.setName("allow_ponding");
		getContentPane().add(comboBox_10, "cell 5 7,growx");
		
		JLabel lblStartDate = new JLabel("Start date:");
		getContentPane().add(lblStartDate, "cell 1 9,alignx trailing");
		
		textField = new JTextField();
		textField.setName("start_date");
		getContentPane().add(textField, "cell 2 9,growx");
		textField.setColumns(10);
		
		JLabel lblStartTime = new JLabel("Start time:");
		getContentPane().add(lblStartTime, "cell 4 9,alignx trailing");
		
		textField_2 = new JTextField();
		textField_2.setName("start_time");
		textField_2.setColumns(10);
		getContentPane().add(textField_2, "cell 5 9,growx");
		
		JLabel lblEndDate = new JLabel("End date:");
		getContentPane().add(lblEndDate, "cell 1 10,alignx trailing");
		
		textField_1 = new JTextField();
		textField_1.setName("end_date");
		textField_1.setColumns(10);
		getContentPane().add(textField_1, "cell 2 10,growx");
		
		JLabel lblEndTime = new JLabel("End time:");
		getContentPane().add(lblEndTime, "cell 4 10,alignx trailing");
		
		textField_3 = new JTextField();
		textField_3.setName("end_time");
		textField_3.setColumns(10);
		getContentPane().add(textField_3, "cell 5 10,growx");
		
		JLabel lblReportStartDate = new JLabel("Report Start Date:");
		getContentPane().add(lblReportStartDate, "cell 1 11,alignx trailing");
		
		textField_4 = new JTextField();
		textField_4.setName("report_start_date");
		textField_4.setColumns(10);
		getContentPane().add(textField_4, "cell 2 11,growx");
		
		JLabel lblReportStartTime = new JLabel("Report Start Time:");
		getContentPane().add(lblReportStartTime, "cell 4 11,alignx trailing");
		
		textField_5 = new JTextField();
		textField_5.setName("report_start_time");
		textField_5.setColumns(10);
		getContentPane().add(textField_5, "cell 5 11,growx");
		
		JLabel lblSweepstart = new JLabel("Sweep Start:");
		lblSweepstart.setName("");
		getContentPane().add(lblSweepstart, "cell 1 12,alignx trailing");
		
		textField_6 = new JTextField();
		textField_6.setName("sweep_start");
		textField_6.setColumns(10);
		getContentPane().add(textField_6, "cell 2 12,growx");
		
		JLabel lblSweepEnd = new JLabel("Sweep End:");
		lblSweepEnd.setName("sweep_start");
		getContentPane().add(lblSweepEnd, "cell 4 12,alignx trailing");
		
		textField_7 = new JTextField();
		textField_7.setName("sweep_end");
		textField_7.setColumns(10);
		getContentPane().add(textField_7, "cell 5 12,growx");
		
		JLabel lblDryDays = new JLabel("Report Step:");
		lblDryDays.setName("");
		getContentPane().add(lblDryDays, "cell 1 14,alignx trailing");
		
		textField_8 = new JTextField();
		textField_8.setName("report_step");
		textField_8.setColumns(10);
		getContentPane().add(textField_8, "cell 2 14,growx");
		
		JLabel lblWetSteps = new JLabel("Wet Step:");
		lblWetSteps.setName("");
		getContentPane().add(lblWetSteps, "cell 4 14,alignx trailing");
		
		textField_12 = new JTextField();
		textField_12.setName("wet_step");
		textField_12.setColumns(10);
		getContentPane().add(textField_12, "cell 5 14,growx");
		
		JLabel lblDrySteps = new JLabel("Dry Step:");
		lblDrySteps.setName("dry_step");
		getContentPane().add(lblDrySteps, "cell 1 15,alignx trailing");
		
		textField_10 = new JTextField();
		textField_10.setName("dry_step");
		textField_10.setColumns(10);
		getContentPane().add(textField_10, "cell 2 15,growx");
		
		JLabel lblRoutingSteps = new JLabel("Routing Step:");
		lblRoutingSteps.setName("");
		getContentPane().add(lblRoutingSteps, "cell 4 15,alignx trailing");
		
		textField_13 = new JTextField();
		textField_13.setName("routing_step");
		textField_13.setColumns(10);
		getContentPane().add(textField_13, "cell 5 15,growx");
		
		JLabel lblLengtheningSteps = new JLabel("Lengthening Step:");
		lblLengtheningSteps.setName("");
		getContentPane().add(lblLengtheningSteps, "cell 1 16,alignx trailing");
		
		textField_11 = new JTextField();
		textField_11.setName("lengthening_step");
		textField_11.setColumns(10);
		getContentPane().add(textField_11, "cell 2 16,growx");
		
		JLabel lblVariableSteps = new JLabel("Variable Step:");
		lblVariableSteps.setName("");
		getContentPane().add(lblVariableSteps, "cell 4 16,alignx trailing");
		
		textField_14 = new JTextField();
		textField_14.setName("variable_step");
		textField_14.setColumns(10);
		getContentPane().add(textField_14, "cell 5 16,growx");
		
		JLabel lblInertialdamping = new JLabel("Inertial Damping:");
		lblInertialdamping.setName("");
		getContentPane().add(lblInertialdamping, "cell 1 17,alignx trailing");
		
		JComboBox comboBox_8 = new JComboBox();
		comboBox_8.setName("inertial_damping");
		getContentPane().add(comboBox_8, "cell 2 17,growx");
		
		JLabel lblNormalflowlimited = new JLabel("Normal Flow Limited");
		lblNormalflowlimited.setName("");
		getContentPane().add(lblNormalflowlimited, "cell 4 17,alignx trailing");
		
		JComboBox comboBox_9 = new JComboBox();
		comboBox_9.setName("normal_flow_limited");
		getContentPane().add(comboBox_9, "cell 5 17,growx");
		
		JLabel lblMinSurfarea = new JLabel("Min Surfarea:");
		lblMinSurfarea.setName("");
		getContentPane().add(lblMinSurfarea, "cell 1 18,alignx trailing");
		
		textField_16 = new JTextField();
		textField_16.setName("min_surfarea");
		textField_16.setColumns(10);
		getContentPane().add(textField_16, "cell 2 18,growx");
		
		JLabel lblMinSlope = new JLabel("Min Slope:");
		lblMinSlope.setName("");
		getContentPane().add(lblMinSlope, "cell 4 18,alignx trailing");
		
		textField_18 = new JTextField();
		textField_18.setName("min_slope");
		textField_18.setColumns(10);
		getContentPane().add(textField_18, "cell 5 18,growx");
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.saveData();
			}
		});
		getContentPane().add(btnSave, "cell 5 20,alignx right");
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				System.out.println("jdialog window closed event received");
			}
			public void windowClosing(WindowEvent e){
				//controller.saveData();
			}
		});		
		
	}


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			OptionsDialog dialog = new OptionsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
