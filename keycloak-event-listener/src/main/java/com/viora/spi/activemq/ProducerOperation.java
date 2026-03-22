package com.viora.spi.activemq;


import jakarta.jms.JMSException;

@FunctionalInterface
public interface ProducerOperation<T> {
    void accept(T session) throws JMSException;
}
