package myproject.app.controller;

import myproject.app.models.*;
import myproject.app.service.*;

// 프레임워크에 탑재된 기능 가져오기
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.ByteBuffer;

// 자바 가지고 있는 내장 모듈
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.List;
import java.util.Iterator;
import java.lang.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController // restApi를 작성할 수 있는 컨트롤러
@RequestMapping("/api/v1/user") // url을 api로 지정
@CrossOrigin(origins = "*", allowedHeaders = "*") // cors허용
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTManager jwtManager;
    private final Bcrypt bcrypt;
    private final Mail mail;
    private final SlackService slack;

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> Hello() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("result", "hello world");
        // 배열 만드는 법
        // Stack<String> emailArray = new Stack<String>();
        //     for(User vo : emails) {
        //         emailArray.push(vo.getEmail());
        //     }
        slack.SendErrorChannel("hi","에러에용");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> SignUp(@RequestBody User req) throws Exception{
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();

            // id 생성
            UUID uuid = UUID.randomUUID();
            long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
            String shortUUID = Long.toString(l, Character.MAX_RADIX);
            // id값 varchar(20)으로 늘려놈
            req.setId(shortUUID);

            // 비밀번호 암호화
            String hashpassword = bcrypt.HashPassword(req.getAppKey());
            req.setAppKey(hashpassword);

            // 토큰 발급
            String rxauth = jwtManager.CreateRToken(shortUUID);
            req.setRefreshtoken(rxauth);

            if ("admin".equals(req.getUserId()) && "admin@gmail.com".equals(req.getEmail())) {
                req.setGrade("관리자");
            } else {
                req.setGrade("승인대기");
            }
            Boolean result = userService.SignUp(req);
            if (result == true) {
                map.put("result", true);
            } else {
                map.put("error", map2);
                map2.put("text", "데이터가 존재하지 않습니다.");
            }
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -signup",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/checkuserid")
    public ResponseEntity<Object> CheckUserId(@RequestParam String user_id) throws Exception {
        try {
            Map<String, Boolean> map = new HashMap<>();
            boolean result = userService.CheckUserId(user_id);
            if (result == true) {
                map.put("result", false);
            } else {
                map.put("result", true);
            }
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -checkuserid",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/emailauth")
    public ResponseEntity<Object> EmailAuth(@RequestBody User req) throws Exception {
        try {
            Map<String, String> map = new HashMap<>();
            boolean result = userService.EmailAuth(req.getEmail());
            double auth = Math.random();
            String authCode = Double.toString(auth).substring(2, 6);
            if (result == true) {
                map.put("result", "이미 가입되어있는 이메일입니다.");
            } else {
                new Thread() {
                    public void run() {
                        try {
                            mail.CheckMail(req.getEmail(), authCode);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                    }
                }.start();
                map.put("result", authCode);
            }
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -emailauth",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/senduserid")
    public ResponseEntity<Object> SendUserId(@RequestBody User req) throws Exception {
        try {
            Map<String, String> map = new HashMap<>();
            User User = userService.SendUserId(req.getEmail());
            if (User == null) {
                map.put("result", "승인된 이메일이 아닙니다.");
            } else {
                new Thread() {
                    public void run() {
                        try {
                            mail.SendId(req.getEmail(), User.getUserId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }.start();
                map.put("result", "success");
            }
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -senduserid",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/sendpassword")
    public ResponseEntity<Object> SendPassword(@RequestBody User req) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, String> map2 = new HashMap<>();

            int leftLimit = 48; // numeral '0'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
            String authCode = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            // 비밀번호 암호화
            String hashpassword = bcrypt.HashPassword(authCode);

            User result = userService.SendPassword(req.getUserId(), hashpassword);
            
            if (result == null) {
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                new Thread() {
                    public void run() {
                        try {
                                mail.SendPassword(result.getEmail(), authCode);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }.start();
                map.put("result", "success");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -sendpassword",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> SignIn(@RequestBody User req) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, String> map2 = new HashMap<>();
            User result = userService.SelectUser(req.getUserId());
            if (result == null) {
                map.put("error", map2);
                map2.put("text", "없는 아이디입니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            Boolean checking = bcrypt.CompareHash(req.getAppKey(), result.getAppKey());

            if (checking) {
                String token = jwtManager.CreateToken(result.getUserId());
                String rxauth = jwtManager.CreateRToken(result.getId());

                result.setRefreshtoken(rxauth);
                map.put("xauth", token);
                map.put("rxauth", rxauth);

            } else {
                map.put("error", map2);
                map2.put("text", "비밀번호가 일치하지 않습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            Boolean updateInfo = userService.UpdateInfo(result);
            if (updateInfo != true) {
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            map.put("rows", result);
            slack.LoginChannel(result.getUserId());
            return new ResponseEntity<>(map, HttpStatus.OK);

        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -signin",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/getuserinfo")
    public ResponseEntity<Object> GetUserInfo(@RequestHeader String xauth) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            User result = userService.SelectUser(decoded);
            if (result == null) {
                map.put("error", map2);
                map2.put("text", "토큰이 만료되었거나 없는 아이디입니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -getuserinfo",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/getuserlist")
    public ResponseEntity<Object> GetUserList(@RequestHeader String xauth, @RequestParam String list_type,
            Integer page) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            Integer offset = 0;
            if (page > 1) {
                offset = 20 * (page - 1);
            }
            User getAuth = userService.IsAdminUser(decoded, "관리자");
            if (getAuth == null) {
                map.put("error", map2);
                map2.put("text", "토큰이 만료되었거나 관리자가 아닙니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            if (list_type.equals("all")) {
                List<Map<String, Object>> findListType = userService.findAllType(offset);
                return new ResponseEntity<>(findListType, HttpStatus.OK);
            } else if (list_type.equals("unaccepted")) {
                String grade = "승인대기";
                List<Map<String, Object>> findUnaccepted = userService.findType(grade, offset);
                return new ResponseEntity<>(findUnaccepted, HttpStatus.OK);
            } else if (list_type.equals("accepted")) {
                String grade = "일반";
                List<Map<String, Object>> findUnaccepted = userService.findType(grade, offset);
                return new ResponseEntity<>(findUnaccepted, HttpStatus.OK);
            } else if (list_type.equals("admin")) {
                String grade = "관리자";
                List<Map<String, Object>> findUnaccepted = userService.findType(grade, offset);
                return new ResponseEntity<>(findUnaccepted, HttpStatus.OK);
            } else {
                map.put("error", map2);
                map2.put("text", "등급 데이터 형식이 맞지 않습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -getuserlist",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/updateuserinfo")
    public ResponseEntity<Object> UpdateUserInfo(@RequestHeader String xauth, @RequestBody User req) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            User result = userService.UpdateUserInfo(decoded, req);
            if (result == null) {
                map.put("error", map2);
                map2.put("text", "토큰이 만료되었거나 없는 아이디입니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -updateuserinfo",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/updatepassword")
    public ResponseEntity<Object> UpdatePassword(@RequestHeader String xauth,
            @RequestBody Map<String, String> req) throws Exception {
        try {
            Map<String,Object> map = new HashMap<>();
            Map<String, String> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            User result = userService.SelectUser(decoded);
            if (result == null) {
                map.put("error", map2);
                map2.put("text", "토큰이 만료되었거나 없는 아이디입니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Boolean checking = bcrypt.CompareHash(req.get("password"), result.getAppKey());
            if (checking == true) {
                String hashpassword = bcrypt.HashPassword(req.get("newPassword"));
                result.setAppKey(hashpassword);
                Boolean updateInfo = userService.UpdateInfo(result);
                if (updateInfo != true) {
                    map.put("error", map2);
                    map2.put("text", "정의되지 않은 오류입니다.");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
                map.put("result", "비밀번호가 변경되었습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                map.put("result", "기존 비밀번호가 일치하지 않습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -updatepassword",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/unregister")
    public ResponseEntity<Object> UnRegister(@RequestHeader String xauth, @RequestBody Map<String, String> req) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, String> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            User result = userService.SelectUser(decoded);
            if (result == null) {
                map.put("error", map2);
                map2.put("text", "토큰이 만료되었거나 없는 아이디입니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Boolean checking = bcrypt.CompareHash(req.get("password"), result.getAppKey());
            if (checking == true) {
                userService.UnRegister(decoded);
                map.put("result", "계정이 삭제되었습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                map.put("result", "비밀번호가 일치하지 않습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -unregister",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/changeauthority")
    public ResponseEntity<Object> ChangeAuthority(@RequestHeader String xauth, @RequestBody Map<String, String> req) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            User getAuth = userService.IsAdminUser(decoded, "관리자");
            if (getAuth == null) {
                map.put("error", map2);
                map2.put("text", "토큰이 만료되었거나 관리자가 아닙니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            // id 리스트 만들어서 이메일 찾아오기
            List<String> userIdList = Stream.of(req.get("id").split(",")).collect(Collectors.toList());
            List<User> emails = userService.FindByIdIn(userIdList);
            if(!"계정삭제".equals(req.get("authority"))){
                userService.BulkUpdateInfo(req.get("authority"),userIdList);
                map.put("result", "success");
            }else if("계정삭제".equals(req.get("authority"))){
                userService.BulkDeleteInfo(userIdList);
                map.put("result", "success");
            }else{
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
            }
            new Thread() {
                public void run() {
                    try {
                        for(User vo : emails) {
                            if(vo.getEmail() != null ){
                            mail.ChangeAuth(vo.getEmail(), req.get("authority"));
                            }
                        }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }.start();
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -changeauthority",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/searchusername")
    public ResponseEntity<Object> SearchUserName(@RequestHeader String xauth, @RequestParam String keywords, Integer page) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            Integer offset = 0;
            if (page > 1) {
                offset = 20 * (page - 1);
            }
            User getAuth = userService.IsAdminUser(decoded, "관리자");
            if (getAuth == null) {
                map.put("error", map2);
                map2.put("text", "토큰이 만료되었거나 관리자가 아닙니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            List<Map<String, Object>> searchUserName = userService.SearchUserName(keywords, offset);
            return new ResponseEntity<>(searchUserName, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -searchusername",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/autoissuetoken")
    public ResponseEntity<Object> AutoIssueToken(@RequestHeader String rxauth) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            String id = jwtManager.VerifyRToken(rxauth);
            User rows = userService.findByIdAndRefreshtoken(id, rxauth);
            if (rows == null) {
                map.put("error", map2);
                map2.put("text", "토큰이 만료되었습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("rows", rows);
            String token = jwtManager.CreateToken(rows.getUserId());
            String refreshToken = jwtManager.CreateRToken(rows.getId());
            rows.setRefreshtoken(refreshToken);
            Boolean result = userService.UpdateInfo(rows);
            if (result != true) {
                map.put("error", map2);
                map2.put("text", "아이디가 일치하지 않습니다.");
            }
            map.put("xauth", token);
            map.put("rxauth", refreshToken);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -autoissuetoken",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/issuetoken")
    public ResponseEntity<Object> IssueToken(@RequestHeader String rxauth) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            String id = jwtManager.VerifyRToken(rxauth);
            User rows = userService.findByIdAndRefreshtoken(id, rxauth);
            if (rows == null) {
                map.put("result", "failed");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            String token = jwtManager.CreateToken(rows.getUserId());
            map.put("xauth", token);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("user -issuetoken",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

}