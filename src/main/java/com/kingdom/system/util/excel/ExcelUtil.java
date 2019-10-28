package com.kingdom.system.util.excel;

import com.kingdom.system.util.DateUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.util.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jay on 16-3-7.
 * excel工具类
 */
public abstract class ExcelUtil {

    protected static final String SERIALVERSIONUID = "serialVersionUID";

    protected InputStream inputStream = null;

    public static ExcelUtil create(InputStream inputStream) throws IOException {
        ExcelUtil excelUtil = null;
        byte[] data = IOUtils.peekFirst8Bytes(inputStream);
        if (FileMagic.valueOf(data) == FileMagic.OLE2) {
            excelUtil = new HssfExcelUtil(inputStream);
        } else if (FileMagic.valueOf(data) == FileMagic.OOXML) {
            excelUtil = new XssfExcelUtil(inputStream);
        }
        return excelUtil;
    }

    /**
     * 将指定excel文件中的数据转换成数据列表
     *
     * @return 返回转换后的数据列表
     * @throws Exception
     */
    public <T> List<T> readExcel(Class<T> clazz) throws Exception {
        return readExcel(clazz, 0, true);
    }

    /**
     * 将指定excel文件中的数据转换成数据列表
     * <p/>
     * 属性列表
     *
     * @param hasTitle 是否带有标题
     * @return 返回转换后的数据列表
     * @throws Exception
     */
    public <T> List<T> readExcel(Class<T> clazz, boolean hasTitle) throws Exception {
        return readExcel(clazz, 0, hasTitle);
    }

    public <T> List<T> readExcel(Class<T> clazz, String groupName) throws Exception {
        return readExcel(clazz, 0, true, groupName);
    }

    /**
     * 抽象方法：将指定excel文件中的数据转换成数据列表，由子类实现，
     *
     * @param clazz    数据类型,不写ExcelAnnotation认为是类的所有字段
     * @param sheetNo  工作表编号
     * @param hasTitle 是否带有标题
     * @return 返回转换后的数据列表
     * @throws Exception
     */
    public abstract <T> List<T> readExcel(Class<T> clazz, int sheetNo, boolean hasTitle)
            throws Exception;

    public abstract <T> List<T> readExcel(Class<T> clazz, int sheetNo, boolean hasTitle, String group)
            throws Exception;

    /**
     * 写入数据到指定excel文件中
     *
     * @param clazz
     *            数据类型
     * @param dataModels
     *            数据列表
     * @param fieldNames
     *            属性列表
     * @throws Exception
     */
    // public <T> void writeExcel(Class<T> clazz, List<T> dataModels,
    // String[] fieldNames) throws Exception {
    // writeExcel(clazz, dataModels, fieldNames, fieldNames);
    // }

    /**
     * 功能：抽象方法：写入数据到指定excel文件中，由子类实现
     *
     * @param response
     * @param filename
     * @param sheetName
     * @param list
     * @param groupName
     * @param password  null "" "null" 表示不进行加密
     * @param <T>
     * @throws Exception
     */
    public abstract <T> void writeExcel(HttpServletResponse response, String filename, String sheetName, List<T> list, String groupName, String password) throws Exception;

    public abstract <T> void writeExcel(HttpServletResponse response, String filename, String sheetName, List<T> list) throws Exception;

    /**
     * 判断属性是否为日期类型
     *
     * @param clazz     数据类型
     * @param fieldName 属性名
     * @return 如果为日期类型返回true，否则返回false
     */
    protected <T> boolean isDateType(Class<T> clazz, String fieldName) {
        boolean flag = false;
        try {
            Field field = clazz.getDeclaredField(fieldName);
            ExcelImport excel = field.getAnnotation(ExcelImport.class);
            return "Date".equals(excel.clazz());
        } catch (Exception e) {
            // 把异常吞掉直接返回false
        }
        return flag;
    }

    /**
     * 根据类型将指定参数转换成对应的类型
     *
     * @param value 指定参数
     * @param type  指定类型
     * @return 返回类型转换后的对象
     */
    protected Object parseValueWithType(String value, Class<?> type) {
        Object result = null;
        try { // 根据属性的类型将内容转换成对应的类型
            if (Boolean.TYPE == type || Boolean.class == type) {
                result = Boolean.parseBoolean(value);
            } else if (Byte.TYPE == type || Byte.class == type) {
                result = Byte.parseByte(value);
            } else if (Short.TYPE == type || Short.class == type) {
                result = Short.parseShort(value);
            } else if (Integer.TYPE == type || Integer.class == type) {
                result = Double.valueOf(value).intValue();
            } else if (Long.TYPE == type || Long.class == type) {
                result = Double.valueOf(value).longValue();
            } else if (Float.TYPE == type || Float.class == type) {
                result = Float.parseFloat(value);
            } else if (Double.TYPE == type || Double.class == type) {
                result = Double.parseDouble(value);
            } else if (Date.class == type) {
                result = DateUtil.formatDate(value);
            } else {
                result = value;
            }
        } catch (Exception e) {
            // 把异常吞掉直接返回null
        }
        return result;
    }

    //判断需要哪些属性，如果注解的属性是空，则认为是所有的属性
    protected String[] getFile(Class clazz) {
        return getFile(clazz, "");
    }

    //判断需要哪些属性，如果注解的属性是空，则认为是所有的属性
    protected String[] getFile(Class clazz, String groupName) {
        Field[] fields = clazz.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        List<String> fieldAnnotations = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
            if (fields[i].isAnnotationPresent(ExcelImport.class)) {
                ExcelImport excel = fields[i].getAnnotation(ExcelImport.class);
                if ("".equals(groupName) || groupName == null || "".equals(excel.group()[0])) {
                    fieldAnnotations.add(fields[i].getName());
                } else if (ArrayUtils.contains(excel.group(), groupName)) {
                    fieldAnnotations.add(fields[i].getName());
                }
            }
        }
        if (fieldAnnotations.size() > 0) {
            fieldNames = new String[fieldAnnotations.size()];
            return fieldAnnotations.toArray(fieldNames);
        }
        return fieldNames;
    }
}