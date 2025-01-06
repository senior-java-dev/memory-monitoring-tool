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

## FAQ: Understanding JVM Memory Monitoring

### 1. What are the key JVM memory areas?

The JVM memory is divided into several key areas:

- **Heap Memory**
  - Where all objects are allocated
  - Divided into Young Generation (Eden + Survivor spaces) and Old Generation
  - Size is controlled by `-Xmx` and `-Xms` flags

- **Non-Heap Memory**
  - **Metaspace**: Stores class metadata (replaced PermGen in Java 8+)
  - **Code Cache**: Stores JIT-compiled code
  - **Thread Stacks**: Each thread has its own stack for local variables and method calls

### 2. How does garbage collection work?

Garbage collection in the JVM follows a generational approach:

1. **Young Generation Collection (Minor GC)**
   - Most objects die young
   - Eden space fills up first
   - Surviving objects move to Survivor spaces
   - Very fast and efficient

2. **Old Generation Collection (Major GC)**
   - Long-lived objects promoted from Young Generation
   - Less frequent but more time-consuming
   - Different algorithms available (G1, Parallel, ZGC, etc.)

3. **Collection Process**
   - Mark: Identify live objects
   - Sweep: Remove dead objects
   - Compact: Defragment memory (optional)

### 3. What triggers an OutOfMemoryError?

OutOfMemoryError occurs in several scenarios:

- **Java Heap Space**
  - Heap is full and no objects can be garbage collected
  - Large object allocations with insufficient heap space
  - Memory leaks preventing garbage collection

- **Metaspace**
  - Too many class definitions
  - Dynamic class generation without cleanup

- **Direct Memory**
  - Native memory allocations exceed limits
  - Common with NIO operations

### 4. How do virtual threads differ from platform threads?

Key differences between virtual and platform threads:

- **Resource Usage**
  - Platform threads: One-to-one mapping with OS threads
  - Virtual threads: Many-to-few mapping (thousands of virtual threads on few platform threads)

- **Memory Overhead**
  - Platform threads: ~2MB stack size each
  - Virtual threads: Very small stack (~1KB), dynamically sized

- **Blocking Behavior**
  - Platform threads: Blocking operations block OS threads
  - Virtual threads: Blocking operations only block the virtual thread

### 5. What metrics are most important for memory monitoring?

Essential memory metrics to monitor:

1. **Heap Usage**
   - Current usage vs. maximum capacity
   - Usage trends over time
   - Percentage of heap used after full GC

2. **Garbage Collection**
   - Frequency of collections
   - Duration of collections
   - Types of collections (minor vs. major)

3. **Memory Allocation Rate**
   - How fast memory is being allocated
   - Sudden spikes in allocation

4. **Thread Memory**
   - Number of active threads
   - Thread stack usage
   - Thread creation/termination rates

5. **Non-Heap Memory**
   - Metaspace usage
   - Code cache size
   - Direct buffer pool usage

This tool helps monitor these metrics and provides alerts when potential issues are detected.
