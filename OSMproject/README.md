###the basic information of data and database(default package)
**Store_Main.java** are used to read data from OSM file and write the data to the SQLITE database. 
I created eight tables to store the data, two for label **node**, three for label **way** and three for label **relation**.  
**"node" storage**  
1.Table "nodes" stores the basic information of label "node"   
2.Table "nodes_tags" stores the label "tag" of the node.  
**"way" storage**   
1.Table "ways" stores the basic information of label "node"  
2.Table "ways_nodes" stores the id of label "node" and "way"  
3.Table "ways_tags" stores the information of label "tag".  
**"relation" storage**  
1.Table "relations" stores the basic information of label "relation"  
2.Table "relations_members" stores the information of label "member"  
3.Table "relations_tags" stores the information of label "tag".    
**NOTICE**  
To store the data from OSM file to SQLITE database, you need to change the path of original data which on the **line 27 of Store.java**
###Handle package
**MaxSpeedGapProblem.java** describes a potential problem that two connected ways has too much max speed gap.  

**NOTICE**  
To run the above classes to check problem, run the **Handle_Main.java**
