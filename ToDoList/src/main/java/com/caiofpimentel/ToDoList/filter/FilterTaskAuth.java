package com.caiofpimentel.ToDoList.filter;


import at.favre.lib.crypto.bcrypt.BCrypt;
import com.caiofpimentel.ToDoList.User.UserRepository.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;


@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.equals("/tasks/")) {
            //faz a solicitação dos dados
            var authorization = request.getHeader("Authorization");
            //remove "Basic" e os espaços
            var authEncoded = authorization.substring("Basic".length()).trim();
            //decodifica base64
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
            //transforma em uma String
            var authString = new String(authDecoded);
            //separa os dados
            String[] credentials = authString.split(":");
            //dados prontos para trabalhar
            String username = credentials[0];
            String password = credentials[1];

            //validar usuario
            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401);
            }//validar a senha
            else {
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }

            }


        } else {
            filterChain.doFilter(request, response);

        }
    }
}
