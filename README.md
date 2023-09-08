# Crypto Trader Sim

Discover CryptoTraderSim, your go-to web app for risk-free cryptocurrency trading. Sign up securely, access real-time crypto market data, manage your virtual portfolio, practice paper trading, view transaction history, and set price alerts - all in one place. Sharpen your crypto trading skills without risking real money. Start today!

## Prerequisites

Before you begin, make sure you have the following installed:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/) (usually comes with Docker installation)

## Getting Started

Follow these steps to develop the Spring Boot application locally using Docker and access the Swagger UI for the API.

### 1. Clone the Repository

Clone this repository to your local machine using the following command:

```bash
git clone https://github.com/tunghng/Crypto-Paper-Trading.git
cd crypto-paper-trading-microservices
```

### 2. Run Docker Compose

Navigate to the root directory of the cloned repository and run the following command to start the Docker containers defined in the `docker-compose.yml` file:

```bash
docker compose -f docker-compose.local.yml up
```

This command will build and start the necessary Docker containers. 

### 3. Access Swagger UI

Once the Docker containers are up and running, start the Spring Boot service that you need and you can access the Swagger UI for interacting with the API. Open your web browser and enter the following URL:

[http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

The Swagger UI provides a user-friendly interface for exploring and testing the API endpoints provided by the Spring Boot application.

## Shutting Down

To stop the Docker containers and shut down the application, you can use the following command in the same terminal window where you started the containers:

```bash
docker compose down
```

This will gracefully shut down the containers and free up the resources.


