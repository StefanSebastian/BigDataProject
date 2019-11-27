package bigdata.drone.monitor.repository;

import bigdata.drone.monitor.model.ErrorOccurence;
import org.springframework.data.repository.CrudRepository;

/**
 * @author stefansebii@gmail.com
 */
public interface ErrorOccurenceRepository extends CrudRepository<ErrorOccurence, String> {
}
