package by.dubinin.graphqldemo.entity;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLQuery(name = "id", description = "A department's id")
    private Long id;
    @GraphQLQuery(name = "name", description = "A department's name")
    private String name;
    @OneToMany(mappedBy = "department")
    private List<Employee> employeeList;

    @GraphQLIgnore
    public List<Employee> getEmployeeList() {
        return employeeList;
    }
}
