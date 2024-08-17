package github.runoob09;

import github.runoob09.entity.User;
import github.runoob09.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UserCenterProjectApplicationTests {
    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("java");
        List<User> userList = userService.searchUsersByTags(tags);
        System.out.println(userList);
    }

}
