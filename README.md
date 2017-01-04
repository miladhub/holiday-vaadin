Holiday Planner with Vaadin
==============

Simple holiday planner using [Vaadin](https://vaadin.com/).

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

Workflow
========

To compile the entire project, run

    mvn install

To run the application, run

    mvn jetty:run
    
or

    java -jar target/dependency/webapp-runner.jar target/*.war
    
and open [http://localhost:8080/](http://localhost:8080/)

This app can also be deployed in any Servlet 3.0-compliant container, including [Tomcat](http://tomcat.apache.org/).

Use cases
=========

## Create plan

Pre-conditions: none
Post-conditions: system keeps record of the plan

1. Actor sets hours/vacation gained per month

2. Actor sets actual hours/vacations at the beginning of a given month

3. Actor chooses an end date

4. System shows hours/vacations in the future, until end date

## Add or remove vacations

Pre-conditions: the plan is available and the system is presenting it to the user
Post-conditions: the plan is recorded along with the changes

1. Actor adds or removes hours off/vacations from a given month

2. System shows changed hours/vacations in the following months, until given date
 
Outcome: the plan has changed with additional hours off/vacations for the given month
