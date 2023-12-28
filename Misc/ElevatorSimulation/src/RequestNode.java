public class RequestNode 
{
    private Request data;
    private RequestNode link;

    public RequestNode(Request request)
    {
        data = request;
        link = null;
    }

    public RequestNode getLink()
    {
        return link;
    }

    public void setLink(RequestNode newLink)
    {
        link = newLink;
    }

    public Request getData()
    {
        return data;
    }

    public void setData(Request newData)
    {
        data = newData;
    }
}
    

