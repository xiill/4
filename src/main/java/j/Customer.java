
package j;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@XmlType
@XmlRootElement(name = "customer")
public class Customer {
    private int id;
    private String name;
    private String login;
    @JsonIgnore
    private String password;
    private List<Rental> rent;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", rent=" + rent +
                '}';
    }

    public static void main(String[] args) {
        try {
            ClassDB classDB = new ClassDB();
            Customer pop = classDB.getCustomer("pop", "123");
            System.out.println(pop);
        } catch (IncorrectConstructorParameters | RemoteException | InstantiationException | InvocationTargetException | NoSuchMethodException | SQLException | IllegalAccessException | ClassNotFoundException incorrectConstructorParameters) {
            incorrectConstructorParameters.printStackTrace();
        }

    }

    // конструктор
    public Customer() {
        rent =  new ArrayList<>();
    }

    // конструктор, проверяющий корректность значений
    public Customer(int id, String name, String login, String password) throws IncorrectConstructorParameters {
        setId(id);
        setLogin(login);
        setName(name);
        setPassword(password);
        rent = new ArrayList<>();
    }

   @XmlElement(name = "id_customer")
    public int getId(){
        return id;
    }

    public void setId(int id) throws IncorrectConstructorParameters {
        if (id >= 0)
            this.id = id;   //присвоить той переменной новое знавение
        else throw new IncorrectConstructorParameters("Incorrect id ");
    }

    @XmlElement(name = "name")
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

   @XmlElement(name = "login")
    public String getLogin(){
        return login;
    }

    public void setLogin(String login){
        this.login = login;
    }

    @XmlElement(name = "password")

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    // добавление объекта точки проката в массив точек проката
    public void addRent(Rental r) throws IOException, ClassNotFoundException {
        this.rent.add(r);
    }

    // удаление объекта точки проката из массив точек проката
    public void removeRent(Rental r) throws IOException, ClassNotFoundException {
        this.rent.remove(r);
    }
    public void setCars(List<Rental> car) {
        this.rent = car;
    }
    // получение размера массива точек проката
    public int size(){
        return this.rent.size();
    }

    // получение точки проката из массива точек проката
    public Rental get(int index) {
        return rent.get(index);
    }

}
