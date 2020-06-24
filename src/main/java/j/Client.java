package j;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;

public class Client {
    // уникальное имя удаленногот объекта
    public static final String UNIQUE_BINDING_NAME = "server.rental";

    public static void main(String[] args) {
        try {
          /*  Scanner in = new Scanner(System.in);
            System.out.print("Input name file-writer: ");
            String filenameWrite = in.next();
            System.out.print("Input name file-reader: ");
            String filenameRead = in.next();
            System.out.println(filenameRead);
            System.out.println(filenameWrite);
            in.close();
            File fin = new File("filenameWrite.txt");
            File fou = new File("filenameRead.txt");
            */
            FileWriter writer = null;
            FileReader reader = null;
            try {
                reader = new FileReader(new File(args[0]));
                writer = new FileWriter(new File(args[1]));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            }

            if (reader == null || writer == null)
                return;

            try {
                // Получаем доступ к регистру удаленных объектов.
                final Registry registry = LocateRegistry.getRegistry("127.0.0.1",1099);

                // Получаем из регистра заглушку удаленного объекта
                RentalInterface server = (RentalInterface) registry.lookup(UNIQUE_BINDING_NAME);

                // Считываем
                Rental readFileCar = Rental.readFileCars(reader);

                //передаем на сервер и удаленно вызов метода на сервере
                Rental fromServer = server.sortCar(readFileCar);

                //записываем в файл тут)
                Rental.writeFileCars(writer, fromServer);

            } catch (RemoteException e) {
                writer.write("Exception: " + e.getMessage());
            } catch (NotBoundException e) {
                writer.write("Object under name " + UNIQUE_BINDING_NAME + " is not ebinded \n");
                writer.write(e.getMessage());
            } catch (ParseException e) {
                writer.write("Can not read the OrderList from " + args[0]);
                writer.write(e.getMessage());
            } catch (IncorrectConstructorParameters incorrectConstructorParameters) {
                incorrectConstructorParameters.printStackTrace();
            }
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}
