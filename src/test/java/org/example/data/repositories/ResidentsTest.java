package org.example.data.repositories;

import org.example.data.models.Resident;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class ResidentsTest {
    @Autowired
    private Residents residents;

    @BeforeEach
    void setUp() {
       residents.deleteAll();
    }


    @Test
    public void saveTest(){
        Resident resident = new Resident();
        resident.setFullName("Quayyum");
        resident.setAddress("123 Main St");
        resident.setPhoneNumber("555-5555");
        resident.setEmail("myemail@gmail.com");
       Resident savedResident = residents.save(resident);
        assertEquals(1, residents.count());
        assertNotNull(savedResident);
    }
    @Test
    public void findByIdTest() {
        Resident resident = new Resident();
        resident.setFullName("Quayyum");
        resident.setAddress("123 Main St");
        resident.setPhoneNumber("555-5555");
        resident.setEmail("myemail@gmail.com");
        Resident savedResident = residents.save(resident);
        Resident foundResident = residents.findById(savedResident.getId()).orElse(null);
        assertNotNull(foundResident);
        assertEquals(savedResident.getId(), foundResident.getId());
    }
    @Test
    public void deleteByIdTest() {
        Resident resident = new Resident();
        resident.setFullName("Quayyum");
        resident.setAddress("123 Main St");
        resident.setPhoneNumber("555-5555");
        resident.setEmail("myemail@gmail.com");
        Resident savedResident = residents.save(resident);
        residents.deleteById(savedResident.getId());
        assertEquals(0, residents.count());
    }

}