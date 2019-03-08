package com.attribe.waiterapp.utils;

import com.attribe.waiterapp.models.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maaz on 4/14/2016.
 */
public class Group {

    public String string;
    public final List<Order> children = new ArrayList<>();

    public Group(String string) {
        this.string = string;
    }

}
