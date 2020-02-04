package com.hy.demo.service;

import com.hy.demo.data.dto.SysUserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void getUser() {
        SysUserDTO user = userService.getUser(1);
    }
}