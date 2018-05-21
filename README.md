# OSM Analysis Project

To build and run, do the following:

```
mvn clean install
java -cp target/osm-analysis-1.0-SNAPSHOT-jar-with-dependencies.jar agentsoz.osm.analysis.app.Main 

This shows command line instruction in console.

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

Test case examples:
1. run without argument -> usage menu

2. --write-osm-to-database mount_alexander_shire_network.osm -out-file osm2.db -> This reads from mount_alexander_shire_network.osm and creates a 'osm.db' in the root directory.

2. --get-ways-relation-speed -file osm.db -> print in console

3. --get-ways-speed-change 50 -file osm.db -out-file way_speed_change.txt  -> write results to text file

4. --search-missing way -value maxspeed -in-file osm.db  ->  search for way which missing value "maxspeed"
