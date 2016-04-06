package com.miicaa.base.share.industry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.miicaa.base.share.ShareMain;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.view.LabelEditView;

public class IndustryMain extends ShareMain {
	public static String industryClass="[{'id':'10100','name':'IT|通信|电子|互联网'},{'id':'10200','name':'金融业'},{'id':'10800','name':'房地产|建筑业'},{'id':'10900','name':'商业服务'},{'id':'10300','name':'贸易|批发|零售|租赁业'},{'id':'10400','name':'文体教育|工艺美术'},{'id':'10500','name':'生产|加工|制造'},{'id':'11500','name':'交通|运输|物流|仓储'},{'id':'10000','name':'服务业'},{'id':'11300','name':'文化|传媒|娱乐|体育'},{'id':'11600','name':'能源|矿产|环保'},{'id':'11100','name':'政府|非盈利机构'},{'id':'11400','name':'农|林|牧|渔|其他'}]";
	public static String secondJson = "{\"10800\":[{\"id\":\"140000\", \"name\":\"房地产/建筑/建材/工程\", \"parent\":\"10800\"}, {\"id\":\"140100\", \"name\":\"家居/室内设计/装饰装潢\", \"parent\":\"10800\"}, {\"id\":\"140200\", \"name\":\"物业管理/商业中心\", \"parent\":\"10800\"}],\"10900\":[{\"id\":\"200300\", \"name\":\"专业服务/咨询(财会/法律/人力资源等)\", \"parent\":\"10900\"}, {\"id\":\"200302\", \"name\":\"广告/会展/公关\", \"parent\":\"10900\"}, {\"id\":\"201400\", \"name\":\"中介服务\", \"parent\":\"10900\"}, {\"id\":\"201300\", \"name\":\"检验/检测/认证\", \"parent\":\"10900\"}, {\"id\":\"300300\", \"name\":\"外包服务\", \"parent\":\"10900\"}],\"10000\":[{\"id\":\"121400\", \"name\":\"医疗/护理/美容/保健/卫生服务\", \"parent\":\"10000\"}, {\"id\":\"200600\", \"name\":\"酒店/餐饮\", \"parent\":\"10000\"}, {\"id\":\"200800\", \"name\":\"旅游/度假\", \"parent\":\"10000\"}],\"11100\":[{\"id\":\"200100\", \"name\":\"政府/公共事业/非盈利机构\", \"parent\":\"11100\"}, {\"id\":\"120600\", \"name\":\"学术/科研\", \"parent\":\"11100\"}],\"10200\":[{\"id\":\"180000\", \"name\":\"基金/证券/期货/投资\", \"parent\":\"10200\"}, {\"id\":\"180100\", \"name\":\"保险\", \"parent\":\"10200\"}, {\"id\":\"300500\", \"name\":\"银行\", \"parent\":\"10200\"}, {\"id\":\"300900\", \"name\":\"信托/担保/拍卖/典当\", \"parent\":\"10200\"}],\"11300\":[{\"id\":\"210300\", \"name\":\"媒体/出版/影视/文化传播\", \"parent\":\"11300\"}, {\"id\":\"200700\", \"name\":\"娱乐/体育/休闲\", \"parent\":\"11300\"}],\"10100\":[{\"id\":\"210500\", \"name\":\"互联网/电子商务\", \"parent\":\"10100\"}, {\"id\":\"160400\", \"name\":\"计算机软件\", \"parent\":\"10100\"}, {\"id\":\"160000\", \"name\":\"IT服务(系统/数据/维护)\", \"parent\":\"10100\"}, {\"id\":\"160500\", \"name\":\"电子技术/半导体/集成电路\", \"parent\":\"10100\"}, {\"id\":\"160200\", \"name\":\"计算机硬件\", \"parent\":\"10100\"}, {\"id\":\"300100\", \"name\":\"通信/电信/网络设备\", \"parent\":\"10100\"}, {\"id\":\"160100\", \"name\":\"通信/电信运营、增值服务\", \"parent\":\"10100\"}, {\"id\":\"160600\", \"name\":\"网络游戏\", \"parent\":\"10100\"}],\"10400\":[{\"id\":\"201100\", \"name\":\"教育/培训/院校\", \"parent\":\"10400\"}, {\"id\":\"120800\", \"name\":\"礼品/玩具/工艺美术/收藏品/奢侈品\", \"parent\":\"10400\"}],\"11500\":[{\"id\":\"150000\", \"name\":\"交通/运输\", \"parent\":\"11500\"}, {\"id\":\"301100\", \"name\":\"物流/仓储\", \"parent\":\"11500\"}],\"10300\":[{\"id\":\"120400\", \"name\":\"快速消费品（食品/饮料/烟酒/日化）\", \"parent\":\"10300\"}, {\"id\":\"120200\", \"name\":\"耐用消费品（服饰/纺织/皮革/家具/家电）\", \"parent\":\"10300\"}, {\"id\":\"170500\", \"name\":\"贸易/进出口\", \"parent\":\"10300\"}, {\"id\":\"170000\", \"name\":\"零售/批发\", \"parent\":\"10300\"}, {\"id\":\"300700\", \"name\":\"租赁服务\", \"parent\":\"10300\"}],\"11400\":[{\"id\":\"100000\", \"name\":\"农/林/牧/渔\", \"parent\":\"11400\"}, {\"id\":\"100100\", \"name\":\"跨领域经营\", \"parent\":\"11400\"}, {\"id\":\"990000\", \"name\":\"其他\", \"parent\":\"11400\"}],\"10500\":[{\"id\":\"121000\", \"name\":\"汽车/摩托车\", \"parent\":\"10500\"}, {\"id\":\"129900\", \"name\":\"大型设备/机电设备/重工业\", \"parent\":\"10500\"}, {\"id\":\"121100\", \"name\":\"加工制造（原料加工/模具）\", \"parent\":\"10500\"}, {\"id\":\"121200\", \"name\":\"仪器仪表及工业自动化\", \"parent\":\"10500\"}, {\"id\":\"210600\", \"name\":\"印刷/包装/造纸\", \"parent\":\"10500\"}, {\"id\":\"120700\", \"name\":\"办公用品及设备\", \"parent\":\"10500\"}, {\"id\":\"121300\", \"name\":\"医药/生物工程\", \"parent\":\"10500\"}, {\"id\":\"121500\", \"name\":\"医疗设备/器械\", \"parent\":\"10500\"}, {\"id\":\"300000\", \"name\":\"航空/航天研究与制造\", \"parent\":\"10500\"}],\"11600\":[{\"id\":\"130000\", \"name\":\"能源/矿产/采掘/冶炼\", \"parent\":\"11600\"}, {\"id\":\"120500\", \"name\":\"石油/石化/化工\", \"parent\":\"11600\"}, {\"id\":\"130100\", \"name\":\"电气/电力/水利\", \"parent\":\"11600\"}, {\"id\":\"201200\", \"name\":\"环保\", \"parent\":\"11600\"}]}";
	public static final int REQUEST_CODE = 0X4;
	private JSONObject json;
	public IndustryMain(Context mContext) {
		super(mContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start(){
		IndustryActivity_.intent(mContext)
		.startForResult(REQUEST_CODE);
	}
	
	@Override
	public boolean invalide(){
		if(json==null){
			PayUtils.showToast(mContext, "客户所属行业不能为空", 3000);
			return false;
		}
		return true;
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			json = new JSONObject(data.getStringExtra("data"));
			((LabelEditView)this.mRootView).setContent(json.optString("name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
