FROM java:8-jdk

MAINTAINER Robert Stern <lexandro2000 gmail com>

RUN mkdir /app

WORKDIR /app

EXPOSE 8080

COPY app/target/imaginarium.jar /app/imaginarium.jar



