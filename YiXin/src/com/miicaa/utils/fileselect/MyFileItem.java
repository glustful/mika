package com.miicaa.utils.fileselect;

import java.io.Serializable;
	public class MyFileItem implements Serializable{
		private static final long serialVersionUID = -3684812708652382088L;
		public String name;
		public String path;
		public String fid;
		//public Drawable resId;
		public Boolean isSelect = false;
	}
