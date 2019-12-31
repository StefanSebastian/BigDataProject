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

    // tracks measurements ; used for id
    private long measurementCounter = 1L;

    // tracks flights ; used for batch id
    private long flightCounter = 1L;

    // track drone position
    private double altitude = 0;
    private double latitude;
    private double longitude;

    /**
     * Direction for drone position
     */
    enum Cardinal {
        NORTH, SOUTH, EAST, WEST
    }
    private Cardinal cardinal = randomEnum(Cardinal.class);

    /**
     * Direction for altitude changes
     */
    enum Direction {
        DESCEND, ASCEND, HOVER
    }

    // in the beginning the drone ascends
    private Direction direction = Direction.ASCEND;

    @PostConstruct
    private void init() {
        DroneStatus lastStatus = droneStatusRepository.findLastStatus();
        logger.info("Last status : " + lastStatus);

        measurementCounter = lastStatus == null ? 1L : parseStringId(lastStatus.getPartId()) + 1;
        flightCounter = lastStatus == null ? 1L : parseStringId(lastStatus.getBatchId()) + 1;
        altitude = 0;
        latitude = getRandInRange(config.getLatitudeLowerBound(), config.getLatitudeUpperBound());
        longitude = getRandInRange(config.getLongitudeLowerBound(), config.getLongitudeUpperBound());

        logger.info("Flight counter " + flightCounter);
        logger.info("Measurement counter " + measurementCounter);
    }

    private  <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    private double getRandInRange(double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }

    @Scheduled(fixedRate = 1000)
    public void generateDroneStatus() {
        logger.info("Measurement : " + measurementCounter);

        DroneStatus status = new DroneStatus();
        status.setPartId(buildStringId(measurementCounter, config.getIdLen()));
        status.setBatchId(buildStringId(flightCounter, config.getBatchIdLen()));
        status.setTimestamp(System.currentTimeMillis());

        double new_altitude = getAltitude();
        status.setAltitude(new_altitude);

        updateCoordinates();
        status.setLatitude(latitude);
        status.setLongitude(longitude);

        droneStatusRepository.save(status);

        ErrorCode errorCode = null;
        if (new_altitude < config.getAltitudeLowerBound() ||
                new_altitude > config.getAltitudeUpperBound()) {
            errorCode = ErrorCode.ALTITUDE_OUTSIDE_LIMITS;
        }
        if (latitude < config.getLatitudeLowerBound() ||
                latitude > config.getLatitudeUpperBound() ||
                longitude < config.getLongitudeLowerBound() ||
                longitude > config.getLongitudeUpperBound()){
            errorCode = errorCode == null ? ErrorCode.POSITION_OUTSIDE_LIMITS : ErrorCode.BOTH_OUTSIDE_LMITS;
        }
        if (errorCode != null) {
            ErrorOccurence errorOccurence = new ErrorOccurence();
            errorOccurence.setPartId(status.getPartId());
            errorOccurence.setCode(errorCode);
            errorOccurenceRepository.save(errorOccurence);
        }

        measurementCounter += 1;
    }

    // return a pair latitude, longitude
    private void updateCoordinates() {
        // randomize direction
        if (random.nextDouble() < config.getCardinalChangeChance()) {
            cardinal = randomEnum(Cardinal.class);
            logger.info("Changed cardinal " + cardinal);
        }

        double movement_lat = 0;
        double movement_long = 0;
        double movement = getRandInRange(0, config.getDistanceOnCoords());
        if (cardinal == Cardinal.NORTH) {
            movement_long = movement;
        } else if (cardinal == Cardinal.SOUTH) {
            movement_long = -movement;
        } else if (cardinal == Cardinal.WEST) {
            movement_lat = -movement;
        } else if (cardinal == Cardinal.EAST) {
            movement_lat = movement;
        }

        latitude = latitude + movement_lat;
        longitude = longitude + movement_long;
    }

    private double getAltitude() {
        double deviceFailure = random.nextDouble();
        if (deviceFailure < config.getDeviceFailureChance()) {
            return -1;
        }

        // low chance to change direction
        if (random.nextDouble() < config.getDirectionChangeChance()){
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
            // on Direction.HOVER don't change progress
        }

        altitude += progress;
        return altitude;
    }

    /**
     * Converts long id to string of given size; fills the space with zeroes
     */
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

    /**
     * Parse the string representation of an id ;
     * removes the zeroes then casts to long
     */
    private long parseStringId(String idStr){
        int index = 0;
        while (idStr.substring(index, index + 1).equals("0")) {
            index += 1;
        }
        String extractedId = idStr.substring(index);
        return Long.valueOf(extractedId);
    }

}
