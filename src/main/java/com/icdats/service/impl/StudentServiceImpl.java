package com.icdats.service.impl;

import com.icdats.mapper.StudentMapper;
import com.icdats.pojo.*;
import com.icdats.service.CourseService;
import com.icdats.service.S_C_ScoreService;
import com.icdats.service.StudentService;
import com.icdats.service.UserService;
import com.icdats.utils.TimeTableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private S_C_ScoreService s_C_ScoreService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    @Override
    public Student getStudentBySid(Integer sid) {
        Student student = studentMapper.getStudentBySid(sid);
        Map<Course, AllScore> mapCScoreBySid = s_C_ScoreService.getMapCScoreBySid(sid);
        student.setCourses(mapCScoreBySid);
        Map<Course, AllScore> coursesMap = student.getCourses();
        Set<Course> courses = coursesMap.keySet();
        List<TimeTable> timeTableListByCourses = TimeTableUtil.getTimeTableListByCourses(courses);
        student.setTimeTables(timeTableListByCourses);
        return student;
    }

    @Override
    public List<Student> getStudentListByCname(String cname) {
        Integer cidByCname = courseService.getCidByCname(cname);
        List<S_C_Score> list = s_C_ScoreService.getSelectedS_C_Score(cidByCname);
        ArrayList<Student> students = new ArrayList<>();
        for (S_C_Score s_c_score : list) {
            Student studentBySid = getStudentBySid(s_c_score.getSid());
            students.add(studentBySid);
        }
        return students;
    }

    @Override
    public List<Integer> getSidByMoHuName(String moHuName) {
        List<Student> students = studentMapper.getSidByMoHuName(moHuName);
        ArrayList<Integer> sids = new ArrayList<>();
        for (Student student : students) {
            sids.add(student.getSid());
        }

        return sids;
    }

    @Override
    public List<Student> getStudentListBySelectNameAndValue(String selectName, String value) {
        List<Student> students = new ArrayList<>();
        if (selectName.equals("??????")) {
            students = studentMapper.getStudentListByMoHuName("");
        } else if (selectName.equals("??????")) {
            if (!(value.matches("^[0-9]+$"))) {
                return students;
            }
            students.add(studentMapper.getStudentDetailBySid(Integer.parseInt(value)));
        } else if (selectName.equals("??????")) {
            students = studentMapper.getStudentListByMoHuName(value);
        } else if (selectName.equals("??????")) {
            students = studentMapper.getStudentListByMoHuMajor(value);
        }
        return students;
    }

    @Override
    public void deleteStudent(Integer sid) {
        studentMapper.deleteStudentBySid(sid);
//        userService.deleteUserById(sid);
        s_C_ScoreService.deleteS_C_ScoreBySid(sid);
    }

    @Override
    public void updateStudent(Integer sid, String name, String sex, String major, String grade, String sclass, String acount, String pwd) {
        studentMapper.updateStudentBySid(sid,name,sex,major,grade,sclass);
//        userService.updateUserById(sid,acount,pwd);
    }

    @Override
    public boolean addStudent(Integer sid, String name, String sex, String major, String grade, String sclass, String pwd) {
//        boolean ok = userService.addUser("s" + sid, pwd, "1",sid);
        //????????????????????????id???ok???false?????????student???????????????sid??????????????????
//        if(!ok || studentMapper.getStudentBySid(sid)!=null){
//            return false;
//        }
        studentMapper.addStudent(sid,name,sex,major,grade,sclass);
        return true;
    }
}
