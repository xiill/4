package j;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    // уникальное имя удаленногот объекта
    public static final String UNIQUE_BINDING_NAME = "server.rental";

    public static void main(String[] args) {
        try {
            final Rental r =  new Rental();
            // заглушка
            Remote stub = UnicastRemoteObject.exportObject((Rental)r, 0);
            // реестр удаленных объектов (локально для сервера)
            final Registry registry = LocateRegistry.createRegistry(1099);
            // регистрируем заглушку в реестре удаленных объектов
            registry.rebind(UNIQUE_BINDING_NAME, stub);
        } catch (RemoteException e){
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}
