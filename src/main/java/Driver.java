import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Driver {
    private String driverID;
    private String name;
    private int experienceYears;
    private String licenseType; // Light, Medium, Heavy, PublicTransport
    private String address;
    private String birthdate;

    // Constructor
    public Driver(String driverID, String name, int experienceYears, String licenseType, String address, String birthdate) {
        // D1. The driverID must be exactly 10 characters long
         if (driverID.length() != 10) {
            throw new IllegalArgumentException("DriverID must be exactly 10 digits");
        }

         // D1. The first two characters must be digits between 2 and 9
        char first = driverID.charAt(0);
        char second = driverID.charAt(1);
        if ((first < '2' || first > '9') || (second < '2' || second > '9') ) {
            throw new IllegalArgumentException("First two characters must be digits between 2 and 9");
        }

        // D1. the last two characters must be uppercase letters
        if (!driverID.matches(".*[A-Z]{2}$")) {
            throw new IllegalArgumentException("Last two characters must be uppercase letters A-Z");
        }

        // D1. there must be at least 2 special characters between characters 3 and 8
        int specialCharCount = 0;
        for (int i = 2; i <= 7; i++ ) {
            char c = driverID.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                specialCharCount++;
            }
        }
        if (specialCharCount < 2 ) {
            throw new IllegalArgumentException("Must have at least 2 special characters between positions 3 and 8");
        }

        // D2. The driver address must follow the format: Street Number|Street Name|City|State|Country
        String[] parts = address.split("\\|"); // "\\ |" tells java to treat this as a literal pipe character, not a regex operator
        if (parts.length != 5) {
            throw new IllegalArgumentException("Address must follow format: StreetNumber|StreetName|City|State|Country");
        }

        validateBirthdate(birthdate);
        validateLicenseType(licenseType);

        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.birthdate = birthdate;
    }

    // D3. The birthdate must follow the format: DD-MM-YYYY
    // dd = day of month, MM = month, yyyy = year
    // checks date entered is a valid date
    public void validateBirthdate(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // define the expected formate
        try {
            LocalDate.parse(birthdate, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Birthdate must be a valid date in format DD-MM-YYYY");
        }
    }

    // D4. validation check - called by DriverRepository.update()
    public void validateLicenseUpdate(String licenseUpdate) {
        if (this.experienceYears > 10) {
            throw new IllegalArgumentException("License type cannot be changed for drivers with more than 10 years experience");
        }
        validateLicenseType(licenseUpdate); // validate the new license type is valid
        this.licenseType = licenseUpdate;
    }

    public void validateLicenseType(String licenseType) {
        if (!licenseType.equals("Light") && !licenseType.equals("Medium") && !licenseType.equals("Heavy") && !licenseType.equals("PublicTransport")) {
            throw new IllegalArgumentException("License type must be Light, Medium, Heavy, or PublicTransport");
        }
    }

    // Getters
    public String getDriverID() {return driverID;}
    public String getName() {return name;}
    public int getExperienceYears() {return experienceYears;}
    public String getLicenseType() {return licenseType;}
    public String getAddress() {return address;}
    public String getBirthdate() {return birthdate;}

    // Setters
    public void setExperience (int experienceYears) {this.experienceYears = experienceYears;}
    public void setAddress (String address) {this.address = address;}

    public void setBirthdate (String newBirthdate) {
        validateBirthdate(newBirthdate); // validate birthdate if its being updated
        this.birthdate = newBirthdate;
    }

    public void setLicenseType (String newLicenseType) {
        validateLicenseType(newLicenseType);
        this.licenseType = newLicenseType;
    }
}
