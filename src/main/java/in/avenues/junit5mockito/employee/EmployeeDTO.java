package in.avenues.junit5mockito.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;
    private String name;
    @NotNull(message = "Email is mandatory")
    private String email;
    private String address;
}
