package com.example.ivansv.snapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;

public class FeedFragment extends Fragment {
    private Button requestButton;
    private Button shareButton;
    private Button postButton;
    private ShareDialog shareDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        requestButton = (Button) view.findViewById(R.id.requestButton);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/feed",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                JSONArray jsonArray = response.getJSONArray();
                            }
                        }
                ).executeAsync();
            }
        });

        shareButton = (Button) view.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog = new ShareDialog(FeedFragment.this);
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    //share link
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Hello Facebook")
                            .setContentDescription(
                                    "The 'Hello Facebook' sample  showcases simple Facebook integration")
                            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                            .build();
                    shareDialog.show(linkContent);
                    //share photo
                    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.com_facebook_button_icon);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();
                    SharePhotoContent photoContent = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(photoContent);
                    //share video
                    Uri videoFileUri = Uri.parse("");
                    ShareVideo video = new ShareVideo.Builder()
                            .setLocalUrl(videoFileUri)
                            .build();
                    ShareVideoContent videoContent = new ShareVideoContent.Builder()
                            .setVideo(video)
                            .build();
                    shareDialog.show(videoContent);
                }
            }
        });

        postButton = (Button) view.findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("message", "This is a test message");
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/feed",
                        params,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                            }
                        }
                ).executeAsync();
            }
        });

        return view;
    }
}
