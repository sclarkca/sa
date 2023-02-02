package com.mr.sa.entity.app;

import com.bstek.bdf3.dorado.jpa.annotation.Generator;
import com.bstek.bdf3.dorado.jpa.policy.impl.UUIDPolicy;
import com.bstek.dorado.annotation.PropertyDef;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author suaike
 * @since 2022-10-08
 */
@Data
@Entity
@Table(name = "biz_app_permission")
public class AppPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 64)
    @PropertyDef(label = "id")
    @Generator(policy = UUIDPolicy.class)
    private String id;

    private String attribute;

    private String resourceId;

    private String resourceType;

    private String roleId;


}
