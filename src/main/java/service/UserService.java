package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    private AtomicLong maxId = new AtomicLong(0);
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());

    private static UserService instance;

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public List<User> getAllUsers() {
        List<User> listAllUsers = new ArrayList<User>(dataBase.values());
        return listAllUsers;
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public boolean addUser(User user) {
         if(!isExistsThisUser(user)){
             dataBase.put(maxId.longValue(), user);
             user.setId(maxId.longValue());
             maxId.incrementAndGet();
             return true;
         }
         else {
             return false;
         }
    }

    public void deleteAllUser() {
        logoutAllUsers();
        dataBase.clear();
        maxId = new AtomicLong(0);
    }

    public boolean isExistsThisUser(User user) {
        for (Map.Entry<Long, User> entry : dataBase.entrySet()) {
            User value = entry.getValue();
            if ((value.getEmail().equals(user.getEmail())) && (value.getPassword().equals(user.getPassword()))) {
                return true;
            }
        }
        return false;
    }

    public boolean isExistsEmailUser (String email) {
        if (dataBase.isEmpty()) return false;
        for (Map.Entry<Long, User> entry : dataBase.entrySet()) {
            User value = entry.getValue();
            if (value.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public List<User> getAllAuth() {
        List<User> listAllAuth = new ArrayList<User>(authMap.values());
        return  listAllAuth;
    }

    public boolean authUser(User user) {
        long id = 0;
        for (Map.Entry<Long, User> entry : dataBase.entrySet()) {
            long key = entry.getKey();
            User value = entry.getValue();
            if ((value.getEmail().equals(user.getEmail()))&&(value.getPassword().equals(user.getPassword()))) {
                if (isLoginThisUser(user)) {
                    return false;
                } else {
                    id = key;
                    authMap.put(id, user);
                    user.setId(id);
                    return true;
                }
            }
        }
        return false;
    }

    public void logoutAllUsers() { authMap.clear(); }

    public boolean isUserAuthById(Long id) {
        return authMap.containsKey(id);
    }

    public boolean isLoginThisUser(User user) {
        for (Map.Entry<Long, User> entry : authMap.entrySet()) {
            User value = entry.getValue();
            if (value.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

}
