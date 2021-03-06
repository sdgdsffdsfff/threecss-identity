package server;

import java.util.TimeZone;

import config.CommonConfigUCenter;
import http.HOpCodeUCenter;
import init.IExpand;
import init.Init;
import service.TokenService;
import service.UserGroupService;
import service.UserService;

public class Expand implements IExpand {

	@Override
	public void init() throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		HOpCodeUCenter.init();
		CommonConfigUCenter.init();
		Init.registerService(UserService.class);
		Init.registerService(UserGroupService.class);
		Init.registerService(TokenService.class);

	}

	@Override
	public void threadInit() throws Exception {

	}

}
