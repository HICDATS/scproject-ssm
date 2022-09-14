package com.icdats.controller;

import com.icdats.json.ToJson;
import com.icdats.pojo.*;
import com.icdats.service.CourseService;
import com.icdats.service.S_C_ScoreService;
import com.icdats.service.StudentService;
import com.icdats.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@RequestMapping("/course")
@SuppressWarnings("all")
@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private S_C_ScoreService s_C_ScoreService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @ResponseBody
    @RequestMapping("/getCourseForStudent")
    //获取当前查询后session域中的课程信息
    public ToJson getCourseForStudent(HttpSession session){
        ToJson result = new ToJson();
        Object coursesForStudent = session.getAttribute("coursesForStudent");
        Object updated = session.getAttribute("updatedForStudentCourses");
        if(coursesForStudent!=null && updated==null){
            List<Course> courses = (List<Course>) coursesForStudent;
            if(courses.size()==0){
                return result;
            }
            result.setCount(courses.size());
            result.setData(courses);
            return result;
        }else{
            session.setAttribute("updatedForStudentCourses",null);
            List<Course> allCourse = courseService.getAllCourse();
            session.setAttribute("coursesForStudent",allCourse);
            if(allCourse.size()==0){
                return result;
            }
            result.setCount(allCourse.size());
            result.setData(allCourse);
            return result;
        }
    }

    @ResponseBody
    @RequestMapping("/getCourseBySelect")
    //通过筛选查询课程信息，为session域中课程对象赋值
    public String getCourseBySelect(String selectName,String value,HttpSession session) {
        if(selectName.equals("全部") || value.equals("")){
            session.setAttribute("coursesForStudent",null);
            return "";
        }
        List<Course> courses = courseService.getCoursesBySelectCidOrMoHuCname(selectName, value);
        session.setAttribute("coursesForStudent",courses);
        return "";
    }

    @ResponseBody
    @RequestMapping("/getSelectedCourse")
    //通过当前登录学生信息获取该学生已选课程信息
    public ToJson getSelectedCourse(HttpSession session) {
        ToJson result = new ToJson();
        Student currStudent = (Student) session.getAttribute("currStudent");
        Map<Course, AllScore> coursesMap = currStudent.getCourses();
        Set<Course> courses = coursesMap.keySet();
        if(courses.size()==0){
            return result;
        }
        result.setCount(courses.size());
        result.setData(courses);
        return result;
    }

    @ResponseBody
    @RequestMapping("/addCourse")
    //为当前学生添加课程信息
    public Map<String,Boolean> addCourse(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        //获取需要添加的课程号
        Integer cid = Integer.parseInt(request.getParameter("cid"));
        //通过课程号获取课程
        Course courseByCid = courseService.getCourseByCid(cid);
        //将该课程的上课时间与已选课程的上课时间进行比较，若相同则返回添加失败
        HashMap<String, Boolean> result = new HashMap<>();
        String[] timeArr = courseByCid.getTimeArr();
        Student currStudent = (Student) session.getAttribute("currStudent");
        Map<Course, AllScore> coursesMap = currStudent.getCourses();
        Set<Course> courses = coursesMap.keySet();
        for (String time : timeArr) {
            for (Course course : courses) {
                String[] myTimeArr = course.getTimeArr();
                List<String> myTimeList = Arrays.asList(myTimeArr);
                if(myTimeList.contains(time)){
                    result.put("no",true);
                    return result;
                } else {
                    continue;
                }
            }
        }
        //上课时间不冲突，继续添加课程
        s_C_ScoreService.addCScore(currStudent.getSid(), cid);
        //根据当前学生学号重新获取具体的学生信息
        Student student = studentService.getStudentBySid(currStudent.getSid());
        //根据当前学生学号重新获取具体的学生信息
        session.setAttribute("currStudent", student);
        //设置学生课程已改变的标记，便于刷新展示数据
        session.setAttribute("updatedForStudentCourses", true);
        return result;
    }

    @ResponseBody
    @RequestMapping("/deleteCourse")
    //学生选择退选选课程
    public String deleteCourse(HttpServletRequest request, HttpSession session) {
        Integer cid = Integer.parseInt(request.getParameter("cid"));
        Student currStudent = (Student) session.getAttribute("currStudent");
        s_C_ScoreService.deleteCScoreBySidAndCid(currStudent.getSid(),cid);
        Student student = studentService.getStudentBySid(currStudent.getSid());//根据当前学生学号重新获取具体的学生信息
        session.setAttribute("currStudent", student);//将学生实体存入session中
        session.setAttribute("updatedForStudentCourses", true);//设置学生课程已改变的标记，便于刷新展示数据
        return "";
    }

    @ResponseBody
    @RequestMapping("/getTimeTableForStudent")
    //查询当前学生用户课表
    public ToJson getTimeTableForStudent(HttpSession session){
        Student currStudent =  (Student)session.getAttribute("currStudent");
        List<TimeTable> timeTables = currStudent.getTimeTables();
        ToJson result = new ToJson();
        if(timeTables.size()==0){
            return result;
        }
        result.setCount(timeTables.size());
        result.setData(timeTables);
        return result;
    }

    @ResponseBody
    @RequestMapping("/getTimeTableForTeacher")
    //查询当前老师用户课表
    public ToJson getTimeTableForTeacher(HttpSession session){
        Teacher currTeacher = (Teacher) session.getAttribute("currTeacher");
        List<TimeTable> timeTables = currTeacher.getTimeTables();
        ToJson result = new ToJson();
        if(timeTables.size()==0){
            return result;
        }
        result.setCount(timeTables.size());
        result.setData(timeTables);
        return result;
    }

    @ResponseBody
    @RequestMapping("/dataOfNeedAuditCourseByUser")
    //查询当前登录老师的待审核课程
    public ToJson dataOfNeedAuditCourseByUser(HttpSession session){
        Teacher currTeacher = (Teacher) session.getAttribute("currTeacher");
        Integer tid = currTeacher.getTid();
        List<Course> courses = courseService.getNeedAuditCourseByTid(tid);
        ToJson result = new ToJson();
        if(courses.size()==0){
            return result;
        }
        result.setCount(courses.size());
        result.setData(courses);
        return result;
    }

    @ResponseBody
    @RequestMapping("/cancelNeedAuditCourse")
    //老师取消待审核课程
    public String cancelNeedAuditCourse(String cname){
        courseService.deleteNeedAuditCourseByCname(cname);
        return "";
    }

    @ResponseBody
    @RequestMapping("/applyCourse")
    //老师提交课程申请信息
    public Map<String,Boolean> applyCourse(Integer tid,String cname,String time,String address,Integer cap,HttpSession session){
        Integer tid2 = tid;
        Teacher currTeacher = (Teacher) session.getAttribute("currTeacher");
        if(currTeacher != null){
            tid2 = currTeacher.getTid();
        }
        time = time.substring(0,time.length()-1);
        HashMap<String, Boolean> result = new HashMap<>();
        if(courseService.getCourseByAddressAndTime(address,time).size()!=0){
            result.put("exist",true);
            return result;
        }
        courseService.addNeedAuditCourse(cname,time,address,cap,tid2);
        result.put("exist",false);
        return result;
    }

    @ResponseBody
    @RequestMapping("/dataOfCourseByCurrTeacher")
    //老师查看所授课程信息
    public ToJson dataOfCourseByCurrTeacher(HttpSession session){
        Teacher currTeacher = (Teacher) session.getAttribute("currTeacher");
        List<Course> courses = currTeacher.getCourses();
        ToJson result = new ToJson();
        if(courses.size()==0){
            return result;
        }
        result.setCount(courses.size());
        for (Course course : courses) {
            Integer countOfCourse = s_C_ScoreService.getCountOfCourse(course.getCid());
            course.setSnum(countOfCourse);
        }
        result.setData(courses);
        return result;
    }

    @ResponseBody
    @RequestMapping("/deleteCourseByTeacher")
    //老师删除课程
    public String deleteCourseByTeacher(String cname,HttpSession session){
        courseService.deleteCourseByCname(cname);
        Teacher currTeacher = (Teacher) session.getAttribute("currTeacher");
        Teacher teacher = teacherService.getTeacher(currTeacher.getTid());//根据账号的id信息获取具体的教师信息
        session.setAttribute("currTeacher", teacher);//将教师实体存入session中
        return "";
    }

    @ResponseBody
    @RequestMapping("/dateOfAllNeedAuditCourse")
    //获取所有的带审核课程
    public ToJson dateOfAllNeedAuditCourse(){
        List<Course> allNeedAuditCourse = courseService.getAllNeedAuditCourse();
        ToJson result = new ToJson();
        if(allNeedAuditCourse.size()==0){
            return result;
        }
        result.setCount(allNeedAuditCourse.size());
        for (Course course : allNeedAuditCourse) {
            Integer countOfCourse = s_C_ScoreService.getCountOfCourse(course.getCid());
            course.setSnum(countOfCourse);
            String teacherName = teacherService.getTeacherName(course.getTid());
            course.setTeachername(teacherName);
        }
        result.setData(allNeedAuditCourse);
        return result;
    }

    @ResponseBody
    //管理员删除待审核课程
    @RequestMapping("denyNeedAuditCourse")
    public String denyNeedAuditCourse(String cname){
        courseService.deleteNeedAuditCourseByCname(cname);
        return "";
    }

    @ResponseBody
    @RequestMapping("/agreeNeedAuditCourse")
    //管理员通过待审核课程
    public String agreeNeedAuditCourse(String cname,String time,String address,Integer cap,Integer tid){
        courseService.agreeNeedAuditCourse(cname,time,address,cap,tid);
        return "";
    }

    @ResponseBody
    @RequestMapping("/addCourseOfRoot")
    public Map<String,String> addCourseOfRoot(String cname,String time,String address,Integer cap,Integer tid,HttpSession session){
        HashMap<String, String> result = new HashMap<>();
        if(courseService.getCourseByAddressAndTime(address,time).size()!=0){
            result.put("add","conflict");
            return result;
        } else if (courseService.getCidByCname(cname) != -1) {
            result.put("add","exist");
            return result;
        } else if (teacherService.getTeacherName(tid) == null) {
            result.put("add","noteacher");
            return result;
        }
        courseService.addCourse(cname,time,address,cap,tid);
        session.setAttribute("updatedForStudentCourses",true);
        result.put("add","true");
        return result;
    }

    @ResponseBody
    @RequestMapping("/deleteCourseOfRoot")
    //管理员删除课程
    public String deleteCourseOfRoot(String cname,HttpSession session){
        courseService.deleteCourseByCname(cname);
        session.setAttribute("updatedForStudentCourses",true);
        return "";
    }

}
