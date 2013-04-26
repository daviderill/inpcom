///*
// * This file is part of INPcom
// * Copyright (C) 2012  Tecnics Associats
// * 
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// * 
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// * 
// * Author:
// *   David Erill <daviderill79@gmail.com>
// */
//
//package com.tecnicsassociats.gvsig.inpcom.gui;
//
//import java.awt.BorderLayout;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//import com.iver.andami.PluginServices;
//import com.iver.andami.ui.mdiManager.SingletonWindow;
//import com.iver.andami.ui.mdiManager.WindowInfo;
//import com.jeta.forms.components.panel.FormPanel;
//
//
//public class MainWindow extends JPanel implements SingletonWindow {
//
//	private static final long serialVersionUID = -4706517331314435778L;				
//	private FormPanel form;
//	private JFrame f;
//
//	
//	public MainWindow() {		
//		setLayout(new BorderLayout());	    						
//		this.form = new FormPanel("FormBoth.jfrm");			
//		add(this.form, BorderLayout.CENTER);
//		this.revalidate();
//		f = new JFrame();
//	}
//
//	public WindowInfo getWindowInfo() {		
//		WindowInfo m_viewInfo = new WindowInfo(WindowInfo.RESIZABLE);
//    	m_viewInfo.setWidth(500);   // 480
//		m_viewInfo.setHeight(370);   // 300
//		m_viewInfo.setTitle(PluginServices.getText(this, "inp_descr"));		
//		return m_viewInfo;
//	}
//	
//	public void setWidth(int width) {
//		this.getWindowInfo().setWidth(width);
//	}
//
//	public Object getWindowProfile() {
//		return null;
//	}
//
//	public Object getWindowModel() {
//		return null;
//	}
//
//	public FormPanel getForm() {
//		return this.form;
//	}
//
//	public JFrame getFrame() {
//		return new JFrame();
//	}
//
//	public void setFrame(JFrame frame) {
//		this.f = frame;
//	}
//
//	public void close() {
//        f.setVisible(false); 
//        f.dispose(); 
//	}
//	
//}
