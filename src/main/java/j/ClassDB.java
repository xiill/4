package j;
//hghfgfhgfg
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClassDB {

    private static Car car;
    private static Rental rental;
    private static Customer customer;
    private static Object ClassDB;

    static {
        try {
            int rental_point = 1;
            car = new Car("Bugatti", "France", 90, 800000);
            rental = new Rental(rental_point);
            customer = new Customer(1, "Tatiana", "tanya", "00");
        } catch (IncorrectConstructorParameters | RemoteException incorrectConstructorParameters) {
            incorrectConstructorParameters.printStackTrace();
        }
    }

    // соединение с базой данных через JDBC URL
    String URL = "jdbc:mysql://localhost:3306/car_rental?serverTimezone=Europe/Moscow&useSSL=false&allowPublicKeyRetrieval=true";
    String user = "root";
    String password = "0000";

    public ClassDB() throws IncorrectConstructorParameters, RemoteException {
    }

    private Connection connect() throws ClassNotFoundException, SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // для загрузки драйвера
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        // для подключения к базе данных
        return DriverManager.getConnection(URL, user, password);
    }
    public String getUserId(String id) throws SQLException {
        Connection connection = null;
        try (PreparedStatement statement = connection.prepareStatement("select login from customer where id_customer = ?")) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString(1);
        }
    }
    public void createTable() throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection connection = this.connect();
        // для взаимодействия с базой данных
        Statement statement = connection.createStatement();
        // execute выполняет любые команды и возвращает значение boolean
        statement.execute("CREATE TABLE car (model VARCHAR(30) PRIMARY KEY not null, manufacture VARCHAR(30) not null, amount BIGINT not null, cost BIGINT not null)");
        statement.execute("CREATE TABLE rental (rental_point INT PRIMARY KEY not null)");
        statement.execute("CREATE TABLE customer (id_customer INT PRIMARY KEY not null, name VARCHAR(30) not null, login VARCHAR(30) not null, password VARCHAR(30) not null)");
        statement.execute("CREATE TABLE order_car (rental_point INT not null, model VARCHAR(30) not null,  PRIMARY KEY (rental_point, model), FOREIGN KEY (rental_point) REFERENCES rental (rental_point), FOREIGN KEY (model) REFERENCES car (model))");
        statement.execute("CREATE TABLE rental_customer (rental_point INT not null, id_customer INT not null, PRIMARY KEY (rental_point, id_customer), FOREIGN KEY (rental_point) REFERENCES rental (rental_point), FOREIGN KEY (id_customer) REFERENCES customer (id_customer))");
        System.out.println("Tables created");
        statement.close();
        connection.close();
    }

    public void insertTable() throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection connect = this.connect();
        // классы для выполнения запросов
        PreparedStatement insert_car = connect.prepareStatement("INSERT car(model, manufacture, amount, cost) VALUES (?,?,?,?)");
        insert_car.setString(1, "Porsche");
        insert_car.setString(2, "Germany");
        insert_car.setInt(3, 13);
        insert_car.setLong(4, 24000000);

        PreparedStatement insert_rental = connect.prepareStatement("INSERT rental(rental_point) VALUES (?)");
        insert_rental.setInt(1,7);

        PreparedStatement insert_order_car = connect.prepareStatement("INSERT order_car(rental_point, model) VALUES (?,?)");
        insert_order_car.setInt(1, 7);
        insert_order_car.setString(2, "Porsche");

        PreparedStatement insert_customer = connect.prepareStatement("INSERT customer(id_customer, name, login, password) VALUES (?,?,?,?)");
        insert_customer.setInt(1, 95);
        insert_customer.setString(2, "POP");
        insert_customer.setString(3, "pop");
        insert_customer.setString(4, "123");

        PreparedStatement insert_rental_customer= connect.prepareStatement("INSERT rental_customer(rental_point, id_customer) VALUES (?,?)");
        insert_rental_customer.setInt(1, 7);
        insert_rental_customer.setInt(2, 95);

        System.out.println("rows added in table CUSTOMER "+ insert_car.executeUpdate());
        System.out.println("rows added in table RENTAL "+ insert_rental.executeUpdate());
        System.out.println("rows added in table CAR "+ insert_order_car.executeUpdate());
        System.out.println( "rows added in table ORDER_CAR "+ insert_customer.executeUpdate());
        System.out.println("rows added in table RENTAL_CUSTOMER "+ insert_rental_customer.executeUpdate());

        connect.close();
    }

    public void addCustomer() throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IncorrectConstructorParameters {
        Connection connect = this.connect();

        PreparedStatement insert_customer = connect.prepareStatement("INSERT customer(id_customer, name, login, password) VALUES (?,?,?,?)");
        insert_customer.setInt(1, customer.getId());
        insert_customer.setString(2, customer.getName());
        insert_customer.setString(3, customer.getLogin());
        insert_customer.setString(4, customer.getPassword());

        Customer cu = new Customer(customer.getId(), customer.getName(), customer.getLogin(), customer.getPassword());

        System.out.printf("%d added rows in table CUSTOMER", insert_customer.executeUpdate());

        connect.close();
    }

    public void addRental(Customer cust) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        Connection connect = this.connect();

        PreparedStatement insert_rental = connect.prepareStatement("INSERT rental(rental_point, id_customer) VALUES (?,?)");
        insert_rental.setInt(1, rental.getRental_point());
        insert_rental.setInt(2, customer.getId());

        Rental re = new Rental(rental.getRental_point());
        cust.addRent(re);

        System.out.printf("%d rows added in table RENTAL", insert_rental.executeUpdate());

        connect.close();
    }

    public void addCar(Rental rent) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IncorrectConstructorParameters {
        Connection connection = this.connect();

        PreparedStatement insert_car = connection.prepareStatement("INSERT car(model, manufacture, amount, cost) VALUES (?,?,?,?)");
        insert_car.setString(1, car.getModel());
        insert_car.setString(2, car.getManufacturer());
        insert_car.setInt(3, car.getAmount());
        insert_car.setLong(4, car.getCost());

        Car avto = new Car(car.getModel(), car.getManufacturer(), car.getAmount(), car.getCost());
        rent.add(avto);

        System.out.printf(" %d rows added in table CAR", insert_car.executeUpdate());

        connection.close();
    }

    public void deleteRental(Customer cust) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        Connection connect = this.connect();

        PreparedStatement delete_rental = connect.prepareStatement("DELETE FROM rental WHERE rental_point = ?");
        delete_rental.setInt(1, rental.getRental_point());

        Rental re = new Rental(rental.getRental_point());
        cust.removeRent(re);
        connect.setAutoCommit(false);
        System.out.println("rows deleted in table RENTAL "+ delete_rental.executeUpdate());

        connect.close();
    }

    public void deleteCar(Rental rent) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IncorrectConstructorParameters {
        Connection connect = this.connect();

        PreparedStatement delete_car = connect.prepareStatement("DELETE FROM car WHERE model = ?");
        delete_car.setString(1, car.getModel());

        System.out.println(delete_car.executeUpdate() + " rows deleted in table CAR" );
        connect.setAutoCommit(false);
        connect.close();
    }
    //проверка существования пользователя с заданными логином и паролем
    public void controlCustomers(Customer cust) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection connect = this.connect();
        PreparedStatement select_customers = connect.prepareStatement("SELECT * FROM customer WHERE login = ? AND password = ?");
        select_customers.setString(1, customer.getLogin());
        select_customers.setString(2, customer.getPassword());
        ResultSet resultSet = select_customers.executeQuery();
        while (resultSet.next()) {
            String id_customer = resultSet.getString("id_customer");
            System.out.println(id_customer);
        }
        connect.close();
    }
    public boolean checkUserCorrect(String login, String password) throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Connection connection = this.connect();
        try (PreparedStatement statement = connection.prepareStatement("select * from customer where login = ? and password = ?")) {
            statement.setString(1, login);
            statement.setString(2, password);
            return statement.executeQuery().next();
        }
    }


    public Rental get_by_id_customer(int id_customer) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RemoteException, IncorrectConstructorParameters {
        Connection connect = this.connect();
        PreparedStatement statement = connect.prepareStatement("SELECT rental_point FROM rental_customer where id_customer = ?");
        statement.setObject(1, customer.getId());
        ResultSet resultSet = statement.executeQuery();
        Rental r = new Rental();
        while (resultSet.next()) {
            String model = resultSet.getString(1);
            String manufacture = resultSet.getString(2);
            int amount = resultSet.getInt(3);
            long cost = resultSet.getLong(4);
            System.out.println("Model: " + String.valueOf(model) + " ");
            System.out.println("Manufacture: " + String.valueOf(manufacture) + " ");
            System.out.println("Amount: " + String.valueOf(amount) + " ");
            System.out.println("Cost: " + String.valueOf(cost) + " ");
            r.add(new Car(model, manufacture, amount, cost));
        }
        connect.close();
        return r;
    }

    public List<Rental> get_car_by_id_customer (Customer cust) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RemoteException, IncorrectConstructorParameters {
        Connection connect = this.connect();

        PreparedStatement select_rent_id = connect.prepareStatement("SELECT rental_point FROM rental WHERE id_customer = ?) ");
        select_rent_id.setObject(1, cust.getId());

        ResultSet resultSet = select_rent_id.executeQuery();
        List<Rental> rent = new ArrayList<>();
        while (resultSet.next()) {
            rent.add( get_by_id_customer(resultSet.getInt(1)));
        }
        connect.close();
        return rent;
    }

    public void rent_point_where_max_sum_cost() throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection connect = this.connect();

        Statement rent_point = connect.createStatement();

        ResultSet resultSet = rent_point.executeQuery("SELECT rental_point FROM car JOIN order_car on car.model = order_car.model group by rental_point");
        while (resultSet.next()) {
            int rental_point = resultSet.getInt("rent point");
            System.out.printf("rent point in which sum all cost maximum 1 \n",  rental_point);
        }

        connect.close();
    }

    public void model_in_car() throws NoSuchMethodException, IllegalAccessException, InstantiationException, SQLException, InvocationTargetException, ClassNotFoundException {
        Connection connect = this.connect();

        Statement model_car = connect.createStatement();

        ResultSet resultSet = model_car.executeQuery("SELECT  manufacture FROM rental_customer JOIN order_car  on rental_customer.rental_point = order_car.rental_point JOIN car on order_car.model  = car.model WHERE id_customer = ?");
        while (resultSet.next()) {
            String model = resultSet.getString("model");
            System.out.println("all name in table car \n" + model);
        }
        connect.close();
    }

    public Customer getCustomer(String login, String password) throws SQLException, ClassNotFoundException, IncorrectConstructorParameters, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection connection = this.connect();
        PreparedStatement statement = connection.prepareStatement("SELECT id_customer, name FROM customer WHERE login = ? AND password = ?");
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        Customer cust = null;
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            cust = new Customer(id, name, login, password);
        }
        statement.close();
        connection.close();
        return cust;
    }
    public Rental CustomerByRental(Customer customer) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RemoteException {
        int id_customer = customer.getId();
        Connection connection = this.connect();
        PreparedStatement statement = connection.prepareStatement("SELECT rental_point FROM rental_customer WHERE id_customer = ? ");
        statement.setObject(1, id_customer );
        ResultSet resultSet = statement.executeQuery();
        Rental rent = new Rental();
        while (resultSet.next()) {
            int rental_point = resultSet.getInt(1);
            rent = new Rental(rental_point);
        }
        connection.close();
        return rent;
    }
    public List<Rental> CustomerByCar(Customer customer) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RemoteException, IncorrectConstructorParameters {
        int id_customer = customer.getId();
        Connection connection = this.connect();
        PreparedStatement statement = connection.prepareStatement("SELECT model FROM order_car JOIN rental_customer on order_car.rental_point = rental_customer.rental_point WHERE id_customer = ?");
        statement.setObject(1, id_customer );
        ResultSet resultSet = statement.executeQuery();
        List<Rental> car = new LinkedList<>();
        while (resultSet.next()) {
            car.add(this.get_by_id_customer(resultSet.getInt(1)));
        }
        connection.close();
        return car;
    }

    public List<String> getCars(Customer customer) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RemoteException, IncorrectConstructorParameters {
        int id_customer = customer.getId();
        Connection connection = this.connect();
        PreparedStatement statement = connection.prepareStatement("SELECT model FROM order_car JOIN rental_customer on order_car.rental_point = rental_customer.rental_point WHERE id_customer = ?");
        statement.setObject(1, id_customer );
        ResultSet resultSet = statement.executeQuery();
        List<String> cars = new LinkedList<>();
        while (resultSet.next()) {
            cars.add(resultSet.getString("model"));
        }
        connection.close();
        return cars;
    }
    public List<Car> carTable(Customer customer) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RemoteException, IncorrectConstructorParameters {
        int id_customer = customer.getId();
        Connection connection = this.connect();
        PreparedStatement statement = connection.prepareStatement("SELECT car.model, manufacture, cost, amount FROM rental_customer JOIN order_car  on rental_customer.rental_point = order_car.rental_point JOIN car on order_car.model  = car.model WHERE id_customer = ? and rental_customer.rental_point = ?");
        statement.setObject(1, id_customer);
        ResultSet resultSet = statement.executeQuery();
        List<Car> cars = new LinkedList<>();
        while (resultSet.next()) {
            String model = resultSet.getString("model");
            String manufacture = resultSet.getString("manufacture");
            int cost= resultSet.getInt("cost");
            int amount = resultSet.getInt("amount");
            cars.add(new Car(model, manufacture, cost, amount));
        }
        connection.close();
        return cars;
    }
    public List<Rental> carTable2(Customer customer) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RemoteException, IncorrectConstructorParameters {
        int id_customer = customer.getId();
        Connection connection = this.connect();
        PreparedStatement statement = connection.prepareStatement("SELECT rental_customer.rental_point, car.model, manufacture, cost, amount FROM rental_customer JOIN order_car  on rental_customer.rental_point = order_car.rental_point JOIN car on order_car.model  = car.model WHERE id_customer = ?");
        statement.setObject(1, id_customer);
        ResultSet resultSet = statement.executeQuery();
        List<Rental> avto = new LinkedList<>();
        List<Car> car = new LinkedList<>();
        while (resultSet.next()) {
            int rental_point = resultSet.getInt("rental_point");
            String model = resultSet.getString("model");
            String manufacture = resultSet.getString("manufacture");
            int cost= resultSet.getInt("cost");
            int amount = resultSet.getInt("amount");
            car.add(new Car(model, manufacture, cost, amount));
            avto.add(new Rental(rental_point, car));
        }
        connection.close();
        return avto;
    }
  /*  public List<Rental> rentalTable(Customer customer) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RemoteException, IncorrectConstructorParameters {
        int id_customer = customer.getId();
        Connection connection = this.connect();
        PreparedStatement statement = connection.prepareStatement("SELECT rental_point FROM rental_customer WHERE id_customer = ?");
        statement.setObject(1, id_customer);
        ResultSet resultSet = statement.executeQuery();
        List<Rental> cars = new LinkedList<>();
        while (resultSet.next()) {
            int rental_point = resultSet.getInt("rental_point");
            cars.add(new Rental(rental_point,carTable(customer, rental_point)));
        }
        connection.close();
        return cars;
    }*/
    public String getUserLogin(String login) throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Connection connection = this.connect();
        try (PreparedStatement statement = connection.prepareStatement("select id_customer from customer where login = ?")) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString(1);
        }
    }
    public void addCar(Car car, int id_customer) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection connection = this.connect();
        connection.setAutoCommit(false);
        PreparedStatement statement = null;
        Savepoint savepoint = connection.setSavepoint();
        try {
            statement = connection.prepareStatement("INSERT INTO car (model , manufacture , amount , cost) VALUES (?, ?, ?, ?)");
            statement.setString(1, car.getModel());
            statement.setString(2, car.getManufacturer());
            statement.setInt(3, car.getAmount());
            statement.setLong(4, car.getCost());
            statement.executeUpdate();
            statement = connection.prepareStatement("INSERT INTO rental_customer (rental_point , id_customer) VALUES (?, ?)");
            statement.setInt(1,  10);
            statement.setInt(2, id_customer);
            statement.executeUpdate();

            connection.commit();
            System.out.println("car has been added in the rental");
            statement.close();
            connection.releaseSavepoint(savepoint);
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback(savepoint);
        } finally {
            connection.close();
        }
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IncorrectConstructorParameters, RemoteException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ClassDB cl = new ClassDB();
        //cl.get_by_id_customer(99);
        //cl.createTable();
          //cl.insertTable();
        //cl.model_in_car();
        //cl.rent_point_where_max_sum_cost();
        // Rental r = new Rental(2);
        //cl.deleteCar(r);

    }
}
