#!/bin/bash


pwd
ls
cd ~
chmod 755 webapp-0.0.1-SNAPSHOT.jar
java -DDB_PASSWORD=Ankit#1992 -jar webapp-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &
source /etc/profile.d/applicationScript.sh

cd /home/ubuntu
sudo cat > applicationScript.sh << START
#!/bin/bash
cd /home/ubuntu
echo "Inside /home/ubuntu"
sudo chmod 755 webapp-0.0.1-SNAPSHOT.jar
nohup java -DDB_PASSWORD=Ankit#1992 -jar /home/ubuntu/webapp-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &
START
chmod 777 /home/ubuntu/applicationScript.sh
echo "applicationScript created"
cd /etc/profile.d
sudo cp /home/ubuntu/applicationScript.sh applicationScript.sh

sudo chmod 777 /etc/profile.d/applicationScript.sh