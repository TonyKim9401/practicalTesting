package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.forDisplay;

/**
 * readOnly = true : 읽기전용
 * CRUD 에서 CUD 동작 X / only Read
 * JPA : CUD 스냅샷 저장, 변경감지 X (성능향상)
 *
 * CQRS - Command / Query -> CUD와 R의 비율이 통상 2:8로 Read의 비율이 압도적으로 높다.
 */

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 동시성 이슈
    // ex)한번에 여러 물건이 등록될 경우 -> 해당 컬럼을 유니크로 잡아두고, 실패시 최대 3회까지 재시도 하는 등
    // ex) UUID 사용
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        // productNumber 부여
        // 001 002 003 004
        // DB 에서 마지막 저장된 Product의 상품번호를 읽어와서 +1
        // 009 -> 010
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }


    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    private String createNextProductNumber() {
        String lastedProductNumber = productRepository.findLastedProductNumber();
        if (lastedProductNumber == null){
            return "001";
        }

        int lastProductNumberInt = Integer.parseInt(lastedProductNumber);
        int nextProductNumberInt = lastProductNumberInt + 1;
        // 9 -> 009, 10 -> 010
        return String.format("%03d", nextProductNumberInt);
    }

}
