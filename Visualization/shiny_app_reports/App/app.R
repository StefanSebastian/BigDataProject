library(shiny)
library(RMariaDB)
library(ggplot2)

ui <- fluidPage(

  titlePanel("title panel"),

  sidebarLayout(
    sidebarPanel("sidebar panel"),
    mainPanel(
      plotOutput("failures",height = 500)
    )
  )
)

# Define server logic required to draw a histogram ----
server <- function(input, output) {
  output$failures <- renderPlot({
    genDb <- dbConnect(RMariaDB::MariaDB(), user='uf5xGXLgpR', password='id46xBiuhV', dbname='uf5xGXLgpR', host='remotemysql.com', port=3306)
    query<-paste("SELECT d.batch_id,
                  sum(case when e.code IS NOT NULL then 1 else 0 end) as error_occurences,
                  count(*) as total_measurements
                  FROM drone_status d left join error_occurence e on d.part_id = e.part_id
                  group by batch_id",sep="")
    rs = dbSendQuery(genDb,query)
    dbRows <- dbFetch(rs)
    dbDisconnect(genDb)

    transformed = transform(dbRows, err_rate = error_occurences/total_measurements)
    ggplot(data=transformed, aes(x=batch_id, y=err_rate)) + geom_bar(stat="identity")
  })
}

shinyApp(ui = ui, server = server)