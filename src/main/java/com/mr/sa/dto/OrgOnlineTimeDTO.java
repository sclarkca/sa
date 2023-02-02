package com.mr.sa.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrgOnlineTimeDTO implements Serializable {


    private String orgName;

    private Long seconds;

    private String time;
}
