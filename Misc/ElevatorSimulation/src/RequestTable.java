public class RequestTable 
{
    private RequestList[] requests;
    private int index;

    public RequestTable(int numFloors)
    {
        requests = new RequestList[numFloors];

        for(int i=0 ; i< requests.length ; i++)
        {
            requests[i] = new RequestList();
        }
    }

    public void add(Request request)
    {
        index = request.getSourceFloor();
        requests[index].addtoTail(request);
    }

    public Request[] getRequests(int sourceFloor)
    {
        Request[] requests = new Request[this.requests[index].size()];
        index = sourceFloor;
        requests = this.requests[index].getAllRequests();
        return requests;
    }
}
    

