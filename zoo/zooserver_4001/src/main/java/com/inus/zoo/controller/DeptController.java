
package com.inus.zoo.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inus.zoo.domain.Dept;
import com.inus.zoo.service.DeptService;

@ RestController
@ RequestMapping("/dept")
public class DeptController
{

    @ Autowired
    private DeptService deptService;

    @ RequestMapping("/add")
    public boolean add(HttpServletRequest request, @ RequestBody Dept dept) throws IOException
    {
        return deptService.add(dept);
    }

    @ RequestMapping("/get/{no}")
    public Dept get(@ PathVariable("no") Integer no)
    {
        return deptService.get(no);
    }

    @ RequestMapping("/list")
    public List<Dept> list()
    {
        return deptService.list();
    }

}
