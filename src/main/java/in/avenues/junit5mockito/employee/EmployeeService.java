package in.avenues.junit5mockito.employee;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public Boolean save(EmployeeDTO payload) {
        try {
            Employee employee = modelMapper.map(payload, Employee.class);
            Employee savedEmployee = employeeRepository.save(employee);
            return savedEmployee != null ? true : false;
        } catch (Exception ex) {
            ex.printStackTrace();
            //LOGGER
        }
        return false;
    }

    public Boolean update(EmployeeDTO payload) {
        try {
            if (payload.getId() != null) {
                Employee employee = getEntityById(payload.getId());
                Employee savedEmployee = employeeRepository.save(employee);
                return savedEmployee != null ? true : false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //LOGGER
        }
        return false;
    }

    private Employee getEntityById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        return employeeOptional.isPresent() ? employeeOptional.get() : null;
    }

    public EmployeeDTO findById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        return employeeOptional.isPresent() ?
                modelMapper.map(employeeOptional.get(), EmployeeDTO.class) :
                null;
    }

    public List<EmployeeDTO> findAllEmployee() {
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList.stream().map(employee -> modelMapper.map(
                employee, EmployeeDTO.class))
                .collect(Collectors.toList());

    }

    public List<EmployeeDTO> findByAddress(String address) {
        List<Employee> employeeList = employeeRepository.findByAddress(address);
        if (!employeeList.isEmpty()) {
            return employeeList.stream().map(
                    employee -> modelMapper.map(employee, EmployeeDTO.class))
                    .collect(Collectors.toList());
        }
        return null;

    }

    public Boolean deleteEmployee(Long id) {
        Employee employee = getEntityById(id);
        if (employee != null) {
            employeeRepository.deleteById(id);
            return getCountById(id) == 0 ? true : false;
        }
        return false;
    }

    private Long getCountById(Long id) {
        return employeeRepository.countById(id);
    }
}
