package org.example.services;

import org.example.data.models.Resident;
import org.example.data.repositories.AccessTokens;
import org.example.data.repositories.Residents;
import org.example.data.repositories.Visitors;
import org.example.dtos.request.FindAccessToken;
import org.example.dtos.request.GenerateAccessTokenRequest;
import org.example.dtos.request.LoginResidentRequest;
import org.example.dtos.request.RegisterResidentRequest;
import org.example.dtos.response.FindAccessTokenResponse;
import org.example.dtos.response.GenerateAccessTokenResponse;
import org.example.dtos.response.LoginResidentResponse;
import org.example.dtos.response.RegisterResidentResponse;
import org.example.exceptions.GatePassException;
import org.example.exceptions.ResidentAlreadyExsitsException;
import org.example.exceptions.ResidentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ResidentServicesImplTest {

    @Autowired
    private ResidentServicesImpl residentService;

    @Autowired
    private Residents residentRepository;

    @Autowired
    private AccessTokens accessTokenRepository;

    @Autowired
    private Visitors visitorRepository;

    private RegisterResidentRequest registerRequest;
    private LoginResidentRequest loginRequest;
    private GenerateAccessTokenRequest generateTokenRequest;
    private FindAccessToken findAccessTokenRequest;
    private Resident testResident;

    @BeforeEach
    void setUp() {
        // Clear repositories before each test
        accessTokenRepository.deleteAll();
        visitorRepository.deleteAll();
        residentRepository.deleteAll();

        // Setup test data
        registerRequest = new RegisterResidentRequest();
        registerRequest.setFullName("John Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setAddress("123 Test St");

        loginRequest = new LoginResidentRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password123");

        generateTokenRequest = new GenerateAccessTokenRequest();
        generateTokenRequest.setEmail("john.doe@example.com");
        generateTokenRequest.setVisitorName("Jane Smith");
        generateTokenRequest.setVisitorPhoneNumber("1234567890");
        generateTokenRequest.setWhomToSee("John Doe");

        findAccessTokenRequest = new FindAccessToken();
        findAccessTokenRequest.setAccessCode("TEST123");

        testResident = new Resident();
        testResident.setFullName("John Doe");
        testResident.setEmail("test@example.com");
        testResident.setPassword("test123");
        testResident.setAddress("123 Test St");
    }

    @Test
    void register_WithNewEmail_ShouldRegisterSuccessfully() {
        // Act
        RegisterResidentResponse response = residentService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("John Doe", response.getFullName());
        assertEquals("john.doe@example.com", response.getEmail());

        // Verify resident is saved in the database
        assertEquals("john.doe@example.com",residentRepository.findByEmail("john.doe@example.com"));
    }

    @Test
    void register_WithExistingEmail_ShouldThrowException() {
        // Arrange
        residentService.register(registerRequest);

        // Act & Assert
        assertThrows(ResidentAlreadyExsitsException.class,
                () -> residentService.register(registerRequest));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnLoginResponse() {
        // Arrange
        residentService.register(registerRequest);

        // Act
        LoginResidentResponse response = residentService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("John Doe", response.getFullName());
        assertEquals("john.doe@example.com", response.getEmail());
    }

    @Test
    void login_WithInvalidEmail_ShouldThrowException() {
        // Arrange
        LoginResidentRequest invalidRequest = new LoginResidentRequest();
        invalidRequest.setEmail("nonexistent@example.com");
        invalidRequest.setPassword("password123");

        // Act & Assert
        assertThrows(GatePassException.class,
                () -> residentService.login(invalidRequest));
    }

    @Test
    void login_WithInvalidPassword_ShouldThrowException() {
        // Arrange
        residentService.register(registerRequest);
        LoginResidentRequest invalidRequest = new LoginResidentRequest();
        invalidRequest.setEmail("john.doe@example.com");
        invalidRequest.setPassword("wrongpassword");

        // Act & Assert
        assertThrows(GatePassException.class,
                () -> residentService.login(invalidRequest));
    }

    @Test
    void generateAccessToken_WithValidRequest_ShouldGenerateToken() {
        // Arrange
        residentService.register(registerRequest);

        // Act
        GenerateAccessTokenResponse response = residentService.generateAccessToken(generateTokenRequest);

        // Assert
        assertEquals("Jane Smith", response.getVisitorName());
    }

    @Test
    void generateAccessToken_WithNonExistentResident_ShouldThrowException() {
        // Arrange
        GenerateAccessTokenRequest invalidRequest = new GenerateAccessTokenRequest();
        invalidRequest.setEmail("nonexistent@example.com");
        invalidRequest.setVisitorName("Test Visitor");

        // Act & Assert
        assertThrows(ResidentNotFoundException.class,
                () -> residentService.generateAccessToken(invalidRequest));
    }

    @Test
    void findAccessToken_WithValidToken_ShouldReturnTokenDetails() {
        // Arrange
        residentService.register(registerRequest);
        GenerateAccessTokenResponse tokenResponse = residentService.generateAccessToken(generateTokenRequest);

        FindAccessToken request = new FindAccessToken();
        request.setAccessCode(tokenResponse.getAccessCode());

        // Act
        FindAccessTokenResponse response = residentService.findAccessToken(request);

        // Assert
        assertNotNull(response);
        assertEquals("Jane Smith", response.getVisitorName());
        assertEquals("John Doe", response.getResidentFullName());
        assertTrue(response.getIsValid());
    }

    @Test
    void findAccessToken_WithInvalidToken_ShouldThrowException() {
        // Arrange
        FindAccessToken request = new FindAccessToken();
        request.setAccessCode("INVALID_TOKEN");

        // Act & Assert
        assertThrows(GatePassException.class,
                () -> residentService.findAccessToken(request));
    }

    @Test
    void getListOfVisitors_ShouldReturnVisitorList() {
        // Arrange
        residentService.register(registerRequest);
        residentService.generateAccessToken(generateTokenRequest);

        // Act
        var visitors = residentService.getListOfVisitors();

        // Assert
        assertNotNull(visitors);
        assertFalse(visitors.isEmpty());
        assertEquals("Jane Smith", visitors.get(0).getName());
    }
}