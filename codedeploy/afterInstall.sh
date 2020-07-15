#!/bin/bash

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ubuntu/opt/cloudwatch-config.json -s
sudo chmod +x /usr/local/bin/webapp.sh
#/usr/local/bin/./Service_Name.sh start
#/usr/local/bin/./Service_Name.sh stop
#/usr/local/bin/./Service_Name.sh restart


cd /home/ubuntu
sudo cat > applicationScript.sh << START
#!/bin/bash
cd /home/ubuntu
echo "Inside /home/ubuntu"
nohup java -DDB_PASSWORD=Ankit#1992 -jar /home/ubuntu/webapp-0.0.1-SNAPSHOT.jar
START
chmod 755 /home/ubuntu/applicationScript.sh
echo "applicationScript created"
cd /etc/profile.d
sudo cp /home/ubuntu/applicationScript.sh applicationScript.sh
sudo chmod 755 /etc/profile.d/applicationScript.sh