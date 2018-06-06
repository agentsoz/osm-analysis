# OSM Analysis Project

To build, do the following:

```
mvn clean install
```

The program can then be run as follows. The default behaviour is to exit with a help message if no arguments are provided:
```
java -cp target/osm-analysis-1.0-SNAPSHOT-jar-with-dependencies.jar agentsoz.osm.analysis.app.Main

usage: Menu
-c,--get-ways-speed-change <arg>           Find information where speed
                                            difference between two
                                            adjacent ways is greater than
                                            input value, parameter: speed
                                            limit
 -f,--in-file <arg>                         Input database
 -g,--get-mismatch-bicycle-way-relation     find information where
                                            relation tag contain
                                            bicycle_yes, while its ways
                                            tag contain bicycle_no or do
                                            not tag bicycle
 -h,--help                                  Show usage
 -m,--get-relation-name-shortname-missing   Search relation that has
                                            attribute short_name, but does
                                            not have attribute name
 -o,--write-osm-to-database <arg>           Write given osm file into
                                            database, command line
                                            --out-file needed
 -r,--get-ways-relation-speed-change        find information where
                                            max_speed of a way exceed the
                                            relation it within
 -s,--search-missing <arg>                  Search missing attribute of
                                            certain type, parameter: type
 -v,--value <arg>                           Search missing_attribute name
                                            of chosen type
 -w,--out-file <arg>                        Write output to file,.txt/.db


```


Here are some examples of how to use the program:
1. `-h, --help`: show usage/command line instruction; also the defautl behaviour if run with no arguments

1. `--write-osm-to-database mount_alexander_shire_network.osm --out-file osm.db`: This reads from `mount_alexander_shire_network.osm` and creates a `osm.db` in the root directory.

1. `--get-ways-relation-speed --in-file osm.db`: print list of ways in console

1. `--get-ways-speed-change 50 --in-file osm.db --out-file way_speed_change.txt`: write results into `way_speed_change.txt`.

1. `--search-missing way -value maxspeed --in-file osm.db`:  search way which doesn't have attribute `maxspeed`.

1. `--search-missing relation -value name --in-file osm.db missing_relation_name.txt`: search relation which doesn't have attribute `name` and write result into `missing_relation_name.txt`.

1. `--get-relation-name-shortname-missing --in-file osm.db`: search relation that has attribute `short_name` but attribute `name` is missing.

