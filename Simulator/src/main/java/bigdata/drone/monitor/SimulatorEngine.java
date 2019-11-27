package bigdata.drone.monitor;

import bigdata.drone.monitor.model.DroneStatus;
import bigdata.drone.monitor.repository.DroneStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;

/**
 * @author stefansebii@gmail.com
 */
@Component
public class SimulatorEngine {

    private Logger logger = LoggerFactory.getLogger(SimulatorEngine.class);

    private static final int ID_SIZE = 10;
    private static final int BATCH_ID_SIZE = 7;

    private long measurementCounter = 1L;

    private long flightCounter = 1L;

    @Autowired
    private DroneStatusRepository droneStatusRepository;

    @PostConstruct
    private void init() {
        DroneStatus lastStatus = droneStatusRepository.findLastStatus();
        logger.info("Last status : " + lastStatus);
        if (lastStatus == null) {
            measurementCounter = 1L;
            flightCounter = 1L;
        } else {
            measurementCounter = parseStringId(lastStatus.getPartId()) + 1;
            flightCounter = parseStringId(lastStatus.getBatchId()) + 1;
        }
        logger.info("Flight counter " + flightCounter);
        logger.info("Measurement counter " + measurementCounter);
    }

    @Scheduled(fixedRate = 3000)
    public void generateDroneStatus() {
        logger.info("Measurement : " + measurementCounter);

        DroneStatus status = new DroneStatus();
        status.setPartId(buildStringId(measurementCounter, ID_SIZE));
        status.setBatchId(buildStringId(flightCounter, BATCH_ID_SIZE));
        status.setTimestamp(new Timestamp(System.currentTimeMillis()));
        droneStatusRepository.save(status);

        measurementCounter += 1;
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
