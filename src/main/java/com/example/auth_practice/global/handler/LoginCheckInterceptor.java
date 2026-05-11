package com.example.auth_practice.global.handler;

import com.example.auth_practice.auth.exception.LoginRequiredException;
import com.example.auth_practice.auth.session.SessionConst;
import com.example.auth_practice.auth.session.SessionMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("로그인 체크 인터셉터 실행 [{}]", request.getRequestURI());

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new LoginRequiredException();
        }

        SessionMember sessionMember = (SessionMember) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (sessionMember == null){
            throw new LoginRequiredException();
        }
        request.setAttribute(SessionConst.REQUEST_MEMBER, sessionMember);
        return true;
    }
}
