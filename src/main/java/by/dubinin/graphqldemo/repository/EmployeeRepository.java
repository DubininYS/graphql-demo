package by.dubinin.graphqldemo.repository;

import by.dubinin.graphqldemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartment_IdIn(List<Long> departmentIds);
}
