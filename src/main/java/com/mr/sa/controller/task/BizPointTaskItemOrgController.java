package com.mr.sa.controller.task;


import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 关系:巡防任务/巡防组织 前端控制器
 * </p>
 *
 * @author suaike
 * @since 2022-10-01
 */
@Controller
@Transactional(readOnly = true)
public class BizPointTaskItemOrgController {

}
