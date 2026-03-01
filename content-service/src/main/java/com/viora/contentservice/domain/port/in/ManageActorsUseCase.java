package com.viora.contentservice.domain.port.in;

import com.viora.contentservice.domain.domain.Actor;
import com.viora.contentservice.domain.port.in.command.AddActorCommand;

public interface ManageActorsUseCase {

    Actor savaActor(AddActorCommand command);

}
