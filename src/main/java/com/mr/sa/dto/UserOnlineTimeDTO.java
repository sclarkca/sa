package com.mr.sa.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserOnlineTimeDTO implements Serializable {

    private String userId;

    private Long seconds;

    private String username;

    private String orgId;

    private String orgName;

    private Date recordDate;

    private String time;
}
