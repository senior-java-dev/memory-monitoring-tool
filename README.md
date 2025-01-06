# Memory Monitoring Tool

<div align="center">
  <img src="docs/images/java_mem_tool.jpg" alt="Memory Monitoring Tool Banner" width="100%">
</div>

<div align="center">
  
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/technologies/downloads/#java21)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Test Coverage](https://img.shields.io/badge/Coverage-95%25-success.svg)](https://github.com/yourusername/memory-monitoring-tool)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

</div>

A robust Spring Boot application designed to monitor JVM memory usage, detect memory leaks, and track garbage collection metrics in real-time.

## Features

- **Memory Usage Monitoring**
  - Heap memory usage tracking
  - Non-heap memory monitoring
  - Memory utilization percentage
  - Maximum and committed memory tracking

- **Memory Leak Detection**
  - Consistent memory growth detection
  - High GC frequency monitoring
  - Poor memory reclamation detection
  - Real-time memory leak alerts

- **Garbage Collection Metrics**
  - GC collection count tracking
  - GC collection time monitoring
  - Collection type identification
  - Per-collector metrics

## Technology Stack

- Java 21
- Spring Boot 3.4.1
- Spring Web
- Spring Boot Actuator
- Lombok
- JUnit 5
- Mockito
- Maven

## Prerequisites

- JDK 21 or higher
- Maven 3.6 or higher
- A Java IDE (recommended: IntelliJ IDEA, Eclipse, or VS Code)

## Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/memory-monitoring-tool.git
   cd memory-monitoring-tool
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Memory Monitoring

- `GET /memory-monitor/heap-memory` - Get current heap memory usage
- `GET /memory-monitor/non-heap-memory` - Get non-heap memory usage
- `GET /memory-monitor/heap-memory-max` - Get maximum heap memory
- `GET /memory-monitor/heap-memory-committed` - Get committed heap memory
- `GET /memory-monitor/memory-utilisation` - Get memory utilization percentage
- `GET /memory-monitor/all` - Get all memory metrics

### Memory Leak Detection

- `GET /memory-leak/status` - Get current memory leak detection status

### Garbage Collection

- `GET /gc/last-gc-info` - Get information about the last garbage collection
- `GET /gc/coll-count` - Get total garbage collection count
- `GET /gc/coll-time` - Get total garbage collection time
- `GET /gc/coll-type` - Get garbage collection type
- `GET /gc/metrics-by-coll-name` - Get metrics grouped by collector name

## Configuration

The application uses the following default configurations that can be customized in `application.properties`:

- Memory growth threshold: 85%
- GC frequency threshold: 10 seconds
- Memory snapshot sample size: 5

## Testing

The project includes comprehensive unit tests with over 95% code coverage. Run tests using:

```bash
mvn test
```

Key test areas include:
- Memory monitoring service tests
- Memory leak detection tests
- Garbage collection monitoring tests
- Controller endpoint tests

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Spring Boot team for the excellent framework
- JVM team for the comprehensive memory management APIs
- All contributors who help improve this tool

## Support

For support, please open an issue in the GitHub repository or contact the maintainers.
