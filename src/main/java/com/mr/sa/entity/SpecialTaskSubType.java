package com.mr.sa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 专项任务子类型
 */
@Data
@Entity
@Table(name = "biz_special_task_sub_type")
public class SpecialTaskSubType extends BaseModel {

	@PropertyDef(label = "任务类型") 
	private String type;
	
	@PropertyDef(label = "名称") 
	private String name;

}