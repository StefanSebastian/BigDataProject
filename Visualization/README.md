Data vizualization layer

1. mapview 
Displays dangerous areas as pins on a map.
Reads data from Cassandra db running on the cluster.
Expects the following columns to be present: latitude, longitude, type.

-- One time version : map_view/mapview.py
It generates an html file when running python map_view.mapview.py

-- Real time version : map_view/mapview.ipynb
Real time dashboard of alerts. Must run in a jupyter notebook
* jupyter notebook
* open file in browser
* run cells

2. dashboard that allows users to create reports over generator data

-install R 
-install packages : 
* install.packages("shiny")
* install.packages("ggplot2")
* install.packages("RMariaDB")
- update paths in start_reports.bat or start_reports.command
run previous file

Dashboard generates the following reports :
- error rate bar plot per drone flight
- histogram of the distribution of error codes over all measurements

