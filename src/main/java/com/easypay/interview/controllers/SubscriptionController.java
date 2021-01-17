package com.easypay.interview.controllers;

import com.easypay.interview.models.Request;
import com.easypay.interview.models.Response;
import com.easypay.interview.models.SubscriptionTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
public class SubscriptionController {

    @PostMapping(value = "/subscriptions", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Create Subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "Request is invalid",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content)
    })
    public ResponseEntity<Response> createSubscription(@RequestBody Request request) {

        Response response = new Response();

        //Validation
        if (request.getType().equals(SubscriptionTypes.MONTHLY)) {
            try {
                int test = Integer.parseInt(request.getDayOf());
            } catch (Exception e) {
                response.setError("dayOf is invalid. if the subscription type was WEEKLY, the day of the week - i.e. TUESDAY. If it was MONTHLY, the date of the month - i.e. 20");
                return ResponseEntity.badRequest().body(response);
            }
        }

        try {
            LocalDate startDate = parseDate(request.getStartDate());
        } catch (Exception e){
            response.setError("Start date is invalid. Accepted format dd/mm/yyyy");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            LocalDate endDate = parseDate(request.getEndDate());
        } catch (Exception e){
            response.setError("End date is invalid. Accepted format dd/mm/yyyy");
            return ResponseEntity.badRequest().body(response);
        }

        response.setAmount(request.getAmount());
        response.setType(request.getType());
        response.setInvoiceDates(invoiceDate(request));

        return ResponseEntity.ok(response);
    }

    private List<String> invoiceDate(Request request) {
        LocalDate startDate = parseDate(request.getStartDate());
        LocalDate endDate = parseDate(request.getEndDate());

        List<String> dates = startDate.datesUntil(parseDate(request.getEndDate()))
                .filter(localDate -> {
                    switch (request.getType()) {
                        case DAILY:
                            return true;
                        case WEEKLY:
                            return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US).equals(request.getDayOf());
                        case MONTHLY:
                            return Integer.valueOf(request.getDayOf()).equals(localDate.getDayOfMonth());
                        default:
                            return false;
                    }
                })
                .map(localDate -> localDate.toString()).collect(Collectors.toList());

        //If end date matches "dayOf" then add them to the list
        switch (request.getType()) {
            case DAILY:
                dates.add(endDate.toString());
            case WEEKLY:
                if(endDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US).equals(request.getDayOf())){
                    dates.add(endDate.toString());
                }
                break;
            case MONTHLY:
                if(Integer.valueOf(request.getDayOf()).equals(endDate.getDayOfMonth())){
                    dates.add(endDate.toString());
                }
                break;
            default:
                break;
        }
        return dates;
    }

    private LocalDate parseDate(String date){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date,format);
    }
}
