package com.my.worldwave.auth.resolver;

import com.my.worldwave.member.dto.MemberInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthenticationPrincipal = parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
        boolean hasMemberType = MemberInfoDto.class.isAssignableFrom(parameter.getParameterType());

        return hasAuthenticationPrincipal && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        MemberInfoDto memberInfo = (MemberInfoDto) session.getAttribute("authUser");
        log.info(memberInfo.getEmail());
        if (memberInfo.getId() == null) {
            return null;
        }

        return memberInfo;
    }

}
