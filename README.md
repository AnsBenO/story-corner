# Story Corner

Story Corner is a full-stack web application for browsing and purchasing books. The frontend is built using Angular with Tailwind CSS for styling, while the backend is built Spring Boot with PostgreSQL as the database.

## Frontend

### Technologies Used
- **Angular 18**: Latest Angular version. 
- **Tailwind CSS**: For utility-first CSS styling.
- **NGRX/Signals**: Managing global state, specifically for notifications and cart functionality.

### Key Features
- **Lazy Loading Routes**: Efficient loading of different parts of the application to improve application performance.
- **Route Guards**: 
  - Prevent unauthorized users from submitting orders.
  - Prevent authenticated users from accessing login or signup pages. 
- **Interceptors**:
  - Handeling http errors
  - Adding Authorization header with access token to outgoing requests. 
- **Reactive Forms**: Used for handling login, signup, and checkout processes. 
- **Global State Management**: 
  - **Notifications**: Managed globally to notify the user about ongoing processes.
  - **Inbox**: Users are notified about the state of the orders they submitted.
  - **Cart Management**: Users can add books to their cart before authentication. Cart state is preserved across different parts of the application.
- **Order real time updates using websocket**:
  - **Inbox Notifications**: Using sockjs and stompjs libraries users are subscibed to a web socket that notifies the client to the order status after processing.  
- **JWT Refresh Token Mechanism**: 
  - Reading expiration dates from JWT access tokens and setting a timeout for refreshing the access token.
  - Sending refresh token requests using HTTP-only cookie. 

## Backend

### Technologies Used
- **Spring Boot**: The core backend framework, handling API development and business logic.
- **PostgreSQL**: The relational database used to store user data, orders, books, and JWT tokens.

### Key Features
- **Flyway Database Migrations**: 
  - Ensures that database schema is versioned and consistent across different environments.
- **Spring Security with JWT Authentication**: 
  - **AccessTokens and RefreshTokens**: Implemented to ensure secure and authentication.
  - **Token Storage in Database**: 
    - JWT tokens are stored in a dedicated database table.
    - Tokens are revoked upon logout.
    - Refresh tokens have a usage limit (3 times) to prevent abuse. 
- **Order status notification using websocket**:
    - Notifying the user about the status of thier orders after processing. 
- **Error Handeling with a Global Controller Adviser** 
    - Ensure that the error responses are consistent and well defined for easier integration with the frontend.

  
### Security
- **JWT Token Revocation**: Tokens are revoked when a user logs out, or when a refresh token is used more than three times.
- **Database Security**: Sensitive information, including JWT tokens, is securely stored in PostgreSQL.

## CI/CD Workflow
### Containerization
- **Backend**: The Spring Boot application is containerized using the `dashaun/builder` image, which simplifies the build and deployment process.
- **Frontend**: Containerizing an Angular application with Docker witch involved creating a Dockerfile that builds the Angular app, packages it, and serves it using Nginx web server.
  
### GitHub Actions
- **Build and Publish Docker Images**: A GitHub Actions workflow is configured to automate the build process for both the frontend and backend.
  - **Frontend**: The workflow uses the Dockerfile to build the Angular application into a Docker image.
  - **Backend**: The Spring Boot application is built and containerized using the `dashaun/builder` image.
  - **Docker Hub Integration**: The workflow automatically publishes the built images to Docker Hub.
