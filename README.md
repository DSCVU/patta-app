# Patta Android App

Patta Android App is an Android App for Patta App Project assigned by DSC VU. Basically Patta is a location sharing app. It will generate a unique 10 digit code for every building which will further be used to share location via sharing that 10 digit code in the form of a QR Code. So in simple, a user can generate QR of his location, other can scan that QR and get his exact pin location.

## Tools

- Android Studio
- Java

## Installation

- Clone this project to your computer.
- Open project in Android Studio.


## Directory Structure

|Directory       |Purpose                          
|----------------|-------------------------------
|activities      |It contains all activities with only UI code            
|Listeners       |It contains all Interfaces which will work as listeners
|models          |It contains classes to get interaction with Database/API
|repositories    |It contains Java classes with actual business logic functions
|retrofit        |It contains further two directories Clients and Objs
|Clients         |It contains actual request making Interfaces
|Objs             |It has retrofit objects to retrieve and parse Json responses from Retrofit requests.
|utils           |It contains helping classes
|viewmodels      |It contains View Models for all the activities. Its functions are get called by UI and it further calls relavent repository class' function.

## Note

Make sure to make debuggable false in Gradle Module before publishing app to PlayStore.

```
buildTypes {
        release {
            debuggable false
        }
    }
```