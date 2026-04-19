# Complete Configuration File Inventory

## Configuration Files Organization

### Location: `/Users/vovchikPersonal/Projects/microservices-viora/Viora/configuration/`

## All 25 Configuration Files

```
configuration/
│
├── Common Configs (5 files)
│   ├── identity-access.yaml                    ✅
│   ├── content-service.yaml                    ✅
│   ├── streaming-and-video.yaml                ✅
│   ├── user-streaming-history.yaml             ✅
│   └── viora-streaming-gateway.yaml            ✅
│
├── Development Configs (5 files) - Profile: dev
│   ├── identity-access-dev.yaml                ✅
│   ├── content-service-dev.yaml                ✅
│   ├── streaming-and-video-dev.yaml            ✅
│   ├── user-streaming-history-dev.yaml         ✅
│   └── viora-streaming-gateway-dev.yaml        ✅
│
├── Staging Configs (5 files) - Profile: stage
│   ├── identity-access-stage.yaml              ✅
│   ├── content-service-stage.yaml              ✅
│   ├── streaming-and-video-stage.yaml          ✅
│   ├── user-streaming-history-stage.yaml       ✅
│   └── viora-streaming-gateway-stage.yaml      ✅
│
└── Production Configs (5 files) - Profile: prod
    ├── identity-access-prod.yaml               ✅
    ├── content-service-prod.yaml               ✅
    ├── streaming-and-video-prod.yaml           ✅
    ├── user-streaming-history-prod.yaml        ✅
    └── viora-streaming-gateway-prod.yaml       ✅
```

## File Details

### Common Configs

#### 1. identity-access.yaml
- **Size**: ~50 lines
- **Sections**: Keycloak, Datasource, JPA, Liquibase, OAuth2, Server
- **Purpose**: Base config for identity service

#### 2. content-service.yaml
- **Size**: ~30 lines
- **Sections**: OAuth2, ActiveMQ, MongoDB, Server
- **Purpose**: Base config for content service

#### 3. streaming-and-video.yaml
- **Size**: ~60 lines
- **Sections**: Datasource, JPA, OAuth2, Resilience4j, Tracing, Server
- **Purpose**: Base config for streaming service with circuit breakers

#### 4. user-streaming-history.yaml
- **Size**: ~25 lines
- **Sections**: MongoDB, ActiveMQ, Server
- **Purpose**: Base config for history service

#### 5. viora-streaming-gateway.yaml
- **Size**: ~50 lines
- **Sections**: Cloud Gateway routes, OAuth2, Redis, Services, Management
- **Purpose**: Base config for API gateway

---

### Development Profiles (-dev.yaml)

#### identity-access-dev.yaml
```yaml
✅ Local PostgreSQL on localhost:5432
✅ JPA show-sql: true
✅ Logging: DEBUG
✅ All actuator endpoints: health,info,metrics,env,configprops
✅ Health details: always
```

#### content-service-dev.yaml
```yaml
✅ Local MongoDB on localhost:27017
✅ Database: content_dev
✅ Logging: DEBUG
✅ All actuator endpoints exposed
```

#### streaming-and-video-dev.yaml
```yaml
✅ Local PostgreSQL on localhost:5431
✅ JPA show-sql: true, DDL: create
✅ Logging: DEBUG for resilience4j
✅ All actuator endpoints exposed
✅ Tracing sampling: 100%
```

#### user-streaming-history-dev.yaml
```yaml
✅ Local MongoDB on localhost:27018
✅ Database: streaming-history-dev
✅ Logging: DEBUG
✅ All actuator endpoints exposed
```

#### viora-streaming-gateway-dev.yaml
```yaml
✅ Local Redis on localhost:6379
✅ High rate limits: 500/1000 tokens
✅ Logging: DEBUG for gateway
✅ All actuator endpoints exposed
✅ Tracing sampling: 100%
```

---

### Staging Profiles (-stage.yaml)

#### identity-access-stage.yaml
```yaml
✅ Stage PostgreSQL: postgres-stage:5432
✅ JPA show-sql: false, DDL: validate
✅ Pool size: 20
✅ Logging: WARN
✅ Actuator: health,info,metrics
```

#### content-service-stage.yaml
```yaml
✅ Stage MongoDB: mongo-stage:27017
✅ Database: content_stage
✅ Logging: INFO
✅ Actuator: health,info,metrics
```

#### streaming-and-video-stage.yaml
```yaml
✅ Stage PostgreSQL: postgres-stage:5432
✅ JPA show-sql: false, DDL: validate
✅ Pool size: 20
✅ Logging: INFO
✅ Tracing sampling: 50%
```

#### user-streaming-history-stage.yaml
```yaml
✅ Stage MongoDB: mongo-stage:27018
✅ Database: streaming-history-stage
✅ Logging: INFO
✅ Actuator: health,info,metrics
```

