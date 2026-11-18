package com.elae.ville.security;

public interface SecParams {
    long EXP_TIME = 10L * 24 * 60 * 60 * 1000;   // 10 jours
    String SECRET = "elae@gmail.com";
    String PREFIX = "Bearer ";

}
