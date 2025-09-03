# JMeter Test Plan for Real Estate Investment Platform

## Overview
This document describes the JMeter test plan for performance testing the Real Estate Investment Platform.

## Test Scenarios

### 1. User Authentication Test
**Objective:** Test login/logout functionality under load
- **Thread Groups:** 100 concurrent users
- **Ramp-up Period:** 10 seconds
- **Loop Count:** 5
- **Endpoints:**
  - POST `/api/auth/login`
  - GET `/api/auth/current`
  - POST `/api/auth/logout`

### 2. Property Search Load Test
**Objective:** Test property search performance
- **Thread Groups:** 200 concurrent users
- **Ramp-up Period:** 20 seconds
- **Loop Count:** 10
- **Endpoints:**
  - GET `/api/properties`
  - POST `/api/properties/search`
  - GET `/api/properties/location/{location}`

### 3. Investment Calculator Stress Test
**Objective:** Test calculation services under heavy load
- **Thread Groups:** 150 concurrent users
- **Ramp-up Period:** 15 seconds
- **Loop Count:** 20
- **Endpoints:**
  - GET `/api/investments/roi`
  - GET `/api/investments/mortgage`
  - POST `/api/investments/calculate`

### 4. Chatbot Performance Test
**Objective:** Test AI chatbot response times
- **Thread Groups:** 50 concurrent users
- **Ramp-up Period:** 5 seconds
- **Loop Count:** 10
- **Endpoints:**
  - POST `/api/chatbot/message`

## JMeter Configuration

### HTTP Request Defaults
```xml
<ConfigTestElement guiclass="HttpDefaultsGui" testclass="ConfigTestElement">
  <stringProp name="HTTPSampler.domain">localhost</stringProp>
  <stringProp name="HTTPSampler.port">8080</stringProp>
  <stringProp name="HTTPSampler.protocol">http</stringProp>
</ConfigTestElement>
```

### User Defined Variables
```xml
<Arguments guiclass="ArgumentsPanel" testclass="Arguments">
  <collectionProp name="Arguments.arguments">
    <elementProp name="BASE_URL" elementType="Argument">
      <stringProp name="Argument.name">BASE_URL</stringProp>
      <stringProp name="Argument.value">http://localhost:8080</stringProp>
    </elementProp>
    <elementProp name="USERNAME" elementType="Argument">
      <stringProp name="Argument.name">USERNAME</stringProp>
      <stringProp name="Argument.value">john_investor</stringProp>
    </elementProp>
  </collectionProp>
</Arguments>
```

### Sample Test Plan Structure
```
Test Plan
├── Thread Group (User Authentication)
│   ├── HTTP Header Manager
│   ├── HTTP Request (Login)
│   ├── JSON Extractor (Extract Token)
│   ├── HTTP Request (Get Current User)
│   └── Response Assertion
├── Thread Group (Property Search)
│   ├── HTTP Request (Get All Properties)
│   ├── HTTP Request (Search Properties)
│   ├── Response Time Graph
│   └── Summary Report
├── Thread Group (Investment Calculator)
│   ├── HTTP Request (Calculate ROI)
│   ├── HTTP Request (Calculate Mortgage)
│   └── Aggregate Report
└── Thread Group (Chatbot)
    ├── HTTP Request (Send Message)
    └── View Results Tree
```

## Assertions

### Response Assertion
- Response Code: 200
- Response Message: OK
- Response contains: "success":true

### Duration Assertion
- Main sample: 3000ms

### JSON Assertion
- Assert JSON path exists: $.data
- Assert JSON path exists: $.success

## Listeners

1. **View Results Tree** - For debugging
2. **Summary Report** - Overall statistics
3. **Aggregate Report** - Detailed metrics
4. **Response Time Graph** - Visual performance
5. **Backend Listener** - For InfluxDB/Grafana integration

## Performance Metrics to Monitor

### Response Times
- Average Response Time < 500ms
- 90th Percentile < 1000ms
- 95th Percentile < 2000ms

### Throughput
- Minimum: 100 requests/second
- Target: 500 requests/second

### Error Rate
- Acceptable: < 1%
- Critical: > 5%

### Resource Utilization
- CPU Usage < 80%
- Memory Usage < 85%
- Database Connection Pool < 90%

## Load Testing Scenarios

### Scenario 1: Normal Load
- 100 concurrent users
- 5-minute duration
- Expected: All responses < 500ms

### Scenario 2: Peak Load
- 500 concurrent users
- 10-minute duration
- Expected: 95% responses < 2000ms

### Scenario 3: Stress Test
- 1000 concurrent users
- 15-minute duration
- Find breaking point

### Scenario 4: Endurance Test
- 200 concurrent users
- 2-hour duration
- Check for memory leaks

## Running the Tests

### Command Line Execution
```bash
# Run test plan
jmeter -n -t real_estate_test_plan.jmx -l results.jtl

# Generate HTML report
jmeter -g results.jtl -o report_folder

# Run with properties
jmeter -n -t test_plan.jmx -Jusers=100 -Jduration=300
```

### GUI Mode (for development only)
```bash
jmeter
# Open test_plan.jmx
# Configure and run
```

## Sample JMeter Script
Save this as `real_estate_test.jmx`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2" properties="5.0">
  <hashTree>
    <TestPlan guiclass="TestPlanGui" testclass="TestPlan" testname="Real Estate Platform Test">
      <boolProp name="TestPlan.functional_mode">false</boolProp>
      <stringProp name="TestPlan.user_define_classpath"></stringProp>
    </TestPlan>
    <hashTree>
      <ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup" testname="Property Search Users">
        <intProp name="ThreadGroup.num_threads">100</intProp>
        <intProp name="ThreadGroup.ramp_time">10</intProp>
        <longProp name="ThreadGroup.duration">300</longProp>
      </ThreadGroup>
      <hashTree>
        <HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy" testname="Get Properties">
          <stringProp name="HTTPSampler.path">/api/properties</stringProp>
          <stringProp name="HTTPSampler.method">GET</stringProp>
        </HTTPSamplerProxy>
      </hashTree>
    </hashTree>
  </hashTree>
</jmeterTestPlan>
```

## Results Analysis

### Key Metrics to Review
1. **Response Time Distribution**
   - Check for outliers
   - Identify bottlenecks

2. **Error Analysis**
   - Group errors by type
   - Identify patterns

3. **Throughput Analysis**
   - Requests per second
   - Bandwidth usage

4. **Resource Monitoring**
   - Server CPU/Memory
   - Database performance
   - Network latency

## Optimization Recommendations

Based on test results, consider:
1. Implementing caching strategies
2. Database query optimization
3. Connection pool tuning
4. Horizontal scaling
5. CDN for static resources
6. API rate limiting
7. Asynchronous processing

## Continuous Testing

Integrate JMeter tests into CI/CD pipeline:
```yaml
performance-test:
  stage: test
  script:
    - jmeter -n -t test_plan.jmx -l results.jtl
    - jmeter-analyzer --input-jtl results.jtl --threshold error:5
```