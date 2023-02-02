package com.mr.sa.entity.rel;

import com.bstek.bdf3.dorado.jpa.annotation.Generator;
import com.bstek.bdf3.dorado.jpa.policy.impl.UUIDPolicy;
import com.bstek.dorado.annotation.PropertyDef;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author: lxp
 **/
@Data
@Entity
@Table(name = "biz_rel_event_point")
public class RelEventPoint {

    @Id
    @Column(name = "id", length = 64)
    @PropertyDef(label = "id")
    @Generator(policy = UUIDPolicy.class)
    private String id;

    @PropertyDef(label = "事件ID")
    @Column(name = "event_id", length = 32)
    private String eventId;

    @PropertyDef(label = "巡防点ID")
    @Column(name = "point_id", length = 64)
    private String pointId;

    @PropertyDef(label = "类型")
    @Column(name = "type", length = 64)
    private String type;

    @PropertyDef(label = "创建时间")
    @Column(name = "create_time", length = 64)
    private Date createTime;

}
