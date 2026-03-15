# LLM代码生成输入说明

## 📋 项目用途

这个项目是 **DevBench** 基准测试的一部分，用于评估 **LLM（大语言模型）的代码生成能力**。

## 🎯 评估流程

```
1. 输入阶段：给LLM提供需求文档、类图等
   ↓
2. 代码生成：LLM生成代码框架和方法体
   ↓
3. 测试验证：运行单元测试和验收测试
   ↓
4. 评估结果：根据测试通过率和代码覆盖率评分
```

---

## 📥 输入给LLM的内容

### 1. **需求文档（PRD.md）**
- **位置**：`docs/PRD.md`
- **内容**：
  - 项目介绍和目标
  - 功能需求（每个类的作用）
  - 技术约束
  - 依赖要求
  - 使用示例
  - 验收标准
- **作用**：告诉LLM"要做什么"

### 2. **UML类图（UML_class.md）**
- **位置**：`docs/UML_class.md`
- **内容**：
  - 所有类的定义
  - 类的属性和方法签名
  - 类之间的关系（依赖、包含等）
- **作用**：告诉LLM"类的结构是什么样的"

### 3. **UML序列图（UML_sequence.md）**
- **位置**：`docs/UML_sequence.md`
- **内容**：
  - 类之间的调用顺序
  - 方法调用的流程
- **作用**：告诉LLM"程序如何运行"

### 4. **架构设计文档（architecture_design.md）**
- **位置**：`docs/architecture_design.md`
- **内容**：
  - 项目文件结构
  - 输出说明（控制台输出、序列化文件）
  - 运行示例
  - 每个类的作用说明
- **作用**：告诉LLM"项目的整体架构"

### 5. **配置文件（repo_config.json）**
- **位置**：`repo_config.json`
- **内容**：
  - 文档路径映射
  - 测试文件位置
  - 测试与代码的关联关系
  - 代码依赖关系（DAG）
  - 测试提示词（coarse/fine prompts）
- **作用**：告诉评估系统"如何组织测试和验证"

---

## 🔧 代码框架结构

### 需要LLM填充的部分

根据你的描述，项目应该包含：

#### 1. **完整框架（已有）**
- 类定义
- 方法签名
- Getter/Setter方法（可能已实现）

#### 2. **需要填充的方法体**
以下方法的方法体需要LLM生成：

**ActorGraph.java:**
- `addActor()` - 添加演员到图
- `addMovie()` - 添加电影到图
- `addActorToMovie()` - 连接演员和电影
- `findConnectionWithPath()` - **核心算法**：使用BFS查找最短路径
- `buildPath()` - 构建路径的辅助方法

**GraphCreation.java:**
- `createGraph()` - 创建图的入口方法
- `populateGraphWithActors()` - 从API获取演员数据
- `processActorElement()` - 处理单个演员元素
- `populateGraphWithMoviesForActor()` - 获取演员的电影列表
- `processMovieElement()` - 处理单个电影元素
- `saveGraphToFile()` - 序列化保存图

**ActorGraphUtil.java:**
- `loadGraph()` - 从文件加载图
- `writeActorsToFile()` - 将演员列表写入文件

**GameplayInterface.java:**
- `loadGraph()` - 加载序列化的图
- `findConnections()` - 查找所有演员对之间的连接
- `readActorsFromFile()` - 从文件读取演员列表
- `generateAllActorPairs()` - 生成所有演员对

**TMDBApi.java:**
- `getMoviesByActorId()` - 根据演员ID获取电影
- `searchPopularActors()` - 搜索流行演员

---

## 🧪 测试验证结构

### 单元测试（Unit Tests）

**位置**：`src/test/java/Actor_relationship_game/`

**测试文件与代码的对应关系**（来自 `repo_config.json`）：

| 测试文件 | 测试的代码文件 | 测试内容 |
|---------|--------------|---------|
| `ActorTest.java` | `Actor.java` | Actor类的属性管理 |
| `MovieTest.java` | `Movie.java` | Movie类的属性管理 |
| `ActorGraphTest.java` | `ActorGraph.java` | 图的添加、查找操作 |
| `ActorGraphUtilTest.java` | `ActorGraphUtil.java` | 图的序列化/反序列化 |
| `GameplayInterfaceTest.java` | `GameplayInterface.java` | 游戏界面的集成测试 |
| `GraphCreationTest.java` | `GraphCreation.java` | 图的创建和保存 |
| `TMDBApiTest.java` | `TMDBApi.java` | API调用测试 |

