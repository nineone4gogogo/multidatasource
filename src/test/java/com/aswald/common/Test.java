package com.aswald.common;

import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Map;

/**
 * @Author Ethan
 * @Date 2019-08-16 11:25
 * @Description
 **/
public class Test {

    @org.junit.Test
    public void testList(){
        String json="{\"txid\":\"56402189f42cb1477c61ed9193c5eada9b6b63d03ffbfec9537f9203b02666fa\",\"hash\":\"56402189f42cb1477c61ed9193c5eada9b6b63d03ffbfec9537f9203b02666fa\",\"version\":1,\"size\":226,\"vsize\":226,\"locktime\":0,\"vin\":[{\"txid\":\"4d5be0adacbb7f1735725e0eff0aaa336c60c9212466430b54ca7ad0842d3f97\",\"vout\":1,\"scriptSig\":{\"asm\":\"3045022100c47a7d8876507f6311ee8cc83bfb940d21aba5ba525a3a09dee624819e3f532502204ff484b3a24e05767682d3fb9639d7784684e97e17c4ed77d5a8aa7bb4364efa[ALL] 038a5a00c17768dcf06ab37f73ddb17695cfac3617425c84d8fd15e4efc1ebefcf\",\"hex\":\"483045022100c47a7d8876507f6311ee8cc83bfb940d21aba5ba525a3a09dee624819e3f532502204ff484b3a24e05767682d3fb9639d7784684e97e17c4ed77d5a8aa7bb4364efa0121038a5a00c17768dcf06ab37f73ddb17695cfac3617425c84d8fd15e4efc1ebefcf\"},\"sequence\":4294967295},{\"txid\":\"4d5be0adacbb7f1735725e0eff0aaa336c60c9212466430b54ca7ad0842d3f97\",\"vout\":1,\"scriptSig\":{\"asm\":\"3045022100c47a7d8876507f6311ee8cc83bfb940d21aba5ba525a3a09dee624819e3f532502204ff484b3a24e05767682d3fb9639d7784684e97e17c4ed77d5a8aa7bb4364efa[ALL] 038a5a00c17768dcf06ab37f73ddb17695cfac3617425c84d8fd15e4efc1ebefcf\",\"hex\":\"483045022100c47a7d8876507f6311ee8cc83bfb940d21aba5ba525a3a09dee624819e3f532502204ff484b3a24e05767682d3fb9639d7784684e97e17c4ed77d5a8aa7bb4364efa0121038a5a00c17768dcf06ab37f73ddb17695cfac3617425c84d8fd15e4efc1ebefcf\"},\"sequence\":4294967295}],\"vout\":[{\"value\":0.00200000,\"n\":0,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 8c43e9cea42ec3e1371c6855f21cc0fa7b54db7d OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a9148c43e9cea42ec3e1371c6855f21cc0fa7b54db7d88ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"1DnexZERxekUubVy7G9eNBbh1os1vh7oJ1\"]}},{\"value\":0.53825426,\"n\":1,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 2396e7eb6066b39965d303b3d3ff114e326136d2 OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a9142396e7eb6066b39965d303b3d3ff114e326136d288ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"14FBRqrPMWKfrTWF9SqiC4SexZcdJKuRSy\"]}}],\"hex\":\"0100000001973f2d84d07aca540b43662421c9606c33aa0aff0e5e7235177fbbacade05b4d010000006b483045022100c47a7d8876507f6311ee8cc83bfb940d21aba5ba525a3a09dee624819e3f532502204ff484b3a24e05767682d3fb9639d7784684e97e17c4ed77d5a8aa7bb4364efa0121038a5a00c17768dcf06ab37f73ddb17695cfac3617425c84d8fd15e4efc1ebefcfffffffff02400d0300000000001976a9148c43e9cea42ec3e1371c6855f21cc0fa7b54db7d88ac924f3503000000001976a9142396e7eb6066b39965d303b3d3ff114e326136d288ac00000000\",\"blockhash\":\"0000000000000000001726486775e189e54038299487e689a2c1b37ab1f3613e\",\"confirmations\":1,\"time\":1565925333,\"blocktime\":1565925333}";
        List<Map<String,Object>> read = JsonPath.read(json, "$.vin[*]");//[?(@.isbn)]
        for (Map<String, Object> stringObjectMap : read) {
            Object txid = stringObjectMap.get("txid");
            Object vout = stringObjectMap.get("vout");
            System.out.println(txid);
            System.out.println(vout);
        }
//        System.out.println(read);
    }
    @org.junit.Test
    public void testList1(){
        String json="{\"txid\":\"56402189f42cb1477c61ed9193c5eada9b6b63d03ffbfec9537f9203b02666fa\",\"hash\":\"56402189f42cb1477c61ed9193c5eada9b6b63d03ffbfec9537f9203b02666fa\",\"version\":1,\"size\":226,\"vsize\":226,\"locktime\":0,\"vin\":[{\"txid\":\"4d5be0adacbb7f1735725e0eff0aaa336c60c9212466430b54ca7ad0842d3f97\",\"vout\":1,\"scriptSig\":{\"asm\":\"3045022100c47a7d8876507f6311ee8cc83bfb940d21aba5ba525a3a09dee624819e3f532502204ff484b3a24e05767682d3fb9639d7784684e97e17c4ed77d5a8aa7bb4364efa[ALL] 038a5a00c17768dcf06ab37f73ddb17695cfac3617425c84d8fd15e4efc1ebefcf\",\"hex\":\"483045022100c47a7d8876507f6311ee8cc83bfb940d21aba5ba525a3a09dee624819e3f532502204ff484b3a24e05767682d3fb9639d7784684e97e17c4ed77d5a8aa7bb4364efa0121038a5a00c17768dcf06ab37f73ddb17695cfac3617425c84d8fd15e4efc1ebefcf\"},\"sequence\":4294967295},{\"txid\":\"4d5be0adacbb7f1735725e0eff0aaa336c60c9212466430b54ca7ad0842d3f97\",\"vout\":1,\"scriptSig\":{\"asm\":\"3045022100c47a7d8876507f6311ee8cc83bfb940d21aba5ba525a3a09dee624819e3f532502204ff484b3a24e05767682d3fb9639d7784684e97e17c4ed77d5a8aa7bb4364efa[ALL] 038a5a00c17768dcf06ab37f73ddb17695cfac3617425c84d8fd15e4efc1ebefcf\",\"hex\":\"483045022100c47a7d8876507f6311ee8cc83bfb940d21aba5ba525a3a09dee624819e3f532502204ff484b3a24e05767682d3fb9639d7784684e97e17c4ed77d5a8aa7bb4364efa0121038a5a00c17768dcf06ab37f73ddb17695cfac3617425c84d8fd15e4efc1ebefcf\"},\"sequence\":4294967295}],\"vout\":[{\"value\":0.00200000,\"n\":0,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 8c43e9cea42ec3e1371c6855f21cc0fa7b54db7d OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a9148c43e9cea42ec3e1371c6855f21cc0fa7b54db7d88ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"1DnexZERxekUubVy7G9eNBbh1os1vh7oJ1\"]}},{\"value\":0.53825426,\"n\":1,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 2396e7eb6066b39965d303b3d3ff114e326136d2 OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a9142396e7eb6066b39965d303b3d3ff114e326136d288ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"14FBRqrPMWKfrTWF9SqiC4SexZcdJKuRSy\"]}}],\"hex\":\"0100000001973f2d84d07aca540b43662421c9606c33aa0aff0e5e7235177fbbacade05b4d010000006b483045022100c47a7d8876507f6311ee8cc83bfb940d21aba5ba525a3a09dee624819e3f532502204ff484b3a24e05767682d3fb9639d7784684e97e17c4ed77d5a8aa7bb4364efa0121038a5a00c17768dcf06ab37f73ddb17695cfac3617425c84d8fd15e4efc1ebefcfffffffff02400d0300000000001976a9148c43e9cea42ec3e1371c6855f21cc0fa7b54db7d88ac924f3503000000001976a9142396e7eb6066b39965d303b3d3ff114e326136d288ac00000000\",\"blockhash\":\"0000000000000000001726486775e189e54038299487e689a2c1b37ab1f3613e\",\"confirmations\":1,\"time\":1565925333,\"blocktime\":1565925333}";

        List<Map<String,Object>> read = (List<Map<String, Object>>) JSONPath.read(json, "$.vin[*]");//[?(@.isbn)]
        for (Map<String, Object> stringObjectMap : read) {
            Object txid = stringObjectMap.get("txid");
            Object vout = stringObjectMap.get("vout");
            System.out.println(txid);
            System.out.println(vout);
        }
//        System.out.println(read);
    }
}
