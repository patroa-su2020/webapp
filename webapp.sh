#!/bin/bash
#SERVICE_NAME=webappService
#PATH_TO_JAR=/home/ubuntu/webapp-0.0.1-SNAPSHOT.jar
#PID_PATH_NAME=/tmp/webappService-pid
#case $1 in
#start)
#       echo "Starting $SERVICE_NAME ..."
#  if [ ! -f $PID_PATH_NAME ]; then
#       nohup java -DDB_PASSWORD=Ankit#1992 -jar $PATH_TO_JAR /tmp 2>> /dev/null >>/dev/null &
#                   echo $! > $PID_PATH_NAME
#       echo "$SERVICE_NAME started ..."
#  else
#       echo "$SERVICE_NAME is already running ..."
#  fi
#;;
#stop)
#  if [ -f $PID_PATH_NAME ]; then
#         PID=$(cat $PID_PATH_NAME);
#         echo "$SERVICE_NAME stoping ..."
#         kill $PID;
#         echo "$SERVICE_NAME stopped ..."
#         rm $PID_PATH_NAME
#  else
#         echo "$SERVICE_NAME is not running ..."
#  fi
#;;
#restart)
#  if [ -f $PID_PATH_NAME ]; then
#      PID=$(cat $PID_PATH_NAME);
#      echo "$SERVICE_NAME stopping ...";
#      kill $PID;
#      echo "$SERVICE_NAME stopped ...";
#      rm $PID_PATH_NAME
#      echo "$SERVICE_NAME starting ..."
#      nohup java -DDB_PASSWORD=Ankit#1992 -jar $PATH_TO_JAR /tmp 2>> /dev/null >> /dev/null &
#      echo $! > $PID_PATH_NAME
#      echo "$SERVICE_NAME started ..."
#  else
#      echo "$SERVICE_NAME is not running ..."
#     fi     ;;
# esac
cd ~
sudo chmod +x /home/ubuntu/webapp.sh
chmod 755 webapp-0.0.1-SNAPSHOT.jar
java -DDB_PASSWORD=Ankit#1992 -jar webapp-0.0.1-SNAPSHOT.jar > /home/ubuntu/output 2> /home/ubuntu/output < /home/ubuntu/output &
