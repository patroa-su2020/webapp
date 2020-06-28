#!/bin/bash

pwd
ps
pid=$(ps aux | pgrep -f webapp)
echo "PID To kill"
echo "$pid"

sudo lsof -i:8080 -t | xargs -r sudo kill
