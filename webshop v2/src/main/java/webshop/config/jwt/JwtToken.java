package webshop.config.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import webshop.config.services.CustomUserDetails;

@Component
public class JwtToken {
  private static final Logger log = LoggerFactory.getLogger(JwtToken.class);
  
  @Value("${webshop.jwtSecret}")
  private String tokenSecret;

  @Value("${webshop.jwtExpirationMs}")
  private int tokenExpirationMs;

  public String createToken(Authentication authInfo) {
	    CustomUserDetails userDetail = (CustomUserDetails) authInfo.getPrincipal();
	    
	    Date currentDate = new Date();
	    long expiryTime = currentDate.getTime() + tokenExpirationMs;
	    Date expiryDate = new Date(expiryTime);
	    
	    JwtBuilder jwtBuilder = Jwts.builder();
	    
	    jwtBuilder.setSubject(userDetail.getUsername());
	    jwtBuilder.setIssuedAt(currentDate);
	    jwtBuilder.setExpiration(expiryDate);
	    jwtBuilder.signWith(getSigningKey(), SignatureAlgorithm.HS256);
	    
	    String jwtToken = jwtBuilder.compact();
	    
	    return jwtToken;
	}

  
  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSecret));
  }

  public String extractUsername(String jwt) {
    return Jwts.parserBuilder()
               .setSigningKey(getSigningKey())
               .build()
               .parseClaimsJws(jwt)
               .getBody()
               .getSubject();
  }

  public boolean isValidToken(String receivedToken) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parse(receivedToken);
      return true;
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token has expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims are empty: {}", e.getMessage());
    }

    return false;
  }
}
