package common;

public class UUID {
	
	public static String getID() {
		return java.util.UUID.randomUUID().toString();
	}
}
