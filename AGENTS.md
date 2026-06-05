# Agent guidelines

## Testing

- Use **JUnit 5 (`org.junit.jupiter.api`)** assertions — `assertEquals`, `assertSame`,
  `assertNull`, `assertTrue`, `assertThrows`, etc. Do **not** use AssertJ (`assertThat`),
  even though it ships with `spring-boot-starter-test`.
- For plain in-memory components (e.g. `PeliculasRepositoryImpl`), write fast unit tests
  that construct the class directly (`new ...()`) instead of booting the Spring context
  with `@SpringBootTest`. Construct a fresh instance per test (`@BeforeEach`) for isolation.
- **Do not run the test suite.** The user runs the tests themselves. Only run tests when
  the user explicitly requests it.
