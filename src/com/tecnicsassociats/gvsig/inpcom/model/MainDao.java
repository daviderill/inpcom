package com.tecnicsassociats.gvsig.inpcom.model;

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
			Utils.showError(e.getMessage(), "", "inp_descr");
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
        		"WHERE schema_name <> 'information_schema' AND schema_name !~ E'^pg_'" +
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
	
	
	public static ResultSet getTableResultset(String table) {
		
        String sql = "SELECT * FROM " + schema + "." + table;
        ResultSet rs = null;        
        try {
            connectionPostgis.setAutoCommit(true);
        	Statement stat = connectionPostgis.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stat.executeQuery(sql);
        } catch (SQLException e) {
            Utils.showError(e.getMessage(), "", "inp_descr");
        }
        return rs;   
        
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
		executeSql(sql, true);
		sql = "INSERT INTO " + schema + "." + table + " VALUES ('" + result + "')";
		executeSql(sql, true);
	}
	
	
	public static void setSchema(String schema) {
		MainDao.schema = schema;
	}
	
	
	public static void setSchemaDrivers(String schemaDrivers) {
		MainDao.schemaDrivers = schemaDrivers;
	}





}
