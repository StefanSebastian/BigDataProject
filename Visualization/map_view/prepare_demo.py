from cassandra.cluster import Cluster
import time
import random

cluster = Cluster(['localhost'],port=9042)
session = cluster.connect()
session.execute('create keyspace if not exists team7 with replication = {\'class\':\'SimpleStrategy\', \'replication_factor\':3};')
session.execute('USE team7')

session.execute('DROP TABLE IF EXISTS team7.vis_demo')
session.execute('CREATE TABLE if not exists team7.vis_demo(part_id text PRIMARY KEY, timestamp bigint, latitude double, longitude double, type text)')
session.execute('CREATE CUSTOM INDEX if not exists ON team7.vis_demo(timestamp) USING \'org.apache.cassandra.index.sasi.SASIIndex\' WITH OPTIONS = {\'mode\':\'SPARSE\'} ')
session.execute('INSERT INTO team7.vis_demo(part_id, timestamp, latitude, longitude, type) values(\'1\',1578682276,46.515704,24.395216,\'test\')')
session.execute('INSERT INTO team7.vis_demo(part_id, timestamp, latitude, longitude, type) values(\'2\',1578682275,46.7636696,23.6134136,\'test\')')
session.execute('INSERT INTO team7.vis_demo(part_id, timestamp, latitude, longitude, type) values(\'3\',1578682277,46.529104,23.8227234,\'test\')')

latitudeLowerBound = 43
latitudeUpperBound = 47
longitudeLowerBound = 22
longitudeUpperBound = 26
part_id = 4
while True:
    time.sleep(3)
    timestamp = str(int(time.time()))
    part_id_str = str(part_id)
    part_id += 1
    lat = str(random.uniform(latitudeLowerBound, latitudeUpperBound))
    lon = str(random.uniform(longitudeLowerBound, longitudeUpperBound))
    print("Generating data " + lat + ", " + lon)
    session.execute('INSERT INTO team7.vis_demo(part_id, timestamp, latitude, longitude, type) values(\'' + part_id_str + '\',' + timestamp + ',' + lat + ',' + lon + ',\'test\')')

    
