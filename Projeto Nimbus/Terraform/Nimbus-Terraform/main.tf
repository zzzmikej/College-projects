module "network" {
  source        = "./networking"
  nimbus_public = module.ec2.nimbus_public_ec2
}

module "ec2" {
  source             = "./modules/ec2"
  vpc_id             = module.network.vpc_id
  nimbus_sg_public   = aws_security_group.nimbus_sg_public.id
  nimbus_sg_private  = aws_security_group.nimbus_sg_private.id
  public_subnet_id_1 = module.network.public_subnet_id_1
  public_subnet_id_2 = module.network.public_subnet_id_2
  private_subnet_id  = module.network.private_subnet_id
}

# module "lb" {
#   source         = "./modules/lb"
#   lb_security_id = aws_security_group.alb_sg.id
#   front_ip_1     = module.network.public_subnet_id_1
#   front_ip_2     = module.network.public_subnet_id_2
#   vpc_id         = module.network.vpc_id
#   frontend       = module.ec2.frontend
# }

output "nimbus_vpc" {
  value = module.network.vpc_id
}

output "private_ip_public" {
  value = module.ec2.private_ip_public
}

output "cidr_block_publica" {
  value = module.network.cidr_block_publica
}

output "ssh_connection_string" {
  value = module.ec2.ssh_connection_string
}