# 🚀 Kafka Listeners Java

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE) ![GitHub stars](https://img.shields.io/github/stars/sameer-m-dev/kafka-listeners-java) ![GitHub forks](https://img.shields.io/github/forks/sameer-m-dev/kafka-listeners-java)


## Overview

Kafka Listeners is a Java application demonstrating basic interactions with Apache Kafka. It includes functionalities to produce and consume messages from a Kafka topic while demonstrating error handling and configuration setup. It can be used as a starting point to debug Kafka connectivity issues or as a reference for implementing Kafka clients in Java.

## 🔧 Features

- The application connects to the specified Kafka broker and performs the following:
  - Lists the brokers connected to the bootstrap server.
  - Produces a message to a Kafka topic.
  - Consumes messages from the same topic.
- Error handling mechanisms and robust configurations
- Maven-based project for easy dependency management and building

## 🛠️ Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Kafka installed and running
- Maven (for dependency management and building)

## Installing Java and Maven on Linux and macOS
- **Java Development Kit (JDK)**
    - **Linux:** On most Linux distributions, you can install OpenJDK using the package manager:
        - **Debian/Ubuntu:**
            ```bash
            sudo apt update
            sudo apt install default-jdk
            ```
        - **CentOS/Fedora:**
            ```bash
            sudo yum install java-1.8.0-openjdk-devel
            ```
        - Verify the installation using `java -version`.

    - **macOS:** On macOS, you can install Java using Homebrew or by downloading from Oracle:
        - Using Homebrew:
            ```bash
            brew install --cask adoptopenjdk
            ```
        - Download from Oracle:
            Visit the [Java SE Downloads](https://www.oracle.com/java/technologies/javase-downloads.html) page and follow the installation instructions.

- **Maven**

    - **Linux:** You can install Maven via the package manager or manually.
        - **Debian/Ubuntu:**
            ```bash
            sudo apt update
            sudo apt install maven
            ```
        - **CentOS/Fedora:**
            ```bash
            sudo yum install maven
            ```
        - **Manual installation (Linux and macOS):**
            - Download the latest Maven binary distribution from the [Maven official website](https://maven.apache.org/download.cgi).
            - Extract the archive and set the `M2_HOME` environment variable to the extracted directory.
            - Add Maven's `bin` directory to the `PATH` variable.

    - **macOS:** You can use Homebrew to install Maven on macOS:

        ```bash
        brew install maven
        ```

## Setup
1. **Clone the Repository**
    ```bash
    git clone https://github.com/sameer-m-dev/kafka-listeners-java.git
    cd kafka-listeners-java
    ```

2. **Dependency Management**

    Ensure the `pom.xml` file includes dependencies for Kafka clients and SLF4J logging as well as Kafka broker address if you wish to use the exec plugin to run the application.
    
    Else you can pass the Kafka broker address as an argument when running the application after creating a JAR file in step 5 below.

3. **Building**

    ```bash
    mvn clean install
    ```

4. **Compile and Run**

    This step is optional and can be used to compile and run the application using the exec plugin. Ensure the `pom.xml` file includes the exec plugin configuration.

    ```bash
    mvn clean compile && mvn exec:java -Dexec.mainClass="com.example.kafka.KafkaTestClient"
    ```

6. **Creating a JAR File**
    Run the following command to create a JAR file in the `target` directory:

    ```bash
    mvn clean package
    ```

## Running the Application

1. After building the project, navigate to the directory containing the compiled `.jar` file.
2. Execute the following command to run the KafkaTestClient application, passing the Kafka broker address as an argument:

    ```bash
    java -jar kafka-listeners-java-1.0.jar localhost:9092
    ```

If the application runs successfully, you should see the following output:

```bash
🥾 Bootstrap server: localhost:9002

✅ Connected to bootstrap server(localhost:9002) and it returned metadata for brokers listed below:

👉 Broker ID: 1, Host: broker1.jmpx2.de, Port: 9001
👉 Broker ID: 2, Host: broker2.jmpx2.de, Port: 9002
👉 Broker ID: 3, Host: broker3.jmpx2.de, Port: 9003

---------------------
ℹ️  This step confirms the successful bootstrap connection and provides broker metadata required for consumer resolution.
ℹ️  Ensure your client can resolve the broker(s) shown in the metadata above.
ℹ️  If the listed host(s) are inaccessible from your client, consider adjusting the advertised.listener configuration on Kafka broker(s).


<Producing>
✅  📬  Message delivered: "test_topic-0@1" to test_topic [partition 0]

<Consuming>
✅  💌  Message received: "foo / 2023-12-21T21:20:52.705" from topic test_topic
```

## Additional Notes

- Adjust the Kafka broker address, topic names, and other configurations as needed.
- Ensure your Kafka environment is properly set up and running before executing the application.
