#!/bin/bash

pwd
ps
pid=$(ps aux | pgrep -f webapp)
echo "PID To kill"
echo "$pid"
