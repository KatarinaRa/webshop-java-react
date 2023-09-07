package webshop.config.jwt;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

	@Component
	public class AuthJwt implements AuthenticationEntryPoint {

	private static final Logger authLogger = LoggerFactory.getLogger(AuthJwt.class);

	@Override
	public void commence(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AuthenticationException authEx)
		throws IOException, ServletException {
		authLogger.error("Unauthorized access: {}", authEx.getMessage());

		httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		populateResponseBody(httpRequest, httpResponse, authEx);
  }
	

	private void populateResponseBody(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException {
		Map<String, Object> responseMap = new LinkedHashMap<>();
		responseMap.put("status_code", HttpServletResponse.SC_UNAUTHORIZED);
		responseMap.put("reason", "Unauthorized Access");
		responseMap.put("description", exception.getMessage());
		responseMap.put("requested_url", request.getRequestURI());

		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.writeValue(response.getOutputStream(), responseMap);
	}
}
