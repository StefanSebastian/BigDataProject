# import libraries
import folium
import pandas as pd
from cassandra.cluster import Cluster

cluster = Cluster(['localhost'],port=9042)
session = cluster.connect('team7',wait_for_all_pools=True)
session.execute('USE team7')
rows = session.execute('SELECT * FROM alerts')
lat = []
lon = []
labels = []
for row in rows:
    print(row)
    lat.append(row.latitude)
    lon.append(row.longitude)
    labels.append(row.type)
 
# Make a data frame with dots to show on the map
data = pd.DataFrame({
'lat':lat,
'lon':lon,
'name':labels
})
 
# Make an empty map, roughly around Romania
m = folium.Map(location=[45.704181,24.0047547], zoom_start=7.08)
 
# Add data picked up from cassandra
for i in range(0,len(data)):
    folium.Marker([data.iloc[i]['lat'], data.iloc[i]['lon']], popup=data.iloc[i]['name']).add_to(m)

# Save it as html
m.save('map_visualisation.html')
