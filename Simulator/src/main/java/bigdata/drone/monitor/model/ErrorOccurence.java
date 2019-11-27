package bigdata.drone.monitor.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author stefansebii@gmail.com
 */
@Entity
public class ErrorOccurence {
    @Id
    private String partId;
    private ErrorCode code;

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public ErrorCode getCode() {
        return code;
    }

    public void setCode(ErrorCode code) {
        this.code = code;
    }
}
