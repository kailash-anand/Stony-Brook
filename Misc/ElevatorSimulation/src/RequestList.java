public class RequestList 
{
    private RequestNode head;
    private RequestNode tail;
    private RequestNode cursor;
    private int size;
    
    public RequestList()
    {
        head = null;
        tail = null;
        cursor = null;
    }

    public void addtoTail(Request newRequest)
    {
        RequestNode newNode = new RequestNode(newRequest);

        if(tail == null)
        {
            head = newNode;
            tail = head;
            cursor = head;
        }
        else
        {
            tail.setLink(newNode);
            tail = newNode;
        }
        size++;
    }

    public void remove()
    {
        head = null;
        tail = null;
        cursor = null;
        size--;
    }

    public void resetCursor()
    { cursor = head; }

    public int size()
    {
        return size;
    }

    public Request[] getAllRequests()
    {
        resetCursor();
        Request[] requests = new Request[size];
        int count = 0;

        while(cursor.getLink() != null)
        {
            requests[count] = cursor.getData();
            count++;
            cursor = cursor.getLink();
        }

        return requests;
    }
}
