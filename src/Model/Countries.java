package Model;

public class Countries {
    private int countryID;
    private String countryName;

    /**
     * constructor for countries
     * @param countryID
     * @param countryName
     */
    public Countries(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     * getterfor countryID
     * @return
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * setter for country ID
     * @param countryID
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * getter for country name
     * @return
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * setter for country name
     * @param countryName
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public String toString() {
        return countryName;
    }
}
