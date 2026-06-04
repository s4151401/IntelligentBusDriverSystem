import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class BusIntegrationTest {

    // clear file before each test runs
    @BeforeEach
    void setup() throws IOException {
        new FileWriter("src/main/resources/buses.txt", false).close(); // false = opens the file in overwrite mode; close = close file after opening
    }

    // Task 4: Test 1 - Verify valid buses are stored correctly
    @Test
    void shouldStoreValidBus() throws IOException {
        BusRepository busRepo = new BusRepository();
        Bus bus = new Bus("12345678", 50, 80.0, "Hybrid");
        busRepo.add(bus);
        assertEquals(1, busRepo.count()); // verify it was stored

        // verify that the stored data was stored in correct format
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/buses.txt"));
        String line =  br.readLine();
        br.close();
        assertEquals("12345678,50,80.0,Hybrid", line);
    }

    // Task 4: Test 2 - Verify invalid buses are rejected
    @Test
    void shouldNotStoreInvalidBus() throws IOException {
        BusRepository busRepo = new BusRepository();
        // create 2 bus objects with the same busID
        Bus bus1 = new Bus("12345678", 60, 50.0, "Diesel");
        Bus bus2 = new Bus("12345678", 100, 20.0, "Hybrid");

        // Add first bus
        busRepo.add(bus1);

        // Try adding another bus with same ID
        assertThrows(IllegalArgumentException.class, () -> {
            busRepo.add(bus2);
        });
        assertEquals(1, busRepo.count()); // 2nd bus should be rejected, so count should be 1
    }

    // Task 4: Test 3 - Verify updates are persisted correctly
    @Test
    void updatesShouldPersist() throws IOException {
        BusRepository busRepo = new BusRepository();
        // add new record
        Bus bus = new Bus("12345678", 50, 80.0, "Hybrid");
        busRepo.add(bus);

        // update the bus
        busRepo.update("12345678", 40, 30.0, "Diesel");

        // retrieve and check updated values have persisted
        List<Bus> buses = busRepo.retrieve();
        assertEquals(40, buses.getFirst().getCapacity());
        assertEquals(30.0, buses.getFirst().getFuelLevel());
        assertEquals("Diesel", buses.getFirst().getFuelType());
    }

    // Task 4: Test 4 - Verify record counts are updated correctly
    @Test
    void shouldUpdateRepoCountCorrectly() throws IOException {
        BusRepository busRepo = new BusRepository();
        // add two new records
        Bus bus1 = new Bus("12345678", 50, 80.0, "Hybrid");
        Bus bus2 = new Bus("87654321", 60, 50.0, "Diesel");
        busRepo.add(bus1);
        busRepo.add(bus2);
        assertEquals(2, busRepo.count()); // count should be 2
    }
}
