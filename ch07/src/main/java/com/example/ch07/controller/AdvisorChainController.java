package com.example.ch07.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch07.service.AdvisorChainService;

@Controller
public class AdvisorChainController {

    private final AdvisorChainService service;

    public AdvisorChainController(AdvisorChainService service) {
        this.service = service;
    }

    @GetMapping("/ai/advisor-chain")
    public String advisorChain() {
        return "advisor-chain";
    }

    @ResponseBody
    @PostMapping("/ai/advisor-chain")
    public String advisorChain(
            @RequestParam("question") String question) {

        return service.call(question);
    }
}