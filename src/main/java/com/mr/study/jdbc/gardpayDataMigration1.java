package com.mr.study.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class gardpayDataMigration1 {
	private static final String driverClassName = "oracle.jdbc.driver.OracleDriver";
	private static final String javaurl = "jdbc:oracle:thin:@172.17.1.27:1521:fingard";
	private static final String username ="AS330106";
	private static final String password ="WVzFXppaiAuQFYbU";
	/*private static final String javaurl = "jdbc:oracle:thin:@10.60.45.171:1521:fingard";
	private static final String username ="ZH_AS330106";
	private static final String password ="ZH_AS330106";*/
	/** 获取数据库连接*/
	private static Connection getConn() {
	    Connection conn = null;
	    try {
	    	Class.forName(driverClassName);
		    // 建立连接
		    conn = DriverManager.getConnection(javaurl, username, password);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	
	/** 同步批次表*/
	public static int SE_BATCH (){
	    Statement s ;
	    int rs = 0 ;
	    try {
	    	//获取连接
	    	Connection conn = getConn();
	    	// statement对象用于与数据库进行交互
		    s = conn.createStatement();
		    StringBuilder stringBuilder = new StringBuilder();
		    stringBuilder.append("insert into SE_BATCH(ENT_NUM,BAT_NO,REQ_DATE,IN_TIME,OUT_TIME,SEND_BANK_TIME,BANK_RETURN_TIME,FG_RETURN_TIME,DEAL_STATE,ENT_CHANNEL_CODE,TOTAL_AMOUNT,TOTAL_NUM");
		    stringBuilder.append(",VERIFY_CODE,BANK_VERIFY_CODE,SUCCESS_AMOUNT,SUCCESS_NUM,FAIL_NUM,BANK_LOCATION_MATCH_STATE,RECONCILIATION_CODE,DETAIL_MD5");
		    stringBuilder.append(",AVG_AMOUNT,MIN_AMOUNT,MAX_AMOUNT,SEND_TO_BANK_FILE_PATH,ACCOUNTING_FLAG,ENCRYPT_FLAG,SOURCE_DATA_MD5,SOURCE_TOTAL_AMOUNT,MONEY_WAY,ENT_ACC_NUM");
		    stringBuilder.append(",REPEAT_CONDITION,URID,FAIL_AMOUNT,FILE_NAME,REQUEST_TYPE,INSIDE_STATE,INSIDE_STATE_INFO,OUTSIDE_STATE,ALL_FAIL_CODE,ALL_FAIL_INFO,BATCH_TO_SINGLE_FLAG");
		    stringBuilder.append(",ROW_VERSION,MD5,INTERFACE_TYPE,VERSION,LAST_MODIFY_USER,LAST_MODIFY_TIME)");
		    stringBuilder.append("select ENTERPRISENUM,BATNO,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') REQDATE,INTIME,OUTTIME,SENDBANKTIME,BANKRETURNTIME,SENDENTITYTIME,case DEALSTATE when 81 then 'Z01' when 52 then 'S04' else 'ZZZ' end DEALSTATE, BANKCODE,TOTALAMOUNT,TOTALNUM,");
		    stringBuilder.append("VERIFYCODE,BANKVERIFYCODE,SUCCESSAMOUNT,SUCCESSNUM,FAILNUM,BANKLOCATIONCODE,RECONCILIATIONCODE,DETAILMD5");
		    stringBuilder.append(",AVGAMOUNT,MINAMOUNT,MAXAMOUNT,SEND2BANKFILEPATH,ACCOUNTINGFLAG,ENCRYPTFLAG,SOURCEDATAMD5,SOURCETOTALAMOUNT,PAYTYPE,ENTERPRISEACCNUM,");
		    /*stringBuilder.append("REPEATCONDITION ,sys_guid(),null,null,'1','IN03',null,'OUT02',null,null,'0',1,null,'1',120,null,null from  SE_BATCH@gardpay_AS330106 t where 1=1 and t.batid < '20181107000002' and TOTALAMOUNT is not null and t.SENDBANKTIME is not  null  ");*/
		   /* stringBuilder.append("REPEATCONDITION ,sys_guid(),null,null,'1','IN03',null,'OUT02',null,null,'0',1,null,'1',120,null,null from  SE_BATCH@gardpay_AS330106 t where 1=1  and TOTALAMOUNT is not null and t.SENDBANKTIME is not  null  ");*/
		    stringBuilder.append("REPEATCONDITION ,sys_guid(),null,null,'1','IN03',null,'OUT02',null,null,'0',1,null,'1',120,null,null from  SE_BATCH@gardpay_AS330106 t where 1=1  and TOTALAMOUNT is not null and t.SENDBANKTIME is not  null and t.batid > '20181130000004' and batid like '2018%' ");
		    stringBuilder.append("union ");
		    stringBuilder.append("select ENTERPRISENUM,BATNO,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') REQDATE,INTIME,OUTTIME,SENDBANKTIME,BANKRETURNTIME,SENDENTITYTIME,case DEALSTATE when 81 then 'Z03' when 52 then 'S04' else 'ZZZ' end DEALSTATE, BANKCODE,TOTALAMOUNT,TOTALNUM,");
		    stringBuilder.append("VERIFYCODE,BANKVERIFYCODE,SUCCESSAMOUNT,SUCCESSNUM,FAILNUM,BANKLOCATIONCODE,RECONCILIATIONCODE,DETAILMD5");
		    stringBuilder.append(",AVGAMOUNT,MINAMOUNT,MAXAMOUNT,SEND2BANKFILEPATH,ACCOUNTINGFLAG,ENCRYPTFLAG,SOURCEDATAMD5,SOURCETOTALAMOUNT,PAYTYPE,ENTERPRISEACCNUM,");
		   /* stringBuilder.append("REPEATCONDITION ,sys_guid(),null,null,'1','IN03',null,'OUT02',null,null,'0',1,null,'1',120,null,null from  SE_BATCH@gardpay_AS330106 t where 1=1 and t.batid < '20181107000002' and TOTALAMOUNT is not null and t.SENDBANKTIME is  null  ");*/
		    /*stringBuilder.append("REPEATCONDITION ,sys_guid(),null,null,'1','IN03',null,'OUT02',null,null,'0',1,null,'1',120,null,null from  SE_BATCH@gardpay_AS330106 t where 1=1  and TOTALAMOUNT is not null and t.SENDBANKTIME is  null  ");*/
		    stringBuilder.append("REPEATCONDITION ,sys_guid(),null,null,'1','IN03',null,'OUT02',null,null,'0',1,null,'1',120,null,null from  SE_BATCH@gardpay_AS330106 t where 1=1  and TOTALAMOUNT is not null and t.SENDBANKTIME is  null and t.batid > '20181130000004' and batid like '2018%' ");
		    String sql = stringBuilder.toString();
		    System.out.println(sql);
		    // 执行查询操作
		    rs = s.executeUpdate(sql);
		    s.close();
		    conn.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	    return rs;
	}
	
	
	/**批次明细表*/
	public static int SE_BD(){
	    Statement s ;
	    int rs = 0 ;
	    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	    	Connection conn = getConn();
	    	System.out.println(sf.format(new Date()));
		    ResultSet result = null ;
	    	// statement对象用于与数据库进行交互
		    s = conn.createStatement();
		    StringBuilder sb = new StringBuilder();
		    sb.append("select table_name from user_tables@gardpay_AS330106 where table_name like 'SE_D20%' and table_name  >= 'SE_D20181130000003'  order by table_name");
		    /*sb.append("select table_name from user_tables@gardpay_AS330106 where table_name like 'SE_D20%'  order by table_name");*/
		    result=s.executeQuery(sb.toString());
		   List<String> tabName = new ArrayList<String>(); 
		    while (result.next()) {
		    	tabName.add(result.getString("table_name"));
		    }
		    int n=20;
		    int size = tabName.size()/n;
		    final List<List<String>> tabNamesList = new ArrayList<List<String>>();
		    for(int i=0;i<n;++i){
		    	List<String> tmp = new ArrayList<String>();
		    	tabNamesList.add(tmp);
		    	for(int j=0;j<size;++j){
		    		tmp.add(tabName.get(i*size+j));
		    	}
		    }
		    for(int j=n*size;j<tabName.size();++j){
		    	tabNamesList.get(tabNamesList.size()-1).add(tabName.get(j));
		    }
		    
		    for(int i=0;i<n;++i){
		    	final int index = i;
		    	new Thread(){
		    		public void run(){
		    			synchBD(tabNamesList.get(index));
		    		}
		    	}.start();
		    	
		    }

		} catch (Exception e) {
			 e.printStackTrace();
		}
	    System.out.println(sf.format(new Date()));
	    return rs;
	}
	
	public static void synchBD(List<String> list){
		try {
			PreparedStatement preparedStatement ;
			Connection conn =getConn();
			int rs = 0;
			for(int i=0;i<list.size();i++){
		    	String tableName = list.get(i);
		    	System.out.println(tableName);
		    	String sql = "select excent2 from " + tableName + "@gardpay_AS330106";
		    	Statement s1 = conn.createStatement();
		    	ResultSet excent2result=s1.executeQuery(sql);
		    	while(excent2result.next()){
		    		StringBuilder stringBuilder =  null;
		    		String excent2 = (String)excent2result.getString("excent2");
		    		String[] excent2array = excent2.split("_");
		    		if(excent2array.length==1){
		    			String flowNo  = excent2array[0];
		    			stringBuilder = new StringBuilder();
				    	stringBuilder.append("insert into SE_BD( BAT_NO,ENT_NUM,BD_NO,CUST_BANK_CODE,ENT_ACC_NUM,CARD_TYPE,CUST_ACC_NUM,CUST_ACC_NAME,CUST_AREA_CODE,CUST_BANK_LOCATION_NAME,CUST_BANK_LOCATION_CODE,PRIVATE_FLAG  ");
				    	stringBuilder.append(",AMOUNT,CURRENCY,PROTOCOL_USER_CODE,PROTOCOL_CODE,CERT_TYPE,CERT_NUM,PHONE,PURPOSE,RECONCILIATION_CODE ,FLOW_NO,SINGLE_TRANS_VERIFY_CODE,SINGLE_QUERY_VERIFY_CODE ,SAME_BANK_FLAG");
					    stringBuilder.append(",BANK_RETURN_STATE,BANK_RETURN_CODE,BANK_RETURN_MSG,MEMO,BANK_LOCATION_MATCH_FLAG,BANK_LOCATION_MATCH_NAME,URGENT_FLAG,MONEY_WAY  ");
					    stringBuilder.append(",BANK_EXTEND_1,BANK_EXTEND_2,BANK_EXTEND_3,BANK_EXTEND_4,BANK_EXTEND_5,ACCOUNTING_DATE,ENT_CHANNEL_CODE,FG_UNIQUE_ID ");
					    stringBuilder.append(",SOURCE_NOTE,MATCH_BANK_ID,MATCH_AREA_CODE,FAIL_ACCOUNTING_DATE,FAIL_BANK_DETAIL_RECONCILE,BANK_DETAIL_RECONCILE,RECONCILE_FILE_RECONCILE ");
					    stringBuilder.append(",TOTAL_AMOUNT_RECONCILE,CYCLE_DATE,BATCH_TO_SINGLE_FLAG,ORG_CODE,SUPPORT_CREDIT_FLAG  ");
					    stringBuilder.append(",UNION_PAY_CARD_TYPE,ABS_TRACT,FAIL_CYCLE_DATE,FAIL_RECONCILE_TYPE ,SOURCE_URID,WARN_REVOKED,REPEAT_CONDITION_VALUE,WARN_DATE  ");
					    stringBuilder.append(",urid,DETAIL_MD5,COMPLIANCE_STATE ");
					    stringBuilder.append(",COMPLIANCE_INFO,OVER_LENGTH,ROW_VERSION,DECODE_AMOUNT,ACCOUNTING_FLAG,REQ_DATE,BANK_RETURN_TIME,POLICY_NUM ");
					    stringBuilder.append(",CUST_AREA_NAME,ENCRYPT_FLAG,CONVERTED_BD_NO,RISK_CONTROL_STATE,RISK_CONTROL_HIT_TYPE,IN_TIME,SEND_BANK_TIME )   ");
		    			stringBuilder.append("select sd.batno,sd.enterprisenum,sd.flowno,sd.bankcode,sd.payaccount,sd.cardtype,sd.accountnum,sd.accountname,sd.areacode,sd.banklocationname,sd.banklocationcode,sd.isprivate  ");
					    stringBuilder.append(",sd.amount,sd.currency,sd.protocolnum,sd.protocolcode,sd.certtype,sd.certnumber,sd.phonenum,sd.purpose,sd.reconciliationcode , ?, null , null ,excent5");
					    stringBuilder.append(",sd.bankreturnstate,sd.bankreturncode,sd.bankreturnmsg,sd.memo,sd.banklocationmatchflag,sd.banklocationmatchname,NVL(sd.isurgent,'0'),sb.PAYTYPE ");
					    stringBuilder.append(",sd.bankextend1,sd.bankextend2,sd.bankextend3,sd.bankextend4,sd.bankextend5,sd.accountingdate,(select t.channel_id from TBIZOPS_ACCOUNT@gardpay_bizops t where t.account_num = sd.payaccount and t.tenant_id='AS330106') ENT_CHANNEL_CODE,sd.fguniqueid ");
					    stringBuilder.append(",sd.sourcenote,sd.matchbankid,sd.matchareacode,sd.failaccountingdate,sd.failbankdetailreconcile,sd.bankdetailreconcile,sd.reconcilefilereconcile ");
					    stringBuilder.append(",sd.totalamountreconcile,sd.cycledate,NVL(sd.issingletrans,0),sd.orgcode,sd.nonsupportcredit    ");
					    stringBuilder.append(",to_char(sd.unionpaycardtype) unionpaycardtype,sd.abstract,sd.failcycledate,to_char( sd.failreconciletype),sd.sourceurid,sd.warnrevoked,sd.repeatconditionvalue,sd.warndate ");
					    stringBuilder.append(",sys_guid() urid,null  DETAIL_MD5,null COMPLIANCE_STATE, ");
					    stringBuilder.append("null COMPLIANCE_INFO,'0' OVER_LENGTH,1 ROW_VERSION ,null DECODE_AMOUNT , ");
					    stringBuilder.append("'2' ACCOUNTING_FLAG,  to_date(sb.REQDATE,'yyyy-MM-dd HH24:mi:ss') REQ_DATE,null BANK_RETURN_TIME,null POLICY_NUM, ");
					    stringBuilder.append("null CUST_AREA_NAME,sb.ENCRYPTFLAG ENCRYPT_FLAG,null CONVERTED_BD_NO,'1' RISK_CONTROL_STATE, ");
					    stringBuilder.append("null RISK_CONTROL_HIT_TYPE,sb.INTIME IN_TIME,null SEND_BANK_TIME ");
					    stringBuilder.append(" from  "+tableName+"@gardpay_AS330106 sd,   SE_BATCH@gardpay_AS330106 sb   where sd.batno=sb.BATNO and  sd.excent2  = ? ");
					    String insertSql = stringBuilder.toString();
				    	 // 执行插入操作
			    		preparedStatement = conn.prepareStatement(insertSql);
			    		preparedStatement.setString(1, flowNo);
			    		preparedStatement.setString(2, excent2);
			    		rs = preparedStatement.executeUpdate();
			    		preparedStatement.close();
		    		}else if(excent2array.length==3){
		    			String flowNo  = excent2array[0];
		    			String single_trans_verify_code  = excent2array[1];
		    			String single_query_verify_code  = excent2array[2];
		    		    stringBuilder = new StringBuilder();
				    	stringBuilder.append("insert into SE_BD( BAT_NO,ENT_NUM,BD_NO,CUST_BANK_CODE,ENT_ACC_NUM,CARD_TYPE,CUST_ACC_NUM,CUST_ACC_NAME,CUST_AREA_CODE,CUST_BANK_LOCATION_NAME,CUST_BANK_LOCATION_CODE,PRIVATE_FLAG  ");
				    	stringBuilder.append(",AMOUNT,CURRENCY,PROTOCOL_USER_CODE,PROTOCOL_CODE,CERT_TYPE,CERT_NUM,PHONE,PURPOSE,RECONCILIATION_CODE ,FLOW_NO,SINGLE_TRANS_VERIFY_CODE,SINGLE_QUERY_VERIFY_CODE ,SAME_BANK_FLAG");
					    stringBuilder.append(",BANK_RETURN_STATE,BANK_RETURN_CODE,BANK_RETURN_MSG,MEMO,BANK_LOCATION_MATCH_FLAG,BANK_LOCATION_MATCH_NAME,URGENT_FLAG,MONEY_WAY  ");
					    stringBuilder.append(",BANK_EXTEND_1,BANK_EXTEND_2,BANK_EXTEND_3,BANK_EXTEND_4,BANK_EXTEND_5,ACCOUNTING_DATE,ENT_CHANNEL_CODE,FG_UNIQUE_ID ");
					    stringBuilder.append(",SOURCE_NOTE,MATCH_BANK_ID,MATCH_AREA_CODE,FAIL_ACCOUNTING_DATE,FAIL_BANK_DETAIL_RECONCILE,BANK_DETAIL_RECONCILE,RECONCILE_FILE_RECONCILE ");
					    stringBuilder.append(",TOTAL_AMOUNT_RECONCILE,CYCLE_DATE,BATCH_TO_SINGLE_FLAG,ORG_CODE,SUPPORT_CREDIT_FLAG  ");
					    stringBuilder.append(",UNION_PAY_CARD_TYPE,ABS_TRACT,FAIL_CYCLE_DATE,FAIL_RECONCILE_TYPE ,SOURCE_URID,WARN_REVOKED,REPEAT_CONDITION_VALUE,WARN_DATE  ");
					    stringBuilder.append(",urid,DETAIL_MD5,COMPLIANCE_STATE ");
					    stringBuilder.append(",COMPLIANCE_INFO,OVER_LENGTH,ROW_VERSION,DECODE_AMOUNT,ACCOUNTING_FLAG,REQ_DATE,BANK_RETURN_TIME,POLICY_NUM ");
					    stringBuilder.append(",CUST_AREA_NAME,ENCRYPT_FLAG,CONVERTED_BD_NO,RISK_CONTROL_STATE,RISK_CONTROL_HIT_TYPE,IN_TIME,SEND_BANK_TIME )   ");
		    			stringBuilder.append("select sd.batno,sd.enterprisenum,sd.flowno,sd.bankcode,sd.payaccount,sd.cardtype,sd.accountnum,sd.accountname,sd.areacode,sd.banklocationname,sd.banklocationcode,sd.isprivate  ");
					    stringBuilder.append(",sd.amount,sd.currency,sd.protocolnum,sd.protocolcode,sd.certtype,sd.certnumber,sd.phonenum,sd.purpose,sd.reconciliationcode ,   ?, ? , ?,excent5 ");
					    stringBuilder.append(",sd.bankreturnstate,sd.bankreturncode,sd.bankreturnmsg,sd.memo,sd.banklocationmatchflag,sd.banklocationmatchname,NVL(sd.isurgent,'0'),sb.PAYTYPE  ");
					    stringBuilder.append(",sd.bankextend1,sd.bankextend2,sd.bankextend3,sd.bankextend4,sd.bankextend5,sd.accountingdate,(select t.channel_id from TBIZOPS_ACCOUNT@gardpay_bizops t where t.account_num = sd.payaccount and t.tenant_id='AS330106') ENT_CHANNEL_CODE,sd.fguniqueid ");
					    stringBuilder.append(",sd.sourcenote,sd.matchbankid,sd.matchareacode,sd.failaccountingdate,sd.failbankdetailreconcile,sd.bankdetailreconcile,sd.reconcilefilereconcile ");
					    stringBuilder.append(",sd.totalamountreconcile,sd.cycledate,NVL(sd.issingletrans,0),sd.orgcode,sd.nonsupportcredit    ");
					    stringBuilder.append(",to_char(sd.unionpaycardtype) unionpaycardtype,sd.abstract,sd.failcycledate,to_char( sd.failreconciletype),sd.sourceurid,sd.warnrevoked,sd.repeatconditionvalue,sd.warndate ");
					    stringBuilder.append(",sys_guid() urid,null  DETAIL_MD5,null COMPLIANCE_STATE, ");
					    stringBuilder.append("null COMPLIANCE_INFO,'0' OVER_LENGTH,1 ROW_VERSION ,null DECODE_AMOUNT , ");
					    stringBuilder.append("'2' ACCOUNTING_FLAG,  to_date(sb.REQDATE,'yyyy-MM-dd HH24:mi:ss') REQ_DATE,null BANK_RETURN_TIME,null POLICY_NUM, ");
					    stringBuilder.append("null CUST_AREA_NAME,sb.ENCRYPTFLAG ENCRYPT_FLAG,null CONVERTED_BD_NO,'1' RISK_CONTROL_STATE, ");
					    stringBuilder.append("null RISK_CONTROL_HIT_TYPE,sb.INTIME IN_TIME,null SEND_BANK_TIME ");
					    stringBuilder.append(" from  "+tableName+"@gardpay_AS330106 sd,   SE_BATCH@gardpay_AS330106 sb   where sd.batno=sb.BATNO and sd.excent2   = ? ");
					    String insertSql = stringBuilder.toString();
				    	 // 执行插入操作
			    		preparedStatement = conn.prepareStatement(insertSql);
			    		preparedStatement.setString(1, flowNo);
			    		preparedStatement.setString(2, single_trans_verify_code);
			    		preparedStatement.setString(3, single_query_verify_code);
			    		preparedStatement.setString(4, excent2);
			    		rs = preparedStatement.executeUpdate();
			    		preparedStatement.close();
		    		}else{
		    			System.out.println("excent2字段不符合规则："+excent2);
		    		}
		    	}
		    	s1.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	/** 批次明细表*/
	/*public static int SE_BD(){
	    Statement s ;
	    Statement s1 ;
	    PreparedStatement preparedStatement ;
	    int rs = 0 ;
	    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	    	
	    	System.out.println(sf.format(new Date()));
	    	Connection conn = getConn();
		    ResultSet result = null ;
	    	// statement对象用于与数据库进行交互
		    s = conn.createStatement();
		    StringBuilder sb = new StringBuilder();
		    sb.append("select table_name from user_tables@gardpay_AS330106 where table_name like 'SE_D20%' and table_name < 'SE_D20181107000002' order by table_name");
		    result=s.executeQuery(sb.toString());
		    while (result.next()) {
		    	s1 = conn.createStatement();
		    	String tableName = (String)result.getString("table_name");
		    	System.out.println(tableName);
		    	String sql = "select excent2 from " + tableName + "@gardpay_AS330106";
		    	ResultSet excent2result=s1.executeQuery(sql);
		    	while(excent2result.next()){
		    		StringBuilder stringBuilder =  null;
		    		String excent2 = (String)excent2result.getString("excent2");
		    		String[] excent2array = excent2.split("_");
		    		if(excent2array.length==1){
		    			String flowNo  = excent2array[0];
		    			stringBuilder = new StringBuilder();
				    	stringBuilder.append("insert into SE_BD( BAT_NO,ENT_NUM,BD_NO,CUST_BANK_CODE,ENT_ACC_NUM,CARD_TYPE,CUST_ACC_NUM,CUST_ACC_NAME,CUST_AREA_CODE,CUST_BANK_LOCATION_NAME,CUST_BANK_LOCATION_CODE,PRIVATE_FLAG  ");
				    	stringBuilder.append(",AMOUNT,CURRENCY,PROTOCOL_USER_CODE,PROTOCOL_CODE,CERT_TYPE,CERT_NUM,PHONE,PURPOSE,RECONCILIATION_CODE ,FLOW_NO,SINGLE_TRANS_VERIFY_CODE,SINGLE_QUERY_VERIFY_CODE ,SAME_BANK_FLAG");
					    stringBuilder.append(",BANK_RETURN_STATE,BANK_RETURN_CODE,BANK_RETURN_MSG,MEMO,BANK_LOCATION_MATCH_FLAG,BANK_LOCATION_MATCH_NAME,URGENT_FLAG,MONEY_WAY  ");
					    stringBuilder.append(",BANK_EXTEND_1,BANK_EXTEND_2,BANK_EXTEND_3,BANK_EXTEND_4,BANK_EXTEND_5,ACCOUNTING_DATE,ENT_CHANNEL_CODE,FG_UNIQUE_ID ");
					    stringBuilder.append(",SOURCE_NOTE,MATCH_BANK_ID,MATCH_AREA_CODE,FAIL_ACCOUNTING_DATE,FAIL_BANK_DETAIL_RECONCILE,BANK_DETAIL_RECONCILE,RECONCILE_FILE_RECONCILE ");
					    stringBuilder.append(",TOTAL_AMOUNT_RECONCILE,CYCLE_DATE,BATCH_TO_SINGLE_FLAG,ORG_CODE,SUPPORT_CREDIT_FLAG  ");
					    stringBuilder.append(",UNION_PAY_CARD_TYPE,ABS_TRACT,FAIL_CYCLE_DATE,FAIL_RECONCILE_TYPE ,SOURCE_URID,WARN_REVOKED,REPEAT_CONDITION_VALUE,WARN_DATE  ");
					    stringBuilder.append(",urid,DETAIL_MD5,COMPLIANCE_STATE ");
					    stringBuilder.append(",COMPLIANCE_INFO,OVER_LENGTH,ROW_VERSION,DECODE_AMOUNT,ACCOUNTING_FLAG,REQ_DATE,BANK_RETURN_TIME,POLICY_NUM ");
					    stringBuilder.append(",CUST_AREA_NAME,ENCRYPT_FLAG,CONVERTED_BD_NO,RISK_CONTROL_STATE,RISK_CONTROL_HIT_TYPE,IN_TIME,SEND_BANK_TIME )   ");
		    			stringBuilder.append("select sd.batno,sd.enterprisenum,sd.flowno,sd.bankcode,sd.payaccount,sd.cardtype,sd.accountnum,sd.accountname,sd.areacode,sd.banklocationname,sd.banklocationcode,sd.isprivate  ");
					    stringBuilder.append(",sd.amount,sd.currency,sd.protocolnum,sd.protocolcode,sd.certtype,sd.certnumber,sd.phonenum,sd.purpose,sd.reconciliationcode , ?, null , null ,excent5");
					    stringBuilder.append(",sd.bankreturnstate,sd.bankreturncode,sd.bankreturnmsg,sd.memo,sd.banklocationmatchflag,sd.banklocationmatchname,NVL(sd.isurgent,'0'),sd.detailtranstype ");
					    stringBuilder.append(",sd.bankextend1,sd.bankextend2,sd.bankextend3,sd.bankextend4,sd.bankextend5,sd.accountingdate,(select t.channel_id from TBIZOPS_ACCOUNT@gardpay_bizops t where t.account_num = sd.payaccount and t.tenant_id='AS330106') ENT_CHANNEL_CODE,sd.fguniqueid ");
					    stringBuilder.append(",sd.sourcenote,sd.matchbankid,sd.matchareacode,sd.failaccountingdate,sd.failbankdetailreconcile,sd.bankdetailreconcile,sd.reconcilefilereconcile ");
					    stringBuilder.append(",sd.totalamountreconcile,sd.cycledate,sd.issingletrans,sd.orgcode,sd.nonsupportcredit    ");
					    stringBuilder.append(",to_char(sd.unionpaycardtype) unionpaycardtype,sd.abstract,sd.failcycledate,to_char( sd.failreconciletype),sd.sourceurid,sd.warnrevoked,sd.repeatconditionvalue,sd.warndate ");
					    stringBuilder.append(",sys_guid() urid,null  DETAIL_MD5,null COMPLIANCE_STATE, ");
					    stringBuilder.append("null COMPLIANCE_INFO,'0' OVER_LENGTH,1 ROW_VERSION ,null DECODE_AMOUNT , ");
					    stringBuilder.append("'2' ACCOUNTING_FLAG,  to_date(sb.REQDATE,'yyyy-MM-dd HH24:mi:ss') REQ_DATE,null BANK_RETURN_TIME,null POLICY_NUM, ");
					    stringBuilder.append("null CUST_AREA_NAME,sb.ENCRYPTFLAG ENCRYPT_FLAG,null CONVERTED_BD_NO,'1' RISK_CONTROL_STATE, ");
					    stringBuilder.append("null RISK_CONTROL_HIT_TYPE,sb.INTIME IN_TIME,null SEND_BANK_TIME ");
					    stringBuilder.append(" from  "+tableName+"@gardpay_AS330106 sd,   SE_BATCH@gardpay_AS330106 sb   where sd.batno=sb.BATNO and  sd.excent2  = ? ");
					    String insertSql = stringBuilder.toString();
				    	 // 执行插入操作
			    		preparedStatement = conn.prepareStatement(insertSql);
			    		preparedStatement.setString(1, flowNo);
			    		preparedStatement.setString(2, excent2);
			    		rs = preparedStatement.executeUpdate();
			    		preparedStatement.close();
		    		}else if(excent2array.length==3){
		    			String flowNo  = excent2array[0];
		    			String single_trans_verify_code  = excent2array[1];
		    			String single_query_verify_code  = excent2array[2];
		    		    stringBuilder = new StringBuilder();
				    	stringBuilder.append("insert into SE_BD( BAT_NO,ENT_NUM,BD_NO,CUST_BANK_CODE,ENT_ACC_NUM,CARD_TYPE,CUST_ACC_NUM,CUST_ACC_NAME,CUST_AREA_CODE,CUST_BANK_LOCATION_NAME,CUST_BANK_LOCATION_CODE,PRIVATE_FLAG  ");
				    	stringBuilder.append(",AMOUNT,CURRENCY,PROTOCOL_USER_CODE,PROTOCOL_CODE,CERT_TYPE,CERT_NUM,PHONE,PURPOSE,RECONCILIATION_CODE ,FLOW_NO,SINGLE_TRANS_VERIFY_CODE,SINGLE_QUERY_VERIFY_CODE ,SAME_BANK_FLAG");
					    stringBuilder.append(",BANK_RETURN_STATE,BANK_RETURN_CODE,BANK_RETURN_MSG,MEMO,BANK_LOCATION_MATCH_FLAG,BANK_LOCATION_MATCH_NAME,URGENT_FLAG,MONEY_WAY  ");
					    stringBuilder.append(",BANK_EXTEND_1,BANK_EXTEND_2,BANK_EXTEND_3,BANK_EXTEND_4,BANK_EXTEND_5,ACCOUNTING_DATE,ENT_CHANNEL_CODE,FG_UNIQUE_ID ");
					    stringBuilder.append(",SOURCE_NOTE,MATCH_BANK_ID,MATCH_AREA_CODE,FAIL_ACCOUNTING_DATE,FAIL_BANK_DETAIL_RECONCILE,BANK_DETAIL_RECONCILE,RECONCILE_FILE_RECONCILE ");
					    stringBuilder.append(",TOTAL_AMOUNT_RECONCILE,CYCLE_DATE,BATCH_TO_SINGLE_FLAG,ORG_CODE,SUPPORT_CREDIT_FLAG  ");
					    stringBuilder.append(",UNION_PAY_CARD_TYPE,ABS_TRACT,FAIL_CYCLE_DATE,FAIL_RECONCILE_TYPE ,SOURCE_URID,WARN_REVOKED,REPEAT_CONDITION_VALUE,WARN_DATE  ");
					    stringBuilder.append(",urid,DETAIL_MD5,COMPLIANCE_STATE ");
					    stringBuilder.append(",COMPLIANCE_INFO,OVER_LENGTH,ROW_VERSION,DECODE_AMOUNT,ACCOUNTING_FLAG,REQ_DATE,BANK_RETURN_TIME,POLICY_NUM ");
					    stringBuilder.append(",CUST_AREA_NAME,ENCRYPT_FLAG,CONVERTED_BD_NO,RISK_CONTROL_STATE,RISK_CONTROL_HIT_TYPE,IN_TIME,SEND_BANK_TIME )   ");
		    			stringBuilder.append("select sd.batno,sd.enterprisenum,sd.flowno,sd.bankcode,sd.payaccount,sd.cardtype,sd.accountnum,sd.accountname,sd.areacode,sd.banklocationname,sd.banklocationcode,sd.isprivate  ");
					    stringBuilder.append(",sd.amount,sd.currency,sd.protocolnum,sd.protocolcode,sd.certtype,sd.certnumber,sd.phonenum,sd.purpose,sd.reconciliationcode ,   ?, ? , ?,excent5 ");
					    stringBuilder.append(",sd.bankreturnstate,sd.bankreturncode,sd.bankreturnmsg,sd.memo,sd.banklocationmatchflag,sd.banklocationmatchname,NVL(sd.isurgent,'0'),sd.detailtranstype ");
					    stringBuilder.append(",sd.bankextend1,sd.bankextend2,sd.bankextend3,sd.bankextend4,sd.bankextend5,sd.accountingdate,(select t.channel_id from TBIZOPS_ACCOUNT@gardpay_bizops t where t.account_num = sd.payaccount and t.tenant_id='AS330106') ENT_CHANNEL_CODE,sd.fguniqueid ");
					    stringBuilder.append(",sd.sourcenote,sd.matchbankid,sd.matchareacode,sd.failaccountingdate,sd.failbankdetailreconcile,sd.bankdetailreconcile,sd.reconcilefilereconcile ");
					    stringBuilder.append(",sd.totalamountreconcile,sd.cycledate,sd.issingletrans,sd.orgcode,sd.nonsupportcredit    ");
					    stringBuilder.append(",to_char(sd.unionpaycardtype) unionpaycardtype,sd.abstract,sd.failcycledate,to_char( sd.failreconciletype),sd.sourceurid,sd.warnrevoked,sd.repeatconditionvalue,sd.warndate ");
					    stringBuilder.append(",sys_guid() urid,null  DETAIL_MD5,null COMPLIANCE_STATE, ");
					    stringBuilder.append("null COMPLIANCE_INFO,'0' OVER_LENGTH,1 ROW_VERSION ,null DECODE_AMOUNT , ");
					    stringBuilder.append("'2' ACCOUNTING_FLAG,  to_date(sb.REQDATE,'yyyy-MM-dd HH24:mi:ss') REQ_DATE,null BANK_RETURN_TIME,null POLICY_NUM, ");
					    stringBuilder.append("null CUST_AREA_NAME,sb.ENCRYPTFLAG ENCRYPT_FLAG,null CONVERTED_BD_NO,'1' RISK_CONTROL_STATE, ");
					    stringBuilder.append("null RISK_CONTROL_HIT_TYPE,sb.INTIME IN_TIME,null SEND_BANK_TIME ");
					    stringBuilder.append(" from  "+tableName+"@gardpay_AS330106 sd,   SE_BATCH@gardpay_AS330106 sb   where sd.batno=sb.BATNO and sd.excent2   = ? ");
					    String insertSql = stringBuilder.toString();
				    	 // 执行插入操作
			    		preparedStatement = conn.prepareStatement(insertSql);
			    		preparedStatement.setString(1, flowNo);
			    		preparedStatement.setString(2, single_trans_verify_code);
			    		preparedStatement.setString(3, single_query_verify_code);
			    		preparedStatement.setString(4, excent2);
			    		rs = preparedStatement.executeUpdate();
			    		preparedStatement.close();
		    		}else{
		    			System.out.println("excent2字段不符合规则："+excent2);
		    		}
		    	}
		    	s1.close();
		    }
		    s.close();
		    conn.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	    System.out.println(sf.format(new Date()));
	    return rs;
	}*/
	
	/** 同步批次原始表*/
	public static int SE_BATCH_ORI (){
	    Statement s ;
	    int rs = 0 ;
	    try {
	    	//获取连接
	    	Connection conn = getConn();
	    	// statement对象用于与数据库进行交互
		    s = conn.createStatement();
		    StringBuilder stringBuilder = new StringBuilder();
		    stringBuilder.append("insert into SE_BATCH_ORI(ENT_NUM,BAT_NO,REQ_DATE,DEAL_STATE,TOTAL_AMOUNT,TOTAL_NUM");
		    stringBuilder.append(",VERIFY_CODE,RECONCILIATION_CODE,ACCOUNTING_FLAG,ENCRYPT_FLAG,SOURCE_DATA_MD5,SOURCE_TOTAL_AMOUNT,MONEY_WAY");
		    stringBuilder.append(",REPEAT_CONDITION,URID,FILE_NAME,REQUEST_TYPE,ROW_VERSION,MD5,LAST_MODIFY_USER,LAST_MODIFY_TIME,FC_CODE ,INFO,VALUE,FORCES,QUERY_TIME,RECEIVE_TIME)");
		    stringBuilder.append("select ENTERPRISENUM,BATNO,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') REQDATE,case DEALSTATE when 81 then '11' when 52 then 'S04' else 'ZZZ' end DEALSTATE,TOTALAMOUNT,TOTALNUM,");
		    stringBuilder.append("VERIFYCODE,RECONCILIATIONCODE,ACCOUNTINGFLAG,ENCRYPTFLAG,SOURCEDATAMD5,SOURCETOTALAMOUNT,PAYTYPE,");
		    /*stringBuilder.append("REPEATCONDITION ,sys_guid(),null,'1',1,null,null,null,null,ERRORTYPE,ERRORMSG,null,null,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') from  SE_BATCH@gardpay_AS330106 t where 1=1 and t.batid < '20181107000002' and t.SENDBANKTIME is not null ");*/
		    /*stringBuilder.append("REPEATCONDITION ,sys_guid(),null,'1',1,null,null,null,null,ERRORTYPE,ERRORMSG,null,null,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') from  SE_BATCH@gardpay_AS330106 t where 1=1  and t.SENDBANKTIME is not null ");*/
		    stringBuilder.append("REPEATCONDITION ,sys_guid(),null,'1',1,null,null,null,null,ERRORTYPE,ERRORMSG,null,null,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') from  SE_BATCH@gardpay_AS330106 t where 1=1  and t.SENDBANKTIME is not null and t.batid > '20181130000004' and batid like '2018%' ");
		    stringBuilder.append("union ");
		    stringBuilder.append("select ENTERPRISENUM,BATNO,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') REQDATE,case DEALSTATE when 81 then '10' when 52 then 'S04' else 'ZZZ' end DEALSTATE,TOTALAMOUNT,TOTALNUM,");
		    stringBuilder.append("VERIFYCODE,RECONCILIATIONCODE,ACCOUNTINGFLAG,ENCRYPTFLAG,SOURCEDATAMD5,SOURCETOTALAMOUNT,PAYTYPE,");
		    /*stringBuilder.append("REPEATCONDITION ,sys_guid(),null,'1',1,null,null,null,null,ERRORTYPE,ERRORMSG,null,null,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') from  SE_BATCH@gardpay_AS330106 t where 1=1 and t.batid < '20181107000002' and t.SENDBANKTIME is  null ");*/
		    /*stringBuilder.append("REPEATCONDITION ,sys_guid(),null,'1',1,null,null,null,null,ERRORTYPE,ERRORMSG,null,null,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') from  SE_BATCH@gardpay_AS330106 t where 1=1 and t.SENDBANKTIME is  null and batno <> '16070600139988' ");*/
		    stringBuilder.append("REPEATCONDITION ,sys_guid(),null,'1',1,null,null,null,null,ERRORTYPE,ERRORMSG,null,null,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') from  SE_BATCH@gardpay_AS330106 t where 1=1 and t.SENDBANKTIME is  null and t.batid > '20181130000004' and batid like '2018%' ");
		    stringBuilder.append("union ");
		    stringBuilder.append("select ENTERPRISENUM,BATNO,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') REQDATE,case DEALSTATE when 81 then '10' when 52 then 'S04' else 'ZZZ' end DEALSTATE,TOTALAMOUNT,TOTALNUM,");
		    stringBuilder.append("VERIFYCODE,RECONCILIATIONCODE,ACCOUNTINGFLAG,ENCRYPTFLAG,SOURCEDATAMD5,SOURCETOTALAMOUNT,PAYTYPE,");
		    /*stringBuilder.append("REPEATCONDITION ,sys_guid(),null,'1',1,null,null,null,null,ERRORTYPE,ERRORMSG,null,null,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') from  SE_BATCH@gardpay_AS330106 t where 1=1 and t.batid < '20181107000002' and t.SENDBANKTIME is  null ");*/
		   /* stringBuilder.append("REPEATCONDITION ,sys_guid(),null,'1',1,null,null,null,null,ERRORTYPE,substr(ERRORMSG, 0, 13),null,null,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') from  SE_BATCH@gardpay_AS330106 t where 1=1 and t.SENDBANKTIME is  null and batno = '16070600139988' ");*/
		    stringBuilder.append("REPEATCONDITION ,sys_guid(),null,'1',1,null,null,null,null,ERRORTYPE,substr(ERRORMSG, 0, 13),null,null,to_date(REQDATE,'yyyy-MM-dd HH24:mi:ss') from  SE_BATCH@gardpay_AS330106 t where 1=1 and t.SENDBANKTIME is  null and t.batid > '20181130000004' and batid like '2018%' ");
		    String sql = stringBuilder.toString();
		    System.out.println(sql);
		    // 执行查询操作
		    rs = s.executeUpdate(sql);
		    s.close();
		    conn.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	    return rs;
	}
	
	/** 同步批次明细原始表 */
	/*public static int SE_BD_ORI(){
	    Statement s ;
	    Statement s1 ;
	    PreparedStatement preparedStatement ;
	    int rs = 0 ;
	    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	    	Connection conn = getConn();
	    	System.out.println(sf.format(new Date()));
		    ResultSet result = null ;
	    	// statement对象用于与数据库进行交互
		    s = conn.createStatement();
		    StringBuilder sb = new StringBuilder();
		    sb.append("select table_name from user_tables@gardpay_AS330106 where table_name like 'SE_D20%' and table_name < 'SE_D20181107000002' order by table_name");
		    result=s.executeQuery(sb.toString());
		    while (result.next()) {
		    	s1 = conn.createStatement();
		    	String tableName = (String)result.getString("table_name");
		    	System.out.println(tableName);
		    	String sql = "select excent2 from " + tableName + "@gardpay_AS330106";
		    	ResultSet excent2result=s1.executeQuery(sql);
		    	while(excent2result.next()){
		    		StringBuilder stringBuilder =  null;
		    		String excent2 = (String)excent2result.getString("excent2");
		    		String[] excent2array = excent2.split("_");
		    		if(excent2array.length==1){
		    			String flowNo  = excent2array[0];
		    			stringBuilder = new StringBuilder();
				    	stringBuilder.append("insert into SE_BD_ORI( BAT_NO,ENT_NUM,BD_NO,CUST_BANK_CODE,ENT_ACC_NUM,CARD_TYPE,CUST_ACC_NUM,CUST_ACC_NAME,CUST_AREA_CODE,CUST_BANK_LOCATION_NAME,CUST_BANK_LOCATION_CODE,PRIVATE_FLAG  ");
				    	stringBuilder.append(",AMOUNT,CURRENCY,PROTOCOL_USER_CODE,PROTOCOL_CODE,CERT_TYPE,CERT_NUM,PHONE,PURPOSE,RECONCILIATION_CODE ,FLOW_NO,SINGLE_TRANS_VERIFY_CODE,SINGLE_QUERY_VERIFY_CODE ,SAME_BANK_FLAG ");
					    stringBuilder.append(",MEMO,URGENT_FLAG ,ENT_CHANNEL_CODE ,SOURCE_NOTE,ORG_CODE,SOURCE_URID,WARN_REVOKED,REPEAT_CONDITION_VALUE,WARN_DATE ,URID,DETAIL_MD5,OVER_LENGTH,ROW_VERSION,REQ_DATE,CUST_AREA_NAME,RECEIVE_TIME )   ");
		    			stringBuilder.append("select sd.batno,sd.enterprisenum,sd.flowno,sd.bankcode,sd.payaccount,sd.cardtype,sd.accountnum,sd.accountname,sd.areacode,sd.banklocationname,sd.banklocationcode,sd.isprivate  ");
					    stringBuilder.append(",sd.amount,sd.currency,sd.protocolnum,sd.protocolcode,sd.certtype,sd.certnumber,sd.phonenum,sd.purpose,sd.reconciliationcode ,   ?, null , null ,excent5");
					    stringBuilder.append(",sd.memo,NVL(sd.isurgent,'0') ,(select t.channel_id from TBIZOPS_ACCOUNT@gardpay_bizops t where t.account_num = sd.payaccount and t.tenant_id='AS330106') ENT_CHANNEL_CODE ");
					    stringBuilder.append(",sd.sourcenote,sd.orgcode,sd.sourceurid,sd.warnrevoked,sd.repeatconditionvalue,sd.warndate ");
					    stringBuilder.append(",sys_guid() urid,null  DETAIL_MD5,'0' OVER_LENGTH,1 ROW_VERSION ,to_date(sb.REQDATE,'yyyy-MM-dd HH24:mi:ss') REQ_DATE, null CUST_AREA_NAME,null ");
					    stringBuilder.append(" from  "+tableName+"@gardpay_AS330106 sd,   SE_BATCH@gardpay_AS330106 sb   where sd.batno=sb.BATNO and  sd.excent2  = ? ");
					    String insertSql = stringBuilder.toString();
				    	 // 执行插入操作
			    		preparedStatement = conn.prepareStatement(insertSql);
			    		preparedStatement.setString(1, flowNo);
			    		preparedStatement.setString(2, excent2);
			    		rs = preparedStatement.executeUpdate();
			    		preparedStatement.close();
		    		}else if(excent2array.length==3){
		    			String flowNo  = excent2array[0];
		    			String single_trans_verify_code  = excent2array[1];
		    			String single_query_verify_code  = excent2array[2];
		    		    stringBuilder = new StringBuilder();
				    	stringBuilder.append("insert into SE_BD_ORI( BAT_NO,ENT_NUM,BD_NO,CUST_BANK_CODE,ENT_ACC_NUM,CARD_TYPE,CUST_ACC_NUM,CUST_ACC_NAME,CUST_AREA_CODE,CUST_BANK_LOCATION_NAME,CUST_BANK_LOCATION_CODE,PRIVATE_FLAG  ");
				    	stringBuilder.append(",AMOUNT,CURRENCY,PROTOCOL_USER_CODE,PROTOCOL_CODE,CERT_TYPE,CERT_NUM,PHONE,PURPOSE,RECONCILIATION_CODE ,FLOW_NO,SINGLE_TRANS_VERIFY_CODE,SINGLE_QUERY_VERIFY_CODE ,SAME_BANK_FLAG");
					    stringBuilder.append(",MEMO,URGENT_FLAG ,ENT_CHANNEL_CODE ,SOURCE_NOTE,ORG_CODE,SOURCE_URID,WARN_REVOKED,REPEAT_CONDITION_VALUE,WARN_DATE ,URID,DETAIL_MD5,OVER_LENGTH,ROW_VERSION,REQ_DATE,CUST_AREA_NAME,RECEIVE_TIME )   ");
		    			stringBuilder.append("select sd.batno,sd.enterprisenum,sd.flowno,sd.bankcode,sd.payaccount,sd.cardtype,sd.accountnum,sd.accountname,sd.areacode,sd.banklocationname,sd.banklocationcode,sd.isprivate  ");
					    stringBuilder.append(",sd.amount,sd.currency,sd.protocolnum,sd.protocolcode,sd.certtype,sd.certnumber,sd.phonenum,sd.purpose,sd.reconciliationcode ,   ?, ?, ?,excent5 ");
					    stringBuilder.append(",sd.memo,NVL(sd.isurgent,'0') ,(select t.channel_id from TBIZOPS_ACCOUNT@gardpay_bizops t where t.account_num = sd.payaccount and t.tenant_id='AS330106') ENT_CHANNEL_CODE ");
					    stringBuilder.append(",sd.sourcenote,sd.orgcode,sd.sourceurid,sd.warnrevoked,sd.repeatconditionvalue,sd.warndate ");
					    stringBuilder.append(",sys_guid() urid,null  DETAIL_MD5,'0' OVER_LENGTH,1 ROW_VERSION ,to_date(sb.REQDATE,'yyyy-MM-dd HH24:mi:ss') REQ_DATE, null CUST_AREA_NAME,null ");
					    stringBuilder.append(" from  "+tableName+"@gardpay_AS330106 sd,   SE_BATCH@gardpay_AS330106 sb   where sd.batno=sb.BATNO and  sd.excent2  = ? ");
					    String insertSql = stringBuilder.toString();
				    	 // 执行插入操作
			    		preparedStatement = conn.prepareStatement(insertSql);
			    		preparedStatement.setString(1, flowNo);
			    		preparedStatement.setString(2, single_trans_verify_code);
			    		preparedStatement.setString(3, single_query_verify_code);
			    		preparedStatement.setString(4, excent2);
			    		rs = preparedStatement.executeUpdate();
			    		preparedStatement.close();
		    		}else{
		    			System.out.println("excent2字段不符合规则："+excent2);
		    		}
		    	}
		    	s1.close();
		    }
		    s.close();
		    conn.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	    System.out.println(sf.format(new Date()));
	    return rs;
	}*/
	
	/** 同步工行预处理表*/
	public static int SE_PRE_TREATMENT_ICBC (){
	    Statement s ;
	    int rs = 0 ;
	    try {
	    	//获取连接
	    	Connection conn = getConn();
	    	// statement对象用于与数据库进行交互
		    s = conn.createStatement();
		    StringBuilder stringBuilder = new StringBuilder();
		    stringBuilder.append("insert into SE_PRE_TREATMENT_ICBC(URID,CUST_ACC_NUM,CUST_ACC_NAME,ENT_ACC_NUM,POLICY_NUM,SAVE_TIME)");
		    /*stringBuilder.append("select sys_guid(),CUSTACCNUM,CUSTACCNAME,CORPACCNUM, POLICYNUM,sysdate from  SC_Detail_Sucess_102@gardpay_AS330106 t where 1=1  ");*/
		    stringBuilder.append("select sys_guid(),CUSTACCNUM,CUSTACCNAME,CORPACCNUM, POLICYNUM,sysdate from  SC_Detail_Sucess_102@gardpay_AS330106 t where 1=1 and sourcebatid > '20181201000003'  and  sourcebatid like '201812%'   ");
		    String sql = stringBuilder.toString();
		    System.out.println(sql);
		    // 执行查询操作
		    rs = s.executeUpdate(sql);
		    s.close();
		    conn.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	    return rs;
	}
	/** 同步风控表*/
	public static int TRISK_BD(){
	    Statement s ;
	    Statement s2 ;
	    int rs = 0 ;
	    try {
	    	System.out.println(System.currentTimeMillis());
	    	Connection conn = getConn();
		    ResultSet result = null ;
	    	// statement对象用于与数据库进行交互
		    s = conn.createStatement();
		    StringBuilder sb = new StringBuilder();
		    /*sb.append("select table_name from user_tables@gardpay_AS330106 where table_name like 'SE_D20%' and table_name  < 'SE_D20181107000002'  order by table_name");*/
		    /*sb.append("select table_name from user_tables@gardpay_AS330106 where table_name like 'SE_D20%'   order by table_name");*/
		    sb.append("select table_name from user_tables@gardpay_AS330106 where table_name like 'SE_D20%'  and table_name  >= 'SE_D20181130000003'  order by table_name");
		    result=s.executeQuery(sb.toString());
		    while (result.next()) {
		    	String tableName = (String)result.getString("table_name");
		    	System.out.println(tableName);
	    		s2 = conn.createStatement();
	    		StringBuilder stringBuilder =  null;
	    		stringBuilder = new StringBuilder();
		    	stringBuilder.append("insert into TRISK_BD( BAT_NO,ENT_NUM,BD_NO,CUST_BANK_CODE,ENT_ACC_NUM,CUST_ACC_NUM,CUST_ACC_NAME,CUST_AREA_CODE,CUST_BANK_LOCATION_NAME,CUST_BANK_LOCATION_CODE  ");
		    	stringBuilder.append(",AMOUNT,PROTOCOL_USER_CODE,PROTOCOL_CODE,CERT_NUM,PHONE,PURPOSE,RECONCILIATION_CODE ");
			    stringBuilder.append(",MEMO,MONEY_WAY,ENT_CHANNEL_CODE,ORG_CODE,SOURCE_URID,WARN_REVOKED,REPEAT_CONDITION_VALUE,WARN_DATE ,urid,DETAIL_MD5,REQ_DATE)   ");
    			stringBuilder.append("select sd.batno,sd.enterprisenum,sd.flowno,sd.bankcode,sd.payaccount,sd.accountnum,sd.accountname,sd.areacode,sd.banklocationname,sd.banklocationcode  ");
			    stringBuilder.append(",sd.amount,sd.protocolnum,sd.protocolcode,sd.certnumber,sd.phonenum,sd.purpose,sd.reconciliationcode ");
			    stringBuilder.append(",sd.memo,sb.PAYTYPE ,(select t.channel_id from TBIZOPS_ACCOUNT@gardpay_bizops t where t.account_num = sd.payaccount) ENT_CHANNEL_CODE,sd.orgcode ,sd.sourceurid,sd.warnrevoked,sd.repeatconditionvalue,sd.warndate ");
			    stringBuilder.append(",sys_guid() urid,null  DETAIL_MD5, to_date(sb.REQDATE,'yyyy-MM-dd HH24:mi:ss') REQ_DATE ");
			    stringBuilder.append(" from  "+tableName+"@gardpay_AS330106 sd,   SE_BATCH@gardpay_AS330106 sb   where sd.batno=sb.BATNO and sd.bankreturnstate  not in ('3') ");
	    		String insertSql = stringBuilder.toString();
		    	 // 执行插入操作
			    rs = s2.executeUpdate(insertSql);
			    s2.close();
		    }
		    s.close();
		    conn.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	    System.out.println(System.currentTimeMillis());
	    return rs;
	}
	
	/** 同步序列表*/
	public static int SE_TRANS_SEQ_HOLDER (){
	    Statement s ;
	    int rs = 0 ;
	    try {
	    	//获取连接
	    	Connection conn = getConn();
	    	// statement对象用于与数据库进行交互
		    s = conn.createStatement();
		    StringBuilder stringBuilder = new StringBuilder();
		    stringBuilder.append("insert into SE_TRANS_SEQ_HOLDER(ID,TENANT_ID,TRANS_DATE,END_NO)");
		    stringBuilder.append("select URID,ENTERPRISENUM,to_char(TRANSDATE,'yyyyMMdd') TRANSDATE,MAXNUM+10000000 from  FCDETAILMAXTRANSNO@gardpay_batchserver_AS330106  t where 1=1 and ENTERPRISENUM ='AS330106' ");
		    String sql = stringBuilder.toString();
		    System.out.println(sql);
		    // 执行查询操作
		    rs = s.executeUpdate(sql);
		    s.close();
		    conn.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	    return rs;
	}
	
	
	public static void main(String[] args) {
		int i = SE_BATCH();
		/*int j = SE_BATCH_ORI();*/
		int k = SE_BD();
		/*int n =SE_PRE_TREATMENT_ICBC ();*/
//		int o =TRISK_BD();
		/*System.out.println(i);
		System.out.println(j);
		System.out.println(k);
		System.out.println(n);
		System.out.println(o);*/
		/*SE_TRANS_SEQ_HOLDER();*/
		
	}
	
	
}
