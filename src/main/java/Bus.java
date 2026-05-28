import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Bus class representing a bus in the Intelligent Bus Driver Guidance System.
 * Enforces rules B1 to B5.
 */
public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity

    /**
     * Constructor - validates busID on creation (B1)
     */
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        // B1: busID must be exactly 8 digits
        if (busID == null || !busID.matches("\\d{8}")) {
            throw new IllegalArgumentException("Bus ID must be exactly 8 digits");
        }

        validateFuelType(fuelType);

        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    /**
     * Validates capacity - B2: capacity cannot increase during update (can decrease)
     */
    public void validateCapacityUpdate(int newCapacity) {
        if (newCapacity > this.capacity) {
            throw new IllegalArgumentException("Capacity cannot increase during update");
        }
        this.capacity = newCapacity;
    }

    /**
     * Validates B3, B4, B5: checks driver is compatible with bus
     */
    public void validateDriverAssignment(Driver driver) {
        // B3: driver older than 50 cannot drive buses with capacity >= 50
        int age = calculateAge(driver.getBirthdate());
        if (age > 50 && this.capacity >= 50) {
            throw new IllegalArgumentException(
                "Driver older than 50 cannot drive buses with capacity 50 or more");
        }

        // B4: electric buses need at least 5 years experience
        if (this.fuelType.equals("Electricity") && driver.getExperienceYears() < 5) {
            throw new IllegalArgumentException(
                "Driver needs at least 5 years experience to drive electric buses");
        }

        // B5: electric and hybrid buses need Heavy or PublicTransport licence
        if (this.fuelType.equals("Electricity") || this.fuelType.equals("Hybrid")) {
            String licence = driver.getLicenseType();
            if (!licence.equals("Heavy") && !licence.equals("PublicTransport")) {
                throw new IllegalArgumentException(
                    "Only Heavy or PublicTransport licence can drive electric/hybrid buses");
            }
        }
    }

    /**
     * Helper method to calculate age from birthdate string DD-MM-YYYY
     */
    private int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dob = LocalDate.parse(birthdate, formatter);
        return Period.between(dob, LocalDate.now()).getYears();
    }

    // validate fuel type
    public void validateFuelType(String fuelType) {
        if (!fuelType.equals("Electricity") && !fuelType.equals("Hybrid") && !fuelType.equals("Diesel")) {
            throw new IllegalArgumentException("Fuel type must be Diesel, Hybrid or Electricity");
        }
    }

    // Getters
    public String getBusID()    { return busID; }
    public int getCapacity()    { return capacity; }
    public double getFuelLevel(){ return fuelLevel; }
    public String getFuelType() { return fuelType; }

    // Setters
    public void setFuelLevel(double fuelLevel) { this.fuelLevel = fuelLevel; }
    public void setFuelType(String newFuelType) {
        validateFuelType(newFuelType);
        this.fuelType = newFuelType;
    }
}
