<h1>Why Juke?</h1>

Modern UI Applications have become complex to the point that logic is spread across the UI and server side layers.
Unit testing frameworks are not designed to test both the UI and server layers together and this creates a gap
in the testing process. Furthermore, writing unit tests is time-consuming and requires a great deal of code to be written and, regrettably, tested.
This complexity creates a productivity trap that can slow down the development process and sap efficiency. The developers
of Juke believe that you shouldn't be writing code to test code. 

In an ideal word, we would like to:
1) Simplify automated test creation to the point where a developer can run a UI browser in recording mode and test a functional story by clicking and typing through a Use Case scenario.
2) Test the UI and server layers together in a single test from the browser.
3) Isolate our code from upstream systems such as databases and other services to be persistent and available and provide realistic
data relevant to the test. 
4) Provide a consistent state for an automated UI testing framework to find and to test the UI layer all the way down to the server side every time we run the test.
5) Allow us to test exception and error flows in the use case to validate alternate flow scenarios that are not easily replicated in a normal behavior testing environment. 

In short, we want a framework that can function as both as a unit testing provides and a behavioral testing framework while minimizing or avoiding the writing and testing of additional code to support it.
We would like a testing framework that can let us validate the full Use Case of the application without a complex build up and tear down process.

<h2>Juke is built to be fit for purpose! </h2>

Juke is a server side library that records upstream interactions via a proxy wrapper interfaces and replays the recordings back
like tracks on a jukebox. Switching recordings is as easy as changing the track using a url REST endpoint.
This means that your upstream data providers can be isolated out and mocked and replayed back to the application. The data, however, data does not have to be manually mocked out because it is real data from a real upstream interaction. Where the
data you need is not easily available,  you can inject mock data into the interactions to simulate the behavior
you are expecting and handling in your code.

Additionally, Juke provides REST API's to generate unit testing like exception flows. The <b>Remix</b> rest services
allow you to modify the behavior of the application interactions by injecting exception or  time delays simulating
failures and slow responses to validate that your application can recover from problem scenarios.
This means you can test the full Use Case and not just the happy day flows in a behavioral test and support the non-sequential multi-threaded UI browser behavior flows.

Run your application and let Juke record your browser/server interactions. Switch to playback mode and run your tests consistently every time.

What differentiates Juke from other testing frameworks such as Selenium, Playwright, and Cypress that provide recording
and playback facilities? 

Juke was designed to work with client side UI frameworks to create full end to end tests. UI testing frameworks are designed to test the UI layer exclusively. Some are able to record and playback server side data interaction; but only at the UI layer. Once recorded, fixes or changes to the server side code will be ignored. Juke tests the ui layer and server side together and only mocks out the upstream layer by abstracting out interfaces to the upstream data access.
Juke also provides additional rest endpoints to modify the behavior of the application to simulate different tests
and behaviors. Juke plays nice with UI testing frameworks to let you create and record ui tests to build out your automated behavioral test suite.

<h2>What's the big deal?</h2>
Unit Test writing and validation is expensive. It requires developer resources and time to write and maintain.
A good set of tests can easily take up as much time to build out as it takes to write the code that is being tested.
Juke is designed to reduce the amount of time and resources required to build tests by skipping or truncating the unit test code writing process.
Just run the application and let Juke record your interaction. You can then let Selenium, Playwright, or
Cypress run automated tests from the recording giving you more time to focus on writing code and less time writing
tests.

<h2>Running Juke in Server Side:</h2>

To run In recording Mode: juke-sample-rest-service
This example was built to work with the juke-sample-ui-service: a react ui application that uses the juke-sample-rest-service as a backend.

Instructions on how to run the Sample Rest Service with Juke in recording mode
Open as a Maven project and run maven install under the Lifecycle on the Juke Test Harness (In intellij, maven tab is on the right menu. Double click to start install/build)
VM ARGUMENTS:
Add the following to application VM args for SampleRestApplication (if they are not set) and  start the application with the arguments:
-Djuke=record -Djuke.path=C:/temp  -Djuke.zip=jukebox -Djuke.tests=jukebox
This will start the application in recording Mode and will beginning recording the tests in memory. 

1) Open a terminal (in intellij its at the bottom of the editor) and go to <Installation Path>\juke\juke-sample-ui-service\src\web-app where Installation path is your
github juke package installation location (e.g. C:\github)

In the terminal, enter the commands sequentially:
2) npm install
3) npm run start

This will build and then automatically open a browser in the url http://localhost:3000
Note: The underylying SampleRestApplication is now running on http://localhost:8080. The npm web server proxies the request from localhost:3000 web server.

Your web browser will show a very simple UI with an input parameter to add a name.
4) Type in a name and submit. Repeat as often as you like.
5) When you are done in the url type http://localhost:8080/service/juke/end
This will close the recording session and download a zip file named jukebox.zip into your downloads folder in your browser.
You can open up the zip file to see the recordings. In this example they will be of the form: com.example.IGreetingsService.$greeting.\<sequence>.json
where the sequence goes from 1 to however many times you entered a name and git submit.
There is also a juke.json file that helps juke build the object structure of the data to recreate complex objects in playback mode.

6) Move the jukebox.zip file to the -Djuke.tsts folder. In this example, use the C:\temp folder

Change the VM arguments on Sample Rest Service to 
7) -Djuke=replay -Djuke.path=C:/temp  -Djuke.zip=jukebox -Djuke.tests=jukebox

8) Restart the Sample Rest Service

9) Next, refresh the web browser. Type in some other test and hit submit. The response will ignore your input and happily replay the same interaction responses from the recording.

<h3>Why would that be useful?</h3>
The purpose of Juke is to help an automated UI test to get the data it is expecting from upstream while validating that changes are not breaking the rest of your code. 
You may not personally always do things the same way and can compensate for that, but automated tests aren't very good at that.
The upstream data should not change. 
However, your code updates may break that way the server or the ui handle that expected data and highlight and focus that failure.



Arguments:
The VM Argument-Djuke has three possible arguments [juke,replay,ignore]
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