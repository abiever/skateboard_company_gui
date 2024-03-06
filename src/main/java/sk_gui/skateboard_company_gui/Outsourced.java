package sk_gui.skateboard_company_gui;

/**
 * This class represents the Outsourced subclass of the Part class, a part that is manufactured outside of the company. It adds a string field for Company Name.
 */
public class Outsourced extends Part {
    private String companyName;

    /**
     * This method first calls the constructor of the parent class and then adds a Company Name parameter.
     * @param id The part's ID
     * @param name The part's name.
     * @param price The part's price.
     * @param stock The part's current inventory level.
     * @param min The part's minimum stock allowed.
     * @param max The part's maximum stock allowed.
     * @param companyName The name of the company that produces the part.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * This method sets the name of the company that produces this part.
     * @param companyName The name of the company that makes this part.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * This method retrieves the name for the company that produces this part.
     * @return The name of this part's company.
     */
    public String getCompanyName() {
        return companyName;
    }

}
