package com.mr.sa.service.common;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.mr.sa.entity.Org;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgService {


    public List<Org> getAllOrgs() {
        return JpaUtil.linq(Org.class).list();
    }
}
