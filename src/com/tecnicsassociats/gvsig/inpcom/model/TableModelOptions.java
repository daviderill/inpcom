package com.tecnicsassociats.gvsig.inpcom.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.tecnicsassociats.gvsig.inpcom.util.Utils;


public class TableModelOptions extends TableModelSuper {

	private static final long serialVersionUID = -3793339630551246161L;
	
	public TableModelOptions(ResultSet results) {
		setMetadata(results);
		setResultSet(results);
		this.rs = results;
	}

	
	public void setCombos(){

		Vector<String> vector;
		TableColumnModel tcm = table.getColumnModel();		
		TableColumn column;
		
		// Yes&No columns: 1,5,6,7,8,9
		List<Integer> listYesNo = new ArrayList<Integer>();
		listYesNo.add(5);
		listYesNo.add(6);
		listYesNo.add(7);
		listYesNo.add(8);
		listYesNo.add(9);
		listYesNo.add(10);		
		for (int i = 0; i < listYesNo.size(); i++) {
			column = tcm.getColumn(listYesNo.get(i));
			vector = MainDao.getTable("inp_value_yesno", null);			
			setColumnRendering(column, vector);
		}

		column = tcm.getColumn(0);
		vector = MainDao.getTable("inp_value_options_fu", null);
		setColumnRendering(column, vector);

		column = tcm.getColumn(1);
		vector = MainDao.getTable("inp_value_options_in", null);
		setColumnRendering(column, vector);
	    
		column = tcm.getColumn(2);
		vector = MainDao.getTable("inp_value_options_fr", null);
		setColumnRendering(column, vector);

		column = tcm.getColumn(3);
		vector = MainDao.getTable("inp_value_options_lo", null);
		setColumnRendering(column, vector);

		column = tcm.getColumn(4);
		vector = MainDao.getTable("inp_value_options_fme", null);
		setColumnRendering(column, vector);

		column = tcm.getColumn(26);
		vector = MainDao.getTable("inp_value_options_id", null);
		setColumnRendering(column, vector);

		column = tcm.getColumn(27);
		vector = MainDao.getTable("inp_value_options_nfl", null);
		setColumnRendering(column, vector);
	
  
	}
	
	
	public void setValueAt(Object value, int row, int col) {

		System.out.println("setValueAt: " + value.toString() + " in " + row + " " + col);
		
		try {
			rs.absolute(row + 1);
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
			rs.updateRow();		
		} catch (SQLException e) {
			Utils.showError(e.getMessage(), "", "inp_descr");
		} catch (ClassCastException e) {
			Utils.showError(e.getMessage(), "", "inp_descr");
		} catch (NumberFormatException e) {
			Utils.showError("NumberFormatException: " + e.getMessage(), "", "inp_descr");
		}
		
		setResultSet(rs);
		setCombos();
		
	}


}
