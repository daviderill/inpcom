package com.tecnicsassociats.gvsig.inpcom.gui;

import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class MyComboBoxEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 5953824239737638592L;

	public MyComboBoxEditor(String[] items) {
		super(new JComboBox<String>(items));
	}

	public MyComboBoxEditor(Vector<String> items) {
		super(new JComboBox<String>(items));
	}
}
