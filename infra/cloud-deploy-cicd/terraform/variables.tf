variable "aws_region" {
  description = "Región de AWS"
  type        = string
  default     = "us-east-1"
}

variable "app_name" {
  description = "Nombre de la aplicación / repositorio ECR"
  type        = string
  default     = "crud-clientes-api"
}
