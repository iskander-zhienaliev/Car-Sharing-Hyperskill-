package carsharing;

import carsharing.car.Car;
import carsharing.car.CarDao;
import carsharing.car.CarDaoImpl;
import carsharing.company.Company;
import carsharing.company.CompanyDao;
import carsharing.company.CompanyDaoImpl;
import carsharing.customer.Customer;
import carsharing.customer.CustomerDao;
import carsharing.customer.CustomerDaoImpl;
import carsharing.database.Database;

import java.util.*;
import java.util.stream.Collectors;

enum LoginState {
    Unauthorized,
    Company,
    Customer
}

public class Main {
    static final String ARG_FILE_NAME = "-databaseFileName";
    static Scanner scanner = new Scanner(System.in);
    static LoginState loggedInAs = LoginState.Unauthorized;
    static Company selectedCompany = null;
    static Customer selectedCustomer = null;

    public static void main(String[] args) {
        String dbName = "test";
        if (args.length >= 2 && args[0].equals(ARG_FILE_NAME)) {
            dbName = args[1];
        }
        Database db = Database.createDB(dbName);
        db.createCompanyTable();
        db.createCarTable();
        db.createCustomerTable();

        while (true) {
            if (loggedInAs == LoginState.Company) {
                if (selectedCompany == null) {
                    printManagerMenu();
                    int mode = scanner.nextInt();
                    switch (mode) {
                        case 0:
                            loggedInAs = LoginState.Unauthorized;
                            break;
                        case 1:
                            getCompanyListMenu();
                            break;
                        case 2:
                            createCompany();
                            break;
                    }
                } else {
                    printCompanyMenu();
                    int mode = scanner.nextInt();
                    switch (mode) {
                        case 0:
                            selectedCompany = null;
                            break;
                        case 1:
                            getCarListMenu();
                            break;
                        case 2:
                            createCar();
                            break;
                    }
                }
            } else if (loggedInAs == LoginState.Customer) {
                if (selectedCustomer == null) {
                    getCustomerListMenu();
                } else {
                    printCustomerMenu();
                    int mode = scanner.nextInt();
                    switch (mode) {
                        case 0:
                            selectedCustomer = null;
                            loggedInAs = LoginState.Unauthorized;
                            break;
                        case 1:
                            rentCarFirstStep();
                            break;
                        case 2:
                            returnCar();
                            break;
                        case 3:
                            printRentedCar();
                            break;
                    }
                }
            } else {
                printIntroMenu();
                int mode = scanner.nextInt();
                if (mode == 0) {
                    break;
                } else if (mode == 1) {
                    loggedInAs = LoginState.Company;
                } else if (mode == 2) {
                    loggedInAs = LoginState.Customer;
                } else if (mode == 3) {
                    createCustomer();
                }
            }
        }
        db.closeDB();
    }

    public static void printIntroMenu() {
        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
    }

    public static void printManagerMenu() {
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
    }

    public static void printCompanyMenu() {
        System.out.printf("'%s' company:\n" +
                "1. Car list\n" +
                "2. Create a car\n" +
                "0. Back\n", selectedCompany.getName());
    }

    public static void printCustomerMenu() {
        System.out.println("1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
    }

    public static void getCompanyListMenu() {
        System.out.println();
        CompanyDao companyDao = new CompanyDaoImpl();
        Map<Integer, Company> companies = companyDao.getCompanies();
        if (companies.size() == 0) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Company list:");
            companies.forEach((k, v) -> System.out.printf("%d. %s%n", k, v.getName()));
            System.out.println("0. Back");
            int companySelected = scanner.nextInt();
            if (companySelected > 0) {
                selectedCompany = companies.get(companySelected);
            }
        }
        System.out.println();
    }

