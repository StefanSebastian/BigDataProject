This layer contains data prepared to be presented to business users.
Data is stored in a Cassandra database running on the cluster.

// TODO humidity , magnetic field strength, batch_id, part_id

Data format : timestamp bigint PRIMARY KEY, latitude double, longitude double, type text

Steps:
1. create our keyspace
* create keyspace team7 with replication = {'class':'SimpleStrategy', 'replication_factor':3};
2. check operation was successful
* describe keyspaces
3. create relevant table
create table alerts(timestamp bigint PRIMARY KEY, latitude double, longitude double, type text);

4. insert some rows for testing
* insert into team7.alerts(timestamp,latitude,longitude,type) values(1578682275,46.7636696,23.6134136,'TEST');
* insert into team7.alerts(timestamp,latitude,longitude,type) values(1578682276,46.515704,24.395216,'TEST');
* insert into team7.alerts(timestamp,latitude,longitude,type) values(1578682277,46.529104,23.8227234,'TEST');

Or through python :
from cassandra.cluster import Cluster
cluster = Cluster(['localhost'],port=9042)
session = cluser.connect()
session.execute('create keyspace team7 with replication = {\'class\':\'SimpleStrategy\', \'replication_factor\':3};'
session.execute('use team7')
session.execute('create table alerts(timestamp bigint PRIMARY KEY, latitude double, longitude double, type text);')
session.execute('insert into team7.alerts(timestamp,latitude,longitude,type) values(1578682275,46.7636696,23.6134136,\'TEST\');')
session.execute('insert into team7.alerts(timestamp,latitude,longitude,type) values(1578682276,46.515704,24.395216,\'TEST\');')
session.execute('insert into team7.alerts(timestamp,latitude,longitude,type) values(1578682277,46.529104,23.8227234,\'TEST\');')