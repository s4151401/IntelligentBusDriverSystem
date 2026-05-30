import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DriverRepository {
    private static final String FILE_PATH = "src/main/resources/drivers.txt";

    // Add - add new driver to TXT file
    public void add(Driver driver) throws IOException {
        // check driverID is unique
        if (findByID(driver.getDriverID()) != null) {
            throw new IllegalArgumentException("Driver with ID " + driver.getDriverID() + " already exists");
        }
        // Format of entering in TXT file - driverID|name|experienceYears|licenseType|address|birthdate
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(driver.getDriverID() + "," + driver.getName() + "," + driver.getExperienceYears() + "," + driver.getLicenseType() + "," + driver.getAddress() + "," + driver.getBirthdate());
            bw.newLine();         // move to a new line
            System.out.println("Successfully appended to the file.");
        }
    }

    // Retrieve - reads from TXT file and returns a list of drivers
    public List<Driver>retrieve() throws IOException {
        // create an empty list to store the buses in from TXT file
        List<Driver> drivers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                drivers.add(new Driver(parts[0].trim(), (parts[1].trim()), Integer.parseInt(parts[2].trim()), parts[3].trim(), parts[4].trim(), parts[5].trim()));
                System.out.println(line);
            }
        }
        return drivers; // return array of drivers from TXT file
    }

    // Update - finds and updates an existing driver record in the TXT file
    // User provides the driverID they want to update plus the new values
    // System finds that record using driverID
    // System checks if the updates are valid (B2 for capacity)
    // If valid, updates the record in the file
    public void update(String driverID, int newExperienceYears, String newLicenseType, String newAddress, String newBirthdate) throws IOException {
        // check the driver being updated exists
        Driver existing = findByID(driverID);
        if (existing == null) {
            throw new IllegalArgumentException("DriverID does not exist");
        }

        // validate and update fields
        if (newExperienceYears != existing.getExperienceYears()) {
            existing.setExperience(newExperienceYears);
        }
        if (!newLicenseType.equals(existing.getLicenseType())) {
            existing.validateLicenseUpdate(newLicenseType);
        }
        if (!newAddress.equals(existing.getAddress())) {
            existing.setAddress(newAddress);
        }
        if (!newBirthdate.equals(existing.getBirthdate())) {
            existing.setBirthdate(newBirthdate);
        }
        // read all lines, replace matching line, write back
        List<Driver> drivers = retrieve();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) { // false means overwrite mode not append
            // For each driver object in the drivers list, call it driver
            for (Driver driver : drivers) {
                // if the driver in the list matches the ID being updated
                if (driver.getDriverID().equals(driverID)) {
                    // write the updated values
                    bw.write(existing.getDriverID() + "," + existing.getName() + "," + existing.getExperienceYears() + "," + existing.getLicenseType() + "," + existing.getAddress() + "," + existing.getBirthdate());
                } else {
                    // if the driver list doesn't match, write the original values unchanged
                    bw.write(driver.getDriverID() + "," + driver.getName() + "," + driver.getExperienceYears() + "," + driver.getLicenseType() + "," + driver.getAddress() + "," + driver.getBirthdate());
                }
                bw.newLine();         // move to a new line
            }
        }
        System.out.println("Successfully updated.");
    }

    // Count - counts how many drivers are in the TXT file
    public int count() throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            while (br.readLine() != null) {
                count++;
            }
        }
        System.out.println("Number of drivers: " + count);
        return count;
    }

    // Find driverID helper for uniqueness check
    private Driver findByID(String driverID) throws IOException {
        for (Driver driver : retrieve()) {
            if (driver.getDriverID().equals(driverID)) {
                return driver;
            }
        }
        return null;
    }
}
