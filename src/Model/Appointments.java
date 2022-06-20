package Model;

public class Appointments {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private String startTimeLocal;
    private String endTimeLocal;
    private int customerID;
    private int userID;
    private int contactID;
    private String contactName;

    /**
     * Constructor for appointment
     * @param appointmentID the appointment ID to set
     * @param title the appointment title to set
     * @param description the appointment description to set
     * @param location the appointment location to set
     * @param type the appointment type to set
     * @param startTimeLocal the appointment start LocalDateTime to set
     * @param endTimeLocal the appointment end LocalDateTime to set
     * @param customerID the customer ID to set
     * @param userID the user ID to set
     * @param contactID the contact ID to set
     */
    public Appointments(int appointmentID, String title, String description, String location, String type, String startTimeLocal, String endTimeLocal, int customerID, int userID, int contactID, String contactName) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTimeLocal = startTimeLocal;
        this.endTimeLocal = endTimeLocal;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.contactName = contactName;
    }

    /**
     * Constructor for appointment
     * @param appointmentID
     * @param title
     * @param startTimeLocal
     */
    public Appointments(int appointmentID, String title, String startTimeLocal) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.startTimeLocal = startTimeLocal;
    }

    /**
     * contructor for appointment
     * @param appointmentID
     * @param title
     * @param type
     * @param description
     * @param startTimeLocal
     * @param endTimeLocal
     * @param customerID
     */
    public Appointments(int appointmentID, String title, String type, String description, String startTimeLocal, String endTimeLocal, int customerID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.type = type;
        this.startTimeLocal = startTimeLocal;
        this.endTimeLocal = endTimeLocal;
        this.customerID = customerID;
    }

    /**
     * getter for AppointmetID
     * @return
     */

    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * getter for title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * getter for description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * getter for location
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * getter for type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * getter for local time
     * @return
     */
    public String getStartTimeLocal() {
        return startTimeLocal;
    }

    /**
     * getter for end local time
     * @return
     */
    public String getEndTimeLocal() {
        return endTimeLocal;
    }

    /**
     * getter for customerID
     * @return
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * getter for userID
     * @return
     */
    public int getUserID() {
        return userID;
    }

    /**
     * getter for contact ID
     * @return
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * getter for contact name
     * @return
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * getter for contact object
     * @return
     */
    public Contacts getContactObject() {

        Contacts c = new Contacts(getContactID(), getContactName());
        return c;
    }
}



