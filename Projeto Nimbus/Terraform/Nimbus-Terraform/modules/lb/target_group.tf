resource "aws_lb_target_group" "nimbus_target_group" {
  name        = "nimbus-frontend-target-group"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "instance"

  health_check {
    path                = "/"
    interval            = 60
    timeout             = 10
    healthy_threshold   = 2
    unhealthy_threshold = 2
    matcher             = "200"
  }

  tags = {
    Name = "nimbus-frontend-tg"
  }
}

resource "aws_lb_target_group_attachment" "nimbus_frontend_attachment" {
  count            = length(var.frontend)
  target_group_arn = aws_lb_target_group.nimbus_target_group.arn
  target_id        = var.frontend[count.index]
  port             = 80
}