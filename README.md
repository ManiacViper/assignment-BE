# ICE Take-home assignment - Senior Fullstack Engineer – Finance - Backend

Imagine you work for a company that offers an intermediary service and you are part of the team responsible for
calculating commissions for clients.

- You will receive a request that contains a non empty list of services we provide
- Each element in the list will contain
  - an identifier, a positive integer
  - total amount of money, that cannot be greater than 1,000,000
- Each request is associated with a specific client
- Our company will charge a percentage of the total amount of each element from the list
- In order to calculate the rate associated with a specific element on the list we will use the amount.

For example:

Given this commission
| amount              | rate       |
|---------------------|------------|
| 0 - 1,000           |   10 %     |
| 1,000 - 3,000       |    5%      |
| 3,000 - 1,000,000   |    1%      |

and this request:

- (1) 900
- (2) 2000
- (3) 4000

the commissions will be:

- (1) 90
- (2) 100
- (3) 40

## Building and Running the Application

This project is built using **SBT (Scala Build Tool)** and targets the Scala ecosystem. It leverages functional architectures via **Cats Effect 3** and **FS2 (Functional Streams for Scala)** for handling non-blocking file I/O operations.

### Prerequisites

Before building or running the application, ensure you have the following installed on your system:

* **JDK:** Version 21 .
* **Scala:** Version 2.13.12.
* **SBT:** Version 1.9.6.

Verify your local installation context using your terminal:
```bash
java -version
sbt --version
```

### Clean, Compile, and Test Workspace
# Remove cached artifacts and target directories
```bash
sbt clean
```

# Compile application source code
```bash
sbt compile
```

# Run the test suite (e.g., StreamingAppSpec)
```bash
sbt test
```

# Run with default
```bash
sbt run
```
which will produce `calculated.csv` in the root of the application with the service ids and the associated calculated commissions 