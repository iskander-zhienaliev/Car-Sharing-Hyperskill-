package carsharing.car;

import java.util.Map;

public interface CarDao {
    public Map<Integer, Car> getCars(Integer companyId);
    public void addCar(String carName, Integer companyId);
    public Car getCarById(Integer carId);
}
