package core2.chapter08.demo2;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Permission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class WarehouseImpl extends UnicastRemoteObject implements Warehouse {
    private Map<String, Product> products;
    private Warehouse backup;

    public WarehouseImpl(Warehouse backup) throws RemoteException {
        products = new HashMap<>();
        this.backup = backup;
    }

    public void add(String keyword, Product product) {
        product.setLocation(this);
        products.put(keyword, product);
    }

    @Override
    public double getPrice(String description) throws RemoteException {
        for (Product product : products.values()) {
            if (product.getDescription().equals(description)) {
                return product.getPrice();
            }
        }
        if (backup == null) {
            return 0;
        } else {
            return backup.getPrice(description);
        }
    }

    @Override
    public Product getProduct(List<String> keywords) throws RemoteException {
        for (String keyword : keywords) {
            Product product = products.get(keyword);
            if (product != null) {
                return product;
            }
        }

        if (backup != null) {
            return backup.getProduct(keywords);
        } else if (products.values().size() > 0) {
            return products.values().iterator().next();
        } else {
            return null;
        }
    }
}

class customSecurityManager extends SecurityManager {

    SecurityManager original;

    customSecurityManager(SecurityManager original) {
        this.original = original;
    }

    public void checkExit(int status) {
        //throw(new SecurityException("Not allowed"));
    }

    public void checkPermission(Permission perm) {
    }

    public SecurityManager getOriginalSecurityManager() {
        return original;
    }
}

public class WareHouseServer2 {
    public static void main(String[] args) throws Exception {
//        System.setProperty("java.security.policy", "server.policy");
//        System.setSecurityManager(new SecurityManager());
//        customSecurityManager cSM = new customSecurityManager(System.getSecurityManager());
//        System.setSecurityManager(cSM);

        System.out.println("Constructing server implementation...");

        WarehouseImpl backupWarehouse = new WarehouseImpl(null);
        WarehouseImpl centralWarehouse = new WarehouseImpl(backupWarehouse);

        centralWarehouse.add("toaster", new Product("Blackwell Toaster", 23.95));
        backupWarehouse.add("java", new Book("Core Java vol.2", "0132354799", 44.95));

        System.out.println("Binding server implementation to registry..,");
        LocateRegistry.createRegistry(8001);
        Naming.rebind("rmi://localhost:8001/warehouse2", centralWarehouse);
        System.out.println("waiting for invocations from clients...");
    }
}
