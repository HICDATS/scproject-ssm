package com.icdats.controller;

import com.icdats.json.ToJson;
import com.icdats.pojo.Teacher;
import com.icdats.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("all")
@Controller
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @ResponseBody
    @RequestMapping("/dataOfTeacherBySelected")
    //管理员界面查看教师用户的详细信息
    public ToJson dataOfTeacherBySelected(HttpSession session) {
        //首先从session域中获取teacherDetail以及修改的标记updatedTeacherUser
        Object teacherDetail = session.getAttribute("teacherDetail");
        Object updated = session.getAttribute("updatedTeacherUser");
        ToJson result = new ToJson();
        //若studentDetail不为空且updatedStudentUser为空则代表已经进行查询并且没有进行修改，则将域中的studentDetail作为数据送至前端页面
        if (teacherDetail != null && updated == null) {
            List<Teacher> teacherDetailList = (List<Teacher>) teacherDetail;
            if (teacherDetailList.size() == 0) {
                return result;
            }
            result.setCount(teacherDetailList.size());
            result.setData(teacherDetailList);
            return result;
        } else {
            session.setAttribute("updatedTeacherUser", null);
            List<Teacher> allTeachers = teacherService.getTeacherListBySelectNameAndValue("全部", "");
            //将查询到的信息存入session域方便下一次查看使用
            session.setAttribute("teacherDetail", allTeachers);
            if (allTeachers.size() == 0) {
                return result;
            }
            result.setCount(allTeachers.size());
            result.setData(allTeachers);
            return result;
        }
    }

    @ResponseBody
    @RequestMapping("/selectTeacherDetail")
    //管理员界面点击筛选学生用户展示的信息
    public String selectTeacherDetail(String selectName, String value, HttpSession session) {
        List<Teacher> teacherListBySelectNameAndValue = teacherService.getTeacherListBySelectNameAndValue(selectName, value);
        session.setAttribute("teacherDetail", teacherListBySelectNameAndValue);
        return "";
    }

    @ResponseBody
    @RequestMapping("/deleteTeacher")
    //对教师信息进行删除
    public String deleteTeacher(Integer tid, HttpSession session) {
        teacherService.deleteTeacher(tid);
        session.setAttribute("updatedTeacherUser", true);
        return "";
    }

    @ResponseBody
    @RequestMapping("/updateTeacher")
    //对教师信息进行修改
    public String updateTeacher(Integer tid, String name, String sex, String college, String jobTitle , String acount, String pwd,HttpSession session) {
        teacherService.updateTeacher(tid,name,sex,college,jobTitle,acount,pwd);
        session.setAttribute("updatedTeacherUser", true);
        return "";
    }


    @ResponseBody
    @RequestMapping("/addTeacher")
    //对教师信息进行添加
    public Map<String, Boolean> addTeacher(Integer tid, String name, String sex, String college, String jobTitle, String pwd, HttpSession session){
        boolean ok = teacherService.addTeacher(tid, name, sex, college, jobTitle, pwd);
        HashMap<String, Boolean> result = new HashMap<>();
        if(ok == false){
            result.put("add",false);
            return result;
        }else{
            session.setAttribute("updatedTeacherUser", true);
            result.put("add",true);
            return result;
        }
    }
}
