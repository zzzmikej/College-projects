variable "name_vpc" {
  description = "Nome para a VPC"
  default     = "nimbus-vpc"
}

variable "name_subnet_public" {
  description = "Nome para a Subnet Publica"
  default     = "nimbus-frontend"
}

variable "name_subnet_private" {
  description = "Nome para a Subnet Privada"
  default     = "nimbus-subnet-private"
}

variable "name_igw_public" {
  description = "Nome para a Internet Gateway Publica"
  default     = "nimbus-igw-public"
}

variable "availability_zone" {
  description = "Zona de Disponibilidade"
  default     = "us-east-1a"
}

variable "name_route_table_public" {
  description = "Nome da Tabela de rotas"
  default     = "nimbus-route-table-public"
}

variable "name_route_table_private" {
  description = "Nome da Tabela de rotas"
  default     = "nimbus-route-table-private"
}

variable "nimbus_public" {
  type = string
}
