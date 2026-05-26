import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Bus class - conditions B1 to B5
 */
class BusTest {

    // ── B1: Bus ID format ─────────────────────────────────

    @Test
    void test1_validBusID() {
        // Normal: exactly 8 digits
        Bus b = new Bus("12345678", 50, 80.0, "Diesel");
        assertEquals("12345678", b.getBusID());
    }

    @Test
    void test2_idWithLetters_rejected() {
        // Invalid: contains a letter
        assertThrows(IllegalArgumentException.class, () ->
            new Bus("1234567A", 50, 80.0, "Diesel")
        );
    }

    @Test
    void test3_idTooShort_rejected() {
        // Edge: only 7 digits
        assertThrows(IllegalArgumentException.class, () ->
            new Bus("1234567", 50, 80.0, "Diesel")
        );
    }

    // ── B2: Capacity can only decrease ────────────────────

    @Test
    void test4_decreaseCapacity_allowed() {
        // Normal: reducing capacity is fine
        Bus b = new Bus("12345678", 50, 80.0, "Diesel");
        assertDoesNotThrow(() -> b.setCapacity(40));
    }

    @Test
    void test5_increaseCapacity_rejected() {
        // Invalid: cannot increase capacity
        Bus b = new Bus("12345678", 50, 80.0, "Diesel");
        assertThrows(IllegalArgumentException.class, () ->
            b.setCapacity(60)
        );
    }

    @Test
    void test6_sameCapacity_allowed() {
        // Edge: keeping same capacity is fine
        Bus b = new Bus("12345678", 50, 80.0, "Diesel");
        assertDoesNotThrow(() -> b.setCapacity(50));
    }

    // ── B3: Driver age restriction ────────────────────────

    @Test
    void test7_youngDriver_bigBus_allowed() {
        // Normal: driver age 40, bus capacity 60
        Driver d = new Driver("29@!abcdXY", "Jane", 10,
                "Heavy", "1|A St|Melbourne|VIC|Australia",
                "15-06-1984");
        Bus b = new Bus("12345678", 60, 80.0, "Diesel");
        assertDoesNotThrow(() -> b.assignDriver(d));
    }

    @Test
    void test8_oldDriver_bigBus_rejected() {
        // Invalid: driver age 55, bus capacity 60
        Driver d = new Driver("29@!abcdXY", "Bob", 20,
                "Heavy", "1|A St|Melbourne|VIC|Australia",
                "15-06-1969");
        Bus b = new Bus("12345678", 60, 80.0, "Diesel");
        assertThrows(IllegalArgumentException.class, () ->
            b.assignDriver(d)
        );
    }

    @Test
    void test9_oldDriver_smallBus_allowed() {
        // Edge: driver age 55, bus capacity 30 - OK
        Driver d = new Driver("29@!abcdXY", "Bob", 20,
                "Heavy", "1|A St|Melbourne|VIC|Australia",
                "15-06-1969");
        Bus b = new Bus("12345678", 30, 80.0, "Diesel");
        assertDoesNotThrow(() -> b.assignDriver(d));
    }

    // ── B4: Electric bus needs 5+ years exp ──────────────

    @Test
    void test10_fiveYearsExp_electricBus_allowed() {
        // Normal: exactly 5 years exp
        Driver d = new Driver("29@!abcdXY", "Ana", 5,
                "Heavy", "1|A St|Melbourne|VIC|Australia",
                "15-06-1990");
        Bus b = new Bus("12345678", 40, 100.0, "Electricity");
        assertDoesNotThrow(() -> b.assignDriver(d));
    }

    @Test
    void test11_fourYearsExp_electricBus_rejected() {
        // Invalid: only 4 years exp
        Driver d = new Driver("29@!abcdXY", "Ana", 4,
                "Heavy", "1|A St|Melbourne|VIC|Australia",
                "15-06-1990");
        Bus b = new Bus("12345678", 40, 100.0, "Electricity");
        assertThrows(IllegalArgumentException.class, () ->
            b.assignDriver(d)
        );
    }

    @Test
    void test12_lowExp_dieselBus_allowed() {
        // Edge: rule only applies to electric buses
        Driver d = new Driver("29@!abcdXY", "Ana", 1,
                "Heavy", "1|A St|Melbourne|VIC|Australia",
                "15-06-1990");
        Bus b = new Bus("12345678", 40, 100.0, "Diesel");
        assertDoesNotThrow(() -> b.assignDriver(d));
    }

    // ── B5: Licence restriction ───────────────────────────

    @Test
    void test13_heavyLicence_hybridBus_allowed() {
        // Normal: Heavy licence can drive Hybrid
        Driver d = new Driver("29@!abcdXY", "Tom", 8,
                "Heavy", "1|A St|Melbourne|VIC|Australia",
                "15-06-1988");
        Bus b = new Bus("12345678", 40, 90.0, "Hybrid");
        assertDoesNotThrow(() -> b.assignDriver(d));
    }

    @Test
    void test14_lightLicence_electricBus_rejected() {
        // Invalid: Light licence cannot drive Electric
        Driver d = new Driver("29@!abcdXY", "Tom", 8,
                "Light", "1|A St|Melbourne|VIC|Australia",
                "15-06-1988");
        Bus b = new Bus("12345678", 40, 90.0, "Electricity");
        assertThrows(IllegalArgumentException.class, () ->
            b.assignDriver(d)
        );
    }

    @Test
    void test15_publicTransportLicence_electricBus_allowed() {
        // Edge: PublicTransport licence is also allowed
        Driver d = new Driver("29@!abcdXY", "Tom", 8,
                "PublicTransport", "1|A St|Melbourne|VIC|Australia",
                "15-06-1988");
        Bus b = new Bus("12345678", 40, 90.0, "Electricity");
        assertDoesNotThrow(() -> b.assignDriver(d));
    }
}