# Data sources para recursos existentes
data "aws_vpc" "techchallenge-vpc" {
  filter {
    name   = "tag:Name"
    values = ["techchallenge-vpc"]
  }
}

data "aws_subnets" "private_subnets" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.techchallenge-vpc.id]
  }
  filter {
    name   = "tag:Name"
    values = ["techchallenge-vpc-private*"]
  }
}

# Data source para obter o NAT Gateway do EKS
data "aws_nat_gateway" "eks_nat" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.techchallenge-vpc.id]
  }
  filter {
    name   = "state"
    values = ["available"]
  }
}

# Projeto MongoDB Atlas
resource "mongodbatlas_project" "techchallenge" {
  name   = "tech-challenge"
  org_id = var.mongodb_atlas_org_id
}

# Cluster MongoDB Atlas (Free Tier M0)
resource "mongodbatlas_cluster" "techchallenge" {
  project_id                 = mongodbatlas_project.techchallenge.id
  name                      = "tech-challenge-cluster"
  provider_name             = "TENANT"
  backing_provider_name     = "AWS"
  provider_region_name      = "US_EAST_1"
  provider_instance_size_name = "M0"

  # Configurações fixas para M0
  mongo_db_major_version    = "6.0"
  auto_scaling_disk_gb_enabled = false

  # Evitar atualizações desnecessárias
  lifecycle {
    ignore_changes = [
      mongo_db_major_version,
      auto_scaling_disk_gb_enabled,
      backing_provider_name,
      provider_instance_size_name,
      provider_name,
      provider_region_name
    ]
  }
}

# IP do ambiente de desenvolvimento local
resource "mongodbatlas_project_ip_access_list" "local_dev" {
  project_id = mongodbatlas_project.techchallenge.id
  ip_address = var.local_ip
  comment    = "Local development environment"
}

# IP do cluster EKS
resource "mongodbatlas_project_ip_access_list" "eks" {
  project_id = mongodbatlas_project.techchallenge.id
  ip_address = data.aws_nat_gateway.eks_nat.public_ip
  comment    = "EKS cluster NAT Gateway"
}