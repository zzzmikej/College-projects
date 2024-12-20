variable "lb_security_id" {
  type = string
}

variable "front_ip_1" {
    type = string
}

variable "front_ip_2" {
    type = string
}

variable "vpc_id" {
  type = string
}

variable "frontend" {
  type = list(string)
}
