#!/bin/bash

pwd
ps
pid=$(ps aux | pgrep -f webapp)
echo "PID To kill"
echo "$pid"

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




