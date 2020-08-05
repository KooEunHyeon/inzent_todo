package com.inzent.todo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inzent.todo.dto.CalDateDetailDto;
import com.inzent.todo.dto.ChkProjectDto;
import com.inzent.todo.dto.ChkSuperTasksDto;
import com.inzent.todo.dto.ClickDateDto;
import com.inzent.todo.dto.FilterDto;
import com.inzent.todo.dto.GanttChartInfoDto;
import com.inzent.todo.dto.ScheduleDto;
import com.inzent.todo.vo.LabelVo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleDao {

    @Autowired
    private SqlSession sqlsession;

    // 필터없는 조회
    public List<ScheduleDto> getCalendarList(String userId) {
        List<ScheduleDto> prjInfo = sqlsession.selectList("calendar.getCalendarInfo", userId);
        return prjInfo;
    }

    // 필터 있는 조회
    public List<ScheduleDto> getCalendarFilterList(Map<String, Object> map) {
        List<ScheduleDto> prjInfo = sqlsession.selectList("calendar.getCalendarFilterInfo", map);
        return prjInfo;
    }

    // 업무등록의 프로젝트 조회
    public List<Map<String, Object>> getProjects(String userId) {
        List<Map<String, Object>> prjInfo = sqlsession.selectList("calendar.getProjects", userId);

        return prjInfo;
    }

    // 선택한 프로젝트의 업무대 조회
    public List<ChkSuperTasksDto> getSuperTasks(ChkProjectDto chkprjdto) {

        return sqlsession.selectList("calendar.getSuperTasks", chkprjdto);
    }

    // 선택한 프로젝트 정보 가지고 있기....
    public Map<String, Object> getChkProjectInfo(ChkProjectDto chkprjdto) {
        Map<String, Object> map = sqlsession.selectOne("calendar.getChkProjectInfo", chkprjdto);
        String pid = chkprjdto.getChkProject();
        List<LabelVo> labelVo = sqlsession.selectList("project.getLabelList", pid);
        map.put("labelVo", labelVo);
        return map;
    }

    // 선택한 상위업무 정보 가지고 있기....
    public String getSubSortNo(String chkSuperTask) {
        String sortNo = sqlsession.selectOne("calendar.getLastSortNo", chkSuperTask);
        return sortNo;
    }

    // 필터 없는 업무 대 상세 조회
    public List<CalDateDetailDto> getClickDateList(ClickDateDto cddto) {
        return sqlsession.selectList("calendar.getClickDateList", cddto);
    }

    // 필터 있는 업무 대 상세 조회
    public List<CalDateDetailDto> getClickDateFilterList(Map<String, Object> map) {
        return sqlsession.selectList("calendar.getClickDateFilterList", map);
    }

    public List<CalDateDetailDto> getSubList(String superId) {
        return sqlsession.selectList("calendar.getSubList", superId);
    }

    public List<FilterDto> getFilter(String userId) {
        return sqlsession.selectList("calendar.getFilter", userId);
    }

    // 캘린더 필터 아이템 추가
    public int addCalFilterItem(String calItem, String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("calItem", calItem);
        map.put("userId", userId);
        return sqlsession.insert("calendar.insertFilterItem", map);
    }

    // 필터 테이블 업데이트
    public int updateFilterItem(String calItem, String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("calItem", calItem);
        map.put("userId", userId);
        return sqlsession.update("calendar.updateFilterItem", map);
    }

    // 캘린더 필터 조회
    public String selectCalFilterItem(String userId) {
        return sqlsession.selectOne("calendar.selectFilterItem", userId);
    }

    // 로그인한 유저가 캘린더 필터를 넣었는지 확인?
    public String selectExistUser(String userId) {
        return sqlsession.selectOne("calendar.selectExistUser", userId);
    }

    public int resetCalFilter(String userId) {
        return sqlsession.update("calendar.deleteCalSetting", userId);
    }

    ////////////////////////////////////// 간트차트 //////////////////////////////

    public List<GanttChartInfoDto> getGanttChartSuperInfo(String userId) {
        return sqlsession.selectList("ganttchart.getGanttSuperTasksInfo", userId);
    }
    public List<GanttChartInfoDto> getGanttChartSubInfo(String userId) {
        return sqlsession.selectList("ganttchart.getGanttSubTasksInfo", userId);
    }
}