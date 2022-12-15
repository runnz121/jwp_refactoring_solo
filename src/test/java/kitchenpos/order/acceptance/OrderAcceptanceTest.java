package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.order.acceptance.TableAcceptanceTest.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("주문 관련 기능 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 양식;
    private MenuResponse 양식_세트;
    private OrderTableResponse 빈_주문테이블;
    private OrderTableResponse 비어있지_않은_주문테이블;

    private Long notExistOrderTableId = -1L;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양식 = 메뉴_그룹_생성_요청("양식").as(MenuGroupResponse.class);
        양식_세트 = 메뉴_생성_요청("양식 세트", new BigDecimal(0), 양식.getId(),
                Collections.emptyList()).as(MenuResponse.class);
        빈_주문테이블 = 주문_테이블_생성(0, true).as(OrderTableResponse.class);
        비어있지_않은_주문테이블 = 주문_테이블_생성(2, false).as(OrderTableResponse.class);
    }

    /**
     * When 등록되어 있지 않은 주문 테이블에서 주문 요청하면
     * Then 주문할 수 없다.
     */
    @DisplayName("등록되어 있지 않은 주문 테이블에서 주문한다.")
    @Test
    void createOrderWithNullOrderTable() {
        // when
        ExtractableResponse<Response> response =
                주문_생성(notExistOrderTableId,
                        Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)));

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * When 등록되지 않은 메뉴를 포함해 주문 요청하면
     * Then 주문할 수 없다.
     */
    @DisplayName("등록되지 않은 메뉴를 포함해 주문한다.")
    @Test
    void createOrderWithNullMenu() {
        // when
        ExtractableResponse<Response> response =
                주문_생성(notExistOrderTableId,
                        Arrays.asList(new OrderLineItemRequest(notExistOrderTableId, 1L)));

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹이 등록되어 있고
     * And 메뉴가 등록되어 있고
     * And 비어있는 주문 테이블이 생성되어 있고
     * When 주문을 요청하면
     * Then 주문할 수 없다.
     */
    @DisplayName("빈 주문 테이블에서 주문한다.")
    @Test
    void createOrderWithEmptyOrderTable() {
        // when
        ExtractableResponse<Response> response =
                주문_생성(빈_주문테이블.getId(),
                        Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)));

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹이 등록되어 있고
     * And 메뉴가 등록되어 있고
     * And 주문 테이블이 생성되어 있고
     * When 주문을 요청하면
     * Then 주문할 수 있다.
     */
    @DisplayName("주문한다.")
    @Test
    void createOrder() {
        // when
        ExtractableResponse<Response> response =
                주문_생성(비어있지_않은_주문테이블.getId(),
                        Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)));

        // then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹이 등록되어 있고
     * And 메뉴가 등록되어 있고
     * And 주문 테이블이 생성되어 있고
     * And 주문이 생성되어 있고
     * When 주문 목록을 조회하면
     * Then 주문 목록을 조회할 수 있다.
     */
    @DisplayName("주문 목록을 조회한다.")
    @Test
    void listOrder() {
        // given
        주문_생성(비어있지_않은_주문테이블.getId(),
                Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)));

        // when
        ExtractableResponse<Response> response = 주문_목록_조회();

        // then
        assertThat(response.jsonPath().getList(".", OrderResponse.class)).hasSize(1);
    }

    /**
     * When 등록되지 않은 주문의 주문 상태를 변경 요청하면
     * Then 주문 상태를 변경할 수 없다.
     */
    @DisplayName("등록되지 않은 주문의 주문 상태를 변경한다.")
    @Test
    void changeOrderStatusWithNullOrder() {
        // when
        ExtractableResponse<Response> response = 주문_상태_변경(notExistOrderTableId, OrderStatusRequest.of(OrderStatus.MEAL));

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹이 등록되어 있고
     * And 메뉴가 등록되어 있고
     * And 주문 테이블이 생성되어 있고
     * And 주문이 생성되어 있고
     * When 이미 완료된 주문의 주문 상태를 변경 요청하면
     * Then 주문 상태를 변경할 수 없다.
     */
    @DisplayName("이미 완료된 주문의 주문 상태를 변경한다.")
    @Test
    void changeOrderStatusWithCompletionOrder() {
        // given
        OrderResponse response = 주문_생성(비어있지_않은_주문테이블.getId(),
                        Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)))
                .as(OrderResponse.class);
        response = 주문_상태_변경(response.getId(), OrderStatusRequest.of(OrderStatus.COMPLETION))
                .as(OrderResponse.class);

        // when
        ExtractableResponse<Response> changed = 주문_상태_변경(response.getId(), OrderStatusRequest.of(OrderStatus.COOKING));

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), changed.statusCode());
    }

    /**
     * Given 메뉴 그룹이 등록되어 있고
     * And 메뉴가 등록되어 있고
     * And 주문 테이블이 생성되어 있고
     * And 주문이 생성되어 있고
     * When 주문 상태를 변경 요청하면
     * Then 주문 상태를 변경할 수 있다.
     */
    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderResponse response = 주문_생성(비어있지_않은_주문테이블.getId(),
                        Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)))
                .as(OrderResponse.class);

        // when
        OrderResponse changed = 주문_상태_변경(response.getId(), OrderStatusRequest.of(OrderStatus.COMPLETION))
                .as(OrderResponse.class);

        // then
        assertEquals(OrderStatus.COMPLETION, changed.getOrderStatus());
    }
    
    public static ExtractableResponse<Response> 주문_생성(Long orderTableId,
                                                             List<OrderLineItemRequest> orderLineItems) {
        OrderRequest request = new OrderRequest(orderTableId, OrderStatus.COOKING, orderLineItems);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 주문_상태_변경(Long orderId, OrderStatusRequest orderStatus) {
        return RestAssured.given().log().all()
                .body(orderStatus)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/orders/" + orderId + "/order-status")
                .then().log().all()
                .extract();
    }
}
