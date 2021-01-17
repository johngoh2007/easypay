package com.easypay.interview;

import com.easypay.interview.controllers.SubscriptionController;
import com.easypay.interview.models.Request;
import com.easypay.interview.models.SubscriptionTypes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SubscriptionController.class)
public class SimpleTests {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void test_successs() throws Exception {
        Request request = new Request();
        request.setAmount((long) 10.00);
        request.setType(SubscriptionTypes.WEEKLY);
        request.setDayOf("Tuesday");
        request.setStartDate("06/02/2018");
        request.setEndDate("27/02/2018");

        mockMvc.perform(MockMvcRequestBuilders.post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceDates", hasSize(4)));

    }
}
