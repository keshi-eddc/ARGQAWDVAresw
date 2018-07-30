package com.edmi;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.edmi.service.service.TrackicoService;
import com.edmi.utils.http.exception.MethodNotSupportException;

/** 
* @ClassName: TrackicoTest 
* @Description: Junit测试 
* @author keshi
* @date 2018年7月30日 下午3:52:31 
*  
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TrackicoTest {
	Logger log = Logger.getLogger(TrackicoTest.class);
	// 注入实例 相当于自动new对象
	@Autowired
	private TrackicoService trackicoService;

	@Test
	public void getICO_trackico_list() throws MethodNotSupportException {
		trackicoService.getICO_trackico_list();
	}
}
