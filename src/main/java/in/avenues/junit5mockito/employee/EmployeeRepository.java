package in.avenues.junit5mockito.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Serializable> {

    List<Employee> findByAddress(String address);
    long countById(Long id);
}
