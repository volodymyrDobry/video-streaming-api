package org.example.userstreaminghistory.api.activemq.ports;

import com.viora.app.message.UserHistoryFailedEvent;

public interface UserHistoryEventHandler {

    void handleSaveHistoryFailure(UserHistoryFailedEvent failure);

}
