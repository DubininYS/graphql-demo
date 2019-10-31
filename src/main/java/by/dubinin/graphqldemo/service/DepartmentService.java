package by.dubinin.graphqldemo.service;

import by.dubinin.graphqldemo.entity.Department;
import by.dubinin.graphqldemo.entity.Employee;
import by.dubinin.graphqldemo.exception.ResourceNotFoundException;
import by.dubinin.graphqldemo.repository.DepartmentRepository;
import by.dubinin.graphqldemo.repository.EmployeeRepository;
import io.leangen.graphql.annotations.*;
import io.leangen.graphql.spqr.spring.annotation.GraphQLApi;
import io.leangen.graphql.spqr.spring.util.ConcurrentMultiRegistry;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@GraphQLApi
public class DepartmentService {

    private final ConcurrentMultiRegistry<Long, FluxSink<Department>> subscribers = new ConcurrentMultiRegistry<>();
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;


    public DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @GraphQLQuery(name = "departments")
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    @GraphQLQuery(name = "department")
    public Optional<Department> getDepartmentById(@GraphQLArgument(name = "id") Long id) {
        return departmentRepository.findById(id);
    }

    @GraphQLQuery(name = "employees")
    public List<Employee> getEmployeeListByDepartment(@GraphQLContext Department department) {
        return department.getEmployeeList();
    }

    @GraphQLQuery(name = "employeeCount")
    public BigDecimal getEmployeeCountByDepartment(@GraphQLContext Department department) {
        long count = department.getEmployeeList().stream().count();
        return new BigDecimal(count);
    }

    @GraphQLMutation
    public Department addDepartment(@GraphQLArgument(name = "department") Department department) {
        return departmentRepository.save(department);
    }

    @GraphQLMutation
    public Department addEmployeeToDepartment(@GraphQLNonNull Long departmentId, @GraphQLNonNull Long employeeId) {
        return departmentRepository.findById(departmentId)
                .map(department -> {
                            final Employee employeeById = employeeRepository.findById(employeeId)
                                    .map(employee -> {
                                        employee.setDepartment(department);
                                        return employeeRepository.save(employee);
                                    })
                                    .orElseThrow(() -> new ResourceNotFoundException("EmployeeId " + employeeId + " not found"));
                            department.getEmployeeList().add(employeeById);
                            subscribers.get(departmentId).forEach(subscriber -> subscriber.next(department));
                            return department;
                        }
                )
                .orElseThrow(() -> new ResourceNotFoundException("DepartmentId " + departmentId + " not found"));
    }

    @GraphQLMutation
    public void deleteDepartment(@GraphQLArgument(name = "id") Long id) {
        final Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DepartmentId " + id + " not found"));
        departmentRepository.delete(department);
    }

    @GraphQLSubscription
    public Publisher<Department> employeeAdded(@GraphQLArgument(name = "departmentId") Long departmentId) {
        return Flux.create(subscriber -> subscribers.add(
                departmentId,
                subscriber.onDispose(() -> subscribers.remove(departmentId, subscriber))),
                FluxSink.OverflowStrategy.LATEST);
    }
}
