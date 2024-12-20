resource "aws_subnet" "nimbus_subnet_public" {
  vpc_id                  = aws_vpc.nimbus_vpc.id
  cidr_block              = "10.0.0.0/26"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-1a"

  tags = {
    Name = "${var.name_subnet_public}1"
  }
}

resource "aws_subnet" "nimbus_subnet_public_2" {
  vpc_id                  = aws_vpc.nimbus_vpc.id
  cidr_block              = "10.0.0.64/26"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-1b"

  tags = {
    Name = "${var.name_subnet_public}2"
  }
}

resource "aws_subnet" "nimbus_subnet_private" {
  vpc_id            = aws_vpc.nimbus_vpc.id
  cidr_block        = "10.0.0.128/26"
  availability_zone = var.availability_zone

  tags = {
    Name = var.name_subnet_private
  }
}