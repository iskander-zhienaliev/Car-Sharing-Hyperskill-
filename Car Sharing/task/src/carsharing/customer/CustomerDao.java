package carsharing.customer;

import java.util.Map;

public interface CustomerDao {
    public Map<Integer, Customer> getCustomers();
    public void addCustomer(String customerName);
    public void rentCar(Integer customerId, Integer carId);
}
