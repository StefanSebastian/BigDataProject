# import libraries
import folium
import pandas as pd
from cassandra.cluster import Cluster

cluster = Cluster(['localhost'],port=9042)
session = cluster.connect('team7',wait_for_all_pools=True)
session.execute('USE team7')
rows = session.execute('SELECT * FROM alerts')
for row in rows:
    print(row)
 
# Make a data frame with dots to show on the map
data = pd.DataFrame({
'lat':[-58, 2, 145, 30.32, -4.03, -73.57, 36.82, -38.5],
'lon':[-34, 49, -38, 59.93, 5.33, 45.52, -1.29, -12.97],
'name':['Buenos Aires', 'Paris', 'melbourne', 'St Petersbourg', 'Abidjan', 'Montreal', 'Nairobi', 'Salvador']
})
 
# Make an empty map
m = folium.Map(location=[45.704181,24.0047547], zoom_start=7.08)
 
# I can add marker one by one on the map
for i in range(0,len(data)):
    folium.Marker([data.iloc[i]['lon'], data.iloc[i]['lat']], popup=data.iloc[i]['name']).add_to(m)

# Save it as html
m.save('312_markers_on_folium_map1.html')
