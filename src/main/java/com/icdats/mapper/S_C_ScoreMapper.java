package com.icdats.mapper;

import com.icdats.pojo.AllScore;
import com.icdats.pojo.Course;
import com.icdats.pojo.S_C_Score;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface S_C_ScoreMapper {
    //通过学号查询学号与课程号及成绩的记录
    public List<S_C_Score> getCScoreBySid(@Param("sid")Integer sid);

    //通过学号以及课程号，添加一个记录
    public void addCScore(@Param("sid")Integer sid, @Param("cid")Integer cid);

    //通过课程号查询该课程已选人数
    public Integer getCountOfCourse(@Param("cid")Integer cid);

    //通过课程号及学号删除选课记录
    public void deleteCScoreBySidAndCid(@Param("sid")Integer sid,@Param("cid")Integer cid);

    //通过课程号查询符合的记录
    public List<S_C_Score> getSelectedS_C_Score(@Param("cid")Integer cid);

    //通过学号以及课程号获取选课记录
    public S_C_Score getS_C_ScoreBySidAndCid(@Param("sid")Integer sid,@Param("cid")Integer cid);

    //通过学号，课程号及需修改的期末成绩值来修改期末成绩
    public void changeScore(@Param("sid")Integer sid,@Param("cid")Integer cid, @Param("newScore")Double newScore);

    //通过学号，课程号及需修改的期末成绩值来修改平时成绩
    public void changeUScore(@Param("sid")Integer sid,@Param("cid")Integer cid, @Param("newScore")Double newUScore);

    //通过课程名删除对应所有记录
    public void deleteS_C_ScoreByCname(@Param("cid")Integer cid);

    //根据学号删除对应所有记录
    public void deleteS_C_ScoreBySid(@Param("sid")Integer sid);
}
