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
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.tecnicsassociats.gvsig.inpcom.controller.RaingageController;


@SuppressWarnings("rawtypes")
public class RaingageDialog extends JDialog {

	private static final long serialVersionUID = -6349825417550216902L;
	private RaingageController controller;
	public HashMap<String, JComboBox> componentMap;
	public HashMap<String, JTextField> textMap;
	private JTextField textField_18;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	
	public RaingageDialog() {
		initConfig();
		createComponentMap();
	}
	
	
	public void setControl(RaingageController controller) {
		this.controller = controller;
	}		

	
	public void setTextField(JTextField textField, Object value) {
		if (value!=null){
			textField.setText(value.toString());
		}
	}	
	
	
	public void setComboModel(JComboBox<String> combo, Vector<String> items) {
		if (items != null){
			ComboBoxModel<String> cbm = new DefaultComboBoxModel<String>(items);
			combo.setModel(cbm);
		}
	}	
	
	
	public void setComboSelectedItem(JComboBox combo, String item){
		combo.setSelectedItem(item);
	}
	
	
	private void createComponentMap() {
		
        componentMap = new HashMap<String, JComboBox>();
        textMap = new HashMap<String, JTextField>();
        // TODO: Get panelGeneral
        Component[] components = getContentPane().getComponents();
        for (int i=0; i < components.length; i++) {
			if (components[i] instanceof JComboBox) {         	
				componentMap.put(components[i].getName(), (JComboBox) components[i]);
			}
			else if (components[i] instanceof JTextField) {      
				textMap.put(components[i].getName(), (JTextField) components[i]);
			}			
        }
        
	}


	private void initConfig(){

		setTitle("Options Table");
		setBounds(100, 100, 633, 246);
		getContentPane().setLayout(new MigLayout("", "[90.00][200px]", "[][15px][36.00]"));
		
		JPanel panelGeneral = new JPanel();
		panelGeneral.setFont(new Font("Tahoma", Font.BOLD, 14));
		panelGeneral.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "GENERAL", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		getContentPane().add(panelGeneral, "cell 0 0 2 1,grow");
		panelGeneral.setLayout(new MigLayout("", "[75.00][200.00,grow][10px][80px][200px,grow]", "[][][][][][]"));

		JLabel lblFlowUnits = new JLabel("Flow type:");
		panelGeneral.add(lblFlowUnits, "cell 0 0,alignx trailing");

		JComboBox flow_units = new JComboBox();
		panelGeneral.add(flow_units, "cell 1 0,growx");
		flow_units.setName("flow_type");
		
		JLabel lblNewLabel_1 = new JLabel("Intvl:");
		panelGeneral.add(lblNewLabel_1, "cell 3 0,alignx trailing");
		
		textField_1 = new JTextField();
		textField_1.setName("intvl");
		textField_1.setColumns(10);
		panelGeneral.add(textField_1, "cell 4 0,growx");

		JLabel lblInfiltration = new JLabel("Scf:");
		panelGeneral.add(lblInfiltration, "cell 0 1,alignx trailing");
		
		textField_2 = new JTextField();
		textField_2.setName("scf");
		textField_2.setColumns(10);
		panelGeneral.add(textField_2, "cell 1 1,growx");
		
		JLabel lblIgnoreSnowmelt = new JLabel("Raingage Type:");
		panelGeneral.add(lblIgnoreSnowmelt, "cell 3 1,alignx trailing");
		
		JComboBox comboBox_3 = new JComboBox();
		panelGeneral.add(comboBox_3, "cell 4 1,growx");
		comboBox_3.setName("rgage_type");

		JLabel lblFlowRouting = new JLabel("Timeseries id:");
		panelGeneral.add(lblFlowRouting, "cell 0 2,alignx trailing");

		JComboBox flow_routing = new JComboBox();
		panelGeneral.add(flow_routing, "cell 1 2,growx");
		flow_routing.setName("timser_id");
		
		JLabel lblIgnoreGroundwater = new JLabel("Fname:");
		panelGeneral.add(lblIgnoreGroundwater, "cell 3 2,alignx trailing");
		
		textField_3 = new JTextField();
		textField_3.setName("fname");
		textField_3.setColumns(10);
		panelGeneral.add(textField_3, "cell 4 2,growx");
		
		JLabel lblMinSlope = new JLabel("Sta:");
		panelGeneral.add(lblMinSlope, "cell 0 3,alignx trailing");
		lblMinSlope.setName("");
		
		textField_18 = new JTextField();
		panelGeneral.add(textField_18, "cell 1 3,growx");
		textField_18.setName("sta");
		textField_18.setColumns(10);
		
		JLabel lblIgnoreRouting = new JLabel("Units:");
		panelGeneral.add(lblIgnoreRouting, "cell 3 3,alignx trailing");
		
		textField_4 = new JTextField();
		textField_4.setName("units");
		textField_4.setColumns(10);
		panelGeneral.add(textField_4, "cell 4 3,growx");
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.saveData();
			}
		});
		getContentPane().add(btnSave, "cell 1 2,alignx right");
		
	}


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RaingageDialog dialog = new RaingageDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}