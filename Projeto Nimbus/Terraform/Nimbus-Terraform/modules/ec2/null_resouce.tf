resource "null_resource" "provisioner_loadbalance" {
  depends_on = [ aws_instance.loadbalance, local_file.nginx-loadbalance ]
  
  provisioner "remote-exec" {
    inline = [
      "sudo usermod -aG ubuntu",
      "sudo apt update -y && sudo apt upgrade -y",
      "sudo apt install nginx -y"
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("./nimbus.pem")
      host        = aws_instance.loadbalance.public_ip
    }
  }

  provisioner "file" {
    source      = "./modules/ec2/nginx.conf"
    destination = "/home/ubuntu/nginx.conf"

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("./nimbus.pem")
      host        = aws_instance.loadbalance.public_ip
    }
  }

  provisioner "remote-exec" {
    inline = [
      "sudo rm /etc/nginx/nginx.conf",
      "sudo mv /home/ubuntu/nginx.conf /etc/nginx/",
      "sudo nginx -t",
      "sudo systemctl restart nginx"
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("./nimbus.pem")
      host        = aws_instance.loadbalance.public_ip
    }
  }  
}

locals {
  frontend_ips = {
    frontend1 = aws_instance.nimbus_frontend1.public_ip
    frontend2 = aws_instance.nimbus_frontend2.public_ip
  }
}

resource "null_resource" "provisioner_frontend" {
  depends_on = [ aws_instance.nimbus_frontend1, aws_instance.nimbus_frontend2 ]
  for_each = local.frontend_ips

  provisioner "file" {
    source      = "./nimbus.pem"
    destination = "/home/ubuntu/nimbus.pem"

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("./nimbus.pem")
      host        = each.value
    }
  }

  provisioner "remote-exec" {
    inline = [
      "sudo chmod 400 nimbus.pem",
      "sudo usermod -aG ubuntu",
      "sudo apt update -y && sudo apt upgrade -y",
      "sudo apt install git -y",
      "sudo apt install nginx -y",
      "cd /etc/nginx/sites-available/",
      "sudo git clone https://github.com/sptech-nimbus/client.git",
      "sudo mv client nimbus-app",
      "sudo chmod 755 /etc/nginx/",
      "sudo bash -c 'cat > /etc/nginx/sites-available/nimbus-app/src/api/config.js << EOF",
      "const config = {",
        "baseURL: \"http://${aws_instance.nimbus_frontend1.public_ip}:8080\",",
        "baseURLChat: \"http://${aws_instance.nimbus_frontend1.public_ip}:3000\"",
      "}",
      "export default config;",
      "EOF'",
      "cd nimbus-app",
      "sudo curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -",
      "sudo apt install nodejs -y",
      "sudo npm install",
      "sudo npm audit fix",
      "sudo npm run build",
      "sudo mv build /etc/nginx/sites-available/",
      "sudo mkdir -p /var/cache/nginx"
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("./nimbus.pem")
      host        = each.value
    }
  }
}

locals {
  backend_ips = {
    backend1 = aws_instance.nimbus_backend1.private_ip
    backend2 = aws_instance.nimbus_backend2.private_ip
  }
}

resource "null_resource" "provisioner_private" {
  depends_on = [ aws_instance.nimbus_backend1, aws_instance.nimbus_backend2 ]
  for_each = local.backend_ips

  provisioner "remote-exec" {
    inline = [
      "sudo mkdir -p /home/ubuntu/nimbus",
      "sudo chown ubuntu:ubuntu /home/ubuntu/nimbus",
      "sudo chmod 755 /home/ubuntu/nimbus",
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("./nimbus.pem")
      host        = each.value


      bastion_host        = aws_instance.nimbus_frontend1.public_ip
      bastion_user        = "ubuntu"
      bastion_private_key = file("./nimbus.pem")
      agent               = false
    }
  }

  provisioner "file" {
    source      = "./docker-compose.yml"
    destination = "/home/ubuntu/nimbus/docker-compose.yml"

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("./nimbus.pem")
      host        = each.value


      bastion_host        = aws_instance.nimbus_frontend1.public_ip
      bastion_user        = "ubuntu"
      bastion_private_key = file("./nimbus.pem")
      agent               = false
    }
  }

  provisioner "remote-exec" {

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("./nimbus.pem")
      host        = each.value


      bastion_host        = aws_instance.nimbus_frontend1.public_ip
      bastion_user        = "ubuntu"
      bastion_private_key = file("./nimbus.pem")
      agent               = false
    }

    inline = [
      "sudo apt update -y && sudo apt upgrade -y",
      "sudo apt install -y docker.io",
      "sudo apt install -y docker-compose",
      "sudo systemctl start docker",
      "sudo docker-compose -f /home/ubuntu/nimbus/docker-compose.yml up -d"
    ]
  }
}

resource "null_resource" "provisioner_nginx_conf" {
  depends_on = [ local_file.nginx ]
  for_each = local.frontend_ips
  provisioner "file" {
    source      = "./nginx.conf"
    destination = "/home/ubuntu/nginx.conf"

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("./nimbus.pem")
      host        = each.value
    }
  }

  provisioner "remote-exec" {
    inline = [
      "sudo apt install nginx -y",
      "sudo rm /etc/nginx/nginx.conf",
      "sudo mv /home/ubuntu/nginx.conf /etc/nginx/",
      "sudo nginx -t",
      "sudo systemctl restart nginx"
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("./nimbus.pem")
      host        = each.value
    }
  }
}