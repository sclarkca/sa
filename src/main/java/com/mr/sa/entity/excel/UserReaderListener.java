package com.mr.sa.entity.excel;

import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.mr.sa.entity.app.AppUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
public class UserReaderListener extends AnalysisEventListener<UserExcelTemplate> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    public List<AppUser> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    Map<String, String> orgMap = new HashMap<>();
    Map<String, String> appRoleMap = new HashMap<>();
    List<String> msg = new ArrayList<>();

    public UserReaderListener(Map<String, String> orgMap, Map<String, String> appRoleMap) {
        this.orgMap = orgMap;
        this.appRoleMap = appRoleMap;
    }

    @Override
    public void invoke(UserExcelTemplate data, AnalysisContext context) {
        if (isLineNullValue(data)) {
            return;
        }
        ReadRowHolder readRowHolder = context.readRowHolder();
        Integer rowIndex = readRowHolder.getRowIndex();
        String row = String.format("第%d行", rowIndex);
        //导入数据校验
        String username = data.getUsername();
        if (StringUtils.isBlank(username)) {
            msg.add(row + "账户用户名不能为空");
        }
        String nickname = data.getNickname();
        if (StringUtils.isBlank(nickname)) {
            msg.add(row + "昵称不能为空");
        }
        String roleName = data.getRoleName();
        if (StringUtils.isBlank(roleName)) {
            msg.add(row + "角色不能为空");
        }
        String roleId = appRoleMap.get(roleName);
        if (StringUtils.isBlank(roleId)) {
            msg.add(row + "角色不存在");
        }
        String orgName = data.getOrgName();
        if (StringUtils.isBlank(orgName)) {
            msg.add(row + "单位不能为空");
        }
        String orgId = orgMap.get(orgName);
        if (StringUtils.isBlank(orgId)) {
            msg.add(row + "单位不存在");
        }

        AppUser appUser = new AppUser();
        appUser.setId(IdUtil.fastSimpleUUID());
        appUser.setUsername(username);
        appUser.setNickname(nickname);
        appUser.setRoleId(roleId);
        appUser.setOrgId(orgId);
        cachedDataList.add(appUser);
        if (cachedDataList.size() >= BATCH_COUNT) {
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }
    /**
     * 判断整行单元格数据是否均为空
     */
    private boolean isLineNullValue(UserExcelTemplate data) {
        try {
            List<Field> fields = Arrays.stream(data.getClass().getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(ExcelProperty.class))
                    .collect(Collectors.toList());
            List<Boolean> lineNullList = new ArrayList<>(fields.size());
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(data);
                if (Objects.isNull(value)) {
                    lineNullList.add(Boolean.TRUE);
                } else {
                    lineNullList.add(Boolean.FALSE);
                }
            }
            return lineNullList.stream().allMatch(Boolean.TRUE::equals);
        } catch (Exception e) {
            log.error("读取数据行[{}]解析失败: {}", data, e.getMessage());
        }
        return true;
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
