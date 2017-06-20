# DataViz

DataViz is a Java web application that retrieves financial data from financial API sources and displays those data using ChartJs.

### The technologies used are:

  - [Java 8]
  - [JUnit 4] for testing the Java code
  - [Maven] as the build automation tool
  - [JavaScript]
  - [AngularJS] as a front-end web application framework
  - [Chart.js] as JavaScript charting library
  - [Jasmine] as a testing framework for JavaScript
  - [Karma 1.0] to run Jasmine tests on several browsers

Deployed on:

 - [Heroku] with a pipeline that serves the Reviewing, Staging and Production phases

Continous Integration:

  - [Codeship] for the backend




## How to create a web app and deploy it on Heroku?

### What do you need?

Make sure to have installed on your machine:

  - Maven
  - Git
  - Heroku CLI

### Create a Maven Web App:

Run this command to create the app and provide the project-packaging and project-name parameters

```sh
$ mvn archetype:generate -DgroupId={project-packaging}
			 -DartifactId={project-name} 
			 -DarchetypeArtifactId=maven-archetype-webapp 
			 -DinteractiveMode=false
```

 The project structure should look like this. If you don't see the java folder, you can create it manually later.

```
.
 |-- src
 |   `-- main
 |       `-- java
 |           |-- resources
 |           |-- webapp
 |           |   `-- WEB-INF
 |           |       `-- web.xml
 |           `-- index.jsp
  `-- pom.xml
```
  The Project Object Model `pom.xml` is an XML file that contains information about the project and configuration details used by Maven to build the project. [Learn more about pom.xml](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html)

#### Add The Webapp Runner Plugin
Edit the `pom.xml` in a text editor and add the following code in your plugins section.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
  ...
  <build>
    ...
    <plugins>
      ...
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <version>2.3</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>copy</goal>
            </goals>
            <configuration>
              <artifactItems>
                <artifactItem>
                  <groupId>com.github.jsimone</groupId>
                  <artifactId>webapp-runner</artifactId>
                  <version>8.0.30.2</version>
                  <destFileName>webapp-runner.jar</destFileName>
                </artifactItem>
              </artifactItems>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
