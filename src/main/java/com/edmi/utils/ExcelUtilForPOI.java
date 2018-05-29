package com.edmi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
     * 注意：由于项目需要导入的文件数据量不多，
     * 所以我调用上述read方法时是一次将整个文件解析并存放在一个集合中，
     * 然后再对这些数据进行后续操作。
     * 如果各位大拿有大数据量的文件要解析导入最好分批次解析导入，
     * 否则很可能发生内存溢出的情况
     * ExcelUtilForPOI.java
     * @author guowm
     * @version 1.0 2016-1-11 下午01:55:16
     *<p>Hey:As I'm young, there's no such thing as failure.</p>
     */
@Component
public class ExcelUtilForPOI {

        private static DecimalFormat decimalF = new DecimalFormat("#.##");
        private static DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd");

        static Logger log = Logger.getLogger(ExcelUtilForPOI.class);

        /**
         * HSSFWorkbook解析excel 2003以及2003以下版本
         * @param hssfWorkbook 解析文件对象
         * @return 解析后得到存储在List容器中的数据结果集
         */
        private  List<String[]> readHSSFWordbook(HSSFWorkbook hssfWorkbook) {
            //存储数据容器
            List<String[]> list = new ArrayList<String[]>();
            FileInputStream is = null;
            POIFSFileSystem fs = null;
            HSSFSheet sheet = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            String tmpstr = null;
//      boolean iftrue = false;
            try {
//          is = new FileInputStream(file);
//          fs = new POIFSFileSystem(is);//具有关闭输入流的功能
//          hssfWorkbook = new HSSFWorkbook(fs);
                sheet = hssfWorkbook.getSheetAt(0);
                //上传excel的最大行数值
                for (int i = 0; i < sheet.getLastRowNum(); i++) {
                    row = sheet.getRow(i);
                    if (row != null) {
                        //用来存储每行数据的容器
                        String[] model = new String[row.getLastCellNum()];
                        //上传excel的最大列值
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            cell = row.getCell(j);
                            //if(j == 0) continue;//第一列ID为标志列，不解析
                            if (cell != null) {
                                /**
                                 * CellType 类型
                                 * 0 CELL_TYPE_NUMERIC 数值型
                                 * 1 CELL_TYPE_STRING 字符串型
                                 * 2 CELL_TYPE_FORMULA 公式型
                                 * 3 CELL_TYPE_BLANK 空值
                                 * 4 CELL_TYPE_BOOLEAN 布尔型
                                 * 5 CELL_TYPE_ERROR 错误
                                 */
                                if (cell.getCellType() == 0) {
                                    //区分处理日期类型和数值
                                    if(HSSFDateUtil.isCellDateFormatted(cell)){
                                        Date date = cell.getDateCellValue();
                                        tmpstr = dateF.format(date);
                                    } else {
                                        tmpstr = decimalF.format(cell.getNumericCellValue());
                                    }
                                } else {
                                    tmpstr = cell.getStringCellValue().trim();
                                }
                            } else {
                                System.err.println("第一列ID不能为空");
                                tmpstr = "";
                            }
                            //数据放入数组
                            model[j] = tmpstr;
                        }
                        //model放入list容器中
                        list.add(model);
                    } else {
                        log.info("该行为空行或者表格中无数据");
                    }
                }
                //上传完成后删除源文件
//          iftrue = file.delete();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("解析失败!");
                return null;
            }
            return list;
        }

        /**
         * XSSFWorkbook解析excel 2007以及2007以上版本
         * @param xssfWorkbook 解析文件对象
         * @return 解析后得到存储在List容器中的数据结果集
         */
        private  List<String[]> readXSSFWordbook(XSSFWorkbook xssfWorkbook) {
            //存储数据容器
            List<String[]> list = new ArrayList<String[]>();

            try {
                XSSFSheet sheet = null;
                XSSFRow row = null;
                XSSFCell cell = null;
                String tmpstr = null;

                sheet = xssfWorkbook.getSheetAt(0);//读取第一个sheet

                int rowNum = 0;
                for(Iterator<?> rows = sheet.iterator(); rows.hasNext(); rowNum++ ){
                    row = (XSSFRow) rows.next();
                    if(rowNum == 0)
                        continue;//从第二行开始读取，不读取表头
                    if(row != null){
                        int columIndex = 0;
                        int lastCellNum = row.getLastCellNum();
                        String[] aCells = new String[lastCellNum];

                        while (columIndex < lastCellNum) {
                            cell = row.getCell(columIndex);
                            if(cell != null){
                                /*if(columIndex == 0) {//第一列ID为标志列，不解析
                                    columIndex++;
                                    continue;
                                }*/
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        if(HSSFDateUtil.isCellDateFormatted(cell)){
                                            Date date = cell.getDateCellValue();
                                            tmpstr = dateF.format(date);
                                        } else {
                                            tmpstr = decimalF.format(cell.getNumericCellValue());
                                        }
                                        break;
                                    default:
                                        tmpstr = cell.getStringCellValue().trim();
                                        break;
                                }
                            } else {
                             /*   if(columIndex == 0) {
                                    System.err.println("第一列ID不能为空");
                                    return null;
                                }
                                tmpstr = "";*/
                            }
                            aCells[columIndex] = tmpstr;
                            columIndex++;
                        }
                        list.add(aCells);
                    }else {
                        System.err.println("该行为空行或者表格中无数据");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("解析失败!");
                return  null;
            }
            return list;
        }


        /**
         * 获取Workbook ,是 HSSFWorkbook和XSSFWorkbook的父接口
         * @param file 文件对象
         * @return workbook要解析的Excel文件对象
         */
        public Workbook getWorkBook(File file){
            InputStream inp = null;
            Workbook wb = null;
            try {
                inp = new FileInputStream(file);
                wb  = WorkbookFactory.create(inp);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (EncryptedDocumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if(inp != null)
                    try {
                        inp.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }
            return wb;
        }

        /**
         * 工具类入口
         * @param fileURL 文件地址
         * @return 解析完成，包含解析数据的容器
         */
        public List<String[]> getData(String fileURL){
            Workbook wb = null;
            File file = new File(fileURL);

            if(file.exists()){
                wb = getWorkBook(file);
            } else {
                System.err.println("读取文件不存在");
            }
            // 根据上传excel的文件头信息，判断上传excel版本
            try {
                HSSFWorkbook hssfWorkbook = (HSSFWorkbook) wb;
                log.info("2003以及2003以下版本");
                return readHSSFWordbook(hssfWorkbook);
            } catch (Exception e) {
                XSSFWorkbook xssfWorkbook = (XSSFWorkbook) wb;
                log.info("2007以及2007以上版本");
                return readXSSFWordbook(xssfWorkbook);
            }
        }

        public static void main(String[] args) {
            ExcelUtilForPOI poiTest = new ExcelUtilForPOI();

            String fileURL = "D:\\projects\\manage_plantform\\src\\main\\resources\\世界各国代码.xls";
            List<String[]> list = poiTest.getData(fileURL);
            if(list != null&&list.size() != 0){
                for (int i = 0; i < list.size(); i++) {
                    String[] strObject = list.get(i);
                    for (int j = 0; j < strObject.length; j++) {
                        System.out.print(strObject[j]+"\t");
                    }
                    System.out.println("");
                }
            }
        }

}
