package j;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "rental")
@XmlRootElement
public class Rental implements RentalInterface, Remote, Serializable {
    private int rental_point;
    private List<Car> auto;
    int size;
    // конструктор
    public Rental() throws RemoteException {
        super();
        this.auto =  new ArrayList<>();
    }

    // конструктор с параметром
    public Rental(int rental_point) throws RemoteException {
        super();
        this.rental_point = rental_point;
        auto = new ArrayList<>();
    }
    public Rental(int rental_point, List<Car> avto) throws RemoteException {
        super();
        this.rental_point = rental_point;
        auto = avto;
    }
    @XmlElement(name = "rental_point")
    public int getRental_point() {
        return rental_point;
    }

    public void setRental_point(int rental_point)
    {
        this.rental_point = rental_point;
    }

    //сортировка объктов в массиве по стоимости
    @Override
    public Rental sortCar(Rental car) throws RemoteException {
        List<Car> listCar = (List<Car>) ((ArrayList<Car>) car.auto).clone();
        listCar.sort(CarComparator.getInstance());
        List<Car> sorted = new ArrayList<>();
        Car avto = listCar.get(0);
        sorted.add(avto);

        for (int i = 0; i < listCar.size(); ++i) {
            Car c = listCar.get(i);
            if (!c.equals(avto))
                sorted.add(c);
            avto = c;
        }
        Rental ans = new Rental(car.getRental_point());
        ans.auto = sorted;
        return ans;
    }



    // получение длины массива
    public int size() {
        return auto.size();
    }

    // получение идентификатора списка
    public Car get_index(int index) {
        return auto.get(index);
    }

    public boolean add(Car car) {
        boolean a = auto.add(car);
        size = auto.size();
        return a;
    }

    // добавление объекта по индексу
    public void addCar(int indexToAdd, Car car) {
        auto.add(indexToAdd, car);
        size = auto.size();
    }

    // удаление объекта по индексу
    public Car removeCar(int indexToDelete) {
        Car r = auto.remove(indexToDelete);
        size = auto.size();
        return r; // возвращает удаленный объект
    }

    @XmlElement(name = "car")
    @XmlElementWrapper(name = "cars")
    public List<Car> getCars() {
        return auto;
    }

    // запись массива объектов в файл
    public static void writeFileCars(Writer out, Rental rental){
        try {
            int size = rental.size();
            out.write(size + " cars at the rental point " + rental.getRental_point() + "\n");
            if (rental.size() > 0) {
                for (int i = 0; i < size; i++) {
                    Car.writeFile(out, rental.get_index(i));
                    out.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // чтение массива объектов из файла
    public static Rental readFileCars(Reader in) throws IOException, ParseException, IncorrectConstructorParameters {

        BufferedReader reader = new BufferedReader(in);

        String str = reader.readLine();
        int size = Character.getNumericValue(str.charAt(0));
        int rental_point = Character.getNumericValue(str.charAt(27));

        Rental rent = new Rental(rental_point);

        for (int i = 0; i < size; ++i) {
            String cars = reader.readLine() + '\n' + reader.readLine() + '\n' +reader.readLine() + '\n' +reader.readLine() ;
            StringReader carReaders = new StringReader(cars);
            Car car = Car.readFile(carReaders);
            rent.add(car);
        }
        reader.close();
        return rent;
    }
}

