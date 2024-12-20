output "vpc_id" {
  value = aws_vpc.nimbus_vpc.id
}

output "public_subnet_id_1" {
  value = aws_subnet.nimbus_subnet_public.id
}

output "public_subnet_id_2" {
  value = aws_subnet.nimbus_subnet_public_2.id
}

output "private_subnet_id" {
  value = aws_subnet.nimbus_subnet_private.id
}

output "id_nat_gateway" {
  value = aws_nat_gateway.nimbus_nat_gateway.id
}

output "cidr_block_publica" {
  value = aws_subnet.nimbus_subnet_public.cidr_block
}