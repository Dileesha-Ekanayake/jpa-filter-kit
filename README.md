# JPA Filter Kit

> A powerful and flexible library for building dynamic JPA Specifications with field mapping, nested filtering, and date range support.

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-green.svg)](https://spring.io/projects/spring-boot)
[![JPA](https://img.shields.io/badge/JPA-2.0+-blue.svg)](https://www.oracle.com/java/technologies/persistence-jsp.html)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 🚀 Features

- **Dynamic JPA Specifications**: Build complex queries dynamically using the Criteria API
- **Field Mapping**: Map client-friendly field names to actual entity field names
- **Nested Field Support**: Filter on nested entity relationships with automatic JOIN handling
- **Date Range Filtering**: Built-in support for date range queries with configurable suffixes
- **Numeric Field Handling**: Special handling for numeric fields with exact matching
- **Thread-Safe**: Concurrent access support with thread-safe field mappings
- **Fluent Builder API**: Easy configuration with a fluent builder pattern
- **Spring Boot Integration**: Seamless integration with Spring Boot applications

## 📦 Installation

### Maven
```xml
<dependency>
    <groupId>io.github.dileesha-ekanayake</groupId>
    <artifactId>specification-builder</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle
```gradle
dependencies {
    implementation("io.github.dileesha-ekanayake:specification-builder:1.0.0")
}
```

## 🛠️ Quick Start

### 1. Configuration

Create a configuration class to set up your filter mappings:

```java
@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private void setupFilters() {
        SpecificationBuilder specBuilder = new SpecificationBuilder();

        // Register numeric fields (for exact matching)
        List<String> numericFields = Arrays.asList("id", "count", "age");
        specBuilder.registerNumericField(numericFields);

        // Configure date suffixes for range filtering
        Map<String, Boolean> dateSuffixes = new HashMap<>();
        dateSuffixes.put("-from", true);   // Start date
        dateSuffixes.put("-to", false);    // End date
        specBuilder.registerDateSuffixes(dateSuffixes);

        // Register field mappings
        specBuilder.registerNestedField("genderid", "gender.id");
        specBuilder.registerDateField("dobirth", "dateOfBirth");
        specBuilder.registerNestedField("roleid", "userRoles.role.id");
        specBuilder.registerSimpleField("name", "fullName");
    }
}
```

### 2. Using with Spring Data JPA Repository

```java
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees(@RequestParam(required = false) HashMap<String, String> filters) {
        List<EmployeeResponseDTO> employeeResponseDTOS = employeeService.getAllEmployees(filters);
        return ResponseEntity.ok(employeeResponseDTOS);
    }
}
```

### 3. Repository Interface

```java
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>,
                                        JpaSpecificationExecutor<Employee> {
}
```

## 🎯 Usage Examples

### Simple Field Filtering

**Request:**
```
GET /http://localhost:8080/employees?name=nimal
```

**Generated Query:**
```sql
SELECT * FROM employee 
WHERE LOWER(name) LIKE '%nimal%'
```

### Nested Field Filtering

**Request:**
```
GET /http://localhost:8080/users?roleid=2
```

**Generated Query:**
```sql
SELECT * FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN role r ON ur.role_id = r.id
WHERE r.id = '2'
```

### Date Range Filtering

**Request:**
```
GET /http://localhost:8080/employees?dobirth-from=1990-01-01&dobirth-to=2000-12-12
```

**Generated Query:**
```sql
SELECT * FROM users 
WHERE date_of_birth >= '1990-01-01' 
  AND date_of_birth <= '2000-12-12'
```

### Numeric Field Filtering

**Request:**
```
GET /http://localhost:8080/eusers?id=123
```

**Generated Query:**
```sql
SELECT * FROM users 
WHERE id = '123'
```

## 🔧 Advanced Configuration

### Using Builder Pattern

```java
@Bean
public SpecificationBuilder userSpecificationBuilder() {
    return SpecificationBuilder.builder()
            .withNumericFields(Arrays.asList("id", "count"))
            .withNestedField("genderid", "gender.id")
            .withNestedField("roleid", "userRoles.role.id")
            .withDateField("dobirth", "dobirth")
            .withDateSuffixes(Map.of("-from", true, "-to", false))
            .build();
}
```

### Multiple Date Suffixes

```java
Map<String, Boolean> dateSuffixes = new HashMap<>();
dateSuffixes.put("-from", true);      // Start date
dateSuffixes.put("-to", false);       // End date
dateSuffixes.put("-start", true);     // Alternative start
dateSuffixes.put("-end", false);      // Alternative end
dateSuffixes.put("-after", true);     // After date
dateSuffixes.put("-before", false);   // Before date

specBuilder.registerDateSuffixes(dateSuffixes);
```

### Complex Nested Relationships

```java
// Deep nested relationship: user -> profile -> address -> city
specBuilder.registerNestedField("cityid", "profile.address.city.id");

// Multiple level joins: user -> userRoles -> role -> permissions
specBuilder.registerNestedField("permissionid", "userRoles.role.permissions.id");
```

## 📋 API Reference

### SpecificationBuilder Methods

| Method | Description |
|--------|-------------|
| `registerSimpleField(clientKey, actualFieldName)` | Register a simple field mapping |
| `registerNestedField(clientKey, actualFieldPath)` | Register a nested field with dot notation |
| `registerDateField(clientKey, actualFieldName)` | Register a date field for range filtering |
| `registerNumericField(fields)` | Register fields that should use exact matching |
| `registerDateSuffixes(suffixes)` | Configure date range suffixes |
| `createFilterSpecifications(filters)` | Create JPA Specification from filter map |
| `clearFieldMapping()` | Clear all field mappings |
| `clearDateSuffix()` | Clear all date suffixes |

### Field Types

#### Simple Fields
Fields that are directly on the entity:
```java
specBuilder.registerSimpleField("name", "fullName");
// Maps: ?name=john -> WHERE full_name LIKE '%john%'
```

#### Nested Fields
Fields that require JOINs to related entities:
```java
specBuilder.registerNestedField("genderid", "gender.id");
// Maps: ?genderid=1 -> LEFT JOIN gender g ON ... WHERE g.id = '1'
```

#### Date Fields
Fields that support range filtering:
```java
specBuilder.registerDateField("dobirth", "dateOfBirth");
// Maps: ?dobirth-from=2020-01-01&dobirth-to=2020-12-31
```

#### Numeric Fields
Fields that use exact matching instead of LIKE:
```java
specBuilder.registerNumericField(Arrays.asList("id", "count"));
// Maps: ?id=123 -> WHERE id = '123' (not LIKE '%123%')
```

## 🔍 Supported Data Types

- **String Fields**: Uses case-insensitive LIKE matching
- **Numeric Fields**: Uses exact equality matching
- **Date Fields**: Supports LocalDate, LocalDateTime, Timestamp
- **Nested Relationships**: Supports multiple levels of JOINs

## 🛡️ Error Handling

The library includes comprehensive error handling:

```java
try {
    Specification<User> spec = specBuilder.createFilterSpecifications(filters);
    return userRepository.findAll(spec, pageable);
} catch (PathElementException e) {
    // Handle field path resolution errors
    log.error("Filter error: {}", e.getMessage());
    return Page.empty();
}
```

## 🎛️ Configuration Options

### Thread Safety
All field mappings are stored in `ConcurrentHashMap` for thread-safe operations in multi-threaded environments.

### Date Suffix Configuration
```java
Map<String, Boolean> dateSuffixes = new HashMap<>();
dateSuffixes.put("-from", true);    // true = start date (>=)
dateSuffixes.put("-to", false);     // false = end date (<=)
```

### Debugging Support
```java
// Get current mappings for debugging
Map<String, String> simpleFields = specBuilder.getSimpleFieldMappings();
Map<String, String> nestedFields = specBuilder.getNestedFieldMappings();
Map<String, String> dateFields = specBuilder.getDateFieldMappings();

log.info("Simple fields: {}", simpleFields);
log.info("Nested fields: {}", nestedFields);
log.info("Date fields: {}", dateFields);
```

## 📝 Examples

### Complete Example with Entity

```java
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", length = 45)
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new LinkedHashSet<>();

}

