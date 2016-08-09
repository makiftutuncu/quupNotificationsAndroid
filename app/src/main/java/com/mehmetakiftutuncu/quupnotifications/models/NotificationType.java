package com.mehmetakiftutuncu.quupnotifications.models;

import com.mehmetakiftutuncu.quupnotifications.R;

public enum NotificationType {
    QuupLike(R.string.notifications_like, R.mipmap.ic_like, "like"),
    CommentLike(R.string.notifications_commentLike, R.mipmap.ic_like, "commentLike"),
    Comment(R.string.notifications_comment, R.mipmap.ic_comment, "comment"),
    Mention(R.string.notifications_mention, R.mipmap.ic_mention, "mention"),
    Message(R.string.notifications_message, R.mipmap.ic_message, "message"),
    Follow(R.string.notifications_follow, R.mipmap.ic_follow, "follow"),
    Share(R.string.notifications_share, R.mipmap.ic_share, "share");

    public final int messageResourceId;
    public final int smallIconResourceId;
    public final String key;

    NotificationType(int messageResourceId, int smallIconResourceId, String key) {
        this.messageResourceId   = messageResourceId;
        this.smallIconResourceId = smallIconResourceId;
        this.key                 = key;
    }
}
