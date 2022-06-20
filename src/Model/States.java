package Model;

public class States {
    public int stateID;
    public String stateName;

    /**
     * Constructor for State object
     * @param stateID stateID
     * @param stateName stateName
     */
    public States(int stateID, String stateName) {
        this.stateID = stateID;
        this.stateName = stateName;
    }

    /**
     * getter and setter for state ID
     * @return
     */
    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    /**
     * getter and setter for state name
     * @return
     */

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /**
     * changes statename to string
     * @return
     */
    public String toString() {
        return stateName;
    }
}
