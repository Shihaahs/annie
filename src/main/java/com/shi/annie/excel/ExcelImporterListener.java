package com.shi.annie.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2019/12/21 11:53 上午
 */
public class ExcelImporterListener extends AnalysisEventListener {

    private Long totalCount = 0l;

    private Long successCount = 0l;

    private Integer currentRowNum;


    @Override
    public void invoke(Object object, AnalysisContext context) {
        if (checkBlankRow((List) object)) {
            //log.debug("current sheet:[{}],totalCount:[{}]", context.getCurrentSheet(), context.getTotalCount());
            doCreate((List<String>) object, context.getCurrentRowNum());
            totalCount += 1;
        }
    }

    private void doCreate(List<String> dataList, Integer currentRowNum) {

        List<Map<String, Object>> dataMap = new ArrayList<>();
        dataMap = convertData(dataList);
        //todo 存入数据库

    }

    private List<Map<String, Object>> convertData(List<String> dataList) {
        //todo 根据对象转换
        return null;
    }

    //去除空行
    private Boolean checkBlankRow(List objectList){
        for (Object obj : objectList) {
            if (null != obj) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //log.debug("reader close");
        System.out.println("import Finished");
    }
}
