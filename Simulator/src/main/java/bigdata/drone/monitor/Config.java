package bigdata.drone.monitor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author stefansebii@gmail.com
 */
@Configuration
@ConfigurationProperties(prefix="simulator")
public class Config {
    private double altitudeLowerBound = 0;
    private double altitudeUpperBound = 121;
    private int idLen = 10;
    private int batchIdLen = 7;

    private double deviceFailureChance = 0.01;
    private double directionChangeChance = 0.01;

    public double getAltitudeLowerBound() {
        return altitudeLowerBound;
    }

    public void setAltitudeLowerBound(double altitudeLowerBound) {
        this.altitudeLowerBound = altitudeLowerBound;
    }

    public double getAltitudeUpperBound() {
        return altitudeUpperBound;
    }

    public void setAltitudeUpperBound(double altitudeUpperBound) {
        this.altitudeUpperBound = altitudeUpperBound;
    }

    public int getIdLen() {
        return idLen;
    }

    public void setIdLen(int idLen) {
        this.idLen = idLen;
    }

    public int getBatchIdLen() {
        return batchIdLen;
    }

    public void setBatchIdLen(int batchIdLen) {
        this.batchIdLen = batchIdLen;
    }

    public double getDeviceFailureChance() {
        return deviceFailureChance;
    }

    public void setDeviceFailureChance(double deviceFailureChance) {
        this.deviceFailureChance = deviceFailureChance;
    }

    public double getDirectionChangeChance() {
        return directionChangeChance;
    }

    public void setDirectionChangeChance(double directionChangeChance) {
        this.directionChangeChance = directionChangeChance;
    }
}
