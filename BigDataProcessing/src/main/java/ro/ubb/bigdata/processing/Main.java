package ro.ubb.bigdata.processing;

import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class Main extends SparkSessionBuilder implements Serializable {


    public void run() throws StreamingQueryException {
        StructType sensorDataSchema = new StructType()
                .add("timestamp", "string")
                .add("humidity", "string")
                .add("magnetic_field", "string");

        StructType generatorDataSchema = new StructType()
                .add("part_id", "string")
                .add("altitude", "double")
                .add("batch_id", "string")
                .add("longitude", "double")
                .add("latitude", "double")
                .add("timestamp", "long");


        Dataset<Row> sensorDataSet = spark.readStream()
                .option("sep", ",")
                .schema(sensorDataSchema)
                .csv("hdfs://10.111.0.250:8020/user/team_7/sensorDataIntegration");

        Dataset<Row> generatorDataSet = spark.readStream()
                .option("sep", ",")
                .schema(generatorDataSchema)
                .csv("hdfs://10.111.0.250:8020/user/team_7/simulator");

        Dataset<Row> resultSensor = sensorDataSet.join(generatorDataSet, "timestamp");

        StreamingQuery resultStream = resultSensor.writeStream()
                .outputMode("append")
                .foreachBatch(new VoidFunction2<Dataset<Row>, Long>() {
                    @Override
                    public void call(Dataset<Row> v1, Long v2) throws Exception {
                        v1.write().format("org.apache.spark.sql.cassandra").options(new HashMap<String, String>() {
                            {
                                put("keyspace", "team7");
                                put("table", "sensor_generator_join");
                            }
                        }).mode(SaveMode.Append).save();

                    }
                })
                .start();

        resultStream.awaitTermination();
    }

    public static void main(String[] args) throws StreamingQueryException, IOException {
        Main main = new Main();
        main.run();
    }
}
