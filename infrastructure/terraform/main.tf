# Definindo a role IAM existente
data "aws_iam_role" "LabRole" {
  name = "LabRole"
}

# Recuperando a VPC existente
data "aws_vpc" "techchallenge-vpc" {
  filter {
    name   = "tag:Name"
    values = ["techchallenge-vpc"]
  }
}

# Filtrando subnets privadas
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

# Subnet group para o DocumentDB
resource "aws_db_subnet_group" "documentdb_subnet_group" {
  name        = "documentdb-subnet-group"
  subnet_ids  = data.aws_subnets.private_subnets.ids
  description = "Subnet group for DocumentDB in private subnets"
}

# Security Group para o DocumentDB
resource "aws_security_group" "documentdb_sg" {
  name        = "documentdb-sg"
  vpc_id      = data.aws_vpc.techchallenge-vpc.id
  description = "Allow MongoDB traffic to DocumentDB cluster"

  # Permitir acesso na porta padrão do MongoDB
  ingress {
    from_port   = 27017
    to_port     = 27017
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]  # Ajuste conforme necessário
  }

  # Egress irrestrito para saída
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Cluster Parameter Group para habilitar TLS
resource "aws_docdb_cluster_parameter_group" "documentdb_cluster_pg" {
  name        = "documentdb-cluster-pg"
  family      = "docdb4.0"
  description = "Parameter group with TLS enabled for DocumentDB"

  parameter {
    name  = "tls"
    value = "enabled"
  }
}

# Cluster DocumentDB
resource "aws_docdb_cluster" "documentdb_cluster" {
  cluster_identifier      = "techchallenge-docdb-cluster"
  engine                  = "docdb"
  master_username         = "your_master_username"
  master_password         = "your_master_password"  # Lembre-se de gerenciar isso com segurança, por exemplo com AWS Secrets Manager
  db_subnet_group_name    = aws_db_subnet_group.documentdb_subnet_group.name
  vpc_security_group_ids  = [aws_security_group.documentdb_sg.id]
  backup_retention_period = 5
  preferred_backup_window = "07:00-09:00"
  skip_final_snapshot     = true
  apply_immediately       = true  # Para aplicar imediatamente após a criação

  depends_on = [aws_docdb_cluster_parameter_group.documentdb_cluster_pg]
}

# Substituir resource por data source para usar projeto existente
data "mongodbatlas_project" "project" {
  name = "tech-challenge-project"
}

resource "mongodbatlas_cluster" "cluster" {
  project_id = data.mongodbatlas_project.project.id
  name       = "tech-challenge-cluster"

  provider_name = "TENANT"
  backing_provider_name = "AWS"
  provider_region_name = "US_EAST_1"
  
  cluster_type = "REPLICASET"
  replication_specs {
    num_shards = 1
    regions_config {
      region_name     = "US_EAST_1"
      electable_nodes = 3
      priority        = 7
      read_only_nodes = 0
    }
  }

  # Configurações do tier M0 (gratuito)
  provider_instance_size_name = "M0"

  depends_on = [aws_docdb_cluster.documentdb_cluster]
}

# Atualizar também a referência no IP access list
resource "mongodbatlas_project_ip_access_list" "ip_list" {
  project_id = data.mongodbatlas_project.project.id  # Atualizar referência aqui também
  cidr_block = data.aws_vpc.techchallenge-vpc.cidr_block
  comment    = "VPC CIDR"
}

# Output para string de conexão
output "mongodb_connection_string" {
  value     = mongodbatlas_cluster.cluster.connection_strings[0].standard_srv
  sensitive = true
}

# Criar o usuário do banco de dados com as permissões necessárias
resource "mongodbatlas_database_user" "db_user" {
  project_id         = data.mongodbatlas_project.project.id
  auth_database_name = "admin"
  username          = "turmafiap"
  password          = "TechChallange2024"

  roles {
    role_name     = "readWrite"
    database_name = "dbtechchallenge"
  }

  roles {
    role_name     = "dbAdmin"
    database_name = "dbtechchallenge"
  }

  scopes {
    name = mongodbatlas_cluster.cluster.name
    type = "CLUSTER"
  }
}

# Output para as credenciais de conexão
output "mongodb_credentials" {
  value = {
    username = mongodbatlas_database_user.db_user.username
    database = "dbtechchallenge"
  }
  sensitive = true
}