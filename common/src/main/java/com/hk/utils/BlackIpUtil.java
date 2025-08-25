package com.hk.utils;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.core.collection.CollectionUtil;

import java.util.Collection;

/**
 * @author huangkun
 * @date 2025/8/25 13:51
 */
public class BlackIpUtil {

    private static BitMapBloomFilter bitMapBloomFilter;

    /**
     * 判断IP是否为黑名单
     *
     * @param ip
     * @return
     */
    public static boolean isBlackIp(String ip) {
        return bitMapBloomFilter.contains(ip);
    }

    /**
     * 添加黑名单IP
     *
     * @param ip
     */
    public static void addBlackIp(String ip) {
        bitMapBloomFilter.add(ip);
    }

    public static void initBackIp(Collection<String> ips) {
        synchronized (BlackIpUtil.class) {
            if (bitMapBloomFilter == null) {
                //采用官方默认的过滤器长度
                bitMapBloomFilter = new BitMapBloomFilter(5);
            }
            if (CollectionUtil.isNotEmpty(ips)) {
                for (String ip : ips) {
                    bitMapBloomFilter.add(ip);
                }
            }
        }
    }

    public static void main(String[] args) {
        initBackIp(CollectionUtil.newArrayList("127.0.0.1"));
        System.out.println(isBlackIp("127.0.0.1"));
    }


}
