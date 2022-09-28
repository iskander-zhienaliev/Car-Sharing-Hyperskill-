package carsharing.car;

import carsharing.database.Database;

import java.util.Map;

public class CarDaoImpl implements CarDao {

    @Override
    public Map<Integer, Car> getCars(Integer companyId) {
        Database db = Database.createDB();
        return db.selectCars(companyId);
    }

    @Override
    public void addCar(String carName, Integer companyId) {
        Database db = Database.createDB();
        db.insertCar(carName, companyId);
    }

    @Override
    public Car getCarById(Integer carId) {
        Database db = Database.createDB();
        return db.selectCarById(carId);
    }
}