    public static void getCarListMenu() {
        System.out.println();
        CarDao carDao = new CarDaoImpl();
        Map<Integer, Car> cars = carDao.getCars(selectedCompany.getCompanyId());
        if (cars.size() == 0) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            for (int i = 0; i < cars.size(); i++) {
                Integer firstKey = (Integer) cars.keySet().toArray()[i];
                System.out.printf("%d. %s%n", i  + 1, cars.get(firstKey).getCarName());
            }
        }
        System.out.println();
    }

    public static void getCustomerListMenu() {
        System.out.println();
        CustomerDao customerDao = new CustomerDaoImpl();
        Map<Integer, Customer> customers = customerDao.getCustomers();
        if (customers.size() == 0) {
            System.out.println("The customer list is empty!");
            loggedInAs = LoginState.Unauthorized;
        } else {
            System.out.println("Choose a customer:");
            customers.forEach((k, v) -> System.out.printf("%d. %s%n", k, v.getCustomerName()));
            System.out.println("0. Back");
            int customerSelected = scanner.nextInt();
            if (customerSelected > 0) {
                selectedCustomer = customers.get(customerSelected);
            } else {
                loggedInAs = LoginState.Unauthorized;
            }
        }
        System.out.println();
    }

    public static void createCompany() {
        System.out.println();
        System.out.println("Enter the company name:");
        scanner.nextLine();
        String name = scanner.nextLine();
        CompanyDao companyDao = new CompanyDaoImpl();
        companyDao.addCompany(name);
        System.out.println("The company was created!");
        System.out.println();
    }

    public static void createCar() {
        System.out.println();
        System.out.println("Enter the car name:");
        scanner.nextLine();
        String name = scanner.nextLine();
        CarDao carDao = new CarDaoImpl();
        carDao.addCar(name, selectedCompany.getCompanyId());
        System.out.println("The car was added!");
        System.out.println();
    }

    public static void createCustomer() {
        System.out.println();
        System.out.println("Enter the customer name:");
        scanner.nextLine();
        String name = scanner.nextLine();
        CustomerDao customerDao = new CustomerDaoImpl();
        customerDao.addCustomer(name);
        System.out.println("The customer was added!");
        System.out.println();
    }

    public static void rentCarFirstStep() {
        System.out.println();
        CustomerDao customerDao = new CustomerDaoImpl();
        Customer customer = customerDao.getCustomers().get(selectedCustomer.getCustomerId());
        if (customer.getRentedCarId() != null && customer.getRentedCarId() != 0) {
            System.out.println("You've already rented a car!");
        } else {
            CompanyDao companyDao = new CompanyDaoImpl();
            Map<Integer, Company> companies = companyDao.getCompanies();
            if (companies.size() == 0) {
                System.out.println("The company list is empty!");
            } else {
                System.out.println("Choose a company:");
                companies.forEach((k, v) -> System.out.printf("%d. %s%n", k, v.getName()));
                System.out.println("0. Back");
                int companySelected = scanner.nextInt();
                if (companySelected > 0) {
                    rentCarSecondStep(companies.get(companySelected));
                }
            }
            System.out.println();
        }
    }

    public static void rentCarSecondStep(Company companySelected) {
        CarDao carDao = new CarDaoImpl();
        Map<Integer, Car> cars = carDao.getCars(companySelected.getCompanyId());
        CustomerDao customerDao = new CustomerDaoImpl();
        List<Integer> rentedCarIds = customerDao.getCustomers()
                .values()
                .stream()
                .map(Customer::getRentedCarId)
                .filter(id -> id != 0 && id != null)
                .collect(Collectors.toList());

        System.out.println(cars.toString());
        System.out.println(rentedCarIds);
        List<Integer> carListId = new ArrayList<>(cars.keySet());
        for (Integer carId: carListId) {
            if (rentedCarIds.contains(carId)) {
                cars.remove(carId);
            }
        }

        if (cars.size() == 0) {
            System.out.printf("No available cars in the '%s' company%n", companySelected.getName());
        } else {
            List<Car> carList = new ArrayList<>();
            System.out.println("Choose a car:");
            for (int i = 0; i < cars.size(); i++) {
                Integer firstKey = (Integer) cars.keySet().toArray()[i];
                carList.add(cars.get(firstKey));
                System.out.printf("%d. %s%n", i  + 1, cars.get(firstKey).getCarName());
            }
            System.out.println("0. Back");
            int carSelected = scanner.nextInt();
            if (carSelected > 0) {
                customerDao.rentCar(selectedCustomer.getCustomerId(), carList.get(carSelected - 1).getCarId());
                System.out.printf("You rented '%s'", carList.get(carSelected - 1).getCarName());
            }
        }
        System.out.println();
    }

    public static void returnCar() {
        CustomerDao customerDao = new CustomerDaoImpl();
        Customer customer = customerDao.getCustomers().get(selectedCustomer.getCustomerId());
        if (customer.getRentedCarId() == null || customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            customerDao.rentCar(selectedCustomer.getCustomerId(), null);
            System.out.println("You've returned a rented car!");
        }
    }

    public static void printRentedCar() {
        CustomerDao customerDao = new CustomerDaoImpl();
        Customer customer = customerDao.getCustomers().get(selectedCustomer.getCustomerId());
        if (customer.getRentedCarId() == null || customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            CarDao carDao = new CarDaoImpl();
            Car car = carDao.getCarById(customer.getRentedCarId());
            Integer companyId = car.getCompanyId();
            CompanyDao companyDao = new CompanyDaoImpl();
            Company company = companyDao.getCompanyById(companyId);
            System.out.println("Your rented car:");
            System.out.println(car.getCarName());
            System.out.println("Company:");
            System.out.println(company.getName());
        }
    }
}