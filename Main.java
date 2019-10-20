import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList; 
import java.util.Queue;
import java.util.Scanner;

public class Main {

    private final static int capacity = 10;
    private Cars parkingSpots[];
    private int profitTotal;

    Main()
    {
        parkingSpots = new Cars[capacity];
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

    //Attempts to find a car in the parking lot spots
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

    //Calculates the payment for cars at parking lot exit gates
    public void calculatePayment(Cars curCar, int tC)
    {
        profitTotal += tC - curCar.getTimestamp();
    }

    //Returns the total amount collected for the parking lot
    public int getProfitTotal()
    {
        return profitTotal;
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

        //These statements initialize the parkingLot, enter gates and exit gates
        Main parkingLot = new Main();
        Gates enterGate1 = new Gates("Entrance");
        Gates enterGate2 = new Gates("Entrance");
        Gates exitGate1 = new Gates("Exit");
        Gates exitGate2 = new Gates("Exit");

        //These statements initiate array lists and queues
        //carsArray - holds all cars entering the parking lot
        //carsEnterQueue - holds all cars waiting to get in the parking lot when
        //                 an entrance gate is not available
        //carsLeaveQueue - holds all cars waiting to leave the parking lot when
        //                 and exit gate is not available
        ArrayList<Cars> carsArray = new ArrayList<Cars>();
        Queue<Cars> carsEnterQueue = new LinkedList<Cars>();
        Queue<Cars> carsLeaveQueue = new LinkedList<Cars>();

        String line = "";
        String fileString = "";
        int timeCount = 0;

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

                //These if-else statements empty the entrance and exit gates
                if (!enterGate1.gateIsOpen())
                {
                    enterGate1.open = true;
                    enterGate1.currentCar = null;
                }
                
                if (!enterGate2.gateIsOpen())
                {
                    enterGate2.open = true;
                    enterGate2.currentCar = null;
                }
                
                if (!exitGate1.gateIsOpen())
                {
                    exitGate1.open = true;
                    exitGate1.currentCar = null;
                }
                
                if (!exitGate2.gateIsOpen())
                {
                    exitGate2.open = true;
                    exitGate2.currentCar = null;
                }

                //These if-else statements assign cars waiting in the carsEnterQueue to any open entrance gates
                //They only assign them to the open entrance gates if there is a parking spot available
                //The cars assigned to an entrance gate will get a time stamp
                if (!carsEnterQueue.isEmpty())  
                {
                    if (enterGate1.gateIsOpen() && parkingLot.parkingSpaceIsOpen())
                    {
                        carsEnterQueue.peek().setTimestamp(timeCount);
                        enterGate1.assignCar(carsEnterQueue.peek());
                        parkingLot.assignCar(carsEnterQueue.peek());
                        carsEnterQueue.remove();
                        enterGate1.open = false;
                    }
                    else
                    {
                        if (enterGate2.gateIsOpen() && parkingLot.parkingSpaceIsOpen())
                        {
                            carsEnterQueue.peek().setTimestamp(timeCount);
                            enterGate2.assignCar(carsEnterQueue.peek());
                            parkingLot.assignCar(carsEnterQueue.peek());
                            carsEnterQueue.remove();
                            enterGate2.open = false;
                        }
                    }
                    
                }
                
                if (!carsEnterQueue.isEmpty() && enterGate2.gateIsOpen() && parkingLot.parkingSpaceIsOpen())
                {
                    carsEnterQueue.peek().setTimestamp(timeCount);
                    enterGate2.assignCar(carsEnterQueue.peek());
                    parkingLot.assignCar(carsEnterQueue.peek());
                    carsEnterQueue.remove();
                    enterGate2.open = false;
                }

                //These if-else statements assign cars waiting in the carsLeaveQueue to any open exit gates
                //The payment is calculated if the car is assigned to an exit gate
                //Otherwise they are assigned to the carsLeaveQueue
                if (!carsLeaveQueue.isEmpty())  
                {
                    if (exitGate1.gateIsOpen())
                    {
                        exitGate1.assignCar(carsLeaveQueue.peek());
                        parkingLot.calculatePayment(carsLeaveQueue.peek(), timeCount);
                        carsLeaveQueue.remove();
                        exitGate1.open = false;
                    }
                    else
                    {
                        if (exitGate2.gateIsOpen())
                        {
                            exitGate2.assignCar(carsLeaveQueue.peek());
                            parkingLot.calculatePayment(carsLeaveQueue.peek(), timeCount);
                            carsLeaveQueue.remove();
                            exitGate2.open = false;
                        }
                    }
                    
                }
                
                if (!carsLeaveQueue.isEmpty() && exitGate2.gateIsOpen())
                {
                    exitGate2.assignCar(carsLeaveQueue.peek());
                    parkingLot.calculatePayment(carsLeaveQueue.peek(), timeCount);
                    carsLeaveQueue.remove();
                    exitGate2.open = false;
                }

                //If the idIntIndex is empty, there are no valid IDs in the current line of input and the input is invalid
                //Therefore, the rest of the code is skipped, the time is incremented and the while loop starts over
                //for the next line of input
                if (iDIntIndex == 0)
                {
                    System.out.println("ERROR: This line of the file is invalid: " + line);
                    timeCount++;
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
                        if (enterGate1.gateIsOpen() && parkingLot.parkingSpaceIsOpen()) //ADD IF...parkingLot.parkingSpaceIsOpen()
                        {
                            int identification = iDIntArray[k];
                            carsArray.add(new Cars(timeCount, identification));
                            enterGate1.assignCar(carsArray.get(carsArray.size()-1));
                            parkingLot.assignCar(carsArray.get(carsArray.size()-1));
                            enterGate1.open = false;
                        }
                        else
                        {
                            if (enterGate2.gateIsOpen() && parkingLot.parkingSpaceIsOpen())
                            {
                                int identification = iDIntArray[k];
                                carsArray.add(new Cars(timeCount, identification));
                                enterGate2.assignCar(carsArray.get(carsArray.size()-1));
                                parkingLot.assignCar(carsArray.get(carsArray.size()-1));
                                enterGate2.open = false;
                            }
                            else
                            {
                                int identification = iDIntArray[k];
                                carsArray.add(new Cars(0, identification));
                                carsEnterQueue.add(carsArray.get(carsArray.size()-1));
                            }
                        }
                    }
                    //If input line states "leave", the code will enter this else if statement
                    //The car (found by ID) is assigned to an exit gate, removed from the parking lot
                    //and the payment is calculated if an exit gate is open.  Otherwise, the car is
                    //assigned to the carsLeaveQueue until an exit gate is open
                    else if (line.contains("leave"))
                    {
                        if(exitGate1.gateIsOpen())
                        {
                            int identification = iDIntArray[k];
                            Cars exitCar = parkingLot.findCar(identification);
                            if (exitCar != null)
                            {
                                exitGate1.assignCar(exitCar);
                                parkingLot.calculatePayment(exitCar, timeCount);
                                parkingLot.removeCar(identification);
                            }
    
                        }
                        else
                        {
                            if (exitGate2.gateIsOpen())
                            {
                                int identification = iDIntArray[k];
                                Cars exitCar = parkingLot.findCar(identification);

                                if (exitCar != null)
                                {
                                    exitGate2.assignCar(exitCar);
                                    parkingLot.calculatePayment(exitCar, timeCount);
                                    parkingLot.removeCar(identification);
                                }
                            
                            }
                            else
                            {
                                int identification = iDIntArray[k];
                                Cars exitCar = parkingLot.findCar(identification);

                                if (exitCar != null)
                                {        
                                    carsLeaveQueue.add(exitCar);
                                    parkingLot.removeCar(identification);
                                }
                              
                            }
                        }
                    }
                }

                //This increments the time by 1 minute before the while loop starts over
                timeCount++; 

            
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
        while (!carsEnterQueue.isEmpty() || !carsLeaveQueue.isEmpty() || !parkingLot.parkingLotIsEmpty())
        {
            //These if-else statements empty the entrance and exit gates
            if (!enterGate1.gateIsOpen())
            {
                enterGate1.open = true;
                enterGate1.currentCar = null;
            }
            else if (!enterGate2.gateIsOpen())
            {
                enterGate2.open = true;
                enterGate2.currentCar = null;
            }
            else if (!exitGate1.gateIsOpen())
            {
                exitGate1.open = true;
                exitGate1.currentCar = null;
            }
            else if (!exitGate2.gateIsOpen())
            {
                exitGate2.open = true;
                exitGate2.currentCar = null;
            }

            //These if-else statements assign cars waiting in the carsEnterQueue to any open entrance gates
            if (!carsEnterQueue.isEmpty()) 
            {
                if (enterGate1.gateIsOpen() && parkingLot.parkingSpaceIsOpen()) 
                {
                    carsEnterQueue.peek().setTimestamp(timeCount);
                    enterGate1.assignCar(carsEnterQueue.peek());
                    parkingLot.assignCar(carsEnterQueue.peek());
                    carsEnterQueue.remove();
                    enterGate1.open = false;
                }
                else
                {
                    if (enterGate2.gateIsOpen() && parkingLot.parkingSpaceIsOpen())
                    {
                        carsEnterQueue.peek().setTimestamp(timeCount);
                        enterGate2.assignCar(carsEnterQueue.peek());
                        parkingLot.assignCar(carsEnterQueue.peek());
                        carsEnterQueue.remove();
                        enterGate2.open = false;
                    }
                }
                
            }

            if (!carsEnterQueue.isEmpty() && enterGate2.gateIsOpen() && parkingLot.parkingSpaceIsOpen())
            {
                {
                    carsEnterQueue.peek().setTimestamp(timeCount);
                    enterGate2.assignCar(carsEnterQueue.peek());
                    parkingLot.assignCar(carsEnterQueue.peek());
                    carsEnterQueue.remove();
                    enterGate2.open = false;
                }

            }

            //These if-else statements assign cars waiting in the carsLeaveQueue to any open exit gates
            if (!carsLeaveQueue.isEmpty())  
            {
                if (exitGate1.gateIsOpen())
                {
                    exitGate1.assignCar(carsLeaveQueue.peek());
                    parkingLot.calculatePayment(carsLeaveQueue.peek(), timeCount);
                    carsLeaveQueue.remove();
                    exitGate1.open = false;
                }
                else
                {
                    if (exitGate2.gateIsOpen())
                    {
                        exitGate2.assignCar(carsLeaveQueue.peek());
                        parkingLot.calculatePayment(carsLeaveQueue.peek(), timeCount);
                        carsLeaveQueue.remove();
                        exitGate2.open = false;
                    }
                }
                
            }

            if (!carsLeaveQueue.isEmpty() && exitGate2.gateIsOpen())
            {
                {
                    exitGate2.assignCar(carsLeaveQueue.peek());
                    parkingLot.calculatePayment(carsLeaveQueue.peek(), timeCount);
                    carsLeaveQueue.remove();
                    exitGate2.open = false;
                }

            }

            if (parkingLot.findRemainingCars() != null)
            {
                Cars remainingCar = parkingLot.findRemainingCars();
                parkingLot.removeCar(remainingCar.getID());

                if(exitGate1.gateIsOpen())
                {
                    exitGate1.assignCar(remainingCar);
                    parkingLot.calculatePayment(remainingCar, timeCount);
                }
                else
                {
                    if (exitGate2.gateIsOpen())
                    {
                        exitGate2.assignCar(remainingCar);
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
                exitGate2.assignCar(remainingCar);
                parkingLot.calculatePayment(remainingCar, timeCount);
            }

            timeCount++;
        }

        System.out.println("\n\nWELCOME TO KEVIN TERWELP'S PARKING LOT");
        System.out.println("\n**************************************************");
        System.out.println("\nTHE TOTAL PROFIT FOR THE PARKING LOT IS $" + parkingLot.getProfitTotal() + " AFTER " + timeCount + " MINUTES.");
        System.out.println("\nTHE PARKING LOT IS EMPTY: " + parkingLot.parkingLotIsEmpty());
        System.out.println("\nTHE ENTRANCE QUEUE IS EMPTY: " + carsEnterQueue.isEmpty());
        System.out.println("\nTHE EXIT QUEUE IS EMPTY: " + carsLeaveQueue.isEmpty());


    }

}