package com.patrollers.breezy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.patrollers.breezy.R;
import com.patrollers.breezy.adapters.ChatAdapter;
import com.patrollers.breezy.database.MessageDao;
import com.patrollers.breezy.database.MessageDb;
import com.patrollers.breezy.helper.SendMessageInBg;
import com.patrollers.breezy.interfaces.BotReply;
import com.patrollers.breezy.models.Message;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatFragment extends Fragment implements BotReply {

    @BindView(R.id.chatView)
    RecyclerView chatView;
    @BindView(R.id.editMessage)
    EditText editMessage;
    @BindView(R.id.btnSend)
    ImageButton btnSend;

    private com.patrollers.breezy.adapters.ChatAdapter chatAdapter;
    private MessageDao messageDao;
    List<Message> messageList;

    private SessionsClient sessionsClient;
    private SessionName sessionName;
    private String uuid = UUID.randomUUID().toString();
    public static String TAG = "ChatFragment";

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        ButterKnife.bind(this, root);
        messageDao = MessageDb.getInstance(getContext()).messageDao();

        messageList = messageDao.loadAllMessages();

        chatAdapter = new ChatAdapter(messageList, getActivity());
        chatView.setAdapter(chatAdapter);


        btnSend.setOnClickListener(view -> {
            String message = editMessage.getText().toString();
            if (!message.isEmpty()) {
                Message msg = new Message(message, false);
                messageList.add(msg);
                messageDao.insertItem(msg);
                editMessage.setText("");
                sendMessageToBot(message);
                Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
                Objects.requireNonNull(chatView.getLayoutManager())
                        .scrollToPosition(messageList.size() - 1);
            } else {
                Toast.makeText(getContext(), "Please enter text!", Toast.LENGTH_SHORT).show();
            }
        });

        setUpBot();

        return root;
    }

    private void setUpBot() {
        try {
            InputStream stream = this.getResources().openRawResource(R.raw.credentials);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            sessionName = SessionName.of(projectId, uuid);

            Log.d(TAG, "projectId : " + projectId);
        } catch (Exception e) {
            Log.d(TAG, "setUpBot: " + e.getMessage());
        }
    }

    private void sendMessageToBot(String message) {
        QueryInput input = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
        new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
    }

    @Override
    public void callback(DetectIntentResponse returnResponse) {
        if (returnResponse != null) {
            String botReply = returnResponse.getQueryResult().getFulfillmentText();
            if (!botReply.isEmpty()) {
                Message msg = new Message(botReply, true);
                messageList.add(msg);
                messageDao.insertItem(msg);
                chatAdapter.notifyDataSetChanged();
                Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
            } else {
                Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }
}