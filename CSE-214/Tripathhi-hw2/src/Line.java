public class Line
{
    private Person headPerson;
    private Person tailPerson;
    private int length;
    private Line lineLink;
    private static int serialCounter = -3;
    private int serialId;

    public Line()
    {
        serialCounter++;
        serialId = serialCounter;
    }

    public Line getLineLink()
    { return lineLink; }

    public int getSerialId()
    { return serialId; }

    public Person getHeadPerson()
    { return headPerson; }

    public Person getTailPerson()
    { return tailPerson; }

    public void setSerialId(int s)
    {
        serialId = s;
    }

    public void repairSerialId()
    {
        serialCounter--;
    }

    public void setLineLink(Line newLink)
    { this.lineLink = newLink;}

    public int getLength()
    { return length; }

    public void addPerson(Person attendee)
    {
        if(headPerson==null)//checks if there are person in the line and sets the headPerson, tailPerson accordingly
        {
            headPerson=attendee;
            tailPerson=attendee;
        }
        else if(length==1)//check if there is a single person and sets the headPerson, tailPerson accordingly
        {
            if(headPerson.getSeatNumber()>attendee.getSeatNumber())
            {
                attendee.setNextPerson(headPerson);
                headPerson=attendee;
                tailPerson=headPerson.getNextPerson();
            }
            else
            {
                headPerson.setNextPerson(attendee);
                tailPerson=attendee;
            }
        }
        else//adds the person by managing the headPerson, tailPerson and length.
        {
            Person p = headPerson;
            if(p.getSeatNumber()>attendee.getSeatNumber())
            {
                attendee.setNextPerson(p);
                headPerson=attendee;
            }
            else
            {
                Person pfront = headPerson.getNextPerson();
                while(pfront!=null)//while loop to find the seatNumber after which the person shall be added and adds the person there
                {
                    if(pfront.getSeatNumber()>attendee.getSeatNumber())
                    {
                        attendee.setNextPerson(pfront);
                        p.setNextPerson(attendee);
                        break;
                    }
                    else
                    {
                        if(pfront==getTailPerson())
                        {
                            pfront.setNextPerson(attendee);
                            tailPerson=attendee;
                            break;
                        }
                        p=p.getNextPerson();
                        pfront=pfront.getNextPerson();
                    }
                }
            }
        }
        length++;//increases the length after person is added
    }


    public Person removeFrontPerson()
    {
        if(length != 0)
        {
            Person removed = headPerson;

            if(length == 1)
            {
                headPerson = null;
                tailPerson = null;
            }
            else
            {
                headPerson = headPerson.getNextPerson();
            }
            length--;
            return removed;
        }
        return null;
    }

    public String toString()
    {
        String line = "";
        Person tempPerson = headPerson;

        for(int i = 0 ; i < length ; i++)
        {
            line += tempPerson.toString();
            line += "\n";
            tempPerson = tempPerson.getNextPerson();
        }

        return line;
    }
}
