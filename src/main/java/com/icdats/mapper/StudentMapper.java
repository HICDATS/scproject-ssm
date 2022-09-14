package com.icdats.mapper;

import com.icdats.pojo.S_C_Score;
import com.icdats.pojo.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentMapper {
    //通过学生学号获取单个学生
    public Student getStudentBySid(@Param("sid") Integer sid);

    //根据姓名模糊查询一位学生的sid
    public List<Student> getSidByMoHuName(@Param("moHuName")String moHuName);

    //根据学生学号获取单个学生所有信息（包括账号，密码等）
    public Student getStudentDetailBySid(@Param("sid")Integer sid);

    //根据学生姓名模糊查询学生列表
    public List<Student> getStudentListByMoHuName(@Param("moHuName")String moHuName);

    //根据学生专业模糊查询学生列表
    public List<Student> getStudentListByMoHuMajor(@Param("moHuMajor")String moHuMajor);

    //根据学号删除学生信息
    public void deleteStudentBySid(@Param("sid")Integer sid);

    //根据学号修改学生信息
    public void updateStudentBySid(@Param("sid") Integer sid, @Param("name")String name, @Param("sex")String sex, @Param("major")String major, @Param("grade")String grade, @Param("sclass")String sclass);

    //添加学生信息
    public void addStudent(@Param("sid") Integer sid,@Param("name")String name,@Param("sex")String sex,@Param("major")String major,@Param("grade")String grade,@Param("sclass")String sclass);

}
