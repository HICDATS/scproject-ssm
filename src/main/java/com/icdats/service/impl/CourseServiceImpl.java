package com.icdats.service.impl;

import com.icdats.mapper.CourseMapper;
import com.icdats.pojo.Course;
import com.icdats.service.CourseService;
import com.icdats.service.S_C_ScoreService;
import com.icdats.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private S_C_ScoreService s_C_ScoreService;
    @Autowired
    private TeacherService teacherService;

    @Override
    public List<Course> getCourseByTid(Integer tid) {
        List<Course> courses = courseMapper.getCourseByTid(tid);
        for (Course course : courses) {
            course.setTimeArr(course.getTime().split("，"));
        }
        return courses;
    }

    @Override
    public Course getCourseByCid(Integer cid) {
        Course course = courseMapper.getCourseByCid(cid);
        if(course==null){
            return course;
        }
        Integer countOfCourse = s_C_ScoreService.getCountOfCourse(course.getCid());
        course.setSnum(countOfCourse);
        course.setTimeArr(course.getTime().split("，"));
        return course;
    }

    @Override
    public List<Course> getAllCourse() {
        List<Course> courses = courseMapper.getAllCourse();
        for (Course course : courses) {
            String teacherName = teacherService.getTeacherName(course.getTid());
            course.setTeachername(teacherName);
            Integer countOfCourse = s_C_ScoreService.getCountOfCourse(course.getCid());
            course.setSnum(countOfCourse);
            course.setTimeArr(course.getTime().split("，"));
        }
        return courses;
    }

    @Override
    public Integer getCidByCname(String cname) {
        Integer cid = courseMapper.getCidByCname(cname);
        //当课程名不存在则返回-1
        if(cid==null){
            return -1;
        }
        //当课程名存在返回对应课程号
        return cid;
    }

    @Override
    public List<Course> getNeedAuditCourseByTid(Integer tid) {
        return courseMapper.getNeedAuditCourseByTid(tid);
    }

    @Override
    public void deleteNeedAuditCourseByCname(String cname) {
        courseMapper.deleteNeedAuditCourseByCname(cname);
    }

    @Override
    public void addNeedAuditCourse(String cname, String time, String address, Integer cap, Integer tid) {
        courseMapper.addNeedAuditCourse(cname,time,address,cap,tid);
    }

    @Override
    public List<Course> getCourseByAddressAndTime(String address, String time) {
        return courseMapper.getCourseByAddressAndTime(address,time);
    }

    @Override
    public void deleteCourseByCname(String cname) {
        Integer cid = courseMapper.getCidByCname(cname);
        s_C_ScoreService.deleteS_C_ScoreByCname(cid);
        courseMapper.deleteCourseByCname(cname);
    }

    @Override
    public List<Integer> getCidsByTidAndMoHuCname(String moHuCname) {
        List<Course> courses = courseMapper.getCoursesByTidAndMoHuCname(moHuCname);
        for (Course course : courses) {
            String teacherName = teacherService.getTeacherName(course.getTid());
            course.setTeachername(teacherName);
            Integer countOfCourse = s_C_ScoreService.getCountOfCourse(course.getCid());
            course.setSnum(countOfCourse);
            course.setTimeArr(course.getTime().split("，"));
        }
        ArrayList<Integer> cids = new ArrayList<>();
        for (Course course : courses) {
            cids.add(course.getCid());
        }
        return cids;
    }

    @Override
    public List<Course> getCoursesBySelectCidOrMoHuCname(String selectName, String value) {
        List<Course> courses = new ArrayList<>();
        if(selectName.equals("课程号")){
            if(!(value.matches("^[0-9]+$"))){
                return courses;
            }
            int cid = Integer.parseInt(value);
            Course courseByCid = courseMapper.getCourseByCid(cid);
            if(courseByCid==null){
                return courses;
            }else{
                courses.add(courseByCid);
                return courses;
            }

        }else{
            List<Course> courseList = courseMapper.getCoursesByTidAndMoHuCname(value);
            for (Course course : courseList) {
                String teacherName = teacherService.getTeacherName(course.getTid());
                course.setTeachername(teacherName);
                Integer countOfCourse = s_C_ScoreService.getCountOfCourse(course.getCid());
                course.setSnum(countOfCourse);
                course.setTimeArr(course.getTime().split("，"));
            }
            return courseList;
        }
    }

    @Override
    public List<Course> getAllNeedAuditCourse() {
        return courseMapper.getAllNeedAuditCourse();
    }

    @Override
    public void agreeNeedAuditCourse(String cname, String time, String address, Integer cap, Integer tid) {
        courseMapper.deleteNeedAuditCourseByCname(cname);
        courseMapper.addCourse(cname,time,address,cap,tid);
    }

    @Override
    public void addCourse(String cname, String time, String address, Integer cap, Integer tid) {
        courseMapper.addCourse(cname, time, address, cap, tid);
    }
}
