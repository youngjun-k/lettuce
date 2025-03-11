#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Setting up Lettuce development environment...${NC}"

# Check if .env file exists, if not create it from template
if [ ! -f .env ]; then
    echo -e "${YELLOW}Creating .env file from template...${NC}"
    cp .env.template .env
    echo -e "${GREEN}Created .env file. Please update it with your actual values.${NC}"
else
    echo -e "${YELLOW}.env file already exists. Skipping...${NC}"
fi

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed. Please install Docker and Docker Compose first.${NC}"
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}Docker Compose is not installed. Please install Docker Compose first.${NC}"
    exit 1
fi

# Start Docker containers
echo -e "${YELLOW}Starting Docker containers...${NC}"
docker-compose up -d

# Wait for MySQL to be ready
echo -e "${YELLOW}Waiting for MySQL to be ready...${NC}"
until docker exec lettuce-mysql mysqladmin ping -h localhost -u root -proot --silent; do
    echo -e "${YELLOW}Waiting for MySQL to be ready...${NC}"
    sleep 3
done

echo -e "${GREEN}MySQL is ready!${NC}"

# Check if Gradle wrapper exists
if [ ! -f ./gradlew ]; then
    echo -e "${RED}Gradle wrapper not found. Please run 'gradle wrapper' first.${NC}"
    exit 1
fi

# Build the project
echo -e "${YELLOW}Building the project...${NC}"
./gradlew clean build -x test

# Run database migrations
echo -e "${YELLOW}Running database migrations...${NC}"
./gradlew migrateDatabase

echo -e "${GREEN}Setup complete! You can now run the application with:${NC}"
echo -e "${YELLOW}./gradlew bootRun${NC}"
echo -e "${GREEN}Or access the following services:${NC}"
echo -e "${YELLOW}MySQL: localhost:3306${NC}"
echo -e "${YELLOW}Redis: localhost:6379${NC}"
echo -e "${YELLOW}MinIO: localhost:9000 (Console: localhost:9001)${NC}"
echo -e "${YELLOW}Adminer: localhost:8081${NC}"
echo -e "${YELLOW}Prometheus: localhost:9090${NC}"
echo -e "${YELLOW}Grafana: localhost:3000${NC}" 