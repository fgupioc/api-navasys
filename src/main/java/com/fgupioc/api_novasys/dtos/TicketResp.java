package com.fgupioc.api_novasys.dtos;

public record TicketResp(
        String filename,
        byte[] pdf
) {
}
