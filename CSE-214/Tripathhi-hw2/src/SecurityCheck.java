public class SecurityCheck
{
    private Line headLine;
    private Line tailLine;
    private Line cursorLine;
    private int lineCount;
    private int totalPeople;
    private Line newLine;


    public SecurityCheck()
    {
        headLine = new Line();
        tailLine = new Line();
        cursorLine = new Line();
        newLine = new Line();
        lineCount = 1;
    }

    public Line getCursorLine()
    { return cursorLine; }

    public void addPerson(String name, int seatNumber)
    {
        Person toBeAdded = new Person(name,seatNumber);

        if(headLine == null || lineCount == 1)
        {
            newLine.addPerson(toBeAdded);
            headLine = newLine;
            tailLine = headLine;
            cursorLine = headLine;
        }
        else
        {
            cursorLine = headLine.getLineLink();
            Line minLine = headLine;

            while(cursorLine != null)
            {
                if(cursorLine.getLength() < minLine.getLength())
                {
                    minLine = cursorLine;
                }

                cursorLine = cursorLine.getLineLink();
            }

            minLine.addPerson(toBeAdded);

        }
        totalPeople++;
    }

    public Person removeNextAttendee() {
        cursorLine = headLine;
        boolean allEmpty = true;//check the exception case
        while(cursorLine != null)//loop to traverse the lines and check if person is there in the line
        {
            if(cursorLine.getLength() != 0)
                allEmpty = false;

            cursorLine = cursorLine.getLineLink();
        }


        int lineCursor=1;// cursor to have a look at the line where the person is to be removed
        cursorLine = headLine;
        Line minLine = cursorLine;//to make implementation easy and have a look at the line having minimum people

        while(minLine.getLength() == 0)//line with minimum people
            minLine = minLine.getLineLink();

        while(cursorLine != null)//loop to traverse through the lines to find minimum people
        {
            if(cursorLine.getLength() == 0)
            {
                cursorLine = cursorLine.getLineLink();
                continue;
            }

            if(cursorLine.getHeadPerson().getSeatNumber() < minLine.getHeadPerson().getSeatNumber())
                minLine = cursorLine;

            cursorLine = cursorLine.getLineLink();
        }

        Person needRemovep = minLine.removeFrontPerson();//remove the front person form the line with minimum people
        System.out.println(needRemovep.getName() + " from seat 1 removed from line " + lineCursor);
        return needRemovep;
    }

    public void addNewLines(int newLines)
    {
        for(int i=0 ; i<newLines ; i++)
        {
            Line newLine = new Line();
            tailLine.setLineLink(newLine);
            tailLine = newLine;
        }

        int totalLines = newLines + lineCount;
        int toTransfer = 0;

        if(totalPeople <= totalLines)
        {
            toTransfer = totalPeople - lineCount;
            cursorLine = headLine;

            if(toTransfer <= 0)
            { return; }

            System.out.println(lineCount);

            Person[] transfer = new Person[toTransfer];
            for(int i=0 ; i< transfer.length ; i++)
            {
                transfer[i] = new Person();
            }

            for(int i=0 ; i<lineCount ; i++)
            {
               while(cursorLine.getLength() != 1)
               {
                   transfer[i] = cursorLine.removeFrontPerson();
               }
                cursorLine = cursorLine.getLineLink();
            }

            for(int i=0 ; i<toTransfer ; i++)
            {
                cursorLine.addPerson(transfer[i]);
                cursorLine = cursorLine.getLineLink();
            }
        }
        else if(totalPeople > totalLines)
        {
            int remainder = totalPeople%totalLines;

            int peoplePerLine = (totalPeople-remainder)/totalLines;
            cursorLine = headLine;
            Line dummyCursor = new Line();
            dummyCursor.repairSerialId();

            dummyCursor = headLine;

            for(int i=0 ; i<lineCount ; i++)
            {
                dummyCursor = dummyCursor.getLineLink();
            }
            int step = peoplePerLine;

            for(int i=0 ; i<lineCount ; i++)
            {
                System.out.println(tailLine.getLength() + "\n");
                while(cursorLine.getLength() > peoplePerLine)
                {
                    if(step == 0 && dummyCursor.getLineLink() != null)
                    {
                        dummyCursor = dummyCursor.getLineLink();
                        step = peoplePerLine;
                    }

                    dummyCursor.addPerson(cursorLine.removeFrontPerson());
                    step--;
                }
                cursorLine = cursorLine.getLineLink();
            }

            cursorLine = headLine;
            while(tailLine.getLength() != peoplePerLine && cursorLine.getLineLink() != null)
            {
                cursorLine.addPerson(tailLine.removeFrontPerson());
                cursorLine = cursorLine.getLineLink();
            }
        }
        lineCount = totalLines;
    }

    public void removeLines(int[] removedLines)
    {
        if(lineCount == 1)
        {
            //throw exception
        }

        for(int i=0 ; i <removedLines.length ; i++)
        {
            if(removedLines[i] > lineCount)
            {
                //throw exception
            }
        }

        for(int i=0 ; i<removedLines.length ; i++)
        {
            Line dummyCursor = new Line();
            Line minLine;
            minLine = headLine;
            minLine.repairSerialId();
            dummyCursor.repairSerialId();

            cursorLine = headLine;
            Line prev = new Line();
            prev = cursorLine;
            prev.repairSerialId();

            if(cursorLine.getLineLink() != null)
            {cursorLine = cursorLine.getLineLink();}

            int current = removedLines[i];

            while(prev.getSerialId() != current)
            {
                cursorLine = cursorLine.getLineLink();
                prev = prev.getLineLink();
            }

            if(prev == tailLine)
            {
                cursorLine = tailLine;
            }

            while(cursorLine.getLength() != 0)
            {
                dummyCursor = headLine.getLineLink();
                minLine = headLine;

                if(cursorLine == tailLine)
                {
                    while(dummyCursor != tailLine)
                    {
                        if(dummyCursor.getLength() < minLine.getLength())
                        {
                            minLine = dummyCursor;
                        }
                        dummyCursor = dummyCursor.getLineLink();
                    }
                    minLine.addPerson(cursorLine.removeFrontPerson());
                }
                else
                {
                    while(dummyCursor != null)
                    {
                        if(dummyCursor == cursorLine && dummyCursor!=tailLine)
                        {
                            dummyCursor = dummyCursor.getLineLink();
                        }

                        if(dummyCursor.getLength() < minLine.getLength())
                        {
                            minLine = dummyCursor;
                        }
                        dummyCursor = dummyCursor.getLineLink();
                    }
                    minLine.addPerson(cursorLine.removeFrontPerson());
                }
            }

            if(cursorLine == tailLine)
            {
                cursorLine = headLine;
                while(cursorLine != tailLine)
                {
                    cursorLine = cursorLine.getLineLink();
                }
                cursorLine.setLineLink(null);
                tailLine = cursorLine;
            }
            else
            { prev.setLineLink(cursorLine.getLineLink()); }

            for(int j=0 ; j<removedLines.length ; j++)
            { cursorLine.repairSerialId(); }

            repairId();
        }
        lineCount = lineCount - removedLines.length;
    }

    public String toString()
    {
        String print = "";
        cursorLine = headLine;
        print = cursorLine.toString();

        return print;
    }

    public void showLineStatus()
    {
        cursorLine = headLine;

        for(int i=0 ; i< lineCount ; i++)
        {
            System.out.print("Line " + cursorLine.getSerialId() + ": " + cursorLine.getLength() + " people waiting.\n" );
            cursorLine = cursorLine.getLineLink();
        }
    }

    public void repairId()
    {
        cursorLine = headLine;
        int count = 1;

        while(cursorLine != null)
        {
            cursorLine.setSerialId(count);
            count++;

            cursorLine = cursorLine.getLineLink();
        }
    }

    public void print()//print the people which actually exits in the line
    {
        System.out.print("    Line    |        Name                |    Seat Number           |  ");
        System.out.println();
        System.out.println("=====================================================================");
        //loop helps to print the people array
        cursorLine=headLine;
        int line=1;
        while(cursorLine!=null)
        {
            Person p = cursorLine.getHeadPerson();
            for(int i=0 ; i< cursorLine.getLength() ; i++)
            {
                System.out.print("      " +    line     + "     |        " +    p.getName() + "                |      " +    p.getSeatNumber() + "                   |");
                System.out.println();
                p=p.getNextPerson();
            }
            cursorLine=cursorLine.getLineLink();
            line++;
        }
    }
}

