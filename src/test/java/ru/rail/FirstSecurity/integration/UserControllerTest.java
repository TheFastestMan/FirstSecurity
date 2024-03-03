package ru.rail.FirstSecurity.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import ru.rail.FirstSecurity.entity.User;
import ru.rail.FirstSecurity.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void listUsers_ShouldReturnUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/users"));
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    public void addUser_ShouldRedirectToUserPage() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("John Doe");

        when(userService.saveUser(any(User.class))).thenReturn(mockUser);

        mockMvc.perform(post("/users")
                        .param("username", "John Doe")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/users/*"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void updateUser_ShouldRedirectToUsersPage() throws Exception {
        Long userId = 1L;
        mockMvc.perform(post("/users/1/update", userId)
                        .param("username", "Aist1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    public void deleteUser_AsAdmin_ShouldBeAllowed() throws Exception {
        mockMvc.perform(post("/users/1/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }


}