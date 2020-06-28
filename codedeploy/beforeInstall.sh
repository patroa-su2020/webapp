#!/bin/bash

pid=$(ps aux | pgrep -f webapp)
echo "PID To kill"
echo $pid
sudo kill -9 $pid