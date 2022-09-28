package carsharing.company;

import java.util.Map;

public interface CompanyDao {
    public Map<Integer, Company> getCompanies();
    public void addCompany(String name);
    public Company getCompanyById(Integer companyId);
}
