package core2.chapter08.demo1;

import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingException;

public class WareHouseClient {
    public static void main(String[] args) throws NamingException, RemoteException {
        Context namingContext = new InitialContext();

        // 1. start to loop up services bound to RMI registry
        System.out.println("RMI registry list. ");
        Enumeration<NameClassPair> enumeration = namingContext.list("rmi://127.0.0.1:8001");
        while (enumeration.hasMoreElements()) {
            System.out.println(enumeration.nextElement().getName());
        }

        // 2. acquire service
        String url = "rmi://127.0.0.1:8001/warehouse1";
        WareHouse centralWareHouse = (WareHouse) namingContext.lookup(url);

        // 3. input argument and get result
        // String description = "Toaster";
        String description = "Microwave Oven";
        double price = centralWareHouse.getPrice(description);
        System.out.println(description + ":" + price);
    }
}