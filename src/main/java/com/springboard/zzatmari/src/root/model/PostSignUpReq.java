package com.springboard.zzatmari.src.root.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSignUpReq {
    private String email;
    private String password;
}
