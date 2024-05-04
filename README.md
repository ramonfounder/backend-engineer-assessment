Sure, here's an updated `README.md` file for your project:

# Backend Engineer Assessment

## Introduction

This is a Spring Boot application using Java, Gradle, and Temporal. It also integrates with Stripe for payment processing. The application provides two APIs: `createAccount` and `updateAccount`.

## Setup

### Prerequisites

- Java 17 or later
- Docker
- Temporal
- Stripe API Keys
- IntelliJ IDEA

### Installation

#### Java

You can download Java from [Azul](https://www.azul.com/downloads/#zulu) or use [SDKMAN](https://sdkman.io/).

#### Docker

You can install Docker from the [official website](https://docs.docker.com/get-docker/).

#### Temporal

You can install Temporal from the [official website](https://docs.temporal.io/cli#install).

#### Stripe API Keys

Sign up for a Stripe account and get your API keys from the [Stripe Dashboard](https://dashboard.stripe.com/apikeys).

#### IntelliJ IDEA

You can download IntelliJ IDEA from the [official website](https://www.jetbrains.com/idea/download/).

### Configuration

Update the `application-prod.properties` and `application.properties` files with your environment variables.

## Run

### Development Mode

First, start the Temporal server:

```sh
temporal server start-dev
```

Then, run the application in IntelliJ IDEA.

### Production Mode

1. Build the Docker image:

```sh
docker build -t backend-engineer-assessment:1.0.0 .
```

2. In the `production` folder, there is a Docker Compose file and an environment file. The DevOps engineer can change the properties in the environment file to configure the application for the production environment.

3. Run the Docker container (for example):

```sh
docker-compose up -d
```

### Test Mode

The application is integrated with Testcontainers for integration testing. Run the tests in IntelliJ IDEA to test the `createAccount` and `updateAccount` APIs.

## APIs

### createAccount

Endpoint: `/api/accounts`
Method: `POST`
Payload: `{...}`

### updateAccount

Endpoint: `/api/accounts/{accountId}`
Method: `PATCH`
Payload: `{...}`
