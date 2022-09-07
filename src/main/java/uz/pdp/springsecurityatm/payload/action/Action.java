package uz.pdp.springsecurityatm.payload.action;

import org.springframework.http.HttpHeaders;
import uz.pdp.springsecurityatm.entity.enums.CardName;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

public class Action {
    public static String getToken(HttpServletRequest request) {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer"))
            return header.replace("Bearer ", "");
        return null;
    }

    public static String generateCardCode(CardName cardName) {
        String code;
        if (cardName.equals(CardName.UZCARD)) {
            code = "8600";
        } else if (cardName.equals(CardName.HUMO)) {
            code = "9860";
        } else if (cardName.equals(CardName.VISA)) {
            code = String.format("4%s", (int) (Math.random() * (999 - 100)) + 100);
        } else {
            return null;
        }
        return String.format("%s%s%s%s", code, getCode(), getCode(), getCode());
    }

    public static int getCode() {
        return (int) (Math.random() * (9999 - 1000)) + 1000;
    }

    public static Date getCustomExpireDate(Integer amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, amount >= 5 ? amount : 5);
        return calendar.getTime();
    }
}
