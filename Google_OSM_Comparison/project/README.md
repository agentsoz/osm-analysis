To build do:
```
mvn clean install
```
Then input:
```
java -cp target/project-1.0-SNAPSHOT-jar-with-dependencies.jar osm.analysis.google.Main
```
plus arguments below:


###### TO GENERATE A DATABASE FROM A *.OSM FILE FIRST

1. Path of your .osm file (Compulsory)
```
--osm-read-path
```

2. Path to store .db file (Compulsory)
```
--db-store-path
```

###### TO GET A SUMMARY RESULT

1. Orig/dest input (Compulsory)
```
--list-of-orig/dest   (JSON format)
```
or
```
--generate-random-origins (integer number)
--radius-of-dest (unit: km)
--db-read-path
```

2. Choose one of the following options to set a threshold (Compulsory)
```
--dist-diff-reporting-threshold-percent  (DEFAULT: 5)
--dist-diff-reporting-threshold-km (DEFAULT: 20)
--time-diff-reporting-threshold-percent  (DEFAULT: 5)
--time-diff-reporting-threshold-hr  (DEFAULT: 0.5)
```

3. Store the summary analysis to a local CSV file (Optional). If not provided, then the output will be printed to stdout.

```
--summary-store-path  (file path)
```

##### TO STORE DETAILED ANALYSIS

1. (Compulsory) Store detailed analysis by route ID from a summary file

```
--input-summary-file
```

```
--analyze-route-id (int,int,int,...)
--analyze-route-id (int-int)
--analyze-route-id (int,int-int,int,...)
```

2. (Compulsory) Define the file path to store detailed analysis

```
--detail-store-path
```


#### ARGUMENTS EXAMPLES

Generate database: (Use your own .osm path)
```
--osm-read-path %mount_shire.osm% --db-store-path osm.db
```

Input a list of orig/dest and print the summary result of ALL the routes:
```
 --list-of-orig/dest "{routes:[{ori:{lat:-37.6941552,lon:144.5793929},dest:{lat:-34.2968327,lon:147.5588931}},{ori:{lat:-36.0905765,lon:146.9311751},dest:{lat:-35.5126728,lon:148.6093932}},{ori:{lat:-34.3001501,lon:142.1915312},dest:{lat:-35.6469135,lon:148.6763364}},{ori:{lat:-34.3607745,lon:148.025851},dest:{lat:-36.6893697,lon:142.4108182}},{ori:{lat:-34.5535703,lon:146.4113448},dest:{lat:-34.6079865,lon:147.1291069}},{ori:{lat:-37.9158962,lon:142.4296366},dest:{lat:-37.1788221,lon:144.0485408}},{ori:{lat:-36.3472808,lon:146.8028536},dest:{lat:-37.2747424,lon:149.0709654}},{ori:{lat:-37.6874412,lon:144.4336144},dest:{lat:-37.7571033,lon:144.9347327}},{ori:{lat:-36.4217525,lon:148.6168595},dest:{lat:-36.1342025,lon:146.9038279}},{ori:{lat:-35.2469941,lon:149.0977115},dest:{lat:-36.1342025,lon:141.284169}}]}" --dist-diff-reporting-threshold-percent 0 --summary-store-path fullset.csv
```

Randomly generate 20 routes (length < 800km) from database and store the summary result
```
--generate-random-origins 10 --radius-of-dest 800 --db-read-path osm.db --time-diff-reporting-threshold-hr 0.1 --summary-store-path summary.csv
```

Store detailed result based on the summary result from last running, choose route id: 2,3,4,6
```
--input-summary-file fullset.csv --analyze-route-id 2-4,6 --detail-store-path detail.csv
```
