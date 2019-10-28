import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main extends ParkingLotPolicy{

    private final static int capacity = 5;
    private Cars parkingSpots[];
    private float profitTotal;
    private Gates enterGate1;
    private Gates enterGate2;
    private Gates exitGate1;
    private Gates exitGate2;

    //These statements initiate array lists and queues
    //carsArray - holds all cars entering the parking lot
    //carsEnterQueue - holds all cars waiting to get in the parking lot when
    //                 an entrance gate is not available
    //carsLeaveQueue - holds all cars waiting to leave the parking lot when
    //                 and exit gate is not available
    private ArrayList<Cars> carsArray;
    private Queue<Cars> carsEnterQueue;
    private Queue<Cars> carsLeaveQueue;
    private int timeCount;

    Main()
    {
        carsArray = new ArrayList<Cars>();
        carsEnterQueue = new LinkedList<Cars>();
        timeCount = 0;
    }

    Main(int price, int discount)
    {
        super(price, discount);
        parkingSpots = new Cars[capacity];
        enterGate1 = new Gates("Entrance");
        enterGate2 = new Gates("Entrance");
        exitGate1 = new Gates("Exit");
        exitGate2 = new Gates("Exit");
        carsLeaveQueue = new LinkedList<Cars>();
    }

    //Determines if the parking lot has a space open
    public boolean parkingSpaceIsOpen()
    {
        for (int i = 0; i < capacity; i++)
        {
            if (parkingSpots[i] == null)
            {
                return true;
            }
        }

        return false;
    }

    //Determines if the parking lot is empty
    public boolean parkingLotIsEmpty()
    {
        for (int i = 0; i < capacity; i++)
        {
            if (parkingSpots[i] == null)
            {
                continue;
            }
            else
            {
                return false;
            }
        }

        return true;
    }

    //Determines if all the parking lots are empty
    public boolean allParkingLotsEmpty(ArrayList<Main> parkingLots)
    {
        for (int i = 0; i < parkingLots.size(); i++)
        {
            if (!parkingLots.get(i).parkingLotIsEmpty())
            {
                return false;
            }
        }

        return true;
    }

    //Determines if all parking lot leave queues are empty
    public boolean allParkingLotLeaveQueuesEmpty(ArrayList<Main> parkingLots)
    {
        for (int i = 0; i < parkingLots.size(); i++)
        {
            if (!parkingLots.get(i).carsLeaveQueue.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    //Updates the entrance and exit gates for the park lot passed to the function
    public void updateGates(Main parkingLot)
    {
        //These if-else statements empty the entrance and exit gates
        if (!parkingLot.enterGate1.gateIsOpen())
        {
            parkingLot.enterGate1.setStatus(true);
            parkingLot.enterGate1.setCar(null);
        }

        if (!parkingLot.enterGate2.gateIsOpen())
        {
            parkingLot.enterGate2.setStatus(true);
            parkingLot.enterGate2.setCar(null);
        }

        if (!parkingLot.exitGate1.gateIsOpen())
        {
            parkingLot.exitGate1.setStatus(true);
            parkingLot.exitGate1.setCar(null);
        }

        if (!parkingLot.exitGate2.gateIsOpen())
        {
            parkingLot.exitGate2.setStatus(true);
            parkingLot.exitGate2.setCar(null);
        }
    }

    //Updates the leave queue for the parking lot passed to the function
    public void updateLeaveQueue(Main parkingLot)
    {
        //These if-else statements assign cars waiting in the carsLeaveQueue to any open exit gates
        //The payment is calculated if the car is assigned to an exit gate
        //Otherwise they are assigned to the carsLeaveQueue
        if (!parkingLot.carsLeaveQueue.isEmpty())
        {
            if (parkingLot.exitGate1.gateIsOpen())
            {
                parkingLot.exitGate1.assignCar(parkingLot.carsLeaveQueue.peek());
                parkingLot.calculatePayment(parkingLot.carsLeaveQueue.peek(), timeCount);
                this.removeCarFromArray(this, parkingLot.carsLeaveQueue.peek().getID());
                parkingLot.carsLeaveQueue.remove();
                parkingLot.exitGate1.setStatus(false);
            }
            else
            {
                if (parkingLot.exitGate2.gateIsOpen())
                {
                    parkingLot.exitGate2.assignCar(parkingLot.carsLeaveQueue.peek());
                    parkingLot.calculatePayment(parkingLot.carsLeaveQueue.peek(), timeCount);
                    this.removeCarFromArray(this, parkingLot.carsLeaveQueue.peek().getID());
                    parkingLot.carsLeaveQueue.remove();
                    parkingLot.exitGate2.setStatus(false);
                }
            }

        }

        if (!parkingLot.carsLeaveQueue.isEmpty() && parkingLot.exitGate2.gateIsOpen())
        {
            parkingLot.exitGate2.assignCar(parkingLot.carsLeaveQueue.peek());
            parkingLot.calculatePayment(parkingLot.carsLeaveQueue.peek(), timeCount);
            parkingLot.carsLeaveQueue.remove();
            parkingLot.exitGate2.setStatus(false);
        }
    }

    //Places an arriving car at an entrance gate and parking spot for parking lot
    //and arriving car passed to function
    public boolean arrivingCar(Main parkingLot, Cars car) {
        if (parkingLot.enterGate1.gateIsOpen()) {
            car.setTimestamp(timeCount);
            parkingLot.enterGate1.assignCar(car);
            parkingLot.assignCar(car);
            parkingLot.enterGate1.setStatus(false);
            return true;
        } else if (parkingLot.enterGate2.gateIsOpen()) {
            car.setTimestamp(timeCount);
            parkingLot.enterGate2.assignCar(car);
            parkingLot.assignCar(car);
            parkingLot.enterGate2.setStatus(false);
            return true;
        } else {
            return false;
        }
    }

    //Places a leaving car at an exit gate or in the leave queue if the exit gate
    //is taken - removes the car from the parking spot based on parking lot and
    //car ID passed to function
    //Each parking lot has its own leave queue
    public void leavingCar(Main parkingLot, int iD) {
        if(parkingLot.exitGate1.gateIsOpen())
        {
            Cars exitCar = parkingLot.findCar(iD);
            if (exitCar != null)
            {
                parkingLot.exitGate1.assignCar(exitCar);
                parkingLot.calculatePayment(exitCar, timeCount);
                removeCarFromArray(this, iD);
                parkingLot.removeCar(iD);
            }

        }
        else if (parkingLot.exitGate2.gateIsOpen())
        {
            Cars exitCar = parkingLot.findCar(iD);

            if (exitCar != null)
            {
                parkingLot.exitGate2.assignCar(exitCar);
                parkingLot.calculatePayment(exitCar, timeCount);
                removeCarFromArray(this, iD);
                parkingLot.removeCar(iD);
            }

        }
        else
        {
            Cars exitCar = parkingLot.findCar(iD);

            if (exitCar != null)
            {
                parkingLot.carsLeaveQueue.add(exitCar);
                parkingLot.removeCar(iD);
            }

        }
    }

    //Attempts to find a car in the parking lot spots and returns Cars object
    public Cars findCar(int id)
    {
        for (int i = 0; i < capacity; i++)
        {
            if (parkingSpots[i] != null && id == parkingSpots[i].getID())
            {
                return parkingSpots[i];
            }
        }

        return null;
    }

    //Attempts to find a car with specific id in the parking lot spots and returns boolean
    public boolean iDSearch(int id)
    {
        for (int i = 0; i < capacity; i++)
        {
            if (parkingSpots[i] != null && id == parkingSpots[i].getID())
            {
                return true;
            }
        }

        return false;
    }

    //Attempts to find any remaining cars in the parking lot and returns one
    public Cars findRemainingCars()
    {
        for (int i = 0; i < capacity; i++)
        {
            if (parkingSpots[i] != null)
            {
                return parkingSpots[i];
            }
        }

        return null;
    }

    //Assigns any remaining cars to a parking lot exit gate and removes it from parking
    //lot for any parking lot passed to function
    public void assignRemainingCars(Main parkingLot)
    {
        if (parkingLot.findRemainingCars() != null)
        {
            Cars remainingCar = parkingLot.findRemainingCars();
            parkingLot.removeCar(remainingCar.getID());

            if(exitGate1.gateIsOpen())
            {
                exitGate1.assignCar(remainingCar);
                this.removeCarFromArray(this, remainingCar.getID());
                parkingLot.calculatePayment(remainingCar, timeCount);
            }
            else
            {
                if (exitGate2.gateIsOpen())
                {
                    exitGate2.assignCar(remainingCar);
                    this.removeCarFromArray(this, remainingCar.getID());
                    parkingLot.calculatePayment(remainingCar, timeCount);
                }
                else
                {
                    carsLeaveQueue.add(remainingCar);
                }
            }
        }

        if (parkingLot.findRemainingCars() != null && exitGate2.gateIsOpen())
        {
            Cars remainingCar = parkingLot.findRemainingCars();
            parkingLot.removeCar(remainingCar.getID());
            removeCarFromArray(this, remainingCar.getID());
            exitGate2.assignCar(remainingCar);
            parkingLot.calculatePayment(remainingCar, timeCount);
        }
    }

    //Assigns a car to an open parking lot spot
    public void assignCar(Cars newCar)
    {
        for (int i = 0; i < capacity; i++)
        {
            if (parkingSpots[i] == null)
            {
                parkingSpots[i] = newCar;
                return;
            }
        }
    }

    //Removes a car from a parking lot spot
    public void removeCar(int id)
    {
        for (int i = 0; i < capacity; i++)
        {
            if (parkingSpots[i] != null && id == parkingSpots[i].getID())
            {
                parkingSpots[i] = null;
                return;
            }
        }
    }

    //Removes cars from the carsArray that are added when the car arrives
    //to a parking lot
    //The carsArray is used to determine if the car is already present in case
    //a car with the same ID arrives or if a car that has not arrived tries to leave
    public void removeCarFromArray(Main parkingLotProgram, int iD)
    {
        for (int i = 0; i < parkingLotProgram.carsArray.size(); i++)
        {
            if (parkingLotProgram.carsArray.get(i).getID() == iD)
            {
                parkingLotProgram.carsArray.remove(i);
                return;
            }
        }
    }

    //Calculates the payment for cars at parking lot exit gates
    //This function receives the discounted price from the ParkingLotPolicy class
    public void calculatePayment(Cars curCar, int tC)
    {
        profitTotal += (tC - curCar.getTimestamp()) * (getDiscountedPrice()/100);
    }

    //Returns the total amount collected for the parking lot
    public float getProfitTotal()
    {
        return profitTotal;
    }

    public float getAllLotsProfitTotal(ArrayList<Main> parkingLots)
    {
        float allLotsProfit = 0;

        for (int i = 0; i < parkingLots.size(); i++)
        {
            allLotsProfit += parkingLots.get(i).getProfitTotal();
        }

        return allLotsProfit;
    }

    //Determines if a number is an integer - used to check if input is
    //a valid car ID
    public static boolean isInt(String str)
    {
        try
        {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe)
        {
            return false;
        }
    }

    public static void main(String[] args) {

        Main parkingLotProgram = new Main();

        float minPrice;
        float midPrice;
        float maxPrice;

        //These statements initialize the parkingLot, enter gates and exit gates
        Main parkingLotGroup1[] = new Main[2];

        for (int i = 0; i < parkingLotGroup1.length; i++)
        {
            parkingLotGroup1[i] = new Main(100, 10);
        }

        Main parkingLotGroup2[] = new Main[2];

        for (int j = 0; j < parkingLotGroup1.length; j++)
        {
            parkingLotGroup2[j] = new Main(150, 20);
        }

        //The following if-else statements determine which parking lot group has the lowest price
        //They are assigned to the variables minPrice, midPrice and maxPrice
        if (parkingLotGroup1[0].getDiscountedPrice() < parkingLotGroup2[0].getDiscountedPrice())
        {
            minPrice = parkingLotGroup1[0].getDiscountedPrice();
            maxPrice = parkingLotGroup2[0].getDiscountedPrice();
        }
        else
        {
            minPrice = parkingLotGroup2[0].getDiscountedPrice();
            maxPrice = parkingLotGroup1[0].getDiscountedPrice();
        }

        Main parkingLotGroup3[] = new Main[2];

        for (int m = 0; m < parkingLotGroup1.length; m++)
        {
            parkingLotGroup3[m] = new Main(200, 60);
        }

        if (parkingLotGroup3[0].getDiscountedPrice() < minPrice)
        {
            midPrice = minPrice;
            minPrice = parkingLotGroup3[0].getDiscountedPrice();
        }
        else if (parkingLotGroup3[0].getDiscountedPrice() > minPrice &&
                parkingLotGroup3[0].getDiscountedPrice() < maxPrice)
        {
            midPrice = parkingLotGroup3[0].getDiscountedPrice();
        }
        else
        {
            midPrice = maxPrice;
            maxPrice = parkingLotGroup3[0].getDiscountedPrice();
        }

        //Parking lots in this array are ordered from lowest price
        //to highest price
        ArrayList<Main> parkingLotArray = new ArrayList<Main>();

        //These if-else statements place the parking lot groups inside the
        //parkingLotArray in order of price from lowest to highest
        if (parkingLotGroup1[0].getDiscountedPrice() == minPrice)
        {
            parkingLotArray.add(parkingLotGroup1[0]);
            parkingLotArray.add(parkingLotGroup1[1]);
        }
        else if (parkingLotGroup2[0].getDiscountedPrice() == minPrice)
        {
            parkingLotArray.add(parkingLotGroup2[0]);
            parkingLotArray.add(parkingLotGroup2[1]);
        }
        else
        {
            parkingLotArray.add(parkingLotGroup3[0]);
            parkingLotArray.add(parkingLotGroup3[1]);
        }

        if (parkingLotGroup1[0].getDiscountedPrice() == midPrice)
        {
            parkingLotArray.add(parkingLotGroup1[0]);
            parkingLotArray.add(parkingLotGroup1[1]);
        }
        else if (parkingLotGroup2[0].getDiscountedPrice() == midPrice)
        {
            parkingLotArray.add(parkingLotGroup2[0]);
            parkingLotArray.add(parkingLotGroup2[1]);
        }
        else
        {
            parkingLotArray.add(parkingLotGroup3[0]);
            parkingLotArray.add(parkingLotGroup3[1]);
        }

        if (parkingLotGroup1[0].getDiscountedPrice() == maxPrice)
        {
            parkingLotArray.add(parkingLotGroup1[0]);
            parkingLotArray.add(parkingLotGroup1[1]);
        }
        else if (parkingLotGroup2[0].getDiscountedPrice() == maxPrice)
        {
            parkingLotArray.add(parkingLotGroup2[0]);
            parkingLotArray.add(parkingLotGroup2[1]);
        }
        else
        {
            parkingLotArray.add(parkingLotGroup3[0]);
            parkingLotArray.add(parkingLotGroup3[1]);
        }

        String line = "";
        String fileString = "";

        try
        {
            //This is the input file that is used for the program
            File file = new File("input1.txt");

            fileString = file.toString();

            Scanner sc = new Scanner(file);

            //This while loop goes line by line through the file until the end of the file
            //One iteration of this while loop is a minute of time for this program
            while (sc.hasNextLine())
            {
                line = sc.nextLine();

                int iDIndex = 0;
                String iDStrArray[] = new String[10];
                int iDIntArray[] = new int[10];

                //These if else statements parse the file's lines and determine incorrect input
                if (line.contains("arrive") && line.length() > 6)
                {
                    String prefix = "arrive ";
                    String newLine = line.substring(line.indexOf(prefix) + prefix.length());

                    String[] tokens = newLine.split(" ");

                    int m = 0;

                    for (String str : tokens)
                    {
                        iDStrArray[m] = str;
                        m++;
                    }

                    iDIndex = m;

                }
                else if (line.contains("leave") && line.length() > 5)
                {
                    String prefix = "leave ";
                    String newLine = line.substring(line.indexOf(prefix) + prefix.length());

                    String[] tokens = newLine.split(" ");

                    int m = 0;

                    for (String str : tokens)
                    {
                        iDStrArray[m] = str;
                        m++;
                    }

                    iDIndex = m;
                }
                else
                {
                    System.out.println("ERROR: This line of the file is invalid: " + line);
                }

                int iDIntIndex = 0;

                //This for loops puts valid car IDs inside the iDIntArray
                for (int i = 0; i < iDIndex; i++)
                {
                    if (isInt(iDStrArray[i]))
                    {
                        iDIntArray[iDIntIndex] = Integer.parseInt(iDStrArray[i]);
                        iDIntIndex++;
                    }
                    else
                    {
                        System.out.println("ERROR: This ID for the car is invalid: " + iDStrArray[i]);
                    }
                }

                //This for loop updates all gates for the parking lots
                for (int n = 0; n < parkingLotArray.size(); n++)
                {
                    parkingLotProgram.updateGates(parkingLotArray.get(n));
                }

                //These if-else statements assign cars waiting in the carsEnterQueue to any open entrance gates
                //They only assign them to the open entrance gates if there is a parking spot available
                //The cars assigned to an entrance gate will get a time stamp
                if (!parkingLotProgram.carsEnterQueue.isEmpty())
                {
                    for (int n = 0; n < parkingLotArray.size(); n++)
                    {
                        //Two if statements used since there are 2 entrance gates for each parking lot
                        if (parkingLotArray.get(n).parkingSpaceIsOpen() && !parkingLotProgram.carsEnterQueue.isEmpty() &&
                        parkingLotProgram.arrivingCar(parkingLotArray.get(n), parkingLotProgram.carsEnterQueue.peek())) {
                            parkingLotProgram.carsEnterQueue.remove();
                        }

                        if (parkingLotArray.get(n).parkingSpaceIsOpen() && !parkingLotProgram.carsEnterQueue.isEmpty() &&
                        parkingLotProgram.arrivingCar(parkingLotArray.get(n), parkingLotProgram.carsEnterQueue.peek())) {
                            parkingLotProgram.carsEnterQueue.remove();
                        }
                    }
                }

                //This for loop updates the parking lot leave queues
                for (int n = 0; n < parkingLotArray.size(); n++)
                {
                    parkingLotProgram.updateLeaveQueue(parkingLotArray.get(n));
                }

                //If the idIntIndex is empty, there are no valid IDs in the current line of input and the input is invalid
                //Therefore, the rest of the code is skipped, the time is incremented and the while loop starts over
                //for the next line of input
                if (iDIntIndex == 0)
                {
                    System.out.println("ERROR: This line of the file is invalid: " + line);
                    parkingLotProgram.timeCount++;
                    continue;
                }

                //This for loop determines where to put the new cars arriving or leaving from the line of input
                //The for loop iterations are equal to the number of valid IDs in the input
                //The time does not increment in the for loop since each line of input equals a minute of time
                for (int k = 0; k < iDIntIndex; k++)
                {
                    //If input line states "arrive", the code will enter the first if statement
                    //The car is assigned to an enter gate and given a time stamp if the enter gate
                    //is open and a parking spot is available.  Otherwise, it is assigned to the
                    //carsEnterQueue until an entrance gate and parking spot are open
                    if (line.contains("arrive"))
                    {
                        boolean assigned = false;
                        boolean carPresent = false;
                        int identification = iDIntArray[k];

                        //This for loop determines if the arriving car is already present
                        //in a parking lot
                        for (int s = 0; s < parkingLotProgram.carsArray.size(); s++)
                        {
                            if (parkingLotProgram.carsArray.get(s).getID() == identification) {
                                carPresent = true;
                                break;
                            }
                        }

                        if (!carPresent)
                        {
                            for (int p = 0; p < parkingLotArray.size(); p++)
                            {
                                if (parkingLotArray.get(p).parkingSpaceIsOpen()) {
                                    if (parkingLotArray.get(p).enterGate1.gateIsOpen() ||
                                            parkingLotArray.get(p).enterGate2.gateIsOpen())
                                    {
                                        parkingLotProgram.carsArray.add(new Cars(parkingLotProgram.timeCount, identification));
                                        parkingLotProgram.arrivingCar(parkingLotArray.get(p),
                                                parkingLotProgram.carsArray.get(parkingLotProgram.carsArray.size()-1));
                                        assigned = true;
                                        break;
                                    }
                                }
                            }

                            //This if statement will only execute if the car was not assigned to a parking lot
                            //in the previous for loop
                            if (!assigned)
                            {
                                parkingLotProgram.carsArray.add(new Cars(0, identification));
                                parkingLotProgram.carsEnterQueue.add(
                                        parkingLotProgram.carsArray.get(parkingLotProgram.carsArray.size()-1));
                            }
                        }
                        else
                        {
                            System.out.println("ERROR: The car with id " + identification + " is already parked.");
                        }

                    }
                    //If input line states "leave", the code will enter this else if statement
                    //The car (found by ID) is assigned to an exit gate, removed from the parking lot
                    //and the payment is calculated if an exit gate is open.  Otherwise, the car is
                    //assigned to the carsLeaveQueue until an exit gate is open
                    else if (line.contains("leave"))
                    {
                        int identification = iDIntArray[k];
                        boolean carPresent = false;

                        //This for loop determines if the car leaving is present in a parking lot before
                        //attempting to remove it
                        for (int a = 0; a < parkingLotProgram.carsArray.size(); a++)
                        {
                            if (parkingLotProgram.carsArray.get(a).getID() == identification) {
                                carPresent = true;
                                break;
                            }
                        }

                        if (carPresent)
                        {
                            for (int r = 0; r < parkingLotArray.size(); r++)
                            {
                                if (parkingLotArray.get(r).iDSearch(identification)) {
                                    if (parkingLotArray.get(r).exitGate1.gateIsOpen() ||
                                            parkingLotArray.get(r).exitGate2.gateIsOpen())
                                    {
                                        parkingLotProgram.leavingCar(parkingLotArray.get(r), identification);
                                    }
                                }
                            }
                        }
                        else
                        {
                            System.out.println("ERROR: The car with id " + identification + " cannot leave since it is not parked.");
                        }

                    }
                }

                //This increments the time by 1 minute before the while loop starts over
                parkingLotProgram.timeCount++;


            }

            sc.close();

        }
        //This will catch an exception if the file is not found and the program ends.
        catch (IOException e)
        {
            System.out.println("ERROR: File \"" + fileString + "\" cannot be found. Terminating program...");
            System.exit(0);
        }

        //This while loop will run code similar to the code above to make certain the enter and
        //leave queues are empty before the program ends.
        //One iteration of this while loop is a minute of time for this program.
        while (!parkingLotProgram.carsEnterQueue.isEmpty() || !parkingLotProgram.allParkingLotsEmpty(parkingLotArray) ||
                !parkingLotProgram.allParkingLotLeaveQueuesEmpty(parkingLotArray))
        {
            for (int n = 0; n < parkingLotArray.size(); n++)
            {
                parkingLotProgram.updateGates(parkingLotArray.get(n));
            }

            //These if-else statements assign cars waiting in the carsEnterQueue to any open entrance gates
            //They only assign them to the open entrance gates if there is a parking spot available
            //The cars assigned to an entrance gate will get a time stamp
            if (!parkingLotProgram.carsEnterQueue.isEmpty())
            {
                for (int n = 0; n < parkingLotArray.size(); n++)
                {
                    //Two if statements used since there are 2 entrance gates for each parking lot
                    if (parkingLotArray.get(n).parkingSpaceIsOpen() && !parkingLotProgram.carsEnterQueue.isEmpty() &&
                            parkingLotProgram.arrivingCar(parkingLotArray.get(n), parkingLotProgram.carsEnterQueue.peek())) {
                        parkingLotProgram.carsEnterQueue.remove();
                    }

                    if (parkingLotArray.get(n).parkingSpaceIsOpen() && !parkingLotProgram.carsEnterQueue.isEmpty() &&
                            parkingLotProgram.arrivingCar(parkingLotArray.get(n), parkingLotProgram.carsEnterQueue.peek())) {
                        parkingLotProgram.carsEnterQueue.remove();
                    }
                }
            }

            for (int n = 0; n < parkingLotArray.size(); n++)
            {
                parkingLotProgram.updateLeaveQueue(parkingLotArray.get(n));
            }

            for (int x = 0; x < parkingLotArray.size(); x++)
            {
                parkingLotProgram.assignRemainingCars(parkingLotArray.get(x));
            }

            parkingLotProgram.timeCount++;
        }

        System.out.println("\n\nWELCOME TO KEVIN TERWELP'S PARKING LOT");
        System.out.println("\n****************************************************");

        for (int y = 0; y < parkingLotArray.size(); y++)
        {
            int parkingLotNo = y + 1;
            System.out.printf("\n\nTHE TOTAL PROFIT FOR PARKING LOT " + parkingLotNo + " IS $%.2f.",
                    parkingLotArray.get(y).getProfitTotal());
        }

        System.out.printf("\n\nTHE TOTAL PROFIT FOR ALL PARKING LOTS IS $%.2f AFTER " +
                parkingLotProgram.timeCount + " MINUTES.", parkingLotProgram.getAllLotsProfitTotal(parkingLotArray));
        System.out.println("\n\nTHE ENTRANCE QUEUE FOR ALL PARKING LOTS IS EMPTY: " + parkingLotProgram.carsEnterQueue.isEmpty());

        for (int z = 0; z < parkingLotArray.size(); z++)
        {
            int parkingLotNo = z + 1;
            System.out.println("\nPARKING LOT " + parkingLotNo + " IS EMPTY: " + parkingLotArray.get(z).parkingLotIsEmpty());
            System.out.println("\nPARKING LOT " + parkingLotNo + " EXIT QUEUE IS EMPTY: " +
                    parkingLotArray.get(z).carsLeaveQueue.isEmpty());
        }

    }
}
