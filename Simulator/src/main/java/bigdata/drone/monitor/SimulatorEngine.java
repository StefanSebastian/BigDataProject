package bigdata.drone.monitor;

import bigdata.drone.monitor.model.DroneStatus;
import bigdata.drone.monitor.repository.DroneStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author stefansebii@gmail.com
 */
@Component
public class SimulatorEngine {

    @Autowired
    private DroneStatusRepository droneStatusRepository;

    @Scheduled(fixedRate = 1000)
    public void generateDroneStatus() {
        DroneStatus status = new DroneStatus();
        status.setPartId(UUID.randomUUID().toString());
        droneStatusRepository.save(status);
    }

}
