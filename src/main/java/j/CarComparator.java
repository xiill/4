package j;
import java.util.Comparator;

class CarComparator implements Comparator<Car>{

    transient static final private CarComparator comparator = new CarComparator();

    public static CarComparator getInstance() {
        return comparator;
    }


    @Override
    public int compare(Car o1, Car o2) {
        if (o1.getCost() > o2.getCost() )
            return 1;
        else if (o1.getCost() < o2.getCost())
            return -1;
        else return 0;
    }


}
