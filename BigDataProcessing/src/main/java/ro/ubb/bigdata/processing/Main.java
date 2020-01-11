package ro.ubb.bigdata.processing;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

public class Main {

    public static void main(String[] args){
        System.out.println("Works!");

        SparkSession spark = SparkSession
                .builder()
                .appName("SensorDataProcessing")
                .master("local[*]")
                .getOrCreate();

        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());



    }
}