#### viora-streaming-gateway-stage.yaml
```yaml
✅ Stage Redis: redis-stage:6379
✅ Service URLs point to service names
✅ Rate limits: 1000/2000 tokens
✅ Logging: WARN
✅ Tracing sampling: 50%
```

---

### Production Profiles (-prod.yaml)

#### identity-access-prod.yaml
```yaml
✅ Production PostgreSQL: postgres-prod:5432
✅ JPA show-sql: false, DDL: validate
✅ Pool size: 30, Max lifetime: 1200s
✅ Logging: ERROR
✅ Compression: enabled
✅ Actuator: health,info,metrics
```

#### content-service-prod.yaml
```yaml
✅ Production MongoDB with connection string
✅ Database: content_prod
✅ Logging: WARN
✅ Compression: enabled
✅ Actuator: health,info,metrics
```

#### streaming-and-video-prod.yaml
```yaml
✅ Production PostgreSQL: postgres-prod:5432
✅ Pool size: 30, Max lifetime: 1200s
✅ JPA show-sql: false, DDL: validate
✅ Logging: ERROR
✅ Tracing sampling: 10%
✅ Optimized Resilience4j: larger windows, higher thresholds
✅ Compression: enabled
```

#### user-streaming-history-prod.yaml
```yaml
✅ Production MongoDB with connection string
✅ Database: streaming-history-prod
✅ Logging: ERROR
✅ Compression: enabled
✅ Actuator: health,info,metrics
```

#### viora-streaming-gateway-prod.yaml
```yaml
✅ Production Redis with SSL: redis-prod:6379
✅ Service URLs point to service names
✅ Rate limits: 2000/5000 tokens
✅ Logging: ERROR
✅ Tracing sampling: 10%
✅ Compression: enabled
```

---

## Profile Selection

### How to Use

```bash
# Development (DEFAULT)
export SPRING_PROFILES_ACTIVE=dev
export CONFIG_SERVER_URL=http://localhost:8888

# Staging
export SPRING_PROFILES_ACTIVE=stage
export CONFIG_SERVER_URL=http://config-stage:8888

# Production
export SPRING_PROFILES_ACTIVE=prod
export CONFIG_SERVER_URL=http://config-prod:8888

# Start service
./gradlew :identity-access:bootRun
```

### Fallback Behavior

If `SPRING_PROFILES_ACTIVE` is not set, Spring Cloud Config will:
1. Try to load `{service}-dev.yaml` (if you set it as default)
2. Fall back to `{service}.yaml` (common config)

---

## Configuration Hierarchy

```
1. {service}.yaml (COMMON)
   ├─ Loaded first
   ├─ Common for all environments
   └─ Contains base/defaults
        ↓
2. {service}-{profile}.yaml (ENVIRONMENT-SPECIFIC)
   ├─ Loaded second (if profile matches)
   ├─ Overrides common settings
   └─ Environment-specific values
        ↓
3. Environment Variables
   ├─ Loaded last
   ├─ Override all YAML settings
   └─ Runtime overrides
        ↓
4. Application starts with merged configuration
```

---

## Key Settings by Profile

### Database Configuration

**Dev**
```
PostgreSQL: localhost:5432 (with default DB)
MongoDB: localhost:27017 (content_dev, streaming-history-dev)
```

**Stage**
```
PostgreSQL: postgres-stage:5432
MongoDB: mongo-stage:27017
```

**Prod**
```
PostgreSQL: postgres-prod:5432
MongoDB: Connection string with credentials
Redis: With SSL enabled
```

### Logging

**Dev**: DEBUG for application code
**Stage**: INFO for application code
**Prod**: ERROR for root, WARN for application

### Monitoring

**Dev**: All endpoints (health, info, metrics, env, configprops)
**Stage**: Limited (health, info, metrics)
**Prod**: Minimal (health, info, metrics)

### Performance

**Dev**: 10 pool size, 100% tracing
**Stage**: 20 pool size, 50% tracing
**Prod**: 30 pool size, 10% tracing, compression enabled

---

## Verification Checklist

✅ All 25 configuration files created
✅ Common configs (5 files)
✅ Dev profiles (5 files)
✅ Stage profiles (5 files)
✅ Prod profiles (5 files)
✅ Profile naming convention: `{service}-{profile}.yaml`
✅ Spring Cloud Config server will auto-discover and serve these

---

## Next Steps

1. **Start Cloud Config Server**
   ```bash
   ./gradlew :cloud-config-service:bootRun
   ```

2. **Verify Configuration Loading**
   ```bash
   curl http://localhost:8888/identity-access/dev | jq '.name'
   ```

3. **Start Services with Profile**
   ```bash
   export SPRING_PROFILES_ACTIVE=dev
   ./gradlew :identity-access:bootRun
   ```

4. **Check Loaded Configuration**
   ```bash
   curl http://localhost:8082/actuator/env
   ```

---

**✅ Profile-based configuration structure is complete and ready to use!**

