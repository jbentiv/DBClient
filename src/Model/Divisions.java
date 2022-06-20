package Model;

    public class Divisions {
        private int Division_ID;
        private String Division;
        private int Country_ID;

        /**
         * constructor for division class
         * @param divisionId
         * @param division
         * @param countryId
         */
        public Divisions(int divisionId, String division, int countryId) {
            Division_ID = divisionId;
            Division = division;
            Country_ID = countryId;
        }
        /**
         * Getter and setter for Division ID
         * @return
         */
        public int getDivision_ID() {
            return Division_ID;
        }

        public void setDivision_ID(int division_ID) {
            Division_ID = division_ID;
        }

        /**
         * Getter and setter for Division name
         * @return
         */
        public String getDivision() {
            return Division;
        }

        public void setDivision(String division) {
            Division = division;
        }

        /**
         * Getter and Setter for Country ID
         * @return
         */
        public int getCountry_ID() {
            return Country_ID;
        }

        public void setCountry_ID(int country_ID) {
            Country_ID = country_ID;
        }

        /**
         * Convert data for combo boxes
         * @return
         */
        @Override
        public String toString(){

            return (getDivision());
        }

    }