@Getter
@Setter
@Entity
@Table(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
```

### Service Layer Integration

```java
@Service
@RequiredArgsConstructor
public class EmployeeServiceIMPL implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DTOMapper dtoMapper;
    private final SpecificationBuilder specificationBuilder; // With @Bean Configuration

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeResponseDTO> getAllEmployees(HashMap<String, String> filters) {
        // SpecificationBuilder specificationBuilder = new SpecificationBuilder(); // Alternative
        List<EmployeeResponseDTO> employeeResponseDTOS = dtoMapper.toEmployeeResponseDTOs(employeeRepository.findAll());

        if (filters == null || filters.isEmpty()) {
            return employeeResponseDTOS;
        }

        Specification<Employee> specification = specificationBuilder.createFilterSpecifications(filters);
        return dtoMapper.toEmployeeResponseDTOs(employeeRepository.findAll(specification));
    }
}
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Dileesha Ekanayake**
- Email: dileesha.r.ekanayake@gmail.com
- GitHub: (https://github.com/dileesha-ekanayake)

## 🙏 Acknowledgments

- Spring Data JPA team for the excellent Specification API
- Hibernate team for the robust Criteria API
- The Spring Boot community for inspiration

## 📊 Version History

- **1.0.0** - Initial release with core filtering functionality
  - Dynamic JPA Specification building
  - Field mapping support
  - Nested field filtering
  - Date range filtering
  - Numeric field handling
  - Thread-safe operations
