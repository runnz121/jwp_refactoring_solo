package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menu.service.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.ui.MenuGroupRestController;

@WebMvcTest(MenuGroupRestController.class)
@DisplayName("MenuGroupRestController 클래스 테스트 ")
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @Nested
    @DisplayName("POST /api/menu-groups")
    class Create {

        @Nested
        @DisplayName("등록할 메뉴 그룹이 주어지면")
        class MenuGroupClass {
            MenuGroup givenMenuGroup = new MenuGroup();

            @BeforeEach
            void setUp() {
                // given
                givenMenuGroup.setName("추천 메뉴");
                // saved
                MenuGroup savedMenuGroup = new MenuGroup();
                savedMenuGroup.setId(1L);
                savedMenuGroup.setName("추천 메뉴");
                when(menuGroupService.create(any(MenuGroup.class)))
                    .thenReturn(savedMenuGroup);
            }

            @DisplayName("201 과 메뉴 그룹을 리턴한다")
            @Test
            void createdAndReturnMenuGroup() throws Exception {
                mockMvc.perform(post("/api/menu-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(givenMenuGroup)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("id").exists())
                    .andExpect(jsonPath("name").exists());
            }
        }
    }

    @Nested
    @DisplayName("GET /api/menu-groups")
    class GetGroups {

        @Nested
        @DisplayName("메뉴 그륩을 반환한다")
        class GetAndReturnGroups {

            List<MenuGroup> menuGroups;

            @BeforeEach
            void setUp() {
                MenuGroup menuGroup = new MenuGroup();
                menuGroups = Arrays.asList(menuGroup);
                when(menuGroupService.list())
                    .thenReturn(menuGroups);
            }

            @DisplayName("메뉴그륩 리스트를 리턴한다")
            @Test
            void returnMenuGroupLists() throws Exception {
                mockMvc.perform(get("/api/menu-groups"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(
                        objectMapper.writeValueAsString(menuGroups)
                    ));
            }
        }
    }
}