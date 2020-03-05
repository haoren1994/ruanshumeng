package com.ruanshumeng.controller;


import com.ruanshumeng.dto.AccessTokenDTO;
import com.ruanshumeng.dto.GithubUser;

import com.ruanshumeng.mapper.UserMapper;
import com.ruanshumeng.model.User;
import com.ruanshumeng.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Autowired
    private UserMapper userMapper;



    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_secret(clientSecret);

        // 获取token
        String token = githubProvider.getAccessToken(accessTokenDTO);

        GithubUser githubUser = githubProvider.getUser(token);

        if(githubUser != null){
            request.getSession().setAttribute("user",githubUser);
            User user = new User();

            user.setAccountId(UUID.randomUUID().toString());
            user.setGmtModified(System.currentTimeMillis());
            user.setGmtCreate(System.currentTimeMillis());
            user.setToken(token);
            user.setName(githubUser.getName());
            System.out.println(user);

            userMapper.insert(user);


            return "redirect:/";
        }else{
            return "redirect:/";
        }
    }

}
