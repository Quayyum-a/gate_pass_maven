package org.example.controllers;



import org.example.data.models.Visitor;
import org.example.dtos.request.FindAccessToken;
import org.example.dtos.request.GenerateAccessTokenRequest;
import org.example.dtos.request.RegisterResidentRequest;
import org.example.dtos.response.ApiResponse;
import org.example.dtos.response.FindAccessTokenResponse;
import org.example.dtos.response.GenerateAccessTokenResponse;
import org.example.dtos.response.RegisterResidentResponse;
import org.example.exceptions.GatePassException;
import org.example.services.ResidentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class ResidentControllers {

    @Autowired
    private ResidentServices residentServices;

    @PostMapping("/resident/register")
    public ResponseEntity<?> registerResidentService(@RequestBody RegisterResidentRequest residentServicesRequest) {
        try {
            RegisterResidentResponse response = residentServices.register(residentServicesRequest);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        } catch (GatePassException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resident/generate/code")
    public ResponseEntity<?> generateAccessTokenForVisitor(@RequestBody GenerateAccessTokenRequest residentRequest) {
        try {
            GenerateAccessTokenResponse tokenResponse = residentServices.generateAccessToken(residentRequest);
            return new ResponseEntity<>(new ApiResponse(tokenResponse, true), HttpStatus.OK);
        }
        catch (GatePassException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resident/find/code")
    public ResponseEntity<?> findAccessTokenForVisitor(@RequestBody FindAccessToken residentRequest) {
        try {
            FindAccessTokenResponse foundToken = residentServices.findAccessToken(residentRequest);
            return new ResponseEntity<>(new ApiResponse(foundToken, true), HttpStatus.FOUND);
        }
        catch (GatePassException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }


}
