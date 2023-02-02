package com.mr.sa.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: lxp
 **/
@Data
public class EventExcelTemplate {

    @ExcelProperty("事件类型")
    private String eventType;

    @ExcelProperty("事件等级")
    private String eventLevel;

    @ExcelProperty("发生时间")
    private Date time;

    @ExcelProperty("发生地点")
    private String address;

    @ExcelProperty("描述")
    private String desc;

    @ExcelProperty("事件上报人")
    private String user;
}
