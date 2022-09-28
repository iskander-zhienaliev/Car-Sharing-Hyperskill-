package carsharing.car;

public class Car {
    private Integer carId;
    private String carName;
    private Integer companyId;

    public Car(Integer carId, String carName, Integer companyId) {
        this.setCarId(carId);
        this.setCarName(carName);
        this.setCompanyId(companyId);
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
