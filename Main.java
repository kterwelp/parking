//Parking lot capacity - static final
//Entrance and exit gates
//Cars receive a time-stamped ticket at entry
//Cars present ticket at exit gates, pay and the leave
//Cars cannot be admitted if there is no space in the parking lot
    //parking lot isFull method
//Goal:  owners of parking lot want to maximize profits

//Classes:  Parking Lot and Cars

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList; 
import java.util.Queue; 

public class Main {

    private final static int capacity = 25;
    private Cars parkingSpots[];
    private int profitTotal;

    Main()
    {
        parkingSpots = new Cars[capacity];
    }

    public bool parkingSpaceIsOpen()
    {

    }

    public Cars findCar(int id)
    {
        for (int i = 0; i < capacity; i++)
        {
            if (id == parkingSpots[i].getID())
            {
                return parkingSpots[i];
            }
        }

        return null;
    }

    public void removeCar(int id)
    {
        for (int i = 0; i < capacity; i++)
        {
            if (id == parkingSpots[i].getID())
            {
                parkingSpots[i] = null;
            }
        }
    }

    public void calculatePayment(Cars curCar, int tC)
    {
        profitTotal += tc - curCar.getTimestamp();
    }

    public static void main(String[] args) {

        Main parkingLot = new Main();
        Gates enterGate1 = new Gates("Entrance");
        Gates enterGate2 = new Gates("Entrance");
        Gates exitGate1 = new Gates("Exit");
        Gates exitGate2 = new Gates("Exit");

        ArrayList<Cars> carsArray = new ArrayList<Cars>();
        Queue<Cars> carsEnterQueue = new LinkedList<Cars>();
        Queue<Cars> carsLeaveQueue = new LinkedList<Cars>();

        String line = "";
        int timeCount = 0;

        //while(!file.eof)
        //getline();

        //To make certain there can be several cars coming at once, each arrive statement
        //may have several ids after arrive - need to parse info and create for loop for the 
        //following code based upon how many cars arrive

        //These if-else statements empty the entrance and exit gates (once outside for loop)
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
        if (!carsEnterQueue.isEmpty())  //Once outside for loop
        {
            if (enterGate1.gateIsOpen())
            {
                enterGate1.assignCar(carsEnterQueue.peek());
                carsEnterQueue.remove();
                enterGate1.open = false;
            }
            else
            {
                if (enterGate2.gateIsOpen())
                {
                    enterGate2.assignCar(carsEnterQueue.peek());
                    carsEnterQueue.remove();
                    enterGate2.open = false;
                }
            }
            
        }

        //These if-else statements assign cars waiting in the carsLeaveQueue to any open exit gates
        if (!carsLeaveQueue.isEmpty())  //Once outside for loop
        {
            if (exitGate1.gateIsOpen())
            {
                exitGate1.assignCar(carsLeaveQueue.peek());
                carsLeaveQueue.remove();
                exitGate1.open = false;
            }
            else
            {
                if (exitGate2.gateIsOpen())
                {
                    exitGate2.assignCar(carsLeaveQueue.peek());
                    carsLeaveQueue.remove();
                    exitGate2.open = false;
                }
            }
            
        }

        if (line.contains("arrive") && enterGate1.gateIsOpen())
        {
            int identification = Integer.parseInt(line.substring(8, line.length()));
            carsArray.add(new Cars(timeCount, identification));
            enterGate1.assignCar(carsArray.get(carsArray.size()));
            enterGate1.open = false;
        }
        else if (line.contains("arrive") && !enterGate1.gateIsOpen())
        {
            if (enterGate2.gateIsOpen())
            {
                int identification = Integer.parseInt(line.substring(8, line.length()));
                carsArray.add(new Cars(timeCount, identification));
                enterGate2.assignCar(carsArray.get(carsArray.size()));
                enterGate2.open = false;
            }
            else
            {
                int identification = Integer.parseInt(line.substring(8, line.length()));
                carsArray.add(new Cars(timeCount, identification));
                carsEnterQueue.add(carsArray.get(carsArray.size()));
            }
        }
        else if (line.contains("leave") && exitGate1.gateIsOpen())
        {
            int identification = Integer.parseInt(line.substring(7, line.length()));
            Cars exitCar = parkingLot.findCar(identification);
            exitGate1.assignCar(exitCar);
            parkingLot.calculatePayment(exitCar, timeCount);
            parkingLot.removeCar(identification);
        }
        else if (line.contains("leave") && !exitGate1.gateIsOpen())
        {
            if (exitGate2.gateIsOpen())
            {
                int identification = Integer.parseInt(line.substring(7, line.length()));
                Cars exitCar = parkingLot.findCar(identification);
                exitGate2.assignCar(exitCar);
                parkingLot.calculatePayment(exitCar, timeCount);
                parkingLot.removeCar(identification);
            }
            else
            {
                int identification = Integer.parseInt(line.substring(7, line.length()));
                Cars exitCar = parkingLot.findCar(identification);
                carsLeaveQueue.add(exitCar);
                parkingLot.removeCar(identification);
            }
        }

        timeCount++; //once outside for loop

    }

}