package carsharing.database;

import carsharing.car.Car;
import carsharing.company.Company;
import carsharing.customer.Customer;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Database {
    private static Database db = null;
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL_BASE = "jdbc:h2:./src/carsharing/db/";
    private Connection connection = null;

    public static Database createDB(String dbPath) {
        if (db == null) {
            db = new Database(dbPath);
        }
        return db;
    }

    public static Database createDB() {
        return db;
    }

    public Database(String dbPath) {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL_BASE + dbPath);
            connection.setAutoCommit(true);
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public void closeDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCompanyTable() {
        try (Statement stmt = connection.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS COMPANY" +
                    "(ID INT AUTO_INCREMENT PRIMARY KEY," +
                    "NAME VARCHAR UNIQUE NOT NULL)";
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCarTable() {
        try (Statement stmt = connection.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS CAR" +
                    "(ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "NAME VARCHAR NOT NULL UNIQUE," +
                    "COMPANY_ID INT NOT NULL, FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID))";
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCustomerTable() {
        try (Statement stmt = connection.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS customer" +
                    "(ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "NAME VARCHAR UNIQUE NOT NULL, " +
                    "RENTED_CAR_ID INT DEFAULT NULL, FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR (ID))";
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCompany(String companyName) {
        try (Statement stmt = connection.createStatement()) {
            Map<Integer, Company> companies = this.selectAllCompanies();
            if (companies.size() == 0) {
                String sql = "ALTER TABLE company ALTER COLUMN id RESTART WITH 1";
                stmt.executeUpdate(sql);
            }
            String query = "INSERT INTO COMPANY (name) VALUES ('" + companyName + "')";
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCar(String carName, Integer companyId) {
        try (Statement stmt = connection.createStatement()) {
            String query = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('" + carName + "', '" + companyId + "')";
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCustomer(String customerName) {
        try (Statement stmt = connection.createStatement()) {
            String query = "INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) VALUES ('" + customerName + "', NULL)";
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Company> selectAllCompanies() {
        Map<Integer, Company> dataMap = new LinkedHashMap<>();
        try (Statement stmt = connection.createStatement()) {
            String query = "SELECT * FROM COMPANY";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                dataMap.put(rs.getInt("ID"), new Company(rs.getInt("ID"), rs.getString("NAME")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    public Map<Integer, Car> selectCars(Integer companyId) {
        Map<Integer, Car> dataMap = new LinkedHashMap<>();
        try (Statement stmt = connection.createStatement()) {
            String query = "SELECT * FROM CAR WHERE COMPANY_ID = " + companyId;
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                dataMap.put(rs.getInt("ID"), new Car(rs.getInt("ID"), rs.getString("NAME"), companyId));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    public Car selectCarById(Integer carId) {
        Car car = null;
        try (Statement stmt = connection.createStatement()) {
            String query = "SELECT * FROM CAR WHERE ID = " + carId;
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                car = new Car(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("COMPANY_ID"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }

    public Company selectCompanyById(Integer companyId) {
        Company company = null;
        try (Statement stmt = connection.createStatement()) {
            String query = "SELECT * FROM COMPANY WHERE ID = " + companyId;
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                company = new Company(rs.getInt("ID"), rs.getString("NAME"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return company;
    }

    public Map<Integer, Customer> selectAllCustomers() {
        Map<Integer, Customer> dataMap = new LinkedHashMap<>();
        try (Statement stmt = connection.createStatement()) {
            String query = "SELECT * FROM CUSTOMER";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                dataMap.put(rs.getInt("ID"), new Customer(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("RENTED_CAR_ID")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    public void updateCustomersCar(Integer customerId, Integer carId) {
        try (Statement stmt = connection.createStatement()) {
            String query;
            if (carId == null) {
                query = "UPDATE CUSTOMER" +
                        " SET RENTED_CAR_ID = NULL " +
                        "WHERE ID = " + customerId;
            } else {
                query = "UPDATE CUSTOMER" +
                        " SET RENTED_CAR_ID = '" + carId + "'" +
                        "WHERE ID = " + customerId;
            }
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
