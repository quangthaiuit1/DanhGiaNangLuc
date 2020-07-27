package trong.lixco.com.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.UploadedFile;

import trong.lixco.com.jpa.entity.ChiTietNangLuc;
import trong.lixco.com.jpa.entity.NangLuc;

public class ToExcel {

	public static void writeXLSX(List<Object[]> params, ServletOutputStream svl) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("Sheet1");
		Row row;
		Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
		for (int i = 0; i < params.size(); i++) {
			empinfo.put((i + 1) + "", params.get(i));
		}
		Set<String> keyid = empinfo.keySet();
		int rowid = 0;
		SimpleDateFormat fd = new SimpleDateFormat("dd-MM-yyyy");
		for (String key : keyid) {
			row = spreadsheet.createRow(rowid++);
			Object[] objectArr = empinfo.get(key);
			int cellid = 0;
			for (Object obj : objectArr) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue(obj + "");
				if (obj != null && obj.getClass().equals(Double.class)) {
					cell.setCellValue((double) obj);
				} else if (obj != null && obj.getClass().equals(Date.class)) {

					cell.setCellValue(fd.format((Date) obj));
				} else {
					cell.setCellValue(obj + "");
				}
			}
		}
		workbook.write(svl);
	}


	public static List<NangLuc> docexcelnangluc(UploadedFile uploadedFile) {
		try {
			List<NangLuc> nangLucs=new ArrayList<NangLuc>();
			InputStream input = uploadedFile.getInputstream();
			// Get the workbook instance for XLS file
			XSSFWorkbook workbook = new XSSFWorkbook(input);
			// Get first sheet from the workbook
			for (int p = 0; p < workbook.getNumberOfSheets(); p++) {
				Sheet sheet = workbook.getSheetAt(p);

				// Iterate through each rows from first sheet
				Row row;
				int tam = -1;
				// Chay danh sach tu 1 -> dong cuoi cung
				int numrow = sheet.getLastRowNum();
				String manpp = "";
				for (int i = 0; i <= numrow; i++) {
					row = sheet.getRow(i);
					if (tam == -1) {
						// Vi tri bat dau la " Ngay"
						for (int j = 0; j <= numrow; j++) {
							try {
								if (row.getCell(j).getStringCellValue().equalsIgnoreCase("Mã năng lực")) {
									tam = j;
									break;
								}
							} catch (Exception e) {
							}
						}
					} else {
						// Bat dau lay gia tri
						String ma = "", ten = "", dinhnghia = "", toithieu = "", coban = "", datyeucau = "", thanhthao = "", xuatsac = "";
						try {
							ma = row.getCell(tam).getStringCellValue();
						} catch (Exception e) {
						}
						try {
							ten = row.getCell(tam + 1).getStringCellValue();
						} catch (Exception e) {
						}
						try {
							dinhnghia = row.getCell(tam + 2).getStringCellValue();
						} catch (Exception e) {
						}
						try {
							toithieu = row.getCell(tam + 3).getStringCellValue();
						} catch (Exception e) {
						}
						try {
							coban = row.getCell(tam + 4).getStringCellValue();
						} catch (Exception e) {
						}
						try {
							datyeucau = row.getCell(tam + 5).getStringCellValue();
						} catch (Exception e) {
						}
						try {
							thanhthao = row.getCell(tam + 6).getStringCellValue();
						} catch (Exception e) {
						}
						try {
							xuatsac = row.getCell(tam + 7).getStringCellValue();
						} catch (Exception e) {
						}
						List<ChiTietNangLuc> cts = new ArrayList<ChiTietNangLuc>();
						ChiTietNangLuc ct1 = new ChiTietNangLuc();
						ct1.setCapdo(1);
						ct1.setNoidung(toithieu);
						cts.add(ct1);
						ChiTietNangLuc ct2 = new ChiTietNangLuc();
						ct2.setCapdo(2);
						ct2.setNoidung(coban);
						cts.add(ct2);
						ChiTietNangLuc ct3 = new ChiTietNangLuc();
						ct3.setCapdo(3);
						ct3.setNoidung(datyeucau);
						cts.add(ct3);
						ChiTietNangLuc ct4 = new ChiTietNangLuc();
						ct4.setCapdo(4);
						ct4.setNoidung(thanhthao);
						cts.add(ct4);
						ChiTietNangLuc ct5 = new ChiTietNangLuc();
						ct5.setCapdo(5);
						ct5.setNoidung(xuatsac);
						cts.add(ct5);
							
							NangLuc nl = new NangLuc();
							nl.setMa(ma);
							nl.setTen(ten);
							nl.setDinhnghia(dinhnghia);
							nl.setChiTietNangLucs(cts);
							nangLucs.add(nl);
						
					}
				}
			}
			input.close();
			return nangLucs;
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<NangLuc>();
		}
	}

	public static double formatNumber(int sole, double value) {
		BigDecimal bd = new BigDecimal(value);
		BigDecimal bd2 = bd.setScale(sole, BigDecimal.ROUND_HALF_UP);

		return bd2.doubleValue();
	}

	public static double formatNumber(double value) {
		BigDecimal bd = new BigDecimal(value);
		BigDecimal bd2 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

		return bd2.doubleValue();
	}

	public static void main(String[] args) {
		String a= "A1001";
		System.out.println(a.substring(0, 1));
	}

}
