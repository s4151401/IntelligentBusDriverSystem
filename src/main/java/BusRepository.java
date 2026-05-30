import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusRepository {
    private static final String FILE_PATH = "src/main/resources/buses.txt";

    // Add - add new bus to TXT file
    public void add(Bus bus) throws IOException {
        // check busID is unique
        if (findByID(bus.getBusID()) != null) {
            throw new IllegalArgumentException("Bus with ID" + bus.getBusID() + " already exist");
        }
        // Format of entering in TXT file - busID|capacity|fuelLevel|fuelType
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(bus.getBusID() + "," + bus.getCapacity() + "," + bus.getFuelLevel() + "," + bus.getFuelType());
            bw.newLine();         // move to a new line
            System.out.println("Successfully appended to the file.");
        }
    }

    // Retrieve - reads from TXT file and returns a list of buses
    public List<Bus>retrieve() throws IOException {
        // create an empty list to store the buses in from TXT file
        List<Bus> buses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                buses.add(new Bus(parts[0].trim(), Integer.parseInt(parts[1].trim()), Double.parseDouble(parts[2].trim()), parts[3].trim()));
                System.out.println(line);
            }
        }
        return buses; // return array of buses from TXT file
    }


    // Update - finds and updates an existing bus record in the TXT file
    // User provides the busID they want to update plus the new values
    // System finds that record using busID
    // System checks if the updates are valid (B2 for capacity)
    // If valid, updates the record in the file
    public void update(String busID, int newCapacity, double newFuelLevel, String newFuelType) throws IOException {
        // check the bus being updated exists
        Bus existing = findByID(busID);
        if (existing == null) {
            throw new IllegalArgumentException("BusID does not exist");
        }

        // validate and update fields
        if (newCapacity != existing.getCapacity()) {
            existing.validateCapacityUpdate(newCapacity);
        }
        if (newFuelLevel != existing.getFuelLevel()) {
            existing.setFuelLevel(newFuelLevel);
        }
        if (!newFuelType.equals(existing.getFuelType())) {
            existing.setFuelType(newFuelType);
        }
        // read all lines, replace matching line, write back
        List<Bus> buses = retrieve();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) { // false means overwrite mode not append
            // For each Bus object in the buses list, call it bus
            for (Bus bus : buses) {
                // if the bus in the list matches the ID being updated
                if (bus.getBusID().equals(busID)) {
                    // write the updated values
                    bw.write(existing.getBusID() + "," + existing.getCapacity() + "," + existing.getFuelLevel() + "," + existing.getFuelType());
                } else {
                    // if the bus list doesn't match, write the original values unchanged
                    bw.write(bus.getBusID() + "," + bus.getCapacity() + "," + bus.getFuelLevel() + "," + bus.getFuelType());
                }
                bw.newLine();         // move to a new line
            }
        }
        System.out.println("Successfully updated.");
    }

    // Count - counts how many buses are in the TXT file
    public int count() throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            while (br.readLine() != null) {
                count++;
            }
        }
        System.out.println("Number of buses: " + count);
        return count;
    }

    // Find busID helper for uniqueness check
    private Bus findByID(String busID) throws IOException {
        for (Bus bus : retrieve()) {
            if (bus.getBusID().equals(busID)) {
                return bus;
            }
        }
        return null;
    }
}
