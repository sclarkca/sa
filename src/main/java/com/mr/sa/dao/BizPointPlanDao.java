package com.mr.sa.dao;

import com.mr.sa.entity.BizPointPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BizPointPlanDao extends JpaRepository<BizPointPlan, String>, JpaSpecificationExecutor<BizPointPlan>, PagingAndSortingRepository<BizPointPlan, String> {

}

