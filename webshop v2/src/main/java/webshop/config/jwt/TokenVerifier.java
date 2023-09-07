package webshop.config.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import webshop.config.services.CustomUserDetailsService;

public class TokenVerifier extends OncePerRequestFilter {
	

	@Autowired
	private JwtToken tokenHelper;

	@Autowired
	private CustomUserDetailsService userDetailService;

	private static final Logger filterLog = LoggerFactory.getLogger(TokenVerifier.class);

	@Override
	protected void doFilterInternal(HttpServletRequest incomingRequest, HttpServletResponse outgoingResponse, FilterChain chain)
			throws ServletException, IOException {

		String token = null;
		try {
			token = getTokenFromRequest(incomingRequest);

			if (token != null && tokenHelper.isValidToken(token)) {
				initiateAuthentication(token, incomingRequest);
			}
		} catch (Exception exception) {
			filterLog.error("Authentication error: {}", exception);
		} finally {
			chain.doFilter(incomingRequest, outgoingResponse);
		}
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		return validateAndExtractToken(authorizationHeader);
	}

	private String validateAndExtractToken(String header) {
		if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
     	return header.substring(7);
		}
		return null;
	}

	private void initiateAuthentication(String token, HttpServletRequest request) {
		String userId = tokenHelper.extractUsername(token);
		UserDetails userDetails = userDetailService.loadUserByUsername(userId);

		UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());

		userAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
	}
}
