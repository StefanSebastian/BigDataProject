package bigdata.drone.monitor.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author stefansebii@gmail.com
 */
@Entity
public class DroneStatus {
    @Id
    private String partId;
    private String batchId;
    private long timestamp;
    private double altitude;
    private double latitude;
    private double longitude;

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "DroneStatus{" +
                "partId='" + partId + '\'' +
                ", batchId='" + batchId + '\'' +
                ", timestamp=" + timestamp +
                ", altitude=" + altitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
