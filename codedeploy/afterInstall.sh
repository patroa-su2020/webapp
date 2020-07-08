#!/bin/bash

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ubuntu/opt/cloudwatch-config.json -s
sudo chmod +x /usr/local/bin/webappService.sh
#/usr/local/bin/./Service_Name.sh start
#/usr/local/bin/./Service_Name.sh stop
#/usr/local/bin/./Service_Name.sh restart


