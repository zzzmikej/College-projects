resource "aws_lb" "nimbus_alb" {
  name               = "nimbus-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [var.lb_security_id]
  subnets            = [var.front_ip_1, var.front_ip_2]

  enable_deletion_protection = false

  tags = {
    Name = "nimbus-alb"
  }
}