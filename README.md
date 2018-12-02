## Simple Event Calendar App ##

This is a simple calendar application for the android. It only shows weekly view of the calendar.

#### Technologies ####
The app is developed to use GridView for the calendar and events. AndroidX is used instead of support library. Modern
MVVM design pattern is followed.

The events are persist locally in a *SQLite* database using *Room* and remotely in *Google Firebase Realtime Database*.
I have to say, as we are using firebase database we can easily get rid of the SQLite database. Because firebase library
also supports local persistence of the database. Additionally it has enough free usage quota for playing around.

For authentication I have used firebase-ui-auth library. It is possible to use the app without signing in. All the
events created during the offline mode will be migrated to remote database upon signing in.

#### Couple of Things to Improve ####
-   Show the events according to time also besides the appropriate date column.
-   Show the events block size according to its time span. This will need to use different view than the GridView.
-   Opportunity to add other calendars (i.e google calendar) to this.
-   We can use firebase database only and get rid of the SQLite database for local and remote both cases.

#### How to build ####
This is very easy to build with gradle. I have used gradle version 4.10.2 for the build.
Target SDK is 28 minimum SDK is 23. There is a debug build available from [here](https://drive.google.com/file/d/16yqTpc2VQ4eXPT_9bGYJ1m6wIbgz6em_/view?usp=sharing)

The debug build is signed with debug signing key. So it should be okay to connect with the firebase database.

-   Execute `./gradlew tasks` to see the available commands.
-   Execute `./gradlew build` to build the application.
-   Execute `./gradlew assembleDebug` for debug apk. Your debug build won't have the appropriate key, so you might not
be able to connect to firebase with google but you should be able to use the email+password signing method.
