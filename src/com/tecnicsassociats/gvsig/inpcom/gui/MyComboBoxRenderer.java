package com.tecnicsassociats.gvsig.inpcom.gui;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MyComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {

	private static final long serialVersionUID = -8125519578870063775L;

	public MyComboBoxRenderer(String[] items) {
		super(items);
	}

	public MyComboBoxRenderer(Vector<String> items) {
		super(items);
	}
	
	public MyComboBoxRenderer() {
		super();
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		setSelectedItem(value);
		return this;
	}

}