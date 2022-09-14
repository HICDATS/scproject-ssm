package com.icdats.service.impl;

import com.icdats.mapper.TeacherMapper;
import com.icdats.pojo.Course;
import com.icdats.pojo.Teacher;
import com.icdats.pojo.TimeTable;
import com.icdats.service.CourseService;
import com.icdats.service.S_C_ScoreService;
import com.icdats.service.TeacherService;
import com.icdats.service.UserService;
import com.icdats.utils.TimeTableUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherMapper teacherMapper;
    private CourseService courseService;
    private UserService userService;
    private S_C_ScoreService s_C_ScoreService;

    @Override
    public Teacher getTeacher(Integer tid) {
        Teacher teacher = teacherMapper.getTeacher(tid);
        List<Course> courseList = courseService.getCourseByTid(teacher.getTid());
        teacher.setCourses(courseList);
        HashSet<Course> coursesSet = new HashSet<>();
        coursesSet.addAll(courseList);
        List<TimeTable> timeTables = TimeTableUtil.getTimeTableListByCourses(coursesSet);
        teacher.setTimeTables(timeTables);
        return teacher;
    }

    @Override
    public String getTeacherName(Integer tid) {
        return teacherMapper.getTeacherName(tid);
    }

    @Override
    public List<Teacher> getTeacherListBySelectNameAndValue(String selectName, String value) {
        List<Teacher> teachers = new ArrayList<>();
        if (selectName.equals("全部")) {
            teachers = teacherMapper.getTeacherListByMoHuName("");
        } else if (selectName.equals("学工号")) {
            if (!(value.matches("^[0-9]+$"))) {
                return teachers;
            }
            teachers.add(teacherMapper.getTeacherDetailByTid(Integer.parseInt(value)));
        } else if (selectName.equals("姓名")) {
            teachers = teacherMapper.getTeacherListByMoHuName(value);
        } else if (selectName.equals("学院")) {
            teachers = teacherMapper.getTeacherListByMoHuCollege(value);
        }
        return teachers;
    }

    @Override
    public void deleteTeacher(Integer tid) {
        teacherMapper.deleteTeacherByTid(tid);
//        userService.deleteUserById(tid);
        s_C_ScoreService.deleteS_C_ScoreBySid(tid);
    }

    @Override
    public void updateTeacher(Integer tid, String name, String sex, String college, String jobTitle, String acount, String pwd) {
        teacherMapper.updateTeacherByTid(tid,name,sex,college,jobTitle);
//        userService.updateUserById(tid,acount,pwd);
    }

    @Override
    public boolean addTeacher(Integer tid, String name, String sex, String college, String jobTitle, String pwd) {
//        boolean ok = userService.addUser("t" + tid, pwd, "2",tid);
        //若用户表中有相同id则ok为false，或者student表中有相同sid不能继续添加
//        if(!ok || teacherMapper.getTeacher(tid)!=null){
//            return false;
//        }
        teacherMapper.addTeacher(tid,name,sex,college,jobTitle);
        return true;
    }
}
