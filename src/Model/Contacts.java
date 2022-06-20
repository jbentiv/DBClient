package Model;

public class Contacts {
    public int contactID;
    public String contactName;
    public String contactEmail;
    /**
     * Constructor for contacts
     * @param contactID the contact ID to set
     * @param contactName the contact name to set
     */
    public Contacts(int contactID, String contactName) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * getter for ID
     * @return
     */
    public int getId() {
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
     * getter for contact email
     * @return
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * getter font contactID
     * @return
     */
    public int getContactID() {
        return contactID;
    }


    @Override
    public String toString() {
        return contactName;
    }
}
