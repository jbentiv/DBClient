package Model;

public class Reports {
    private String loc;
    private String typ;
    private String total;

    /**
     * Constructor for reports
     * @param loc
     * @param typ
     * @param total
     */
    public Reports(String loc, String typ, String total) {
        this.loc = loc;
        this.typ = typ;
        this.total = total;
    }

    /**
     * getter and setter for location
     * @return
     */
    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    /**
     * getter and setter for type
     * @return
     */
    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    /**
     * getter and setter for total count
     * @return
     */
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

