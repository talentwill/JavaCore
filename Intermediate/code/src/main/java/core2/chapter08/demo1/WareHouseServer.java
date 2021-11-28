package core2.chapter08.demo1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

class WarehouseImpl extends UnicastRemoteObject implements WareHouse {
    private Map<String, Double> prices;

    public WarehouseImpl() throws RemoteException {
        prices = new HashMap<>();
        prices.put("Toaster", 24.95);
        prices.put("Microwave Oven", 49.95);
    }

    /**
     * The implementation of getPrice.
     */
    @Override
    public double getPrice(String description) throws RemoteException {
        Double price = prices.get(description);
        return price == null ? 0 : price;
    }
}

public class WareHouseServer {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        System.out.println("Generate Server Object.");
        WarehouseImpl centralWarehouse = new WarehouseImpl();

        System.out.println("Bind server object to port 8001, providing services");
        LocateRegistry.createRegistry(8001);
        Naming.rebind("rmi://127.0.0.1:8001/warehouse1", centralWarehouse);

        System.out.println("Waiting for client's invocation.");
    }
}