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

1) set actual hours/vacations at the beginning of a given month, hours/vacation gained per month
--> look at hours/vacations in the future, until given date

2) add hours off/vacations in a given month
--> look at changed hours/vacations in the following months, until given date 
-- prerequisite: actor has to know how much they added on the month (use case 1)

3) remove hours off/vacations from a given month
--> look at changed hours/vacations in the following months, until given date
-- prerequisite: actor has to know how much they added on the month (use case 1)
