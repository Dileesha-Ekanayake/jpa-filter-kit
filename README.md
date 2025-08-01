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
    <groupId>lk.dileesha</groupId>
    <artifactId>jpa-filter-kit</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle
```gradle
implementation 'lk.dileesha:jpa-filter-kit:1.0.0'
```

## 🛠️ Quick Start

### 1. Configuration

Create a configuration class to set up your filter mappings:

```java
@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    @PostConstruct
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
@RequiredArgsConstructor
public class UserController {
    
    private final UserRepository userRepository;
    private final SpecificationBuilder specBuilder;
    
    @GetMapping("/users")
    public Page<User> getUsers(@RequestParam Map<String, String> filters,
                              Pageable pageable) {
        
        Specification<User> spec = specBuilder.createFilterSpecifications(filters);
        return userRepository.findAll(spec, pageable);
    }
}
```

### 3. Repository Interface

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long>, 
                                       JpaSpecificationExecutor<User> {
}
```

## 🎯 Usage Examples

### Simple Field Filtering

**Request:**
```
GET /users?name=john&email=gmail
```

**Generated Query:**
```sql
SELECT * FROM users 
WHERE LOWER(full_name) LIKE '%john%' 
  AND LOWER(email) LIKE '%gmail%'
```

### Nested Field Filtering

**Request:**
```
GET /users?genderid=1&roleid=2
```

**Generated Query:**
```sql
SELECT * FROM users u
LEFT JOIN gender g ON u.gender_id = g.id
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN role r ON ur.role_id = r.id
WHERE g.id = '1' AND r.id = '2'
```

### Date Range Filtering

**Request:**
```
GET /users?dobirth-from=1990-01-01&dobirth-to=2000-12-31
```

**Generated Query:**
```sql
SELECT * FROM users 
WHERE date_of_birth >= '1990-01-01' 
  AND date_of_birth <= '2000-12-31'
```

### Numeric Field Filtering

**Request:**
```
GET /users?id=123&count=5
```

**Generated Query:**
```sql
SELECT * FROM users 
WHERE id = '123' AND count = '5'
```

## 🔧 Advanced Configuration

### Using Builder Pattern

```java
SpecificationBuilder specBuilder = SpecificationBuilder.builder()
    .withSimpleField("name", "fullName")
    .withSimpleField("email", "emailAddress")
    .withNestedField("genderid", "gender.id")
    .withNestedField("departmentid", "department.id")
    .withDateField("dobirth", "dateOfBirth")
    .withDateField("joindate", "joinDate")
    .withNumericFields(Arrays.asList("id", "age", "salary"))
    .withDateSuffixes(Map.of(
        "-from", true,
        "-to", false,
        "-start", true,
        "-end", false
    ))
    .build();
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
@Entity
@Table(name = "users")
public class User {
    @Id
    private Long id;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;
    
    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoles;
    
    // getters and setters...
}

@Entity
public class Gender {
    @Id
    private Long id;
    private String name;
    // getters and setters...
}

@Entity
public class UserRole {
    @Id
    private Long id;
    
    @ManyToOne
    private User user;
    
    @ManyToOne
    private Role role;
    // getters and setters...
}
```

### Service Layer Integration

```java
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final SpecificationBuilder specBuilder;
    
    public Page<UserDTO> searchUsers(Map<String, String> filters, Pageable pageable) {
        Specification<User> spec = specBuilder.createFilterSpecifications(filters);
        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(this::convertToDTO);
    }
    
    public List<User> findUsersByComplexCriteria(String name, Long genderId, 
                                                LocalDate birthFrom, LocalDate birthTo) {
        Map<String, String> filters = new HashMap<>();
        if (name != null) filters.put("name", name);
        if (genderId != null) filters.put("genderid", genderId.toString());
        if (birthFrom != null) filters.put("dobirth-from", birthFrom.toString());
        if (birthTo != null) filters.put("dobirth-to", birthTo.toString());
        
        Specification<User> spec = specBuilder.createFilterSpecifications(filters);
        return userRepository.findAll(spec);
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
