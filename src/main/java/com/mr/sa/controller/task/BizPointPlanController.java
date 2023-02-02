package com.mr.sa.controller.task;


import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.data.vo.BizPointPlanVO;
import com.mr.sa.service.jooq.BizPointPlanService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 巡防活动 前端控制器
 * </p>
 *
 * @author suaike
 * @since 2022-10-01
 */

@Controller
@Transactional(readOnly = true)
public class BizPointPlanController {

    @Resource
    private BizPointPlanService bizPointPlanService;

    @DataProvider
    public void query(Page<BizPointPlanVO> page, Map<String, Object> parameter) {
        bizPointPlanService.page(parameter, page);
    }

}
