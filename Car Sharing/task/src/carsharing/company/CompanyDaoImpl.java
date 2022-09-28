package carsharing.company;

import carsharing.database.Database;

import java.util.Map;

public class CompanyDaoImpl implements CompanyDao {
    @Override
    public void addCompany(String name) {
        Database db = Database.createDB();
        db.insertCompany(name);
    }

    @Override
    public Company getCompanyById(Integer companyId) {
        Database db = Database.createDB();
        return db.selectCompanyById(companyId);
    }

    @Override
    public Map<Integer, Company> getCompanies() {
        Database db = Database.createDB();
        return db.selectAllCompanies();
    }
}
