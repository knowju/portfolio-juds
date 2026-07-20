# cloud-deploy-cicd

Cómo se **contieneriza y despliega** una API del portafolio (`crud-clientes-api`) a **AWS** con un pipeline **CI/CD**. Reúne los artefactos de la parte de infraestructura del puesto: Docker, GitHub Actions e IaC con Terraform.

```
push a main ─▶ GitHub Actions ─▶ build+test (Gradle) ─▶ docker build
                                                            │
                                              push a Amazon ECR
                                                            │
                                              deploy a ECS Fargate ─▶ API en AWS
```

## Contenido

| Archivo | Qué es |
|---|---|
| `Dockerfile` | Imagen multi-stage (build del jar + runtime JRE, usuario no-root) |
| `docker-compose.yml` | Levanta la API en contenedor localmente |
| `ci-cd.yml` | Pipeline de referencia (build → ECR → ECS) con OIDC hacia AWS |
| `terraform/` | IaC: repositorio ECR + cluster ECS + task definition Fargate |

## Probar el contenedor localmente

```bash
# build de la imagen (contexto = la app Spring Boot)
docker build -f infra/cloud-deploy-cicd/Dockerfile -t crud-clientes-api java/crud-clientes-api
docker run -p 8080:8080 crud-clientes-api
# → http://localhost:8080/swagger-ui.html

# o con compose
docker compose -f infra/cloud-deploy-cicd/docker-compose.yml up --build
```

## Qué demuestra

- **Contenerización** con imagen multi-stage y usuario no-root (buenas prácticas).
- **CI/CD**: build+test automáticos en cada push; deploy solo desde `main`.
- **Autenticación sin llaves estáticas**: OIDC de GitHub → rol de AWS (`aws-actions/configure-aws-credentials`).
- **IaC** con Terraform: infraestructura versionada y reproducible (ECR + ECS Fargate).

## Notas

- `ci-cd.yml` se deja **aquí** (no en `.github/workflows/`) a propósito: para activarlo se mueve a la raíz y se configuran los secrets (`AWS_ROLE_ARN`, etc.). Así el repo no intenta desplegar sin credenciales.
- `terraform apply` requiere una cuenta de AWS; el módulo se incluye como muestra de IaC.

## Stack

Docker (multi-stage) · GitHub Actions · Terraform · AWS ECR + ECS Fargate · OIDC
