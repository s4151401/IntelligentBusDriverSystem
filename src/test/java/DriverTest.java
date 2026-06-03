import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;


 //Unit tests for Driver class - conditions D1 to D5

class DriverTest {

    //D1: DriverID format 

    @Test
    void test1_validDriverID() {
        // Normal: valid driverID 
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertEquals("59dd@%67TA", d.getDriverID());
    }

    @Test
    void test2_edgeCaseDriverID() {
        // Edge: boundary digits 2 and 9 in first two positions
        Driver d = new Driver("29A@AA@AAA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertEquals("29A@AA@AAA", d.getDriverID());
    }

    @Test
    void test3_invalidDriverID_rejected() {
        // Invalid: first char is 0 
        assertThrows(IllegalArgumentException.class, () ->
                new Driver("0946^5$Hjj", "John Smith", 5, "Light",
                        "09|Jefferson|Melbourne|Victoria|Australia",
                        "05-07-2000")
        );
    }

    //D2: Address format
    @Test
    void test4_validAddressFormat() {
        // Normal: valid pipe-separated address with all 5 fields
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertEquals("09|Jefferson|Melbourne|Victoria|Australia", d.getAddress());
    }

    @Test
    void test5_edgeCaseEmptyCity_rejected() {
    // Edge: only 4 fields instead of 5 
        assertThrows(IllegalArgumentException.class, () ->
                new Driver("59dd@%67TA", "John Smith", 5, "Light",
                    "72|Swanston|Victoria|Australia", // only 4 parts
                    "05-07-2000")
    );
}

    @Test
    void test6_invalidAddressFormat_rejected() {
        // Invalid: commas used instead of pipes
        assertThrows(IllegalArgumentException.class, () ->
                new Driver("59dd@%67TA", "John Smith", 5, "Light",
                        "89,Collin,Melbourne,Victoria,Australia",
                        "05-07-2000")
        );
    }

    @Test
    void test7_edgeCaseForeignCountry_allowed() {
        // Edge: foreign country value
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "31|Lonsdale|Melbourne|Victoria|Philippines",
                "05-07-2000");
        assertEquals("31|Lonsdale|Melbourne|Victoria|Philippines", d.getAddress());
    }

    //D3: Birthdate format

    @Test
    void test8_validBirthdateFormat() {
        // Normal: valid DD-MM-YYYY format
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertEquals("05-07-2000", d.getBirthdate());
    }

    @Test
    void test9_edgeCaseImpossibleValues_rejected() {
        // Edge: format looks correct but values are impossible
        assertThrows(IllegalArgumentException.class, () ->
                new Driver("59dd@%67TA", "John Smith", 5, "Light",
                        "09|Jefferson|Melbourne|Victoria|Australia",
                        "00-96-3400")
        );
    }

    @Test
    void test10_invalidBirthdateFormat_rejected() {
        // Invalid: commas used instead of hyphens
        assertThrows(IllegalArgumentException.class, () ->
                new Driver("59dd@%67TA", "John Smith", 5, "Light",
                        "09|Jefferson|Melbourne|Victoria|Australia",
                        "14,01,1990")
        );
    }

    @Test
    void test11_edgeCaseValidDate_allowed() {
        // Edge: valid real calendar date
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "21-06-2007");
        assertEquals("21-06-2007", d.getBirthdate());
    }

    @Test
    void test12_edgeCaseDay31ValidMonth_allowed() {
        // Edge: 31st May 
        Driver d = new Driver("59dd@%67TA", "John Smith", 5, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "31-05-2009");
        assertEquals("31-05-2009", d.getBirthdate());
    }

    //D4: License update restriction
    @Test
    void test13_validExperience_licenseUpdate_allowed() {
        // Normal: driver with 4 years experience can change licenseType

        Driver d = new Driver("59dd@%67TA", "John Smith", 4, "Light",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertDoesNotThrow(() -> d.validateLicenseUpdate("Medium"));
        assertEquals("Medium", d.getLicenseType());
    }

    @Test
    void test14_invalidExperience_licenseUpdate_rejected() {
        // Invalid: driver with 15 years experience cannot change licenseType
        Driver d = new Driver("59dd@%67TA", "John Smith", 15, "Heavy",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertThrows(IllegalArgumentException.class, () ->
                d.validateLicenseUpdate("PublicTransport")
        );
        assertEquals("Heavy", d.getLicenseType());
    }

    @Test
    void test15_edgeCaseExactlyTenYears_allowed() {
        // Edge: exactly 10 years experience CAN still update
        Driver d = new Driver("59dd@%67TA", "John Smith", 10, "Medium",
                "09|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertDoesNotThrow(() -> d.validateLicenseUpdate("Heavy"));
        assertEquals("Heavy", d.getLicenseType());
    }

    //D5: Immutable fields

    @Test
    void test16_validUpdate_allowedField() {
        // Normal: address updated using setAddress()
        Driver d = new Driver("29A@AA@AAA", "Joe Smith", 5, "Light",
                "19|Jefferson|Melbourne|Victoria|Australia",
                "05-07-2000");
        assertDoesNotThrow(() ->
                d.setAddress("09|Jefferson|Melbourne|Victoria|Australia")
        );
        assertEquals("29A@AA@AAA", d.getDriverID());
        assertEquals("Joe Smith", d.getName());
        assertEquals("09|Jefferson|Melbourne|Victoria|Australia", d.getAddress());
    }
@Test
void test17_invalidUpdate_noSetterForDriverID() {
    // Invalid: D5 is enforced by having no setDriverID() or setName() setter
    Driver d = new Driver("29A@AA@AAA", "Joe Smith", 5, "Light",
            "09|Jefferson|Melbourne|Victoria|Australia",
            "05-07-2000");


    assertEquals("29A@AA@AAA", d.getDriverID());
    assertEquals("Joe Smith", d.getName());

   
    assertThrows(IllegalArgumentException.class, () ->
            new Driver("INVALID!!!!", "Joe Smith", 5, "Light",
                    "09|Jefferson|Melbourne|Victoria|Australia",
                    "05-07-2000")
    );
}

@Test
void test18_edgeCase_attemptNameChange_noSetter() {
    // Edge: same driverID, attempting name change has no mechanism
    Driver d = new Driver("67dd@%67TA", "Bob", 5, "Light",
            "09|Jefferson|Melbourne|Victoria|Australia",
            "05-07-2000");


    assertDoesNotThrow(() ->
            d.setAddress("31|Lonsdale|Melbourne|Victoria|Australia")
    );

   
    assertEquals("67dd@%67TA", d.getDriverID());
    assertEquals("Bob", d.getName());
}
}