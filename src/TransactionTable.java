import java.util.HashMap;


public class TransactionTable {
	// If boolean == true then signal == 1
	// If boolean == false then signal == 0
	public static HashMap<String, String> tableTrans;
	public static HashMap<String, String> tableOutputs;
	private static String high = " == 1";
	private static String low = " == 0";
	private static String stable = "$stable";
	private static String cluster3 = "cluster3";
	
	public static void init() {
		tableTrans = new HashMap<String,String>();
		tableTrans.put("asserted", high);
		tableTrans.put("is LOW", low);
		tableTrans.put("is HIGH", high);

		tableTrans.put("are also LOW", low);
		tableTrans.put("stable", stable);
		
		// cluster 5
		tableTrans.put("is not permitted", "cluster5");
		// cluster 3
		tableTrans.put("remains asserted until", "cluster3");
		tableTrans.put("be greater than", "cluster1");
		
		// cluster 11) CSYSREQ is only permitted to change from HIGH to LOW when CSYSACK is HIGH. [4]
		tableTrans.put("change from HIGH to LOW","cluster11");
	
	}
	
	public static String searchTrans(String str) {
		String s = tableTrans.get(str);
		return s;
	}
	
	
	
}
