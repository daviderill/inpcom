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
package com.tecnicsassociats.inpcom.model;

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

import com.tecnicsassociats.inpcom.RptTarget;
import com.tecnicsassociats.inpcom.util.Utils;


public class ModelPostgis extends Model {

	private static Connection connectionPostgis;
    private static String insertSql;
	private static ArrayList<String> tokens;
	private static ArrayList<ArrayList<String>> tokensList;	
	private static File fileRpt;
	private static String projectName;
	private static int lineNumber;   // Number of lines read
	private static ArrayList<String> pollutants;
	
	private static String firstLine;
	private static String lastTimeHydraulic = "";
	
	private static final String OPTIONS_TABLE = "v_inp_options";
	private static final String REPORTS_TABLE = "inp_report";
	private static final String TIMES_TABLE = "inp_times";
	private static final String PATTERNS_TABLE = "inp_pattern";
	private static final Integer DEFAULT_SPACE = 23;

    
    public static void execute(String execType, String export, String exec, String import_) {

        // Get log file
        logger = Utils.getLogger();
		iniProperties = MainDao.getPropertiesFile();   
        
        // Process all Postgis tables and output to INP file
        if (export.equals("1")) {
            processAll(null);
        }

        if (exec.equals("1")) {
            execSWMM(null, null);
        }

        if (import_.equals("1")) {
            File file = new File(Model.iniProperties.getProperty("FILE_RPT"));            	
            importRpt(file, "test");
        }

    }

    
    public static boolean setConnectionPostgis(String host, String port, String db, String user, String password) {
    	boolean isConnected = MainDao.setConnectionPostgis(host, port, db, user, password);
    	connectionPostgis = MainDao.connectionPostgis;
    	return isConnected;
    }
    
	
    
