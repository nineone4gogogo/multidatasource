package com.aswald.common;

/**
 * @Author Ethan
 * @Date 2019-08-08 16:45
 **/
public enum DataSourceNames {
    vipmain("vip_main"),
    vipfinancial("vip_financial");

    String dbname;

    DataSourceNames(String dbname) {
        this.dbname=dbname;
    }
}
