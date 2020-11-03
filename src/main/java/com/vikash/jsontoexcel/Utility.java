package com.vikash.jsontoexcel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Utility {

	static Utility instance;

	public static Utility getInstance() {
		if(instance==null) {
			instance=new Utility();
		}
		return instance;
	}


	private ArrayList<ServerBean> creteData(String fileName) throws IOException, InterruptedException {


		ArrayList<ServerBean> serverbean=null;serverbean=new ArrayList<>();
		InputStream fis=new FileInputStream(fileName);
		JsonReader jsonReader=Json.createReader(fis);
		System.out.println("******** Reading Json ********");
		JsonObject jsonObject=jsonReader.readObject();
		jsonReader.close();
		fis.close();
		Set<String> set=jsonObject.keySet();
		for (String key:set) {
			JsonObject obj=jsonObject.getJsonObject(key);
			JsonArray context=obj.getJsonArray("context");
			JsonArray size=obj.getJsonArray("size");

			String [] contextArray=new String[context.size()];
			String [] sizeArray=new String[size.size()];
			int indexContext=0;
			int indexSize=0;
			for(JsonValue value:context) {
				contextArray[indexContext++]=String.valueOf(value).replaceAll("^\"+|\"+$", "");
			}
			for(JsonValue value:size) {
				sizeArray[indexSize++]=String.valueOf(getSize(Long.valueOf(value.toString())));
			}



			serverbean.add(new ServerBean(key,contextArray,sizeArray));
		}
		return  serverbean;
	}

	public void writeToExcel(String filename,String [] columns) throws IOException, InterruptedException  {
		@SuppressWarnings("resource")
		Workbook workbook=new XSSFWorkbook();
		Sheet sh=workbook.createSheet("Transformed by JSON-CSV.CO");
		Font headerFont=workbook.createFont();
		headerFont.setFontName("Arial");
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short)12);
		headerFont.setColor(IndexedColors.BLACK.index);

		// column style

		CellStyle headeStyle=workbook.createCellStyle();
		headeStyle.setFont(headerFont);
		headeStyle.setBorderBottom(BorderStyle.THIN);
		headeStyle.setBorderLeft(BorderStyle.THIN);
		headeStyle.setBorderRight(BorderStyle.THIN);
		headeStyle.setBorderTop(BorderStyle.THIN);
		headeStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
		headeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		//create column header

		Row headerRow=sh.createRow(0);
		for(int i=0;i<columns.length;i++) {
			Cell cell=headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headeStyle);
		}
		ArrayList<ServerBean> serverData=creteData(filename);
		int rownum=1;  


		// write all data to excel
		for (ServerBean serverBean:serverData) { 
			System.out.println("******** "+serverBean.getServerName()+" --->  Dump To Excel ********");
			Row row=sh.createRow(rownum);
			row.createCell(0).setCellValue(serverBean.getServerName());
			if(serverBean.getPath().length!=0 && serverBean.getSize().length!=0 ) {
				for(int i=0;i<serverBean.getPath().length;i++) {
					row.createCell(1).setCellValue(serverBean.getPath()[i]); 
					row.createCell(2).setCellValue(serverBean.getSize()[i]);
					row=sh.createRow(++rownum);
				}
			}else {
				row.createCell(1).setCellValue(""); 
				row.createCell(2).setCellValue("");
			}
			row=sh.createRow(++rownum);
		}

		// column size according to value

		for (int i=0;i<columns.length;i++) {
			sh.autoSizeColumn(i);

		}
		System.out.println();
		System.out.println("-------------- Completed --------------------");
		FileOutputStream fout=new FileOutputStream(System.getProperty("user.dir")+"/result.xlsx");
		workbook.write(fout);
		fout.close();

	}

	public static String getSize(long size) {
		long n = 1024L;
		String s = "";
		double kb =(double) size / n;
		double mb = (double)size /n;
		double gb = mb / n;
		double tb =gb / n;
		if(size < n) {
			s = size + " Bytes";
		} else if(size >= n && size < (n * n)) {
			s =  String.format("%.2f", kb) + " KB";
		} else if(size >= (n * n) && size < (n * n * n)) {
			s = String.format("%.2f", mb) + " MB";
		} else if(size >= (n * n * n) && size < (n * n * n * n)) {
			s = String.format("%.2f", gb) + " GB";
		} else if(size >= (n * n * n * n)) {
			s = String.format("%.2f", tb) + " TB";
		}
		return s;
	}
}
