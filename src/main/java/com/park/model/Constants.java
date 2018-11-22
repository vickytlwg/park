package com.park.model;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.springframework.asm.commons.StaticInitMerger;

import com.park.service.ParkService;
import com.park.service.PosChargeDataService;
import com.park.service.UdpConnectorInfoService;
import com.park.service.impl.ParkServiceImpl;
import com.park.service.impl.UdpConnectorsInfoServiceImpl;

public class  Constants{
	
	public static  String PINGAPIKEY = "sk_live_Wr104O5Gu5m508arbTmX9aL8";
	public static  String PINGAPPID = "app_bX1SKKWbP4K0Ge1i";
	public static  String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	public static  int ACCESSTABLESCOUNT = 500;
	public static  String URL = "http://www.iotclouddashboard.com/parkpictures/";
	public static  String UPLOADDIR = "/root/apache-tomcat-8.5.20/webapps/parkpictures/";
	public static  String WEBAPIURL="http://www.iotclouddashboard.com/park";
	public static  Set<Udpconnectors>  udpconnectors=new HashSet<>();
	public static  DatagramSocket socket=null;
	public static StringBuilder  dataReceived=new StringBuilder();
	public static  Map<String, Object> iotData=new HashMap<>();
	public static Map<String, Object> tcpReceiveDatas=new HashMap<>();
	public static UdpConnectorInfoService udpConnectorInfoService=new UdpConnectorsInfoServiceImpl();
	public static PosChargeDataService poschargeSerivce=null;
	public static ParkService parkService=new ParkServiceImpl();
	//给工商发送的队列
	public static int[] parkToQuene={224,260,267,268};
	//发送给自己
	public static int[] ToQuenePark={4,334,258,355,182,248,352};
	//美凯龙
	public static String alipayPrivateKey0="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN65J5HkbeaMA+6G1WC/9FnOpvXJeI8JDJINXmEra5bu4PLcB1g1hRi188nDdCahIsKJapW+N4b456bRFyAATXyNiYw2YzmFPuXiBOpXAqkylArqRSRAumPcGTht0ubGuvco1bL09BCX/845x5UufSnqhm0p6zAtl9ZXyqchnyNlAgMBAAECgYA0nHYtb1FKqX5wROd4oD9fS4KDAuF4oEGY6pGF0JTspWb711/gxVu4V5rL6WCxk1S7sWw9DR7ewkMuU6vrwkaLGalgSzGzL4vmagofLP6QBv4JEDHY3YqTFItajHIoz7A1fr6CO+Irp+MzExUitMCWGmK+FI3ZFxqgawdhNTk5QQJBAPQa3ZYyxxoTHCKSv5NOUG8rV+5LIS5Rff4p3+mVRbckC8oBtkf4qzWGINnTdHW59aXWGoYvy831+gcNlJLDHJECQQDpk48dTcJ72tfSQWoH42zttUMDNFn22lPMiA7RMH63odBZyM8LmLsf/B3zMLRC5xtqITWH1hZ0cJLRGPHfRNOVAkAdp+3nw3+A4XV3ldCEeu02tFNYdfQwBmKCMGAtpp7zowNzpUWZ9NIIDKWNES2C5/a6EQrj5oaiHRIlLTcxpVahAkEAlnWe8tlsNiw6ag4JJxuwx07D5ASNDx6EbHQYV1hNyCxL8goQlP/FSk01Xbo54/7LAgpyWRXnUQpTCK5w38mOIQJBALLK7YV3+Fd1MO9drIu/yasrsRX9wfJF63iDjkJgaixBXQVA342tnNv/HSPhYKHl7Oi66QI2iEIH/ThBDZ04l5s=";
	public static String alipayPublicKey0="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDeuSeR5G3mjAPuhtVgv/RZzqb1yXiPCQySDV5hK2uW7uDy3AdYNYUYtfPJw3QmoSLCiWqVvjeG+Oem0RcgAE18jYmMNmM5hT7l4gTqVwKpMpQK6kUkQLpj3Bk4bdLmxrr3KNWy9PQQl//OOceVLn0p6oZtKeswLZfWV8qnIZ8jZQIDAQAB";
	public static String alipayAppId0="2017101609330373";
	//美凯龙当面付
	public static String alipayPrivateKey4="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKXVb3I7ZPgKFt0xiDEdnSzDTVTiBgCBGG+MwMGMglTnAWf5M7d0A8JEa377FIR6UaKavKhGrHUJPPd70aewJJI1BkzR9s4SMA5SNQxq5NINXl7Lga2iCvx3UYmW223RvSQCM57+ySVp7PdogUhfks97XjT5jUr5YDvqMQa5LE4rAgMBAAECgYBSWUqeTyA0i84OySn4Odxy/gjIvfR9C542xo5xjnSp9IyeNQGZd+Ll9VPsGoJ1xsgDFxPX/EBnXc8IllPmEXIW16vkaH7NJVnsnU411lzAqguAZoDivVGj5Ai2WsHkgsSblcQV39ENKc01hGyi7wuHfnOI8mpwmrDB6NPz9TOiAQJBAPwmFjZ9I85SU13bHp3d5Koq0gPAmm7qEkkKkNVwgqTCv0FRVtKkLPpJ0P78Bn/V7+SxA+64axrgHk1RklYBNIkCQQCoXdpRMUBCDGi19xqSOto0CKzb4bnaXNCWI4Xxjqszs+y6Fw09NPp9hrYBiv0wLLY6VYjYliwZpOTGPBz34CgTAkEAzXy4g4VuRblfyVBGXj80F923s2kmMhe9+RqMN/Vt8pnnRXLlP9qd8QdxNCvtnAacbxI3czeFyaw15asBh+eJiQJBAKaYt32/jiXMwY/Mgq5PLbpvGxw3rv8y9/KYE+GBmkX+sy8vwBngYjvzIAVG0mDo3Se7mz00205Eq1q8AK4FT6ECQQDr6AK7kfl0mWRZp8xo+zO+jRZuK7YrD2FacJB5TuM37KyhkUlWrsac8A4fBmY1+CzygZjWcy57HBNnmuf5Wh74";
	public static String alipayPublicKey4="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	public static String alipayAppId4="2017110809798545";
	//九比特
	public static String alipayPrivateKey="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMellSPcYZV7dk0/"
			+ "xvIgHE8jnHfIpgTt6KL+rLsSGYmFQ8aOkRvTnZSP92+98Z0NWzDMKU5ohf3QVPzO"
			+ "X/QhmGRylFMUwEThZ8etlUuKDxk7hUjmkI1Okb4Qunczr8Q9jpfX8FFRGV5j52Od"
			+ "p6ncY2pkEWuUBrwqH9VE16I8C/9tAgMBAAECgYEAhiOBtit/QVFHphWA1POwMZgK"
			+ "rAybR1qV4NXeNn6tu9FXPVRSuPCQwt2L8X8clFoB+CJkanMd++/6+jSrEbt0yG/7"
			+ "W5mOPVJMGS1vPFWjmofwZqfPEgH4zqXmcTV8DV7QkYa8pBECuV5n+sA6eKeiw3dk"
			+ "NOj/hxpaEvHmE6q02p0CQQDzRDocuh/UyL5k4sMSD2OadQ8Fq+yIK+H5MSNZMo+U"
			+ "/YLy6ZGVIr5ZO3yql1Bq2oK+b8f1nZL7vSxp4HizsNIPAkEA0hjZ6aK/HMl67234"
			+ "1smrRYZw3UjHKdUvW/P1TcCcqyMEp6Sf2m9RYUKxvviB8SPRwc6c9HhhgBj9rdyZ"
			+ "9ioiwwJAMXyrpbRnTU4ZDUTkEgR3arBtgeXblEf5DExmuHqEovZ/cRL6vq/2sQhc"
			+ "8AcgINyaxErRDrIjeHqfUlqLs2JBGQJBAKhPTFOFE4FmT1v8R7saOGEsQMKliRgU"
			+ "Nyp9F+lAAsJ+/T2n/n+pahJ2sZqBzud1gJa4hLi8r69FVgSwk47HVq0CQQDa8iXR"
			+ "319hbSivw4z4BWQ2hjWALaUAocVP4+4tV1zAINJkopqxpMJ7HivXkO/DczDEdbnpEeFNfMCTp5GoFgdO";
	public static String alipayPublicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	public static String alipayAppId="2015101400439228";
	//包泊
	public static String alipayPrivateKey1="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN65J5HkbeaMA+6G1WC/9FnOpvXJeI8JDJINXmEra5bu4PLcB1g1hRi188nDdCahIsKJapW+N4b456bRFyAATXyNiYw2YzmFPuXiBOpXAqkylArqRSRAumPcGTht0ubGuvco1bL09BCX/845x5UufSnqhm0p6zAtl9ZXyqchnyNlAgMBAAECgYA0nHYtb1FKqX5wROd4oD9fS4KDAuF4oEGY6pGF0JTspWb711/gxVu4V5rL6WCxk1S7sWw9DR7ewkMuU6vrwkaLGalgSzGzL4vmagofLP6QBv4JEDHY3YqTFItajHIoz7A1fr6CO+Irp+MzExUitMCWGmK+FI3ZFxqgawdhNTk5QQJBAPQa3ZYyxxoTHCKSv5NOUG8rV+5LIS5Rff4p3+mVRbckC8oBtkf4qzWGINnTdHW59aXWGoYvy831+gcNlJLDHJECQQDpk48dTcJ72tfSQWoH42zttUMDNFn22lPMiA7RMH63odBZyM8LmLsf/B3zMLRC5xtqITWH1hZ0cJLRGPHfRNOVAkAdp+3nw3+A4XV3ldCEeu02tFNYdfQwBmKCMGAtpp7zowNzpUWZ9NIIDKWNES2C5/a6EQrj5oaiHRIlLTcxpVahAkEAlnWe8tlsNiw6ag4JJxuwx07D5ASNDx6EbHQYV1hNyCxL8goQlP/FSk01Xbo54/7LAgpyWRXnUQpTCK5w38mOIQJBALLK7YV3+Fd1MO9drIu/yasrsRX9wfJF63iDjkJgaixBXQVA342tnNv/HSPhYKHl7Oi66QI2iEIH/ThBDZ04l5s=";
	public static String alipayPublicKey1="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	public static String alipayAppId1="2017080908114505";
	//public static String alipayAppId1="2017103109631099";
	//包泊返佣
	public static String alipayAppId5="2017103109631099";
	public static String alipayPublicKey5="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	public static String alipayPrivateKey5="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMngvA+ysADG9bQfGB2bTjrF40mxgZRLTMQtf454rLerWNFMhYiKWQ52DCpzssSvKu/q/3BJLXzYJs+3vAG4XJU+iHYt/TZ5L05z0+ECzKg796ONgRQjY9ROFQfYoBzWsoq4DJSqX7+c9X8y7zxF+MmrbIc+WhYJbrOZynmyU9arAgMBAAECgYEAor0e/Uyfo1DAO16hkyRicPCbZAnLGp+FF9RRsqlHGpzvcXHNZ5+XYRf6gLJaF0HaTCPVnU/wcmbRCrvjvENBpcNNFf2yeQ16YfFbLiH/5VpONE3Emsz2TpTXjg4DkZPK2gmxV7hmkVJbgLJLH9H5Kr7O3k25J2naSLof6OCwjhECQQDrh2+dpt9pm25qp2hTyUCh8ipmSiYdvV7290hBfwL43/CRKWi967+RmyTIFjUfA80K+dtC4tBSi9Wj6jUL0Vz5AkEA22yNxxEHfadn52xZYAY+Fov5vzVMg58usyiRuAECpjeEraDVMByEsbCom4wcz8xrpTQQyv9ivCKvp05GHGRtwwJAO3PpkdMQrBjHC3cMl7HCdd6qIz3uVy15LkAHZjkIaAgnMo1QWjiLF9cgtr0cVaLg/DdLyJt2WI/hssmo8D2+CQJAPAxOFG89nI6GqdeF8fhjX2o5E7GzWcXjUtZEYpWsMu64mYeNMBht2+7bhY3niJqJzvEbs2IGlDPt3BxYpYZ6XwJBAJFV4LXRq1+XKoEhE3KV+3puTGR7SDPBoCb1o+Q27dVZhrp4kxmCWEwhlhaK5oWRWZQ29ZfCWUaEPSivYri8zRA=";
	//家乐福
	public static String alipayPrivateKey6="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALmMnqgSPypInLeDKGf/wpI5/KHtjU7y52SCsc4jDrBTI9ttDic8sVSqbbZdroVegajZukzywATXHN0dxqvYC3Z/Hv5ZS60bd8Ku3TjvUC3wpeZHqYQZyyW+GTX0pZyIHdTBLXxctDT6chVVadhHav3TeNiiPxWI+qyh0nITb7wTAgMBAAECgYEAtihR/ehj8Iv2psZxXaAeEVYyKXpAFviqp+cSza9nFT2Z3yoyEd0orljeeoF/+tjr66tMlYxfbKEOahl4WG4x2QOz6Kka2ZBLzBI46f8PleH96FGM+xqz5L/DURF9z2bxsKHBGRaKTDNn+YfXoUFNjjeJAwhcNjiaUntqI1D34LkCQQD8O2i77zg1/ttU7MtlFk5SfwBakiiRjdVgElsST0vAKWO+imhwirb5A8pXpaQgJ77Ub6aCTjB3r8k/cuZsoA+fAkEAvFIzeHJXjybtr4mu6Of+1IROXv6lVJ71e2kuz/WBOLKCJVB97SzX7zbxP1OWfR/oPUoMzQ0TQGy5gtjDttdvDQJAMDCFhQpiOea0LtiCz5BgbGB5R9SOzahEcPNw1OGzfkYqGVhGNeI6rP6tZOtvnPka+2lNba3UkrlSAL5KCczWzwJBAJmAt4u6SDzbkmq/Y99dG47Gucsc7k7ns1mBBzNj/ozklMktrzegkfLfag4gaE5dr8QllAGgYszk0uIhJobnXBECQCN8wKw3lTGyJpP4du8XtwuYI1DcNoS6jG1+ejRrYAKXHaux6AjWYkyb++wJ63Vcuqxh4mxqufb3+mvgdBGxqQ8=";
	public static String alipayPublicKey6="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	public static String alipayAppId6="2017102509514540";
}
