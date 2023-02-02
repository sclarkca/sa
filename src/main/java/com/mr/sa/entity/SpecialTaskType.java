package com.mr.sa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 专项任务类型
 */
@Data
@Entity
@Table(name = "biz_special_task_type")
public class SpecialTaskType extends BaseModel {

	@PropertyDef(label = "名称")
	@Column(name = "name")
	private String name;

}