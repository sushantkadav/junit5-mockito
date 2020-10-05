package in.avenues.junit5mockito.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @NotNull(message = "Email is mandatory")
    private String email;
    private String address;
}
