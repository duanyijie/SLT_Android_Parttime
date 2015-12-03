package com.niit.parttime.config;

/**
 * 服务器端Servlet处理的网址
 * 
 * @author Admin
 *
 */
public class URLs {
	// 10.0.2.2代表本地网络，而不是127.0.0.1
	public static final String BASIC_URL = "http://10.0.2.2:8080/SLT_Andorid_Parttime";
	public static final String LOGIN = BASIC_URL
			+ "/users/users?action=validate";

	public static final String REGISTER = BASIC_URL + "/users/users?action=add";
	public static final String USER_RESETPWD = BASIC_URL
			+ "/users/users?action=forgotpwd";

	public static final String JOB_LIST = BASIC_URL
			+ "/Joblist/Joblist?action=list";

	public static final String JOB_VIEW = BASIC_URL
			+ "/Joblist/Joblist?action=view&jobID=%s";
	// %s 为占位符 有多少个%s说明就有多少个参数

	public static final String JOB_COLLECTION_List = BASIC_URL
			+ "/Jobcollect/Jobcollect?action=list&usersID=%s";

	public static final String JOB_COLLECTION_ADD = BASIC_URL
			+ "/Jobcollect/Jobcollect?action=add&usersID=%s&jobID=%s";
	public static final String USERS_MD_PWD = BASIC_URL
			+ "/users/users?action=mdpwd";
}
