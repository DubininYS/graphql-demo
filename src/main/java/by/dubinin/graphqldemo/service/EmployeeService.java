package by.dubinin.graphqldemo.service;

import by.dubinin.graphqldemo.entity.Employee;
import by.dubinin.graphqldemo.repository.EmployeeRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotation.GraphQLApi;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@GraphQLApi
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GraphQLQuery(name = "employees")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @GraphQLQuery(name = "employee")
    public Optional<Employee> getEmployeeById(@GraphQLArgument(name = "id") Long id) {
        return employeeRepository.findById(id);
    }

    @GraphQLMutation
    public Employee addEmployee(@GraphQLArgument(name = "employee") Employee employee) {
        return employeeRepository.save(employee);
    }

    @GraphQLMutation
    public void deleteEmployee(@GraphQLArgument(name = "id") Long id) {
        employeeRepository.deleteById(id);
    }

//    @GraphQLQuery(name = "employeesPaged")
//    public Page<Employee> getEmployees(@GraphQLArgument(name = "pageable") Pageable pageable) {
//        return employeeRepository.findAll(pageable);
//    }

}
