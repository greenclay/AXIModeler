import java.util.HashMap;


public class TransactionTable {
	// If boolean == true then signal == 1
	// If boolean == false then signal == 0
	public static HashMap<String, String> tableTrans;
	public static HashMap<String, String> tableOutputs;
	private static String high = " == 1";
	private static String low = " == 0";
	private static String stable = "$stable";

	public static void init() {
		tableTrans = new HashMap<String,String>();
		tableTrans.put("asserted", high);
		tableTrans.put("is LOW", low);
		tableTrans.put("is HIGH", high);

		tableTrans.put("are also LOW", low);
		tableTrans.put("stable", stable);
	}
	public static String searchTrans(String str) {
		String s = tableTrans.get(str);
		return s;
	}
	
}
