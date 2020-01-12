import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.Trigger;
import org.apache.spark.sql.types.StructType;

public class Main {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession
                .builder()
                .appName("SensorDataIntegration")
                .master("local[*]")
                .getOrCreate();

        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

        StructType userSchema = new StructType()
                .add("timestamp", "string")
                .add("humidity", "string")
                .add("magnetic_field", "string");

        Dataset<Row> csv = spark.readStream()
                .option("sep", ";")
                .schema(userSchema)
                .csv("/Users/gritcoandreea/sparkStuff/sensorData");


        StreamingQuery start = csv.writeStream()
                .format("csv")
                .outputMode("append")
                .trigger(Trigger.ProcessingTime("5 seconds"))
                .option("checkpointLocation","hdfs://10.111.0.250:8020/user/team_7/sparkStreamingFiles")
                .option("path","hdfs://10.111.0.250:8020/user/team_7/sensorDataIntegration")
                .start();

        start.awaitTermination();
    }
}
