package com.mr.sa.controller.app;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.bstek.bdf3.dorado.jpa.CriteriaUtils;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.app.AppConf;
import com.mr.sa.entity.app.AppRole;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.entity.excel.CustomerColumnWidthStyleStrategy;
import com.mr.sa.entity.excel.SelectSheetWriteHandler;
import com.mr.sa.entity.excel.UserExcelTemplate;
import com.mr.sa.entity.excel.UserReaderListener;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.utils.common.ExcelStyleUtil;
import com.mr.sa.utils.common.MyDateUtil;
import com.mr.sa.utils.jpa.MyCriteriaUtils;
import com.mr.sa.utils.page.PageUtil;
import com.mr.sa.utils.pwd.MakeRandomPasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@Transactional
public class AppUserController extends QueryFilter {

    @Value("${default.user.avatar}")
    String avatar;

    @Value("${default.user.pwd}")
    String defaultPwd;

    @Resource
    ExcelStyleUtil excelStyleUtil;

    @Resource
    PageUtil pageUtil;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @DataProvider
    public List<AppUser> query(Map<String, Object> parameter) {
        Criteria criteria = new Criteria();
        criteria = condition(criteria, parameter);
        List<AppUser> appUserList = JpaUtil.linq(AppUser.class).where(criteria).desc("updateDate").list();
        return appUserList;
    }

    @DataProvider
    public void queryAll(Page<AppConf> page, Map<String, Object> parameter) {
        Criteria criteria = new Criteria();
        criteria = condition(criteria, parameter);
        JpaUtil.linq(AppUser.class)
                .collect("code", Org.class, "orgId")
                .where(criteria).desc("updateDate")
                .paging(page);

    }

    @DataResolver
    @Transactional
    public void save(List<AppUser> appUsers) {
        JpaUtil.save(appUsers, new SmartSavePolicyAdapter() {

            @Override
            public boolean beforeInsert(SaveContext context) {
                // app用户管理增加用户默认头像
                AppUser appUser = context.getEntity();
                if (StringUtils.isBlank(appUser.getAvatar())) {
                    appUser.setAvatar(avatar);
                }
                if (!appUser.getPassword().startsWith("{bcrypt}$")) {
                    appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
                }
                return true;
            }

            @Override
            public boolean beforeUpdate(SaveContext context) {
                AppUser appUser = context.getEntity();
                if (!appUser.getPassword().startsWith("{bcrypt}$")) {
                    appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
                }
                return true;
            }
        });
    }

    @Expose
    @Transactional
    public void changePassword(String username, String newPassword) {
        AppUser appUser = JpaUtil.linq(AppUser.class).equal("username", username).findOne();
        appUser.setPassword(passwordEncoder.encode(newPassword));
        JpaUtil.mergeAndFlush(appUser);
    }

    @Expose
    public String isExists(Map<String, Object> parameter) {
        AppUser appUser = (AppUser) parameter.get("entity");
        String id = appUser.getId();
        String username = appUser.getUsername();
        // @formatter:off
        boolean isExists = JpaUtil.linq(AppUser.class)
                .addIf(StringUtils.isBlank(id))
                .equal("username", username)
                .endIf()
                .addIf(StringUtils.isNotBlank(id))
                .notEqual("id", id)
                .equal("username", username)
                .endIf()
                .exists();
        // @formatter:on
        if (isExists) {
            return "用户名已存在。";
        }
        return null;
    }

    /**
     * 查询-服务端过滤-全部
     */
    @DataProvider
    public List<AppUser> loadAllListByFilter(Map<String, Object> parameter) {
        if (parameter == null) {
            parameter = new HashMap<>();
        }
        List<AppUser> appUserList = new ArrayList<AppUser>();
        try {
            MyCriteriaUtils.clearEmptyValue(parameter);
            Criteria criteria = new Criteria();
            if (parameter.containsKey("filterValue")) {
                CriteriaUtils.add(criteria, "username", FilterOperator.like, parameter.get("filterValue"));
                parameter.remove("filterValue", parameter.get("filterValue"));
            }
            MyCriteriaUtils.buildCriteriaMap(parameter);
            criteria = MyCriteriaUtils.add(criteria, parameter);

            appUserList = JpaUtil.linq(AppUser.class).where(criteria).asc("username").list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appUserList;
    }

    @Log(module = "APP用户导入", category = "IMPORT", desc = "APP用户导入")
    @FileResolver
    @Transactional
    public String importExcel(UploadFile file, Map<String, Object> parameter) throws IOException {
        log.info("user file import");
        List<AppUser> appUserList = JpaUtil.linq(AppUser.class).list();
        List<String> appUserNameList = appUserList.stream().map(AppUser::getUsername).collect(Collectors.toList());
        List<Org> orgList = JpaUtil.linq(Org.class).list();
        List<AppRole> appRoleList = JpaUtil.linq(AppRole.class).list();
        MultipartFile multipartFile = file.getMultipartFile();
        InputStream inputStream = multipartFile.getInputStream();
        Map<String, String> orgMap = new HashMap<>();
        orgList.forEach(org -> {
            orgMap.put(org.getName(), org.getCode());
        });
        Map<String, String> appRoleMap = new HashMap<>();
        appRoleList.forEach(appRole -> {
            appRoleMap.put(appRole.getName(), appRole.getId());
        });
        UserReaderListener userReaderListener = new UserReaderListener(orgMap, appRoleMap);

        EasyExcel.read(inputStream, UserExcelTemplate.class, userReaderListener).doReadAll();
        List<AppUser> cachedDataList = userReaderListener.getCachedDataList();
        List<String> msgList = userReaderListener.getMsg();
        if (CollectionUtils.isNotEmpty(msgList)) {
            String message = msgList.stream().collect(Collectors.joining("\n"));
            return message;
        }
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        cachedDataList.forEach(appUser -> {
            appUser.setPassword(passwordEncoder.encode(defaultPwd));
        });
        // 排出已存在的用户
        Iterator<AppUser> itr = cachedDataList.iterator();
        while (itr.hasNext()) {
            AppUser next = itr.next();
            if (appUserNameList.contains(next.getUsername())) {
                itr.remove();
            }
        }
        JpaUtil.persist(cachedDataList);
        log.info("存储数据库成功！");
        return "success";
    }

    @GetMapping("/user/exportTemplate")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("用户导入模板" + MyDateUtil.formatToDay(new Date()), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");


        List<Org> orgList = JpaUtil.linq(Org.class).asc("name", "code").list();
        List<AppRole> appRoleList = JpaUtil.linq(AppRole.class).list();
        //k是列,v是下拉的选项
        Map<Integer, List<String>> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(orgList)) {
            map.put(3, orgList.stream().map(Org::getName).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(appRoleList)) {
            map.put(2, appRoleList.stream().map(AppRole::getName).collect(Collectors.toList()));
        }
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream());
        excelWriterBuilder.registerWriteHandler(excelStyleUtil.defaultStyle());
        excelWriterBuilder.registerWriteHandler(new CustomerColumnWidthStyleStrategy());
        ExcelWriter excelWriter = excelWriterBuilder.build();
        WriteSheet writeSheet = EasyExcel.writerSheet(0).head(UserExcelTemplate.class).registerWriteHandler(new SelectSheetWriteHandler(map)).build();
        excelWriter.write(Collections.emptyList(), writeSheet);
        excelWriter.finish();
    }


}
