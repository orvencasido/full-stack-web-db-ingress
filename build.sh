#!/bin/bash

set -e 

docker stop be fe || true
docker rm be fe  || true
docker rmi be fe || true

docker build -t be be/ 
docker build -t fe fe/ 

#docker network create my-network

docker run -d --name be --network my-network -p 5000:5000 be
docker run -d --name fe --network my-network -p 80:80 fe
