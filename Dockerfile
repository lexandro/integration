FROM java:8-jdk

MAINTAINER Robert Stern <lexandro2000 gmail com>

RUN mkdir /app

COPY app/target/imaginarium.jar /app/imaginarium.jar

EXPOSE 8080

WORKDIR /app