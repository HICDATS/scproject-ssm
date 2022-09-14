package com.icdats.controller;

import com.icdats.json.ToJson;
import com.icdats.mapper.UserMapper;
import com.icdats.pojo.Student;
import com.icdats.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserMapper userMapper;

    @ResponseBody
    @RequestMapping("/getStudentsBycname")
    //当老师点击课程表，得到对应课程名，从而获取该课程的学生列表,存入session域
    //或者管理员点击课程列表上的学生名单，得到对应课程名，从而获取该课程的学生列表,存入session域
    public String getStudentsBycname(String cname, HttpSession session) {
        if (cname == null) {
            return "";
        }
        //因当老师点击课程表时，对应课程名称格式为 "高等数学上 6教-502" 所以需要进行格式变化改为"高等数学上"
        if(cname.indexOf(" ")!=-1){
            cname = cname.substring(0, cname.indexOf(" "));
        }
        //若当前为管理员点击至此，则课程名不用进行改变
        List<Student> studentListByCname = studentService.getStudentListByCname(cname);
        session.setAttribute("studentListByCname", studentListByCname);
        return "";
    }

    @ResponseBody
    @RequestMapping("/getDataOfStudents")
    //将之前获取到的某课程的学生列表从session域中取出，转换为json格式的数据传送至页面
    public ToJson getDataOfStudents(HttpSession session) {
        ToJson result = new ToJson();
        List<Student> studentListByCname = (List<Student>) session.getAttribute("studentListByCname");
        //获取后将session域中的studentListByCname清空
        session.setAttribute("studentListByCname", new ArrayList());
        if (studentListByCname.size() == 0) {
            return result;
        }
        result.setCount(studentListByCname.size());
        result.setData(studentListByCname);
        return result;
    }

    @ResponseBody
    @RequestMapping("dataOfStudentBySelected")
    //管理员界面查看学生用户的详细信息
    public ToJson dataOfStudentBySelected(HttpSession session) {
        //首先从session域中获取studentDetail以及修改的标记updatedStudentUser
        ToJson result = new ToJson();
        Object studentDetail = session.getAttribute("studentDetail");
        Object updated = session.getAttribute("updatedStudentUser");
        //若studentDetail不为空且updatedStudentUser为空则代表已经进行查询并且没有进行修改，则将域中的studentDetail作为数据送至前端页面
        if (studentDetail != null && updated == null) {
            List<Student> studentDetailList = (List<Student>) studentDetail;
            if (studentDetailList.size() == 0) {
                return result;
            }
            result.setCount(studentDetailList.size());
            result.setData(studentDetailList);
            return result;
        } else {
            session.setAttribute("updatedStudentUser", null);
            List<Student> allStudents = studentService.getStudentListBySelectNameAndValue("全部", "");
            //将查询到的信息存入session域方便下一次查看使用
            session.setAttribute("studentDetail", allStudents);
            if (allStudents.size() == 0) {
                return result;
            }
            result.setCount(allStudents.size());
            result.setData(allStudents);
            return result;
        }
    }

    @ResponseBody
    @RequestMapping("/selectStudentDetail")
    //管理员界面点击筛选学生用户展示的信息
    public String selectStudentDetail(String selectName, String value, HttpSession session) {
        List<Student> studentListBySelectNameAndValue = studentService.getStudentListBySelectNameAndValue(selectName, value);
        session.setAttribute("studentDetail", studentListBySelectNameAndValue);
        return "";
    }

    @ResponseBody
    @RequestMapping("/deleteStudent")
    //对学生信息进行删除
    public String deleteStudent(Integer sid, HttpSession session) {
        studentService.deleteStudent(sid);
        session.setAttribute("updatedStudentUser", true);
        return "";
    }

    @ResponseBody
    @RequestMapping("/updateStudent")
    //对学生信息进行修改
    public String updateStudent(Integer sid, String name, String sex, String major, String grade, String sclass , String acount, String pwd,HttpSession session) {
        studentService.updateStudent(sid,name,sex,major,grade,sclass,acount,pwd);
        session.setAttribute("updatedStudentUser", true);
        return "";
    }

    @ResponseBody
    @RequestMapping("/addStudent")
    //对学生信息进行添加
    public Map<String, Boolean> addStudent(Integer sid, String name, String sex, String major, String grade, String sclass, String pwd, HttpSession session){
        boolean ok = studentService.addStudent(sid, name, sex, major, grade, sclass, pwd);
        HashMap<String, Boolean> result = new HashMap<String, Boolean>();
        if(ok == false){
            result.put("add",false);
            return result;
        }else{
            session.setAttribute("updatedStudentUser", true);
            result.put("add",true);
            return result;
        }
    }
}
