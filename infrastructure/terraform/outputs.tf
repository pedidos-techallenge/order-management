# Outputs
output "documentdb_cluster_endpoint" {
  value = aws_docdb_cluster.documentdb_cluster.endpoint
  description = "DocumentDB cluster endpoint"
}

output "documentdb_security_group_id" {
  value = aws_security_group.documentdb_sg.id
  description = "Security group ID for DocumentDB access"
}