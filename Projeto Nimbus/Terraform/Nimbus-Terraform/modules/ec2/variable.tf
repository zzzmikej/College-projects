variable "ami" {
  description = "AMI padrão"
  type        = string
  default     = "ami-0e86e20dae9224db8"
}

variable "instance_type" {
  description = "Instância Padrão EC2"
  type        = string
  default     = "t2.nano"
}

variable "site_instance" {
  description = "Instância Padrão do Site"
  type        = string
  default     = "t2.small"
}

variable "instace_for_backend" {
  description = "Instancia padrão para o Backend"
  type = string
  default = "t2.medium"
}

variable "nimbus_sg_public" {
  type = string
}

variable "nimbus_sg_private" {
  type = string
}

variable "key_pair_name" {
  type    = string
  default = "nimbus"
}

variable "vpc_id" {
  description = "ID da VPC onde as instâncias serão lançadas"
  type        = string
}

variable "public_subnet_id_1" {
  type = string
}

variable "public_subnet_id_2" {
  type = string
}

variable "private_subnet_id" {
  type = string
}