package org.example.commandManager;

import org.example.collectionManager.CollectionManager;
import org.example.managers.UserStatusManager;
import org.example.models.Movie;
import org.example.response.Response;
import org.example.response.STATUS;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Show - выводит все элементы коллекции
 */
public class GetLinkedList extends Command{
    private final CollectionManager collectionManager;
    private final Logger logger;
    public GetLinkedList(CollectionManager collectionManager, CommandManager commandManager, Logger logger){
        super("getLinkedList", "вернуть коллекцию");
        commandManager.addCommandList(getName(), getDescription());
        this.collectionManager = collectionManager;
        this.logger = logger;
    }
    /**
     * Выполнение команды
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    @Override
    public Response execution(String args, Object object, UserStatusManager userStatusManager){
        if ((args == null || args.isEmpty())){
            logger.info(super.getName());
            LinkedList<Movie> newMovies = new LinkedList<>();
            for (Movie movie: collectionManager.getCollection()){
                if (userStatusManager.getUser_name().equals(movie.getUser_name())){
                    Movie movie1 = movie;
                    movie1.setCreationDate(new Date(12, 01, 12));
                    newMovies.add(movie1);
                }
            }
            return new Response(STATUS.OK, "Возвращение коллекции", newMovies);
        } else {
            logger.warning("Неправильное количество аргументов!");
            return new Response(STATUS.ERROR, "Неправильное количество аргументов!");
        }
    }
}
