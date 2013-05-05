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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import com.tecnicsassociats.gvsig.inpcom.RptTarget;
import com.tecnicsassociats.gvsig.inpcom.util.Utils;


public class ModelPostgis extends Model {

	public static Connection connectionPostgis;   	
    private String insertSql;
	private ArrayList<String> tokens;
	private ArrayList<ArrayList<String>> tokensList;	
	private File fileRpt;
	private String projectName;
	private int lineNumber;   // Number of lines read
	private ArrayList<String> pollutants;
	
	
    public ModelPostgis(String export, String execType) {
    	this.sExport = export;
        this.execType = execType;    	
    	init();
    }

    public ModelPostgis() {
    	init();
    }    

    
    private void init(){
    	
        // Get properties file
        if (!enabledPropertiesFile()) {
            return;
        }

        // Sets initial configuration files
        configIni();
        
        // Get log file
        logger = Utils.getLogger();
        
    }

    
    public void execute(String execType, String export, String exec, String import_) {

        // Process all Postgis tables and output to INP file
        if (export.equals("1")) {
            processAll(null);
        }

        if (exec.equals("1")) {
            execSWMM(null, null);
        }

        if (import_.equals("1")) {
            File file = new File(iniProperties.getProperty("FILE_RPT"));            	
            importRpt(file, "test");
        }

    }

    
    public boolean setConnectionPostgis(String host, String port, String db, String user, String password) {
    	boolean isConnected = MainDao.setConnectionPostgis(host, port, db, user, password);
    	connectionPostgis = MainDao.connectionPostgis;
    	return isConnected;
    }
    

    // Read content of the table saved it in an Array
    private ArrayList<LinkedHashMap<String, String>> getTableData(String tableName) {

    	LinkedHashMap<String, String> mDades;
        ArrayList<LinkedHashMap<String, String>> mAux = new ArrayList<LinkedHashMap<String, String>>();
        String sql = "SELECT * FROM sewnet." + tableName;
        PreparedStatement ps;
        try {
            ps = connectionPostgis.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            // Get columns name
            int fields = rsmd.getColumnCount();
            String[] columns = new String[fields];
            for (int i = 0; i < fields; i++) {
                columns[i] = rsmd.getColumnName(i + 1);
            }
            String value;
            while (rs.next()) {
                mDades = new LinkedHashMap<String, String>();
                for (int i = 0; i < fields; i++) {
                    Object o = rs.getObject(i + 1);
                    if (o != null) {
                        value = o.toString();
                        mDades.put(columns[i], value);
                    }
                }
                mAux.add(mDades);
            }
            rs.close();
        } catch (SQLException e) {
        	Utils.showError(e, sql);
        }
        return mAux;

    }


