# Sử dụng một base image chứa Java 17 (OpenJDK) để build và chạy ứng dụng
FROM openjdk:17-jdk-alpine

# Thiết lập người viết ra Dockerfile
LABEL mentainer = LeCongThanh

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép file .jar đã build từ máy local vào thư mục /app trong container
COPY ./target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose cổng mà ứng dụng Spring Boot(container) sẽ chạy
EXPOSE 9004

ENTRYPOINT ["java", "-jar", "app.jar"]