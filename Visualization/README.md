Data vizualization layer

1. mapview 
Displays dangerous areas as pins on a map.
Reads data from Cassandra db running on the cluster.
Expects the following columns to be present: latitude, longitude, type.

2. dashboard that allows users to create reports over generator data

-install R + R Studio
-install shiny : install.packages("shiny")
-open R Studio
-open project from shiny_app_reports
-runApp('App')

Dashboard generates the following reports :
- error rate bar plot per drone flight
- histogram of the distribution of error codes over all measurements

