package com.fgupioc.api_novasys.helpers;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project ticket-sales
 * @since 02/07/2026
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}