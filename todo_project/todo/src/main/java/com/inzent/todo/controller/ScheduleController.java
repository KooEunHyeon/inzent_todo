package com.inzent.todo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.inzent.todo.dto.ChkProjectDto;
import com.inzent.todo.dto.ChkSuperTasksDto;
import com.inzent.todo.dto.ClickDateDto;
import com.inzent.todo.dto.ScheduleDto;
import com.inzent.todo.security.Auth;
import com.inzent.todo.service.ScheduleService;
import com.inzent.todo.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Auth
    @GetMapping("/calendar")
    public List<ScheduleDto> getCalendatList(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        List<ScheduleDto> list = scheduleService.getCalendatList(userId);
        return list;
    }

    // 일정 등록 - 선택된 프로젝트에 관련된 업무대 출력
    @Auth
    @PostMapping("/chkproject")
    public List<ChkSuperTasksDto> getSuperTasks(@RequestBody ChkProjectDto chkprjdto, HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        chkprjdto.setId(userId);

        List<ChkSuperTasksDto> list = scheduleService.getSuperTasks(chkprjdto);
        return list;
    }

    @Auth
    @PostMapping("/clickdate")
    public List<ScheduleDto> getClickDateList(@RequestBody ClickDateDto cddto, HttpServletRequest req) {
        System.out.println("으으응?");
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        cddto.setId(userId);

        System.out.println("들어왓느냐~~~~~~~~" + cddto.toString());

        List<ScheduleDto> list = scheduleService.getClickDateList(cddto);
        return list;
    }

}