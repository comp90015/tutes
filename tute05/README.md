# Challenge 05

This week we will take a detour to focus on project 1.


### multi-jar builds

When we have been running our code so far, it has been with class files and with the help of an IDE.

For the project, you will need to submit two JAR files along with your source code.

The difference here is that these JAR files must contain all the dependencies to execute your code!

What if you want to have some shared code between the client and server? How can this be done in a clean and professional manner?

Luckily there are two great build tools in heavy use within the java ecosystem.

**Gradle** and **Maven**.

We have been using Maven so far for quickly configuring a new project, however it has plenty of additional functionality.

For this challenge, we will setup a gradle project that has three sub-projects. 

```sh
client/ # client src code
server/ # server src code
base/   # shared code (types/serialization)
```

##### Gradle

Gradle is a build and dependency manager. It is much less verbose in comparison to Maven.

If you look at the sample structure:

```
├── base
│   ├── build.gradle # deps + build target for base
│   └── src          # your shared code goes here
├── client
│   ├── build.gradle # deps + build target for client
│   └── src          # your client specific code here
├── gradle 
│   └── wrapper      
├── gradlew          # gradle build script for mac/unix
├── gradlew.bat      # gradle build script for windows
├── README.md 
├── server
│   ├── build.gradle # deps + build target for server
│   └── src          # your server specific code here
└── settings.gradle  # declares the project structure
```

In `server/build.gradle` `client/build.gradle` you will see that they each have dependencies on

```groovy
implementation project(':base')
```

Meaning, we want the "base" project to be available to our code in these separate projects.

This also includes any transitive dependencies that base has, we will also include.


### JSON Parsing

There is no default implementation of a JSON serializer and deserializer in the JDK.

To add this functionality, we can include a library.

The two most popular are

[jackson](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core)
[gson](https://mvnrepository.com/artifact/com.google.code.gson/gson)


Similar to last weeks challenge, the role of these libraries is to serialize and desrialize code to and from byte buffers.


You can see the annotations and tests that do so in base!

### What do you need to do?

Play around with the annotations and other methods.

Decide what is right for your or even upgrade the current setup you have!
