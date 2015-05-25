# AppDirect integration PoC

## Intro

Sample app to demonstrate AppDirect integration by Robert Stern based on Java 8, MongoDB and Spring Boot.


## How to build
Prerequisites: The application needs to be built with Maven 3.0.4++, Java 8 and uses Lombok to generate setter/getters etc. Please have a look on https://projectlombok.org/ to see how is your IDE supported.  

To build:
    $ mvn clean package

Building the Docker image
    cd app
    docker build -t <your repo>/imaginarium .
     
## Deploy/run

Java app is available on localhost:8080 and the passed security settings is mandatory for some virtualized environment, otherwise the startup takes 30 mins o.O 
    java -DMONGO_URI=<mongodb connection string> -Djava.security.egd=file:/dev/urandom -jar imaginarium.jar


Docker image exposes the app on port 80 and you just need to pull it from the public hub:
    docker pull lexandro/imaginarium // optional
    docker run -dt --name 'imaginarium' -e MONGO_URI=<mongodb connection string> -p 80:8080 lexandro/imaginarium java -Djava.security.egd=file:/dev/urandom -jar imaginarium.jar
     

## Delivery pipeline
The app has a fully automated build pipeline on: http://ci.lexandro.com/

At the momnent the sonar step is demonstrational to show how the automated code quality check could be integrated into the process.






## Running the app
===============
Normal run

Docker run

