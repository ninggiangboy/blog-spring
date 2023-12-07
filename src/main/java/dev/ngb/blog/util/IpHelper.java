package dev.ngb.blog.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import dev.ngb.blog.exception.FileException;
import dev.ngb.blog.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@NoArgsConstructor(access = AccessLevel.NONE)
public class IpHelper {
    private static final File database = new File("./geo/GeoLite2-City.mmdb");
    private static final DatabaseReader dbReader;

    static {
        try {
            dbReader = new DatabaseReader.Builder(database).build();
        } catch (IOException e) {
            throw new FileException("GeoLite2-City.mmdb not found");
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String forwarded = request.getHeader("X-FORWARDED-FOR");
        if (forwarded != null) {
            return "Invalid IP";
        }
        return request.getRemoteAddr();
    }

    public static String getRealCityAddress(String ip) {
        try {
            CityResponse city = dbReader.city(InetAddress.getByName(ip));
            return city.getCity().getName() + ", " + city.getCountry().getName();
        } catch (IOException | GeoIp2Exception e) {
            throw new NotFoundException("Address not found for IP: " + ip);
        }
    }

    public static boolean isSameLocation(String newIp, String oldIp) {
        if (newIp.equals(oldIp)) return true;
        try {
            CityResponse oldCity = dbReader.city(InetAddress.getByName(oldIp));
            CityResponse newCity = dbReader.city(InetAddress.getByName(newIp));
            return isSameCity(oldCity, newCity);
        } catch (IOException | GeoIp2Exception e) {
            throw new NotFoundException("Address not found for IP: " + oldIp + " or " + newIp);
        }
    }

    private static boolean isSameCity(CityResponse oldCity, CityResponse newCity) {
        return oldCity.getCity().getGeoNameId().equals(newCity.getCity().getGeoNameId());
    }
}
