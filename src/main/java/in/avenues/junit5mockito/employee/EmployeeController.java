package in.avenues.junit5mockito.employee;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;
    private ModelMapper modelMapper;

    public EmployeeController(EmployeeService employeeService, ModelMapper modelMapper) {
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/test")
    public String getTest() {
        return "sanity check!";
    }

    @PostMapping("/save")
    public ResponseEntity<Boolean> saveEmployee(@Valid @RequestBody EmployeeDTO payload) {
        Boolean value = employeeService.save(payload);
        if (value == false) {
            return new ResponseEntity(
                    "Record not saved",
                    HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(value);
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateEmployee(@Valid @RequestBody EmployeeDTO payload) {
        Boolean value = employeeService.update(payload);
        if (value == false) {
            return new ResponseEntity(
                    "Record not updated",
                    HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(value);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDTO>> findAllEmployee() {
        List<EmployeeDTO> employeeDTOList = employeeService.findAllEmployee();
        return ResponseEntity.ok(employeeDTOList);
    }

    @GetMapping("by-id/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id) {
        EmployeeDTO employeeDTO = employeeService.findById(id);
        if (employeeDTO == null) {
            return new ResponseEntity(
                    "Record not found",
                    HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(employeeDTO);
    }

    @GetMapping("/by-address/{address}")
    public ResponseEntity<List<EmployeeDTO>> findByAddress(@PathVariable String address) {
        List<EmployeeDTO> employeeDTOList = employeeService.findByAddress(address);
        if (employeeDTOList == null) {
            return new ResponseEntity(
                    "Record not found for the address " + address,
                    HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(employeeDTOList);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        Boolean value = employeeService.deleteEmployee(id);
        if (value == false) {
            return new ResponseEntity(
                    "Record not deleted",
                    HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(value);
    }

}
