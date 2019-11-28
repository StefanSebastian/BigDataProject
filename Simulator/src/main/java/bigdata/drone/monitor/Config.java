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

    private double latitudeLowerBound = 22;
    private double latitudeUpperBound = 26;
    private double longitudeLowerBound = 43;
    private double longitudeUpperBound = 47;

    private double cardinalChangeChance = 0.1;

    private double distanceOnCoords = 0.00005;

    public double getDistanceOnCoords() {
        return distanceOnCoords;
    }

    public void setDistanceOnCoords(double distanceOnCoords) {
        this.distanceOnCoords = distanceOnCoords;
    }

    public double getCardinalChangeChance() {
        return cardinalChangeChance;
    }

    public void setCardinalChangeChance(double cardinalChangeChance) {
        this.cardinalChangeChance = cardinalChangeChance;
    }

    public double getLatitudeLowerBound() {
        return latitudeLowerBound;
    }

    public void setLatitudeLowerBound(double latitudeLowerBound) {
        this.latitudeLowerBound = latitudeLowerBound;
    }

    public double getLatitudeUpperBound() {
        return latitudeUpperBound;
    }

    public void setLatitudeUpperBound(double latitudeUpperBound) {
        this.latitudeUpperBound = latitudeUpperBound;
    }

    public double getLongitudeLowerBound() {
        return longitudeLowerBound;
    }

    public void setLongitudeLowerBound(double longitudeLowerBound) {
        this.longitudeLowerBound = longitudeLowerBound;
    }

    public double getLongitudeUpperBound() {
        return longitudeUpperBound;
    }

    public void setLongitudeUpperBound(double longitudeUpperBound) {
        this.longitudeUpperBound = longitudeUpperBound;
    }

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
