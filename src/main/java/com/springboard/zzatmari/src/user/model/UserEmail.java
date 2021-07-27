package com.springboard.zzatmari.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserEmail {
    private int count;
    private int userIdx;
    private String email;
    private String password;
    private int loginType;
}
