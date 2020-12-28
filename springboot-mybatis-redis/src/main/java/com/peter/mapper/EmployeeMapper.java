package com.peter.mapper;

import com.peter.model.Employee;

import java.util.List;

public interface EmployeeMapper {

    List<Employee> getEmpById(Integer id);
}
