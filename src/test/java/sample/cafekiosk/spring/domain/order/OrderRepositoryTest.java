package sample.cafekiosk.spring.domain.order;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;


@Transactional
class OrderRepositoryTest extends IntegrationTestSupport{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;



    @DisplayName("해당 일자에 결제 완료된 주문들을 가져온다")
    @Test
    void readPaymentCompletedOrdersAtSpecificDay() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 1000);
        Product product2 = createProduct("002", HANDMADE, HOLD,"카페라떼", 2000);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 3000);


        productRepository.saveAll(List.of(product1, product2, product3));

        List<Product> productYesterday = List.of(
                product1,
                product2
        );
        Order orderYesterday = Order.create(productYesterday, yesterday);
        orderYesterday.paymentCompleted(yesterday);

        List<Product> productToday1 = List.of(
                product1,
                product2,
                product3
        );
        List<Product> productToday2 = List.of(
                product1,
                product2
        );
        Order orderToday1 = Order.create(productToday1, now);
        Order orderToday2 = Order.create(productToday2, now);
        orderToday1.paymentCompleted(now.minusMinutes(20));
        orderToday2.paymentCompleted(now.plusMinutes(20));

        orderRepository.saveAll(List.of(orderYesterday, orderToday1, orderToday2));


        // when
        List<Order> orders = orderRepository.findOrdersBy(now.minusHours(2), now.plusDays(1), OrderStatus.PAYMENT_COMPLETED);

        // then
        assertThat(orders).hasSize(2);
        int orderTotalPrice = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();
        assertThat(orderTotalPrice).isEqualTo(9000);
    }


    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus selling, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(selling)
                .name(name)
                .price(price)
                .build();
    }
}