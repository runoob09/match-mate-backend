package github.runoob09.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import github.runoob09.entity.Tags;
import github.runoob09.service.TagsService;
import github.runoob09.mapper.TagsMapper;
import org.springframework.stereotype.Service;

/**
* @author ZJH
* @description 针对表【tags(标签表)】的数据库操作Service实现
* @createDate 2024-08-11 22:59:44
*/
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags>
    implements TagsService{

}




