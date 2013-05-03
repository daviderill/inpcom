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
package com.tecnicsassociats.gvsig.inpcom.model;

import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class TableModelCatchment extends TableModelSuper {

	private static final long serialVersionUID = -3793339630551246161L;
	
	public TableModelCatchment(ResultSet results) {
		setMetadata(results);
		setResultSet(results);
		this.rs = results;
	}

	
	public void setCombos(){

		Vector<String> vector;
		TableColumnModel tcm = table.getColumnModel();		
		TableColumn column;
		
		column = tcm.getColumn(0);
		vector = MainDao.getTable("catchment", null);
		setColumnRendering(column, vector);
  
	}
	
	
	public void setValueAt(Object value, int row, int col) {

		super.setValueAt(value, row, col);
		setCombos();
		
//		System.out.println("setValueAt: " + value.toString() + " in " + row + " " + col);
//		boolean isInsert = false;
//		
//		try {
//			
//			rs.absolute(row + 1);
//			System.out.println(rs.isAfterLast());
//			if (rs.isAfterLast()){
//				isInsert = true;				
//				rs.moveToInsertRow();
//			}
//			
//			int columnType = metadata.getColumnType(col + 1);
//			if (columnType == Types.CHAR || columnType == Types.VARCHAR || columnType == Types.LONGVARCHAR) {
//				rs.updateString(col+1, (String) value);
//			}
//			else if(columnType == Types.INTEGER || columnType == Types.BIGINT || columnType == Types.SMALLINT || columnType == Types.NUMERIC)  {
//				Integer aux = Integer.parseInt(value.toString());
//				rs.updateInt(col+1, (Integer) aux);
//			}
//			else if(columnType == Types.DECIMAL || columnType == Types.DOUBLE || columnType == Types.FLOAT || columnType == Types.REAL) {
//				Double aux = Double.parseDouble(value.toString());				
//				rs.updateDouble(col+1, aux);
//		    }
//			else if(columnType == Types.TIME || columnType == Types.TIMESTAMP || columnType == Types.DATE) {
//				rs.updateTimestamp(col+1, (Timestamp) value);
//			}
//			
//			if (isInsert){			
//				rs.insertRow();
//			}
//			else{
//				rs.updateRow();				
//			}
//			
//		} catch (SQLException e) {
//			Utils.showError(e.getMessage(), "", "inp_descr");
//		} catch (ClassCastException e) {
//			Utils.showError(e.getMessage(), "", "inp_descr");
//		} catch (NumberFormatException e) {
//			Utils.showError("NumberFormatException: " + e.getMessage(), "", "inp_descr");
//		}
//		
//		setResultSet(rs);
		
	}

}