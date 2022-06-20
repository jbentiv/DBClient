package Model;

public class ReportsCustomer {
    private String month;
    private String type;
    private String totalApp;

    /**
     * constructor for ReportsCustomer
     * @param month
     * @param type
     * @param totalApp
     */
    public ReportsCustomer(String month, String type, String totalApp) {
        this.month = month;
        this.type = type;
        this.totalApp = totalApp;
    }

    /**
     * getter and setter for month
     * @return
     */
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * getter and setter for type
     * @return
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter and setter for totalapp
     * @return
     */
    public String getTotalApp() {
        return totalApp;
    }

    public void setTotalApp(String totalApp) {
        this.totalApp = totalApp;
    }
}
