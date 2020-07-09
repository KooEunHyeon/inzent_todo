package com.inzent.todo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.inzent.todo.dto.DeptDto;
import com.inzent.todo.dto.PwdDto;
import com.inzent.todo.dto.UserDto;
import com.inzent.todo.repository.UserDao;
import com.inzent.todo.security.Auth;
import com.inzent.todo.service.JwtService;
import com.inzent.todo.service.UserService;
import com.inzent.todo.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @GetMapping("/api/hello")
    public String hello() {
        System.out.println("HELLO");
        System.out.println(userDao);
        userDao.selectDao();
        System.out.println("db 성공!");

        return "hello";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto user) {

        String token = null;

        // 들어온 로그인 정보(ID, PWD)로 DB에서 조회

        UserDto userToken = userService.getUserToken(user);

        Map<String, Object> map = new HashMap<String, Object>();

        System.out.println(userToken);

        // 존재/유효한 user가 있다면 token 생성
        if (userToken != null) {
            token = jwtService.createLoginToken(userToken);
            UserDto loginUser = userService.getLoginUser(user);
            loginUser.setPassword(null);
            map.put("accessToken", token);
            map.put("loginUser", loginUser);
        }

        return token != null ? new ResponseEntity<Object>(map, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Auth
    @PostMapping("/loginByToken")
    public UserVo login(HttpServletRequest req, @RequestBody String accessToken) {
        System.out.println("토큰이 이미 발급된 유저로그인");
        UserVo user = (UserVo) req.getAttribute("user");
        user = userService.getById(user.getId());
        user.setPassword(null);
        return user;
    }

    @PostMapping("/pwdCheck")
    public boolean pwdCheck(@RequestBody PwdDto pwdDto) {
        return userService.pwdCheck(pwdDto);
    }

    @GetMapping("/deptList")
    public List<DeptDto> getDeptList() {
        return userService.getDeptList();
    }

    @PostMapping("/userList")
    public List<UserDto> getUserList(@RequestBody String[] deptList) {
        return userService.getUserList(deptList);
    }

}