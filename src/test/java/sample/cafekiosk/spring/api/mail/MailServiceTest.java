package sample.cafekiosk.spring.api.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.histody.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.histody.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Spy // Mock객체가 아닌 실제 객체를 받음
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // given
        /*
        MailSendClient mailSendClient = mock(MailSendClient.class);
        MailSendHistoryRepository mailSendHistoryRepository = mock(MailSendHistoryRepository.class);
        MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);
        */
            // mock 객체 생성 후 주입
        /* @Spy 환경에서는 작동 X
         when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);
        */

        doReturn(true)
                .when(mailSendClient)
                .sendEmail(any(String.class), any(String.class), any(String.class), any(String.class));

        // when
        boolean result = mailService.sendMail("", "", "", "");

        // then
        assertThat(result).isTrue();

        //Mock 객체의 어떤 행위가 몇번 일어났는지 검증하는 기능
        verify(mailSendClient, times(1)).sendEmail(any(String.class), any(String.class), any(String.class), any(String.class));
        verify(mailSendHistoryRepository, times(1))
                .save(any(MailSendHistory.class));
    }


}