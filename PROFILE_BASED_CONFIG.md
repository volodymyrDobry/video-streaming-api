# Profile-Based Configuration Structure

## Overview

Your Spring Cloud Config is now organized with **profile separation** for different environments:

- **Application-specific** (`{service}.yaml`) - Common settings for all environments
- **Environment-specific** (`{service}-{profile}.yaml`) - Environment overrides

## Directory Structure

```
/configuration/
├── Common (Application-specific)
│   ├── identity-access.yaml
│   ├── content-service.yaml
│   ├── streaming-and-video.yaml
│   ├── user-streaming-history.yaml
│   └── viora-streaming-gateway.yaml
│
└── Environment-specific
    ├── Development (dev)
    │   ├── identity-access-dev.yaml
    │   ├── content-service-dev.yaml
    │   ├── streaming-and-video-dev.yaml
    │   ├── user-streaming-history-dev.yaml
    │   └── viora-streaming-gateway-dev.yaml
    │
    ├── Staging (stage)
    │   ├── identity-access-stage.yaml
    │   ├── content-service-stage.yaml
    │   ├── streaming-and-video-stage.yaml
    │   ├── user-streaming-history-stage.yaml
    │   └── viora-streaming-gateway-stage.yaml
    │
    └── Production (prod)
        ├── identity-access-prod.yaml
        ├── content-service-prod.yaml
        ├── streaming-and-video-prod.yaml
        ├── user-streaming-history-prod.yaml
        └── viora-streaming-gateway-prod.yaml
```

## How It Works

### Configuration Loading Hierarchy

```
1. Load common config: {service}.yaml
   ↓
2. Load environment-specific: {service}-{profile}.yaml (overrides)
   ↓
3. Apply environment variables (override all)
   ↓
4. Service starts with merged configuration
```

### Example: identity-access

**Common** (`identity-access.yaml`):
```yaml
spring:
  datasource:
    url: ${POSTGRES_URL:jdbc:postgresql://localhost:5432/postgres}
    hikari:
      maximum-pool-size: ${HIKARI_MAX_POOL_SIZE:10}
  jpa:
    show-sql: ${JPA_SHOW_SQL:false}
```

