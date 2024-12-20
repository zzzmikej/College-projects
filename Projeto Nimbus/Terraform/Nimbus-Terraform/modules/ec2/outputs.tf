output "nimbus_public_ec2" {
  value = aws_instance.nimbus_frontend2.public_ip
}

output "nimbus_private_ec2" {
  value = aws_instance.nimbus_backend1.private_ip
}

output "ssh_connection_string" {
  value = "ssh -i ${var.key_pair_name}.pem ubuntu@${aws_instance.nimbus_frontend1.public_ip}"
}

output "private_ip_public" {
  value = aws_instance.nimbus_frontend2.private_ip
}

output "frontend" {
  value = [aws_instance.nimbus_frontend1.id, aws_instance.nimbus_frontend2.id]
}