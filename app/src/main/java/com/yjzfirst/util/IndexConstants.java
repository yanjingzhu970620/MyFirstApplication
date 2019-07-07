package com.yjzfirst.util;

/**
 * Created by manyships on 2018/1/15.
 */

public class IndexConstants {
    public static String ip_key="ip";
    public static String port_key="port";
    public static String db_key="db_key";
    public static String token_key="token";
    public static String email_key = "email";
    public static String password_key = "password";
    public static String rights_key="rights";
    public static final String LOGINURL="/app/login";
    public static final String TAKINGCHECKBARCODE="/api/taking/checkbarcode";

    public static final String CHECKDELIVERYFORM="/app/sale_delivery/get";
    public static final String CHECKDELIVERYPRODUCT="/app/sale_delivery/check_product";
    public static final String CHECKDELIVERYINVENTORY="/app/stock_inventory/code/get";
	public static final String CHECKDELIVERYPRODUCTLABEL="/app/product_label/get";
    public static final String CHECKDELIVERYWAREHOUSE="/app/sale_delivery/done";
    public static final String CHECKLIBRARY="/app/stock_location/get";


    public static final String CHECKCARDID="/app/mrp_runcard/get";
    public static final String REPORTCARD="/app/mrp_runcard/report";
    public static final String REPORTCARD_CANCLE="/app/mrp_runcard/report_cancel";
    public static final String REPORT_INSPECTPASS="/app/mrp_runcard/inspect_pass";
    public static final String REPORT_INSPECTCANCLE="/app/mrp_runcard/inspect_cancel";
    public static final String REPORT_INSPECTNG="/app/mrp_runcard/inspect_ng";
    public static final String REPORTMATERIAL="/app/mrp_runcard/material";
    public static final String REPORT_MATERIALCANCLE="/app/mrp_runcard/material_cancel";


    public static final String CHECKORDERID="/app/mrp_workorder/get";
    public static final String CHECKENTRYPRODUCTID="/app/product_packaging_together/get";

    public static final String INSTOCKCHECKBARCODE="/api/instock/checkbarcode ";
    public static final String OUTSTOCKCHECKBARCODE="/api/outstock/checkbarcode ";


    public static final String TAKINGSTOCK="/api/taking/stock_taking ";
    public static final String INSTOCK="/api/instock/product_instock  ";
    public static final String OUTSTOCK="/app/sale_delivery/add_op";
}
