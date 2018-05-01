package examprep.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileProcessor {

    static final Logger logger = Logger.getLogger(ExcelFileProcessor.class.getName());

    public List<ArrayList<String>> read(File excelFile) {

        try {

            FileInputStream fs = new FileInputStream(excelFile);

            String fileExt = excelFile.getName().substring(excelFile.getName().indexOf("."));

            logger.log(Level.INFO, "file extenstion: {0}", fileExt);

            Workbook wb = null;
            Sheet sheet;
            Iterator<Row> rowIterator;

            switch (fileExt) {
                case ".xls":
                    wb = new HSSFWorkbook(new POIFSFileSystem(fs));
                    break;
                case ".xlsx":
                    wb = new XSSFWorkbook(fs);
                    break;
                default:
                    logger.info("Wrong File Type");
                    break;
            }

            sheet = wb.getSheetAt(0);
            rowIterator = sheet.iterator();
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            List<ArrayList<String>> list = new ArrayList();
            int rowNum = 0;
            while (rowIterator.hasNext()) {
                logger.log(Level.INFO, "row read {0}", rowNum++);
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                ArrayList<String> s = new ArrayList();
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    Cell cell = row.getCell(cn);
                    if (cell == null) {
                        s.add("");
                        continue;
                    }
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            logger.log(Level.INFO, "column: {0}, value: {1}", new Object[]{cell.getColumnIndex(), cell.getNumericCellValue()});
                            s.add(cell.getNumericCellValue() + "");
                            break;

                        case Cell.CELL_TYPE_STRING:
                            logger.log(Level.INFO, "column: {0}, value: {1}", new Object[]{cell.getColumnIndex(), cell.getStringCellValue()});
                            s.add(cell.getStringCellValue());
                            break;

                        case Cell.CELL_TYPE_FORMULA:
                            CellValue cv = evaluator.evaluate(cell);

                            if (cv.getCellType() == Cell.CELL_TYPE_STRING) {
                                logger.log(Level.INFO, "column: {0}, value: {1}", new Object[]{cell.getColumnIndex(), cell.getStringCellValue()});
                                s.add(cell.getStringCellValue());
                            }
                            else if (cv.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                logger.log(Level.INFO, "column: {0}, value: {1}", new Object[]{cell.getColumnIndex(), cell.getNumericCellValue()});
                                s.add(cell.getNumericCellValue() + "");
                            }

                            break;

                    }
                }

                list.add(s);
            }

            fs.close();

            return list;
        }
        catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }

    }
}
