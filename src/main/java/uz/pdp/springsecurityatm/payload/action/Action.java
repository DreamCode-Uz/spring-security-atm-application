package uz.pdp.springsecurityatm.payload.action;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

public class Action {
    public static String getToken(HttpServletRequest request) {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer"))
            return header.replace("Bearer ", "");
        return null;
    }
}
