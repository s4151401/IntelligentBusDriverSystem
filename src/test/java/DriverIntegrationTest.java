import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DriverIntegrationTest {
        // clear file before each test runs
    @BeforeEach
    void setup() throws IOException {
        new FileWriter("src/main/resources/driver.txt", false).close(); // false = opens the file in overwrite mode; close = close file after opening
    }

//Task 3: (Test 1) Ensure valid drivers are stored successfully 
@Test
void shouldStoreValidDriver() throws IOException {
    DriverRepository driverRepo = new DriverRepository();
    Driver driver = new Driver("2234!@ABCD", "Alex Volkov", 8, "Heavy", "20|Batman|Melbourne|Victoria|Australia", "16-08-1997");
    driverRepo.add(driver);
    assertEquals(1, driverRepo.count());

    BufferedReader d = new BufferedReader(new FileReader("src/main/resources/driver.txt"));
    String line = d.readLine();
    d.close();
    assertEquals("1234!@ABCD|Alex Volkov|8|Heavy|20|Batman|Melbourne|Victoria|Australia|16-08-1997", line);
}

//Task 3: (Test 2) Ensure invalid drivers are rejected 
@Test 
void shouldRejectInvalidDriver() throws IOException {
    DriverRepository driverRepo = new DriverRepository();
    Driver driver1 = new Driver("2234!@ABCD", "Alex Volkov", 8, "Heavy", "20|Batman|Melbourne|Victoria|Australia", "16-08-1997");
    Driver driver2 = new Driver("2234!@ABCD", "Josh Chen", 4, "Light", "57|Sesame|Melbourne|Victoria|Australia", "01-05-2000");
    //add first driver
    driverRepo.add(driver1);
    //adding anoter driver with a duplicate ID
    assertThrows(IllegalArgumentException.class, () -> {driverRepo.add(driver2);});
    assertEquals(1, driverRepo.count()); //duplicate driver ID should be rejected
}

//Task 3: (Test 3) Ensure updates are persisted correctly 
@Test
void shouldPersistUpdates() {
    DriverRepository driverRepo = new DriverRepository();
    // add new record 
    Driver driver = new Driver("2234!@ABCD", "Josh Chen", 4, "Light", "57|Sesame|Melbourne|Victoria|Australia", "1-05-2000");
    driverRepo.add(driver);
    //update the driver records
    driverRepo.update("2234!@ABCD", "Josh Chen", 7, "Medium", "57|Sesame|Melbourne|Victoria|Australia", "1-05-2000");
    // retrieve and check updated values have persisted
    List<Driver> drivers = driverRepo.retrieve();
    assertEquals(7, drivers.getFirst().getExperienceYears());
    assertEquals("Medium", drivers.getFirst().getLicenseType());
}

//Task 3: (Test 4) Ensure record counts are updated correctly 
@Test 
void shouldUpdateRecordsCorrectly() throws IOException {
    DriverRepository driverRepo = new DriverRepository();
    Driver driver1 = new Driver("2236#$ACDC", "Rhys Larsen", 5, "Medium", "89|Lois Lane|Melbourne|Victoria|Australia", "23-02-2001");
    Driver driver2 = new Driver("2237%*QWER", "Chris Harper", 9, "Heavy", "20|Willard|Melbourne|Victoria|Australia", "06-10-1995");
    driverRepo.add(driver1);
    driverRepo.add(driver2);
    assertEquals(2, driverRepo.count()); 
}

}
