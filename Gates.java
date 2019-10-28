public class Gates {

    private String name;
    private boolean open;
    private Cars currentCar;

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

    //Sets the current car for the gate
    public void setCar(Cars car)
    {
        currentCar = car;
    }

    //Sets the current status for the gate
    public void setStatus(boolean status)
    {
        open = status;
    }
}
