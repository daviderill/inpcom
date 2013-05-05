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
import com.tecnicsassociats.gvsig.inpcom.model.TableModelRaingage;


public class TableWindow extends JPanel {
	
	private static final long serialVersionUID = 7046850563517014315L;
	private JTable table;
	private JButton btnInsert;

	private TableModelCatchment tableModelCatchment;
	private TableModelRaingage tableModelRaingage;
	private JButton btnDelete;

	
	public TableWindow() {
		initConfig();
	}
	
	
	public TableWindow(ResultSet rs, String tableName) {
	
		initConfig();
		
		if(tableName.equals("catch_selection")){
			tableModelCatchment = new TableModelCatchment(rs);
			tableModelCatchment.setTable(table);
			table.setModel(tableModelCatchment);
			tableModelCatchment.setCombos();
			btnInsert.setVisible(true);
			btnDelete.setVisible(true);			
		}
		else if(tableName.equals("raingage")){
			tableModelRaingage = new TableModelRaingage(rs);
			tableModelRaingage.setTable(table);
			table.setModel(tableModelRaingage);
			tableModelRaingage.setCombos();		
			btnInsert.setVisible(false);			
			btnDelete.setVisible(false);	
		}
		
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

