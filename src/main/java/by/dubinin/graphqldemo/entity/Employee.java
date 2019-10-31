package by.dubinin.graphqldemo.entity;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLQuery(name = "id", description = "A employee's id")
    private Long id;
    @GraphQLQuery(name = "name", description = "A employee's name")
    private String name;
    @GraphQLQuery(name = "email", description = "A employee's email")
    private String email;
    @GraphQLQuery(name = "department", description = "A employee's department")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;
    @GraphQLQuery(name = "dateOfBirth", description = "A employee's date of birthday")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;
}
