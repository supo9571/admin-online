package com.data.service.impl;

import com.data.mapper.LoginMapper;
import com.data.service.Loginservice;
import com.manager.common.core.domain.model.Login;
import com.manager.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author marvin 2021/8/21
 */
@Service
@Slf4j
public class LoginserviceImpl implements Loginservice {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public List selectLogin(Login login) {
        return loginMapper.selectLogin(login);
    }

    /**
     * 今日登录总数
     */
    public Integer selectTodayLogins() {
        return loginMapper.selectTodayLogins(DateUtils.datePath());
    }

    /**
     * 近期登录总数
     */
    public List selectLoginCounts(String type) {
        List list = new ArrayList();
        if ("w".equals(type)) {
            list = loginMapper.selectLoginCounts(DateUtils.getWeek(), DateUtils.datePath());
        } else if ("m".equals(type)) {
            list = loginMapper.selectLoginCounts(DateUtils.getMonth(), DateUtils.datePath());
        } else if ("y".equals(type)) {
            list = loginMapper.selectLoginCounts(DateUtils.getYear(), DateUtils.datePath());
        }
        return list;
    }
}
