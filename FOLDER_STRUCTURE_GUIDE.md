# Organized Configuration Structure

## 📁 New Folder-Based Organization

Your configuration is now beautifully organized by service with subfolders:

```
/configuration/
│
├── identity-access/
│   ├── application.yaml           (Common config)
│   ├── application-dev.yaml       (Development overrides)
│   ├── application-stage.yaml     (Staging overrides)
│   └── application-prod.yaml      (Production overrides)
│
├── content-service/
│   ├── application.yaml
│   ├── application-dev.yaml
│   ├── application-stage.yaml
│   └── application-prod.yaml
│
├── streaming-and-video/
│   ├── application.yaml
│   ├── application-dev.yaml
│   ├── application-stage.yaml
│   └── application-prod.yaml
│
├── user-streaming-history/
│   ├── application.yaml
│   ├── application-dev.yaml
│   ├── application-stage.yaml
│   └── application-prod.yaml
│
└── viora-streaming-gateway/
    ├── application.yaml
    ├── application-dev.yaml
    ├── application-stage.yaml
    └── application-prod.yaml
```

## ✨ Benefits of This Structure

✅ **Better Organization** - Each service has its own folder
✅ **Easy Navigation** - Find configs faster with clear folder structure
✅ **Scalability** - Add new services easily
✅ **Maintenance** - Profile overrides grouped with common config
✅ **Clean** - Standard naming convention `application.yaml` and `application-{profile}.yaml`

## 🔧 Cloud Config Server Configuration

The cloud config server automatically discovers this structure via the updated `search-locations`:

```yaml
search-locations:
  - file:../${CONFIG_SEARCH_PATH:configuration}/{application}
  - file:../${CONFIG_SEARCH_PATH:configuration}
```

**How it works:**
- `{application}` = service name (e.g., `identity-access`)
- Config server looks in: `/configuration/identity-access/`
- Finds `application.yaml` + `application-{profile}.yaml`

## 🚀 How Services Load Configuration

### Step 1: Service Starts with Profile

```bash
export SPRING_PROFILES_ACTIVE=dev
export CONFIG_SERVER_URL=http://localhost:8888

./gradlew :identity-access:bootRun
```

### Step 2: Config Server Processes Request

```
Request: /identity-access/dev
       ↓
Search path 1: /configuration/identity-access/
       ↓
Load: /configuration/identity-access/application.yaml
       ↓
Load: /configuration/identity-access/application-dev.yaml (overrides)
       ↓
Return merged configuration
```

### Step 3: Service Starts with Merged Config

- Common settings from `application.yaml`
- Dev-specific overrides from `application-dev.yaml`
- Environment variables (if any)
- Service starts with final merged configuration

## 📋 Configuration Files

### identity-access/
- **application.yaml**: Keycloak, Datasource, JPA, Liquibase, OAuth2 (42 lines)
- **application-dev.yaml**: Local DB, DEBUG logging, all endpoints (20 lines)
- **application-stage.yaml**: Stage DB, WARN logging, limited endpoints (20 lines)
- **application-prod.yaml**: Prod DB, ERROR logging, compression (25 lines)

### content-service/
- **application.yaml**: MongoDB, OAuth2, ActiveMQ (24 lines)
- **application-dev.yaml**: Local MongoDB, DEBUG logging (18 lines)
- **application-stage.yaml**: Stage MongoDB, INFO logging (15 lines)
- **application-prod.yaml**: Production MongoDB, compression (20 lines)

### streaming-and-video/
- **application.yaml**: Datasource, Resilience4j, OAuth2, Tracing (58 lines)
- **application-dev.yaml**: Local DB, DEBUG, 100% tracing (24 lines)
- **application-stage.yaml**: Stage DB, INFO, 50% tracing (22 lines)
- **application-prod.yaml**: Prod DB, ERROR, 10% tracing, tuned resilience (38 lines)

### user-streaming-history/
- **application.yaml**: MongoDB, ActiveMQ (20 lines)
- **application-dev.yaml**: Local MongoDB, DEBUG logging (17 lines)
- **application-stage.yaml**: Stage MongoDB, INFO logging (14 lines)
- **application-prod.yaml**: Production MongoDB, compression (18 lines)

### viora-streaming-gateway/
- **application.yaml**: Gateway routes, Redis, OAuth2 (52 lines)
- **application-dev.yaml**: Local Redis, DEBUG, high rate limits (34 lines)
- **application-stage.yaml**: Stage Redis, INFO, moderate rate limits (26 lines)
- **application-prod.yaml**: Prod Redis with SSL, ERROR, compression (36 lines)

---

## 🎯 Quick Start

### 1. Start Config Server

```bash
cd /Users/vovchikPersonal/Projects/microservices-viora/Viora
./gradlew :cloud-config-service:bootRun
```

