package sample.cafekiosk.spring.docs;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
//@SpringBootTest -> MockMvcBuilders.webAppContextSetup 정의 필요
public abstract class RestDocsSupport {

    protected MockMvc mockMvc;

    // 스프링 의존성이 없기 때문에 새로 만들어줌
    protected ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
                .apply(documentationConfiguration(provider))
                .build();
    }

    // 하위 구현 클래스에서 initController를 구현하여 Controller를 반환할 수 있도록 함
    protected abstract Object initController();
}
