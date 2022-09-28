package carsharing.company;

public class Company {
    private Integer companyId;
    private String companyName;

    public Company(Integer id, String name) {
        this.setCompanyId(id);
        this.setName(name);
    }

    public void setName(String name) {
        this.companyName = name;
    }

    public String getName() {
        return this.companyName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
