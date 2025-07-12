package com.hk.utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.regex.Pattern;

/**
 * @author huangkun
 * @date 2025/7/6 10:39
 */
@Component
public class IPUtil {
    private static DatabaseReader databaseReader;
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
    );
    // 初始化GeoIP2数据库读取器
    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("geoip/GeoLite2-City-Test.mmdb");
        try (InputStream inputStream = resource.getInputStream()) {
            databaseReader = new DatabaseReader.Builder(inputStream).build();
        }
    }

    /**
     * 获取客户端IP（处理代理情况）
     */
    public static String getClientIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        String ip = null;
        // 按优先级检查所有可能的代理头
        for (String header : headers) {
            ip = request.getHeader(header);
            if (isValidIpv4(ip)) break;
        }

        // 如果头信息未找到有效IP，使用远程地址
        if (!isValidIpv4(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理多IP情况（取第一个有效IP）
        if (ip != null && ip.contains(",")) {
            for (String candidate : ip.split("\\s*,\\s*")) {
                if (isValidIpv4(candidate)) {
                    ip = candidate;
                    break;
                }
            }
        }

        // 最终验证并返回
        return isValidIpv4(ip) ? ip : "127.0.0.1";
    }

    /**
     * 验证是否为有效IPv4地址
     * 1. 检查非空
     * 2. 匹配IPv4正则
     * 3. 排除本地回环地址
     */
    private static boolean isValidIpv4(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        ip = ip.trim();

        // 验证基本格式
        if (!IPV4_PATTERN.matcher(ip).matches()) {
            return false;
        }

        // 排除本地回环地址
        return !ip.startsWith("127.") &&
                !ip.startsWith("0.") &&
                !ip.startsWith("255.") &&
                !ip.equals("0.0.0.0");
    }
    /**
     * 获取详细设备信息
     */
    public static DeviceInfo getDeviceInfo(HttpServletRequest request) {
        String userAgentStr = request.getHeader("User-Agent");
        if (userAgentStr == null) {
            return new DeviceInfo("Unknown", "Unknown", "Unknown");
        }

        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
        return new DeviceInfo(
                userAgent.getOperatingSystem().getDeviceType().getName(),
                userAgent.getOperatingSystem().getName(),
                userAgent.getBrowser().getName()
        );
    }

    /**
     * 根据IP获取地理位置
     */
    public static LocationInfo getLocationByIp(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = databaseReader.city(ipAddress);

            return new LocationInfo(
                    response.getCountry().getName(),
                    response.getMostSpecificSubdivision().getName(),
                    response.getCity().getName(),
                    response.getLocation().getLatitude(),
                    response.getLocation().getLongitude()
            );
        } catch (IOException | GeoIp2Exception e) {
            return new LocationInfo("Unknown", "Unknown", "Unknown", null, null);
        } catch (NullPointerException e) {
            // 处理本地IP等特殊情况
            return new LocationInfo("Local Network", "Local Network", "Local Network", null, null);
        }
    }

    // 设备信息DTO
    public record DeviceInfo(String deviceType, String os, String browser) {}

    // 位置信息DTO
    public record LocationInfo(
            String country,
            String region,
            String city,
            Double latitude,
            Double longitude
    ) {}

}
