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
# resource "aws_security_group" "docdb" {
#   name        = "docdb-security-group"  # Nome do grupo de segurança
#   description = "Security group for DocumentDB cluster"  # Descrição do grupo de segurança
#   vpc_id      = data.aws_vpc.techchallenge-vpc.id  # ID da VPC onde o grupo de segurança será criado

#   # Regras de entrada (ingress) para permitir tráfego na porta 27017 (padrão do MongoDB)
#   ingress {
#     from_port   = 27017  # Porta de origem
#     to_port     = 27017  # Porta de destino
#     protocol    = "tcp"  # Protocolo TCP
#     cidr_blocks = ["0.0.0.0/0"]  # Permitir acesso de qualquer IP (não recomendado para produção)
#   }

#   # Regras de saída (egress) para permitir todo o tráfego de saída
#   egress {
#     from_port   = 0  # Porta de origem
#     to_port     = 0  # Porta de destino
#     protocol    = "-1"  # Todos os protocolos
#     cidr_blocks = ["0.0.0.0/0"]  # Permitir acesso a qualquer IP
#   }

#   # Tags para identificação do grupo de segurança
#   tags = {
#     Name = "docdb-security-group"  # Nome da tag
#   }
# }

# Subnet Group para o DocumentDB
# resource "aws_docdb_subnet_group" "docdb" {
#   name       = "docdb-subnet-group"  # Nome do grupo de sub-rede
#   subnet_ids = data.aws_subnets.private_subnets.ids  # IDs das sub-redes privadas onde o DocumentDB será implantado

#   # Tags para identificação do grupo de sub-rede
#   tags = {
#     Name = "docdb-subnet-group"  # Nome da tag
#   }
# }

# Cluster DocumentDB
# resource "aws_docdb_cluster" "docdb" {
#   cluster_identifier      = "tech-challenge-docdb"  # Identificador do cluster
#   engine                  = "docdb"  # Tipo de banco de dados (DocumentDB)
#   engine_version          = "5.0.0"  # Versão do engine do DocumentDB
#   master_username         = var.mongodb_username  # Nome de usuário do master (definido em variáveis)
#   master_password         = var.mongodb_password  # Senha do master (definido em variáveis)
#   db_subnet_group_name    = aws_docdb_subnet_group.docdb.name  # Nome do grupo de sub-rede associado ao cluster
#   vpc_security_group_ids  = [aws_security_group.docdb.id]  # IDs dos grupos de segurança associados ao cluster
#   skip_final_snapshot     = true  # Ignorar a criação de um snapshot final ao excluir o cluster
#   deletion_protection     = false  # Permitir a exclusão do cluster
#   apply_immediately       = true  # Aplicar as alterações imediatamente

#   # Tags para identificação do cluster
#   tags = {
#     Name = "tech-challenge-docdb"  # Nome da tag
#   }
# }

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