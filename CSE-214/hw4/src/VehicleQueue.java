/**
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #4 for CSE 214
 * Date:
 *    March 21st, 2023
 */
public class VehicleQueue
{
    private VehicleNode front;
    private VehicleNode rear;

    /**
     * Queue constructor
     */
    public VehicleQueue()
    {
        front = null;
        rear = null;
    }

    /**
     *  Checks if the queue is empty
     *
     * @return
     *  true if the queue is empty
     */
    public boolean isEmpty()
    {
        return (front == null);
    }

    /**
     *  Enqueues a new vehicle to the queue
     *
     * @param newVehicle
     *  The vehicle to be enqueued
     */
    public void enqueue(Vehicle newVehicle)
    {
       VehicleNode newNode = new VehicleNode(newVehicle);

       if(isEmpty())
       {
           front = newNode;
       }
       else
       {
           rear.setLink(newNode);
       }
        rear = newNode;
    }

    /**
     * Dequeues and return a vehicle
     *
     * @return
     *  The vehicle dequeued
     */
    public Vehicle dequeue()
    {
        Vehicle v;
        if(!isEmpty())
        {
            v = front.getVehicle();
            front = front.getLink();
            return v;
        }
        return null;
    }
}
