# Dev VM Deployment

## Scope

This repo deploys `notificationservice-sox` to the existing dev VM as a Docker container on the shared runtime host.

- runtime mode: Docker container on the VM
- active Spring profile: `dev`

The deployment is push-based:

- GitHub Actions builds and publishes an immutable image
- GitHub Actions uploads a small rollout bundle to the VM
- the VM is runtime-only
- no `git pull` happens on the VM

## Runtime contract on the VM

Container contract:

- container name: `notificationservice-sox`
- bind address: `127.0.0.1:8081`
- container port: `8080`
- restart policy: `unless-stopped`
- Docker network: `sitionix-dev`
- Spring profile: `dev`

The container consumes two VM-side env files:

- shared internal-auth file:
  - `/opt/sitionix/runtime/shared/dev-internal-auth.env`
- notification-only runtime file:
  - `/opt/sitionix/runtime/notificationservice-sox/shared/notificationservice-sox.dev.env`

The deploy script does not create or overwrite the shared internal-auth file.
It requires that file to already exist on the VM.

The notification-only runtime file contains:

- `SPRING_PROFILES_ACTIVE=dev`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092`
- `API_REST_CLIENT_ATHSSOX_BASE_PATH=http://authorisationservice-sox:9090/authsox`
- `API_REST_CLIENT_BFFSSOX_BASE_PATH=http://bffssox-service:8080/bffssox`

## Downstream topology

Notification uses the existing shared dev runtime:

- Kafka: `kafka:9092`
- auth service: `http://authorisationservice-sox:9090/authsox`
- BFF: `http://bffssox-service:8080/bffssox`

These names must resolve from inside `sitionix-dev`.

## Shared internal auth contract

This service consumes the existing shared VM secret file:

- `/opt/sitionix/runtime/shared/dev-internal-auth.env`

That file remains infra-owned.
Notification deploy does not materialize `FORGE_SECURITY_DEV_JWT_SECRET` itself.

## Workflow flow

Push workflow:
- `.github/workflows/dev-deploy-on-push.yml`

Comment router:
- `.github/workflows/deploy-on-comment.yml`

Comment execution workflow:
- `.github/workflows/service-deploy-on-command.yml`

Composite actions:
- `.github/actions/dev-deploy-run/action.yml`
- `.github/actions/dev-deploy-resolve/action.yml`
- `.github/actions/materialize-maven-settings/action.yml`

Flow:

1. build and publish immutable image to GHCR
2. create a small release payload with:
   - VM deploy script
   - release manifest
   - non-secret release env
   - runtime secret env
3. upload the payload to the VM over SSH
4. run the VM deploy script
5. wait for local actuator readiness on `127.0.0.1:8081`
6. verify private readiness and health through an SSH tunnel

Comment deploy command:

```text
/deploy service --name notificationservice-sox --env dev
```

## Verification

The deployment uses private verification through an SSH tunnel:

1. private readiness on `http://127.0.0.1:8081/actuator/health/readiness`
2. private health on `http://127.0.0.1:8081/actuator/health`

This proves:

- the container booted under `dev`
- the deployed notification service is reachable on the VM loopback bind
- Spring actuator health endpoints are serving correctly

## GitHub contract

Repository vars:

- `MAVEN_REPOSITORY_USERNAME`

Repository secrets:

- `MAVEN_REPOSITORY_TOKEN`
- `SONAR_TOKEN`

GitHub Environment `dev` vars:

- `DEPLOY_VM_PORT`
  - optional
  - default SSH port: `22`

GitHub Environment `dev` secrets:

- `DEPLOY_VM_HOST`
- `DEPLOY_VM_USER`
- `DEPLOY_VM_SSH_PRIVATE_KEY`
- `GHCR_PULL_USERNAME`
- `GHCR_PULL_TOKEN`

## VM prerequisites

The VM must already provide:

- Docker installed and usable by the deploy user
- the `sitionix-dev` Docker network, or permission for the deploy user to create it
- Kafka reachable as `kafka:9092` from inside `sitionix-dev`
- auth reachable as `authorisationservice-sox:9090`
- BFF reachable as `bffssox-service:8080`
- shared internal auth file at `/opt/sitionix/runtime/shared/dev-internal-auth.env`
