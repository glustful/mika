package com.miicaa.base.pay.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import android.app.Activity;
import android.content.Context;

import com.alipay.sdk.app.PayTask;

public class Alipay {

	private String PARTNER = "";
	private String SELLER = "";
	private String RSA_PRIVATE = "";
	private String RSA_PUBLIC = "";
	private String ORDER_ID = "";
	private String NOTIFY_URL = "";
	
	private Context mContext;
	private String subject;
	String body;
	String price;

	

	public Alipay(Context context){
		this.mContext = context;
	}
	public void setNeedParams(String partner,String rsa_private,String rsa_public,String notify_url,String orderid,String seller){
		this.PARTNER = partner;
		if(rsa_private!= null)
		this.RSA_PRIVATE = rsa_private.replaceAll("\\/", "/");
		if(rsa_public!=null)
		this.RSA_PUBLIC = rsa_public.replaceAll("\\/", "/");
		if(notify_url!=null)
		this.NOTIFY_URL = notify_url.replaceAll("\\/", "/");
		this.ORDER_ID = orderid;
		this.SELLER = seller;
	
	}
	
	public void setOrderInfo(String subject, String body, String price){
		this.subject = subject;
		this.body = body;
		this.price = price;
	}
	
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(final AlipayPaymentListener l) {
		String orderInfo = getOrderInfo();
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity) mContext);
				// 调用支付接口
				String result = alipay.pay(payInfo);
				if(l != null){
					l.onPay(new Result(result));
				}    
				
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(final ChecktAccountIfExistListener l) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask payTask = new PayTask((Activity) mContext);
				boolean isExist = payTask.checkAccountIfExist();

				if(l != null){
					l.onCheck(isExist);
				}
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * @return 
	 * 
	 */
	public String getSDKVersion() {
		PayTask payTask = new PayTask((Activity) mContext);
		String version = payTask.getVersion();
		return version;
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo() {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + NOTIFY_URL
				+ "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"15d\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 */
	public String getOutTradeNo() {
		

		return ORDER_ID;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	public interface AlipayPaymentListener{
		public void onPay(Result result);
	}
	
	public interface ChecktAccountIfExistListener{
		public void onCheck(boolean result);
	}
}
