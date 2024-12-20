resource "aws_route_table" "nimbus_route_table_public" {
  vpc_id = aws_vpc.nimbus_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.nimbus_igw_public.id
  }

  tags = {
    Name = var.name_route_table_public
  }
}

resource "aws_route_table_association" "nimbus_association_public" {
  subnet_id      = aws_subnet.nimbus_subnet_public.id
  route_table_id = aws_route_table.nimbus_route_table_public.id
}

resource "aws_route_table" "nimbus_route_table_frontend" {
  vpc_id = aws_vpc.nimbus_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.nimbus_igw_public.id
  }

  tags = {
    Name = var.name_route_table_public
  }
}

resource "aws_route_table_association" "nimbus_association_frontend" {
  subnet_id      = aws_subnet.nimbus_subnet_public_2.id
  route_table_id = aws_route_table.nimbus_route_table_frontend.id
}

resource "aws_route_table" "nimbus_route_table_private" {
  vpc_id = aws_vpc.nimbus_vpc.id
  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nimbus_nat_gateway.id
  }
  tags = {
    Name = var.name_route_table_private
  }
}

resource "aws_route_table_association" "private_association" {
  subnet_id      = aws_subnet.nimbus_subnet_private.id
  route_table_id = aws_route_table.nimbus_route_table_private.id
}