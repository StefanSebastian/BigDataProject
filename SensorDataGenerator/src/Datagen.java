import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Datagen {

    private Double humidity_max = null;
    private Double humidity_min = null;
    private Double x_min = null;
    private Double x_max = null;
    private Double y_min = null;
    private Double y_max = null;
    private Double z_min = null;
    private Double z_max = null;
    private String FilePath = null;

    List<Double> LisOfVariables = new CopyOnWriteArrayList<>();


    private BufferedWriter writer = null;
    private static Random randomValue = new Random();

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public Datagen() {
        read_prop();
    }

    public void start() {
        openFileAndWrite();
    }

    private void read_prop() {

        InputStream inputStream;

        try {
            Properties prop = new Properties();
            String propFileName = "app.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            humidity_max = Double.parseDouble(prop.getProperty("h_max"));
            humidity_min = Double.parseDouble(prop.getProperty("h_min"));
            x_max = Double.parseDouble(prop.getProperty("x_max"));
            x_min = Double.parseDouble(prop.getProperty("x_min"));
            y_max = Double.parseDouble(prop.getProperty("y_max"));
            y_min = Double.parseDouble(prop.getProperty("y_min"));
            z_max = Double.parseDouble(prop.getProperty("z_max"));
            z_min = Double.parseDouble(prop.getProperty("z_min"));
            FilePath = prop.getProperty("csv_path");

            inputStream.close();
            System.out.println("Properties imported!");

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println("Exception: " + e);
            System.exit(0);
        }
    }

    private void openFileAndWrite() {

        try {
            writer = new BufferedWriter(new FileWriter(new File(FilePath), true));
            System.out.println("File created/opened!");

            generateFirstValues();
            System.out.println("First values generated");

            executor.scheduleAtFixedRate(RandomizerRunnable, 0, 200, TimeUnit.MILLISECONDS);
            executor.scheduleAtFixedRate(WriterRunnable, 0, 500, TimeUnit.MILLISECONDS);

            System.out.println("Schedulers set");

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            System.exit(1);
        }
    }

    Runnable WriterRunnable = new Runnable() {
        public void run() {
            try {
                writer.write(System.currentTimeMillis() + ";" +
                        String.format("%.3f", LisOfVariables.get(0)) + ";" +
//                        String.format("%.3f", LisOfVariables.get(1)) + ";" +
//                        String.format("%.3f", LisOfVariables.get(2)) + ";" +
//                        String.format("%.3f", LisOfVariables.get(3)) + ";" +
                        String.format("%.3f", LisOfVariables.get(4)) + "\n");
                writer.flush();

            } catch (Exception ex) {
                System.out.println("Error on writing to file");
                System.out.println(ex.getMessage());
                executor.shutdown();
                System.exit(1);
            }
        }
    };

    Runnable RandomizerRunnable = new Runnable() {

        public void run() {
            try {

                executor.execute(() -> {
                    changeGenericWithIncremental(x_min, x_max, 0);
                });

                executor.execute(() -> {
                    changeGenericWithIncremental(y_min, y_max, 1);
                });
                executor.execute(() -> {
                    changeGenericWithIncremental(z_min, z_max, 2);
                });

                executor.execute(() -> {
                    changeGenericWithIncremental(humidity_min, humidity_max, 3);
                });

                executor.execute(() -> {
                    changeMagneticFieldValue();
                });
            } catch (Exception ex) {
                System.out.println("Error on data randomizer");
                System.out.println(ex.getMessage());
                executor.shutdown();
                System.exit(1);
            }
        }
    };

    // min * (max - min) * rand
    public Double GenerateIncrementalValue() {
        return -1 + (1 - (-1)) * randomValue.nextDouble();
    }

    private void generateFirstValues() {
        Double currentHumidity = (randomValue.nextDouble() * (humidity_max - humidity_min)) + humidity_min;
        Double currentX = (randomValue.nextDouble() * (x_max - x_min)) + x_min;
        Double currentY = (randomValue.nextDouble() * (y_max - y_min)) + y_min;
        Double currentZ = (randomValue.nextDouble() * (z_max - z_min)) + z_min;
        Double currentMagnetic = Math.sqrt(currentX * currentX + currentY * currentY + currentZ * currentZ);

        LisOfVariables.add(currentX);
        LisOfVariables.add(currentY);
        LisOfVariables.add(currentZ);
        LisOfVariables.add(currentHumidity);
        LisOfVariables.add(currentMagnetic);
    }

    private void changeGenericWithIncremental(Double MinVal, Double MaxVal, Integer index) {
        Double auxVal = GenerateIncrementalValue();
        Double ValueVar = LisOfVariables.get(index);
        if (ValueVar + auxVal > MaxVal)
            ValueVar -= auxVal;
        else if (ValueVar + auxVal < MinVal) {
            ValueVar += auxVal;
        } else {
            ValueVar += auxVal;
        }
        LisOfVariables.set(index, ValueVar);
    }

    private void changeMagneticFieldValue() {
        LisOfVariables.set(4, Math.sqrt(LisOfVariables.get(0) * LisOfVariables.get(0) +
                LisOfVariables.get(1) * LisOfVariables.get(1) + LisOfVariables.get(2) * LisOfVariables.get(2)));
    }
}
