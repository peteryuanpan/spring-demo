package mybatis;

import java.util.List;

public interface EmployeeMapper {

    List<Employee> getEmpById(Integer id);
}
