package sk_gui.skateboard_company_gui;

/**
 * This class represents the InHouse subclass of the Part class, a part that is manufactured in-house by the company. It adds an integer field for Machine ID.
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * This method first calls the constructor of the parent class and then adds a Machine ID parameter.
     * @param id The part's ID.
     * @param name The part's name.
     * @param price The part's price.
     * @param stock The part's current inventory level.
     * @param min The part's minimum stock allowed.
     * @param max The part's maximum stock allowed.
     * @param machineId The part's machine ID.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * This method sets the ID of the machine for this part.
     * @param machineId The ID of the machine that produces this part.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * This method retrieves the ID for the machine that produces this part.
     * @return The ID of this part's machine.
     */
    public int getMachineId() {
        return machineId;
    }
}
