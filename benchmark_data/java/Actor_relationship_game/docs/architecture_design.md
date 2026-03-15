# Architecture Design
Below is a text-based representation of the file tree for the Actor Relationship Game project, including test classes for each Java class.
```bash
├── .gitignore
├── src
│   ├── acceptanceTest
│   │   ├── java
│   │   │   ├── Actor_relationship_game
│   │   │   │   └── ActorRelationshipAcceptanceTest.java
│   ├── main
│   │   ├── java
│   │   │   ├── Actor_relationship_game
│   │   │   │   ├── Actor.java
│   │   │   │   ├── Movie.java
│   │   │   │   ├── ActorGraph.java
│   │   │   │   ├── ActorGraphUtil.java
│   │   │   │   ├── GameplayInterface.java
│   │   │   │   ├── GraphCreation.java
│   │   │   │   └── TMDBApi.java
│   ├── test
│   │   ├── java
│   │   │   ├── Actor_relationship_game
│   │   │   │   ├── ActorTest.java
│   │   │   │   ├── MovieTest.java
│   │   │   │   ├── ActorGraphTest.java
│   │   │   │   ├── ActorGraphUtilTest.java
│   │   │   │   ├── GameplayInterfaceTest.java
│   │   │   │   ├── GraphCreationTest.java
│   │   │   │   └── TMDBApiTest.java
├── build.gradle
└── README.md
```

<!-- 项目通过两种方式产生和保存结果 一个是控制台输出，一个是序列化文件-->
Output:
- The project relies on console output and serialized files (`actorGraph.ser` and `actors_list.txt`) for its functionality.

<!-- 该项目需要顺序执行三个步骤,步骤1.创建图，也就是runGraphCreation，步骤二提取演员列表，步骤三是查找任务两个演员之间的最短路径 -->
Examples:
- To execute the project, the user should run the Java classes in the specified order after compiling them with Gradle and setting `TMDB_API_KEY` environmental varible. For example, run `run_ActorRelationshipGame.sh`:
    ```bash
    ./gradlew runGraphCreation -PfileName="./src/main/java/Actor_relationship_game/actorGraph.ser"
    ./gradlew runActorGraphUtil -PgraphPath="./src/main/java/Actor_relationship_game/actorGraph.ser" -PfilePath="./src/main/java/Actor_relationship_game/actors_list.txt"
    ./gradlew runGamePlayInterface -PgraphPath="./src/main/java/Actor_relationship_game/actorGraph.ser" -PactorPath="./src/main/java/Actor_relationship_game/actors_list.txt" -PfilePath="./src/main/java/Actor_relationship_game/actor_connection_results.txt"
    ```


`.gitignore:`
    - Ignore compiled files, serialized objects, and other non-source files (like IDE-specific configurations).

<!-- 存放所有java代码源文件 -->
`src/main/java/Actor_relationship_game`:
    - This directory contains all the Java classes required for the project.

<!-- 存放单元测试代码，验证每个类的功能 -->
`src/test/java`:
    - This directory contains the test cases for the Java classes. It should include unit tests for validating the functionalities of each class.

<!-- 项目配置文件，需要那些依赖 -->
`build.gradle`:
    - This file contains the build configuration for Gradle, specifying the dependencies and plugins required for the project.

Each Java class serves a specific purpose in the project:
- `Actor.java`: Represents an actor entity.
- `Movie.java`: Represents a movie entity.
- `ActorGraph.java`: Manages the graph of actors and movies.
- `ActorGraphUtil.java`: Provides utility functions for the actor graph.
- `GameplayInterface.java`: Handles the user interface and game interaction.
- `GraphCreation.java`: Responsible for creating the initial graph using data from TMDB API.
- `TMDBApi.java`: Manages API calls to TMDB.
