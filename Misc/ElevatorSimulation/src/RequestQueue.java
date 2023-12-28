
public class RequestQueue
{
    private final int CAPACITY = 10;
    private Request[] requests;
    private int front;
    private int rear;
    private String display = "";

    public RequestQueue()
    {
        front = rear = -1;
        requests = new Request[CAPACITY];
    }

    public void enqueue(Request newRequest)
    {
        if(newRequest == null)
        {
            throw new IllegalArgumentException("Invalid Input");
        }

        if((rear+1)%CAPACITY == front)
        {
            ensureCapacity();
        }

        if(rear == -1)
        {
            rear = 0;
            front = 0;
        }
        else
        { rear = (rear+1)%CAPACITY; }

        requests[rear] = newRequest;
        display += newRequest.toString() + "\n";
    }

    public Request dequeue()
    {
        if(isEmpty())
        {
            return null;
        }

        Request dequeued = requests[front];

        if(front == rear)
        { front = rear = -1; }
        else
        { front = (front+1)%CAPACITY; }
        return dequeued;
    }

    public boolean isEmpty()
    {
        if(front == -1)
        { return true; }
        else
        { return false; }
    }

    public void ensureCapacity()
    {
        Request increasedRequests[] = new Request[CAPACITY*2 + 1];
        System.arraycopy(requests,0,increasedRequests,0,CAPACITY);
        requests = increasedRequests;
    }

    public String toString()
    {
        return display;
    }
}
