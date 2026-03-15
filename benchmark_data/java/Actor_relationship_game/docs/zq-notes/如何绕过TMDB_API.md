# 如何绕过TMDB API运行项目

## 📋 概述

这个文档说明如何在不配置真实TMDB API的情况下运行和测试Actor Relationship Game项目。

## 🎯 为什么需要绕过API？

1. **不需要真实API密钥**：避免注册和配置TMDB账号
2. **测试更稳定**：不依赖网络连接和API服务状态
3. **运行更快**：不需要等待网络请求
4. **数据可预测**：使用固定的测试数据，测试结果一致

## ✅ 解决方案

我已经修改了 `TMDBApi.java` 文件，添加了**测试模式**功能。当启用测试模式时，API会返回模拟数据而不是调用真实的TMDB API。

## 🚀 使用方法

### 方法1：使用环境变量（推荐）

在运行项目前，设置环境变量：

**Windows (PowerShell):**
```powershell
$env:USE_MOCK_TMDB = "true"
```

**Windows (CMD):**
```cmd
set USE_MOCK_TMDB=true
```

**Linux/Mac:**
```bash
export USE_MOCK_TMDB=true
```

然后正常运行项目：
```bash
./gradlew runGraphCreation -PfileName="./src/main/java/Actor_relationship_game/actorGraph.ser"
```

### 方法2：使用系统属性

在运行Gradle任务时添加系统属性：

```bash
./gradlew runGraphCreation -PfileName="./src/main/java/Actor_relationship_game/actorGraph.ser" -Duse.mock.tmdb=true
```

### 方法3：在测试中设置

如果你在编写测试代码，可以在测试类中设置系统属性：

```java
@BeforeEach
public void setUp() {
    System.setProperty("use.mock.tmdb", "true");
}
```

**注意**：Java中无法在运行时设置环境变量，只能设置系统属性。

## 📊 模拟数据说明

测试模式使用的模拟数据包括：

### 演员列表（5位演员）：
- Tom Hanks (ID: 123)
- Leonardo DiCaprio (ID: 456)
- Brad Pitt (ID: 789)
- Meryl Streep (ID: 101)
- Robert De Niro (ID: 202)

### 电影关系：
- **Tom Hanks** 出演：Forrest Gump, Cast Away, The Terminal
- **Leonardo DiCaprio** 出演：Titanic, Inception, Cast Away（与Tom Hanks有连接）
- **Brad Pitt** 出演：Fight Club, Ocean's Eleven, The Terminal（与Tom Hanks有连接）
- **Meryl Streep** 出演：The Devil Wears Prada, Mamma Mia!, Ocean's Eleven（与Brad Pitt有连接）
- **Robert De Niro** 出演：Taxi Driver, Goodfellas, Fight Club（与Brad Pitt有连接）

这些数据创建了一个**连接图**，可以用来测试演员关系查找功能。

## 🧪 运行测试

### 单元测试

```bash
# 设置测试模式
export USE_MOCK_TMDB=true  # Linux/Mac
# 或
set USE_MOCK_TMDB=true     # Windows CMD
$env:USE_MOCK_TMDB = "true"  # Windows PowerShell

# 运行测试
./gradlew test
```

### 验收测试

```bash
# 设置测试模式
export USE_MOCK_TMDB=true

# 运行验收测试
./gradlew acceptanceTest
```

## ⚠️ 注意事项

1. **默认行为**：如果不设置 `USE_MOCK_TMDB=true`，代码会尝试使用真实的TMDB API（需要API密钥）

2. **API密钥**：使用测试模式时，**不需要**设置 `TMDB_API_KEY` 环境变量

3. **数据一致性**：每次运行测试模式，返回的数据都是相同的，这有助于测试的稳定性

4. **修改模拟数据**：如果需要不同的测试数据，可以编辑 `TMDBApi.java` 中的 `getMockPopularActors()` 和 `getMockMoviesByActorId()` 方法

## 🔄 切换回真实API

如果想使用真实的TMDB API：

1. **移除测试模式设置**：
   ```bash
   unset USE_MOCK_TMDB  # Linux/Mac
   # 或关闭终端窗口（Windows）
   ```

2. **设置真实的API密钥**：
   ```bash
   export TMDB_API_KEY="your_real_api_key"
   ```

3. **正常运行项目**

## 📝 完整示例

### Windows PowerShell完整流程：

```powershell
# 1. 启用测试模式
$env:USE_MOCK_TMDB = "true"

# 2. 创建图
./gradlew runGraphCreation -PfileName="./src/main/java/Actor_relationship_game/actorGraph.ser"

# 3. 生成演员列表
./gradlew runActorGraphUtil -PgraphPath="./src/main/java/Actor_relationship_game/actorGraph.ser" -PfilePath="./src/main/java/Actor_relationship_game/actors_list.txt"

# 4. 运行游戏界面
./gradlew runGameplayInterface -PgraphPath="./src/main/java/Actor_relationship_game/actorGraph.ser" -PactorPath="./src/main/java/Actor_relationship_game/actors_list.txt" -PfilePath="./src/main/java/Actor_relationship_game/actor_connection_results.txt"

# 5. 运行测试
./gradlew test
./gradlew acceptanceTest
```

### Linux/Mac完整流程：

```bash
# 1. 启用测试模式
export USE_MOCK_TMDB=true

# 2. 创建图
./gradlew runGraphCreation -PfileName="./src/main/java/Actor_relationship_game/actorGraph.ser"

# 3. 生成演员列表
./gradlew runActorGraphUtil -PgraphPath="./src/main/java/Actor_relationship_game/actorGraph.ser" -PfilePath="./src/main/java/Actor_relationship_game/actors_list.txt"

# 4. 运行游戏界面
./gradlew runGameplayInterface -PgraphPath="./src/main/java/Actor_relationship_game/actorGraph.ser" -PactorPath="./src/main/java/Actor_relationship_game/actors_list.txt" -PfilePath="./src/main/java/Actor_relationship_game/actor_connection_results.txt"

# 5. 运行测试
./gradlew test
./gradlew acceptanceTest
```

## 🎓 理解项目流程

1. **GraphCreation**：使用API（或模拟数据）创建演员-电影关系图，保存为 `.ser` 文件
2. **ActorGraphUtil**：从 `.ser` 文件中读取图，提取所有演员名字，保存到文本文件
3. **GameplayInterface**：从 `.ser` 文件加载图，查找任意两个演员之间的最短路径

**关键点**：只有第一步需要API，后续步骤都使用序列化的图文件！

## ❓ 常见问题

**Q: 测试模式的数据足够测试所有功能吗？**  
A: 是的，模拟数据包含了5个演员和多个电影，创建了完整的连接关系，足以测试所有核心功能。

**Q: 可以修改模拟数据吗？**  
A: 可以！编辑 `TMDBApi.java` 中的模拟数据方法即可。

**Q: 测试会通过吗？**  
A: 单元测试和大部分功能测试应该可以通过。验收测试可能需要根据实际测试要求调整。

**Q: 这个修改会影响原有功能吗？**  
A: 不会。默认情况下（不设置USE_MOCK_TMDB），代码行为与原来完全一致。

