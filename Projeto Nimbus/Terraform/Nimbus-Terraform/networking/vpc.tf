resource "aws_vpc" "nimbus_vpc" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = var.name_vpc
  }
}