package com.manager.common.core.domain.entity;

import com.manager.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 用户对象 sys_ip_while
 *
 * @author marvin
 */
@Data
public class SysIpWhite extends BaseEntity {
    private long id;

    private long deptId;

    private long userId;

    private String ip;

    private long createUserId;

    private String creatTime;

    public SysIpWhite() {
    }

    public SysIpWhite(long deptId, long userId, long createUserId, String ip) {
        this.deptId = deptId;
        this.userId = userId;
        this.ip = ip;
        this.createUserId = createUserId;
    }
}