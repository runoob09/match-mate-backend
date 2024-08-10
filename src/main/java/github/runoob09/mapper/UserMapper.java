package github.runoob09.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import github.runoob09.entity.User;
import org.apache.ibatis.annotations.Select;

/**
 * @author ZJH
 * @description 针对表【user】的数据库操作Mapper
 * @createDate 2024-07-24 13:16:48
 * @Entity github.runoob09.entity.User
 */

public interface UserMapper extends BaseMapper<User> {
}