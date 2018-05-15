import googlemaps
from datetime import datetime

def savefile(item, file_name):
    try:
##        saveFile = open(str(file_name),'a')
        saveFile.write(item)
##        saveFile.close()
        
    except Exception, e:
        print 'failed Saving'
        print str(e)
#        time.sleep(10)



gmaps = googlemaps.Client(key='AIzaSyAFxtBeTK_yNWtcy6ura_HBRJTwM0y-bh4')

# snapping to a road
#(syntax: "starting coords|(optional: coords in between for accuracy)|ending coords",interpolate))
#interpolate=(True if you want the road to include all points making the path, so that the result is the snap matching the road exactly,will give more points.
#False if you want the road to snap approximately))
#snap_road = gmaps.snap_to_roads(""" -37.0011720, 144.3100919| -36.9944190, 144.3125640""")
snap_road = gmaps.snap_to_roads(" -36.9965541, 144.3054444")

for x in snap_road:
    print (x)
    saveFile = open('output.txt','a')
    saveFile.write(str(x))
    saveFile.write('\n')
    saveFile.close()
