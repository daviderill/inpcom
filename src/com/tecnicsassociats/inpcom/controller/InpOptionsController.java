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
package com.tecnicsassociats.inpcom.controller;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.tecnicsassociats.inpcom.gui.OptionsDialog;
import com.tecnicsassociats.inpcom.model.MainDao;
import com.tecnicsassociats.inpcom.util.Utils;


public class InpOptionsController {

	private OptionsDialog view;
    private ResultSet rs;
	
	
	public InpOptionsController(OptionsDialog dialog, ResultSet rs) {
		this.view = dialog;
        this.rs = rs;
	    view.setControl(this);        
	}
	
	
	// Update ComboBox items and selected item
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setComponents(){

		try {
			rs.first();
			HashMap<String, JComboBox> map = view.componentMap; 
			for (Map.Entry<String, JComboBox> entry : map.entrySet()) {
			    String key = entry.getKey();
			    JComboBox combo = entry.getValue();
				String value = rs.getString(key);
				view.setComboModel(combo, getComboValues(key));
				view.setComboSelectedItem(combo, value);
			}
			HashMap<String, JTextField> textMap = view.textMap; 
			for (Map.Entry<String, JTextField> entry : textMap.entrySet()) {
			    String key = entry.getKey();
			    JTextField component = entry.getValue();
			    Object value = rs.getObject(key);
				view.setTextField(component, value);
			}			
		} catch (SQLException e) {
			Utils.showError(e);
		}
		
	}
	

	// Get ComboBox items from Database
	public Vector<String> getComboValues(String comboName) {

		Vector<String> values = null;
		String tableName = "";
		if (comboName.equals("flow_units")){
			tableName = "inp_value_options_fu";
		}
		else if (comboName.equals("infiltration")){
			tableName = "inp_value_options_in";
		}
		else if (comboName.equals("force_main_equation")){
			tableName = "inp_value_options_fme";
		}
		else if (comboName.equals("flow_routing")){
			tableName = "inp_value_options_fr";
		}
		else if (comboName.equals("inertial_damping")){
			tableName = "inp_value_options_id";
		}
		else if (comboName.equals("link_offsets")){
			tableName = "inp_value_options_lo";
		}
		else if (comboName.equals("normal_flow_limited")){
			tableName = "inp_value_options_nfl";
		}
		else{
			tableName = "inp_value_yesno";
		}				
		values = MainDao.getTable(tableName, null);
		
		return values;
		
	}


	// Update Database table
	@SuppressWarnings("rawtypes")
	public void saveData() {

		String key;
		Object value;
		try {
			ResultSetMetaData metadata = rs.getMetaData();					
			HashMap<String, JComboBox> map = view.componentMap; 			
			for (Map.Entry<String, JComboBox> entry : map.entrySet()) {
				key = entry.getKey();
				JComboBox combo = entry.getValue();
				value = combo.getSelectedItem();
				if (value == null || ((String)value).trim().equals("")){
					rs.updateNull(key);						
				} else{
					rs.updateString(key, (String) value);
				}				
			}
			HashMap<String, JTextField> textMap = view.textMap; 			
			for (Map.Entry<String, JTextField> entry : textMap.entrySet()) {
				key = entry.getKey();
				JTextField component = entry.getValue();
				value = component.getText();
				int col = rs.findColumn(key);
				int columnType = metadata.getColumnType(col);
				if (columnType == Types.CHAR || columnType == Types.VARCHAR || columnType == Types.LONGVARCHAR) {
					if (value == null || ((String)value).trim().equals("")){
						rs.updateNull(col);						
					} else{
						rs.updateString(col, (String) value);
					}
				}
				else if(columnType == Types.INTEGER || columnType == Types.BIGINT || columnType == Types.SMALLINT || columnType == Types.NUMERIC) {
					if (((String)value).trim().equals("")){
						rs.updateNull(col);
					} else{					
						Integer aux = Integer.parseInt(value.toString());
						rs.updateInt(col, aux);						
					}
				}
				else if(columnType == Types.DECIMAL || columnType == Types.DOUBLE || columnType == Types.FLOAT || columnType == Types.REAL) {
					Double aux = Double.parseDouble(value.toString());				
					rs.updateDouble(col, aux);
			    }
				else if(columnType == Types.TIME || columnType == Types.TIMESTAMP || columnType == Types.DATE) {
					rs.updateTimestamp(col, (Timestamp) value);
				}				
			}		
			rs.updateRow();		
		} catch (SQLException e) {
			Utils.showError(e);
		} catch (Exception e) {
			Utils.showError(e);
			return;
		}
		
	}	
	
}