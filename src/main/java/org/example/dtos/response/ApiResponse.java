package org.example.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic API response wrapper
 * @param <T> The type of the data being returned
 */
@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private boolean success;
    private String message;

    public ApiResponse(T data, String message) {
        this.data = data;
        this.success = true;
        this.message = message;
    }

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.data = null;
    }
}
