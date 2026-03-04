package agiliz.projetoAgiliz.configs.security;

import agiliz.projetoAgiliz.configs.security.JWT.GerenciadorTokenJWT;
import agiliz.projetoAgiliz.services.AutenticacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AutenticacaoFilter extends OncePerRequestFilter {
    private final AutenticacaoService autenticacaoService;
    private final GerenciadorTokenJWT jwtTokenManager;

    public AutenticacaoFilter(AutenticacaoService autenticacaoService, GerenciadorTokenJWT jwtTokenManager) {
        this.autenticacaoService = autenticacaoService;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String userName = null;
        String jwtToken = null;
        String requestTokenHeader = request.getHeader("Authorization");
        if (Objects.nonNull(requestTokenHeader) && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                userName = jwtTokenManager.getUsernameFromToken(jwtToken);
            } catch (ExpiredJwtException e) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Token expirado");
                ObjectMapper objectMapper = new ObjectMapper();
                response.getOutputStream().println(objectMapper.writeValueAsString(responseBody));
                return;
            }
        }
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            addUsernameInContext(request, userName, jwtToken);
        }
        filterChain.doFilter(request, response);
    }

    private void addUsernameInContext(HttpServletRequest request, String userName, String jwtToken) {
        UserDetails userDetails = autenticacaoService.loadUserByUsername(userName);
        if (jwtTokenManager.validateToken(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            System.out.println(auth + "porra da silva");
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            System.out.println("❌ Token inválido!");
        }
    }

}
