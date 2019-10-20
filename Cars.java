
public class Cars {

    private int id;
    private int timeStamp;
    
    Cars(int tS, int identification)
    {
        id = identification;
        timeStamp = tS;
    }

    //Returns the car's time stamp
    public int getTimestamp()
    {
        return timeStamp;
    }

    //Sets the car's time stamp
    public void setTimestamp(int ts)
    {
        timeStamp = ts;
    }

    //Returns the car's ID
    public int getID()
    {
        return id;
    }
}