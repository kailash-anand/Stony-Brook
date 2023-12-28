public class Person
{
    private String name;
    private int seatNumber;
    private Person nextPerson;

    public Person()
    {}

    public Person(String name, int seatNumber)
    {
        if(name == null || seatNumber < 0)
        {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.seatNumber = seatNumber;
    }

    public String getName()
    { return name; }

    public int getSeatNumber()
    { return seatNumber; }

    public Person getNextPerson()
    { return nextPerson; }

    public void setName(String name)
    {
        if(name == null)
        {
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    public void setSeatNumber(int seatNumber)
    {
        if(seatNumber < 0)
        {
            throw new IllegalArgumentException();
        }

        this.seatNumber = seatNumber;
    }

    public void setNextPerson(Person p)
    {
        if(p == null)
        {
            throw new IllegalArgumentException();
        }

        nextPerson = p;
    }

    public String toString()
    {
        String person = "";
        person += name;
        person += "   |   " + seatNumber;
        return person;
    }
}
