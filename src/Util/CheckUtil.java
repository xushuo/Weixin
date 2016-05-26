package Util;

import java.util.Arrays;

public class CheckUtil {

	private static final String token="xushuo";
	
	public static boolean checkSignature(String signature,String timestamp,String nonce){
		String[] arr = new String[]{token,timestamp,nonce};
		//排序
		Arrays.sort(arr);
		//生成字符串
		StringBuffer content =new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		//sha1加密
		String temp = Sha1Util.passAlgorithmsCiphering(content+"",Sha1Util.SHA_1);
		System.out.println(temp);
		System.out.println(signature);
		return temp.equals(signature);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
