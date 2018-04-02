HandleNode, HandleRelation and HandleWay classes are used to read data from data and write the data to the sqlite. 
I created eight tables to store the data, two for label "node", three for label "way" and three for label "relation".
<node>: table "nodes" stores the basic information of label "node" and table "nodes_tags" stores the label "tag" of the node.
<way>: table "ways" stores the basic information of label "node", table "ways_nodes" stores the id of label "node" and "way", table "ways_tags" stores the information of label "tag".
<relation>: table "relations" stores the basic information of label "relation", table "relations_members" stores the information of label "member", table "relations_tags" stores the information of label "tag".  

NOTICE:
if you want to run this, you need to change the path of original data which on the line 27 of HandleNode.java