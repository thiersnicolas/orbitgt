package com.orbitgis.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.time.LocalDate;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.orbitgis.entities.Gebruiker;
import com.orbitgis.entities.Kalender;
import com.orbitgis.entities.KalenderEntry;

public class KalenderFileRW {
	public static boolean kalenderNaarExcel(Kalender kalender, String locatie) {
		try (OutputStream os = new FileOutputStream(locatie);
				Workbook wb = new HSSFWorkbook()){
		Sheet sheet = wb.createSheet("OrbitKalender");
		int i=0;
		for (LocalDate datum:kalender.getKalenderMap().keySet()) {
			int y=0;
			if (i==0) {
				Row row = sheet.createRow(i++);
				row.createCell(y++).setCellValue("Datum");
				for (Gebruiker gebruiker:kalender.getGebruikers()) {
					row.createCell(y++).setCellValue(gebruiker.getNaam());
				}
				y=0;
			}
			Row row = sheet.createRow(i++);
			row.createCell(y++).setCellValue(Date.valueOf(datum));
			for (KalenderEntry kalenderEntry:kalender.getKalenderMap().get(datum)) {
				row.createCell(y++).setCellValue(kalenderEntry.getEntry());
			}
		}
		wb.write(os);
		
		return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
