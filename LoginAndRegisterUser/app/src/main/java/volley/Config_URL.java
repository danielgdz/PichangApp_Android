package volley;

public class Config_URL
{
	private static String acl_URL = "http://52.15.217.203:5100/webservice/user/";
	private static String base_URL = "http://52.15.217.203:5200/";
	// User login url
	public static String URL_LOGIN = acl_URL+"login";
	// User register url
	public static String URL_REGISTER = acl_URL+"register";
	// SUBSIDIARIES url
	public static String URL_SUBSIDIARIES= base_URL+"subsidiaries?flag_active=0";
}
