package com.example.uzum_market.annotations;

public class CurrentUserArgumentResolver {
//        implements HandlerMethodArgumentResolver {

//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        return parameter.getParameterAnnotation(CurrentUser.class) != null;
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
//
//        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
//        String authorizationHeader = request.getHeader("Authorization");
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//            String username = jwtUtil.extractUsername(token);
//
//            if (username != null) {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                if (jwtUtil.validateToken(token, userDetails)) {
//                    return userDetails;
//                }
//            }
//        }
//        return null; // or throw an exception if necessary
//    }
}
