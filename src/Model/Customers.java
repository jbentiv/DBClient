package Model;

public class Customers {
    private int custID;
    private String custName;
    private String custPhone;
    private String custAddress;
    private String custState;
    private String custCountry;
    private String custPostal;
    private int custCountryID;
    private int custStateID;

    /**
     * constructor for customers
     * @param custID
     * @param custName
     * @param custPhone
     * @param custAddress
     * @param custState
     * @param custCountry
     * @param custPostal
     * @param custCountryID
     * @param custStateID
     */
    public Customers(int custID, String custName, String custPhone, String custAddress, String custState, String custCountry, String custPostal, int custCountryID, int custStateID) {
        this.custID = custID;
        this.custName = custName;
        this.custPhone = custPhone;
        this.custAddress = custAddress;
        this.custState = custState;
        this.custCountry = custCountry;
        this.custPostal = custPostal;
        this.custCountryID = custCountryID;
        this.custStateID = custStateID;
    }

    /**
     * getter and setter for custID
     * @return
     */
    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    /**
     * getter and setter for cust name
     * @return
     */
    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * getter and setter for cust phone
     * @return
     */
    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    /**
     * getter and setter for customer address
     * @return
     */
    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    /**
     * getter and setter for cust state
     * @return
     */
    public String getCustState() {
        return custState;
    }

    public void setCustState(String custState) {
        this.custState = custState;
    }

    /**
     * getter and setter for cust country
     * @return
     */
    public String getCustCountry() {
        return custCountry;
    }

    public void setCustCountry(String custCountry) {
        this.custCountry = custCountry;
    }

    /**
     * getter and setter for postal code
     * @return
     */
    public String getCustPostal() {
        return custPostal;
    }

    public void setCustPostal(String custPostal) {
        this.custPostal = custPostal;
    }

    /**
     * getter and setter for cust country ID
     * @return
     */
    public int getCustCountryID() {
        return custCountryID;
    }

    public void setCustCountryID(int custCountryID) {
        this.custCountryID = custCountryID;
    }

    /**
     * getter and setter for custState ID
     * @return
     */
    public int getCustStateID() {
        return custStateID;
    }

    public void setCustStateID(int custStateID) {
        this.custStateID = custStateID;
    }

    /**
     * getter and setter for cust country box
     * @return
     */
    public Countries getCustCountryBox() {
        Countries c = new Countries(getCustCountryID(), getCustCountry());
        return c;
    }
    public States getCustStateBox() {
        States s = new States(getCustStateID(), getCustState());
        return s;
    }
}

