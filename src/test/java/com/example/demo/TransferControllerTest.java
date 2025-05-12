package com.example.demo;

import com.example.demo.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final Long fromUserId = 1L;
    private final Long toUserId = 2L;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testTransferApiSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(""))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testTransferApiInsufficientFunds() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/transfer")
                        .param("fromUserId", fromUserId.toString())
                        .param("toUserId", toUserId.toString())
                        .param("amount", "10000")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTransferApiInvalidAmount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/transfer")
                        .param("fromUserId", fromUserId.toString())
                        .param("toUserId", toUserId.toString())
                        .param("amount", "-10")
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTransferApiSameUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/transfer")
                        .param("fromUserId", fromUserId.toString())
                        .param("toUserId", fromUserId.toString())
                        .param("amount", "100")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}

