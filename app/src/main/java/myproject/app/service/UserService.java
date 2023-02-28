package myproject.app.service;

import java.util.List;
import java.util.Map;
import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import myproject.app.models.User;
import myproject.app.repository.UserRepository;
import myproject.app.repository.UserRepositoryImpl;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserRepositoryImpl userRepositoryImpl;

    @Autowired
    public UserService(UserRepository userRepository, UserRepositoryImpl userRepositoryImpl) {
        this.userRepository = userRepository;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    @Transactional
    public Boolean SignUp(User user) throws Exception {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            throw new Exception(e);
        }
    }

    public Boolean CheckUserId(String user_id) throws Exception {
        try {
            return userRepository.existsByUserId(user_id);
        } catch (Exception e) {
            // TODO: handle exception
            throw new Exception(e);
        }
    }

    public Boolean EmailAuth(String email) throws Exception {
        try {
            return userRepository.existsByEmail(email);
        } catch (Exception e) {
            // TODO: handle exception
            throw new Exception(e);
        }
    }

    public User SendUserId(String email) throws Exception {
        try {
            User user = userRepository.findByEmail(email);
            return user;
        } catch (Exception e) {
            // TODO: handle exception
            throw new Exception(e);
        }
    }

    @Transactional
    public User SendPassword(String user_id, String hashpassword) throws Exception {
        try {
            User user = userRepository.findByUserId(user_id);
            if (user == null) {
                return null;
            } else {
                user.setAppKey(hashpassword);
                userRepository.save(user);
                return user;
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public User SelectUser(String user_id) throws Exception {
        try {
            return userRepository.findByUserId(user_id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public Boolean UpdateInfo(User user) throws Exception {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public User IsAdminUser(String user_id, String grade) throws Exception {
        try {
            return userRepository.findByUserIdAndGrade(user_id, grade);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<Map<String, Object>> findAllType(Integer offset) throws Exception {
        try {
            List<Map<String, Object>> list = userRepository.findAllType(offset);
            return list;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<Map<String, Object>> findType(String grade, Integer offset) throws Exception {
        try {
            List<Map<String, Object>> list = userRepository.findType(grade, offset);
            return list;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public User UpdateUserInfo(String decoded, User req) throws Exception {
        try {
            User user = userRepository.findByUserId(decoded);
            if (user == null) {
                return null;
            } else {
                user.setName(req.getName());
                user.setNumber(req.getNumber());
                user.setDepartment(req.getDepartment());
                user.setPosition(req.getPosition());
                userRepository.save(user);
                return user;
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public void UnRegister(String decoded) throws Exception {
        try {
            userRepository.deleteByUserId(decoded);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public int BulkUpdateInfo(String authority,List<String> id) throws Exception {
        try {
            return userRepository.BulkUpdateInfo(authority,id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<User> FindByIdIn(List<String> id) throws Exception {
        try {
            return userRepository.findByIdIn(id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    @Transactional
    public int BulkDeleteInfo(List<String> id) throws Exception {
        try {
            return userRepository.BulkDeleteInfo(id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<Map<String, Object>> SearchUserName(String keywords, Integer offset) throws Exception {
        try {
            List<Map<String, Object>> list =  userRepository.SearchUserName(keywords,offset);
            return list;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public User findByIdAndRefreshtoken(String id,String rxauth) throws Exception {
        try {
            return userRepository.findByIdAndRefreshtoken(id,rxauth);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    // @Transactional
    // public User UpdateUserSite(User req) throws Exception {
    //     try {
    //         User user = userRepository.findOneById(req.getId());
    //         if (user == null) {
    //             return null;
    //         } else {
    //             user.setSiteId(req.getSiteId());
    //             userRepository.save(user);
    //             return user;
    //         }
    //     } catch (Exception e) {
    //         throw new Exception(e);
    //     }
    // }

    @Transactional
    public int UpdateUserSite(String site_id,List<String> id) throws Exception {
        try {
            int user = userRepository.UpdateUserSite(site_id,id);
            return user;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


}
