package com.googlecode.propidle.authorisation.groups;

import com.googlecode.propidle.util.tinytype.StringTinyType;
import com.googlecode.totallylazy.Callable1;

public class GroupName extends StringTinyType<GroupName> {
    public static GroupName groupName(String value) {
        return new GroupName(value);
    }

    protected GroupName(String value) {
        super(value);
    }

    public static Callable1<String, GroupName> asGroupName(){
        return new Callable1<String, GroupName>() {
            public GroupName call(String value) throws Exception {
               return groupName(value);
            }
        };
    }
}
