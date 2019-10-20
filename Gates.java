public class Gates extends Main {
    protected String name;
    protected boolean open;
    protected Cars currentCar;

    Gates(String n)
    {
        name = n;
        open = true;
    }

    //Determines if a gate is open
    public boolean gateIsOpen()
    {
        if (open == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //Assigns a car to the gate
    public void assignCar(Cars newCar)
    {
        currentCar = newCar;
    }
}