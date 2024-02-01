package az.projectdailyreport.projectdailyreport.unit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class Mail {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");  // E-posta sağlayıcınızın SMTP bilgilerini buraya ekleyin
        mailSender.setPort(587);  // E-posta sağlayıcınızın SMTP port bilgisini buraya ekleyin

        mailSender.setUsername("eliyev.99.18@gmail.com");
        mailSender.setPassword("kkqthecytbndxehb");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