```

PS: You may want to change the JUnit version for future tests. Just modify the version element to `<version>4.12</version>`.

### Create the Heroku Procfile:

The `Procfile` is a simple text file which contains the command that should be called by Heroku to launch your application. Learn more about Heroku Procfile [here](https://devcenter.heroku.com/articles/procfile).

Add this command in the `Procfile` using a text editor:

```
web: java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port $PORT target/*.war
```

### Make the project folder a Git repository and link it to GitHub:

To link the project to GitHub, we have to [create a new repository](https://help.github.com/articles/creating-a-new-repository/) online, then init the project folder as a Git repo:

``` sh
$ git init
$ git add .
$ git commit -m "Commit Message"
$ git remote add origin <github-repo-url>
$ git push origin master
```
To always push to `master` run this command
```sh
$ git push --set-upstream origin master
```

### Create Heroku Application
After creating an account on Heroku and installed the Heroku CLI, open the command line in the application directory and run the following Heroku command:

```sh
# Login if it's the first time running a heroku command
$ heroku login
$ heroku create YourAppName
```

PS: The application name should be unique on Heroku

#### Connecting Heroku App to the GitHub repo

If the application was created successfully:

 - Open the Heroku dashboard and select the newly created app
 - In the “Deploy” tab you can connect your application to a GitHub repo by searching for it
 - Click the button “Enable Automatic Deploys”

Finally, you can click on “Deploy Branch” button in the "Manual deploy" section and your application will be published online.

#### Create a deployment Pipeline

A pipeline is a group of Heroku apps that share the same codebase. Each app in a pipeline represents one of the following steps in a [continuous delivery workflow](http://en.wikipedia.org/wiki/Continuous_delivery):

 - Review
 - Development
 - Staging
 - Production

A common Heroku continuous delivery workflow has the following steps:

 1. A developer creates a pull request to make a change to the codebase.
 2. Heroku automatically creates a review app for the pull request, allowing developers to test the change.
 3. When the change is ready, it’s merged into the codebase’s master branch.
 4. The master branch is automatically deployed to staging for further testing.
 5. When it’s ready, the staging app is promoted to production, where the change is available to end users of the app.

Learn More about Heroku Pipeline [here](https://devcenter.heroku.com/articles/pipelines).

Here you can find a simple and quick video that explains how to create a Heroku pipeline and add the continuous delivery approach to your project.

[Continuous Delivery with Heroku and GitHub](https://www.youtube.com/watch?v=_tiecDrW6yY)

### Add Continuous Integration to your app

> *Continuous Integration (CI) is a development practice that requires developers to integrate code into a shared repository several times a day. Each check-in is then verified by an automated build, allowing teams to detect problems early.* [\[source\]](https://www.thoughtworks.com/continuous-integration)

In this example, we will be using [Codeship.com](https://codeship.com/) as a Continuous Integration service in the cloud.

The main profit to gain when adding a CI service is that the Unit-Tests will be automatically executed on each push, so if the test fails, the person responsible will be notified and the commited code will not be published online.

#### Add your project to Codeship

After creating an account on Codeship:

 - Create a new Project in the projects section
 - Connect it to the GitHub repo
 - Select Codeship Basic
 - Select Java from the "Select your technology to prepopulate basic commands" dropdown list
 - In the "Configure Test Pipelines" section, un-comment the `mvn test` command and remove the others

#### Configure Codeship project with Heroku App

##### **Heroku:**

 - Go to Heroku pipeline dashboard
 - Select the top right button to see more option concerning the app
 - Select the "Configure automatic deployment..." option
 - Check that "Wait for CI to pass before deploy" checkbox is selected

##### **Codeship:**

Follow the steps in [this link](https://documentation.codeship.com/basic/continuous-deployment/deployment-to-heroku/) to configure Codeship with your heroku app.

#### Testing Codeship CI

To test if Codeship is working properly, make sure you have changed the JUnit version in the `pom.xml` file.

Now go ahead and create new Java Class in the following directory:

`YourProject > src > test > [application-packages]`

Here is a JUnit Java Class Test:

``` java
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MyTest {
	@Test
	public void myFirstTest() {
		assertEquals("evaluate", 3 + 3 , 6);
	} 
}

```

To trigger Codeship, add the new file to git and commit/push it to upstream

``` sh
$ git add .
$ git commit -m "Added a Test Class"
$ git push origin master
```
A new "Build" will be created in the Codeship dashboard.
The test will pass and a new version of the application will be deployed on Heroku.

Now try Failing the test as follow: 

``` java
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MyTest {
	@Test
	public void myFirstTest() {
		assertEquals("evaluate", 3 + 3 , 6);
	} 
	
	@Test
	public void mySecondTest() {
		assertEquals("evaluate", 3 + 3 , 5);
	} 
}
```

And change the `index.jsp` file to see if the updates will be deployed online or not.

`YourProject > src > main > webapp > index.jsp`

``` html
<html>
	<body>
		<h2>Hello John</h2>
	</body>
</html>
```

Re-run the git commands:

``` sh
$ git add .
$ git commit -m "Failing the test on purpose"
$ git push origin master
```

You'll see that the build in Codeship will not succeed, and the home page of your application will still have the "Hello World" title instead of "Hello John"

[//]: # (Links)

   [AngularJS]: <http://angularjs.org>
   [Jasmine]: <https://jasmine.github.io>
   [Chart.js]: <http://www.chartjs.org>
   [Karma 1.0]: <https://karma-runner.github.io/1.0/index.html>
   [Heroku]: <https://www.heroku.com>
   [Codeship]: <https://codeship.com>
   [Maven]: <https://maven.apache.org>
   [JavaScript]: <https://developer.mozilla.org/fr/docs/Web/JavaScript>
   [Java 8]: <https://www.java.com/fr/download/faq/java8.xml>
   [JUnit 4]: <http://junit.org/junit4>
