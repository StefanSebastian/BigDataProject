package ro.ubb.bigdata.processing;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

public class SparkSessionBuilder  implements Serializable {

    public transient SparkSession spark = SparkSession
            .builder()
            .appName("SensorDataIntegration")
            .config("spark.cassandra.connection.host", "localhost")
            .config("spark.cassandra.connection.port", "9042")
            .master("local[*]")
            .getOrCreate();

    public transient JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
}

// ssh -L localhost:5452:localhost:9042 team_7@10.111.0.250