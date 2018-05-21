# OSM Analysis Project

To build and run, do the following:

```
mvn clean install
java -cp target/osm-analysis-1.0-SNAPSHOT-jar-with-dependencies.jar agentsoz.osm.analysis.app.Main 

This shows command line instruction in console.

```
<mark>*Please add command line arguments, progress indicator, help messages, etc. to make the program more user friendly [DSingh 1-May-2018].*</mark>

usage: Menu
 -f,--in-file <arg>                       Input database
 -h,--help                                Show usage
 -o,--write-osm-to-database <arg>         Write given osm file into
                                          database, command line
                                          --out-file needed
 -opt1,--get-ways-speed-change <arg>      Find information where speed
                                          difference between two adjacent
                                          ways is greater than input
                                          value, parameter: speed limit
 -opt2,--get-ways-relation-speed-change   Find information where max_speed
                                          of a way exceed the relation it
                                          within
 -s,--search-missing <arg>                Search missing attribute of
                                          certain type, parameter: type
 -v,--value <arg>                         Search missing_attribute name of
                                          chosen type
 -w,--out-file <arg>                      Write output to file,.txt/.db

Test case examples:
1. run without argument -> show usage/command line instruction

2. -h/--help -> show usage/command line instruction

3. --write-osm-to-database mount_alexander_shire_network.osm --out-file osm.db -> This reads from mount_alexander_shire_network.osm and creates a 'osm.db' in the root directory.

4. --get-ways-relation-speed --in-file osm.db -> print in console

5. --get-ways-speed-change 50 -file osm.db --out-file way_speed_change.txt  -> write results to text file

6. --search-missing way -value maxspeed --in-file osm.db  ->  search way which doesn't have attribute "maxspeed"

7. --search-missing relation -value name --in-file osm.db missing_relation_name.txt -> search relation which doesn't have attribute "name" and write result into missing_relation_name.txt.

8. run test case 5 argument twice -> prompt: file alreay exist

9. run --in-file with database which doesnt't exist -> prompt : file not exist

