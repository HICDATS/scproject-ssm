package com.icdats.service.impl;

import com.icdats.mapper.UserMapper;
import com.icdats.pojo.User;
import com.icdats.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String acount, String pwd) {
        return userMapper.getUser(acount,pwd);
    }

//    @Override
//    public User getUserById(Integer id) {
//        return userMapper.getUserById(id);
//    }
//
//    @Override
//    public void changePwdById(Integer id, String newPassword) {
//        userMapper.changePwdById(id,newPassword);
//    }
//
//    @Override
//    public void deleteUserById(Integer id) {
//        userMapper.deleteUserById(id);
//    }
//
//    @Override
//    public void updateUserById(Integer id, String acount, String pwd) {
//        userMapper.updateUserById(id,acount,pwd);
//    }
//
//    @Override
//    public boolean addUser(String acount, String pwd, String role, Integer id) {
//        if(userMapper.getUserById(id)!=null){
//            return false;
//        }else{
//            userMapper.addUser(acount,pwd,role,id);
//            return true;
//        }
//    }
}
