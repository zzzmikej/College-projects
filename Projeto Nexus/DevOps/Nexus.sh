sudo adduser adminNexus
sudo usermod -a -G sudo adminNexus
su adminNexus
sudo apt update && sudo apt upgrade

java -version
if [ $? = 0 ]; then
  echo "Java ja está instalado"
else
  echo "Java não está instalado."
  echo "Pressione 's' para instalar, ou qualquer outra tecla para cancelar"
  read get
  if [ "$get" == "s" ]; then
    sudo apt install openjdk-17-jre -y
  else
    echo "Instalacao Cancelada."
    exit 1
  fi
fi

git clone https://github.com/Nexus-Enterprises/login-Java.git
cd login-Java/Nexus/target
java -jar Nexus-1.0-jar-with-dependencies.jar