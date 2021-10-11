package com.data.service.impl;

import com.data.mapper.UserMapper;
import com.data.service.UserService;
import com.manager.common.core.domain.entity.DataUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author marvin 2021/8/28
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer findByphone(String phone) {
        return userMapper.findByphone(phone);
    }

    @Override
    public Integer insertToDataUser(DataUser dataUser) {
        return userMapper.insertToDataUser(dataUser);
    }

    @Override
    public Integer loadDataUserName(DataUser dataUser) {
        return userMapper.loadDataUserName(dataUser);
    }

    @Override
    public DataUser findUserBySeedToken(String seedToken) {
        return userMapper.findUserBySeedToken(seedToken);
    }

    @Override
    public DataUser findByPassword(String phoneNumber, String password) {
        return userMapper.findByPassword(phoneNumber, password);
    }

    @Override
    public DataUser findByPhone(String phoneNumber) {
        return userMapper.findByPhone(phoneNumber);
    }

    @Override
    public void updatePassword(String phoneNumber, String password) {
        userMapper.updatePassword(phoneNumber, password);
    }

    @Override
    public int updateDataUser(DataUser dataUser) {
        return userMapper.updateDataUser(dataUser);
    }

    @Override
    public Map selectLock(String uid) {
        return userMapper.selectLock(uid);
    }

}