### 2. Verify Configuration Discovery

```bash
# Test dev profile
curl http://localhost:8888/identity-access/dev | jq '.name'
# Returns: "identity-access"

# Test stage profile
curl http://localhost:8888/identity-access/stage | jq '.name'
# Returns: "identity-access"

# Test prod profile
curl http://localhost:8888/identity-access/prod | jq '.name'
# Returns: "identity-access"
```

### 3. Start Services by Profile

```bash
# Development
export SPRING_PROFILES_ACTIVE=dev
export CONFIG_SERVER_URL=http://localhost:8888
./gradlew :identity-access:bootRun

# Staging
export SPRING_PROFILES_ACTIVE=stage
export CONFIG_SERVER_URL=http://config-stage:8888
./gradlew :identity-access:bootRun

# Production
export SPRING_PROFILES_ACTIVE=prod
export CONFIG_SERVER_URL=http://config-prod:8888
./gradlew :identity-access:bootRun
```

---

## 🔍 Verification Commands

```bash
# List all available configurations
curl http://localhost:8888/identity-access/dev | jq '.propertySources[0].source | keys'

# Check specific property
curl http://localhost:8888/identity-access/dev | jq '.propertySources[0].source."spring.datasource.url"'

# Compare profiles
echo "=== DEV ==="
curl -s http://localhost:8888/identity-access/dev | jq '.propertySources[0].source."logging.level.root"'
echo "=== STAGE ==="
curl -s http://localhost:8888/identity-access/stage | jq '.propertySources[0].source."logging.level.root"'
echo "=== PROD ==="
curl -s http://localhost:8888/identity-access/prod | jq '.propertySources[0].source."logging.level.root"'
```

---

## 📊 Statistics

- **Total Configuration Files**: 25
- **Service Folders**: 5
- **Profile Types**: 4 (common + dev + stage + prod)
- **Total Configuration Lines**: ~400+
- **Search Pattern**: `{application}/` for service-specific folders

---

## 🔄 File Naming Convention

All configuration files follow Spring Boot standards:

```
application.yaml           = Default/Common config
application-dev.yaml       = Development profile
application-stage.yaml     = Staging profile
application-prod.yaml      = Production profile
```

**Spring Cloud Config recognizes:**
- `application.yaml` as the base configuration
- `application-{profile}.yaml` as profile-specific overrides
- Automatically merges them in the correct order

---

## 🌍 Environment Variables

Override specific properties at runtime:

```bash
# Database
export POSTGRES_URL=jdbc:postgresql://your-host:5432/db
export MONGO_HOST=your-mongo-host

# Services
export CONFIG_SERVER_URL=http://your-config-server:8888
export SPRING_PROFILES_ACTIVE=prod

# Start service
./gradlew :identity-access:bootRun
```

---

## 📝 Adding New Services

To add a new microservice:

1. Create folder in `/configuration/`:
   ```bash
   mkdir -p /configuration/my-new-service
   ```

2. Create configuration files:
   ```
   application.yaml         (common)
   application-dev.yaml     (dev overrides)
   application-stage.yaml   (stage overrides)
   application-prod.yaml    (prod overrides)
   ```

3. Update service's `application.yaml`:
   ```yaml
   spring:
     application:
       name: my-new-service
     profiles:
       active: ${SPRING_PROFILES_ACTIVE:dev}
     config:
       import: "optional:configserver:${CONFIG_SERVER_URL:http://localhost:8888},optional:vault://"
   ```

4. Start the service:
   ```bash
   export SPRING_PROFILES_ACTIVE=dev
   ./gradlew :my-new-service:bootRun
   ```

---

## 🛠️ Troubleshooting

### Config files not found

```bash
# Check if folders exist
ls -R /configuration/

# Should show:
# identity-access/
# content-service/
# streaming-and-video/
# user-streaming-history/
# viora-streaming-gateway/
```

### Profile not loading correctly

```bash
# Verify profile is set
echo $SPRING_PROFILES_ACTIVE

# Test config server endpoint
curl http://localhost:8888/identity-access/dev | jq '.profiles'
```

### Wrong configuration loaded

```bash
# Check which files config server found
curl http://localhost:8888/identity-access/dev | jq '.propertySources[] | .name'

# Should show paths like:
# file:/path/to/configuration/identity-access/application.yaml
# file:/path/to/configuration/identity-access/application-dev.yaml
```

---

## 🎉 You're All Set!

Your configuration structure is now:
- ✅ **Organized** by service folders
- ✅ **Beautiful** with clear naming
- ✅ **Scalable** for adding new services
- ✅ **Maintainable** with logical grouping
- ✅ **Compatible** with Spring Cloud Config

Each service has its own dedicated folder with common and profile-specific configurations neatly organized!

