package com.viora.spi.activemq;

import com.viora.spi.usecase.RegisterAccountUseCase;
import com.viora.spi.usecase.command.RegisterUserCommand;
import lombok.extern.slf4j.Slf4j;

import static com.viora.spi.activemq.ActiveMQProperties.ACTIVEMQ_QUEUE_USERS;


@Slf4j
public class UserMessageProducer implements RegisterAccountUseCase {

    private final ActiveMQProducer activeMQProducer;

    public UserMessageProducer() {
        this.activeMQProducer = new ActiveMQProducer();
    }

    @Override
    public void registerAccount(RegisterUserCommand registerUserCommand) {
        this.activeMQProducer.convertAndSend(ACTIVEMQ_QUEUE_USERS, registerUserCommand);
    }
}
