HOW TO RUN THE TESTS:

Test can be run locally by running the command, this will grab the properties in the config.properties global file.

./gradlew test

This can be modified to run certain set of test or specify a browser

SELECT ENVIRONMENT (default Dev, Options: dev/qa/staging):

example: ./gradlew test -Denv=qa

SELECT BROWSER (chrome/firefox):

Example ./gradlew test -Dbrowser=firefox

SELECT TESTS By TAG:

./gradlew test -PexcludeTags=Api
or
./gradlew test -PincludeTags=Login,Api

Tests can also select whether if the browser will be run locally or remote in a Selenium Grid :

./gradlew test -Dbrowser=firefox -Dwebdriver.remote=true -Dgrid.url=http://localhost:4444

PROPERTY FILES & FIXTURES (Test Data):
All the data is chosen depending on the environment variable passed in cli

Docker Instructions:

To setup the docker container with 5 instances of each browser the grid just run ( You need to have docker installed )

docker-compose up -d --scale chromium-node=5 --scale firefox-node=5

the default url is going http://localhost:4444 and you should use that as -Dgrid.url variable.
