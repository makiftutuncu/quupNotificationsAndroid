package com.mehmetakiftutuncu.quupnotifications.models;

import com.mehmetakiftutuncu.quupnotifications.R;

public enum NotificationType {
    QuupLike(R.string.notifications_like, R.mipmap.ic_like),
    CommentLike(R.string.notifications_commentLike, R.mipmap.ic_like),
    Comment(R.string.notifications_comment, R.mipmap.ic_comment),
    Mention(R.string.notifications_mention, R.mipmap.ic_mention),
    Message(R.string.notifications_message, R.mipmap.ic_message),
    Follow(R.string.notifications_follow, R.mipmap.ic_follow),
    Share(R.string.notifications_share, R.mipmap.ic_share);

    public final int messageResourceId;
    public final int smallIconResourceId;

    NotificationType(int messageResourceId, int smallIconResourceId) {
        this.messageResourceId   = messageResourceId;
        this.smallIconResourceId = smallIconResourceId;
    }
}
