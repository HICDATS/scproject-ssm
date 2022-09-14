package com.icdats.mapper;

import com.icdats.pojo.Course;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CourseMapper {
    public List<Course> getCourseByTid(@Param("tid") Integer tid);

    //根据课程号查询课程
    public Course getCourseByCid(@Param("cid")Integer cid);

    public List<Course> getAllCourse();

    //根据课程名获取课程cid
    public Integer getCidByCname(@Param("cname")String cname);

    //根据老师学工号获取待审核课程
    public List<Course> getNeedAuditCourseByTid(@Param("tid")Integer tid);

    //根据课程名删除待审核课程
    public void deleteNeedAuditCourseByCname(@Param("cname")String cname);

    //根据课程信息添加待审核课程
    public void addNeedAuditCourse(@Param("cname")String cname,@Param("time")String time,@Param("address")String address,@Param("cap")Integer cap,@Param("tid")Integer tid);

    //通过上课地点和时间获取现有课程
    public List<Course> getCourseByAddressAndTime(@Param("address")String address,@Param("time")String time);

    //通过课程名删除课程记录
    public void deleteCourseByCname(@Param("cname")String cname);

    //通过模糊课程名查询课程
    public List<Course> getCoursesByTidAndMoHuCname(@Param("moHuCname")String moHuCname);


    //获取所有待审核的课程
    public List<Course> getAllNeedAuditCourse();

    //向课程列表中添加课程
    public void addCourse(@Param("cname")String cname,@Param("time")String time,@Param("address")String address,@Param("cap")Integer cap,@Param("tid")Integer tid);
}
