package com.ttt.chat_module.presenters.chat_bot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.JsonElement;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.models.ContactInfo;
import com.ttt.chat_module.models.chat_bot_message.ContactQueryMessage;
import com.ttt.chat_module.views.chat_bot.ChatBotActivityView;


import java.util.List;

import ai.api.model.AIResponse;
import ai.api.model.Result;

public class ChatBotPresenterImpl implements ChatBotPresenter {
    private static final String TAG = "ChatBotPresenterImpl";

    private Context context;
    private ChatBotActivityView chatBotActivityView;
    private ChatBotInteractor chatBotInteractor;
    private Result pendingResult;
    private Camera camera;

    public ChatBotPresenterImpl(Context context, ChatBotActivityView chatBotActivityView) {
        this.context = context;
        this.chatBotActivityView = chatBotActivityView;
        this.chatBotInteractor = new ChatBotInteractorImpl(context);
    }

    @Override
    public void onViewDestroy() {
        chatBotInteractor.onViewDestroy();
    }

    @Override
    public void registerApiAiService() {
        chatBotInteractor.initApiAiService();
    }

    @Override
    public void sendQueryMessage(String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        chatBotActivityView.clearInputMessage();
        chatBotActivityView.showUserMessage(message);
        chatBotInteractor.query(message, new OnBotResponseListener() {
            @Override
            public void onBotResponse(AIResponse aiResponse) {
                pendingResult = aiResponse.getResult();
                handleChatBotPendingAction();
            }

            @Override
            public void onError(String message) {
                Log.i(TAG, "onError: " + message);
                ToastUtils.quickToast(context, R.string.chat_bot_error);
            }
        });
    }

    @Override
    public void handleChatBotPendingAction() {
        if (pendingResult == null) {
            return;
        }
        switch (pendingResult.getAction()) {
            case Constants.CHAT_BOT_ACTION_DIAL: {
                handleActionDial();
            }
            break;

            case Constants.CHAT_BOT_ACTION_CALL: {
                handleActionCall();
            }
            break;

            case Constants.CHAT_BOT_ACTION_CONTACT: {
                handleActionContact();
            }
            break;

            case Constants.CHAT_BOT_ACTION_VOLUME: {
                handleActionAdjustVolume();
            }
            break;

            case Constants.CHAT_BOT_ACTION_PLAY_MUSIC: {
                handleActionPlayMusic();
            }
            break;

            case Constants.CHAT_BOT_ACTION_OPEN_FLASHLIGHT: {
                handleActionOpenFlash();
            }
            break;

            default: {
                chatBotActivityView.showChatBotMessage(pendingResult.getFulfillment().getSpeech());
                pendingResult = null;
                break;
            }
        }
    }

    private void handleActionCall() {
        if (chatBotActivityView.requestPhoneCallPermission()) {
            call(pendingResult.getParameters().get(Constants.CHAT_BOT_PHONE_NUMBER_PARAM).getAsString());
            chatBotActivityView.showChatBotMessage(pendingResult.getFulfillment().getSpeech());
            pendingResult = null;
        }
    }

    @SuppressLint("MissingPermission")
    private void call(String phone) {
        Uri number = Uri.parse("tel:" + phone);
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        context.startActivity(callIntent);
    }

    private void handleActionContact() {
        if (chatBotActivityView.requestReadContactsAndPhoneCallPermission()) {
            JsonElement contactPram = pendingResult.getParameters().get(Constants.CHAT_BOT_CONTACT_PARAM);
            List<ContactInfo> contactsInfo = chatBotInteractor.searchContacts(contactPram.getAsString());
            if (contactsInfo.size() == 0) {
                chatBotActivityView.showChatBotMessage(context.getString(R.string.not_found) + " '" + contactPram.getAsString() + "' " + context.getString(R.string.in_contact));
            } else if (contactsInfo.size() == 1) {
                String phoneNumber = contactsInfo.get(0).getContactNumber();
                call(phoneNumber);
                chatBotActivityView.showChatBotMessage(context.getString(R.string.make_call) + " " + phoneNumber);
            } else {
                String message = contactsInfo.size() + " " + context.getString(R.string.search_result_with_keyword) + " '" + contactPram.getAsString() + "' " + context.getString(R.string.in_contact);
                ContactQueryMessage contactQueryMessage = new ContactQueryMessage(message, Constants.CHAT_BOT_ACTION_CALL, contactsInfo);
                chatBotActivityView.showChatBotMessageAndItems(contactQueryMessage);
            }
            pendingResult = null;
        }
    }