	public static void rollback() throws SQLException{
		ModelPostgis.connectionPostgis.rollback();
	}	    

	
    // Read content of the table saved it in an Array
    private static ArrayList<LinkedHashMap<String, String>> getTableData(String tableName) {

    	LinkedHashMap<String, String> mDades;
        ArrayList<LinkedHashMap<String, String>> mAux = new ArrayList<LinkedHashMap<String, String>>();
        String sql = "SELECT * FROM " + MainDao.getSchema() + "." +  tableName;
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
    public static boolean processAll(File fileInp) {

        // Get log file
        logger = Utils.getLogger();
        logger.info("exportINP");

		iniProperties = MainDao.getPropertiesFile();         
    	String sql = "";
   	
        try {
        	
            // Get default INP output file
            if (fileInp == null) {
                String sFile = iniProperties.getProperty("DEFAULT_INP", "out");
                sFile = MainDao.folderConfig + sFile;
                fileInp = new File(sFile);
            }
            
            // Get INP template File
            String templatePath = MainDao.folderConfig + softwareVersion + ".inp";
            File fileTemplate = new File(templatePath);
            if (!fileTemplate.exists()){
            	Utils.showMessage("inp_error_notfound", fileTemplate.getAbsolutePath(), "inp_descr");
            	return false;
            }
                
            // Open template and output file
            rat = new RandomAccessFile(fileTemplate, "r");
            raf = new RandomAccessFile(fileInp, "rw");
            raf.setLength(0);

            // Get content of target table
            sql = "SELECT target.id as target_id, target.name as target_name, lines, main.id as main_id, main.dbase_table as table_name "
            		+ "FROM inp_target as target " 
            		+ "INNER JOIN inp_table as main ON target.table_id = main.id";             
            Statement stat = connectionDrivers.createStatement();            
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
            	logger.info("INP target: " + rs.getInt("target_id") + " - " + rs.getString("table_name") + " - " + rs.getInt("lines"));
            	if (rs.getString("table_name").equals(OPTIONS_TABLE) || 
            		rs.getString("table_name").equals(REPORTS_TABLE) ||
            		rs.getString("table_name").equals(TIMES_TABLE)){
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
    private static void processTarget(int id, String tableName, int lines) throws IOException, SQLException {

        // Go to the first line of the target
        for (int i = 1; i <= lines; i++) {
            String line = rat.readLine();
            raf.writeBytes(line + "\r\n");
        }

        // If table is null or doesn't exit then exit function
        if (!MainDao.checkTable(MainDao.getSchema(), tableName) && !MainDao.checkView(MainDao.getSchema(), tableName)) {
        	logger.info("Table or view doesn't exist: " + tableName);
            return;
        }

        // Get data of the specified Postgis table
        lMapDades = getTableData(tableName);
        if (lMapDades.isEmpty()) {
        	logger.info("Table or view empty: " + tableName);
            return;
        }

        // Get table columns to write into this target
        mHeader = new LinkedHashMap<String, Integer>();
        String sql = "SELECT name, space FROM inp_target_fields" + 
        	" WHERE target_id = " + id + " ORDER BY pos";
        Statement stat = connectionDrivers.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        while (rs.next()) {
            mHeader.put(rs.getString("name").trim().toLowerCase(), rs.getInt("space"));
        }
        rs.close();

        ListIterator<LinkedHashMap<String, String>> itData = lMapDades.listIterator();
        LinkedHashMap<String, String> rowData;   // Current Postgis row data
        String sKey;
        int size, sizeId;
        Set<String> set = mHeader.keySet();
        Iterator<String> itKey = set.iterator();        
        
        if (tableName.equals(PATTERNS_TABLE)){
	        // Iterate over Postgis table
	        while (itData.hasNext()) {
	            rowData = itData.next();
	            itKey = set.iterator();
	            // First element: id
                String sKeyId = (String) itKey.next();
                sKeyId = sKeyId.toLowerCase();
                sizeId = mHeader.get(sKeyId);
	            parseField(rowData, sKeyId, sizeId);
	            // Every Postgis row fills 4 lines -> 6 factors per line	            
	            int i = 0;
	            while (itKey.hasNext()) {
	            	// Iterate over fields specified in table target_fields
	                sKey = (String) itKey.next();
	                sKey = sKey.toLowerCase();
	                size = mHeader.get(sKey);
		            parseField(rowData, sKey, size);
	            	i++;
	            	if (i%6 == 0 && i%24 != 0){
	    	            raf.writeBytes("\r\n");
	    	            parseField(rowData, sKeyId, sizeId);		    	            
	            	}
				}
	            raf.writeBytes("\r\n");
	        }
        }
        
        else{
	        // Iterate over Postgis table
	        while (itData.hasNext()) {
	            rowData = itData.next();
	            itKey = set.iterator();
	            // Iterate over fields specified in table target_fields
	            while (itKey.hasNext()) {
	                sKey = (String) itKey.next();
	                sKey = sKey.toLowerCase();
	                size = mHeader.get(sKey);
		            parseField(rowData, sKey, size);	
	            }
	            raf.writeBytes("\r\n");
	        }
        }
        
    }

    
    private static void parseField(LinkedHashMap<String, String> rowData, String sKey, int size) throws IOException {

        // Write to the output file if the field exists in Postgis table
        if (rowData.containsKey(sKey)) {
            String sValor = (String) rowData.get(sKey);
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
        if (size == 0){
        	raf.writeBytes(" ");
        }        
        
	}


	// Process target options or target report
    private static void processTarget2(int id, String tableName, int lines) throws IOException, SQLException {

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
        	logger.info("Empty table: " + tableName);        	
            return;
        }

        ListIterator<LinkedHashMap<String, String>> it = options.listIterator();
        LinkedHashMap<String, String> m;   // Current Postgis row data
        String sValor = null;
        int size = DEFAULT_SPACE;
        
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
    public static boolean execSWMM(File fileInp, File fileRpt) {

        // Get log file
        logger = Utils.getLogger();
        logger.info("execSWMM");
        
		iniProperties = MainDao.getPropertiesFile();   

		String exeCmd = MainDao.getSoftwareExecutable("postgis", softwareVersion);
        File exeFile = new File(exeCmd);
        // Check if exeFile exists
		if (!exeFile.exists()){
			exeCmd = JOptionPane.showInputDialog(null, "EPA Software .exe file nout found: " + exeCmd + "\n" +
            	"Please set correct path");
            exeFile = new File(exeCmd);
    		if (!exeFile.exists()){            
    			Utils.showError("inp_error_notfound", exeCmd, "inp_descr");
    			return false;
    		}
    		MainDao.updateSoftware(softwareVersion, exeCmd);
		}

        String sFile;
        // Get files
        if (fileInp == null) {
            sFile = iniProperties.getProperty("DEFAULT_INP", "out");
            sFile = MainDao.folderConfig + sFile;
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

        // Create command
        exeCmd = "\"" + exeCmd + "\"";
        exeCmd += " " + fileInp.getAbsolutePath() + " " + fileRpt.getAbsolutePath() + " " + fileOut.getAbsolutePath();

        // Ending message
        logger.info(exeCmd);            

        // Exec process
    	Process p;
		try {
			p = Runtime.getRuntime().exec(exeCmd);
	        p.waitFor();			
	        p.destroy();
		} catch (IOException e) {
			Utils.showError("inp_error_io", exeCmd, "inp_descr");
			return false;
		} catch (InterruptedException e) {
			Utils.showError("inp_error_io", exeCmd, "inp_descr");
			return false;
		}

        // Ending message
        // Utils.showMessage("inp_end", fileOut.getAbsolutePath(), "inp_descr");          
        String msg = Utils.getBundleText().getString("inp_end") + "\n" + fileRpt.getAbsolutePath() + "\n" + 
        	Utils.getBundleText().getString("rpt_view_file");
        int answer = JOptionPane.showConfirmDialog(null, msg, Utils.getBundleText().getString("inp_descr"), JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION){
        	Utils.openFile(fileRpt.getAbsolutePath());
        }        

        return true;

    }


    // Import RPT file into Postgis tables
    public static boolean importRpt(File fileRpt, String projectName) {
        
        // Get log file
        logger = Utils.getLogger();    	
        logger.info("importRpt");

		iniProperties = MainDao.getPropertiesFile();   
    	ModelPostgis.fileRpt = fileRpt;
    	ModelPostgis.projectName = projectName;
            
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
        String sql = "SELECT id, db_table, description, type, title_lines, tokens, dbf_table FROM rpt_target " +
        	"WHERE type <> 0 ORDER BY id";
        try {
            Statement stat = connectionDrivers.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                RptTarget rpt = new RptTarget(rs);
                targets.put(rpt.getId(), rpt);
            }
            rs.close();
			connectionPostgis.setAutoCommit(false);       
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
            boolean ok = false;
            boolean processTarget = true;
            boolean continueTarget;
            if (softwareName.equals("swmm")){
            	ok = processRpt(rpt);
            } 
            else{
        		if (rpt.getId() >= 40){
        			processTarget = false;
        			continueTarget = true;
		    		if (exists){
		    			sql = "DELETE FROM " + MainDao.getSchema() + "." + rpt.getTable() + " WHERE result_id = '" + projectName + "'";
		    			//logger.info(sql);
		    			MainDao.executeUpdateSql(sql);
		    		}            			
        			while (continueTarget){
            			insertSql = "";            				
        				ok = processRptEpanet(rpt);
        	        	if (ok){
        		    		if (!insertSql.equals("")){
        		            	if (softwareName.equals("epanet") && rpt.getId() >= 40){
        		            		firstLine = firstLine.substring(15, 24).trim(); 
        		            		sql = "UPDATE " + MainDao.getSchema() + "." + rpt.getTable() + " SET time = '" + firstLine + "' WHERE time is null;";
        		            		insertSql+= sql;
        		            	}
        		    			//logger.info(insertSql);	            	
        			    		if (!MainDao.executeUpdateSql(insertSql)){
        							return false;
        						}
        		    		}
        	        	}
        				continueTarget = (lineNumber > 0);            	        	
        			}
        		}
        		else{
    				ok = processRptEpanet(rpt);
        		}
            }
            
        	if (ok && processTarget){
	    		if (exists){
	    			sql = "DELETE FROM " + MainDao.getSchema() + "." + rpt.getTable() + " WHERE result_id = '" + projectName + "'";
	    			//logger.info(sql);
	    			MainDao.executeUpdateSql(sql);
	    		}
	    		if (!insertSql.equals("")){
	    			//logger.info(insertSql);	            	
		    		if (!MainDao.executeUpdateSql(insertSql)){
						return false;
					}
	    		}
        	} 
        	else{
           		logger.info("Target not found: " + rpt.getId() + " - " + rpt.getDescription());
        	}
        	
        }
        
        // Commit transaction ONLY if everything ok
        try {
        	if (!connectionPostgis.getAutoCommit()){
        		connectionPostgis.commit();
        	}
		} catch (SQLException e) {
            Utils.showError(e, sql);
		}
        
        // Ending message
        Utils.showMessage("import_end", "", "inp_descr");        

		return true;
		
    }
    

	private static boolean existsProjectName() {
		
		String sql = "SELECT * FROM " + MainDao.getSchema() + ".rpt_result_cat " +
			" WHERE result_id = '" + projectName + "'";
		try {
			PreparedStatement ps = connectionPostgis.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        return rs.next();
		} catch (SQLException e) {
            Utils.showError(e, sql);
			return false;
		}
		
	}

		
	private static boolean processRpt(RptTarget rpt) {

		// Read lines until rpt.getDescription() is found		
		boolean found = false;
		String line;
		String aux;
		
		logger.info("Target: " + rpt.getId() + " - " + rpt.getDescription());
		
		// Read lines until rpt.getDescription() is found		
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
	

	private static void getPollutants(RptTarget rpt) {
		
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
				parseLine1(scanner, false);		
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
	private static void parseLines(RptTarget rpt) {
		
		tokens = new ArrayList<String>();			
		boolean blankLine = false;		
		while (!blankLine){
			try {
				lineNumber++;
				String line = rat.readLine().trim();
				blankLine = (line.length()==0);
				if (!blankLine){
					Scanner scanner = new Scanner(line);
					if (rpt.getType() == 1){
						parseLine1(scanner, false);
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
						parseLine1(scanner, true);
						processTokens(rpt);							
					}	
					else if (rpt.getType() == 5){					
						tokens = new ArrayList<String>();
						parseLine1(scanner, false);
						processTokens5(rpt);						
					}				
					else if (rpt.getType() == 6){					
						tokens = new ArrayList<String>();
						parseLine1(scanner, false);
						processTokens6(rpt);						
					}			
					else if (rpt.getType() == 7){					
						tokens = new ArrayList<String>();
						parseLine1(scanner, false);
						processTokens6(rpt);						
					}							
				}
			} catch (IOException e) {
				Utils.showError("inp_error_io", "", "inp_descr");
			}
		}		
		
	}
	

	// Parse values of current line
	private static void parseLine1(Scanner scanner, boolean together) {
		
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
	private static void parseLine2(Scanner scanner, RptTarget rpt, boolean together) {
		
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
		}	
		
	}
	
	
	private static void processTokens(RptTarget rpt) {

		String fields = "result_id, ";
		String values = "'" + projectName + "', ";
		String sql = "SELECT * FROM " + MainDao.getSchema() + "." + rpt.getTable();
		try {
	        PreparedStatement ps = connectionPostgis.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData rsmd = rs.getMetaData();	
            if (softwareName.equals("epanet")){
            	if (tokens.size() < rsmd.getColumnCount() - 3){
            		Utils.getLogger().warning("Line not valid");
            		return;
            	}
            }
	        rs.close();
	        int j;
	        for (int i=0; i<tokens.size(); i++){
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
		sql = "INSERT INTO " + MainDao.getSchema() + "." + rpt.getTable() + " (" + fields + ") VALUES (" + values + ");\n";
		insertSql += sql;
		
	}
	
	
	private static void processTokens3(RptTarget rpt) {

		String sql = "SELECT * FROM " + MainDao.getSchema() + "." + rpt.getTable();
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
				sql = "INSERT INTO " + MainDao.getSchema() + "." + rpt.getTable() + " (" + fields + ") VALUES (" + values + ");\n";
				insertSql += sql;				
	        }
			
		} catch (SQLException e) {
			Utils.showError(e, sql);
		} catch (Exception e) {
			Utils.showError(e);
		}
		
	}		
		
	
	private static void processTokens5(RptTarget rpt) {

		String fields = "result_id, subc_id, poll_id, value";
		String fixedValues = "'" + projectName + "', '" + tokens.get(0) + "', ";
		String sql;
		String values;
		Double units;
		
		// No permetre tipus String (peta a Subcatchment)
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
				sql = "INSERT INTO " + MainDao.getSchema() + "." + rpt.getTable() + " (" + fields + ") VALUES (" + values + ");\n";
				insertSql += sql;		        
			}
		}
		
	}	

	
	private static void processTokens6(RptTarget rpt) {
		
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
		sql = "INSERT INTO " + MainDao.getSchema() + "." + rpt.getTable() + " (" + fields + ") VALUES (" + values + ");\n";
		insertSql += sql;				
		
		// Iterate over pollutants
		for (int i = 0; i < pollutants.size(); i++) {
			int j = i + 5;
			units = Double.valueOf(tokens.get(j));
			values = fixedValues + "'" + pollutants.get(i) + "', " + units;
			sql = "DELETE FROM " + MainDao.getSchema() + ".rpt_outfallload_sum " +
				"WHERE result_id = '" + projectName + "' AND node_id = '"  + tokens.get(0) + "' AND poll_id = '" + pollutants.get(i) + "';\n";
			insertSql += sql;	
			sql = "INSERT INTO " + MainDao.getSchema() + ".rpt_outfallload_sum (" + fields2 + ") VALUES (" + values + ");\n";
			insertSql += sql;		        
		}
		
	}

	
	
	// Epanet
	private static boolean processRptEpanet(RptTarget rpt) {

		boolean found = false;
		String line;
		String aux;
		
		logger.info("Target: " + rpt.getId() + " - " + rpt.getDescription());

		// Read lines until rpt.getDescription() is found			
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
					aux = line.substring(0, rpt.getDescription().length()).toLowerCase();
					if (aux.equals(rpt.getDescription().toLowerCase())){
						found = true;
						firstLine = line;						
						logger.info("Target line number: " + lineNumber);						
					}
				}
			} catch (IOException e) {
				Utils.showError(e);
			}
		}
		
		// Jump number of lines specified in rpt.getTitleLines()
		for (int i=1; i<=rpt.getTitleLines(); i++) {
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
		if (rpt.getType() == 7){
			parseLinesHydraulic(rpt);
		}
		else{
			parseLines(rpt);
			if (rpt.getType() == 2){
				processTokens(rpt);
			}
		}
		
		return true;
		
	}	

	
	// Parse all lines of Hydraulic Target
	private static void parseLinesHydraulic(RptTarget rpt) {
		
		tokens = new ArrayList<String>();			
		boolean blankLine = false;		
		int numBlankLines = 0;
		while (numBlankLines <= 2){
			try {
				lineNumber++;
				String line = rat.readLine().trim();
				blankLine = (line.length()==0);
				if (!blankLine){
					Scanner scanner = new Scanner(line);
					parseLineHydraulic(scanner);
					processTokensHydraulic(rpt);
					numBlankLines = 0;					
				}
				else{
					numBlankLines++;
				}
			} catch (IOException e) {
				Utils.showError("inp_error_io", "", "inp_descr");
			}
		}		
		
	}
	
	
	// Parse values of current line
	private static void parseLineHydraulic(Scanner scanner) {
		
		// Parse line
		tokens = new ArrayList<String>();	
		String token = "";
		String first = "";
		boolean firstWord = true;
		while (scanner.hasNext()){
			if (firstWord){
				first = scanner.next();
				firstWord = false;
			}
			else{
				token += " " + scanner.next();
			}
		}
		tokens.add(first.trim());
		tokens.add(token.trim());
		
	}	
	
	
	private static void processTokensHydraulic(RptTarget rpt) {

		String fields = "result_id, time, text";
		String values = "'" + projectName + "', ";
        String time;
        String text = "";
        time = tokens.get(0);
        time = tokens.get(0).substring(0, time.length() - 1);
        if (time.substring(0, 7).toLowerCase().equals("warning")){
        	text = tokens.get(0) + " ";
        	time = lastTimeHydraulic;
        } else{
        	lastTimeHydraulic = time;
        }
        text += tokens.get(1);
		values += "'" + time + "', '" + text + "'";
	
		String sql = "INSERT INTO " + MainDao.getSchema() + "." + rpt.getTable() + " (" + fields + ") VALUES (" + values + ");\n";
		insertSql += sql;
		
	}
	

}