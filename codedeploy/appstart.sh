#!/bin/bash


pwd
ls
cd ~
chmod 755 webapp-0.0.1-SNAPSHOT.jar
java -DDB_PASSWORD=Ankit#1992 -jar webapp-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &
source /etc/profile.d/applicationScript.sh