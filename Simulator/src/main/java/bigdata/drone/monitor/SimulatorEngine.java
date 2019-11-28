package bigdata.drone.monitor;

import bigdata.drone.monitor.model.DroneStatus;
import bigdata.drone.monitor.model.ErrorCode;
import bigdata.drone.monitor.model.ErrorOccurence;
import bigdata.drone.monitor.repository.DroneStatusRepository;
import bigdata.drone.monitor.repository.ErrorOccurenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * @author stefansebii@gmail.com
 */
@Component
public class SimulatorEngine {

    private Logger logger = LoggerFactory.getLogger(SimulatorEngine.class);

    private Random random = new Random();

    @Autowired
    private DroneStatusRepository droneStatusRepository;

    @Autowired
    private ErrorOccurenceRepository errorOccurenceRepository;

    @Autowired
    private Config config;

    private long measurementCounter = 1L;

    private long flightCounter = 1L;

    // track drone position
    private double altitude = 0;
    private double latitude;
    private double longitude;

    @PostConstruct
    private void init() {
        DroneStatus lastStatus = droneStatusRepository.findLastStatus();
        logger.info("Last status : " + lastStatus);
        if (lastStatus == null) {
            measurementCounter = 1L;
            flightCounter = 1L;
            altitude = 0;
        } else {
            measurementCounter = parseStringId(lastStatus.getPartId()) + 1;
            flightCounter = parseStringId(lastStatus.getBatchId()) + 1;
            altitude = 0;
        }
        logger.info("Flight counter " + flightCounter);
        logger.info("Measurement counter " + measurementCounter);
    }

    @Scheduled(fixedRate = 10)
    public void generateDroneStatus() {
        logger.info("Measurement : " + measurementCounter);

        DroneStatus status = new DroneStatus();
        status.setPartId(buildStringId(measurementCounter, config.getIdLen()));
        status.setBatchId(buildStringId(flightCounter, config.getBatchIdLen()));
        status.setTimestamp(System.currentTimeMillis());
        double new_altitude = getAltitude();
        if (new_altitude < config.getAltitudeLowerBound() ||
                new_altitude > config.getAltitudeUpperBound()) {
            ErrorOccurence errorOccurence = new ErrorOccurence();
            errorOccurence.setCode(ErrorCode.ALTITUDE_OUTSIDE_LIMITS);
            errorOccurence.setPartId(status.getPartId());
            errorOccurenceRepository.save(errorOccurence);
        }
        status.setAltitude(getAltitude());

        droneStatusRepository.save(status);

        measurementCounter += 1;
    }

    enum Direction {
        DESCEND, ASCEND, HOVER
    }

    private Direction direction = Direction.ASCEND;

    private <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    private double getAltitude() {
        double deviceFailure = random.nextDouble();
        if (deviceFailure < config.getDeviceFailureChance()) {
            return -1;
        }

        // low chance to change direction
        if (random.nextDouble() < 0.01){
            direction = randomEnum(Direction.class);
            logger.info("Changed vertical direction to " + direction);
        }

        // get altitude change from a Gaussian distribution
        double progress = random.nextGaussian();
        if (altitude < 50) { // for lower altitude highly favour positive values
            if (random.nextDouble() > 0.1 && progress < 0) {
                progress = -progress;
            }
        } else if (altitude > config.getAltitudeUpperBound()) { // over legal limit try to descend
            if (random.nextDouble() > 0.1 && progress > 0) {
                progress = -progress;
            }
        } else { // otherwise follow a direction which changes randomly
            if (direction == Direction.ASCEND && progress < 0) {
                progress = -progress;
            } else if (direction == Direction.DESCEND && progress > 0) {
                progress = -progress;
            }
        }

        altitude += progress;
        return altitude;
    }

    private String buildStringId(long id, int size) {
        StringBuilder partIdStr = new StringBuilder();
        partIdStr.append(id);

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < size - partIdStr.length(); i++) {
            buffer.append('0');
        }
        buffer.append(partIdStr);
        return buffer.toString();
    }

    private long parseStringId(String idStr){
        int index = 0;
        while (idStr.substring(index, index + 1).equals("0")) {
            index += 1;
        }
        String extractedId = idStr.substring(index);
        return Long.valueOf(extractedId);
    }

}
