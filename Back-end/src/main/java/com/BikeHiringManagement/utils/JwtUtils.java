package com.BikeHiringManagement.utils;

import com.BikeHiringManagement.service.system.UserDetailObject;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    //@Value("${trackingasset.app.jwtSecret}")
    public static String jwtSecret = "bezKoderSecretKey";

    // @Value("${trackingasset.app.jwtExpirationMs}")
    private int jwtExpirationMs = 86400000 * 10;

    public String generateJwtToken(Authentication authentication) {
        UserDetailObject userPrincipal = (UserDetailObject) authentication.getPrincipal();
        System.out.println(jwtSecret);
        Map<String, Object> mapClaim = new HashMap<>();
        mapClaim.put("userId", userPrincipal.getId());
        return Jwts.builder().setClaims(mapClaim).setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
    }

    public static UserDetailObject getPrincipal() {
        if (SecurityContextHolder.getContext() != null
                && SecurityContextHolder.getContext().getAuthentication() != null) {
            return (UserDetailObject) (SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
        }
        return null;
    }

    public static String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public static String getUserIdFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("userId").toString();
    }

    public static String getUserNameJwtToken(String token) {
        String username;
        try {
            Claims claims= Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            username =  claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;

    }

    public static String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public static String getLoggedInUsername() {
        UserDetailObject principal = getPrincipal();
        if (principal != null) {
            return principal.getUsername();
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
