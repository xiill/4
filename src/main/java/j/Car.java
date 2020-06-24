package j;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.*;
import java.util.Objects;

@XmlType(name = "car")
@XmlRootElement
public class Car implements Serializable {
    private String model;
    private String manufacturer; // производитель
    private int amount; // кол-во машин на автопарке
    private long cost;

    /* private String Color;
       private int number_of_doors; // кол-во дверей
       private int year_of_issue;  // год выпуска*/

    // конструктор
    public Car() { }

    // конструктор, проверяющий корректность значений
    public Car (String model, String manufacturer, int amount, long cost) throws IncorrectConstructorParameters {
        setAmount(amount);
        setCost(cost);
        setModel(model);
        setManufacturer(manufacturer);
    }

    @XmlElement(name = "model")
    public String getModel()
    {               //получить
        return model;
    }
    public void setModel(String model){         //присвоить
        this.model = model;
    }
    @XmlElement(name = "manufacturer")
    public String getManufacturer(){
        return manufacturer;
    }
    public void setManufacturer(String manufacturer){
        this.manufacturer = manufacturer;
    }
    @XmlElement(name = "amount")
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) throws IncorrectConstructorParameters {
        if (amount >= 0)
            this.amount = amount;    //присвоить той переменной новое знавение
        else throw new IncorrectConstructorParameters("Incorrect parameter 'amount' !");
    }
    @XmlElement(name = "cost")
    public long getCost(){
        return cost;
    }
    public void setCost(long cost) throws IncorrectConstructorParameters {
        if (cost >= 0)
            this.cost = cost;
        else throw new IncorrectConstructorParameters("Incorrect parameter 'cost' !");
    }

    @Override
    public String toString() {
        return
                "Model: " + getModel() + '\n' +
                        "Manufacture: " + getManufacturer() + '\n' +
                        "Amount: " + getAmount() +'\n' +
                        "Cost: " + getCost() +'\n' ;
    }

    // Запись объекта в файл
    public static void writeFile(Writer out, Car car)  {
        try {
            out.write(car.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    // Чтение объекта из файла
    public static Car readFile(Reader in) throws IOException, IncorrectConstructorParameters {
        Car car = new Car();
        StringBuilder temp = new StringBuilder();
        BufferedReader t = new BufferedReader(in);
        car.setModel(t.readLine());
        car.setManufacturer(t.readLine());
        car.setAmount(Integer.parseInt(t.readLine()));
        car.setCost(Long.parseLong(t.readLine()));
        return  car;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || (this.getClass() != o.getClass())){
            return false;
        }
        Car avto = (Car) o;
        return  Objects.equals(avto.model, model)
                && Objects.equals(avto.manufacturer, manufacturer)
                && Objects.equals(avto.amount, amount)
                && Objects.equals(avto.cost, cost);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(new Object[] {
                model,
                manufacturer,
                amount,
                cost
        });
    }
}
