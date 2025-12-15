# Swagger/OpenAPI Documentation Guide

## 📚 Tổng quan

Project đã được tích hợp **SpringDoc OpenAPI 3.0** (Swagger) để tạo tài liệu API tự động và giao diện thử nghiệm API tương tác.

## 🚀 Cách sử dụng

### 1. Khởi động ứng dụng

```bash
mvn spring-boot:run
```

### 2. Truy cập Swagger UI

Sau khi ứng dụng chạy, bạn có thể truy cập Swagger UI tại:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

### 3. Khám phá API

Trên Swagger UI, bạn có thể:
- ✅ Xem tất cả các endpoints có sẵn
- ✅ Xem chi tiết về request/response của từng API
- ✅ Thử nghiệm API trực tiếp từ trình duyệt
- ✅ Xem schema của các model/DTO

## 📋 Các API endpoints hiện có

### Officer Management

1. **GET** `/api/officers/test` - Test API connection
2. **GET** `/api/officers` - Get all officers
3. **GET** `/api/officers/{id}` - Get officer by ID
4. **GET** `/api/officers/customer/{custId}` - Get officers by customer ID
5. **GET** `/api/officers/search?keyword={keyword}` - Search officers

## ⚙️ Cấu hình

### Dependencies đã thêm vào `pom.xml`

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

### Cấu hình trong `application.properties`

```properties
# Swagger/OpenAPI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.tags-sorter=alpha
springdoc.swagger-ui.operations-sorter=alpha
```

## 📝 Annotations được sử dụng

### Controller Level
- `@Tag` - Mô tả nhóm API
- `@Operation` - Mô tả chi tiết cho từng endpoint
- `@ApiResponse` / `@ApiResponses` - Mô tả các response codes
- `@Parameter` - Mô tả parameters

### Model/DTO Level
- `@Schema` - Mô tả model và các fields

## 🎨 Tính năng

- ✨ Tự động sinh tài liệu API
- ✨ Giao diện đẹp, dễ sử dụng
- ✨ Thử nghiệm API trực tiếp
- ✨ Hỗ trợ OpenAPI 3.0 standard
- ✨ Tương thích với Spring Boot 4.x

## 🔧 Tùy chỉnh thêm

Để tùy chỉnh thêm, bạn có thể chỉnh sửa file:
- `src/main/java/com/example/demospring/config/SwaggerConfig.java`

Các thông tin có thể tùy chỉnh:
- Tiêu đề API
- Phiên bản
- Mô tả
- Thông tin liên hệ
- License
- Server URLs

## 📚 Tài liệu tham khảo

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)

