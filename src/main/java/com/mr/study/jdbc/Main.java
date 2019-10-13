package com.mr.study.jdbc;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhanxp
 * @version 1.0 2019/7/5
 */
public class Main {
    private static final String driverClassName = "oracle.jdbc.driver.OracleDriver";
    private static final String javaurl = "jdbc:oracle:thin:@10.60.45.173:1521:fingard";
    private static final String username = "AS330106";
    private static final String password = "AS330106";

    public static void main(String[] args) {

        //批次表
        //        executeSql(initBatch());

        //批次明细表
        //        initDetail();

        //单笔
        executeSql(initSingle());
    }

    /**
     * 获取数据库连接
     */
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

    private static void executeSql(String sql) {
        Statement s;
        int rs = 0;
        try {
            System.out.println("当前执行的sql为:" + sql);
            //获取连接
            Connection conn = getConn();
            // statement对象用于与数据库进行交互
            s = conn.createStatement();
            // 执行查询操作
            rs = s.executeUpdate(sql);
            s.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("影响的行数为:" + rs);
    }

    private static void initDetail() {
        Statement s;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Connection conn = getConn();
            ResultSet result;
            // statement对象用于与数据库进行交互
            s = conn.createStatement();
            String tableListSql = "select table_name from user_tables@linkAC110005 where table_name like 'SE_D20%' and table_name  >= 'SE_D20190704000003' and table_name <='SE_D20190705000003'  order by table_name";
            result = s.executeQuery(tableListSql);
            List<String> tabName = new LinkedList<>();
            while (result.next()) {
                tabName.add(result.getString("table_name"));
            }
            int n = 20;
            int size = tabName.size() / n;
            final List<List<String>> tabNamesList = new LinkedList<>();
            for (int i = 0; i < n; ++i) {
                List<String> tmp = new LinkedList<>();
                tabNamesList.add(tmp);
                for (int j = 0; j < size; ++j) {
                    tmp.add(tabName.get(i * size + j));
                }
            }
            for (int j = n * size; j < tabName.size(); ++j) {
                tabNamesList.get(tabNamesList.size() - 1).add(tabName.get(j));
            }

            for (int i = 0; i < n; ++i) {
                final int index = i;
                new Thread() {
                    @Override
                    public void run() {
                        synchBD(tabNamesList.get(index));
                    }
                }.start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sf.format(new Date()));
    }

    private static void synchBD(List<String> tableList) {
        try {
            PreparedStatement preparedStatement;
            Connection conn = getConn();
            int rs;
            for (int i = 0; i < tableList.size(); i++) {
                String tableName = tableList.get(i);
                System.out.println(tableName);
                String sql = "select excent2 from " + tableName + "@linkAC110005";
                Statement s1 = conn.createStatement();
                ResultSet excent2result = s1.executeQuery(sql);
                while (excent2result.next()) {
                    String excent2 = excent2result.getString("excent2");
                    String[] excent2array = excent2.split("_");
                    if (excent2array.length == 1) {
                        String flowNo = excent2array[0];

                        String insertSql = initBatchDetailSql1(tableName);
                        System.out.println("需要执行的sql为：" + insertSql);
                        // 执行插入操作
                        preparedStatement = conn.prepareStatement(insertSql);
                        preparedStatement.setString(1, flowNo);
                        preparedStatement.setString(2, excent2);
                        rs = preparedStatement.executeUpdate();
                        System.out.println("插入:" + rs + "行");
                        preparedStatement.close();
                    } else if (excent2array.length == 3) {
                        String flowNo = excent2array[0];

                        String insertSql = initBatchDetailSql2(tableName);
                        System.out.println("需要执行的sql为：" + insertSql);
                        // 执行插入操作
                        preparedStatement = conn.prepareStatement(insertSql);
                        preparedStatement.setString(1, flowNo);
                        preparedStatement.setString(2, excent2array[1]);
                        preparedStatement.setString(3, excent2array[2]);
                        preparedStatement.setString(4, excent2);
                        rs = preparedStatement.executeUpdate();
                        System.out.println("插入:" + rs + "行");
                        preparedStatement.close();
                    } else {
                        System.out.println("excent2字段不符合规则：" + excent2);
                    }
                }
                s1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String initBatchDetailSql2(String tableName) {
        //0下标为上线时核对字段，1下标为.net字段，2下标为对账时表结构字段
        String[][] batchDetail = new String[][]{
                {"BAT_NO", "sd.batno", "BAT_NO"},
                {"ENT_NUM", "sd.enterprisenum", "ENT_NUM"},
                {"BD_NO", "sd.flowno", "BD_NO"},
                {"CUST_BANK_CODE", "sd.bankcode", "CUST_BANK_CODE"},
                {"ENT_ACC_NUM", "sd.payaccount", "ENT_ACC_NUM"},
                {"CARD_TYPE ", "sd.cardtype", "CARD_TYPE"},
                {"CUST_ACC_NUM", "sd.accountnum", "CUST_ACC_NUM"},
                {"CUST_ACC_NAME", "sd.accountname", "CUST_ACC_NAME"},
                {"CUST_AREA_CODE", "sd.areacode", "CUST_AREA_CODE"},
                {"CUST_BANK_LOCATION_NAME", "sd.banklocationname", "CUST_BANK_LOCATION_CODE"},
                {"CUST_BANK_LOCATION_CODE", "sd.banklocationcode", "CUST_BANK_LOCATION_NAME"},
                {"PRIVATE_FLAG", "sd.isprivate", "PRIVATE_FLAG"},
                {"AMOUNT", "sd.amount", "AMOUNT"},
                {"CURRENCY", "sd.currency", "CURRENCY"},
                {"PROTOCOL_USER_CODE", "sd.protocolnum", "PROTOCOL_USER_CODE"},
                {"PROTOCOL_CODE", "sd.protocolcode", "PROTOCOL_COCE"},
                {"CERT_TYPE", "sd.certtype", "CERT_TYPE"},
                {"CERT_NUM", "sd.certnumber", "CERT_NUM"},
                {"PHONE", "sd.phonenum", "PHONE"},
                {"PURPOSE", "sd.purpose", "PURPOSE"},
                {"RECONCILIATION_CODE", "sd.reconciliationcode", "RECONCILIATION_CODE"},
                {"FLOW_NO", "?", "FLOW_NO"},
                {"SINGLE_TRANS_VERIFY_CODE", "?", "SINGLE_TRANS_VERIFY_CODE"},
                {"SINGLE_QUERY_VERIFY_CODE", "?", "SINGLE_QUERY_VERIFY_CODE"},
                {"SAME_BANK_FLAG", "excent5", "SAME_BANK_FLAG"},
                {"BANK_RETURN_STATE", "sd.bankreturnstate", "BANK_RETURN_STATE"},
                {"BANK_RETURN_CODE", "sd.bankreturncode", "BANK_RETURN_CODE"},
                {"BANK_RETURN_MSG", "sd.bankreturnmsg", "BANK_RETURN_MSG"},
                {"MEMO", "sd.memo", "MEMO"},
                {"BANK_LOCATION_MATCH_FLAG", "sd.banklocationmatchflag", "BANK_LOCATION_MATCH_FLAG"},
                {"BANK_LOCATION_MATCH_NAME", "sd.banklocationmatchname", "BANK_LOCATION_MATCH_NAME"},
                {"URGENT_FLAG", "NVL(sd.isurgent,'0')", "URGENT_FLAG"},
                {"MONEY_WAY", "sb.PAYTYPE", "MONEY_WAY"},
                {"BANK_EXTEND_1", "sd.bankextend1", "BANK_EXTEND_1"},
                {"BANK_EXTEND_2", "sd.bankextend2", "BANK_EXTEND_2"},
                {"BANK_EXTEND_3", "sd.bankextend3", "BANK_EXTEND_3"},
                {"BANK_EXTEND_4", "sd.bankextend4", "BANK_EXTEND_4"},
                {"BANK_EXTEND_5", "sd.bankextend5", "BANK_EXTEND_5"},
                {"ACCOUNTING_DATE", "to_char(sb.INTIME,'yyyyMMdd')", "ACCOUNTING_DATE"},
                {"ENT_CHANNEL_CODE", "sb.BANKCODE"},
                {"FG_UNIQUE_ID", "sd.fguniqueid", "FG_UNIQUE_ID"},
                {"SOURCE_NOTE", "sd.sourcenote", "SOURCE_NOTE"},
                {"MATCH_BANK_ID", "sd.matchbankid", "MATCH_BANK_ID"},
                {"MATCH_AREA_CODE", "sd.matchareacode", "MATCH_AREA_CODE"},
                {"FAIL_ACCOUNTING_DATE", "sd.failaccountingdate", "FAIL_ACCOUNTING_DATE"},
                {"FAIL_BANK_DETAIL_RECONCILE", "sd.failbankdetailreconcile", "FAIL_BANK_DETAIL_RECONCILE"},
                {"BANK_DETAIL_RECONCILE", "sd.bankdetailreconcile", "BANK_DETAIL_RECONCILE"},
                {"RECONCILE_FILE_RECONCILE", "sd.reconcilefilereconcile", "RECONCILE_FILE_RECONCILE"},
                {"TOTAL_AMOUNT_RECONCILE", "sd.totalamountreconcile", "TOTAL_AMOUNT_RECONCILE"},
                {"CYCLE_DATE", "sd.cycledate", "CYCLE_DATE"},
                {"BATCH_TO_SINGLE_FLAG", "NVL(sd.issingletrans,0)", "BATCH_TO_SINGLE_FLAG"},
                {"ORG_CODE", "sd.orgcode", "ORG_CODE"},
                {"SUPPORT_CREDIT_FLAG", "sd.nonsupportcredit", "SUPPORT_CREDIT_FLAG"},
                {"UNION_PAY_CARD_TYPE", "to_char(sd.unionpaycardtype) unionpaycardtype", "UNION_PAY_CARD_TYPE"},
                {"ABS_TRACT", "sd.abstract", "ABS_TRACT"},
                {"FAIL_CYCLE_DATE", "sd.failcycledate", "FAIL_CYCLE_DATE"},
                {"FAIL_RECONCILE_TYPE", "to_char(sd.failreconciletype)", "FAIL_RECONCILE_TYPE"},
                {"SOURCE_URID", "sd.sourceurid", "SOURCE_URID"},
                {"WARN_REVOKED", "sd.warnrevoked", "WARN_REVOKED"},
                {"REPEAT_CONDITION_VALUE", "sd.repeatconditionvalue", "REPEAT_CONDITION_VALUE"},
                {"WARN_DATE", "sd.warndate", "WARN_DATE"},
                {"urid", "sys_guid() urid", "URID"},
                {"DETAIL_MD5", "null DETAIL_MD5", "DETAIL_MD5"},
                {"COMPLIANCE_STATE", "null COMPLIANCE_STATE", "COMPLIANCE_STATE"},
                {"COMPLIANCE_INFO", "null COMPLIANCE_INFO", "COMPLIANCE_INFO"},
                {"OVER_LENGTH", "'0' OVER_LENGTH", "OVER_LENGTH"},
                {"ROW_VERSION", "1 ROW_VERSION", "ROW_VERSION"},
                {"DECODE_AMOUNT", "null DECODE_AMOUNT", "DECODE_AMOUNT"},
                {"ACCOUNTING_FLAG", "'2' ACCOUNTING_FLAG", "ACCOUNTING_FLAG"},
                {"REQ_DATE", "to_date(sb.REQDATE,'yyyy-MM-dd HH24:mi:ss') REQ_DATE", "REQ_DATE"},
                {"BANK_RETURN_TIME", "null BANK_RETURN_TIME", "BANK_RETURN_TIME"},
                {"POLICY_NUM", "null PILICY_NUM", "POLICY_NUM"},
                {"CUST_AREA_NAME", "null CUST_AREA_NAME", "CUST_AREA_NAME"},
                {"ENCRYPT_FLAG", "sb.ENCRYPTFLAG ENCRYPT_FLAG", "ENCRYPE_FLAG"},
                {"CONVERTED_BD_NO", "null CONVERTED_BD_NO", "CONVERTED_BD_NO"},
                {"RISK_CONTROL_STATE", "'1' RISK_CONTROL_STATE", "RISK_CONTROL_STATE"},
                {"RISK_CONTROL_HIT_TYPE", "null RISK_CONTROL_HIT_TYPE", "RISK_CONTROL_HIT_TYPE"},
                {"IN_TIME", "sb.INTIME IN_TIME", "IN_TIME"},
                {"SEND_BANK_TIME", "null SEND_BANK_TIME", "SEND_BANK_TIME"},
                {"LAST_QUERY_TIME", "sb.INTIME", "LAST_QUERY_TIME"},
                {"LAST_MODIFY_TIME", "sb.INTIME", "LAST_MODIFY_TIME"}
        };

        String twoTable = "SE_BD";
        String oneTable = tableName + "@linkAC110005 sd,SE_BATCH@linkAC110005 sb";
        String condition = "sd.batno = sb.BATNO and sd.excent2 = ?";

        List<String> twoFileds = Arrays.stream(batchDetail).map(x -> x[0]).collect(Collectors.toList());
        List<String> oneFileds = Arrays.stream(batchDetail).map(x -> x[1]).collect(Collectors.toList());
        String twoSql = String.join(",", twoFileds);
        String oneSql = String.join(",", oneFileds);

        String allSql = "insert into " + twoTable + " (" + twoSql + ") select " +
                oneSql + " from " + oneTable + " where 1=1 and " + condition;
        return allSql;
    }

    private static String initBatchDetailSql1(String tableName) {
        //0下标为上线时核对字段，1下标为.net字段，2下标为对账时表结构字段
        String[][] batchDetail = new String[][]{
                {"BAT_NO", "sd.batno", "BAT_NO"},
                {"ENT_NUM", "sd.enterprisenum", "ENT_NUM"},
                {"BD_NO", "sd.flowno", "BD_NO"},
                {"CUST_BANK_CODE", "sd.bankcode", "CUST_BANK_CODE"},
                {"ENT_ACC_NUM", "sd.payaccount", "ENT_ACC_NUM"},
                {"CARD_TYPE ", "sd.cardtype", "CARD_TYPE"},
                {"CUST_ACC_NUM", "sd.accountnum", "CUST_ACC_NUM"},
                {"CUST_ACC_NAME", "sd.accountname", "CUST_ACC_NAME"},
                {"CUST_AREA_CODE", "sd.areacode", "CUST_AREA_CODE"},
                {"CUST_BANK_LOCATION_NAME", "sd.banklocationname", "CUST_BANK_LOCATION_CODE"},
                {"CUST_BANK_LOCATION_CODE", "sd.banklocationcode", "CUST_BANK_LOCATION_NAME"},
                {"PRIVATE_FLAG", "sd.isprivate", "PRIVATE_FLAG"},
                {"AMOUNT", "sd.amount", "AMOUNT"},
                {"CURRENCY", "sd.currency", "CURRENCY"},
                {"PROTOCOL_USER_CODE", "sd.protocolnum", "PROTOCOL_USER_CODE"},
                {"PROTOCOL_CODE", "sd.protocolcode", "PROTOCOL_COCE"},
                {"CERT_TYPE", "sd.certtype", "CERT_TYPE"},
                {"CERT_NUM", "sd.certnumber", "CERT_NUM"},
                {"PHONE", "sd.phonenum", "PHONE"},
                {"PURPOSE", "sd.purpose", "PURPOSE"},
                {"RECONCILIATION_CODE", "sd.reconciliationcode", "RECONCILIATION_CODE"},
                {"FLOW_NO", "?", "FLOW_NO"},
                {"SINGLE_TRANS_VERIFY_CODE", "null", "SINGLE_TRANS_VERIFY_CODE"},
                {"SINGLE_QUERY_VERIFY_CODE", "null", "SINGLE_QUERY_VERIFY_CODE"},
                {"SAME_BANK_FLAG", "excent5", "SAME_BANK_FLAG"},
                {"BANK_RETURN_STATE", "sd.bankreturnstate", "BANK_RETURN_STATE"},
                {"BANK_RETURN_CODE", "sd.bankreturncode", "BANK_RETURN_CODE"},
                {"BANK_RETURN_MSG", "sd.bankreturnmsg", "BANK_RETURN_MSG"},
                {"MEMO", "sd.memo", "MEMO"},
                {"BANK_LOCATION_MATCH_FLAG", "sd.banklocationmatchflag", "BANK_LOCATION_MATCH_FLAG"},
                {"BANK_LOCATION_MATCH_NAME", "sd.banklocationmatchname", "BANK_LOCATION_MATCH_NAME"},
                {"URGENT_FLAG", "NVL(sd.isurgent,'0')", "URGENT_FLAG"},
                {"MONEY_WAY", "sb.PAYTYPE", "MONEY_WAY"},
                {"BANK_EXTEND_1", "sd.bankextend1", "BANK_EXTEND_1"},
                {"BANK_EXTEND_2", "sd.bankextend2", "BANK_EXTEND_2"},
                {"BANK_EXTEND_3", "sd.bankextend3", "BANK_EXTEND_3"},
                {"BANK_EXTEND_4", "sd.bankextend4", "BANK_EXTEND_4"},
                {"BANK_EXTEND_5", "sd.bankextend5", "BANK_EXTEND_5"},
                {"ACCOUNTING_DATE", "sd.accountingdate", "ACCOUNTING_DATE"},
                {"ENT_CHANNEL_CODE", "(select t.channel_id from TBIZOPS_ACCOUNT@gardpay_bizops t where t.account_num = " +
                        "sd.payaccount and t.tenant_id = 'AS330106') ENT_CHANNEL_CODE", "ENT_CHANNEL_CODE"},
                {"FG_UNIQUE_ID", "sd.fguniqueid", "FG_UNIQUE_ID"},
                {"SOURCE_NOTE", "sd.sourcenote", "SOURCE_NOTE"},
                {"MATCH_BANK_ID", "sd.matchbankid", "MATCH_BANK_ID"},
                {"MATCH_AREA_CODE", "sd.matchareacode", "MATCH_AREA_CODE"},
                {"FAIL_ACCOUNTING_DATE", "sd.failaccountingdate", "FAIL_ACCOUNTING_DATE"},
                {"FAIL_BANK_DETAIL_RECONCILE", "sd.failbankdetailreconcile", "FAIL_BANK_DETAIL_RECONCILE"},
                {"BANK_DETAIL_RECONCILE", "sd.bankdetailreconcile", "BANK_DETAIL_RECONCILE"},
                {"RECONCILE_FILE_RECONCILE", "sd.reconcilefilereconcile", "RECONCILE_FILE_RECONCILE"},
                {"TOTAL_AMOUNT_RECONCILE", "sd.totalamountreconcile", "TOTAL_AMOUNT_RECONCILE"},
                {"CYCLE_DATE", "sd.cycledate", "CYCLE_DATE"},
                {"BATCH_TO_SINGLE_FLAG", "NVL(sd.issingletrans,0)", "BATCH_TO_SINGLE_FLAG"},
                {"ORG_CODE", "sd.orgcode", "ORG_CODE"},
                {"SUPPORT_CREDIT_FLAG", "sd.nonsupportcredit", "SUPPORT_CREDIT_FLAG"},
                {"UNION_PAY_CARD_TYPE", "to_char(sd.unionpaycardtype) unionpaycardtype", "UNION_PAY_CARD_TYPE"},
                {"ABS_TRACT", "sd.abstract", "ABS_TRACT"},
                {"FAIL_CYCLE_DATE", "sd.failcycledate", "FAIL_CYCLE_DATE"},
                {"FAIL_RECONCILE_TYPE", "to_char(sd.failreconciletype)", "FAIL_RECONCILE_TYPE"},
                {"SOURCE_URID", "sd.sourceurid", "SOURCE_URID"},
                {"WARN_REVOKED", "sd.warnrevoked", "WARN_REVOKED"},
                {"REPEAT_CONDITION_VALUE", "sd.repeatconditionvalue", "REPEAT_CONDITION_VALUE"},
                {"WARN_DATE", "sd.warndate", "WARN_DATE"},
                {"urid", "sys_guid() urid", "URID"},
                {"DETAIL_MD5", "null DETAIL_MD5", "DETAIL_MD5"},
                {"COMPLIANCE_STATE", "null COMPLIANCE_STATE", "COMPLIANCE_STATE"},
                {"COMPLIANCE_INFO", "null COMPLIANCE_INFO", "COMPLIANCE_INFO"},
                {"OVER_LENGTH", "'0' OVER_LENGTH", "OVER_LENGTH"},
                {"ROW_VERSION", "1 ROW_VERSION", "ROW_VERSION"},
                {"DECODE_AMOUNT", "null DECODE_AMOUNT", "DECODE_AMOUNT"},
                {"ACCOUNTING_FLAG", "'2' ACCOUNTING_FLAG", "ACCOUNTING_FLAG"},
                {"REQ_DATE", "to_date(sb.REQDATE,'yyyy-MM-dd HH24:mi:ss') REQ_DATE", "REQ_DATE"},
                {"BANK_RETURN_TIME", "null BANK_RETURN_TIME", "BANK_RETURN_TIME"},
                {"POLICY_NUM", "null PILICY_NUM", "POLICY_NUM"},
                {"CUST_AREA_NAME", "null CUST_AREA_NAME", "CUST_AREA_NAME"},
                {"ENCRYPT_FLAG", "sb.ENCRYPTFLAG ENCRYPT_FLAG", "ENCRYPE_FLAG"},
                {"CONVERTED_BD_NO", "null CONVERTED_BD_NO", "CONVERTED_BD_NO"},
                {"RISK_CONTROL_STATE", "'1' RISK_CONTROL_STATE", "RISK_CONTROL_STATE"},
                {"RISK_CONTROL_HIT_TYPE", "null RISK_CONTROL_HIT_TYPE", "RISK_CONTROL_HIT_TYPE"},
                {"IN_TIME", "sb.INTIME IN_TIME", "IN_TIME"},
                {"SEND_BANK_TIME", "null SEND_BANK_TIME", "SEND_BANK_TIME"},
                {"LAST_QUERY_TIME", "sb.REQDATE", "LAST_QUERY_TIME"},
                {"LAST_MODIFY_TIME", "sb.REQDATE", "LAST_MODIFY_TIME"}
        };

        String twoTable = "SE_BD";
        String oneTable = tableName + "@linkAC110005 sd,SE_BATCH@linkAC110005 sb";
        String condition = "sd.batno = sb.BATNO and sd.excent2 = ?";

        List<String> twoFileds = Arrays.stream(batchDetail).map(x -> x[0]).collect(Collectors.toList());
        List<String> oneFileds = Arrays.stream(batchDetail).map(x -> x[1]).collect(Collectors.toList());
        String twoSql = String.join(",", twoFileds);
        String oneSql = String.join(",", oneFileds);

        String allSql = "insert into " + twoTable + " (" + twoSql + ") select " +
                oneSql + " from " + oneTable + " where 1=1 and " + condition;
        return allSql;
    }

    private static String initSingle() {
        String[][] single = new String[][]{
                {"URID", "sys_guid()"},
                {"ENT_NUM", "enterprisenum"},
                {"TRANS_NO", "transno"},
                {"SOURCE_TRANS_NO", "sourcetransno"},
                {"FG_COMMAND_CODE", "commandcode"},
                {"ENT_CHANNEL_CODE", "CORPBANKCODE"},
                {"MONEY_WAY", "detailtranstype"},
                {"SOURCE_MONEY_WAY", "MONEYWAY"},
                {"TRANS_DATE", "SAVETIME"},
                {"ACCOUNTING_FLAG", "ACCOUNTINGFLAG"},
                {"ENT_ACC_NUM", "ENTERPRISEACCNUM"},
                {"CUST_BANK_CODE", "CUSTBANKCODE"},
                {"MATCHED_CUST_BANK_CODE", "CUSTBANKCODE"},
                {"CUST_ACC_NUM", "CUSTACCNUM"},
                {"CUST_ACC_NAME", "CUSTACCNAME"},
                {"AREA_CODE", "AREACODE"},
                {"BANK_LOCATION_CODE", "BANKLOCATIONCODE"},
                {"BANK_LOCATION_NAME", "BANKLOCATIONNAME"},
                {"CARD_TYPE", "CARDTYPE"},
                {"PRIVATE_FLAG", "ISPRIVATE"},
                {"URGENT_FLAG", "ISURGENT"},
                {"AMOUNT", "AMOUNT"},
                {"CURRENCY", "CURRENCY"},
                {"CERT_TYPE", "CERTTYPE"},
                {"CERT_NUM", "CERTNUM"},
                {"PHONE", "MOBILE"},
                {"PURPOSE", "PURPOSE"},
                {"MEMO", "MEMO"},
                {"RECONCILIATION_CODE", "RECONCILIATIONCODE"},
                {"POLICY_NUMBER", "'unknown'"},
                {"EXTEND1", "EXTENT1"},
                {"EXTEND2", "EXTENT2"},
                {"VERIFY_CODE", "VERIFYCODE"},
                {"FG_VERIFY_CODE", "'unknown'"},
                {"TO_BATCH_VERIFY_CODE", "'unknown'"},
                {"PROTOCOL_CODE", "'unknown'"},
                {"ORG_CODE", "ORGCODE"},
                {"IN_TIME", "SAVETIME"},
                {"FALL_DOWN_STATUS", "'0'"},
                {"FALL_DOWN_REASON", "''"},
                {"REPEAT_CHECK_FLAG", "'1'"},
                {"SINGLE_TO_BATCH_FLAG", "SINGLETOBATCHFLAG"},
                {"SEND_BANK_TIME", "SAVETIME"},
                {"BANK_RETURN_TIME", "SAVETIME"},
                {"FORCE_RETURN_FLAG", "'0'"},
                {"ENCRYPT_FLAG", "TRANSKEYENCRYPTFLAG"},
                {"BANK_RETURN_STATE", "STATE"},
                {"BANK_RETURN_CODE", "INFOCODE"},
                {"BANK_RETURN_MSG", "INFO"},
                {"QUERY_RESULT", "''"},
                {"ROW_VERSION", "1"},
                {"LAST_MODIFY_USER", "'ADMIN'"},
                {"LAST_MODIFY_TIME", "SAVETIME"},
                {"COMMAND_CODE", "COMMANDCODE"},
                {"FALL_DOWN_TIME", "null"},
                {"SYNC_TIME", "null"},
                {"CHANNEL_SYSTEM_ID", "null"},
                {"RESP_MSG", "''"},
                {"ACCOUNTING_DATE", "to_char(SAVETIME,'YYYYMMDD')"}
        };

        //二代表名
        String twoTable = "SE_SINGLE";

        //一代表名
        String oneTable = "se_singletrans@linkas110014";

        //查询条件


        String[] condition = new String[]{
                "savetime >=TO_DATE('2019-07-07 00:00:00','YYYY-MM-dd HH24:MI:SS')",
                "savetime <TO_DATE('2019-07-08 00:00:00','YYYY-MM-dd HH24:MI:SS')"
        };
        List<String[]> list = Arrays.asList(single);
        List<String> twoFields = list.stream().map(x -> x[0]).collect(Collectors.toList());
        List<String> oneFields = list.stream().map(x -> x[1]).collect(Collectors.toList());
        String twoSql = String.join(",", twoFields);
        String oneSql = String.join(",", oneFields);
        String whereSql = Arrays.stream(condition).map(x -> " AND " + x).collect(Collectors.joining());

        String allSql = "insert into " + twoTable + " (" + twoSql + ") " +
                "select " + oneSql + " from " + oneTable + " where 1 = 1 " + whereSql;
        return allSql;
    }

    private static String initBatch() {
        //0下标为上线时核对字段，1下标为.net字段，2下标为对账时表结构字段
        String[][] batch = new String[][]{
                {"ENT_NUM", "ENTERPRISENUM", "ent_num"},
                {"BAT_NO", "BATNO", "bat_no"},
                {"REQ_DATE", "to_date(REQDATE, 'yyyy-MM-dd HH24:mi:ss') REQDATE", "req_date"},
                {"IN_TIME", "INTIME", "in_time"},
                {"OUT_TIME", "OUTTIME", "out_time"},
                {"SEND_BANK_TIME", "SENDBANKTIME", "send_bank_time"},
                {"BANK_RETURN_TIME", "BANKRETURNTIME", "bank_return_time"},
                {"FG_RETURN_TIME", "SENDENTITYTIME", "fg_return_time"},
                {"DEAL_STATE", "'Z01'", "deal_state"},
                {"ENT_CHANNEL_CODE", "BANKCODE", "ent_channel_code"},
                {"TOTAL_AMOUNT", "TOTALAMOUNT", "total_amount"},
                {"TOTAL_NUM", "TOTALNUM", "total_num"},
                {"VERIFY_CODE", "'test' VERIFYCODE", "verify_code"},
                {"BANK_VERIFY_CODE", "BANKVERIFYCODE", "bank_verify_code"},
                {"SUCCESS_AMOUNT", "SUCCESSAMOUNT", "success_num"},
                {"SUCCESS_NUM", "SUCCESSNUM", "success_amount"},
                {"FAIL_NUM", "FAILNUM", "fail_num"},
                {"BANK_LOCATION_MATCH_STATE", "BANKLOCATIONCODE", "bank_location_match_state"},
                {"RECONCILIATION_CODE", "RECONCILIATIONCODE", "reconciliation_code"},
                {"DETAIL_MD5", "DETAILMD5", "detail_md5"},
                {"AVG_AMOUNT", "AVGAMOUNT", "avg_amount"},
                {"MIN_AMOUNT", "MINAMOUNT", "min_amount"},
                {"MAX_AMOUNT", "MAXAMOUNT", "max_amount"},
                {"SEND_TO_BANK_FILE_PATH", "SEND2BANKFILEPATH", "send_to_bank_file_path"},
                {"ACCOUNTING_FLAG", "ACCOUNTINGFLAG", "accounting_flag"},
                {"ENCRYPT_FLAG", "ENCRYPTFLAG", "encrypt_flag"},
                {"SOURCE_DATA_MD5", "SOURCEDATAMD5", "source_date_md5"},
                {"SOURCE_TOTAL_AMOUNT", "SOURCETOTALAMOUNT", "source_total_amount"},
                {"MONEY_WAY", "PAYTYPE", "money_way"},
                {"ENT_ACC_NUM", "ENTERPRISEACCNUM", "ent_acc_num"},
                {"REPEAT_CONDITION", "REPEATCONDITION", "repeat_condition"},
                {"URID", "sys_guid()", "urid"},
                {"FAIL_AMOUNT", "null", "fail_amount"},
                {"FILE_NAME", "null", "file_name"},
                {"REQUEST_TYPE", "'1'", "request_type"},
                {"INSIDE_STATE", "'IN03'", "inside_state"},
                {"INSIDE_STATE_INFO", "null", "inside_state_info"},
                {"OUTSIDE_STATE", "'OUT02'", "outside_state"},
                {"ALL_FAIL_CODE", "null", "all_fail_code"},
                {"ALL_FAIL_INFO", "null", "all_fail_info"},
                {"BATCH_TO_SINGLE_FLAG", "''", "batch_to_single_flag"},
                {"ROW_VERSION", "1", "row_version"},
                {"MD5", "null", "md5"},
                {"INTERFACE_TYPE", "'1'", "interface_type"},
                {"VERSION", "120", "version"},
                {"LAST_MODIFY_USER", "null", "last_modify_user"},
                {"LAST_MODIFY_TIME", "BANKRETURNTIME", "last_modify_time"},
        };

        //二代表名
        String twoTable = "SE_BATCH";

        //一代表名
        String oneTable = "SE_BATCH@linkAC110005";

        //table_name  >= 'SE_D20190704000003' and table_name <='SE_D20190705000003'
        //查询条件
        String[] condition = new String[]{
                "TOTALAMOUNT IS NOT NULL",
                "SENDBANKTIME IS NOT NULL",
                "BATID >= '20190704000004'",
                "BATID <= '20190705000004'",
                "BATID LIKE '2019%'",
                "DEALSTATE = 81"
        };

        List<String[]> list = Arrays.asList(batch);
        List<String> twoFields = list.stream().map(x -> x[0]).collect(Collectors.toList());
        List<String> oneFields = list.stream().map(x -> x[1]).collect(Collectors.toList());
        String twoSql = String.join(",", twoFields);
        String oneSql = String.join(",", oneFields);
        String whereSql = Arrays.stream(condition).map(x -> " AND " + x).collect(Collectors.joining());

        //需要执行的sql
        String allSql = "insert into " + twoTable + " (" + twoSql + ") " +
                "select " + oneSql + " from " + oneTable + " where 1 = 1 " + whereSql;

        System.out.println("需要执行的sql为：" + allSql);
        return allSql;
    }
}
