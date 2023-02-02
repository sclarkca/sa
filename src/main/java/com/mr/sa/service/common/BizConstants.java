package com.mr.sa.service.common;

public interface BizConstants {

	/**
	 * 联盟成员类型
	 */
	public interface USER_TYPE {

		public static final String MEMBER = "1";
		public static final String COP = "2";
		public static final String AP = "3";
	}

	/**
	 * 巡防计划
	 */
	public interface PATROL_PLAN {
		/**
		 * 状态
		 */
		public interface WORK_STATUS {
			/**
			 * 未开始
			 */
			public static final String READY = "1";
			/**
			 * 进行中
			 */
			public static final String ING = "2";
			/**
			 * 已结束
			 */
			public static final String DONE = "3";
			/**
			 * 执行失败
			 */
			public static final String FAIL = "4";
		}

	}

	/**
	 * 巡防任务
	 */
	public interface PATROL_TASK {
		/**
		 * 状态
		 */
		public interface ACTIVE_STATUS {
			/**
			 * 未开始
			 */
			public static final String INACTIVE = "1";
			/**
			 * 进行中
			 */
			public static final String ING = "2";
			/**
			 * 已结束
			 */
			public static final String END = "3";
		}

	}

	/**
	 * 事件状态
	 */
	public interface EVENT {

		public interface STATUS {

			/**
			 * 已上报
			 */
			public static final String UNDO = "0";
			/**
			 * 待派发
			 */
			public static final String READY = "1";
			/**
			 * 未处理
			 */
			public static final String ING = "2";
			/**
			 * 已处理
			 */
			public static final String DONE = "3";
		}

	}

	String GT_CID_USERID = "GT_CID_USERID";

	public static final String EVENT_TYPE_BRIGHT = "1";

	public static final String EVENT_TYPE_DARK = "2";

	String REDIS_ONLINE_DURATION_DAILY = "online_duration";

	String IP_WHITE_LIST = "IP_WHITE_LIST";
}
