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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileReader.Row;

import com.tecnicsassociats.gvsig.inpcom.util.Utils;


public class ModelDbf extends Model{

	private Map<Integer, File> dbfFiles;

	
    public ModelDbf(String export, String execType) {
    	this.sExport = export;
    	this.execType = execType;
    	init();
    }   	

    
    public ModelDbf() {
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
        
        dbfFiles = new HashMap<Integer, File>();
        
    }
    
    
	// Read content of the DBF file and saved it in an Array
	public ArrayList<LinkedHashMap<String, String>> readDBF(File file) {

		FileChannel in;
		Row row;
		ArrayList<LinkedHashMap<String, String>> mAux = null;
		LinkedHashMap<String, String> mDades;
		try {
			mAux = new ArrayList<LinkedHashMap<String, String>>();
			in = new FileInputStream(file).getChannel();
			DbaseFileReader r = new DbaseFileReader(in);
			int fields = r.getHeader().getNumFields();
			while (r.hasNext()) {
				mDades = new LinkedHashMap<String, String>();
				row = r.readRow();
				for (int i = 0; i < fields; i++) {
					String field = r.getHeader().getFieldName(i).toLowerCase();
					Object oAux = row.read(i);
					String value = oAux.toString();
					mDades.put(field, value);
				}
				mAux.add(mDades);
			}
			r.close();
		} catch (FileNotFoundException e) {
			return mAux;
		} catch (IOException e) {
			return mAux;
		} catch (Exception e){
			Utils.getLogger().warning(e.getMessage());
		}

		return mAux;

	}


	// Main procedure
	public void processAll(String dirOut, String fileOut) {

		try {

			// Get INP output file
			if (dirOut.equals("")){
				dirOut = folderConfig;
			}
			if (fileOut.equals("")){
				fileOut = iniProperties.getProperty(sExport + "INP");
			}
			String sFile = dirOut + File.separator + fileOut;
			File fileInp = new File(sFile);
			
			// Get some properties
			//polygons_target_id = Integer.parseInt(iniProperties.getProperty(sExport + "POLYGONS_TARGET_ID"));
			default_size = Integer.parseInt(iniProperties.getProperty(sExport + "SIZE_DEFAULT"));

			// Open template and output file
			rat = new RandomAccessFile(this.fileTemplate, "r");
			raf = new RandomAccessFile(fileInp, "rw");
			raf.setLength(0);

			// Get content of target table	
			String sql = "SELECT id, name, dbf_id, lines FROM target";
			Statement stat = connectionSqlite.createStatement();
			ResultSet rs = stat.executeQuery(sql);					
			while (rs.next()) {
				processTarget(rs.getInt("id"), rs.getInt("dbf_id"), rs.getInt("lines"));	
			}		    
			rs.close();
			rat.close();
			raf.close();

			// Ending message
			Utils.showMessage("inp_end", fileInp.getAbsolutePath(), "inp_descr");

		} catch (IOException e) {
			Utils.showError("inp_error_io", e.getMessage(), "inp_descr");			
		} catch (SQLException e) {
			Utils.showError("inp_error_execution", e.getMessage(), "inp_descr");			
		}

	}


	// Process target specified by id parameter
	private void processTarget(int id, int fileIndex, int lines) throws IOException, SQLException {

		// Go to the first line of the target
		for (int i = 1; i <= lines; i++) {
			String line = rat.readLine();
			raf.writeBytes(line + "\r\n");
		}

		// If file is null or out of bounds or not exists then exit function
		if (fileIndex < 0){
			return;
		}
		File file = dbfFiles.get(fileIndex);		
		if (file == null || !file.exists()){
			return;
		}

		// Get data of the specified DBF file
		try{
			this.lMapDades = readDBF(file);
		}
		catch (Exception e){
			Utils.getLogger().warning(e.getMessage());
		}
		if (this.lMapDades.isEmpty()) return;		

		// Get DBF fields to write into this target
		mHeader = new LinkedHashMap<String, Integer>();		
		String sql = "SELECT name, space FROM target_fields WHERE target_id = " + id + " ORDER BY pos" ;
		Statement stat = connectionSqlite.createStatement();
		ResultSet rs = stat.executeQuery(sql);			 		
		while (rs.next()) {
			mHeader.put(rs.getString("name").trim().toLowerCase(), rs.getInt("space"));
		}
		rs.close();

		ListIterator<LinkedHashMap<String, String>> it = this.lMapDades.listIterator();
		Map<String, String> m;   // Current DBF row data
		int index = 0;
		String sValor = null;
		int size = 0;
		// Iterate over DBF content
		while (it.hasNext()) {
			m = it.next();
			Set<String> set = mHeader.keySet();
			Iterator<String> itKey = set.iterator();
			// Iterate over fields specified in table target_fields
			while (itKey.hasNext()) {
				String sKey = (String) itKey.next();
				sKey = sKey.toLowerCase();
				size = mHeader.get(sKey);
				// Write to the output file if the field exists in DBF file
				if (m.containsKey(sKey)) {
					sValor = (String) m.get(sKey);
					raf.writeBytes(sValor);
					// Complete spaces with empty values
					for (int j = sValor.length(); j <= size; j++) {
						raf.writeBytes(" ");
					}
				}
				// If key doesn't exist write empty spaces
				else{
					for (int j = 0; j <= size; j++) {
						raf.writeBytes(" ");
					}					
				}
			}
            raf.writeBytes("\r\n");			
		}

	}


	// Check all the necessary files to run the process
	public boolean checkFiles(String sDirShp, String sDirOut) {

		// Get INP template file
		String sFile = iniProperties.getProperty(sExport + "TEMPLATE");
		sFile = folderConfig + File.separator + sFile;
		fileTemplate = new File(sFile);
		if (!fileTemplate.exists()) {
			Utils.showError("inp_error_notfound", sFile, "inp_descr");				
			return false;
		}
		
		String sql = "SELECT id, name FROM dbf WHERE id > -1 ORDER BY id";
		try {
			Statement stat = connectionSqlite.createStatement();
			ResultSet rs = stat.executeQuery(sql);		
			while (rs.next()) {
				String sDBF = sDirShp + File.separator + rs.getString("name").trim() + ".dbf";
				dbfFiles.put(rs.getInt("id"), new File(sDBF));
			}
			rs.close();
		} catch (SQLException e) {
			Utils.showError("inp_error_execution", e.getMessage(), "inp_descr");				
			return false;	
		}				

		return true;

	}

}