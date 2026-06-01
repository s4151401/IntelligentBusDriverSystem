import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class DriverTest {

    // ── D1: Driver ID format ──────────────────────────────────────────

    @Test
    void test1_validDriverID() {
        // Normal: valid driverID format accepted
        // DriverID: 59dd@%67TA
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertTrue(d.validateDriverID());
    }

    @Test
    void test2_edgeCaseDriverID() {
        // Edge: boundary digits and exactly 2 special chars
        // DriverID: 29A@AA@AAA
        Driver d = new Driver("29A@AA@AAA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertTrue(d.validateDriverID());
    }

    @Test
    void test3_invalidDriverID() {
        // Invalid: first char is 0 (outside 2-9) and last two are lowercase
        // DriverID: 0946^5$Hjj
        Driver d = new Driver("0946^5$Hjj", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertFalse(d.validateDriverID());
    }

    // ── D2: Address format ────────────────────────────────────────────

    @Test
    void test4_validAddressFormat() {
        // Normal: valid pipe-separated address
        // Address: 09|Jefferson|Melbourne|Victoria|Australia
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertTrue(d.validateAddress());
    }

    @Test
    void test5_edgeCaseEmptyCity() {
        // Edge: correct pipes but City field is empty
        // Address: 72|Swanston||Victoria|Australia
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "72|Swanston||Victoria|Australia",
                "05-07-2000");
        assertFalse(d.validateAddress());
    }

    @Test
    void test6_invalidAddressFormat() {
        // Invalid: commas used instead of pipes
        // Address: 89,Collin,Melbourne,Victoria,Australia
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "89,Collin,Melbourne,Victoria,Australia",
                "05-07-2000");
        assertFalse(d.validateAddress());
    }

    @Test
    void test7_edgeCaseForeignCountry() {
        // Edge: foreign country - format still valid as D2 only checks format
        // Address: 31|Lonsdale|Melbourne|Victoria|Philippines
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "31|Lonsdale|Melbourne|Victoria|Philippines",
                "05-07-2000");
        assertTrue(d.validateAddress());
    }

    // ── D3: Birthdate format ──────────────────────────────────────────

    @Test
    void test8_validBirthdateFormat() {
        // Normal: valid DD-MM-YYYY format
        // Birthdate: 05-07-2000
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertTrue(d.validateBirthdate());
    }

    @Test
    void test9_edgeCaseImpossibleValues() {
        // Edge: correct format but impossible day, month and year values
        // Birthdate: 00-96-3400
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "00-96-3400");
        assertFalse(d.validateBirthdate());
    }

    @Test
    void test10_invalidBirthdateFormat() {
        // Invalid: commas used instead of hyphens
        // Birthdate: 14,01,1990
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "14,01,1990");
        assertFalse(d.validateBirthdate());
    }

    @Test
    void test11_edgeCaseValidDate() {
        // Edge: valid date with real calendar values
        // Birthdate: 21-06-2007
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "21-06-2007");
        assertTrue(d.validateBirthdate());
    }

    @Test
    void test12_edgeCaseDay31ValidMonth() {
        // Edge: day 31 in May - May has 31 days so this is VALID
        // Birthdate: 31-05-2009
        // NOTE: Expected result in test table was marked Invalid but
        // 31-05-2009 (31st May) is a valid calendar date - corrected to Valid
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "31-05-2009");
        assertTrue(d.validateBirthdate());
    }

    // ── D4: License update restriction ───────────────────────────────

    @Test
    void test13_validExperienceLicenseUpdate() {
        // Normal: driver with 4 years experience can change licenseType
        // Current: Light → New: Medium
        Driver d = new Driver("59dd@%67TA", "John Smith", 4, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        boolean result = d.updateLicenseType("Medium");
        assertTrue(result);
        assertEquals("Medium", d.getLicenseType());
    }

    @Test
    void test14_invalidExperienceLicenseUpdate() {
        // Invalid: driver with 15 years experience cannot change licenseType
        // Current: Heavy → New: PublicTransport - should be blocked
        Driver d = new Driver("59dd@%67TA", "John Smith", 15, "Heavy",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        boolean result = d.updateLicenseType("PublicTransport");
        assertFalse(result);
        assertEquals("Heavy", d.getLicenseType());
    }

    @Test
    void test15_edgeCaseExactlyTenYears() {
        // Edge: driver with exactly 10 years CAN still update
        // Rule says MORE THAN 10 - so 10 exactly is still allowed
        // Current: Medium → New: Heavy
        Driver d = new Driver("59dd@%67TA", "John Smith", 10, "Medium",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        boolean result = d.updateLicenseType("Heavy");
        assertTrue(result);
        assertEquals("Heavy", d.getLicenseType());
    }

    // ── D5: Immutable fields ──────────────────────────────────────────

    @Test
    void test16_validUpdateAllowedField() {
        // Normal: address updated, driverID and name remain unchanged
        // NewAddress: 09|Jefferson|Melbourne|Victoria|Australia
        // CurrentAddress: 19|Jefferson|Melbourne|Victoria|Australia
        Driver d = new Driver("29A@AA@AAA", "Joe Smith", 5, "Light",
                "19|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        boolean result = d.updateDriver(
                "29A@AA@AAA",                               // same driverID
                "Joe Smith",                                 // same name
                "09|Jefferson|Melbourne|Victoria|Australia", // new address
                "05-07-2000"                                 // same birthdate
        );
        assertTrue(result);
        assertEquals("29A@AA@AAA", d.getDriverID());
        assertEquals("Joe Smith", d.getName());
    }

    @Test
    void test17_invalidUpdateImmutableFields() {
        // Invalid: attempt to change both name and driverID is rejected
        // NewName: Joe, NewDriverID: 59dd@%67TA
        // CurrentName: Joe Smith, CurrentDriverID: 29A@AA@AAA
        Driver d = new Driver("29A@AA@AAA", "Joe Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        boolean result = d.updateDriver(
                "59dd@%67TA", // different driverID - blocked
                "Joe",         // different name - blocked
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000"
        );
        assertFalse(result);
        assertEquals("29A@AA@AAA", d.getDriverID());
        assertEquals("Joe Smith", d.getName());
    }

    @Test
    void test18_edgeCaseSameNameDifferentDriverID() {
        // Edge: same name submitted but different driverID attempted
        // NewName: Bob (same), NewDriverID: 29A@AA@AAA (different)
        // CurrentName: Bob, CurrentDriverID: 67dd@%67TA
        Driver d = new Driver("67dd@%67TA", "Bob", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        boolean result = d.updateDriver(
                "29A@AA@AAA", // different driverID - blocked
                "Bob",         // same name - not a change
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000"
        );
        assertFalse(result);
        assertEquals("67dd@%67TA", d.getDriverID());
        assertEquals("Bob", d.getName());
    }
}