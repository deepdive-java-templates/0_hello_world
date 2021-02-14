package com.example;

import java.util.ArrayList;
import java.util.List;

public class Train {

    List<Commuter> passengers = new ArrayList<>(); //A - one of these is not the right place for the variable

    public void addCommuter(Commuter passenger) {
        List<Commuter> passengers = new ArrayList<>(); //A - one of these is not the right place for the variable
        if(passenger.hasTrainTicket()) {
            passengers.add(passenger);
        }
    }

    public int getPassengers() {
        return passengers.size();
    }
}
