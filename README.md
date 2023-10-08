#Why Juke?

Modern UI Applications have become complex to the point that logic is spread across the UI and server side layers.
Unit testing frameworks are not designed to test both the UI and server layers together and this creates a gap
in the testing process. Writing unit test is time-consuming and requires a lot of code to be written and also tested.
This process creates a productivity trap that can slow down the development process and sap efficiency. The developers
of Juke don't believe in writing code to test code.

We would like automated test creation to be as simple as running
a UI browser in recording mode and testing the Use Case story by clicking and typing through the scenario.
Ideally, we would like to test the UI and server layers together in a single test from the browser.
We would like upstream systems such as databases and other services to always be available and providing realistic
data relevant to the test. We would like the data to be consistent enough for an automated UI testing framework to
find and to test the UI layer all the way down to the server side every time we run the test.
We would like to validate exception handling and alternate flow scenarios that are not easily replicated in a normal
behavior testing environment. In short, we want something that can do what Unit Testing Frameworks provide but also
work as a behavioral and integration testing framework without writing and testing any additional code.
We want a test harness that can let us validate the full Use Case of the application without a complex setup.

Juke is built to do just that!

Juke records upstream interactions via a proxy wrapper interfaces and replays the recordings back
like tracks on a jukebox. Switching recordings is as easy as changing the track using a rest endpoint.
This means that your upstream data providers can be isolated out and mocked and replayed back to the application.
But the data does not have to be mocked. Because, its real data from a previous interaction. However, if the
data you need is not available then you can inject mock data into the interactions to simulate the behavior
you are expecting and handling in your code.

Additionally, Juke provides rest API's to generate unit testing-like exception flows. The Remix rest services
allow you to modify the behavior of the application interactions by injecting exception or  time delays simulating
failures and slow responses to validate that your application can properly recover from problem scenarios.
This means you can test the full Use Case and not just the happy day flows in a behavioral test.

Throw away your unit testing frameworks and stop writing test code. Run your application and let Juke record your
browser interactions. Switch to playback mode and run your tests consistently every time you run it.

What differentiates Juke from other testing frameworks such as Selenium, Playwright, and Cypress that provide recording
and playback facilities?

Other UI testing frameworks are designed to test the UI layer only. Some are able to record and playback server side
data interaction; but only at the UI layer. Once recorded, fixes or changes to the server side code will be ignored.
Juke tests the ui layer and server side together and only mocks out the upstream layer by abstracting out interfaces to the upstream data access.
Juke also provides additional rest endpoints to modify the behavior of the application to simulate different tests
and behaviors. In fact, Juke plays nice with  UI testing frameworks to let you create and record ui tests
to build out your automated behavioral test suite.

What's the big deal?
Unit Test writing and validation is expensive. It requires developer resources and time to write and maintain.
A good set of tests can easily take up as much time to build out as it takes to write the code that is being tested.
Juke is designed to reduce the amount of time and resources required to build tests by skipping the unit test code writing process.
Just run the application and let Juke record your interaction. You can then let Selenium, Playwright, or
Cypress run automated tests from the recording giving you more time to focus on writing code and less time writing
tests.

Running Juke in Server Side:

To run In recording Mode: juke-sample-rest-service
This example was built to  work with the juke-sample-ui-service: a react ui application that uses the juke-sample-rest-service as a backend.

Instructions on how to run the Sample Rest Service with Juke in recording mode
VM ARGUMENTS:
Add the following to application VM args:
-Djuke=record -Djuke.path=C:/temp  -Djuke.zip=jukebox -Djuke.tests=jukebox

This will start the application in recording Mode and will record the tests in memory. The juke.zip,juke.path, and juke.tests parameters are only used in playback mode

-Djuke has three possible arguments [juke,replay,ignore]
record - will run the application in recording mode
replay - will run the application in playback mode
ignore - will run the application in normal mode (default)

-Djuke.path=<dir path> is the path to the directory where the zip file will be loaded from. This can be the same directory
as java.io.tmpdir or a different directory. In the example above its in the windows c:/temp folder

-Djuke.zip is the name of the zip file that will be loaded from the juke.path directory at startup.
In the above example, the zip file is jukebox.zip. Copying the file from java.io.tmpdir and renaming is a good way to create a new zip file.

-Djuke.tests is a white-listing facility to help the application ensure that users will access only the tests that are explicitly defined
are allowed to be run. This is a security feature to prevent illegal access to secure file system resources.


To run In playback Mode:
Change the vm arguments for juke to replay. Include as many run as arguments that you want to run in playback mode as juke.tests
-Djuke=replay -Djuke.path=C:/temp  -Djuke.zip=jukebox -Djuke.tests=jukebox,jukeboxSampleZipName


Spring Boot Application Arguments: BasicApplication.java
Include the following into component scan. This allows the application to find the juke framework components and rest endpoints
@ComponentScan(basePackages =  {"org.juke.remix", "org.juke.framework","com.example"})
Service End Points:
Switching between playbacks is done by calling the following endpoint by adding the track zip file name as a parameter:
http://localhost:8080/service/record?track=jukeboxSampleZipName

Ends the recording and downloads to browser the zip file containing the tests in the Downloads folder.
http://localhost:8080/service/record/end

Switches and resets the playback to the track name specified in the parameter
http://localhost:8080/service/replay?track=jukeboxSampleZipName

Remix Rest API's:
remix exceptionSchedule - injects an exception into the application interaction. In the case below the first call to interface IGreetingService.greeting will throw an IOException

http://localhost:8080/service/remix/exceptionSchedule?classAndMethodSequence=com.example.IGreetingsService.$greeting.1&exception=IOException&exceptionMessage=IOException
remix delaySchedule - injects an delay into the application interaction. In the case below the first call to interface IGreetingService.greeting will will wait 10000 milliseconds before returning

http://localhost:8080/service/remix/delaySchedule?classAndMethodSequence=com.example.IGreetingsService.$greeting.1&waitTimeInMS=10000