package com.miicaa.home.ui.discover;

import com.miicaa.home.ui.discover.crm.CRMStateImp;
import com.miicaa.home.ui.discover.dailywork.DailyWorkStateImp;

public class DiscoverStateFactory {

	public final static int DID_NOVER = 0x10;
	public final static int DID_OVER = 0x11;
	public final static int OBSERVE_NOVER = 0x12;
	public final static int OBSERVE_OVER = 0x13;
	public final static int ALLCOM_ARRANGE = 0x14;
	public final static int ALLCOM_APPROVE = 0x15;
	public final static int DAILY_WORK = 0x16;
	public final static int CRM = 0x17;
	public static DiscoverState build(int discoverType) {
		DiscoverState mState = null;
		switch (discoverType) {
		case DID_NOVER:
			break;
		case DID_OVER:
			break;
		case OBSERVE_NOVER:
			break;
		case OBSERVE_OVER:
			break;
		case ALLCOM_APPROVE:
			break;
		case ALLCOM_ARRANGE:
			break;
		case DAILY_WORK:
			mState = new DailyWorkStateImp();
			break;
		case CRM:
			mState = new CRMStateImp();
			break;
		default:
			break;
		}
		return mState;
	}

}
