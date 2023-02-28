package myproject.app.service;

import java.util.List;

// 프레임워크에 탑재된 기능 가져오기
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.ByteBuffer;

// 자바 가지고 있는 내장 모듈
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.List;
import java.util.Iterator;
import java.lang.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Value;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Service
public class SlackService {
    @Value(value = "${SLACK_ERROR_HOOK_URL}")
    String ErrorUrl;

    @Value(value = "${SLACK_LOG_HOOK_URL}")
    String LogUrl;

    @Value(value = "${SLACK_LOGIN_HOOK_URL}")
    String LoginkUrl;

    public void SendErrorChannel(String location,String message) throws Exception{
        try {
            String text = "발생한 위치 : " + location + "\n" + "발생한 에러 : " + message;
            WebhookResponse response = null;
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder().text(text).build();
            response = slack.send(ErrorUrl, payload);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void LoggingChannel(String location) throws Exception{
        try {
            String text = "발생한 위치 : " + location;
            WebhookResponse response = null;
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder().text(text).build();
            response = slack.send(LogUrl, payload);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void LoginChannel(String user) throws Exception{
        try {
            String text = user + " 님이 로그인하셨습니다.";
            WebhookResponse response = null;
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder().text(text).build();
            response = slack.send(LoginkUrl, payload);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}