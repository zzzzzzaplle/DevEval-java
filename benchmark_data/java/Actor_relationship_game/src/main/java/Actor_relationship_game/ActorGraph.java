package Actor_relationship_game;

import java.io.Serializable;
import java.util.*;

public class ActorGraph implements Serializable {
    private static final long serialVersionUID=1L;
    private Map<String, Actor> actors;
    private Map<String, Movie> movies;
    private Map<String, String> nameToIdMap;
    private Map<String, String> idToNameMap;

    public ActorGraph() {
        this.actors = new HashMap<>();
        this.movies = new HashMap<>();
        this.nameToIdMap = new HashMap<>();
        this.idToNameMap = new HashMap<>();
    }

    // getters
    public Map<String, Actor> getActors() {
        return actors;
    }
    public Map<String, Movie> getMovies() {
        return movies;
    }
    public Map<String, String> getIdToNameMap() {
        return idToNameMap;
    }
    public Map<String, String> getNameToIdMap() {
        return nameToIdMap;
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    // Methods
    public void addActor(Actor actor) {
        actors.putIfAbsent(actor.getId(), actor);

        // 将演员名字和id映射关系保存到nameToIdMap和idToNameMap中
        // 第一个是用于根据名字查找id，第二个是用于根据id查找名字
        nameToIdMap.put(actor.getName(), actor.getId());
        idToNameMap.put(actor.getId(), actor.getName());
    }

    public void addMovie(Movie movie) {
        movies.putIfAbsent(movie.getId(), movie);
    }

    public String getActorIdByName(String name) {
        return nameToIdMap.get(name);
    }

    public String getActorNameById(String id) {
        return idToNameMap.get(id);
    }

    public List<String> getAllActorNames() {
        return new ArrayList<>(nameToIdMap.keySet());
    }

    /**
     * This connects an actor to a movie.
     * It's useful for building the graph based on TMDB API data.
     */
    public void addActorToMovie(String actorId, String movieId) {
        if (actors.containsKey(actorId) && movies.containsKey(movieId)) {
            // 先检查演员和电影是否存在
            // 如果存在，则将电影id添加到演员的movieIds中，将演员id添加到电影的actorIds中
            Actor actor = actors.get(actorId);
            Movie movie = movies.get(movieId);
            actor.getMovieIds().add(movieId);
            movie.getActorIds().add(actorId);
        }
    }

    /**
     * Implements BFS to find the shortest path from startActorId to endActorId.
     * It uses a queue for BFS and a map (visited) to track the visited actors and their previous actor in the path.
     */
    // 查找两个演员之间的最短路径
    // 使用BFS算法，从startActorId开始，沿着visited和previousMovie的映射关系，构建路径
    // 路径的每个元素是演员名字和电影名字的映射
    // 路径的第一个元素是startActorId，最后一个元素是endActorId
    public List<Map.Entry<String, String>> findConnectionWithPath(String startActorId, String endActorId) {
        if (!actors.containsKey(startActorId) || !actors.containsKey(endActorId)) {
            return Collections.emptyList();
        }

        Queue<String> queue = new LinkedList<>();
        Map<String, String> visited = new HashMap<>();
        Map<String, String> previousMovie = new HashMap<>();
        queue.add(startActorId);
        visited.put(startActorId, null);

        while (!queue.isEmpty()) {
            String currentActorId = queue.poll();
            Actor currentActor = actors.get(currentActorId);

            for (String movieId : currentActor.getMovieIds()) {
                Movie movie = movies.get(movieId);
                for (String coActorId : movie.getActorIds()) {
                    if (!visited.containsKey(coActorId)) {
                        visited.put(coActorId, currentActorId);
                        previousMovie.put(coActorId, movieId);
                        queue.add(coActorId);

                        if (coActorId.equals(endActorId)) {
                            return buildPath(visited, previousMovie, endActorId);
                        }
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Helper method to construct the path from the endActorId back to the startActorId using the visited map.
     */
    // 构建路径的辅助方法
    // 从endActorId开始，沿着visited和previousMovie的映射关系，构建路径
    // 路径的每个元素是演员名字和电影名字的映射
    // 路径的第一个元素是endActorId，最后一个元素是startActorId
    private List<Map.Entry<String, String>> buildPath(Map<String, String> visited, Map<String, String> previousMovie, String endActorId) {
        LinkedList<Map.Entry<String, String>> path = new LinkedList<>();
        String current = endActorId;
        while (current != null) {
            String movieId = previousMovie.get(current);
            String movieName = (movieId != null) ? movies.get(movieId).getTitle() : "Start";
            path.addFirst(new AbstractMap.SimpleEntry<>(idToNameMap.get(current), movieName));
            current = visited.get(current);
        }
        return path;
    }
}

