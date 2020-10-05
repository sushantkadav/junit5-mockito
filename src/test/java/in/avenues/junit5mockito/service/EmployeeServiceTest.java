package in.avenues.junit5mockito.service;

import in.avenues.junit5mockito.employee.Employee;
import in.avenues.junit5mockito.employee.EmployeeDTO;
import in.avenues.junit5mockito.employee.EmployeeRepository;
import in.avenues.junit5mockito.employee.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
@DisplayName("Employee service")
class EmployeeServiceTest {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;


    @Test
    @DisplayName("Saved")
    public void shouldSavedEmployee() throws Exception {

        final Employee mockEmployee = new Employee(null, "vito", "vito@gmail.com", "mumbai");

        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);
        Boolean value = mockEmployee != null ? true : false;

        EmployeeDTO mockEmployeeDTO = modelMapper.map(mockEmployee, EmployeeDTO.class);
        assertEquals(value, employeeService.save(mockEmployeeDTO));
        verify(employeeRepository, times(1)).save(mockEmployee);

    }

    @Test
    @DisplayName("Update")
    public void shouldUpdateEmployeeTest() throws Exception {

        Employee mockEmployee = new Employee(101L, "vito", "vito@gmail.com", "mumbai");
        when(employeeRepository.findById(mockEmployee.getId())).thenReturn(Optional.of(mockEmployee));

        mockEmployee.setEmail("vito2020@gmail.com");
        when(employeeRepository.save(mockEmployee)).thenReturn(mockEmployee);
        Boolean value = mockEmployee != null ? true : false;

        EmployeeDTO mockEmployeeDTO = modelMapper.map(mockEmployee, EmployeeDTO.class);

        assertEquals(value, employeeService.update(mockEmployeeDTO));
        verify(employeeRepository, times(1)).save(mockEmployee);

    }

    @Test
    @DisplayName("Return Find All")
    public void shouldReturnFindAll() {
        when(employeeRepository.findAll()).thenReturn(Stream.of(
                new Employee((long) 101, "foo", "foo@gmail.com", "mumbai"),
                new Employee((long) 102, "voli", "voli@gmail.com", "pune"),
                new Employee((long) 103, "po", "po@gmail.com", "surat")
        ).collect(Collectors.toList()));

        assertEquals(3, employeeService.findAllEmployee().size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find by ID")
    public void findByEmployeeId() {

        Employee mockEmployee = new Employee(1L, "foo", "foo@gmail.com", "mumbai");
        when(employeeRepository.findById(mockEmployee.getId())).thenReturn(Optional.of(mockEmployee));

        EmployeeDTO employeeDTO = modelMapper.map(mockEmployee, EmployeeDTO.class);

        assertEquals(employeeDTO, employeeService.findById(mockEmployee.getId()));

        verify(employeeRepository, times(1)).findById(mockEmployee.getId());
    }

    @Test
    @DisplayName("Find by address")
    public void getByAddressTest() {
        String address = "mumbai";
        when(employeeRepository.findByAddress(address)).thenReturn(Stream.of(
                new Employee((long) 101, "foo", "foo@gmail.com", "mumbai"),
                new Employee((long) 102, "voli", "voli@gmail.com", "mumbai")
        ).collect(Collectors.toList()));

        assertEquals(2, employeeService.findByAddress(address).size());
        verify(employeeRepository, times(1)).findByAddress(address);
    }


    @Test
    @DisplayName("Delete")
    public void deleteEmployeeTest() {

        Employee mockEmployee = new Employee(1L, "foo", "foo@gmail.com", "mumbai");
        when(employeeRepository.findById(mockEmployee.getId())).thenReturn(Optional.of(mockEmployee));

        employeeService.deleteEmployee(mockEmployee.getId());
        employeeService.deleteEmployee(mockEmployee.getId());

        verify(employeeRepository, times(2)).deleteById(mockEmployee.getId());
    }

}
