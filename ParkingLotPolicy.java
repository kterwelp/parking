import java.lang.Math;

public class ParkingLotPolicy {

    protected int price;
    protected int discount;

    ParkingLotPolicy()
    {

    }

    ParkingLotPolicy(int p, int d)
    {
        price = p;
        discount = d;
    }

    //Calculates the discounted price for the parking lot group
    //using the current price and the discount
    public float getDiscountedPrice()
    {
        float percentage = 1 - ((float)discount/100);
        return Math.round(percentage * price);
    }
}
