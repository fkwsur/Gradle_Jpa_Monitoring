package myproject.app.controller;

import myproject.app.models.*;
import myproject.app.models.ExcelRiskAlarm.AlarmEntity;
import myproject.app.service.*;

// 프레임워크에 탑재된 기능 가져오기
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
// 자바 가지고 있는 내장 모듈
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.lang.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

@RestController // restApi를 작성할 수 있는 컨트롤러
@RequestMapping("/api/v1") // url을 api로 지정
@CrossOrigin(origins = "*", allowedHeaders = "*") // cors허용
@RequiredArgsConstructor
public class MongodbController {

    private final MongodbService mongodbService;
    private final UserService userService;
    private final JWTManager jwtManager;
    private final Bcrypt bcrypt;
    private final SlackService slack;

    @GetMapping("/hello")
    public ResponseEntity<Object> Hello() {
        try {
            Map<String, Object> map = new HashMap<>();
            List<CarliftLog> result = mongodbService.findAll("site5");
            map.put("result", result);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
 
    }

    @GetMapping("/carlift/realtimelog")
    public ResponseEntity<Object> RealTimeLog(@RequestHeader String xauth, @RequestParam String Site_Name, Integer page ) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            User getAuth = userService.SelectUser(decoded);
            if("승인대기".equals(getAuth.getGrade())){
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
            }
            Integer offset = 0;
            if (page > 1) {
                offset = 20 * (page - 1);
            }

            List<CarliftLog> result = mongodbService.RealTimeLog(Site_Name,offset);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("carlift -realtimelog",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
 
    }

    @GetMapping("/carlift/excelcarliftlog")
    public ResponseEntity<Object> ExcelCarLiftLog(@RequestHeader String xauth, @RequestParam String Site_Name) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            User getAuth = userService.SelectUser(decoded);
            if("승인대기".equals(getAuth.getGrade())){
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
            }
            List<ExcelCarLiftLog> result = mongodbService.ExcelCarLiftLog(Site_Name);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("carlift -excelcarliftlog",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
 
    }

    @GetMapping("/carlift/excelriskalarm")
    public ResponseEntity<Object> ExcelRiskAlarm(@RequestHeader String xauth, @RequestParam String Site_Name) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            User getAuth = userService.SelectUser(decoded);
            if("승인대기".equals(getAuth.getGrade())){
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
            }
            List<ExcelRiskAlarm> rows = mongodbService.ExcelRiskAlarm(Site_Name);
            Stack<Object> result = new Stack<Object>();
            for(ExcelRiskAlarm vo : rows) {
                Map<String, Object> map3 = new LinkedHashMap<>();
                map3.put("Site_Name", vo.getSiteName());
                map3.put("CarLift", vo.getCarLift());
                map3.put("Gate", vo.getGate());
                if(vo.getAlarm() != null ){
                    ObjectMapper mapper = new ObjectMapper();
                    List<AlarmEntity> alarmList = vo.getAlarm();
                    String str = mapper.writeValueAsString(alarmList);
                    str = str.replace("[", "");
                    str = str.replace("]", "");
                    map3.put("Alarm", str);
                }else{
                    map3.put("Alarm", null);

                }
                result.push(map3);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("carlift -excelriskalarm",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/chatting/getchatting")
    public ResponseEntity<Object> GetChatting(@RequestHeader String xauth, @RequestParam String roomCode, Integer page ) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            User getAuth = userService.SelectUser(decoded);
            if("승인대기".equals(getAuth.getGrade())){
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
            }
            Integer offset = 0;
            if (page > 1) {
                offset = 20 * (page - 1);
            }
            List<Chatting> result = mongodbService.GetChatting(roomCode,offset);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("chatting -getchatting",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

}
