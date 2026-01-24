package br.com.todolist.toDoList.filter;

import br.com.todolist.toDoList.repository.UserRepository;
import br.com.todolist.toDoList.entities.UserEntity;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();
        
        // Permitir requisições OPTIONS (preflight do CORS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if (servletPath.startsWith("/tasks")) {

            var authorization = request.getHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Basic ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String authEncoded = authorization.substring(6);
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecode, StandardCharsets.UTF_8);

            String[] credentials = authString.split(":", 2);

            String email = credentials[0];
            String password = credentials[1];

            Optional<UserEntity> user = this.userRepository.findByEmail(email);
            if (user.isEmpty()) {
                response.sendError(401);
                return;
            }
            if (!passwordEncoder.matches(password, user.get().getPassword())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            request.setAttribute("IdUser",user.get().getId());
            filterChain.doFilter(request, response);

        }else {
            filterChain.doFilter(request, response);
        }


    }
}

