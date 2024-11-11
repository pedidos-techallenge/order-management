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

# Security Group para o DocumentDB
resource "aws_security_group" "docdb" {
  name        = "docdb-security-group"
  description = "Security group for DocumentDB cluster"
  vpc_id      = data.aws_vpc.techchallenge-vpc.id

  ingress {
    from_port   = 27017
    to_port     = 27017
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "docdb-security-group"
  }
}

# Subnet Group para o DocumentDB
resource "aws_docdb_subnet_group" "docdb" {
  name       = "docdb-subnet-group"
  subnet_ids = data.aws_subnets.private_subnets.ids

  tags = {
    Name = "docdb-subnet-group"
  }
}

# Cluster DocumentDB
resource "aws_docdb_cluster" "docdb" {
  cluster_identifier      = "tech-challenge-docdb"
  engine                  = "docdb"
  engine_version         = "5.0.0"
  master_username         = var.mongodb_username
  master_password         = var.mongodb_password
  db_subnet_group_name    = aws_docdb_subnet_group.docdb.name
  vpc_security_group_ids  = [aws_security_group.docdb.id]
  skip_final_snapshot     = true
  deletion_protection     = false
  apply_immediately       = true

  tags = {
    Name = "tech-challenge-docdb"
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