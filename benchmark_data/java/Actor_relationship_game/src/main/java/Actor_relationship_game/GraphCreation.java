package Actor_relationship_game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class GraphCreation {
    private final TMDBApi tmdbApi;
    private final ActorGraph actorGraph;

    public GraphCreation() {
        this.tmdbApi = new TMDBApi();
        this.actorGraph = new ActorGraph();
    }

    public void createGraph(String fileName) throws IOException {
        //填充图数据，先填充演员数据，再填充电影数据
        populateGraphWithActors();
        saveGraphToFile(fileName);
    }

    private void populateGraphWithActors() throws IOException {
        //从TMDB API获取流行演员数据
        String popularActorsJson = tmdbApi.searchPopularActors();
        //将演员数据转换为JSON数组
        JsonArray actorsArray = JsonParser.parseString(popularActorsJson)
                .getAsJsonObject().getAsJsonArray("results");

        //遍历演员数组，处理每个演员
        for (JsonElement actorElement : actorsArray) {
            //处理每个演员
            //将演员数据转换为Actor对象，并添加到图数据中
            processActorElement(actorElement);
            //为每个演员填充电影数据
            populateGraphWithMoviesForActor(actorElement.getAsJsonObject().get("id").getAsString());
        }
    }

    private void processActorElement(JsonElement actorElement) throws IOException {
        JsonObject actorObject = actorElement.getAsJsonObject();
        String actorId = actorObject.get("id").getAsString();
        String actorName = actorObject.get("name").getAsString();

        Actor actor = new Actor(actorId, actorName);
        actorGraph.addActor(actor);
        //为每个演员填充电影数据
        populateGraphWithMoviesForActor(actorId);
    }

    private void populateGraphWithMoviesForActor(String actorId) throws IOException {
        //从TMDB API获取演员的电影数据
        String moviesJson = tmdbApi.getMoviesByActorId(actorId);
        //将电影数据转换为JSON数组
        JsonArray moviesArray = JsonParser.parseString(moviesJson)
                .getAsJsonObject().getAsJsonArray("cast");

        //遍历电影数组，处理每个电影
        for (JsonElement movieElement : moviesArray) {
            //处理每个电影
            //将电影数据转换为Movie对象，并添加到图数据中
            //将电影id和演员id添加到图数据中
            processMovieElement(movieElement, actorId);
        }
    }

    private void processMovieElement(JsonElement movieElement, String actorId) {
        JsonObject movieObject = movieElement.getAsJsonObject();
        String movieId = movieObject.get("id").getAsString();
        //将电影标题转换为字符串
        String movieTitle = movieObject.get("title").getAsString();
        //将电影数据转换为Movie对象，并添加到图数据中
        Movie movie = new Movie(movieId, movieTitle);
        actorGraph.addMovie(movie);
        actorGraph.addActorToMovie(actorId, movieId);
    }

    private void saveGraphToFile(String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(actorGraph);
            System.out.println("Serialized data is saved in "+fileName);
        }
    }

    public static void main(String[] args) {
        try {
            String fileName = args[0];
            new GraphCreation().createGraph(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


/*
数据流程
TMDB API
   ↓
searchPopularActors() → 获取流行演员列表
   ↓
processActorElement() → 处理每个演员
   ↓
getMoviesByActorId() → 获取每个演员的电影
   ↓
processMovieElement() → 处理每部电影
   ↓
ActorGraph (内存中的图结构)
   ↓
saveGraphToFile() → 序列化保存
   ↓
actorGraph.ser (磁盘文件)
 */