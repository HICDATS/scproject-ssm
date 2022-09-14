package com.icdats.service.impl;

import com.icdats.mapper.S_C_ScoreMapper;
import com.icdats.pojo.AllScore;
import com.icdats.pojo.Course;
import com.icdats.pojo.S_C_Score;
import com.icdats.service.S_C_ScoreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class S_C_ScoreServiceImpl implements S_C_ScoreService {

    private S_C_ScoreMapper s_C_ScoreMapper;
    @Override
    public List<S_C_Score> getCScoreBySid(Integer sid) {
        return s_C_ScoreMapper.getCScoreBySid(sid);
    }

    @Override
    public Map<Course, AllScore> getMapCScoreBySid(Integer sid) {
        return s_C_ScoreMapper.getMapCScoreBySid(sid);
    }

    @Override
    public void addCScore(Integer sid, Integer cid) {
        s_C_ScoreMapper.addCScore(sid,cid);
    }

    @Override
    public Integer getCountOfCourse(Integer cid) {
        return s_C_ScoreMapper.getCountOfCourse(cid);
    }

    @Override
    public void deleteCScoreBySidAndCid(Integer sid, Integer cid) {
        s_C_ScoreMapper.deleteCScoreBySidAndCid(sid,cid);
    }

    @Override
    public List<S_C_Score> getSelectedS_C_Score(Integer cid) {
        return s_C_ScoreMapper.getSelectedS_C_Score(cid);
    }

    @Override
    public S_C_Score getS_C_ScoreBySidAndCid(Integer sid, Integer cid) {
        return s_C_ScoreMapper.getS_C_ScoreBySidAndCid(sid,cid);
    }

    @Override
    public void changeScore(Integer sid, Integer cid, Double newScore) {
        s_C_ScoreMapper.changeScore(sid,cid,newScore);
    }

    @Override
    public void changeUScore(Integer sid, Integer cid, Double newUScore) {
        s_C_ScoreMapper.changeUScore(sid,cid,newUScore);
    }

    @Override
    public void deleteS_C_ScoreByCname(Integer cid) {
        s_C_ScoreMapper.deleteS_C_ScoreByCname(cid);
    }

    @Override
    public void deleteS_C_ScoreBySid(Integer sid) {
        s_C_ScoreMapper.deleteS_C_ScoreBySid(sid);
    }


}
