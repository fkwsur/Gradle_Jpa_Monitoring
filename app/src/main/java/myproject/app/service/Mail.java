package myproject.app.service;

import javax.mail.PasswordAuthentication;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
public class Mail {
    
    @Value("${spring.mail.username}")
    String Sender;
    
    @Value("${spring.mail.password}")
    String SenderPassword;

    @Value("${spring.mail.port}")
    String Port;

    @Value("${spring.mail.host}")
    String Host;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    String Enable;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    String Auth;

    public void CheckMail(String Receiver,String authCode) throws Exception{      
        Properties props = new Properties();
        props.put("mail.smtp.auth", Auth);
        props.put("mail.smtp.ssl.trust", Host);
        props.put("mail.smtp.starttls.enable", Enable);
        props.put("mail.smtp.host", Host);
        props.put("mail.smtp.port", Port);

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Sender, SenderPassword);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);     
            message.setFrom(new InternetAddress(Sender)); // from
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(Receiver));
            message.setSubject("인증메일입니다.");
            message.setText("인증 번호 " + authCode + " 를 입력해주세요.");
            Transport.send(message);
            System.out.println("mail send ok");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void SendId(String Receiver,String user_id) throws Exception{      
        Properties props = new Properties();
        props.put("mail.smtp.auth", Auth);
        props.put("mail.smtp.ssl.trust", Host);
        props.put("mail.smtp.starttls.enable", Enable);
        props.put("mail.smtp.host", Host);
        props.put("mail.smtp.port", Port);

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Sender, SenderPassword);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);     
            message.setFrom(new InternetAddress(Sender)); // from
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(Receiver));
            message.setSubject(" 가입하신 아이디 정보입니다.");
            message.setText("본 이메일로 가입된 계정의 아이디는 " + user_id + " 입니다.");
            Transport.send(message);
            System.out.println("mail send ok");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void SendPassword(String Receiver,String authCode) throws Exception{      
        Properties props = new Properties();
        props.put("mail.smtp.auth", Auth);
        props.put("mail.smtp.ssl.trust", Host);
        props.put("mail.smtp.starttls.enable", Enable);
        props.put("mail.smtp.host", Host);
        props.put("mail.smtp.port", Port);

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Sender, SenderPassword);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);     
            message.setFrom(new InternetAddress(Sender)); // from
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(Receiver));
            message.setSubject(" 임시 비밀번호가 발급되었습니다.");
            message.setText("임시비밀번호는 " + authCode + " 입니다. 마이페이지에서 비밀번호를 변경해주세요.");
            Transport.send(message);
            System.out.println("mail send ok");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void ChangeAuth(String Receiver,String authority) throws Exception{      
        Properties props = new Properties();
        props.put("mail.smtp.auth", Auth);
        props.put("mail.smtp.ssl.trust", Host);
        props.put("mail.smtp.starttls.enable", Enable);
        props.put("mail.smtp.host", Host);
        props.put("mail.smtp.port", Port);

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Sender, SenderPassword);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);     
            message.setFrom(new InternetAddress(Sender)); // from
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(Receiver));
            if(authority.equals("계정삭제")){
                message.setSubject(" 계정이 삭제되었습니다.");
                message.setText("사용자 계정이 관리자에 의해서 삭제되었습니다.");
            }else{
                message.setSubject(" 권한이 변경되었습니다.");
                message.setText("사용자 등급이 " + authority + "로 변경되었습니다.");
            }
            Transport.send(message);
            System.out.println("mail send ok");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