    // Main procedure
    public boolean processAll(File fileInp) {

        logger.info("exportINP");
    	String sql = "";
   	
        try {
        	
            // Get default INP output file
            if (fileInp == null) {
                String sFile = iniProperties.getProperty(sExport + "INP");
                sFile = folderConfig + File.separator + sFile;
                fileInp = new File(sFile);
            }
                
            // Get some properties
            default_size = Integer.parseInt(iniProperties.getProperty(sExport + "SIZE_DEFAULT"));

            // Open template and output file
            rat = new RandomAccessFile(this.fileTemplate, "r");
            raf = new RandomAccessFile(fileInp, "rw");
            raf.setLength(0);

            // Get content of target table
            sql = "SELECT target.id as target_id, target.name as target_name, lines, main.id as main_id, main.name as table_name "
            		+ "FROM " + MainDao.schemaDrivers + ".swmm_inp_target as target " 
            		+ "INNER JOIN " + MainDao.schemaDrivers + ".swmm_inp_table as main ON target.table_id = main.id";            
            Statement stat = connectionPostgis.createStatement();            
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                //System.out.println(rs.getInt("target_id") + "  " + rs.getString("table_name"));
            	Integer optionsTable = Integer.parseInt(iniProperties.getProperty("INP_OPTIONS_TABLE"));
            	Integer reportTable = Integer.parseInt(iniProperties.getProperty("INP_REPORT_TABLE"));
            	if (rs.getInt("main_id") == optionsTable || rs.getInt("main_id") == reportTable){
            		processTarget2(rs.getInt("target_id"), rs.getString("table_name"), rs.getInt("lines"));
            	}
            	else{
            		processTarget(rs.getInt("target_id"), rs.getString("table_name"), rs.getInt("lines"));
            	}
            }
            rs.close();
            rat.close();
            raf.close();

            // Ending message
            Utils.showMessage("inp_end", fileInp.getAbsolutePath(), "inp_descr");
            logger.info(fileInp.getAbsolutePath());
            return true;

        } catch (IOException e) {
            Utils.showError(e);
            return false;
        } catch (SQLException e) {
            Utils.showError(e, sql);
            return false;
        }

    }


    // Process target specified by id parameter
    private void processTarget(int id, String tableName, int lines) throws IOException, SQLException {

        // Go to the first line of the target
        for (int i = 1; i <= lines; i++) {
            String line = rat.readLine();
            raf.writeBytes(line + "\r\n");
        }

        // If table is null or doesn't exit then exit function
        if (!MainDao.checkTable(tableName) && !MainDao.checkView(tableName)) {
            return;
        }

        // Get data of the specified Postgis table
        this.lMapDades = getTableData(tableName);
        if (this.lMapDades.isEmpty()) {
            return;
        }

        // Get table columns to write into this target
        mHeader = new LinkedHashMap<String, Integer>();
        String sql = "SELECT name, space FROM " + MainDao.schemaDrivers + ".swmm_inp_target_fields WHERE target_id = " + id + " ORDER BY pos";
        Statement stat = connectionPostgis.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        while (rs.next()) {
            mHeader.put(rs.getString("name").trim().toLowerCase(), rs.getInt("space"));
        }
        rs.close();

        ListIterator<LinkedHashMap<String, String>> it = this.lMapDades.listIterator();
        LinkedHashMap<String, String> m; // Current Postgis row data
        //int index = 0;
        String sValor = null;
        int size = 0;
        // Iterate over Postgis table
        while (it.hasNext()) {
            m = it.next();
            Set<String> set = mHeader.keySet();
            Iterator<String> itKey = set.iterator();
            // Iterate over fields specified in table target_fields
            while (itKey.hasNext()) {
                String sKey = (String) itKey.next();
                sKey = sKey.toLowerCase();
                size = mHeader.get(sKey);
                // Write to the output file if the field exists in Postgis table
                if (m.containsKey(sKey)) {
                    sValor = (String) m.get(sKey);
                    raf.writeBytes(sValor);
                    // Complete spaces with empty values
                    for (int j = sValor.length(); j <= size; j++) {
                        raf.writeBytes(" ");
                    }
                } // If key doesn't exist write empty spaces
                else {
                    for (int j = 0; j <= size; j++) {
                        raf.writeBytes(" ");
                    }
                }
            }
            raf.writeBytes("\r\n");
        }

    }

    
    // Process target options or target report
    private void processTarget2(int id, String tableName, int lines) throws IOException, SQLException {

        // Go to the first line of the target
        for (int i = 1; i <= lines; i++) {
            String line = rat.readLine();
            raf.writeBytes(line + "\r\n");
        }

        // If table is null or doesn't exit then exit function
        if (!MainDao.checkTable(tableName) && !MainDao.checkView(tableName)) {
            return;
        }

        // Get data of the specified Postgis table
        ArrayList<LinkedHashMap<String, String>> options = getTableData(tableName);
        if (options.isEmpty()) {
            return;
        }

        ListIterator<LinkedHashMap<String, String>> it = options.listIterator();
        LinkedHashMap<String, String> m; // Current Postgis row data
        //int index = 0;
        String sValor = null;
        int size = Integer.parseInt(iniProperties.getProperty("INP_OPTIONS_SIZE"));
        // Iterate over Postgis table (only one element)
        while (it.hasNext()) {
            m = it.next();
            Set<String> set = m.keySet();
            Iterator<String> itKey = set.iterator();
            // Iterate over fields and write 
            while (itKey.hasNext()) {
                // Write to the output file (one per row)
            	String sKey = (String) itKey.next();
                sValor = (String) m.get(sKey);
                raf.writeBytes(sKey.toUpperCase());
                // Complete spaces with empty values
                for (int j = sKey.length(); j <= size; j++) {
                    raf.writeBytes(" ");
                }
                raf.writeBytes(sValor);
                raf.writeBytes("\r\n");                
            }
        }

    }    

    
    // Exec SWMM
    public boolean execSWMM(File fileInp, File fileRpt) {

        logger.info("execSWMM");

//		file = folder + "swmm5.exe " + folder + "222.inp " + folder + "2.rpt " + folder + "2.out";

        String folder = iniProperties.getProperty("FOLDER_EPA_SWMM");
        String exe = iniProperties.getProperty("EXE_EPA_SWMM");
        String exeCmd = folder + File.separator + exe;
        File exeFile = new File(exeCmd);
        // Check if exeFile exists
		if (!exeFile.exists()){
			Utils.showError("inp_error_notfound", exeCmd, "inp_descr");
			return false;
		}

        String sFile;
        // Get files
        if (fileInp == null) {
            sFile = iniProperties.getProperty(sExport + "INP");
            sFile = folderConfig + File.separator + sFile;
            fileInp = new File(sFile);
            sFile = sFile.replace(".inp", ".rpt");
            fileRpt = new File(sFile);
        }
        if (!fileInp.exists()){
			Utils.showError("inp_error_notfound", fileInp.getAbsolutePath(), "inp_descr");     
			return false;
        }
        sFile = fileRpt.getAbsolutePath().replace(".rpt", ".out");
        File fileOut = new File(sFile);

        exeCmd += " " + fileInp.getAbsolutePath() + " " + fileRpt.getAbsolutePath() + " " + fileOut.getAbsolutePath();

        // Ending message
        logger.info(exeCmd);            

        // Exec process
    	Process p;
		try {
			p = Runtime.getRuntime().exec(exeCmd);
	        p.waitFor();			
		} catch (IOException e) {
			Utils.showError("inp_error_io", exeCmd, "inp_descr");
			return false;
		} catch (InterruptedException e) {
			Utils.showError("inp_error_io", exeCmd, "inp_descr");
			return false;
		}

        // Ending message
        Utils.showMessage("inp_end", fileOut.getAbsolutePath(), "inp_descr");                

        return true;

    }


    // Import RPT file into Postgis tables
    public boolean importRpt(File fileRpt, String projectName) {
        
    	this.fileRpt = fileRpt;
    	this.projectName = projectName;

        logger.info("importRpt");
        
    	// Check if Project Name exists in rpt_result_id
    	boolean exists = false;
    	if (existsProjectName()){
    		exists = true;
        	int reply = Utils.confirmDialog("project_exists", "inp_descr");    		
        	if (reply == JOptionPane.NO_OPTION){
        		return false;
        	}
    	}
    	
		// Open RPT file
		try {
			rat = new RandomAccessFile(fileRpt, "r");
			lineNumber = 0;
		} catch (FileNotFoundException e) {
			Utils.showError("inp_error_notfound", fileRpt.getAbsolutePath(), "inp_descr");
			return false;
		}			
        
        // Get info from rpt_target into memory
        TreeMap<Integer, RptTarget> targets = new TreeMap<Integer, RptTarget>();
        String sql = "SELECT * FROM " + MainDao.schemaDrivers + "." + sExport.toLowerCase() + "rpt_target " +
        		"WHERE type <> 0 ORDER BY id";
        try {
    		connectionPostgis.setAutoCommit(false);        	
            Statement stat = connectionPostgis.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                RptTarget rpt = new RptTarget(rs);
                targets.put(rpt.getId(), rpt);
            }
            rs.close();
        } catch (SQLException e) {
            Utils.showError(e, sql);
            return false;
        }
        
        // Iterate over targets
        Iterator<Entry<Integer, RptTarget>> it = targets.entrySet().iterator();
        while (it.hasNext()) {
        	insertSql = "";
            Map.Entry<Integer, RptTarget> mapEntry = it.next();
            RptTarget rpt = mapEntry.getValue();
        	if (processRpt(rpt)){
	    		if (exists){
	    			sql = "DELETE FROM " + MainDao.schema + "." + rpt.getTable() + " WHERE result_id = '" + projectName + "'";
	    			logger.info(sql);
	    			MainDao.executeSql(sql);
	    		}
	    		if (!insertSql.equals("")){
	    			logger.info(insertSql);	    			
		    		if (!MainDao.executeSql(insertSql)){
						return false;
					}
	    		}
        	} else{
           		logger.info("Target not found: " + rpt.getId() + " - " + rpt.getDescription());
        	}
        }
        
        // Commit transaction ONLY if everything ok
        try {
			connectionPostgis.commit();
		} catch (SQLException e) {
            Utils.showError(e, sql);
		}
        
        // Ending message
        Utils.showMessage("import_end", "", "inp_descr");                

		return true;
		
    }


	private boolean existsProjectName() {
		
		String sql = "SELECT * FROM " + MainDao.schema + ".rpt_result_cat WHERE result_id = '" + projectName + "'";
		try {
			PreparedStatement ps = connectionPostgis.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        return rs.next();
		} catch (SQLException e) {
            Utils.showError(e, sql);
			return false;
		}
		
	}

		
	private boolean processRpt(RptTarget rpt) {

		// Read lines until rpt.getDescription() is found		
		boolean found = false;
		String line;
		String aux;
		
		logger.info("Target: " + rpt.getId() + " - " + rpt.getDescription());
		
//		if (rpt.getId() == 10){
//			for (int i = 0; i < 13; i++) {
//				try {
//					lineNumber++;							
//					line = rat.readLine().trim();
//				} catch (IOException e) {
//					Utils.showError(e);
//				}
//			}
//		}
		
		while (!found){
			try {
				// If pointer has reached EOF, return to first position
				if (rat.getFilePointer() >= rat.length()){
					rat.seek(0);
					lineNumber = 0;
					return false;
				}
				lineNumber++;				
				line = rat.readLine().trim();
				if (line.length() >= rpt.getDescription().length()){
					aux = line.substring(0, rpt.getDescription().length());
					if (aux.equals(rpt.getDescription())){
						found = true;
						logger.info("Target line number: " + lineNumber);						
					}
				}
			} catch (IOException e) {
				Utils.showError(e);
			}
		}
		
        if (rpt.getType() == 3 || rpt.getType() == 5 || rpt.getType() == 6){
        	getPollutants(rpt);
        }
		
		// Jump number of lines specified in rpt.getTitleLines()
		for (int i = 1; i <= rpt.getTitleLines(); i++) {
			try {
				lineNumber++;
				line = rat.readLine();
				// Check if we have reached next Target
				if (line.contains("No ")){
					return false;
				}				
			} catch (IOException e) {
				Utils.showError("inp_error_io", "", "inp_descr");
			}
		}		
		
		// Read following lines until blank line is found
		tokensList = new ArrayList<ArrayList<String>>();		
		parseLines(rpt);
		if (rpt.getType() == 2){
			processTokens(rpt);
		}
		else if (rpt.getType() == 3){
			processTokens3(rpt);
		}
		
		return true;
		
	}	
	

	private void getPollutants(RptTarget rpt) {
		
		// Open RPT file again
		if (rpt.getType() == 3){
			try {
				rat = new RandomAccessFile(fileRpt, "r");
			} catch (FileNotFoundException e) {
				Utils.showError("inp_error_notfound", fileRpt.getAbsolutePath(), "inp_descr");
				return;
			}
		}
		
		String line = "";
		int jumpLines;
		try {		
			if (rpt.getType() == 3){			
				for (int i = 1; i < lineNumber - 1; i++) {
					//System.out.println(rat.readLine().trim());
					rat.readLine().trim();
				}
				System.out.println("");
				line = rat.readLine().trim();		
				lineNumber--;
			} else{
				jumpLines = (rpt.getType() == 5) ? 4 : 5;
				for (int i = 1; i <= jumpLines; i++) {		
					lineNumber++;					
					line = rat.readLine().trim();
				}
			}
			boolean blankLine = (line.length() == 0);
			if (!blankLine){
				Scanner scanner = new Scanner(line);
				if (rpt.getType() == 3 || rpt.getType() == 6){
					int jumpScanner	= (rpt.getType() == 3) ? 1 : 4;
					for (int i = 0; i < jumpScanner; i++) {
						scanner.next();						
					}
				}
				// Get pollutant name
				parseLine1(scanner, rpt, false);		
				pollutants = new ArrayList<String>();	
				for (int i = 0; i < tokens.size(); i++) {
					pollutants.add(tokens.get(i));
				}
			}
		} catch (IOException e) {
			Utils.showError("inp_error_io", "", "inp_descr");
		}		
		
	}
	
	
	// Parse all lines
	private void parseLines(RptTarget rpt) {
		
		tokens = new ArrayList<String>();			
		boolean blankLine = false;		
		while (!blankLine){
			try {
				lineNumber++;
				String line = rat.readLine().trim();
				//System.out.println(line);
				blankLine = (line.length()==0);
				if (!blankLine){
					Scanner scanner = new Scanner(line);
					if (rpt.getType() == 1){
						parseLine1(scanner, rpt, false);
						processTokens(rpt);							
					}		
					else if (rpt.getType() == 2){					
						parseLine2(scanner, rpt, true);
					}
					else if (rpt.getType() == 3){	
						tokens = new ArrayList<String>();
						parseLine2(scanner, rpt, false);
						tokensList.add(tokens);
					}					
					else if (rpt.getType() == 4){					
						parseLine1(scanner, rpt, true);
						processTokens(rpt);							
					}	
					else if (rpt.getType() == 5){					
						tokens = new ArrayList<String>();
						parseLine1(scanner, rpt, false);
						processTokens5(rpt);						
					}				
					else if (rpt.getType() == 6){					
						tokens = new ArrayList<String>();
						parseLine1(scanner, rpt, false);
						processTokens6(rpt);						
					}							
				}
			} catch (IOException e) {
				Utils.showError("inp_error_io", "", "inp_descr");
			}
		}		
		
	}
		

	// Parse values of current line
	private void parseLine1(Scanner scanner, RptTarget rpt, boolean together) {
		
		// Parse line
		tokens = new ArrayList<String>();	
		String token = "";
		while (scanner.hasNext()){
			if (together){
				token += " " + scanner.next();
			}
			else{
				token = scanner.next();
				tokens.add(token);
			}
		}
		if (together){
			tokens.add(token.trim());
		}
		
	}	
	
	
	// Parse values of current line that contains ".." in it
	private void parseLine2(Scanner scanner, RptTarget rpt, boolean together) {
		
		// Parse line
		String token;
		boolean valid = false;
		String aux = "";
		int numTokens = 0;
		while (scanner.hasNext()){
			token = scanner.next();
			if (valid == true){
				numTokens++;
				if (numTokens <= rpt.getTokens()){
					if (together){
						aux += token + " ";
					}
					else{
						tokens.add(token);						
					}
				}
			}
			if (token.contains("..")){
				valid = true;
			}
		}
		if (valid == true && together){
			tokens.add(aux.trim());
			//System.out.println(aux);				
		}	
		
	}
	
	
	private void processTokens(RptTarget rpt) {

		String fields = "result_id, ";
		String values = "'" + projectName + "', ";
		String sql = "SELECT * FROM " + MainDao.schema + "." + rpt.getTable();
		try {
	        PreparedStatement ps = connectionPostgis.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData rsmd = rs.getMetaData();	
	        rs.close();
	        int j;
	        for (int i = 0; i < tokens.size(); i++){
	        	j = i + 2;
	        	switch (rsmd.getColumnType(j)) {
				case Types.NUMERIC:
				case Types.DOUBLE:
				case Types.INTEGER:
					values += tokens.get(i) + ", ";
					break;					
				case Types.VARCHAR:
					values += "'" + tokens.get(i) + "', ";
					break;					
				default:
					values += "'" + tokens.get(i) + "', ";
					break;
				}
				fields += rsmd.getColumnName(j) + ", ";        	
	        }
		} catch (SQLException e) {
			Utils.showError(e, sql);
		}
	
		fields = fields.substring(0, fields.length() - 2);
		values = values.substring(0, values.length() - 2);
		sql = "INSERT INTO " + MainDao.schema + "." + rpt.getTable() + " (" + fields + ") VALUES (" + values + ");\n";
		insertSql += sql;
		
	}
	
	
	private void processTokens3(RptTarget rpt) {

		String sql = "SELECT * FROM " + MainDao.schema + "." + rpt.getTable();
		try {
	        PreparedStatement ps = connectionPostgis.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData rsmd = rs.getMetaData();	
	        rs.close();

			// Iterate over pollutants
			for (int i = 0; i < pollutants.size(); i++) {
				String fields = "result_id, poll_id, ";
				String values = "'" + projectName + "', '" + pollutants.get(i) + "', ";
				// Iterate over fields
				for (int j = 0; j < tokensList.size(); j++) {
					String value = tokensList.get(j).get(i);
		        	switch (rsmd.getColumnType(j + 3)) {
					case Types.NUMERIC:
					case Types.DOUBLE:
					case Types.INTEGER:
						values += value + ", ";
						break;					
					case Types.VARCHAR:
						values += "'" + value + "', ";
						break;					
					default:
						values += "'" + value + "', ";
						break;
					}
					fields += rsmd.getColumnName(j + 3) + ", ";        	
				}
				fields = fields.substring(0, fields.length() - 2);
				values = values.substring(0, values.length() - 2);
				sql = "INSERT INTO " + MainDao.schema + "." + rpt.getTable() + " (" + fields + ") VALUES (" + values + ");\n";
				insertSql += sql;				
	        }
			
		} catch (SQLException e) {
			Utils.showError(e, sql);
		} catch (Exception e) {
			Utils.showError(e);
		}
		
	}		
		
	
	private void processTokens5(RptTarget rpt) {

		String fields = "result_id, subc_id, poll_id, value";
		String fixedValues = "'" + projectName + "', '" + tokens.get(0) + "', ";
		String sql;
		String values;
		Double units;
		
		// TODO: No permetre tipus String (peta a Subcatchment)
		try{
			Integer.parseInt(tokens.get(0));
		} catch (NumberFormatException e){
			return;
		}
		
		// Iterate over pollutants
		if (tokens.size() > pollutants.size()){
			for (int i = 0; i < pollutants.size(); i++) {
				units = Double.valueOf(tokens.get(i + 1));
				values = fixedValues + "'" + pollutants.get(i) + "', " + units;
				sql = "INSERT INTO " + MainDao.schema + "." + rpt.getTable() + " (" + fields + ") VALUES (" + values + ");\n";
				insertSql += sql;		        
			}
		}
		
	}	

	
	private void processTokens6(RptTarget rpt) {
		
		String fields = "result_id, node_id, flow_freq, avg_flow, max_flow, total_vol";
		String fields2 = "result_id, node_id, poll_id, value";		
		String fixedValues = "'" + projectName + "', '" + tokens.get(0) + "', ";
		String values;
		String sql;
		Double units;
	
		// If found separator o resume line skip them
		if (tokens.size() < 5 || tokens.get(0).equals("System")){
			return;
		}
		
		// Iterate over first 5 fields
		values = fixedValues;
		for (int j = 1; j < 5; j++) {
			values += tokens.get(j) + ", ";
		}
		values = values.substring(0, values.length() - 2);		
		sql = "INSERT INTO " + MainDao.schema + "." + rpt.getTable() + " (" + fields + ") VALUES (" + values + ");\n";
		insertSql += sql;				
		
		// Iterate over pollutants
		for (int i = 0; i < pollutants.size(); i++) {
			int j = i + 5;
			units = Double.valueOf(tokens.get(j));
			values = fixedValues + "'" + pollutants.get(i) + "', " + units;
			sql = "DELETE FROM " + MainDao.schema + ".rpt_outfallload_sum " +
				"WHERE result_id = '" + projectName + "' AND node_id = '"  + tokens.get(0) + "' AND poll_id = '" + pollutants.get(i) + "';\n";
			insertSql += sql;	
			sql = "INSERT INTO " + MainDao.schema + ".rpt_outfallload_sum (" + fields2 + ") VALUES (" + values + ");\n";
			insertSql += sql;		        
		}
		
	}		
	

}