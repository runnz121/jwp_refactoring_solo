package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

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

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@WebMvcTest(TableGroupRestController.class)
@DisplayName("TableGroupRestControllerTest")
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    @Nested
    @DisplayName("POST /api/table-groups")
    class Create {

        @Nested
        @DisplayName("테이블 그룹이 주어지면")
        class CreateTableGroup {
            TableGroup givenTableGroup = new TableGroup();

            @BeforeEach
            void setUp() {
                OrderTable orderTable1 = new OrderTable();
                orderTable1.setId(1L);
                OrderTable orderTable2 = new OrderTable();
                orderTable2.setId(2L);
                givenTableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

                when(tableGroupService.create(any(TableGroup.class)))
                    .thenReturn(givenTableGroup);

            }

            @DisplayName("201과 함께 테이블 그룹을 생성하여 반환한다.")
            @Test
            void returnTableGroup() throws Exception {
                mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(givenTableGroup)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(
                        objectMapper.writeValueAsString(givenTableGroup)
                    ));
            }
        }
    }

    @Nested
    @DisplayName("DELETE /api/table-groups/{tableGroupId}")
    class Ungroup {

        @Nested
        @DisplayName("테이블 그룹 아이디가 주어지면")
        class UnGroupTable {

            final Long givenTableGroupId = 1L;

            @DisplayName("빈 응답과 함께 테이블 그룹을 해제한다")
            @Test
            void unGroupTable() throws Exception {
                mockMvc.perform(delete("/api/table-groups/{tableGroupId}", givenTableGroupId))
                    .andExpect(status().isNoContent());
            }
        }
    }
}