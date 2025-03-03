# Lettuce Project Structure

## Overview

This document provides a comprehensive view of the Lettuce project's architecture and structure, highlighting the key components and their relationships.

## Directory Structure

```
lettuce/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── lettuce/
│   │   │               ├── domain/                     # Domain Layer
│   │   │               │   ├── carbonfootprint/        # Carbon Footprint Domain
│   │   │               │   │   ├── aggregate/          # Aggregate Roots
│   │   │               │   │   ├── event/              # Domain Events
│   │   │               │   │   ├── repository/         # Repositories
│   │   │               │   │   └── service/            # Domain Services
│   │   │               │   ├── reward/                 # Reward Domain
│   │   │               │   │   ├── aggregate/          # Aggregate Roots
│   │   │               │   │   ├── engine/             # Reward Rule Engine
│   │   │               │   │   ├── event/              # Domain Events
│   │   │               │   │   ├── repository/         # Repositories
│   │   │               │   │   ├── service/            # Domain Services
│   │   │               │   │   └── specification/      # Specifications
│   │   │               │   └── user/                   # User Domain
│   │   │               │       ├── aggregate/          # Aggregate Roots
│   │   │               │       ├── repository/         # Repositories
│   │   │               │       └── service/            # Domain Services
│   │   │               ├── global/                     # Global Components
│   │   │               │   ├── config/                 # Configuration Classes
│   │   │               │   ├── framework/              # Framework Components
│   │   │               │   │   ├── event/              # Event Framework
│   │   │               │   │   ├── exception/          # Exception Handling
│   │   │               │   │   └── security/           # Security Framework
│   │   │               │   └── infrastructure/         # Infrastructure Components
│   │   │               │       ├── ai/                 # AI Services
│   │   │               │       └── storage/            # Storage Services
│   │   │               ├── interfaces/                 # Application Layer
│   │   │               │   ├── api/                    # REST API Controllers
│   │   │               │   ├── dto/                    # Data Transfer Objects
│   │   │               │   └── mapper/                 # DTO-Entity Mappers
│   │   │               └── application/                # Application Services
│   │   │                   ├── carbonfootprint/        # Carbon Footprint Application Services
│   │   │                   ├── reward/                 # Reward Application Services
│   │   │                   └── user/                   # User Application Services
│   │   └── resources/
│   │       ├── application.yml                         # Main Configuration
│   │       ├── rules/                                  # Drools Rules
│   │       │   └── reward-rules.drl                    # Reward Calculation Rules
│   │       ├── db/                                     # Database Migration Scripts
│   │       │   └── migration/                          # Flyway Migrations
│   │       └── static/                                 # Static Resources
│   └── test/
│       ├── java/                                       # Test Classes
│       │   └── com/
│       │       └── example/
│       │           └── lettuce/
│       │               ├── domain/                     # Domain Tests
│       │               ├── interfaces/                 # Interface Tests
│       │               └── application/                # Application Tests
│       └── resources/                                  # Test Resources
├── build.gradle                                        # Gradle Build Configuration
├── settings.gradle                                     # Gradle Settings
├── README.md                                           # Project Documentation
├── LICENSE                                             # MIT License
└── .gitignore                                          # Git Ignore Configuration
```

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        Presentation Layer                        │
│                                                                 │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  │
│  │  REST API       │  │  DTO            │  │  Mappers        │  │
│  │  Controllers    │  │                 │  │                 │  │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        Application Layer                         │
│                                                                 │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  │
│  │  Carbon         │  │  Reward         │  │  User           │  │
│  │  Footprint      │  │  Application    │  │  Application    │  │
│  │  Application    │  │  Services       │  │  Services       │  │
│  │  Services       │  │                 │  │                 │  │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                          Domain Layer                            │
│                                                                 │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  │
│  │  Carbon         │  │  Reward         │  │  User           │  │
│  │  Footprint      │  │  Domain         │  │  Domain         │  │
│  │  Domain         │  │                 │  │                 │  │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘  │
│                                                                 │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  │
│  │  Aggregates     │  │  Domain Events  │  │  Specifications │  │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Infrastructure Layer                        │
│                                                                 │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  │
│  │  Event Store    │  │  Rule Engine    │  │  Repositories   │  │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘  │
│                                                                 │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  │
│  │  Disruptor      │  │  OpenCV         │  │  Columnar       │  │
│  │  Event Queue    │  │  Image          │  │  Storage        │  │
│  │                 │  │  Processing     │  │  Service        │  │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Key Components

### Domain Layer

1. **Carbon Footprint Domain**
   - `CarbonFootprint` (Aggregate Root): Tracks carbon footprint calculations
   - `FootprintCalculatedEvent`: Published when a carbon footprint is calculated
   - `ImageProcessingService`: Processes images for carbon footprint calculation

2. **Reward Domain**
   - `RewardWallet` (Aggregate Root): Manages user rewards and transactions
   - `RewardTransaction`: Records reward transactions
   - `RewardGrantedEvent`: Published when a reward is granted to a user
   - `RewardRuleEngine`: Calculates rewards based on configurable rules
   - `EligibleForRewardSpec`: Determines if a carbon footprint is eligible for rewards
   - `CarbonNeutralSpec`: Determines if a user has achieved carbon neutrality

3. **User Domain**
   - `User` (Aggregate Root): Manages user profile and authentication

### Infrastructure Layer

1. **Event Framework**
   - `JpaEventStore`: Stores domain events in a database
   - `DisruptorEventQueue`: High-performance inter-thread messaging
   - `EventEntity`: JPA entity for storing events

2. **Storage Services**
   - `ColumnarStorageService`: Efficient storage and querying of large datasets
   - `CarbonFootprintAnalytics`: Analytics for carbon footprint data

3. **Configuration**
   - `OpenCVConfig`: Configuration for OpenCV image processing
   - `DroolsConfig`: Configuration for Drools rule engine
   - `DisruptorConfig`: Configuration for Disruptor event queue
   - `HadoopConfig`: Configuration for Hadoop and Parquet

## Data Flow

1. **Carbon Footprint Calculation Flow**
   ```
   User uploads image → ImageProcessingService preprocesses image → 
   AI analyzes image → CarbonFootprint created → FootprintCalculatedEvent published → 
   DisruptorEventQueue processes event → RewardRuleEngine calculates reward → 
   RewardWallet updated → RewardGrantedEvent published
   ```

2. **Analytics Flow**
   ```
   User requests analytics → CarbonFootprint data retrieved → 
   ColumnarStorageService processes data → CarbonFootprintAnalytics generated → 
   Results returned to user
   ```

## Performance Optimizations

1. **Lock-Free Queue Design**
   - Disruptor Pattern for high-performance inter-thread messaging
   - Capable of processing 150,000 events per second

2. **Columnar Storage**
   - Apache Parquet + S3 Select for efficient storage and querying
   - 70% faster analytics compared to row-based storage

3. **JVM Tuning**
   - ZGC for low-latency garbage collection
   - Maximum 10ms pause times 