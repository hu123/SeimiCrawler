package cn.wanghaomiao.dao.mybatis;


import cn.wanghaomiao.model.BlogContent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.huluo.redwheat.entity.Passage;

public interface PassageDao {

    @Insert("insert into passage (title,dateAndTag,currentPage) values (#{passage.title},#{passage.dateAndTag},#{passage.currentPage})")
    @Options(useGeneratedKeys = true, keyProperty = "passage.id")
    int save(@Param("passage") Passage passage);
}