**Development** (`identity-access-dev.yaml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/viora_identity_dev
  jpa:
    show-sql: true
logging:
  level:
    com.viora: DEBUG
```

**Staging** (`identity-access-stage.yaml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres-stage:5432/viora_identity_stage
    hikari:
      maximum-pool-size: 20
  jpa:
    show-sql: false
logging:
  level:
    com.viora: INFO
```

**Production** (`identity-access-prod.yaml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres-prod:5432/viora_identity_prod
    hikari:
      maximum-pool-size: 30
  jpa:
    show-sql: false
logging:
  level:
    com.viora: WARN
```

## Starting Services in Different Environments

### Development

```bash
export SPRING_PROFILES_ACTIVE=dev
export CONFIG_SERVER_URL=http://localhost:8888

./gradlew :identity-access:bootRun
./gradlew :content-service:bootRun
./gradlew :streaming-and-video:bootRun
./gradlew :user-streaming-history:bootRun
./gradlew :viora-streaming-gateway:bootRun
```

### Staging

```bash
export SPRING_PROFILES_ACTIVE=stage
export CONFIG_SERVER_URL=http://config-stage:8888

./gradlew :identity-access:bootRun
./gradlew :content-service:bootRun
# ... other services
```

### Production

```bash
export SPRING_PROFILES_ACTIVE=prod
export CONFIG_SERVER_URL=http://config-prod:8888

./gradlew :identity-access:bootRun
./gradlew :content-service:bootRun
# ... other services
```

## Verify Configuration Loading

```bash
# Check what config is loaded for dev
curl http://localhost:8888/identity-access/dev | jq '.propertySources[0].source."spring.datasource.url"'
# Returns: jdbc:postgresql://localhost:5432/viora_identity_dev

# Check for stage
curl http://localhost:8888/identity-access/stage | jq '.propertySources[0].source."spring.datasource.url"'
# Returns: jdbc:postgresql://postgres-stage:5432/viora_identity_stage

# Check for prod
curl http://localhost:8888/identity-access/prod | jq '.propertySources[0].source."spring.datasource.url"'
# Returns: jdbc:postgresql://postgres-prod:5432/viora_identity_prod
```

## Configuration Per Service

### identity-access
- **Common**: OAuth2, Keycloak, Liquibase, JPA, Hikari pools
- **Dev**: Full logging, SQL output, validate DDL
- **Stage**: Production-like setup, validate DDL, higher pool size
- **Prod**: Minimal logging, validate DDL, highest pool size, compression

### content-service
- **Common**: OAuth2, ActiveMQ, MongoDB
- **Dev**: Local MongoDB, full logging, all metrics exposed
- **Stage**: Stage MongoDB cluster, reduced logging, metrics
- **Prod**: Production MongoDB (with connection string), minimal logging, compression

### streaming-and-video
- **Common**: PostgreSQL, Resilience4j, OAuth2, tracing
- **Dev**: Create DDL, full debugging, all metrics
- **Stage**: Validate DDL, reduced tracing (50%), higher pools
- **Prod**: Validate DDL, minimal tracing (10%), highest pools, tuned resilience

### user-streaming-history
- **Common**: MongoDB, ActiveMQ
- **Dev**: Local MongoDB, full debugging
- **Stage**: Stage MongoDB, production-like
- **Prod**: Production MongoDB with connection string, compression

### viora-streaming-gateway
- **Common**: Cloud gateway routes, Redis, OAuth2
- **Dev**: Local Redis, high rate limits, all metrics
- **Stage**: Stage Redis, higher rate limits, metrics exposed
- **Prod**: Production Redis with SSL, highest rate limits, minimal metrics

## Key Differences by Environment

| Aspect | Dev | Stage | Prod |
|--------|-----|-------|------|
| **Database** | localhost | stage-server | prod-server |
| **Logging** | DEBUG | INFO | ERROR |
| **Pool Size** | 10 | 20 | 30 |
| **DDL** | create/update | validate | validate |
| **Tracing** | 100% | 50% | 10% |
| **Metrics** | All endpoints | Limited | Health only |
| **Compression** | No | No | Yes |
| **SSL** | No | No | Yes |

## Environment Variables

All placeholders support environment variable override:

```bash
# Database
export POSTGRES_USERNAME=postgres
export POSTGRES_PASSWORD=secure_password
export POSTGRES_URL=jdbc:postgresql://localhost:5432/postgres

# MongoDB
export MONGO_USERNAME=root
export MONGO_PASSWORD=secret
export MONGO_HOST=localhost
export MONGO_PORT=27017

# Redis
export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=password

# ActiveMQ
export ACTIVEMQ_BROKER_URL=tcp://localhost:61616
export ACTIVEMQ_USER=admin
export ACTIVEMQ_PASSWORD=admin

# Keycloak
export KEYCLOAK_URI=http://localhost:8081

# Config Server
export CONFIG_SERVER_URL=http://localhost:8888
export SPRING_PROFILES_ACTIVE=dev
```

## Example: Deploying to Stage

```bash
# 1. Set environment
export SPRING_PROFILES_ACTIVE=stage
export CONFIG_SERVER_URL=http://config-stage:8888
export POSTGRES_URL=jdbc:postgresql://postgres-stage:5432/...
export MONGO_HOST=mongo-stage
export REDIS_HOST=redis-stage
export KEYCLOAK_URI=http://keycloak-stage:8081

# 2. Start services
docker-compose up

# OR manually
./gradlew :cloud-config-service:bootRun
./gradlew :identity-access:bootRun
# ... other services

# 3. Verify
curl http://localhost:8082/actuator/env | jq '.propertySources[] | select(.source | has("spring.datasource.url"))'
```

## Adding New Profiles

To add a new environment (e.g., `testing`):

1. Create files in `/configuration`:
   ```
   identity-access-testing.yaml
   content-service-testing.yaml
   streaming-and-video-testing.yaml
   user-streaming-history-testing.yaml
   viora-streaming-gateway-testing.yaml
   ```

2. Start service with profile:
   ```bash
   export SPRING_PROFILES_ACTIVE=testing
   ./gradlew :identity-access:bootRun
   ```

## Troubleshooting

### Config not loading for profile
```bash
# Verify config exists
curl http://localhost:8888/identity-access/dev

# Check profiles in service
curl http://localhost:8082/actuator/env | jq '.propertySources[0] | {name, source}'
```

### Wrong config being applied
```bash
# Verify active profile
export SPRING_PROFILES_ACTIVE=dev
echo $SPRING_PROFILES_ACTIVE

# Check config server sees the profile
curl http://localhost:8888/identity-access/dev | jq '.profiles'
```

### Environment variables not overriding
```bash
# Ensure env var is exported
export POSTGRES_URL=new_value

# Restart service to pick up new env var
```

---

✅ **Profile-based configuration is now ready!**

Use `SPRING_PROFILES_ACTIVE=dev|stage|prod` to control which environment settings are applied. 🚀

