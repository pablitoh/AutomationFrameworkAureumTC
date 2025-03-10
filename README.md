## Prerequisites
- **Java**: Ensure that Java 8 or above is installed on your machine.
- **Gradle**: Verify that Gradle 6.6 or above is installed.
- **Allure**: Install Allure for generating test reports. Installation [instructions](https://docs.qameta.io/allure/#_installing_a_commandline) are available here.

## HOW TO RUN THE TESTS:

Test can be run locally by running the command, this will grab the properties in the config.properties global file.
```bash
./gradlew test
```
And then to see the report

```bash
allure serve build/allure-results
```

## TEST PARAMETRIZATION : 
Environment, Browser , Tags from the CLI , Ideally for CI implementations on demand. 


## SELECT ENVIRONMENT (default Dev, Options: dev/qa/staging):

```bash
 ./gradlew test -Denv=qa
```

## SELECT BROWSER (chrome/firefox):
```bash
./gradlew test -Dbrowser=firefox
```

## HEADLESS MODE (true/false):
```bash
./gradlew test -Dheadless=true
```

## SELECT TESTS By TAG:
Allows to create Test Suites on the go by combining or excluding tags, for example Smoke Test vs Regression test. Or combine features for a sanity check.

```bash
./gradlew test -PexcludeTags=Api
or
./gradlew test -PincludeTags=Login,Api
```

Tests can also select whether if the browser will be run locally or remote in a Selenium Grid :

```bash
./gradlew test -Dbrowser=firefox -Dwebdriver.remote=true -Dgrid.url=http://localhost:4444
```

## PROPERTY FILES & FIXTURES (Test Data):
All the data is chosen depending on the environment variable passed in cli.

If something is setup locally in the test data folder for the environment thaty will gain priority over the global properties of the config.properties file.

- CLI commands override everything!!!


## Docker Instructions:
Project contains a docker-compose.yml with the images to create a Selenium Grid where the test may be run. This could be setup in a AWS EC2 container, and the CI will create this container, run the test and then delete it. That will guarantee the execution on isolation of the test each time.

To setup the docker container with 5 instances of each browser the grid just run ( You need to have docker installed )
```bash
docker-compose up -d --scale chromium-node=5 --scale firefox-node=5
```

the default url is going http://localhost:4444 and you should use that as -Dgrid.url variable.


