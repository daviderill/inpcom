package com.tecnicsassociats.gvsig.inpcom.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;

import com.tecnicsassociats.gvsig.inpcom.model.TableModelCatchment;
import com.tecnicsassociats.gvsig.inpcom.model.TableModelOptions;
import com.tecnicsassociats.gvsig.inpcom.model.TableModelRaingage;


public class TableWindow extends JPanel {
	
	private static final long serialVersionUID = 7046850563517014315L;
	private JTable table;
	private JButton btnInsert;

	private TableModelOptions tableModelOptions;
	private TableModelCatchment tableModelCatchment;
	private TableModelRaingage tableModelRaingage;
	private JButton btnDelete;

	
	public TableWindow() {
		initConfig();
	}
	
	
	public TableWindow(ResultSet rs, String tableName) {
	
		initConfig();
		
		if (tableName.equals("inp_options")){
			tableModelOptions = new TableModelOptions(rs);
			tableModelOptions.setTable(table);
			table.setModel(tableModelOptions);
			// Mostrem columnes que tenen FK amb desplegables
			tableModelOptions.setCombos();			
			btnInsert.setVisible(false);
		}
		else if(tableName.equals("catch_selection")){
			tableModelCatchment = new TableModelCatchment(rs);
			tableModelCatchment.setTable(table);
			table.setModel(tableModelCatchment);
			// Mostrem columnes que tenen FK amb desplegables
			tableModelCatchment.setCombos();			
		}
		else if(tableName.equals("raingage")){
			tableModelRaingage = new TableModelRaingage(rs);
			tableModelRaingage.setTable(table);
			table.setModel(tableModelRaingage);
			// Mostrem columnes que tenen FK amb desplegables
			tableModelRaingage.setCombos();		
			btnInsert.setVisible(false);			
		}
		
		//tableModel.addTableModelListener(tableModel);
		//MyTableModelListener lis = new MyTableModelListener(tableOptions, rs);
		//tableOptions.getModel().addTableModelListener(lis);
		
	}

	
	private void initConfig(){
		
		setLayout(new MigLayout("", "[10px][400px:600px:850px,grow][12]", "[25.00][12px][:130px:200px][12px][]"));
		
		JLabel lblTable = new JLabel("Table content");
		lblTable.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(lblTable, "cell 1 0,alignx center");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPane, "cell 1 2,grow");
		
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 10));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setRowSelectionAllowed(true);
		scrollPane.setViewportView(table);
		
		btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insert();
			}
		});
		add(btnInsert, "flowx,cell 1 4,alignx left");
		
		btnDelete = new JButton("Delete");
		btnDelete.setVisible(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				delete();
			}
		});
		add(btnDelete, "cell 1 4");
		
	}


	private void insert() {
		tableModelCatchment.insertEmptyRow();	
		tableModelCatchment.setCombos();
	}
	
	private void delete(){
		int rowIndex = table.getSelectedRow();
		tableModelCatchment.deleteRow(rowIndex);
	}

}

