package j;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RentalInterface extends Remote{

    // Сортировка объектов в массиве
    public Rental sortCar(Rental car) throws RemoteException;
}
