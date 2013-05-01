package com.tecnicsassociats.gvsig.inpcom.model;

import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class TableModelRaingage extends TableModelSuper {

	private static final long serialVersionUID = -3793339630551246161L;
	
	public TableModelRaingage(ResultSet results) {
		setMetadata(results);
		setResultSet(results);
		this.rs = results;
	}

	
	public void setCombos(){

		Vector<String> vector;
		TableColumnModel tcm = table.getColumnModel();		
		TableColumn column;
		
		column = tcm.getColumn(1);
		vector = MainDao.getTable("inp_value_raingage", null);
		setColumnRendering(column, vector);

		column = tcm.getColumn(4);
		vector = MainDao.getTable("inp_typevalue_raingage", null);
		setColumnRendering(column, vector);
		
		column = tcm.getColumn(5);
		vector = MainDao.getTable("inp_timser_id", null);
		setColumnRendering(column, vector);
		
	}
	
	
}