package com.ttt.chat_module.presenters.chat_bot;

import ai.api.model.AIResponse;

public interface OnBotResponseListener {
    void onBotResponse(AIResponse aiResponse);
    void onError(String message);
}
