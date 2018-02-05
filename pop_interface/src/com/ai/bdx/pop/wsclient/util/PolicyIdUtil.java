package com.ai.bdx.pop.wsclient.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;


/**
 * 策略Id生成
 * @author luozn
 *
 */
public class PolicyIdUtil {
	private static Logger log = Logger.getLogger(PolicyIdUtil.class.getName());
	//private static Logger log = LogManager.getLogger();

	public static String generatePolicyId(String policyIndex, String netWorkType, String provinceType,
			String businessType, String businessPolicyType) {
		StringBuffer policyCode = new StringBuffer();
		//拼接策略索引   1
		policyCode.append(policyIndex);
		if ("9".equals(policyIndex)) {
			return "9" + policyCode + createRandomString(31);
		} else {
			//拼接网络类型 2
			policyCode.append(netWorkType);
			//拼接省份3-5
			String cityCode = "000";
			if (netWorkType.equals("2")) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("beijing", "010");
				map.put("tianjin", "020");
				map.put("shanghai", "021");
				map.put("chongqing", "023");
				map.put("hebei", "0311");
				map.put("henan", "0371");
				map.put("hubei", "027");
				map.put("hunan", "0731");
				map.put("jiangsu", "025");
				map.put("jiangxi", "0791");
				map.put("liaoning", "024");
				map.put("jilin", "0431");
				map.put("beijing", "010");
				map.put("helongjiang", "0451");
				map.put("shaanxi", "029");
				map.put("shandong", "0531");
				map.put("sichuan", "028");
				map.put("qinghai", "0971");
				map.put("anhui", "0551");
				map.put("hainan", "0898");
				map.put("guangdong", "020");
				map.put("guizhou", "0851");
				map.put("zhejiang", "0571");
				map.put("fujian", "0591");
				map.put("gansu", "0931");
				map.put("yunnan", "0871");
				map.put("xizhang", "0891");
				map.put("ningxia", "0951");
				map.put("guangxi", "0771");
				map.put("xinjiang", "0991");
				map.put("neimenggu", "0471");
				//String province = Configure.getInstance().getProperty("PROVINCE");
				String province = "";   //省份代码
				cityCode = map.get(province);
				if(cityCode.length()>3){
					cityCode=cityCode.substring(1, 3);
				}else{
					cityCode=cityCode.substring(1, 2)+"0";
				}
			}
			policyCode.append(cityCode);
			//拼接业务大类6-7
			businessType = "000";
			policyCode.append(businessType);
			//拼接业务策略类型8-9
			policyCode.append(businessPolicyType);
			policyCode.append(createRandomString(23));
			return policyCode.toString();
		}

	}

	//生成指定长度的随机字符串
	public static synchronized String createRandomString(int length) {
		char ch[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
				'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1' };//最后又重复两个0和1，因为需要凑足数组长度为64

		Random random = new Random();
		if (length > 0) {
			int index = 0;
			char[] temp = new char[length];
			int num = random.nextInt();
			for (int i = 0; i < length % 5; i++) {
				temp[index++] = ch[num & 63];//取后面六位，记得对应的二进制是以补码形式存在的。
				num >>= 6;//63的二进制为:111111
				// 为什么要右移6位？因为数组里面一共有64个有效字符。为什么要除5取余？因为一个int型要用4个字节表示，也就是32位。
			}
			for (int i = 0; i < length / 5; i++) {
				num = random.nextInt();
				for (int j = 0; j < 5; j++) {
					temp[index++] = ch[num & 63];
					num >>= 6;
				}
			}
			return new String(temp, 0, length);
		} else if (length == 0) {
			return "";
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 调用shell向爱立信设备执行脚本
	 * @param fileName
	 * @return
	 */
	public static void sendPcrfInformation(final String filePath) {
		Thread thread = new Thread() {
			public void run() {
				//生成文件调用本地shell
				//String loadSh = Configure.getInstance().getProperty("LOAD_SH");
				String loadSh = PropUtil
				.getProp("ericsson_sh_path", "pcrf.properties");   
				log.info("LOAD_SHELL----------------------------" + filePath);
				StringBuffer sbTableName = new StringBuffer();
				sbTableName.append(filePath);
				if (StringUtil.isNotEmpty(loadSh)) {
					//String filePath = Configure.getInstance().getProperty("SYS_COMMON_UPLOAD_PATH");
//					String filePath = PropUtil
//					.getProp("ericsson_upload_path", "pcrf.properties");       //上传路径
//					if (!filePath.endsWith(File.separator)) {
//						filePath = filePath + File.separator;
//					}
					String filePathName = filePath;
					filePathName += ".ldif";
					File path = new File(filePathName);
					if (path.exists()) {
						//先生成文件然后  关掉流才能入库，马上入库会读不到数据   导入数据库文件
						StringBuffer sbLoadsh = new StringBuffer();
						sbLoadsh.append(loadSh).append(" ").append(filePath);
						log.info("loadsh-------------------------------" + sbLoadsh.toString());
						Process process;
						try {
							process = Runtime.getRuntime().exec(sbLoadsh.toString());
							process.waitFor();
							log.info("文件 " + filePath + " 执行添加策略脚本完成");

						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						log.info("文件 " + filePath + " 没有找到--");
					}
				}
			};

		};
		thread.setName("向爱立信Pcrf发送脚本");
		thread.start();
	}

}
