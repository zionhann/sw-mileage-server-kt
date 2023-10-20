package edu.handong.cseemileage

import org.jetbrains.annotations.NotNull

class LoginForm(
    @field: NotNull
    var password: String,
    @field: NotNull
    var uniqueId: String // 학번 or 직번
)
