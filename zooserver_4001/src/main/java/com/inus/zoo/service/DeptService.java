
package com.inus.zoo.service;

import java.util.List;

import com.inus.zoo.domain.Dept;

public interface DeptService
{

    public boolean add(Dept dept);

    public Dept get(int no);

    public List<Dept> list();

}
