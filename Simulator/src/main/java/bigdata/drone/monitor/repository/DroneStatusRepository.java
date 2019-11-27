package bigdata.drone.monitor.repository;

import bigdata.drone.monitor.model.DroneStatus;
import org.springframework.data.repository.CrudRepository;

/**
 * @author stefansebii@gmail.com
 */
public interface DroneStatusRepository extends CrudRepository<DroneStatus, String> {

}
