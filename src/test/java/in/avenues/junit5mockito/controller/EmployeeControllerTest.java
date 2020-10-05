package in.avenues.junit5mockito.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.avenues.junit5mockito.employee.EmployeeDTO;
import in.avenues.junit5mockito.employee.EmployeeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DisplayName("Employee Controller")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeControllerTest {

    private MockMvc mockMvc;
    private EmployeeDTO mockEmployeeDTO;
    private List<EmployeeDTO> mockEmployeeDTOList;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockEmployeeDTO = new EmployeeDTO((long) 101, "foo", "foo@gmail.com", "mumbai");

        mockEmployeeDTOList = Stream.of(
                new EmployeeDTO((long) 101, "foo", "foo@gmail.com", "mumbai"),
                new EmployeeDTO((long) 102, "voli", "voli@gmail.com", "pune"),
                new EmployeeDTO((long) 103, "po", "po@gmail.com", "surat")
        ).collect(Collectors.toList());

    }


    @Test
    @DisplayName("Get test")
    void getTest() throws Exception {
        mockMvc.perform(get("/employee/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("sanity check!"));
    }

    @Test
    @DisplayName("Create")
    void shouldCreateNewEmployee() {
        try {

            when(employeeService.save(Mockito.any(EmployeeDTO.class))).thenReturn(Boolean.TRUE);

            String jsonRequest = objectMapper.writeValueAsString(mockEmployeeDTO);

            mockMvc.perform(post("/employee/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));

            verify(employeeService, times(1)).save(Mockito.any(EmployeeDTO.class));
            verifyNoMoreInteractions(employeeService);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @Disabled
    void shouldReturn400WhenCreateNewEmployeeWithoutEmail() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO(null, "vini", null, "Name");

        mockMvc.perform(post("/employee/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", Matchers.is("application/problem+json")))
                .andExpect(jsonPath("$.title", Matchers.is("Constraint Violation")))
                .andExpect(jsonPath("$.status", Matchers.is(400)))
                .andExpect(jsonPath("$.violations", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field", Matchers.is("email")))
                .andExpect(jsonPath("$.violations[0].message", Matchers.is("Email should not be empty")))
                .andReturn()
        ;
    }

    @Test
    @DisplayName("Return 404 When Saving Employee")
    void shouldReturn404WhenSavingEmployee() throws Exception {

        when(employeeService.save(mockEmployeeDTO)).thenReturn(false);

        this.mockMvc.perform(post("/employee/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockEmployeeDTO)))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Update")
    void shouldUpdateEmployee() {
        try {
            Long employeeId = 101L;
            when(employeeService.findById(employeeId)).thenReturn(mockEmployeeDTO);
            when(employeeService.update(Mockito.any(EmployeeDTO.class))).thenReturn(Boolean.TRUE);

            String jsonRequest = objectMapper.writeValueAsString(mockEmployeeDTO);

            mockMvc.perform(post("/employee/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));

            verify(employeeService, times(1)).update(Mockito.any(EmployeeDTO.class));
            verifyNoMoreInteractions(employeeService);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @DisplayName("Return 404 When Updating Non Existing Employee")
    void shouldReturn404WhenUpdatingNonExistingEmployee() throws Exception {

        when(employeeService.update(mockEmployeeDTO)).thenReturn(false);

        this.mockMvc.perform(post("/employee/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockEmployeeDTO)))
                .andExpect(status().isNotFound());

    }


    @Test
    @DisplayName("Find by id")
    void shouldFindById() throws Exception {

        final Long employeeID = 101L;

        when(employeeService.findById(employeeID)).thenReturn(mockEmployeeDTO);

        mockMvc.perform(get("/employee/by-id/{id}", 101L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Matchers.is(mockEmployeeDTO.getName())))
                .andExpect(jsonPath("$.email", Matchers.is(mockEmployeeDTO.getEmail())))
                .andExpect(jsonPath("$.address", Matchers.is(mockEmployeeDTO.getAddress())));

        verify(employeeService, times(1)).findById(101L);
        verifyNoMoreInteractions(employeeService);

    }

    @Test
    @DisplayName("Return 404 When Find By Id")
    void shouldReturn404WhenFindById() throws Exception {
        final Long employeeId = 1L;
        when(employeeService.findById(employeeId)).thenReturn(null);

        this.mockMvc.perform(get("/employee/by-id/{id}", employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Fetch all")
    void shouldFetchAllEmployee() throws Exception {

        when(employeeService.findAllEmployee()).thenReturn(mockEmployeeDTOList);

        mockMvc.perform(get("/employee/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(mockEmployeeDTOList.size())));

        verify(employeeService, times(1)).findAllEmployee();
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    @DisplayName("Fetch by address")
    void shouldFetchByAddress() throws Exception {
        final String address = "surat";
        List<EmployeeDTO> mockEmployees = Arrays.asList(
                new EmployeeDTO((long) 103, "po", "po@gmail.com", "surat"));

        when(employeeService.findByAddress(address)).thenReturn(mockEmployees);

        mockMvc.perform(get("/employee/by-address/{address}", address))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(mockEmployees.size())));

        verify(employeeService, times(1)).findByAddress(address);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    @DisplayName("Return 404 for Find By Address")
    void shouldReturn404WhenFindByAddress() throws Exception {
        final String address = "mumbai";
        when(employeeService.findByAddress(address)).thenReturn(null);

        this.mockMvc.perform(get("/employee/by-address/{address}", address))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete by id")
    public void deleteByIdTest() throws Exception {
        Long employeeId = 1L;
        given(employeeService.deleteEmployee(employeeId)).willReturn(Boolean.TRUE);
        this.mockMvc.perform(post("/employee/delete/{id}", employeeId))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).deleteEmployee(employeeId);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    @DisplayName("Return 404 When Deleting Non Existing Employee")
    void shouldReturn404WhenDeletingNonExistingEmployee() throws Exception {
        Long employeeId = 1L;
        when(employeeService.deleteEmployee(employeeId)).thenReturn(false);

        this.mockMvc.perform(post("/employee/delete/{id}", employeeId))
                .andExpect(status().isNotFound());

    }

}