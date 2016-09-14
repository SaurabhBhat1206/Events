package com.events.hanle.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.events.hanle.events.Chat.ChatListEvent;
import com.events.hanle.events.app.*;
import com.events.hanle.events.com.applozic.mobicomkit.sample.*;
import com.events.hanle.events.com.applozic.mobicomkit.sample.LoginActivity;

/**
 * Created by Hanle on 6/7/2016.
 */
public class FragmentChat extends Fragment {

    private String TAG = FragmentChat.class.getSimpleName();
    Button b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        b.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     //Aoolozic code here
//
//                MobiComUserPreference userPreference = MobiComUserPreference.getInstance(getActivity());
//                if (!userPreference.isRegistered()) {
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    //startActivity(intent);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(intent);
//                    getActivity().finish();
//                } else {
//
//                    Intent intent = new Intent(getActivity(),ConversationActivity.class);
//                    startActivity(intent);
//                }
                                     if (com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser() != null) {
                                         startActivity(new Intent(getActivity(), ChatListEvent.class));
                                         getActivity().finish();
                                     } else {
                                         startActivity(new Intent(getActivity(), com.events.hanle.events.LoginActivity.class));
                                     }
                                 }



                //Applozic code ends here


    }

    );
    return v;
}
}
