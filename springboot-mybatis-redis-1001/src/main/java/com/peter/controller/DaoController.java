package com.peter.controller;

import com.peter.mapper.EmployeeMapper;
import com.peter.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/dao")
public class DaoController {

    private static final Logger logger = LoggerFactory.getLogger(DaoController.class);

    @Resource
    private RedisTemplate<String, Employee> redisTemplate;

    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping("/test1")
    public void test1(HttpServletRequest request, HttpServletResponse response) {
        List<Employee> employee = employeeMapper.getEmpById(1);
        logger.info(employee.toString());
    }

    @GetMapping("/test2")
    public void test2(HttpServletRequest request, HttpServletResponse response) {
        logger.info(redisTemplate.hasKey("1").toString());
        Employee employee = redisTemplate.opsForValue().get("1");
        if (employee == null) {
            employee = employeeMapper.getEmpById(1).get(0);
            redisTemplate.opsForValue().set("1", employee);
        }
        logger.info(employee.toString());
    }
}
