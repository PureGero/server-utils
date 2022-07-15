# server-utils
MultiPaper-compatible utility commands to manage your Paper server.
This is primarily just for debugging MultiPaper.

## Commands:

```
/executeonallservers
/instancesping
/ping
/setpersistence
/setplayercount
/setsimulationdistance
/setviewdistance
/subscribers
```

## Building

1. Build MultiPaper and then run (inside the MultiPaper directory):  
`./gradlew publishToMavenLocal`

2. Enter the server-utils directory and run:  
`mvn`

3. The plugin jar is available at  
`target/server-utils-1.19-v1.jar`