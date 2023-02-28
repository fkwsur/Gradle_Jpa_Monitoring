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

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

@RestController // restApi를 작성할 수 있는 컨트롤러
@RequestMapping("/api/v1/site") // url을 api로 지정
@CrossOrigin(origins = "*", allowedHeaders = "*") // cors허용
@RequiredArgsConstructor
public class SiteController {
    
    private final UserService userService;
    private final SiteService siteService;
    private final JWTManager jwtManager;
    private final Bcrypt bcrypt;
    private final SlackService slack;

    @Value("${spring.mail.username}")
    String K;

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> Hello() throws Exception {
        System.out.println(K);
        Map<String, String> map = new HashMap<>();

        map.put("result", "hello world2");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/getsiteinfo")
    public ResponseEntity<Object> GetSiteInfo(@RequestHeader String xauth, @RequestParam String id) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            User getAuth = userService.SelectUser(decoded);
            if("승인대기".equals(getAuth.getGrade())){
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
            }
            List<String> siteIdList = Stream.of(id.split(",")).collect(Collectors.toList());
            List<Site> result = siteService.FindByIdIn(siteIdList);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -getsiteinfo",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/getsitelist")
    public ResponseEntity<Object> GetSiteList(@RequestHeader String xauth, @RequestParam Integer page) throws Exception {
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
            List<Map<String, Object>> getSiteList = siteService.GetSiteList(offset);
            return new ResponseEntity<>(getSiteList, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -getsitelist",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/getsitelistnopage")
    public ResponseEntity<Object> GetSiteListNopage() throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            List<Site> getSiteList = siteService.GetSiteListNopage();
            return new ResponseEntity<>(getSiteList, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -getsitelistnopage",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/addsite")
    public ResponseEntity<Object> AddSite(@RequestHeader String xauth, @RequestBody Site req) throws Exception {
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

            // id 생성
            UUID uuid = UUID.randomUUID();
            long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
            String shortUUID = Long.toString(l, Character.MAX_RADIX);
            // id값 varchar(20)으로 늘려놈
            req.setId(shortUUID);

            Boolean result = siteService.AddSite(req);
            if (result == true) {
                map.put("result", "success");
            } else {
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
            }
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -addsite",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/updatesite")
    public ResponseEntity<Object> UpdateSite(@RequestHeader String xauth, @RequestBody Site req) throws Exception {
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

            Site result = siteService.UpdateSite(req);
            if (result == null) {
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("result", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -updatesite",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/deletesite")
    public ResponseEntity<Object> DeleteSite(@RequestHeader String xauth, @RequestBody Site req) throws Exception {
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
            siteService.DeleteSite(req);
            siteService.UpdateUserSite(req.getId());            
            siteService.DeleteCctvBySiteId(req.getId());
            map.put("result", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -deletesite",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/searchsitename")
    public ResponseEntity<Object> SearchSiteName(@RequestHeader String xauth, @RequestParam String keywords, Integer page) throws Exception {
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
            Integer offset = 0;
            if (page > 1) {
                offset = 20 * (page - 1);
            }
            List<Map<String, Object>> rows = siteService.SearchSiteName(keywords,offset);     
            return new ResponseEntity<>(rows, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -searchsitename",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/updateusersite")
    public ResponseEntity<Object> UpdateUserSite(@RequestHeader String xauth, @RequestBody User req) throws Exception {
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
            List<String> userIdList = Stream.of(req.getId().split(",")).collect(Collectors.toList());
            userService.UpdateUserSite(req.getSiteId(),userIdList);   
            siteService.UpdateUserGrade();

            map.put("result", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -updateusersite",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/addcctv")
    public ResponseEntity<Object> AddCCTV(@RequestHeader String xauth, @RequestBody SiteCctv req) throws Exception {
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

            UUID uuid = UUID.randomUUID();
            long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
            String shortUUID = Long.toString(l, Character.MAX_RADIX);
            // id값 varchar(20)으로 늘려놈
            req.setId(shortUUID);

            Boolean result = siteService.AddCCTV(req);
            if (result == true) {
                map.put("result", "success");
            } else {
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
            }
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -addcctv",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/updatecctv")
    public ResponseEntity<Object> UpdateCCTV(@RequestHeader String xauth, @RequestBody SiteCctv req) throws Exception {
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

            SiteCctv result = siteService.UpdateCCTV(req);
            if (result == null) {
                map.put("error", map2);
                map2.put("text", "정의되지 않은 오류입니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("result", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -updatecctv",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/getcctvlist")
    public ResponseEntity<Object> GetCCTVList(@RequestHeader String xauth, @RequestParam String site_id) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            String decoded = jwtManager.VerifyToken(xauth);
            User getAuth = userService.SelectUser(decoded);
            if("승인대기".equals(getAuth.getGrade())){
                map.put("error", "정의되지 않은 오류입니다.");
            }
            List<SiteCctv> rows = siteService.GetCCTVList(site_id);
            return new ResponseEntity<>(rows, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -getcctvlist",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/deletecctv")
    public ResponseEntity<Object> DeleteCctv(@RequestHeader String xauth, @RequestBody SiteCctv req) throws Exception {
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

            siteService.DeleteCctv(req);

            map.put("result", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -deletecctv",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/getreplaylist")
    public ResponseEntity<Object> GetReplayList(@RequestHeader String xauth, @RequestParam String id, Integer page) throws Exception {
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
            List<Map<String, Object>> rows = siteService.findAndCountAll(id,offset);
            return new ResponseEntity<>(rows, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            slack.SendErrorChannel("site -getreplaylist",e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }


}
