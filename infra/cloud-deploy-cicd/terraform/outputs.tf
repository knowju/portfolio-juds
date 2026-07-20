output "ecr_repository_url" {
  description = "URL del repositorio ECR para hacer docker push"
  value       = aws_ecr_repository.api.repository_url
}

output "ecs_cluster_name" {
  description = "Nombre del cluster ECS"
  value       = aws_ecs_cluster.this.name
}
