# Agent guidelines

> **Note:** `AGENTS.md` is the canonical agent guidelines file. The other agent files
> (`CLAUDE.md`, `GEMINI.md`) are symlinks pointing here. Tools may refuse to edit through
> a symlink — resolve to `AGENTS.md` and edit that directly.

## Testing

- Use **JUnit 5 (`org.junit.jupiter.api`)** assertions — `assertEquals`, `assertSame`,
  `assertNull`, `assertTrue`, `assertThrows`, etc. Do **not** use AssertJ (`assertThat`),
  even though it ships with `spring-boot-starter-test`.
- For plain in-memory components (e.g. `PeliculasRepositoryImpl`), write fast unit tests
  that construct the class directly (`new ...()`) instead of booting the Spring context
  with `@SpringBootTest`. Construct a fresh instance per test (`@BeforeEach`) for isolation.
- **Mockito is available and fine to use** (it ships with `spring-boot-starter-test`).
  For components that depend on a collaborator interface (e.g. a controller depending on
  `PeliculasService`), prefer mocking the collaborator with `@Mock` +
  `@ExtendWith(MockitoExtension.class)` and constructing the class under test directly,
  rather than hand-writing fakes or booting the Spring context. See
  `PeliculasServiceImplTest` and `PeliculasControllerTest` for the established pattern.
- **Do not run the test suite.** The user runs the tests themselves. Only run tests when
  the user explicitly requests it.
