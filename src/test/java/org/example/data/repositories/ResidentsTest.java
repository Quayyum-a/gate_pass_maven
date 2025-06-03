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
    public void findAllTest(){
        Resident resident1 = new Resident();
        resident1.setFullName("Quayyum");
        resident1.setAddress("123 Main St");
        resident1.setPhoneNumber("555-5555");
        resident1.setEmail("myemail@gmail.com");

        Resident resident2 = new Resident();
        resident2.setFullName("John");
        resident2.setAddress("456 Elm St");
        resident2.setPhoneNumber("555-5556");
        resident2.setEmail("johndoe@gmail.com");

        residents.save(resident1);
        residents.save(resident2);

        Iterable<Resident> allResidents = residents.findAll();
        int count = 0;
        for(Resident resident : allResidents){
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void findByEmailTest(){
        Resident resident = new Resident();
        resident.setFullName("Quayyum");
        resident.setAddress("123 Main St");
        resident.setPhoneNumber("555-5555");
        resident.setEmail("myemail@gmail.com");

        Resident savedResident = residents.save(resident);
        Resident foundResident = residents.findByEmail("myemail@gmail.com");
        assertNotNull(foundResident);
        assertEquals(savedResident.getFullName(), foundResident.getFullName());
    }

    @Test
    public void findByEmailNotFoundTest(){
        Resident foundResident = residents.findByEmail("myemail@gmail.com");
        assertNull(foundResident);
    }

    @Test
    public void updateTest(){
        Resident resident = new Resident();
        resident.setFullName("Quayyum");
        resident.setAddress("123 Main St");
        resident.setPhoneNumber("555-5555");
        resident.setEmail("myemail@gmail.com");

        Resident savedResident = residents.save(resident);
        savedResident.setFullName("Quayyum Khan");

        Resident updatedResident = residents.save(savedResident);
        assertNotNull(updatedResident);
        assertEquals("Quayyum Khan", updatedResident.getFullName());
    }

    @Test
    public void deleteTest(){
        Resident resident = new Resident();
        resident.setFullName("Quayyum");
        resident.setAddress("123 Main St");
        resident.setPhoneNumber("555-5555");
        resident.setEmail("myemail@gmail.com");

        Resident savedResident = residents.save(resident);
        residents.delete(savedResident);
        Resident foundResident = residents.findByEmail("myemail@gmail.com");
        assertNull(foundResident);
    }

    @Test
    public void deleteByIdTest(){
        Resident resident = new Resident();
        resident.setFullName("Quayyum");
        resident.setAddress("123 Main St");
        resident.setPhoneNumber("555-5555");
        resident.setEmail("myemail@gmail.com");

        Resident savedResident = residents.save(resident);
        residents.deleteById(savedResident.getId());
        Resident foundResident = residents.findByEmail("myemail@gmail.com");
        assertNull(foundResident);
    }

    @Test
    public void findByPhoneNumberTest(){
        Resident resident = new Resident();
        resident.setFullName("Quayyum");
        resident.setAddress("123 Main St");
        resident.setPhoneNumber("555-5555");
        resident.setEmail("myemail@gmail.com");

        Resident savedResident = residents.save(resident);
        Resident foundResident = residents.findByPhoneNumber("555-5555");
        assertNotNull(foundResident);
        assertEquals(savedResident.getFullName(), foundResident.getFullName());
/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    }

    @Test
    public void findByPhoneNumberNotFoundTest(){
        Resident foundResident = residents.findByPhoneNumber("555-5555");
        assertNull(foundResident);
/* <<<<<<<<<<  adb2da96-d0fd-4a30-91c2-ff362d5df16f  >>>>>>>>>>> */
    }

    @Test
    public void findByPhoneNumberNotFoundTest(){
        Resident foundResident = residents.findByPhoneNumber("555-5555");
        assertNull(foundResident);
    }

    @Test
    public void findByFullNameTest(){
        Resident resident = new Resident();
        resident.setFullName("Quayyum");
        resident.setAddress("123 Main St");
        resident.setPhoneNumber("555-5555");
        resident.setEmail("myemail@gmail.com");

        Resident savedResident = residents.save(resident);
        Resident foundResident = residents.findByFullName("Quayyum");
        assertNotNull(foundResident);
        assertEquals(savedResident.getFullName(), foundResident.getFullName());
    }

    @Test
    public void findByFullNameNotFoundTest(){
        Resident foundResident = residents.findByFullName("Quayyum");
        assertNull(foundResident);
    }

    @Test
    public void findByAddressTest(){
        Resident resident = new Resident();
        resident.setFullName("Quayyum");
        resident.setAddress("123 Main St");
        resident.setPhoneNumber("555-5555");
        resident.setEmail("myemail@gmail.com");

        Resident savedResident = residents.save(resident);
        Resident foundResident = residents.findByAddress("123 Main St");
        assertNotNull(foundResident);
        assertEquals(savedResident.getFullName(), foundResident.getFullName());
    }

    @Test
    public void findByAddressNotFoundTest(){
        Resident foundResident = residents.findByAddress("123 Main St");
        assertNull(foundResident);
    }

}