**测试提示词级别**：
- **Coarse（粗粒度）**：给出测试的总体目的和依赖
- **Fine（细粒度）**：给出每个测试方法的具体要求

### 验收测试（Acceptance Tests）

**位置**：`src/acceptanceTest/java/Actor_relationship_game/`

**测试内容**：
- `testGeneratedFiles` - 验证生成的文件是否存在
- `testActorGraph` - 验证图的序列化是否正确
- `testActorList` - 验证演员列表是否正确
- `testConnectionResults` - 验证连接查找结果是否正确

---

## 📊 代码依赖关系（DAG）

来自 `repo_config.json` 的 `code_file_DAG`：

```
ActorGraph.java
  ├── Actor.java
  └── Movie.java

GraphCreation.java
  ├── ActorGraph.java
  └── TMDBApi.java

ActorGraphUtil.java
  └── ActorGraph.java

GameplayInterface.java
  └── ActorGraph.java

build.gradle
  ├── GameplayInterface.java
  ├── ActorGraphUtil.java
  └── GraphCreation.java
```

**含义**：LLM需要按照依赖顺序生成代码，先实现被依赖的类。

---

## 🔄 完整的评估流程

### 阶段1：输入准备
```
提供给LLM：
├── PRD.md（需求）
├── UML_class.md（类图）
├── UML_sequence.md（序列图）
├── architecture_design.md（架构）
└── repo_config.json（配置）
```

### 阶段2：代码生成
```
LLM需要生成：
├── 类定义（框架已有）
├── Getter/Setter（可能已有）
└── 方法体（需要填充）
    ├── 业务逻辑方法
    └── 核心算法（如BFS路径查找）
```

### 阶段3：测试验证
```bash
# 运行单元测试
./gradlew test

# 运行验收测试
./gradlew acceptanceTest

# 生成代码覆盖率报告
./gradlew jacocoTestReport
```

### 阶段4：评估指标
- **单元测试通过率**：所有单元测试是否通过
- **验收测试通过率**：端到端功能是否正常
- **代码覆盖率**：生成的代码是否被测试覆盖
- **功能正确性**：生成的文件是否符合预期

---

## 📝 关键理解点

### 1. **输入文档的作用**
- **PRD**：告诉LLM"做什么"
- **UML类图**：告诉LLM"结构是什么"
- **UML序列图**：告诉LLM"流程是什么"
- **架构设计**：告诉LLM"整体如何组织"

### 2. **代码框架 vs 方法体**
- **框架**：类定义、方法签名（可能已提供）
- **方法体**：具体的实现逻辑（LLM需要生成）

### 3. **测试的作用**
- **单元测试**：验证每个方法是否正确
- **验收测试**：验证整个系统是否工作
- **测试提示词**：帮助LLM理解测试要求

### 4. **依赖关系的重要性**
- LLM必须按照依赖顺序生成代码
- 先实现基础类（Actor, Movie）
- 再实现依赖它们的类（ActorGraph）
- 最后实现使用它们的类（GraphCreation, GameplayInterface）

---

## 🎓 实际使用示例

### 给LLM的提示词可能包含：

```
你是一个代码生成助手。请根据以下信息生成Java代码：

1. 需求文档：docs/PRD.md
2. 类图：docs/UML_class.md
3. 序列图：docs/UML_sequence.md
4. 架构设计：docs/architecture_design.md

需要实现的方法：
- ActorGraph.findConnectionWithPath(): 使用BFS算法查找两个演员之间的最短路径
- GraphCreation.createGraph(): 从TMDB API获取数据并创建图
- GameplayInterface.findConnections(): 查找所有演员对之间的连接

约束：
- 只能使用PRD中指定的依赖
- 必须通过所有单元测试
- 必须通过验收测试
```

---

## 📌 总结

这个项目作为LLM代码生成的输入，提供了：

1. **完整的需求文档** - 告诉LLM要做什么
2. **清晰的类图设计** - 告诉LLM结构是什么
3. **详细的测试用例** - 告诉LLM如何验证正确性
4. **明确的依赖关系** - 告诉LLM生成顺序
5. **代码框架** - 提供基础结构，LLM填充方法体

**评估目标**：测试LLM能否根据需求文档和设计图，生成能通过所有测试的代码。

