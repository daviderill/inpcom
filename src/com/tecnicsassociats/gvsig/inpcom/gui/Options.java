package com.tecnicsassociats.gvsig.inpcom.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

//import com.tecnicsassociats.gvsig.inpcom.controller.MyTableModelListener;
import com.tecnicsassociats.gvsig.inpcom.model.MyModel;
import com.tecnicsassociats.gvsig.inpcom.model.ResultSetTableModel;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;


public class Options extends JPanel {
	
	private static final long serialVersionUID = 7046850563517014315L;
	private JTable tableOptions;
	private ResultSetTableModel tableModel;
	private MyModel myModel;	
	private ResultSet rs;
	private JButton btnInsert;

	
	public Options() {
		initConfig();
	}
	
	
	public Options(ResultSet rs) {
	
		initConfig();
		this.rs = rs;
		//tableModel = new ResultSetTableModel(rs);
		//tableOptions.setModel(tableModel);
		myModel = new MyModel(rs);
		tableOptions.setModel(myModel);
		//insert();			
		//tableModel.addTableModelListener(tableModel);
		//MyTableModelListener lis = new MyTableModelListener(tableOptions, rs);
		//tableOptions.getModel().addTableModelListener(lis);

	}
	
	
	private void initConfig(){
		
		setLayout(new MigLayout("", "[12px][600px:800px:800px][12]", "[42.00][12px][:150px:200px][12px][]"));
		
		JLabel lblOptionsTable = new JLabel("Options table");
		lblOptionsTable.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(lblOptionsTable, "cell 1 0,alignx center");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPane, "cell 1 2,grow");
		
		tableOptions = new JTable();
		tableOptions.setFont(new Font("Tahoma", Font.PLAIN, 10));
		tableOptions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableOptions.setColumnSelectionAllowed(true);
		tableOptions.setCellSelectionEnabled(true);
		tableOptions.setRowSelectionAllowed(true);
		scrollPane.setViewportView(tableOptions);
		
		btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insert();
			}
		});
		add(btnInsert, "cell 1 4,alignx right");
		
	}


	protected void insert() {
		
		System.out.println("insert");

		//myModel.insertEmptyRow();	
		myModel.insertFillRow(90, "sname");
		myModel.saveNewRecord();
		
		//DefaultTableModel model = (DefaultTableModel) tableOptions.getModel();
		//model.addRow(new Object[]{"Column 1", "Column 2", "Column 3"});
		//Object[] o = new Object[]{20, "Column 2"};
		//tableModel.addRow(o);
//		try {
//			rs.moveToInsertRow();
//		    rs.updateInt("id", 60);
//		    rs.updateString("name", "Harrington");			
//			rs.insertRow();
//			rs.moveToCurrentRow();
//			rs.updateRow();
//			rs.close();
			//tableModel.fireTableCellUpdated(x, 0); // Repaint one cell.
//			tableModel.setValueAt(60, 3, 0);
//			tableModel.setValueAt("SS", 3, 1);
//			tableModel.setResultset(rs);
//			tableModel.fireTableDataChanged();
//			tableOptions.setModel(tableModel);
//			tableOptions.updateUI();
//			//tableModel.insertRow(row, rowData)
//			tableOptions.repaint();
//		} catch (SQLException e) {
//			Utils.showError(e.getMessage(), "", "inp_descr");
//		}
		
	}

}
