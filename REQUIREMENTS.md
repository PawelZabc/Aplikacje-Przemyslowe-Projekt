# Project Requirements

This document details all functional and non-functional requirements for the Java Spring Boot kiosk/order system project. Requirements are grouped by scope and business area, and reflect both common and project-specific needs.

---

## 1. Data Model, Repository, and JdbcTemplate

- **Entities:**
  - Use JPA annotations: `@Entity`, `@Id`, `@GeneratedValue`, `@Column`.
  - Model relationships:
    - `@OneToMany`/`@ManyToOne` with `@JoinColumn`
    - `@ManyToMany` with `@JoinTable`
- **Repositories:**
  - Extend `JpaRepository<T, ID>`
  - Implement custom query methods (`findBy...`, `@Query`)
  - Support pagination with `Page<T>`
  - Configure datasource, JPA, and Hibernate in `application.properties` (or `application.yml`)
  - Use `schema.sql` and `data.sql` for DB initialization
- **JdbcTemplate:**
  - Use as a dependency for custom SQL
  - SELECT queries: use `query()` with `RowMapper`
  - INSERT/UPDATE/DELETE: use `update()`
  - Use in service or dedicated DAO classes

## 2. REST API

- **Controllers:**
  - Use `@RestController` with `@RequestMapping("/api/v1/...)
  - Implement endpoints:
    - GET (list with pagination, single resource)
    - POST (create)
    - PUT (update)
    - DELETE (remove)
  - Use `@PathVariable`, `@RequestBody`, `@RequestParam` as needed
  - Return `ResponseEntity` with correct HTTP status codes (200, 201, 204, 400, 404)
- **Documentation:**
  - Integrate Springdoc OpenAPI
  - Swagger UI available at `/swagger-ui.html`
  - All endpoints must be documented

## 3. Application Layer & Business Logic

- **Services:**
  - Annotate with `@Service`, use `@Transactional(readOnly = true/false)`
  - Use constructor-based dependency injection
  - Map Entity ↔ DTO (use mappers)
  - Implement custom exceptions (e.g., `ResourceNotFoundException`)
  - Global error handling: `@RestControllerAdvice` + `@ExceptionHandler`
- **Validation:**
  - Use Bean Validation in DTOs: `@NotNull`, `@NotBlank`, `@Size`, `@Email`, `@Valid`
  - Validate at controller and service layer
  - Provide consistent error messages
- **Web Views:**
  - Use `@Controller` with `Model` and `@ModelAttribute`
  - Render views with Thymeleaf:
    - `th:each` for lists
    - `th:object`/`th:field` for forms
    - `th:errors` for error display
    - Layouts with `th:fragment`, `th:replace`
    - Use Bootstrap 5 for styling
- **File Upload/Download:**
  - Upload: `MultipartFile`, `enctype="multipart/form-data"`
  - Save files: `Files.copy`
  - Download: `Resource`, `ResponseEntity<byte[]>`
  - Support export to CSV/PDF

## 4. Security

- **Spring Security:**
  - Configure `SecurityFilterChain` as a `@Bean`
  - Use `authorizeHttpRequests` with `requestMatchers` for access control
  - Configure `formLogin`
  - Use `BCryptPasswordEncoder` for password hashing
  - Implement `UserDetailsService` with `loadUserByUsername`

## 5. Testing

- **Repository Tests:**
  - Use `@DataJpaTest`
  - Minimum 10 CRUD tests
  - Test custom queries and `RowMapper`
- **Service Tests:**
  - Use Mockito: `@Mock`, `@InjectMocks`, `when().thenReturn()`, `verify()`
  - Unit tests for business logic
- **Controller Tests:**
  - Use `@WebMvcTest` or `@SpringBootTest`
  - Use `MockMvc`: `perform()`, `andExpect()`
  - Use `@WithMockUser` for security tests
  - Minimum 5 business scenarios
- **Coverage:**
  - Use JaCoCo
  - Minimum 70% code coverage
  - Cover both happy path and error cases
- **Architecture Tests:**
  - Use JUnit + ArchUnit
  - Enforce rules (e.g., controllers must not depend on entities, services in `.service` package)
- **E2E Tests:**
  - Automated browser test to verify app loads (e.g., login or Swagger page, check title/element)
- **Test Utilities:**
  - Use `TestDataUtil.java` for test data setup

## 6. Logging

- Use logging at INFO, ERROR, and DEBUG levels
- Log at key business process points (method entry, error occurrence)

## 7. Docker & Deployment

- Provide `Dockerfile` for the app
- Provide `docker-compose.yml` to run app and database
- One-command startup: `docker-compose up`

## 8. Business Logic & Project-Specific Requirements

- **Order Flow:**
  - Welcome screen (advertisement) → Order type selection (dine-in/takeaway, stored in session) → Menu
  - Order type must affect final order (e.g., packaging fee)
- **Product Configuration:**
  - Display products by category
  - After selecting a product, user can edit its ingredients (Product-Ingredients relation)
  - Adding ingredient (e.g., "Extra Bacon") increases price; removing may be free
  - Price must update dynamically
- **Cart:**
  - Works without user account
  - Full order preview, edit quantity, remove items, re-edit product (e.g., change extras)
- **Order Numbering:**
  - Simulate payment
  - After order confirmation, generate unique, human-readable order number (e.g., #055)
  - Number resets daily or cycles 0-999
  - Show order summary to customer
- **Admin & Stats:**
  - Password-protected admin area
  - CRUD for products and ingredients (set extra prices)
  - Sales statistics module:
    - Aggregate data (SQL GROUP BY)
    - Show daily/monthly sales (order count, total revenue)

---

If any requirement is unclear or you need more detail, please ask for clarification.
