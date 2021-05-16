# Pet Finder
A Java application that is built to help pet owners find their lost companions, using the following technologies:
* Java 15
* Gradle (as build tool)
* JavaFx (as GUI)
* Nitrite Java (as database)
## Register
To sign up, users need to list their username, a valid password, their phone number and role (Individual or Shelter)
If they want, they can complete additional information such as their address and profile picture.
## Individual user
An individual is able to search through announcements, send and receive requests, as well as create lost, found and adoption announcements.
## Shelter user
A shelter is able to add pets to their pet list, and adoption announcements will be automatically created for each of them. They are also able to receive adoption requests.
## Prerequisites
To be able to install and run this project, please make sure you have installed Java 11 or higher. Otherwise, the setup will note work! To check your java version, please run java -version in the command line.

To install a newer version of Java, you can go to Oracle or OpenJDK.

It would be good if you also installed Gradle to your system. To check if you have Gradle installed run gradle -version.

If you need to install any of them, please refer to the official Gradle docs.

Make sure you install JavaFX SDK on your machine, using the instructions provided in the Official Documentation. Make sure to export the PATH_TO_FX environment variable, or to replace it in every command you will find in this documentation from now on, with the path/to/javafx-sdk-15.0.1/lib.

Note: you can download version 15 of the javafx-sdk, by replacing in the download link for version 16 the 16 with 15.
## Setup & Run
**Clone the repository using:**

git clone https://github.com/fis2021/pet-finder

Verify that the project Builds locally

Open a command line session and cd pet-finder. If you have installed all the prerequisites, you should be able to run any of the following commands:


gradle clean build

If you prefer to run using the wrappers, you could also build the project using


./gradlew clean build (for Linux or MacOS)

or

gradlew.bat clean build (for Windows)

**Open in IntelliJ IDEA**

To open the project in IntelliJ idea, you have to import it as a Gradle project. After you import it, in order to be able to run it, you need to set up your IDE according to the official documentation. Please read the section for Non-Modular Projects from IDE. If you managed to follow all the steps from the tutorial, you should also be able to start the application by pressing the run key to the left of the Main class.


**Run the project with Gradle**

The project has already been setup for Gradle according to the above link. To start and run the project use one of the following commands:


gradle run or ./gradlew run (to start the run task of the org.openjfx.javafxplugin plugin)

To understand better how to set up a project using JavaFX 11+ and Gradle, please check the official OpenJFX documentation.


