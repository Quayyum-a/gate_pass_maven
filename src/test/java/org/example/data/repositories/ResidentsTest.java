package org.example.data.repositories;

import org.example.data.models.Resident;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class ResidentsTest {
    @Autowired
    private Residents residents;


    @Test
    public void saveTest(){
        Resident resident = new Resident();
       Resident savedResident = residents.save(resident);
        assertEquals(1, residents.count());
        assertNotNull(savedResident);

    }

}