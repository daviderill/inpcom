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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.tecnicsassociats.gvsig.inpcom.util.Utils;

public class MainDao {
	
	public static Connection connectionPostgis;   	
    public static String schema;
	public static String schemaDrivers = "drivers";
	
	
    public static boolean setConnectionPostgis(String host, String port, String db, String user, String password) {
    	
        //String connectionString = iniProperties.getProperty("POSTGIS_CONNECTION_STRING");
    	//jdbc\:postgresql\://176.31.185.134\:5432/demo_mtvo?user\=tecnics&password\=XavierTorret
        String connectionString = "jdbc:postgresql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + password;
        try {
            connectionPostgis = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            try {
                connectionPostgis = DriverManager.getConnection(connectionString);
            } catch (SQLException e1) {
                Utils.showError(e1.getMessage(), "", "inp_descr");
                return false;
            }   		
        }
        return true;
        
    }	
    
    
	public static boolean executeSql(String sql, boolean commit) {
		try {
			Statement ps = connectionPostgis.createStatement();
	        ps.executeUpdate(sql);
	        if (commit){
	        	connectionPostgis.commit();
	        }
			return true;
		} catch (SQLException e) {
			Utils.showError(e, sql);
			return false;
		}
	}	
	
	
	public static boolean executeSql(String sql) {
		try {
			Statement ps = connectionPostgis.createStatement();
	        ps.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			Utils.showError(e, sql);
			return false;
		}
	}		
    
	
    // Check if the table exists
	public static boolean checkTable(String tableName) {
        String sql = "SELECT * FROM pg_tables WHERE lower(tablename) = '" + tableName + "'";
        try {
            Statement stat = connectionPostgis.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            return (rs.next());
        } catch (SQLException e) {
        	Utils.showError(e.getMessage(), "", "inp_descr");
            return false;
        }
    }
	
	
    // Check if the table exists
	public static boolean checkTable(String schemaName, String tableName) {
        String sql = "SELECT * FROM pg_tables " +
        		"WHERE lower(schemaname) = '" + schemaName + "' AND lower(tablename) = '" + tableName + "'";
        try {
            Statement stat = connectionPostgis.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            return (rs.next());
        } catch (SQLException e) {
        	Utils.showError(e.getMessage(), "", "inp_descr");
            return false;
        }
    }	
    
    
    // Check if the view exists
    public static boolean checkView(String viewName) {
        String sql = "SELECT * FROM pg_views WHERE lower(viewname) = '" + viewName + "'";
        try {
            Statement stat = connectionPostgis.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            return (rs.next());
        } catch (SQLException e) {
        	Utils.showError(e.getMessage(), "", "inp_descr");
            return false;
        }
    }    
    
    
	public static List<String> getSchemas(){

        String sql = "SELECT schema_name FROM information_schema.schemata " +
        		"WHERE schema_name <> 'information_schema' AND schema_name !~ E'^pg_' " +
        		"AND schema_name <> 'drivers' AND schema_name <> 'public' " +
        		"ORDER BY schema_name";
    	List<String> elems = new ArrayList<String>();
        try {
    		connectionPostgis.setAutoCommit(false);        	
            Statement stat = connectionPostgis.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
            	elems.add(rs.getString(1));
            }
            rs.close();
    		return elems;	            
        } catch (SQLException e) {
            Utils.showError(e.getMessage(), "", "inp_descr");
            return elems;
        }
		
	}
	
	
	private static ResultSet getResultset(String sql){
		
        ResultSet rs = null;        
        try {
            connectionPostgis.setAutoCommit(true);
        	Statement stat = connectionPostgis.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
        			ResultSet.CONCUR_UPDATABLE);
            rs = stat.executeQuery(sql);
        } catch (SQLException e) {
            Utils.showError(e.getMessage(), "", "inp_descr");
        }
        return rs;   
        
	}
	
	public static ResultSet getTableResultset(String table) {
        String sql = "SELECT * FROM " + schema + "." + table;
        return getResultset(sql);
	}
	
	
	public static ResultSet getRaingageResultset(String table) {
        String sql = "SELECT rg_id, form_type, intvl, scf, rgage_type, timser_id, fname, sta, units" +
        		" FROM " + schema + "." + table;
        return getResultset(sql);
	}	

	
    public static List<String> getSoftware(){

        // Get content of target table
        String sql = "SELECT * "
        		+ "FROM " + schemaDrivers + ".epa_software " 
        		+ "WHERE status_datamanager = 'Ok' ";            
        Statement stat;
        List<String> list = new ArrayList<String>();
		try {
			stat = connectionPostgis.createStatement();
	        ResultSet rs = stat.executeQuery(sql);
	        while (rs.next()) {
	        	list.add(rs.getString("id"));
	        }
	        rs.close();   			
		} catch (SQLException e) {
            Utils.showError(e.getMessage(), "", "inp_descr");
		}            
		return list;
    	
    }


	public static Vector<String> getTable(String table, String schemaParam) {
        
        Vector<String> vector = new Vector<String>();
        vector.add("");
        
		if (schemaParam == null){
			schemaParam = schema;
		}
		if (!checkTable(schemaParam, table)) {
			return vector;
		}
		String sql = "SELECT * FROM " + schemaParam + "." + table;
        Statement stat;
		try {
			stat = connectionPostgis.createStatement();
	        ResultSet rs = stat.executeQuery(sql);
	        while (rs.next()) {
	        	vector.add(rs.getString(1));
	        }
	        stat.close();
		} catch (SQLException e) {
            Utils.showError(e.getMessage(), "", "inp_descr");
		}            
		return vector;
		
	}	
	
	
	public static void setResultSelect(String schema, String table, String result) {
		String sql = "DELETE FROM " + schema + "." + table;
		executeSql(sql);
		sql = "INSERT INTO " + schema + "." + table + " VALUES ('" + result + "')";
		executeSql(sql);
	}
	
	
	public static void setSchema(String schema) {
		MainDao.schema = schema;
	}
	
	
	public static void setSchemaDrivers(String schemaDrivers) {
		MainDao.schemaDrivers = schemaDrivers;
	}


	public static void deleteSchema(String schemaName) {
		String sql = "DROP schema IF EXISTS " + schemaName + " CASCADE;";
		executeSql(sql, true);		
	}


	public static void createSchema(String schemaName) {
		
		String sql = "CREATE schema " + schemaName;
		executeSql(sql);	
		sql = "SET search_path TO '" + schemaName + "'";
		executeSql(sql);	
		try {
	    	String folderRoot = new File(".").getCanonicalPath() + File.separator;         		
			String file = folderRoot + "inp/create_schema.sql";
			File fileName = new File(file);			
			RandomAccessFile rat = new RandomAccessFile(fileName, "r");
			String line;
			String content = "";
			while ((line = rat.readLine()) != null){
				content += line + "\n";
			}
			if (executeSql(content, true)){
				rat.close();				
				sql = "SET search_path TO '" + schemaName + "', 'public'";
				executeSql(sql);					
				file = folderRoot + "inp/create_schema_2.sql";
				fileName = new File(file);			
				rat = new RandomAccessFile(fileName, "r");
				content = "";
				while ((line = rat.readLine()) != null){
					content += line + "\n";
				}
				if (executeSql(content, true)){
					Utils.showMessage("Schema creation completed", "", "INPcom");							
				}
				rat.close();	
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}