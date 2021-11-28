package core2.chapter08.demo2;

import javax.naming.*;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class WarehouseClient2 {
    public static void main(String[] args) throws NamingException, RemoteException {
        // System.setProperty("java.security.policy", "client.policy");
        // System.setSecurityManager(new SecurityManager());
        Context namingContext = new InitialContext();

        NamingEnumeration<NameClassPair> enumeration = namingContext.list("rmi://localhost:8001");
        while (enumeration.hasMore()) {
            System.out.println(enumeration.next().getName());

            String url = "rmi://localhost:8001/warehouse2";
            Warehouse centralWarehouse = (Warehouse) namingContext.lookup(url);

            Scanner in = new Scanner(System.in);
            System.out.println("Enter keywords: ");
            List<String> keywords = Arrays.asList(in.nextLine().split("\\s+"));
            Product product = centralWarehouse.getProduct(keywords);

            System.out.println(product.getDescription() + ": " + product.getPrice());
            System.out.println(product.getLocation());
            in.close();
        }
    }
}
