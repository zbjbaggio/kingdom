package com.kingdom.system.util.excel;

import com.kingdom.system.util.ChanelContext;
import com.kingdom.system.util.ReflectUtil;
import com.kingdom.system.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 功能：用于97-2003之间（.xls）
 */
public class HssfExcelUtil extends ExcelUtil {

    private final static String FILE_TYPE = ".xls" ;

    private final static String PASSWORD = "1233211234567" ;

    private static HSSFCellStyle contentStyleDefault;

    private static HSSFCellStyle contentStyleDate;

    private static HSSFCellStyle contentStyleDouble;

    // sheet 多少条数据
    private final static Integer XLS_MAX_ROW = 50000;

    private final static String MESSAGE = "请选择或输入有效的选项！" ;

    // 列数
    private int columnCount;

    // 行数
    private int rowCount;

    HSSFWorkbook workbook;

    public HssfExcelUtil(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public HssfExcelUtil() {
        columnCount = 0;
        rowCount = 0;
        workbook = new HSSFWorkbook();
    }

    public <T> List<T> readExcel(Class<T> clazz, int sheetNo, boolean hasTitle) throws Exception {
        return readExcel(clazz, sheetNo, hasTitle, "");
    }

    @Override
    public <T> List<T> readExcel(Class<T> clazz, int sheetNo, boolean hasTitle, String group) throws Exception {
        List<T> dataModels = new ArrayList<>();
        // 获取excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(sheetNo);
        int start = sheet.getFirstRowNum() + (hasTitle ? 1 : 0); // 如果有标题则从第二行开始
        String[] fieldNames = getFile(clazz, group);
        List<String> andNullFiled = new ArrayList<>();
        //获取检查字段名称
        for (String fieldName : fieldNames) {
            if (clazz.getDeclaredField(fieldName).getAnnotation(ExcelImport.class).isAndNull()) {
                andNullFiled.add(fieldName);
            }
        }
        for (int i = start; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            // 生成实例并通过反射调用setter方法
            T target = clazz.newInstance();
            for (int j = 0; j < fieldNames.length; j++) {
                String fieldName = fieldNames[j];
                if (fieldName == null || SERIALVERSIONUID.equals(fieldName)) {
                    continue; // 过滤serialVersionUID属性
                }
                // 获取excel单元格的内容
                HSSFCell cell = row.getCell(j);
                if (cell == null || cell.toString().length() == 0) {
                    continue;
                }
                // 如果属性是日期类型则将内容转换成日期对象
                if (isDateType(clazz, fieldName)) {
                    Date date = null;
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double d = cell.getNumericCellValue();
                        date = HSSFDateUtil.getJavaDate(d);
                    }
                    // 如果属性是日期类型则将内容转换成日期对象
                    ReflectUtil.invokeSetter(target, fieldName, com.kingdom.system.util.DateUtil.formatDate(date));
                } else {
                    cell.setCellType(CellType.STRING);
                    String content = cell.getStringCellValue();
                    Field field = clazz.getDeclaredField(fieldName);
                    ReflectUtil.invokeSetter(target, fieldName, parseValueWithType(content, field.getType()));
                }
            }
            //检查是否读取该条数据
            if (checkIsAddList(target, andNullFiled)) {
                dataModels.add(target);
            }
        }
        return dataModels;
    }

