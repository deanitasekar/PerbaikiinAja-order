package id.ac.ui.cs.advprog.order.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.ac.ui.cs.advprog.order.security.JwtTokenProvider;
import id.ac.ui.cs.advprog.order.service.NotificationService;

@WebMvcTest(NotificationController.class)
@Import(id.ac.ui.cs.advprog.order.config.SecurityConfig.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "00000000-0000-0000-0000-000000000001", roles = {"USER"})
    void testGetNotifications() throws Exception {
    }

    @Test
    @WithMockUser(username = "00000000-0000-0000-0000-000000000001", roles = {"USER"})
    void testDeleteNotificationSuccess() throws Exception {
    }

    @Test
    @WithMockUser(username = "00000000-0000-0000-0000-000000000001", roles = {"USER"})
    void testDeleteNotificationNotFound() throws Exception {
    }
}
