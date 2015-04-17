# Introduction #

http://hoteljavaopensource.blogspot.com/2009/10/migration-to-google-app-engine-and-unit.html

This wikipage describes solution details and describes how to set up environment to run test unit example.



## Solution description ##

Assume that our big JPA (server side) application is having some entity classes (Customer), DataAccessService facace with two methods - addCustomer and getCustomerById and huge test suite of JUnit4 test cases.

Migration to Google App Engine probably ended up with two sets of entity classes and two persistence.xml files, DataAccessService refactored more or less and very urgent need to enable suite of tests to minimize the risk of regression and side effects.

Entity class


Google App Engine:
http://code.google.com/p/javahotel/source/browse/trunk/examples/localunittesting/source/gaesource/org/example/jpa/entities/

Non Google App Engine
http://code.google.com/p/javahotel/source/browse/trunk/examples/localunittesting/source/nongaesource/org/example/jpa/entities

```
    @Entity
    public class Customer {       
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private String id;       
            @Basic
            private String desciption;
    ...}
```

Server side data access code (common for Google App Engine and non Google App Engine)

http://code.google.com/p/javahotel/source/browse/trunk/examples/localunittesting/source/jpasource/org/example/service

```
    public class DataAccessService {
            public void addCustomer(Customer c) {
              ...
            }
            public Customer getCustomerByKey(String key) {
              ...
            }
```

Google App Engine specific code to set up test environment.

http://code.google.com/p/javahotel/source/browse/trunk/examples/localunittesting/source/gaesource/org/example/jpa/

> It is code taken from http://code.google.com/intl/pl/appengine/docs/java/howto/unittesting.html. and refactored a little bit. It is convenient to keep the separation of LocalDatasoteEnvironment and LocalServiceEnvironment to have this code ready in the case od testing some Google App Engine specific services.

```
    public class LocalDatastoreEnvironment extends LocalServiceEnvironment {
        @Override
        public void beforeTest() {
            super.beforeTest();
            ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
            proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString());
        }
        @Override
        public void afterTest() {
            ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
            LocalDatastoreService datastoreService = (LocalDatastoreService) proxy.getService("datastore_v3");
            datastoreService.clearProfiles();
            super.afterTest();
        }
    }


    public class LocalServiceEnvironment {
                public void beforeTest() {
                    ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
                    ApiProxy.setDelegate(new ApiProxyLocalImpl(new File(".")){});
                }
                public void afterTest() {
                    // not strictly necessary to null these out but there's no harm either
                    ApiProxy.setDelegate(null);
                    ApiProxy.setEnvironmentForCurrentThread(null);
                }

    package org.example.jpa.entities;
    public class TestCaseEnvironment extends LocalDatastoreEnvironment
    }
```

Creating additional (empty) TestCaseEnvironment seems uneccessary here but this way we can keep name style being used in Google App Engine documentation and avoid propagating this names to the whole solution.

http://code.google.com/p/javahotel/source/browse/trunk/examples/localunittesting/source/nongaesource/org/example/jpa/entities/TestCaseEnvironment.java

The same TestCaseEnvironment implementation for no Google App Engine contains empty method.

```
    public class TestCaseEnvironment {
            public void beforeTest() {
            }
            public void afterTest() {
            }
    }
```

Test suite (common for Google App Engine and non Google App Engine)

http://code.google.com/p/javahotel/source/browse/trunk/examples/localunittesting/source/jpasource/test/example/jpa/

```
    public class TestCase1 extends TestCaseHelper {       
            @Test
            public void Test1() {
            ..
           }
```

I found very useful to extend all test case classes with a common helper class, it could contain some code, checks and declarations shared by all tests to avoid code duplication and makes creating new test classes more easy.

TestCaseHelper contains common code being run before and after every test and also contains code for setting up and closing down test environment.

http://code.google.com/p/javahotel/source/browse/trunk/examples/localunittesting/source/jpasource/test/example/jpa/TestCaseHelper.java

```
    abstract public class TestCaseHelper {
            private final TestCaseEnvironment testE = new TestCaseEnvironment();
            @Before
            public void setUp() {
                    testE.beforeTest();
                    ......
            }
            @After
            public void tearDown() {
                    ........
                    testE.afterTest();
            }
    }
```

Tests for Google App Engine solution and for non Google App Engine solution are run in different context and in different instance of JVM, so by means of combining proper class path we can achieve our purpose without any additional tasks.

The more detailed instruction how to create development envorinment using this example in Eclipse is described here.

http://code.google.com/p/javahotel/wiki/GooglaAppEngineUnitTesting

## Setup development environment to run a test ##

### Prerequisite ###

Eclipse with plugins:
Google App Engine Java SDK

Eclipse Data Tool Platform

### Steps to create project ###


  * Create project (not Java project). This project will play a role of source container. Checkout example to this project using svn : http://javahotel.googlecode.com/svn/trunk/examples/localunittesting javahotel-read-only. Following directory structure should be creaeted:
    * gaesource
    * jpasource
    * nongaesource

  * Create project TestProjectGae. It should be Google Web Application Project. GWT is not necessary. Add JUnit4 library to project path. Add also appengine-api-stubs.jar and appengie-local-runtime.jar to project path. Using eclipse "link source" add 'gaesource' and "jpasource" as source folders.

  * Create project TestProjectNonGae. It should have Java Persisten facet. Also add Derby Embedded JDBC Driver to project path. Using eclipse "link source' add 'nongaesource' and 'jpasource' as source folders. Add also JUnit4 library to project path.

### Run ###

  * To run demo test unit for gae project simply run 'JUnit Test' for TestCase1 class.
  * To run demo test unit for nongae project the same but from TestProjectNonGae

### What more ###

Simply add next test method to TestCase1 class and run again for both project. Pay attention that you add new test to common class without bothering about Google App Engine and non Google App Engine frameworks.