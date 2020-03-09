package com.shi.annie.excel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.*;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2019/12/21 12:12 下午
 */

//TODO 导出要做异步，不然把业务逻辑都夯住了
public class ExcelAsyncExporter {


    public static void export() {

        //这里只写excel的读取逻辑
        //

    }

    private static SXSSFWorkbook getExcel () {
        SXSSFWorkbook wb = new SXSSFWorkbook(1000);
        Font font = wb.createFont();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);


        Sheet sh = wb.createSheet("Sheet1");
        sheetExcelTitle(sh, font, cellStyle);

        //handlePageData(modelFields, fieldsArray, forData.getContent());  //时间枚举的替换
        //sheetExcelContent(list, fieldsArray, modelFields, sh, cellStyle);  //封装数据到Excel
        return wb;
    }

    private static void sheetExcelTitle(Sheet sh, Font headFont, CellStyle headCellStyle) {
        Cell cell;
        //表头样式
        headFont.setBold(Boolean.TRUE);
        headFont.setFontHeightInPoints((short) 13);
        headCellStyle.setFont(headFont);
        //表头
        Row row = sh.createRow(0);
//        for (int i = 0; i < fieldsArray.length; i++) {
//            //关联字段 需用 '/' 分隔显示  -> 关联字段/字段A ，关联字段/字段B...
//            String title = getFieldDisplayName(model, fieldsArray[i]);
//            sh.setColumnWidth(i, title.getBytes().length * 360);
//            cell = row.createCell(i);
//            cell.setCellStyle(headCellStyle);
//            cell.setCellValue(title);
//        }
    }


    public static void sheetExcelContent(List<Map<String, Object>> list, String[] fieldsArray, Sheet sh, CellStyle cellStyle) {
        //allRowData <行号，<列号，列数据>>
        Map<Integer, Map<Integer, String>> allRowData = new HashMap<>();
        int startLine = sh.getLastRowNum() + 1;
        //解析内容
        //int fieldSize = modelFields.size();
        int fieldSize = 0;
        for (Map<String, Object> data : list) {
            int firstLine = startLine;
            //allCellData <列号，列数据>
            Map<Integer, String> allCellData;
            for (int c = 0; c < fieldSize; c++) {
                String field = fieldsArray[c];
                List<String> fieldValue = new ArrayList<>();
                //ModelFieldUtils.getFieldValueBySplit(fieldValue, field, data, "/");
                if (CollectionUtils.isNotEmpty(fieldValue)) {
                    for (int i = 0; i < fieldValue.size(); i++) {
                        int currentLine = firstLine + i;
                        //如果不存在当前行, 就新建行
                        allCellData = allRowData.get(currentLine);
                        if (null == allCellData) {
                            allCellData = new HashMap<>();
                            allRowData.put(currentLine, allCellData);
                        }
                        allCellData.put(c, fieldValue.get(i));
                    }
                    if (fieldValue.size() > (startLine - firstLine)) {
                        startLine = firstLine + fieldValue.size();
                    }
                }
            }
        }
        //由于SXSSFWork会按行索引刷内存，所以必须在一个行内就处理完所有列数据
        for (Integer rowNum : allRowData.keySet()) {
            Map<Integer, String> allCellData = allRowData.get(rowNum);
            Row row = sh.createRow(rowNum);
//            for (int j = 0; j < modelFields.size(); j++) {
//                if (StringUtils.isNotBlank(allCellData.get(j))) {
//                    Cell cell = row.createCell(j);
//                    int currentWidth = allCellData.get(j).getBytes().length * 360;
//                    if (sh.getColumnWidth(j) < currentWidth) {
//                        //取最大列宽
//                        sh.setColumnWidth(j, currentWidth);
//                    }
//                    cell.setCellStyle(cellStyle);
//                    cell.setCellValue(allCellData.get(j));
//                }
//            }
        }
    }


//    /**
//     * 对查出来的数据做一些处理
//     * 1. 枚举换值
//     * 2. 时间转换成字符串
//     */
//    private static void handlePageData(List<ModelField> modelFields, String[] fieldsArray, List<Map<String, Object>> content) {
//        for (int i = 0; i < fieldsArray.length; i++) {
//            ModelField modelField = modelFields.get(i);
//            //1. 枚举换值
//            if (StringUtils.isNotBlank(modelField.getSelection())
//                    &&
//                    (ModelTtypeEnum.ENUM.getValue().equals(modelField.getTtype())
//                            || ModelTtypeEnum.MULTI_ENUM.getValue().equals(modelField.getTtype()))) {
//                //当字段为单枚举或多枚举类型时，要将数据库的值替换成对应的枚举值
//                String fieldStr = fieldsArray[i];
//                for (Map<String, Object> lineData : content) {
//                    if (fieldStr.contains(SLASH)) {
//                        int index = fieldStr.indexOf(SLASH);
//                        String fStr = fieldStr.substring(0, index);
//                        String lStr = fieldStr.substring(index + 1);
//                        Object subContent = lineData.get(fStr);
//                        if (subContent instanceof Map) {   //多对一 传入的是对象 Map<String,Object>
//                            handlePageData(Collections.singletonList(modelField), new String[]{lStr}, Collections.singletonList((Map<String, Object>) subContent));
//                        }
//                        if (subContent instanceof ArrayList) {   //一对多  传入的是集合 List<Map<String,Object>>
//                            handlePageData(Collections.singletonList(modelField), new String[]{lStr}, (List<Map<String, Object>>) subContent);
//                        }
//                    } else {
//                        if (null != lineData.get(modelField.getName())) {  //如果数据库的值不存在，就没必要转换
//                            String value = convertEnumValueToDisplayName(modelField, (String) lineData.get(modelField.getName()));
//                            lineData.put(modelField.getName(), value);
//                        }
//                    }
//                }
//            }
//            //2. 日期类型转换
//            if (modelField.getTtype().equals(ModelTtypeEnum.DATE.getValue())) {
//                for (Map<String, Object> lineData : content) {
//                    Date date = (Date)lineData.get(modelField.getName());
//                    if (null != date) {
//                        lineData.put(modelField.getName(), DateUtils.formatDate(date,DateUtils.yyyyMMddHHmmss));
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 枚举方法，将value -> displayName
//     *
//     * @param modelField 枚举字段
//     * @param value   字段数据 value中对应的是 enum中的key
//     * @return displayName  对应key的displayName
//     * 如果多枚举对应key1,key2  ->  displayName1,displayName2
//     */
//    public static String convertEnumValueToDisplayName(ModelField modelField, String value) {
//        if (StringUtils.isBlank(value)) {
//            return "";
//        }
//        if (!ModelTtypeEnum.MULTI_ENUM.getValue().equals(modelField.getTtype())
//                && !ModelTtypeEnum.ENUM.getValue().equals(modelField.getTtype())) {
//            //该字段不是枚举类型
//            return value;
//        }
//        //多枚举,可能存在多个key
//        String[] keys = value.split(COMMA);
//        String result = "";
//        JSONObject enumObject = JSONObject.parseObject(modelField.getSelection());
//        JSONArray enumValues = (JSONArray) enumObject.get("enumValues");
//        for (String key : keys) {
//            for (Object enumValue : enumValues) {
//                JSONObject enumMap = (JSONObject) enumValue;
//                if (enumMap.get("value").toString().equals(key)) {
//                    if (StringUtils.isBlank(result)) {
//                        result = (String) enumMap.get("displayName");
//                    } else {
//                        result = result + COMMA + enumMap.get("displayName");
//                    }
//                    break;
//                }
//            }
//        }
//        return StringUtils.isBlank(result) ? value : result;
//    }


}
