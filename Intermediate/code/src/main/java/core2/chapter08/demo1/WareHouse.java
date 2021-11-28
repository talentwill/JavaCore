package core2.chapter08.demo1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WareHouse extends Remote {
    double getPrice(String description) throws RemoteException;
}