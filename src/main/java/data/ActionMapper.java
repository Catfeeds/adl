package data;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface ActionMapper {
	
	@Insert("insert into action(id, name, time) values(#{id}, #{name}, #{time})")
	@Options(useGeneratedKeys = true)
	void addActionBean(ActionBean actionBean);
}
