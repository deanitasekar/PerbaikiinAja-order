# Build the application
FROM docker.io/library/eclipse-temurin:21-jdk-alpine AS builder

# Set working directory
WORKDIR /src/order

# Copy all project files
COPY . .

# Make gradlew executable and build
RUN chmod +x gradlew && \
    ./gradlew clean bootJar --no-daemon

# Stage 2: Runtime image  
FROM docker.io/library/eclipse-temurin:21-jre-alpine AS runner

# Create non-root user for security
ARG USER_NAME=orderapp
ARG USER_UID=1000
ARG USER_GID=${USER_UID}

RUN addgroup -g ${USER_GID} ${USER_NAME} && \
    adduser -h /opt/orderapp -D -u ${USER_UID} -G ${USER_NAME} ${USER_NAME}

# Switch to non-root user
USER ${USER_NAME}
WORKDIR /opt/orderapp

# Copy the built JAR from builder stage
COPY --from=builder --chown=${USER_UID}:${USER_GID} /src/order/build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]