package id.ac.ui.cs.advprog.order.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.ac.ui.cs.advprog.order.model.Notification;
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
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Notification n1 = new Notification(userId, "Title1", "Message1");
        n1.setId(UUID.randomUUID());
        Notification n2 = new Notification(userId, "Title2", "Message2");
        n2.setId(UUID.randomUUID());
        List<Notification> notifications = Arrays.asList(n1, n2);
        Mockito.when(notificationService.getNotifications(userId)).thenReturn(notifications);

        mockMvc.perform(get("/notifications").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title1"))
                .andExpect(jsonPath("$[1].title").value("Title2"));
    }

    @Test
    @WithMockUser(username = "00000000-0000-0000-0000-000000000001", roles = {"USER"})
    void testDeleteNotificationSuccess() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID notificationId = UUID.randomUUID();
        Mockito.doNothing().when(notificationService).deleteNotification(notificationId, userId);

        mockMvc.perform(delete("/notifications/" + notificationId).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "00000000-0000-0000-0000-000000000001", roles = {"USER"})
    void testDeleteNotificationNotFound() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID notificationId = UUID.randomUUID();
        Mockito.doThrow(new Exception("Notification not found"))
                .when(notificationService).deleteNotification(notificationId, userId);

        mockMvc.perform(delete("/notifications/" + notificationId).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Notification not found"));
    }
}
