package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
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

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@WebMvcTest(MenuRestController.class)
@DisplayName("MenuRestControllerTest")
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @Nested
    @DisplayName("POST /api/menus")
    class Create {

        Menu givenMenu = new Menu();

        @BeforeEach
        void setUp() {
            //given
            givenMenu.setId(1L);
            givenMenu.setName("추천메뉴");
            givenMenu.setPrice(new BigDecimal(1000));
            givenMenu.setMenuGroupId(1L);
            givenMenu.setMenuProducts(Arrays.asList(new MenuProduct()));

            //when
            Menu savedMenu = new Menu();
            savedMenu.setId(1L);
            savedMenu.setName("추천메뉴");
            savedMenu.setPrice(new BigDecimal(1000));
            savedMenu.setMenuGroupId(1L);
            savedMenu.setMenuProducts(Arrays.asList(new MenuProduct()));
            when(menuService.create(any(Menu.class)))
                .thenReturn(savedMenu);
        }

        @DisplayName("201과 메뉴를 반환한다")
        @Test
        void createAndReturnMenu() throws Exception {
            mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenMenu)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("price").exists())
                .andExpect(jsonPath("menuGroupId").exists())
                .andExpect(jsonPath("menuProducts").exists());
        }
    }

    @Nested
    @DisplayName("GET /api/menus")
    class Lists {

        @Nested
        @DisplayName("메뉴 그륩 리스틀 반환한다")
        class GetMenuList {

            List<Menu> menuList;

            @BeforeEach
            void setUp() {
                Menu menu = new Menu();
                menuList = Arrays.asList(menu);
                when(menuService.list())
                    .thenReturn(menuList);
            }

            @DisplayName("200 과 메뉴 리스트를 반환한다")
            @Test
            void getAndReturnMenuList() throws Exception {
                mockMvc.perform(get("/api/menus"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(
                        objectMapper.writeValueAsString(menuList)
                    ));
            }
        }
    }
}