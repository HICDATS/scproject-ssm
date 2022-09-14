package com.icdats.mapper;

import com.icdats.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TeacherMapper {
    public Teacher getTeacher(@Param("tid")Integer tid);
    public String getTeacherName(@Param("tid")Integer tid);

    //根据学工号获取单个教师所有信息（包括账号，密码等）
    public Teacher getTeacherDetailByTid(@Param("tid")Integer tid);

    //根据学生姓名模糊查询学生列表
    public List<Teacher> getTeacherListByMoHuName(@Param("moHuName")String moHuName);

    //根据学院名称模糊查询教师列表
    public List<Teacher> getTeacherListByMoHuCollege(@Param("moHuCollege")String moHuCollege);

    //根据学工号删除教师信息
    public void deleteTeacherByTid(@Param("tid")Integer tid);

    //根据学工号修改教师信息
    public void updateTeacherByTid(@Param("tid")Integer tid, @Param("name")String name, @Param("sex")String sex, @Param("college")String college, @Param("jobTitle")String jobTitle);

    //添加教师信息
    public void addTeacher(@Param("tid")Integer tid,@Param("name")String name, @Param("sex")String sex, @Param("college")String college, @Param("jobTitle")String jobTitle);


}
