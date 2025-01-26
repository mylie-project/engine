# Mylie - Engine
#### Build State:

[![GitHub License](https://img.shields.io/github/license/mylie-project/engine?lable=license)](./LICENSE.md)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/mylie-project/engine/build_test.yml)
![GitHub last commit](https://img.shields.io/github/last-commit/mylie-project/engine)
![GitHub commit activity](https://img.shields.io/github/commit-activity/w/mylie-project/engine)
![GitHub repo size](https://img.shields.io/github/repo-size/mylie-project/engine)

#### Code Quality:

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mylie-project_engine&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mylie-project_engine)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=mylie-project_engine&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=mylie-project_engine)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=mylie-project_engine&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=mylie-project_engine)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=mylie-project_engine&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=mylie-project_engine)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=mylie-project_engine&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=mylie-project_engine)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=mylie-project_engine&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=mylie-project_engine)

## How to build:

Follow the steps below to download the repository, build, and run the project using Gradle:

### Step 1: Prerequisites

Before proceeding, ensure you have the following installed on your system:

- **Java Development Kit (JDK)** (version 8 or later)
- **Git**
- **Gradle** (optional, as the project includes a Gradle wrapper)

### Step 2: Clone the Repository

1. Open a terminal or command prompt.
2. Run the following command to clone the repository:
   ```bash
   git clone https://github.com/mylie-project/engine.git
   ```
3. Navigate to the project directory:
   ```bash
   cd engine
   ```

### Step 3: Build the Project

1. Use the Gradle wrapper to build the project. Run the following command:
   ```bash
   ./gradlew build
   ```
   (On Windows, use `gradlew.bat build` instead of `./gradlew build`.)

2. The build process will compile the code, run tests, and package the application. You should see a `BUILD SUCCESSFUL`
   message if everything passes.

### Step 4: Run the Project

1. To execute the application, use the following command:
   ```bash
   ./gradlew run
   ```
   (On Windows, use `gradlew.bat run`.)

2. Gradle will start the application, and any output will be displayed in the terminal.

### Step 5: Clean the Build (Optional)

If you make changes or need to refresh the build, clean the project by running:

```bash
./gradlew clean
```

(On Windows, use `gradlew.bat clean`.)

That's it! You've successfully downloaded, built, and run the project.

## How to contribute:

We welcome contributions to the project! Follow the steps below to create a pull request and ensure your code adheres to
the project's formatting guidelines:

### Step 1: Fork the Repository

1. Navigate to the [GitHub repository](https://github.com/mylie-project/engine.git).
2. Click on the `Fork` button at the top-right corner of the page to create your own fork of the repository.

### Step 2: Clone Your Fork

1. Open a terminal or command prompt.
2. Run the following command to clone your forked repository:
   ```bash
   git clone https://github.com/your-username/engine.git
   ```
3. Navigate to the project directory:
   ```bash
   cd engine
   ```

### Step 3: Create a Branch

1. Create a new branch for your feature or bug fix:
   ```bash
   git checkout -b your-branch-name
   ```

### Step 4: Make Changes and Format the Code

1. Make the necessary changes to the code.
2. Before committing your changes, ensure that the code is formatted correctly by running the following command:
   ```bash
   ./gradlew spotlessApply
   ```
   (On Windows, use `gradlew.bat spotlessApply`.)

3. Verify the changes by checking the formatting with:
   ```bash
   ./gradlew spotlessCheck
   ```
   This ensures your code follows the required style guidelines.

### Step 5: Commit and Push Changes

1. Stage the changes:
   ```bash
   git add .
   ```
2. Commit your changes with a descriptive message:
   ```bash
   git commit -m "Description of your changes"
   ```
3. Push the branch to your fork:
   ```bash
   git push origin your-branch-name
   ```

### Step 6: Create a Pull Request

1. Go to your forked repository on GitHub.
2. Click on the `Compare & pull request` button.
3. Add a meaningful title and description for your pull request.
4. Submit the pull request.

### Additional Notes:

- Ensure that your changes pass all the checks (e.g., the build is successful, tests pass, and formatting is correct).
- Regularly sync your fork with the main repository to reduce conflicts:
  ```bash
  git fetch upstream
  git merge upstream/master
  ```

Thank you for contributing to the project!