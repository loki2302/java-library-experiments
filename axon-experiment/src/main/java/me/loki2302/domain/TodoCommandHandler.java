package me.loki2302.domain;

import me.loki2302.domain.commands.CreateTodoCommand;
import me.loki2302.domain.commands.DeleteTodoCommand;
import me.loki2302.domain.commands.UpdateTodoCommand;
import me.loki2302.domain.exceptions.TodoCountLimitExceededException;
import me.loki2302.domain.exceptions.TodoNotFoundException;
import me.loki2302.query.todocount.TodoCountEntity;
import me.loki2302.query.todocount.TodoCountEntityRepository;
import me.loki2302.query.todocount.TodoCountEntityUpdatingEventHandler;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.repository.AggregateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TodoCommandHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(TodoCommandHandler.class);

    @Autowired
    private EventSourcingRepository<TodoAggregateRoot> todoRepository;

    @Autowired
    private TodoCountEntityRepository todoCountEntityRepository;

    @CommandHandler
    public String handle(CreateTodoCommand command) throws TodoCountLimitExceededException {
        // !!! using read model to enforce write model constraints !!!
        TodoCountEntity todoCountEntity =
                todoCountEntityRepository.findOne(TodoCountEntityUpdatingEventHandler.SINGLETON_TODO_COUNT_ENTITY_ID);
        if(todoCountEntity.count == 3) {
            throw new TodoCountLimitExceededException();
        }

        String id = UUID.randomUUID().toString();

        TodoAggregateRoot todoAggregateRoot = new TodoAggregateRoot();
        todoAggregateRoot.create(id, command.text);
        todoRepository.add(todoAggregateRoot);

        return id;
    }

    @CommandHandler
    public void handle(UpdateTodoCommand command) throws TodoNotFoundException {
        TodoAggregateRoot todoAggregateRoot;
        try {
            todoAggregateRoot = todoRepository.load(command.todoId);
        } catch (AggregateNotFoundException e) {
            throw new TodoNotFoundException();
        }

        todoAggregateRoot.update(command.text);
    }

    @CommandHandler
    public void handle(DeleteTodoCommand command) throws TodoNotFoundException {
        TodoAggregateRoot todoAggregateRoot;
        try {
            todoAggregateRoot = todoRepository.load(command.todoId);
        } catch (AggregateNotFoundException e) {
            throw new TodoNotFoundException();
        }

        todoAggregateRoot.delete();
    }
}
