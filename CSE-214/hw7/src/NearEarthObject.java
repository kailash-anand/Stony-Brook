/**
 * The NearEarthObject class which represents an object in space.
 *
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #7 for CSE 214
 * Date:
 *    April 30th, 2023
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NearEarthObject
{
    private int referenceId;
    private String name;
    private double absoluteMagnitude;
    private double averageDiameter;
    private boolean isDangerous;
    private Date closestApproach;
    private double missDistance;
    private String orbitingBody;
    private String newDate;

    /**
     * Default Constructor.
     *
     * @param referenceId
     *  Unique the ID of the NEO.
     *  Fetched using the "near_earth_objects/neo_reference_id" identifier.
     * @param name
     *  Unique name of the asteroid or orbital body.
     *  Fetched using the "near_earth_objects/name" identifier.
     * @param absoluteMagnitude
     *  Absolute brightness of the asteroid or orbital body in the sky.
     *  Fetched using the "near_earth_objects/absolute_magnitude_h" identifier.
     * @param minDiameter
     *  Estimated minimum diameter of the asteroid or orbital body in kilometers.
     *  Fetched using the "near_earth_objects/estimated_diameter/kilometers/estimated_diameter_min" identifier.
     * @param maxDiameter
     *  Estimated maximum diameter of the asteroid or orbital body in kilometers.
     *  Fetched using the "near_earth_objects/estimated_diameter/kilometers/estimated_diameter_max" identifier.
     * @param isDangerous
     *  Boolean value indicating whether the asteroid or orbital body is a potential impact threat.
     *  Fetched using the "near_earth_objects/is_potentially_hazardous_asteroid" identifier.
     * @param closestDateTimeStamp
     *  Unix timestamp representing the date of the closest approach.
     *  Fetched using the "near_earth_objects/close_approach_data/epoch_date_close_approach" identifier.
     * @param missDistance
     *  Distance in kilometers at which the asteroid or orbital body will pass by the Earth on the date of it's closest approach.
     *  Fetched using the "near_earth_objects/close_approach_data/miss_distance/kilometers" identifier.
     * @param orbitingBody
     *  Planet or other orbital body which this NEO orbits.
     *  Fetched using the "near_earth_objects/close_approach_data/orbiting_body" identifier.
     *
     * @throws ParseException
     */
    public NearEarthObject(int referenceId, String name, double absoluteMagnitude, double minDiameter, double maxDiameter,
                           boolean isDangerous, long closestDateTimeStamp, double missDistance, String orbitingBody) throws ParseException {
        this.referenceId = referenceId;
        if(name.length() > 26)
        {
            this. name = name.substring(0,26);
        }
        else
        {
            this.name = name;
        }
        this.absoluteMagnitude = absoluteMagnitude;
        this.isDangerous = isDangerous;
        this.missDistance = missDistance;
        this.orbitingBody = orbitingBody;
        this.averageDiameter = (maxDiameter+minDiameter)/2;
        Date date = new Date(closestDateTimeStamp);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-YYYY");
        newDate = format.format(date);
        closestApproach = date;
    }


    public int getReferenceId()
    { return referenceId; }

    public String getName()
    { return name; }

    public double getAverageDiameter()
    { return averageDiameter; }

    public double getAbsoluteMagnitude()
    { return absoluteMagnitude; }

    public double getMissDistance()
    { return missDistance; }

    public Date getClosestApproach()
    { return closestApproach; }

    public String getOrbitingBody()
    { return orbitingBody; }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbsoluteMagnitude(double absoluteMagnitude) {
        this.absoluteMagnitude = absoluteMagnitude;
    }

    public void setAverageDiameter(double averageDiameter) {
        this.averageDiameter = averageDiameter;
    }

    public void setDangerous(boolean dangerous) {
        isDangerous = dangerous;
    }

    public void setClosestApproach(Date closestApproach) {
        this.closestApproach = closestApproach;
    }

    public void setMissDistance(double missDistance) {
        this.missDistance = missDistance;
    }

    public void setOrbitingBody(String orbitingBody) {
        this.orbitingBody = orbitingBody;
    }

    public void setNewDate(String newDate) {
        this.newDate = newDate;
    }

    /**
     * Returns a printable representation of this NearEarthObject.
     *
     * @return
     *  A printable representation of this NearEarthObject.
     */
    public String toString()
    {
        return String.format("%-9d%-28s%-8.1f%-10.3f%-10s%-12s%-12.0f%s",referenceId,name,absoluteMagnitude,
                averageDiameter,isDangerous,newDate,missDistance,orbitingBody);
    }
}
