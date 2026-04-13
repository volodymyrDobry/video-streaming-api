package org.example.userstreaminghistory.api.activemq.ports;

import org.example.userstreaminghistory.api.activemq.message.SaveHistoryEventFailure;

public interface UserHistoryEventHandler {

    void handleSaveHistoryFailure(SaveHistoryEventFailure failure);

}
