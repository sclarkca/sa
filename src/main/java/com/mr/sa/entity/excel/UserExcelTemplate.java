package com.mr.sa.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author: lxp
 **/
@Data
public class UserExcelTemplate {

    @ExcelProperty("账号用户名")
    private String username;

    @ExcelProperty("昵称")
    private String nickname;

    @ExcelProperty("角色")
    private String roleName;

    @ExcelProperty("单位")
    private String orgName;

}
