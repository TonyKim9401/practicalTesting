package sample.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 직렬화 / 역직렬화를 도움
     * 대상 객체는 기본 생성자가 반드시 있어야함
     * -> @NoArgsConstructor
     */
    @Autowired
    private ObjectMapper objectMapper;

    // Mock으로 만든 ProductService Bean이 ProductController 에 주입되며 사용됨
    @MockBean
    private ProductService productService;

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        /**
         * request를 body에 넣게 되면 직렬화/역직렬화 과정을 거치게 됨
         */


        // when  // then
        //      api 를 쏘는 역할
        mockMvc.perform(
                    post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request)) // request 직렬화
                        .contentType(APPLICATION_JSON) // 컨텐트 타입
                )
                .andDo(print()) // 실행 상세 로그 확인
                .andExpect(status().isOk()); // 결과 상태 조회
    }

    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
    @Test
    void createProductWithoutType() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        /**
         * request를 body에 넣게 되면 직렬화/역직렬화 과정을 거치게 됨
         */


        // when  // then
        //      api 를 쏘는 역할
        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request)) // request 직렬화
                                .contentType(APPLICATION_JSON) // 컨텐트 타입
                )
                .andDo(print()) // 실행 상세 로그 확인
                .andExpect(status().isBadRequest())// 결과 상태 조회
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 상품을 등록할 때 상품 판매 상태는 필수값이다.")
    @Test
    void createProductWithoutSellingStatus() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .name("아메리카노")
                .price(4000)
                .build();
        /**
         * request를 body에 넣게 되면 직렬화/역직렬화 과정을 거치게 됨
         */


        // when  // then
        //      api 를 쏘는 역할
        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request)) // request 직렬화
                                .contentType(APPLICATION_JSON) // 컨텐트 타입
                )
                .andDo(print()) // 실행 상세 로그 확인
                .andExpect(status().isBadRequest())// 결과 상태 조회
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 판매 상태는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다.")
    @Test
    void createProductWithoutName() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .price(4000)
                .build();
        /**
         * request를 body에 넣게 되면 직렬화/역직렬화 과정을 거치게 됨
         */


        // when  // then
        //      api 를 쏘는 역할
        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request)) // request 직렬화
                                .contentType(APPLICATION_JSON) // 컨텐트 타입
                )
                .andDo(print()) // 실행 상세 로그 확인
                .andExpect(status().isBadRequest())// 결과 상태 조회
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 상품을 등록할 때 상품 가격은 양수이다.")
    @Test
    void createProductWithZeroPrice() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(0)
                .build();
        /**
         * request를 body에 넣게 되면 직렬화/역직렬화 과정을 거치게 됨
         */


        // when  // then
        //      api 를 쏘는 역할
        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request)) // request 직렬화
                                .contentType(APPLICATION_JSON) // 컨텐트 타입
                )
                .andDo(print()) // 실행 상세 로그 확인
                .andExpect(status().isBadRequest())// 결과 상태 조회
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        // given
        List<ProductResponse> result = List.of();
        when(productService.getSellingProducts()).thenReturn(result);

        // when  // then
        //      api 를 쏘는 역할
        mockMvc.perform(
                        get("/api/v1/products/selling")
//                                .queryParam("name", "이름")
                )
                .andDo(print()) // 실행 상세 로그 확인
                .andExpect(status().isOk())// 결과 상태 조회
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
    }

}