    private void handleActionDial() {
        JsonElement contactPram = pendingResult.getParameters().get(Constants.CHAT_BOT_CONTACT_PARAM);
        if (contactPram == null) {
            JsonElement phonePram = pendingResult.getParameters().get(Constants.CHAT_BOT_PHONE_NUMBER_PARAM);
            dial(phonePram.getAsString());
            chatBotActivityView.showChatBotMessage(pendingResult.getFulfillment().getSpeech());
            pendingResult = null;
        } else {
            if (chatBotActivityView.requestReadContactsPermission()) {
                List<ContactInfo> contactsInfo = chatBotInteractor.searchContacts(contactPram.getAsString());
                if (contactsInfo.size() == 0) {
                    chatBotActivityView.showChatBotMessage(context.getString(R.string.not_found) + " '" + contactPram.getAsString() + "' " + context.getString(R.string.in_contact));
                } else if (contactsInfo.size() == 1) {
                    String phoneNumber = contactsInfo.get(0).getContactNumber();
                    dial(phoneNumber);
                    chatBotActivityView.showChatBotMessage(context.getString(R.string.make_dial) + " " + phoneNumber);
                } else {
                    String message = contactsInfo.size() + " " + context.getString(R.string.search_result_with_keyword) + " '" + contactPram.getAsString() + "' " + context.getString(R.string.in_contact);
                    ContactQueryMessage contactQueryMessage = new ContactQueryMessage(message, Constants.CHAT_BOT_ACTION_DIAL, contactsInfo);
                    chatBotActivityView.showChatBotMessageAndItems(contactQueryMessage);
                }
                pendingResult = null;
            }
        }
    }

    private void dial(String phoneNumber) {
        Uri number;
        if (phoneNumber == null) {
            number = Uri.parse("tel:");
        } else {
            number = Uri.parse("tel:" + phoneNumber);
        }
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        context.startActivity(callIntent);
    }

    private void handleActionAdjustVolume() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        String botMessage;
        if (audioManager == null) {
            botMessage = context.getString(R.string.bot_cant_change_volume);
        } else {
            JsonElement volume = pendingResult.getParameters()
                    .get(Constants.CHAT_BOT_VOLUME_PARAM);
            if (volume == null) {
                botMessage = pendingResult.getFulfillment().getSpeech();
            } else {
                String adjust = volume.getAsJsonObject()
                        .get(Constants.CHAT_BOT_ADJUST_PARAM)
                        .getAsString();
                switch (adjust) {
                    case Constants.CHAT_BOT_MAX_VOLUME: {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
                        botMessage = pendingResult.getFulfillment().getSpeech();
                    }
                    break;

                    case Constants.CHAT_BOT_SILENCE_VOLUME: {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
                        botMessage = pendingResult.getFulfillment().getSpeech();
                    }
                    break;

                    default: {
                        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        float volumeValue = Float.parseFloat(adjust.replace("%", ""));
                        if (volumeValue < 0 || volumeValue > 100) {
                            botMessage = context.getString(R.string.volume_value_invalid);
                        } else {
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Math.round(volumeValue / 100 * maxVolume), AudioManager.FLAG_SHOW_UI);
                            botMessage = pendingResult.getFulfillment().getSpeech();
                        }
                    }
                    break;
                }
            }
        }
        chatBotActivityView.showChatBotMessage(botMessage);
        pendingResult = null;
    }

    private void handleActionPlayMusic() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
        context.startActivity(intent);
        pendingResult = null;
    }

    private void handleActionOpenFlash() {
        if (!chatBotActivityView.requestCameraPermission()) {
            return;
        }
        String botMessage;
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            botMessage = context.getString(R.string.no_camera_flash);
        } else {
            String state = pendingResult.getParameters()
                    .get(Constants.CHAT_BOT_STATE_PARAM)
                    .getAsString();
            switch (state) {
                case Constants.CHAT_BOT_ON: {
                    if (camera == null) {
                        camera = Camera.open();
                        Camera.Parameters p = camera.getParameters();
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(p);
                        camera.startPreview();
                        botMessage = pendingResult.getFulfillment().getSpeech();
                    } else {
                        botMessage = context.getString(R.string.camera_on);
                    }
                }
                break;

                case Constants.CHAT_BOT_OFF: {
                    if (camera == null) {
                        botMessage = context.getString(R.string.camera_off);
                    } else {
                        camera.stopPreview();
                        camera.release();
                        camera = null;
                        botMessage = pendingResult.getFulfillment().getSpeech();
                    }
                }
                break;

                default: {
                    botMessage = context.getString(R.string.chat_bot_error);
                    break;
                }
            }
        }
        chatBotActivityView.showChatBotMessage(botMessage);
        pendingResult = null;
    }
}
