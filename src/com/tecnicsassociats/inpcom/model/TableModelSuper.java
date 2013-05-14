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
package com.tecnicsassociats.inpcom.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.tecnicsassociats.inpcom.gui.MyComboBoxEditor;
import com.tecnicsassociats.inpcom.gui.MyComboBoxRenderer;
import com.tecnicsassociats.inpcom.util.Utils;


public class TableModelSuper extends AbstractTableModel {

	private static final long serialVersionUID = -3793339630551246161L;
	protected String[] columnNames = new String[0];
	protected Vector<String[]> rows_data = new Vector<String[]>();
	protected ResultSet rs;
	protected ResultSetMetaData metadata; // Additional information about the results
	protected int columns;
	protected JTable table;

	
	public TableModelSuper(){
		
	}
	
	public TableModelSuper(ResultSet results) {
		setMetadata(results);
		setResultSet(results);
		this.rs = results;
	}

	
	protected void setMetadata(ResultSet results){

		try {
			metadata = results.getMetaData();
			columns = metadata.getColumnCount();
			columnNames = new String[columns];
			for (int i = 0; i < columns; i++) {
				columnNames[i] = metadata.getColumnLabel(i + 1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}

	
	public void setResultSet(ResultSet results) {
		
		try {
			rows_data.clear();
			String[] rowData;
			results.absolute(0);
			while (results.next()) {
				rowData = new String[columns];
				for (int i = 0; i < columns; i++){
					rowData[i] = results.getString(i + 1);
				}
				rows_data.addElement(rowData);
			}
			this.fireTableChanged(null);
		} catch (SQLException se) {
			se.printStackTrace();
		}

	}


	public void setTable(JTable tableOptions) {
		this.table = tableOptions;
		
	}

	
	public void insertEmptyRow() {
		String[] rowData = new String[columnNames.length];
		for (int i = 0; i < columnNames.length; i++){
			rowData[i] = "";
		}
		rows_data.addElement(rowData);
		this.fireTableChanged(null);
	}
	
	
	protected void setColumnRendering(TableColumn column, Vector<String> vector){
	    column.setCellEditor(new MyComboBoxEditor(vector));
	    column.setCellRenderer(new MyComboBoxRenderer(vector));	
	}
	
	
	public void deleteRow(int row) {
		try {
			rs.absolute(row + 1);
			rs.deleteRow();
		} catch (SQLException e) {
			Utils.showError(e.getMessage(), "", "inp_descr");
		}
	}
	
	
	public void setValueAt(Object value, int row, int col) {

		System.out.println("setValueAt: " + value.toString() + " in " + row + " " + col);
		boolean isInsert = false;
		
		try {
			
			rs.absolute(row + 1);
			System.out.println(rs.isAfterLast());
			if (rs.isAfterLast()){
				isInsert = true;				
				rs.moveToInsertRow();
			}
			
			int columnType = metadata.getColumnType(col + 1);
			if (columnType == Types.CHAR || columnType == Types.VARCHAR || columnType == Types.LONGVARCHAR) {
				rs.updateString(col+1, (String) value);
			}
			else if(columnType == Types.INTEGER || columnType == Types.BIGINT || columnType == Types.SMALLINT || columnType == Types.NUMERIC)  {
				Integer aux = Integer.parseInt(value.toString());
				rs.updateInt(col+1, (Integer) aux);
			}
			else if(columnType == Types.DECIMAL || columnType == Types.DOUBLE || columnType == Types.FLOAT || columnType == Types.REAL) {
				Double aux = Double.parseDouble(value.toString());				
				rs.updateDouble(col+1, aux);
		    }
			else if(columnType == Types.TIME || columnType == Types.TIMESTAMP || columnType == Types.DATE) {
				rs.updateTimestamp(col+1, (Timestamp) value);
			}
			
			if (isInsert){			
				rs.insertRow();
			}
			else{
				rs.updateRow();				
			}
			
		} catch (SQLException e) {
			Utils.showError(e.getMessage(), "", "inp_descr");
		} catch (ClassCastException e) {
			Utils.showError(e.getMessage(), "", "inp_descr");
		} catch (NumberFormatException e) {
			Utils.showError("NumberFormatException: " + e.getMessage(), "", "inp_descr");
		}
		
		setResultSet(rs);
		
	}
	
	
	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return rows_data == null ? 0 : rows_data.size();
	}

	public String getValueAt(int row, int column) {
		return rows_data.elementAt(row)[column];
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public boolean isCellEditable(int row, int column) {
		return true;
	}
	
}