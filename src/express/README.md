## Setup Instructions

1. Create a new `.env` file in the root directory of your project.
2. Add the following line to the `.env` file:

    ```properties
    DEPENDENCY_SERVICE_URL=http://localhost:8080/copilotGHASExtension
    ```
Note: This url corresponds to the url of the SringBoot application which is being hosted as a microservice
    
3. Runb the application using the below command:
   ```
   npm i dotenv
   npm extension.js
   ```
4. The url of this node application is to be configured in the Copilot Settings under your GitHub App settings

