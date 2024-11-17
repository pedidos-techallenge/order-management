# Outputs
# output "docdb_endpoint" {
#   value = aws_docdb_cluster.docdb.endpoint
# }

# output "docdb_credentials" {
#   value = {
#     username = aws_docdb_cluster.docdb.master_username
#     database = "dbtechchallenge"
#   }
#   sensitive = true
# }

output "mongodb_connection_string" {
  description = "MongoDB Atlas Connection String"
  value       = mongodbatlas_cluster.techchallenge.connection_strings[0].standard_srv
  sensitive   = true
}

output "mongodb_cluster_host" {
  description = "MongoDB Atlas Cluster Host"
  value       = mongodbatlas_cluster.techchallenge.connection_strings[0].standard
  sensitive   = true
}

output "mongodb_cluster_state" {
  description = "MongoDB Atlas Cluster State"
  value       = mongodbatlas_cluster.techchallenge.state_name
}