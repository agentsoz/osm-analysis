**************************************** USAGE ***************************************

Command: java -jar OSM.jar -file C:\\Users\\Castiel\\Desktop\\osm_wenbo\\OSMproject\\mount_alexander_shire_network.osm
Description: store input into database

Command: java -jar OSM.jar -maxspeedproblem
Description:

Command: java -jar OSM1.jar -relationspeedproblem
Description:




Command：java -jar OSM1.jar -prepare
Description: a glimpse of data pool
Test Result: "-prepare"

May 01, 2018 11:31:27 AM handler.DataHandler prepare
INFO: running time for get ways from database:241744ms
May 01, 2018 11:31:27 AM handler.DataHandler prepare
INFO: how many ways in this area: 14360
May 01, 2018 11:31:27 AM handler.DataHandler prepare
INFO: how many ways have max speed: 5717
May 01, 2018 11:31:27 AM handler.DataHandler prepare
INFO: running time for get relations from database:124243ms
May 01, 2018 11:31:27 AM handler.DataHandler prepare
INFO: how many relations in this areas:7829
May 01, 2018 11:31:27 AM handler.DataHandler prepare
INFO: how many ways which in the relation dont exist in the table ways:122693
May 01, 2018 11:31:27 AM handler.DataHandler prepare
INFO: how many ways which in the relation exist in the table ways:16621
