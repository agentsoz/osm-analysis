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

usage: Menu
 -f,--file <arg>                          Here you can set database path
 -opt1,--get-ways-speed-change <arg>      get two adjacent way speed
                                          different greater than input
                                          value
 -opt2,--get-ways-relation-speed-change   get ways' max_speed exceed its
                                          relation
 -relation <arg>                          choose type relation
 -s,--search-missing <arg>                Search missing attribute
 -w,--out-file <arg>                      Write file to a text file
 -way                                     choose type way

Test case example:
1. run without argument -> usage menu
2. --get-ways-relation-speed -file osm.db -> print in console
3. --get-ways-speed-change 50 -file osm.db -out-file way_speed_change.txt  -> write to text file
 
