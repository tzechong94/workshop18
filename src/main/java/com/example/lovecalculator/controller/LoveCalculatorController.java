package com.example.lovecalculator.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lovecalculator.model.Result;
import com.example.lovecalculator.service.LoveCalculatorService;

@Controller
public class LoveCalculatorController {

    @Autowired
    private LoveCalculatorService loveSvc;


    @GetMapping(path="result")
    public String getResult(@RequestParam(required = true)String fname,
     @RequestParam(required = true) String sname, 
     Model model) throws Exception{

        Optional<Result> r = loveSvc.getResult(fname, sname);
        model.addAttribute("result", r.get());
        System.out.println("RESULTS" + r.get());
        return "result";
    }

    @GetMapping(path="/list")
    public String getList(Model model){
        List<Result> resultMap = loveSvc.findAll();
        if (resultMap!=null){
            model.addAttribute("result", resultMap);
          
        }
        return "list";
    }
}
