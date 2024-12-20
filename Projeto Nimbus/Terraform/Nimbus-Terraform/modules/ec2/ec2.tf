resource "aws_key_pair" "nimbus_key" {
  key_name   = var.key_pair_name
  public_key = file("./nimbus.pem.pub")
}

resource "aws_instance" "loadbalance" {
  ami                         = var.ami
  instance_type               = var.instance_type
  subnet_id                   = var.public_subnet_id_1
  associate_public_ip_address = true

  root_block_device {
    volume_size = 20
    volume_type = "gp2"
  }
  
  key_name               = var.key_pair_name
  security_groups = [var.nimbus_sg_public]

  tags = {
    Name = "Load-Balance"
  }
}

resource "aws_instance" "nimbus_frontend1" {
  ami                         = var.ami
  instance_type               = var.site_instance
  subnet_id                   = var.public_subnet_id_1
  associate_public_ip_address = true

  root_block_device {
    volume_size = 20
    volume_type = "gp2"
  }
  
  key_name               = var.key_pair_name
  security_groups = [var.nimbus_sg_public]

  tags = {
    Name = "Nimbus-FrontEnd1"
  }
}

resource "aws_instance" "nimbus_frontend2" {
  depends_on = [ aws_instance.nimbus_frontend1 ]
  ami                         = var.ami
  instance_type               = var.site_instance
  subnet_id                   = var.public_subnet_id_2
  associate_public_ip_address = true

  root_block_device {
    volume_size = 20
    volume_type = "gp2"
  }

  key_name               = var.key_pair_name
  security_groups = [var.nimbus_sg_public]

  tags = {
    Name = "Nimbus-FrontEnd2"
  }
}


resource "aws_instance" "nimbus_backend1" {
  depends_on = [ aws_instance.nimbus_frontend2 ]
  ami           = var.ami
  instance_type = var.instace_for_backend
  subnet_id     = var.private_subnet_id

  root_block_device {
    volume_size = 50
    volume_type = "gp2"
  }

  key_name               = var.key_pair_name
  vpc_security_group_ids = [var.nimbus_sg_private]

  tags = {
    Name = "Nimbus-EC2-Private"
  }

}

resource "aws_instance" "nimbus_backend2" {
  depends_on = [ aws_instance.nimbus_backend1 ]

  ami           = var.ami
  instance_type = var.instace_for_backend
  subnet_id     = var.private_subnet_id

  root_block_device {
    volume_size = 50
    volume_type = "gp2"
  }

  key_name               = var.key_pair_name
  vpc_security_group_ids = [var.nimbus_sg_private]

  tags = {
    Name = "Nimbus-Back-loadbalance"
  }

}

locals {
  public_ip = aws_instance.nimbus_frontend1.public_ip
  public_ip2= aws_instance.nimbus_frontend2.public_ip
  loadbalance = aws_instance.loadbalance.public_ip
}

data "template_file" "docker_compose" {
  template = file("./modules/ec2/templates/docker-compose.tpl.yml")
  vars = {
    public_ip = local.public_ip
    public_ip2 = local.public_ip2
    cors_origin = local.loadbalance
  }
}

data "template_file" "nginx_config" {
  template = file("./modules/ec2/templates/nginx.tpl.conf")
  vars = {
    public_ip = local.public_ip
    public_ip2 = local.public_ip2
    loadbalance_private_ip = aws_instance.nimbus_backend2.private_ip
    private_ip = aws_instance.nimbus_backend1.private_ip
    loadbalance = local.loadbalance
  }
}

data "template_file" "nginx_config_loadbalance" {
  template = file("./modules/ec2/templates/nginx_load.tpl.conf")
  vars = {
    public_ip = local.public_ip
    public_ip2 = local.public_ip2
  }
}

resource "local_file" "docker_compose" {
  content  = data.template_file.docker_compose.rendered
  filename = "./docker-compose.yml"
}

resource "local_file" "nginx" {
  content  = data.template_file.nginx_config.rendered
  filename = "./nginx.conf"
}

resource "local_file" "nginx-loadbalance" {
  content = data.template_file.nginx_config_loadbalance.rendered
  filename = "./modules/ec2/nginx.conf"
}