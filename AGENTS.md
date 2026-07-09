# Repository Guidelines

## Project Structure & Module Organization
This workspace has two sibling projects:

- `user-center-backend/`: Spring Boot Java 21 service. Main code is in `src/main/java/com/fzq`, config in `src/main/resources`, MyBatis XML in `src/main/resources/mapper`, SQL in `sql/`, and tests in `src/test/java`.
- `user-center-frontend-vue/`: Vue 3 + Vite + TypeScript app. Source files are in `src/`: API clients in `src/api`, routes in `src/router`, Pinia state in `src/store`, pages in `src/pages`, components in `src/components`, and assets in `src/assets` or `public/`.

Avoid generated or dependency folders: `target/`, `dist/`, and `node_modules/`.

## Build, Test, and Development Commands
Run backend commands from `user-center-backend/`:

- `mvn spring-boot:run`: start the API service locally.
- `mvn test`: run JUnit/Spring Boot tests.
- `mvn package`: validate, test, and build the jar.

Run frontend commands from `user-center-frontend-vue/`:

- `npm install`: install dependencies from `package-lock.json`.
- `npm run dev`: start the Vite development server.
- `npm run build`: run type checking and produce a production build.
- `npm run lint`: run Oxlint and ESLint with auto-fix.
- `npm run preview`: serve the built frontend locally.

## Coding Style & Naming Conventions
Backend Java uses UTF-8, Java 21, Spring Boot conventions, and Spring Java Format. Keep packages under `com.fzq`; use `PascalCase` for classes, `camelCase` for methods and fields, and layer suffixes such as `UserController`, `UserService`, `UserServiceImpl`, and `UserMapper`.

Frontend formatting follows `.editorconfig`: 2-space indentation, LF line endings, final newline, trimmed trailing whitespace, and 100-character max line length. Use `PascalCase.vue` for components/pages and `camelCase.ts` for stores, utilities, and API modules.

## Testing Guidelines
Backend tests use JUnit 5 via `spring-boot-starter-test`. Place tests under `user-center-backend/src/test/java` and name them `*Test.java`, such as `UserServiceTest.java`. Cover service, controller, and mapper changes.

No frontend test runner is currently configured. For frontend changes, run `npm run lint` and `npm run build`; add a test framework before introducing component or end-to-end tests.

## Commit & Pull Request Guidelines
The backend Git repository has no commits yet, and the frontend has no local Git history here. Use concise, imperative commit messages with an optional scope, for example `backend: validate user registration` or `frontend: add login error handling`.

Pull requests should include a short summary, affected area, verification commands run, linked issue if available, and screenshots or screen recordings for UI changes.

## Security & Configuration Tips
Do not commit secrets. Keep environment-specific values in frontend `.env*` files and backend `application-*.yml`; document any required local database setup using `user-center-backend/sql/create_table.sql`.
