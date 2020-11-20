![fritz2](https://www.fritz2.dev/images/fritz2_logo_grey.png)

# Spring TodoMVC

This project is an example for using [fritz2](https://www.fritz2.dev/) with in a spring boot-web application.
It will demonstrate how-to interact with a backend application in fritz2 and how-to setup this kind of project.

This project consists of two sub-projects "app" and "backend". 
The "app" project is a kotlin multi platform project which contains the client-site model and application. 
The "backend" project is the spring boot 2 web-application and contains all the server-side code.
For using some shared parts of the "app" the "backend" has a dependence to the `commonMain` section of the "app" project.

This project uses the following libraries:
* [fritz2](https://github.com/jwstegemann/fritz2) - mainly in "app", except validation and model in both sides
* [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - currently, only in "app" because there is no official support for spring web yet
* [Spring Framework](https://start.spring.io/) - by using Spring Boot, Spring Web, Spring Data JPA in "backend"
* [H2 Database](https://www.h2database.com/html/main.html) - running as in-memory-db which stores the data

# Current status
What is currently there:
* running full-stack-example based on the [TodoMVC](http://todomvc.com/)
* working validation provided by fritz2 extension on client- and server-side
* complete gradle configuration
* some tests

# Run
To run this application you only need to run the following gradle task:
```
> gradlew bootRun
``` 
Then navigate in your browser to [localhost:8080](http://localhost:8080/)

# Build
You can build a runnable fatJar by running the `bootJar` gradle task. 
If you instead want to deploy this application to a Java web server you
can run the gradle `bootWar` task to get a war file.

Both files are then located at the `./backend/build/libs` folder.

# Contribution
If you like this example and how fritz2 works it would be great 
when you give us a &#11088; at our [fritz2 github page](https://github.com/jwstegemann/fritz2).

When you find some bugs or improvements please let us know by reporting an issue 
[here](https://github.com/jamowei/fritz2-spring-todomvc/issues).
Also, feel free to use this project as template for your own applications.