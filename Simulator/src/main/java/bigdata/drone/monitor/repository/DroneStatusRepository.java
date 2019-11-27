package bigdata.drone.monitor.repository;

import bigdata.drone.monitor.model.DroneStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author stefansebii@gmail.com
 */
public interface DroneStatusRepository extends CrudRepository<DroneStatus, String> {
    @Query("SELECT s FROM DroneStatus s WHERE s.timestamp = (SELECT MAX(s1.timestamp) FROM DroneStatus s1)")
    DroneStatus findLastStatus();
}
