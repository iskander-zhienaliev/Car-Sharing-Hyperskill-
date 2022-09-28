package carsharing.customer;

import carsharing.database.Database;

import java.util.Map;

public class CustomerDaoImpl implements CustomerDao {

    @Override
    public Map<Integer, Customer> getCustomers() {
        Database db = Database.createDB();
        return db.selectAllCustomers();
    }

    @Override
    public void addCustomer(String customerName) {
        Database db = Database.createDB();
        db.insertCustomer(customerName);
    }

    @Override
    public void rentCar(Integer customerId, Integer carId) {
        Database db = Database.createDB();
        db.updateCustomersCar(customerId, carId);
    }
}
