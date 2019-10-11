public class Gates extends Main {
    protected String name;
    protected bool open;
    protected Cars currentCar;

    Gates(String n)
    {
        name = n;
        open = true;
    }

    public bool gateIsOpen()
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

    public void assignCar(Cars newCar)
    {
        currentCar = newCar;
    }
}