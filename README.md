# OSM Analysis Project

To build and run, do the following:

```
mvn clean install
java -cp target/osm-analysis-1.0-SNAPSHOT-jar-with-dependencies.jar agentsoz.osm.analysis.app.Main -file data/mount_alexander_shire_network_2018.osm
```

This creates a `osm.db` in the root directory.

Next, we can ask to analyse problems, using something like:
```
java -cp target/osm-analysis-1.0-SNAPSHOT-jar-with-dependencies.jar agentsoz.osm.analysis.app.Main -maxspeedgapproblem 60 osm.db
```
<mark>*Please add command line arguments, progress indicator, help messages, etc. to make the program more user friendly [DSingh 1-May-2018].*</mark>
