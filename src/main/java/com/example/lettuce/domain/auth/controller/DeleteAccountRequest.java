package com.example.lettuce.domain.auth.controller;

import com.example.lettuce.global.framework.security.annotation.ValidPassword;

public record DeleteAccountRequest(@ValidPassword String password) {
}