    private <T> boolean checkIsAddList(T target, List<String> andNullFiled) throws Exception {
        if (andNullFiled.size() > 0) {
            for (String fieldName : andNullFiled) {
                Object o = ReflectUtil.invokeGetter(target, fieldName);
                if (o != null) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public <T> void writeExcel(HttpServletResponse response, String filename, String sheetName, List<T> list) throws Exception {
        writeExcel(response, filename, sheetName, list, "", "");
    }

    @Override
    public <T> void writeExcel(HttpServletResponse response, String filename, String sheetName, List<T> list, String groupName, String password) throws Exception {
        // reponse init
        response.setContentType("octets/stream");
        response.addHeader("Content-Type", "octets/stream; charset=utf-8");
        filename = new String(filename.getBytes("UTF-8"), "iso8859-1");
        response.addHeader("Content-Disposition", "attachment;filename=" + filename + FILE_TYPE);
        OutputStream outputStream = response.getOutputStream();
        // 声明一个工作薄
        contentStyleDefault = null;
        contentStyleDate = null;
        contentStyleDouble = null;
        List<Object> fields = new ArrayList<>();
        boolean isLock = getHeadName(list.get(0).getClass(), fields, groupName);
        setSheet(sheetName, list, fields, isLock);
        if (!StringUtils.isEmpty(password)) {
            Biff8EncryptionKey.setCurrentUserPassword(password);
        }
        try {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
        }
    }

    private <T> void setSheet(String sheetName, List<T> list, List<Object> fields, boolean isLock) throws Exception {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 表头样式
        HSSFCellStyle headerStyle = getHeadStyle(workbook);
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        if (isLock) {
            sheet.protectSheet(PASSWORD);
        }
        Map<String, List<String>> parentMap = new HashMap<>();
        setName(workbook, fields, sheet, headerStyle, row, parentMap);
        rowCount++;
        getList(list, fields, sheet, 0, null, true);
        //目前只展示第一个sheet
        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 1; i < sheetCount; i++) {
            workbook.setSheetHidden(i, true);
            HSSFSheet hiddenSheet = workbook.getSheetAt(i);
            hiddenSheet.protectSheet(PASSWORD);
        }
    }

    private void setName(HSSFWorkbook workbook, List<Object> fields, HSSFSheet sheet, HSSFCellStyle headerStyle, HSSFRow row,
                         Map<String, List<String>> parentMap) throws Exception {
        HSSFDataFormat format = workbook.createDataFormat();
        HSSFRichTextString text;
        for (Object object : fields) {
            if (object instanceof FieldObject) {
                FieldObject fieldObject = (FieldObject) object;
                HSSFCell cell = row.createCell(columnCount);
                cell.setCellStyle(headerStyle);
                text = new HSSFRichTextString(fieldObject.getTitle());
                cell.setCellValue(text);
                //设置列宽度自适应
                if (fieldObject.getWidth() == 0) {
                    sheet.setColumnWidth(columnCount, fieldObject.getTitle().getBytes().length * 700);
                } else {
                    sheet.setColumnWidth(columnCount, fieldObject.getWidth());
                }
                sheet.setDefaultColumnStyle(columnCount, getCellStyle(workbook, sheet, format, fieldObject, parentMap));
                columnCount++;
            } else if (object instanceof FieldList) {
                setName(workbook, ((FieldList) object).getFields(), sheet, headerStyle, row, parentMap);
            }
        }
    }

    private <T> void getList(List<T> list, List<Object> fields, HSSFSheet sheet, int columnCount, HSSFRow row, boolean isMerge) {
        for (T t : list) {
            // 正文内容样式
//            HSSFCell cell = row.createCell(rowCount);
//            cell.setCellStyle(contentStyle);
            int oldRowCount = setRowValue(sheet, t, fields, columnCount, row);
            if (isMerge) {
                int lastFileListIndex = 0;
                for (int i = fields.size() - 1; i >= 0; i--) {
                    if (fields.get(i) instanceof FieldList) {
                        lastFileListIndex = i;
                        break;
                    }
                }
                if (lastFileListIndex != 0 && oldRowCount != rowCount) {
                    CellRangeAddress region = new CellRangeAddress(oldRowCount, rowCount, this.columnCount - lastFileListIndex , this.columnCount -  1);
                    sheet.addMergedRegion(region);
                }
            }
            row = null;
            rowCount++;
        }

    }

    // 填写sheet的每行的值
    private int setRowValue(HSSFSheet sheet, Object obj, List<Object> fields, int columnCount, HSSFRow row) {
        int firstCol = 0;
        if (row == null) {
            row = sheet.createRow(rowCount);
        }
        Class<?> c = obj.getClass();
        Object value;
        PropertyDescriptor pd;
        int oldRowCount = rowCount;
        for (int i = 0; i < fields.size(); i++) {
            try {
                // cell.setCellStyle(getCellStyle(workbook, sheet, format, fieldObject, parentMap));
/*                HSSFCellStyle cellStyle = cell.getCellStyle();
                cellStyle.setLocked(fields.get(i).getLockBoolean());*/
                Object object = fields.get(i);
                if (object instanceof FieldObject) {
                    HSSFCell cell = row.createCell(columnCount + i);
                    FieldObject fileObject = (FieldObject) object;
                    pd = new PropertyDescriptor(fileObject.getName(), c);
                    Method getMethod = pd.getReadMethod();// 获得get方法
                    value = getMethod.invoke(obj);
                    if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    } else if (value instanceof Date) {
                        cell.setCellValue((Date) value);
                    } else {
                        cell.setCellValue(value == null ? "" : value.toString());
                    }
                } else if (object instanceof FieldList) {
                    FieldList fieldObject = (FieldList) object;
                    pd = new PropertyDescriptor(fieldObject.getName(), c);
                    Method getMethod = pd.getReadMethod();// 获得get方法
                    List valueList = (List)getMethod.invoke(obj);
                    getList(valueList, fieldObject.getFields(), sheet, i, row, false);
                    columnCount = columnCount + fieldObject.getFields().size() - 1;
                    rowCount--;
                    if (oldRowCount != rowCount) {
                        CellRangeAddress region = new CellRangeAddress(oldRowCount, rowCount, firstCol, i - 1);
                        sheet.addMergedRegion(region);
                        firstCol = columnCount;
                    }
                    //row = sheet.createRow(rowCount++);
                }
                //sheet.autoSizeColumn(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return oldRowCount;
    }

    private CellStyle getCellStyle(HSSFWorkbook workbook, HSSFSheet sheet, HSSFDataFormat format, FieldObject fileObject, Map<String, List<String>> parentMap) throws Exception {
        ExcelDataEnums enums = fileObject.getFormat();
        switch (enums) {
            case YYYYMM:
                if (contentStyleDate == null) {
                    contentStyleDate = getContentStyle(workbook);
                    contentStyleDate.setDataFormat(format.getFormat("yyyy-mm"));
                }
                return contentStyleDate;
            case DOUBLE:
                if (contentStyleDouble == null) {
                    contentStyleDouble = getContentStyle(workbook);
                    contentStyleDouble.setDataFormat(format.getFormat("0.00"));
                }
                return contentStyleDouble;
            case SELECT:
                if (contentStyleDefault == null) {
                    contentStyleDefault = getContentStyle(workbook);
                }
                if (CollectionUtils.isEmpty(fileObject.getSelects()) && fileObject.getReturnSelectDataClass().isInterface()
                        && fileObject.getReturnSelectMapClass().isInterface()) {
                    throw new Exception("该类没有实现接口!");
                }
                // 不需要联动的下拉框
                if (!fileObject.getReturnSelectDataClass().isInterface() || !CollectionUtils.isEmpty(fileObject.getSelects())) {
                    if (!fileObject.getReturnSelectDataClass().isInterface()) {
                        String className = fileObject.getReturnSelectDataClass().getSimpleName();
                        ExcelSelectInterface bean = (ExcelSelectInterface) ChanelContext.getBean(className.substring(0, 1).toLowerCase() + className.substring(1));
                        List<String> selects = bean.returnSelectData();
                        if (selects == null || selects.size() == 0) {
                            throw new Exception("选项中没有数据!");
                        }
                        fileObject.setSelects(selects);
                    }
                    writeSelects(workbook, fileObject);
                    parentMap.put(fileObject.getName(), fileObject.getSelects());
                } else if (!fileObject.getReturnSelectMapClass().isInterface() && !StringUtils.isEmpty(fileObject.getParentName())) {
                    String className = fileObject.getReturnSelectMapClass().getSimpleName();
                    ExcelSelectMapInterface bean = (ExcelSelectMapInterface) ChanelContext.getBean(className.substring(0, 1).toLowerCase() + className.substring(1));
                    Map<String, List<String>> map = bean.returnSelectMapData();
                    if (map == null || map.size() == 0) {
                        throw new Exception("选项中没有数据!");
                    }
                    fileObject.setMap(map);
                    writeSelectsMap(workbook, fileObject, parentMap);
                } else {
                    throw new Exception("属性设置有误!");
                }
                initSheetNameMapping(sheet, fileObject);
                return contentStyleDefault;
            default:
                if (contentStyleDefault == null) {
                    contentStyleDefault = getContentStyle(workbook);
                }
                return contentStyleDefault;
        }
    }

    private void writeSelects(HSSFWorkbook workbook, FieldObject fileObject) {
        HSSFSheet sheet = workbook.createSheet(fileObject.getName());
        List<String> selects = fileObject.getSelects();
        for (int i = 0; i < selects.size(); i++) {
            HSSFRow row = sheet.createRow(i);
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(selects.get(i));
        }
        Name name = workbook.createName();
        name.setNameName(fileObject.getName());
        name.setRefersToFormula(sheet.getSheetName() + "!$A$1:$A$" + selects.size());
    }

    private void writeSelectsMap(HSSFWorkbook workbook, FieldObject fileObject, Map<String, List<String>> parentMap) throws Exception {
        String parentName = fileObject.getParentName();
        List<String> parentList = parentMap.get(parentName);
        if (CollectionUtils.isEmpty(parentList)) {
            throw new Exception("联动parent list未找到！");
        }
        HSSFSheet wsSheet = workbook.getSheet(parentName);
        for (int i = 0; i < parentList.size(); i++) {
            int referColNum = i + 1;
            String parent = parentList.get(i);
            int rowCount = wsSheet.getLastRowNum();
            Map<String, List<String>> map = fileObject.getMap();
            List<String> sub = map.get(parent);
            if (!CollectionUtils.isEmpty(sub)) {
                for (int j = 0; j < sub.size(); j++) {
                    if (j <= rowCount) { //前面创建过的行，直接获取行，创建列
                        wsSheet.getRow(j).createCell(referColNum).setCellValue(sub.get(j)); //设置对应单元格的值
                    } else { //未创建过的行，直接创建行、创建列
                        wsSheet.setColumnWidth(j, 4000); //设置每列的列宽
                        //创建行、创建列
                        wsSheet.createRow(j).createCell(referColNum).setCellValue(sub.get(j)); //设置对应单元格的值
                    }
                }
                Name name = workbook.createName();
                name.setNameName(parent);
                String referColName = getColumnName(referColNum);
                String formula = parentName + "!$" + referColName + "$1:$" + referColName + "$" + sub.size();
                name.setRefersToFormula(formula);
            }
        }
    }

    /**
     * 根据数据值确定单元格位置（比如：0-A, 27-AB）
     *
     * @param index
     * @return
     */
    private String getColumnName(int index) {
        StringBuilder s = new StringBuilder();
        while (index >= 26) {
            s.insert(0, (char) ('A' + index % 26));
            index = index / 26 - 1;
        }
        s.insert(0, (char) ('A' + index));
        return s.toString();
    }

    private void initSheetNameMapping(HSSFSheet mainSheet, FieldObject fileObject) {
        DataValidation warehouseValidation = getDataValidationByFormula(fileObject);
        // 主sheet添加验证数据
        mainSheet.addValidationData(warehouseValidation);
    }

    private DataValidation getDataValidationByFormula(FieldObject fileObject) {
        // 加载下拉列表内容
        DVConstraint constraint;
        if (fileObject.getName().equals("term")) {
            constraint = DVConstraint.createFormulaListConstraint("INDIRECT($A1)");
        } else {
            constraint = DVConstraint.createFormulaListConstraint(fileObject.getName());
        }

        // 设置数据有效性加载在哪个单元格上。
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(1, XLS_MAX_ROW, fileObject.getIndex(), fileObject.getIndex());
        // 数据有效性对象
        DataValidation dataValidationList = new HSSFDataValidation(regions, constraint);
        dataValidationList.createErrorBox("Error", MESSAGE);
        if (StringUtils.isEmpty(fileObject.getPointOut())) {
            dataValidationList.createPromptBox("提示", MESSAGE);
        } else {
            dataValidationList.createPromptBox("提示", fileObject.getPointOut());
        }
        return dataValidationList;
    }

    //基本样式
    private HSSFCellStyle getCellBaseStyle(HSSFWorkbook workbook) {
        HSSFCellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderLeft(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);
        contentStyle.setBorderTop(BorderStyle.THIN);
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        return contentStyle;
    }

    // 抬头样式
    private HSSFCellStyle getHeadStyle(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = getCellBaseStyle(workbook);
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);
        return headerStyle;
    }

    //内容样式
    private HSSFCellStyle getContentStyle(HSSFWorkbook workbook) {
        HSSFCellStyle contentStyle = getCellBaseStyle(workbook);
        contentStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFFont font = workbook.createFont();
        font.setBold(false);
        font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        contentStyle.setFont(font);
        return contentStyle;
    }

    // 获取标有excel注解的属性, 得到字段的中文和字段名称,返回是否需要锁定
    private boolean getHeadName(Class<?> c, List<Object> fields, String groupName) {
        boolean isLock = false;
        Field[] allFields = c.getDeclaredFields();
        int index = 0;
        for (Field field : allFields) {
            if (!field.isAnnotationPresent(ExcelExport.class)) {
                continue;
            }
            ExcelExport excel = field.getAnnotation(ExcelExport.class);
            FieldObject fieldObject;
            if ("".equals(groupName) || groupName == null || "".equals(excel.group()[0]) || ArrayUtils.contains(excel.group(), groupName)) {
                fieldObject = new FieldObject();
                if (excel.lockBoolean()) {
                    isLock = true;
                }
                if (field.getType().equals(List.class)) {
                    Type genericType = field.getGenericType();
                    ParameterizedType pt = (ParameterizedType) genericType;
                    Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                    List<Object> childFields = new ArrayList<>();
                    getHeadName(actualTypeArgument, childFields, groupName);
                    FieldList fieldList = new FieldList();
                    fieldList.setName(field.getName());
                    fieldList.setFields(childFields);
                    fields.add(fieldList);
                    continue;
                }
                fieldObject.setIndex(index++);
                fieldObject.setLockBoolean(excel.lockBoolean());
                fieldObject.setName(field.getName());
                fieldObject.setFormat(excel.format());
                fieldObject.setSelects(Arrays.asList(excel.constant()));
                fieldObject.setReturnSelectDataClass(excel.returnSelectDataClass());
                fieldObject.setReturnSelectMapClass(excel.returnSelectMapClass());
                fieldObject.setParentName(excel.parentName());
                fieldObject.setPointOut(excel.pointOut());
                fieldObject.setWidth(excel.width());
                fieldObject.setTitle(excel.name());
                fields.add(fieldObject);
            }
        }
        return isLock;
    }
}