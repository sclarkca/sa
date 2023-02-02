/*
 * This file is generated by jOOQ.
*/
package com.mr.sa.generator.tables;


import com.mr.sa.generator.Indexes;
import com.mr.sa.generator.Keys;
import com.mr.sa.generator.Sa;
import com.mr.sa.generator.tables.records.BizPointTaskItemOrgRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * 关系:巡防任务/巡防组织
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.8"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BizPointTaskItemOrg extends TableImpl<BizPointTaskItemOrgRecord> {

    private static final long serialVersionUID = 652413702;

    /**
     * The reference instance of <code>sa.biz_point_task_item_org</code>
     */
    public static final BizPointTaskItemOrg BIZ_POINT_TASK_ITEM_ORG = new BizPointTaskItemOrg();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<BizPointTaskItemOrgRecord> getRecordType() {
        return BizPointTaskItemOrgRecord.class;
    }

    /**
     * The column <code>sa.biz_point_task_item_org.id</code>.
     */
    public final TableField<BizPointTaskItemOrgRecord, String> ID = createField("id", org.jooq.impl.SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>sa.biz_point_task_item_org.point_task_item_id</code>. 点任务ID
     */
    public final TableField<BizPointTaskItemOrgRecord, String> POINT_TASK_ITEM_ID = createField("point_task_item_id", org.jooq.impl.SQLDataType.VARCHAR(100), this, "点任务ID");

    /**
     * The column <code>sa.biz_point_task_item_org.org_id</code>. 组织ID
     */
    public final TableField<BizPointTaskItemOrgRecord, String> ORG_ID = createField("org_id", org.jooq.impl.SQLDataType.VARCHAR(100), this, "组织ID");

    /**
     * The column <code>sa.biz_point_task_item_org.created_date</code>. 创建日期
     */
    public final TableField<BizPointTaskItemOrgRecord, LocalDateTime> CREATED_DATE = createField("created_date", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "创建日期");

    /**
     * The column <code>sa.biz_point_task_item_org.creator</code>. 创建人
     */
    public final TableField<BizPointTaskItemOrgRecord, String> CREATOR = createField("creator", org.jooq.impl.SQLDataType.VARCHAR(32), this, "创建人");

    /**
     * The column <code>sa.biz_point_task_item_org.update_date</code>. 修改日期
     */
    public final TableField<BizPointTaskItemOrgRecord, LocalDateTime> UPDATE_DATE = createField("update_date", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "修改日期");

    /**
     * The column <code>sa.biz_point_task_item_org.modifier</code>. 修改人
     */
    public final TableField<BizPointTaskItemOrgRecord, String> MODIFIER = createField("modifier", org.jooq.impl.SQLDataType.VARCHAR(32), this, "修改人");

    /**
     * Create a <code>sa.biz_point_task_item_org</code> table reference
     */
    public BizPointTaskItemOrg() {
        this(DSL.name("biz_point_task_item_org"), null);
    }

    /**
     * Create an aliased <code>sa.biz_point_task_item_org</code> table reference
     */
    public BizPointTaskItemOrg(String alias) {
        this(DSL.name(alias), BIZ_POINT_TASK_ITEM_ORG);
    }

    /**
     * Create an aliased <code>sa.biz_point_task_item_org</code> table reference
     */
    public BizPointTaskItemOrg(Name alias) {
        this(alias, BIZ_POINT_TASK_ITEM_ORG);
    }

    private BizPointTaskItemOrg(Name alias, Table<BizPointTaskItemOrgRecord> aliased) {
        this(alias, aliased, null);
    }

    private BizPointTaskItemOrg(Name alias, Table<BizPointTaskItemOrgRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "关系:巡防任务/巡防组织");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Sa.SA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.BIZ_POINT_TASK_ITEM_ORG_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<BizPointTaskItemOrgRecord> getPrimaryKey() {
        return Keys.KEY_BIZ_POINT_TASK_ITEM_ORG_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<BizPointTaskItemOrgRecord>> getKeys() {
        return Arrays.<UniqueKey<BizPointTaskItemOrgRecord>>asList(Keys.KEY_BIZ_POINT_TASK_ITEM_ORG_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointTaskItemOrg as(String alias) {
        return new BizPointTaskItemOrg(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointTaskItemOrg as(Name alias) {
        return new BizPointTaskItemOrg(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public BizPointTaskItemOrg rename(String name) {
        return new BizPointTaskItemOrg(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public BizPointTaskItemOrg rename(Name name) {
        return new BizPointTaskItemOrg(name, null);
    }
}
