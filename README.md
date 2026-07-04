# Notification Service

A modern full-stack notification system built with Spring Boot, RabbitMQ, WebSockets, React, and Vite. It demonstrates real-time order notifications from a backend event flow to a live dashboard experience.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.15-brightgreen)
![React](https://img.shields.io/badge/React-19-blue)
![Vite](https://img.shields.io/badge/Vite-8-purple)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED)

## ✨ Overview

This project combines:
- a Spring Boot backend for authentication, order management, and event publishing
- RabbitMQ for asynchronous messaging
- WebSocket/STOMP for real-time notifications
- a React frontend dashboard for sending orders and viewing live events

It is designed as a practical example of event-driven architecture with a responsive UI.

## 🚀 Features

- User login with JWT-based authentication
- Order creation and persistence in PostgreSQL
- RabbitMQ-based event publishing
- Real-time notification delivery through WebSockets
- Live dashboard UI for monitoring incoming events
- Docker-ready setup for fast local development

## 🧰 Tech Stack

### Backend
- Java 17
- Spring Boot 3.5
- Spring Security
- Spring WebSocket
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- JWT

### Frontend
- React 19
- Vite
- Axios
- STOMP over WebSockets

## 📁 Project Structure

```text
notification-service/
├── frontend/                # React + Vite client
├── notification-service/    # Spring Boot backend
│   ├── src/main/java/       # Application code
│   ├── src/main/resources/  # Configuration and properties
│   └── pom.xml              # Maven build file
└── README.md                # Project documentation
```

## ⚙️ Prerequisites

Before running the app, make sure you have:
- Java 17+
- Maven or the Maven wrapper included in the backend folder
- Node.js 18+
- Docker Desktop (optional, recommended)

## ▶️ Quick Start with Docker

From the backend folder, run:

```bash
cd notification-service
docker compose up --build
```

This will start:
- PostgreSQL
- RabbitMQ
- the Spring Boot application
- the app will be available at http://localhost:8080

The frontend can be run separately with:

```bash
cd frontend
npm install
npm run dev
```

Then open http://localhost:5173

## 🛠️ Backend Development

Run the backend locally:

```bash
cd notification-service
./mvnw spring-boot:run
```

### Default Demo Credentials
- Username: admin
- Password: password123

## 🔌 API Endpoints

### Authentication
- POST /api/auth/login
  - Authenticates the user and returns a JWT token

### Orders
- POST /api/orders
  - Creates an order and publishes an event to RabbitMQ

## 🔗 Real-Time Flow

1. A user logs in from the React frontend.
2. The frontend sends an order to the backend.
3. The backend stores the order in PostgreSQL.
4. An event is published to RabbitMQ.
5. The consumer forwards the message through WebSockets.
6. The UI updates instantly with the latest notification.

## 🧪 Environment Notes

The backend uses environment-based configuration. Key values are defined in [notification-service/src/main/resources/application.properties](notification-service/src/main/resources/application.properties).

Common defaults include:
- PostgreSQL host: localhost
- RabbitMQ host: localhost
- app port: 8080

## 📌 Notes

This project is a learning and demo-friendly implementation of event-driven communication and real-time updates. It is ideal for exploring how Spring Boot, RabbitMQ, and WebSockets work together in a practical application.

## 🤝 Contributing

Contributions are welcome. If you want to improve the UI, expand the API, or add tests, feel free to open a pull request.
