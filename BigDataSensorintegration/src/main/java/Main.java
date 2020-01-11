import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession
                .builder()
                .appName("SensorDataIntegration")
                .master("local[*]")
                .getOrCreate();

        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

        String path = "csvData.csv";
        JavaRDD<String> lines = jsc.textFile(path);
//        lines.collect();

        for(String line:lines.collect()){
            System.out.println(line);
        }

        lines.saveAsTextFile("hdfs://10.111.0.250:8020/user/team_7/sensorDataIntegration");


        System.out.println("Program finished");

        spark.stop();
    }
}
