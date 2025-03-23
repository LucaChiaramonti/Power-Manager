# Power Manager

Power Manager is a Spring Boot application that manages powers and their associated classes and levels. It retrieves power data from the "https://www.psionics.info/" website and processes it to store in a local database.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Setup](#setup)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)

## Features

- Retrieve power data from an external API
- Process and store power data in a local database
- Manage classes and levels associated with powers
- Retry mechanism for API calls
- Timeout handling for API requests

## Technologies

- Java
- Spring Boot
- Maven
- RestTemplate
- JPA/Hibernate
- Lombok

## Setup

1. Clone the repository:

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## Usage

The application exposes REST endpoints to interact with the power data. You can use tools like `curl` or Postman to make requests to these endpoints.

## Endpoints

- **Import Powers**
    - **URL:** `/api/power`
    - **Method:** `POST`
    - **Description:** Imports powers from the external API and stores them in the database.

- **Get Powers by Description**
    - **URL:** `/api/power/description={description}`
    - **Method:** `GET`
    - **Description:** Retrieves powers that match the given description.

- **Get Powers by Name**
    - **URL:** `/api/power/name={name}`
    - **Method:** `GET`
    - **Description:** Retrieves powers that match the given name.

## Configuration

The application uses a `RestTemplate` bean configured with timeouts. You can customize the timeouts in the `RestTemplateConfig` class.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

1. Fork the repository
2. Create a new branch (`git checkout -b feature-branch`)
3. Make your changes
4. Commit your changes (`git commit -am 'Add new feature'`)
5. Push to the branch (`git push origin feature-branch`)
6. Create a new Pull Request

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
