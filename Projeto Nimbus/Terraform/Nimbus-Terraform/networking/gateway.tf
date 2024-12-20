resource "aws_eip" "nimbus_nat_eip" {
}

resource "aws_nat_gateway" "nimbus_nat_gateway" {
  allocation_id     = aws_eip.nimbus_nat_eip.id
  subnet_id         = aws_subnet.nimbus_subnet_public.id
  connectivity_type = "public"

  tags = {
    Name = "nimbus_nat_gateway"
  }
}

resource "aws_internet_gateway" "nimbus_igw_public" {
  vpc_id = aws_vpc.nimbus_vpc.id
  tags = {
    Name = var.name_igw_public
  }
}