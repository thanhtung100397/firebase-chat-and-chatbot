package com.ttt.chat_module.presenters.chat_bot;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ContactInfo;

import java.util.ArrayList;
import java.util.List;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

public class ChatBotInteractorImpl implements ChatBotInteractor {
    private Context context;
    private AIDataService aiDataService;

    public ChatBotInteractorImpl(Context context) {
        this.context = context;
    }

    @Override
    public void initApiAiService() {
        AIConfiguration config = new AIConfiguration(Constants.API_AI_CLIENT_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiDataService = new AIDataService(context, config);
    }

    @Override
    public void onViewDestroy() {

    }

    @Override
    public void query(String message, OnBotResponseListener listener) {
        AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(message);
        new AIRequestTask(aiDataService, listener).execute(aiRequest);
    }

    private static class AIRequestTask extends AsyncTask<AIRequest, AIResponse, Void> {
        private AIDataService aiDataService;
        private OnBotResponseListener listener;

        AIRequestTask(AIDataService aiDataService, OnBotResponseListener listener) {
            this.aiDataService = aiDataService;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(AIRequest... aiRequests) {
            try {
                for (AIRequest aiRequest : aiRequests) {
                    AIResponse aiResponse = aiDataService.request(aiRequest);
                    publishProgress(aiResponse);
                }
            } catch (AIServiceException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(AIResponse... values) {
            listener.onBotResponse(values[0]);
        }
    }

    @Override
    public List<ContactInfo> searchContacts(String query) {
        List<ContactInfo> result = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        String[] projects = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER};
        String selection = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query.replaceAll("\\s+", "%") + "%"};
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                projects,
                selection,
                selectionArgs,
                null);
        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor.moveToNext()) {
                String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    String[] phoneProjects = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                    String phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
                    String[] phoneSelectionArgs = new String[]{contactID};
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            phoneProjects,
                            phoneSelection,
                            phoneSelectionArgs,
                            null);
                    while (phoneCursor.moveToNext()) {
                        String contactNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        result.add(new ContactInfo(contactName, contactNumber));
                    }
                    phoneCursor.close();
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return result;
    }
}
