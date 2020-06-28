#!/bin/bash
sudo lsof -i:8080 -t | xargs -r sudo kill