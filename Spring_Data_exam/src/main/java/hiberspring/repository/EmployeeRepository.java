package hiberspring.repository;

import hiberspring.domain.dtos.EmployeeExportDto;
import hiberspring.domain.dtos.products_import.ExportEmployeeDto;
import hiberspring.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee AS e INNER JOIN e.card WHERE e.card.number = :cardNumber")
    Optional<Employee> findByCard(@Param("cardNumber") String card);

//    @Query(value = "" +
//            "SELECT hiberspring.domain.dtos.EmployeeExportDto() " +
//            "FROM employees AS e " +
//            "INNER JOIN branches AS b " +
//            "ON e.branch = b.id " +
//            "INNER JOIN products AS p " +
//            "ON b.id = p.branch " +
//            "ORDER BY CONCAT(e.first_name, ' ', e.last_name), CHAR_LENGTH(e.position) DESC ", nativeQuery = true)
//    @Query("SELECT hiberspring.domain.dtos.EmployeeExportDto(e.firstName, e.lastName, e.position, ec.number) " +
//            "FROM Employee AS e " +
//            "INNER JOIN Branch AS b " +
//            "INNER JOIN EmployeeCard AS ec " +
//            "INNER JOIN Product AS p " +
//            "ON p.branch = b.id " +
//            "ORDER BY CONCAT(e.firstName, ' ', e.lastName), CHAR_LENGTH(e.position) DESC ")


//    @Query(value = "SELECT e.first_name, e.last_name, e.position, ec.number " +
//            "FROM employees AS e " +
//            "INNER JOIN branches AS b " +
//            "ON e.branch = b.id " +
//            "INNER JOIN products AS p " +
//            "ON b.id = p.branch " +
//            "INNER JOIN employee_cards AS ec " +
//            "ON e.card = ec.id " +
//            "ORDER BY CONCAT(e.first_name, ' ', e.last_name), CHAR_LENGTH(e.position) DESC", nativeQuery = true)
    @Query("SELECT e " +
            "FROM Employee AS e " +
            "INNER JOIN e.branch AS b " +
            "INNER JOIN Product AS p " +
            "ON b.id = p.branch " +
            "ORDER BY CONCAT(e.firstName, ' ', e.lastName), CHAR_LENGTH(e.position) DESC ")
    List<Employee> exportProductiveEmployees();
}
