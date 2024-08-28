# Story Corner

Story Corner is a full-stack web application designed to provide a seamless experience for browsing and purchasing books. The frontend is built using Angular with Tailwind CSS for styling, while the backend is powered by Spring Boot with PostgreSQL as the database.

## Frontend

### Technologies Used
- **Angular 18**: Utilizing the latest features such as standalone components.
- **Tailwind CSS**: For utility-first CSS styling.
- **NGRX/Signals**: Managing global state, specifically for notifications and cart functionality.

### Key Features
- **Standalone Components**: Modular and reusable components designed to enhance maintainability and scalability.
- **Lazy Loading Routes**: Efficient loading of modules to improve application performance.
- **Route Guards**: 
  - Prevent unauthorized users from submitting orders.
  - Prevent authenticated users from accessing login or signup pages.
- **Reactive Forms**: Used for handling login, signup, and checkout processes, providing a robust and reactive way to handle form data.
- **Global State Management**: 
  - **Notifications**: Managed globally to notify the user about on going processes.
  - **Cart Management**: Users can add books to their cart before authentication. Cart state is preserved across different parts of the application.
- **JWT Refresh Token Mechanism**: 
  - Reading expiration dates from JWT access tokens and setting a timeout for refreshing the access token.
  - Sending refresh token requests using HTTP-only cookies.

## Backend

### Technologies Used
- **Spring Boot**: The core backend framework, handling API development and business logic.
- **PostgreSQL**: The relational database used to store user data, orders, books, and JWT tokens.

### Key Features
- **Flyway Database Migrations**: 
  - Ensures that database schema is versioned and consistent across different environments.
- **Spring Security with JWT Authentication**: 
  - **AccessTokens and RefreshTokens**: Implemented to ensure secure and stateless authentication.
  - **Token Storage in Database**: 
    - JWT tokens are stored in a dedicated database table.
    - Tokens are revoked upon logout.
    - Refresh tokens have a usage limit (3 times) to prevent abuse.
  
### Security
- **JWT Token Revocation**: Tokens are revoked when a user logs out, or when a refresh token is used more than three times.
- **Database Security**: Sensitive information, including JWT tokens, is securely stored in PostgreSQL.

## CI/CD Workflow
### Containerization
- **Backendr**: The Spring Boot application is containerized using the `dashaun/builder` image, which simplifies the build and deployment process.
- **Frontend**: Containerizing an Angular application with Docker witch involved creating a Dockerfile that builds the Angular app, packages it, and serves Nginx.
  
### GitHub Actions
- **Build and Publish Docker Images**: A GitHub Actions workflow is configured to automate the build process for both the frontend and backend.
  - **Frontend**: The workflow uses the Dockerfile to build the Angular application into a Docker image.
  - **Backend**: The Spring Boot application is built and containerized using the `dashaun/builder` image.
  - **Docker Hub Integration**: The workflow automatically publishes the built images to Docker Hub.
