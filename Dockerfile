# Sử dụng một base image chứa Java 17 (OpenJDK) để build và chạy ứng dụng
FROM openjdk:17-jdk-alpine

# Thiết lập người viết ra Dockerfile
LABEL mentainer = LeCongThanh

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép file .jar đã build từ máy local vào thư mục /app trong container
COPY ./target/backend-0.0.1-SNAPSHOT.jar app.jar

# Thiết lập biến môi trường để kết nối đến MySQL
ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/webtoeic
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=k65hust2020

# Expose cổng mà ứng dụng Spring Boot(container) sẽ chạy trên
EXPOSE 9004

ENTRYPOINT ["java", "-jar", "/app.jar"]