#!/usr/bin/env bash

# Build project
gradle clean build

# Shut down docker-compose
docker-compose down

# Prepare a full clean restart:
docker system prune --force
docker container prune --force
docker volume prune --force
docker image prune --force

# Build images
docker-compose build

# Run docker-compose
docker-compose up -d
