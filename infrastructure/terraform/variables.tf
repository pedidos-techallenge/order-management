variable "mongodb_username" {
  description = "MongoDB Atlas username"
  type        = string
}

variable "mongodb_password" {
  description = "MongoDB Atlas password"
  type        = string
  sensitive   = true
}

variable "mongodb_atlas_org_id" {
  description = "MongoDB Atlas Organization ID"
  type        = string
}

variable "mongodb_atlas_public_key" {
  description = "MongoDB Atlas Public Key"
  type        = string
}

variable "mongodb_atlas_private_key" {
  description = "MongoDB Atlas Private Key"
  type        = string
  sensitive   = true
}

variable "local_ip" {
  description = "IP do ambiente de desenvolvimento local"
  type        = string
}