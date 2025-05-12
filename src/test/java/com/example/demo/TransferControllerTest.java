package com.example.demo;

import com.example.demo.config.TestSecurityConfig;
import com.example.demo.dto.TransferRequest;
import com.example.demo.entity.User;
import com.example.demo.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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

    private User mockUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("testUser");
        user.setPassword("password");
        return user;
    }

    @Test
    public void testTransferApiSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TransferRequest(toUserId, new BigDecimal(300))))
                        .with(csrf())
                        .with(user(new UserDetailsImpl(mockUser(fromUserId)))))
                .andExpect(status().isOk());
    }

    @Test
    public void testTransferApiInsufficientFunds() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TransferRequest(toUserId, new BigDecimal(-10))))
                        .with(csrf())
                        .with(user(new UserDetailsImpl(mockUser(fromUserId)))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTransferApiInvalidAmount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TransferRequest(toUserId, new BigDecimal(100000))))
                        .with(csrf())
                        .with(user(new UserDetailsImpl(mockUser(fromUserId)))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTransferApiSameUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TransferRequest(fromUserId, new BigDecimal(-10))))
                        .with(csrf())
                        .with(user(new UserDetailsImpl(mockUser(fromUserId)))))
                .andExpect(status().isBadRequest());
    }
}

