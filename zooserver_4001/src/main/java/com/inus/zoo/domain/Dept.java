
package com.inus.zoo.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ AllArgsConstructor
@ NoArgsConstructor
@ Data
@ Accessors(chain = true)
public class Dept implements Serializable
{
    private static final long serialVersionUID = 7439868287885523566L;

    private int no;
    private String name;
    private String db;

}
