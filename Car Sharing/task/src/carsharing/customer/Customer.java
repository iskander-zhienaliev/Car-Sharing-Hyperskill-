package carsharing.customer;

public class Customer {
    private String customerName;
    private Integer customerId;
    private Integer rentedCarId;

    public Customer(Integer id, String name, Integer carId) {
        this.setCustomerId(id);
        this.setCustomerName(name);
        this.setRentedCarId(carId);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}
