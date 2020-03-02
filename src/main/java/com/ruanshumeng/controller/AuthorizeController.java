package com.ruanshumeng.controller;


import com.ruanshumeng.dto.AccessTokenDTO;
import com.ruanshumeng.dto.GithubUser;
import com.ruanshumeng.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,@RequestParam(name = "state") String state ){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_secret(clientSecret);

        // 获取token
        String token = githubProvider.getAccessToken(accessTokenDTO);

        GithubUser user = githubProvider.getUser(token);

        System.out.println(user.getName());


        return "index";
    }

}
