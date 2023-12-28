/**
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #4 for CSE 214
 * Date:
 *    March 9th, 2023
 */
public class VehicleNode
{
    private Vehicle vehicle;
    private VehicleNode link;

    public VehicleNode(Vehicle newVehicle)
    {
        if(newVehicle == null)
        {
            throw new IllegalArgumentException();
        }

        vehicle = newVehicle;
    }

    public Vehicle getVehicle()
    {
        return vehicle;
    }

    public void setVehicle(Vehicle newVehicle)
    {
        if(newVehicle == null)
        {
            throw new IllegalArgumentException();
        }

        vehicle = newVehicle;
    }

    public VehicleNode getLink()
    {
        return link;
    }

    public void setLink(VehicleNode newLink)
    {
        link = newLink;
    }
}
