package com.icdats.mapper;

import com.icdats.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    User getUser(@Param("acount") String acount, @Param("pwd") String pwd);

    //添加用户
    void addUser(@Param("acount") String acount,@Param("pwd") String pwd,@Param("role") String role,@Param("id") Integer id);

    User getUserById(@Param("id") Integer id);

    void changePwdById(@Param("id") Integer id , @Param("newPassword")String newPassword);

    //删除用户信息
    public void deleteUserById(@Param("id") Integer id);

    //根据id修改用户信息
    public void updateUserById(@Param("id")Integer id,@Param("acount")String acount,@Param("pwd")String pwd);

}
