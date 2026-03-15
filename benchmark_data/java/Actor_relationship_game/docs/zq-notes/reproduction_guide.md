# Actor_relationship_game 代码生成复现指南

## 1. 项目概述

**Actor_relationship_game** 是一个基于 TMDB API 的演员关系探索游戏，用户可以通过该应用探索演员之间通过电影合作的关联关系，并找到任意两位演员之间的最短连接路径。

### 项目核心功能
- 从 TMDB API 获取演员和电影数据
- 构建演员关系图（ActorGraph）
- 查找演员之间的最短连接路径

### 代码结构
```
Actor_relationship_game/
├── src/
│   ├── main/java/Actor_relationship_game/
│   │   ├── Actor.java           # 演员实体类
│   │   ├── Movie.java           # 电影实体类
│   │   ├── ActorGraph.java      # 演员关系图
│   │   ├── ActorGraphUtil.java  # 图工具类
│   │   ├── GameplayInterface.java # 游戏交互界面
│   │   ├── GraphCreation.java   # 图创建模块
│   │   └── TMDBApi.java         # TMDB API 封装
│   ├── test/java/               # 单元测试
│   └── acceptanceTest/java/     # 验收测试
├── docs/                        # 项目文档（输入）
│   ├── PRD.md                   # 产品需求文档
│   ├── UML_class.md             # 类图设计
│   ├── UML_sequence.md          # 序列图设计
│   └── architecture_design.md   # 架构设计
├── build.gradle                 # Gradle 构建配置
├── repo_config.json             # 实验配置文件
└── examples/                    # 使用示例
```

---

## 2. 实验输入

代码生成实验的输入文件位于 `docs/` 目录和 `repo_config.json`：

### 2.1 核心输入文件

| 文件 | 路径 | 说明 |
|------|------|------|
| PRD | `docs/PRD.md` | 产品需求文档，定义功能需求和技术约束 |
| UML类图 | `docs/UML_class.md` | 类结构设计，包含属性和方法 |
| UML序列图 | `docs/UML_sequence.md` | 系统交互流程设计 |
| 架构设计 | `docs/architecture_design.md` | 项目文件结构和模块职责 |
| 实验配置 | `repo_config.json` | 定义测试脚本、依赖关系、代码DAG等 |

### 2.2 repo_config.json 关键配置

```json
{
    "PRD": "docs/PRD.md",
    "UML_class": "docs/UML_class.md",
    "UML_sequence": "docs/UML_sequence.md",
    "architecture_design": "docs/architecture_design.md",
    "language": "java",
    "unit_tests": "src/test/java/Actor_relationship_game",
    "acceptance_tests": "src/acceptanceTest/java/Actor_relationship_game",
    "unit_test_script": "./gradlew test",
    "acceptance_test_script": "./gradlew acceptanceTest",
    "code_file_DAG": { ... },      // 代码文件依赖关系
    "unit_test_linking": { ... }   // 测试与源文件对应关系
}
```

### 2.3 第三方依赖（build.gradle:37-45）

| 依赖 | 版本 | 用途 |
|------|------|------|
| `junit-jupiter` | 5.8.1 | 单元测试框架 |
| `okhttp` | 4.9.0 | HTTP 客户端（TMDB API 调用） |
| `gson` | 2.8.9 | JSON 解析 |
| `mockito-core` | 4.11.0 | Mock 测试框架 |

---

## 3. 环境配置

### 3.1 基础环境要求

| 组件 | 版本要求 | 说明 |
|------|---------|------|
| Java | 1.8+ | 运行环境 |
| Gradle | 8.2 | 构建工具 |
| Python | 3.8+ | 运行 agent 系统 |
| TMDB API Key | - | 从 https://developer.themoviedb.org 获取 |

### 3.2 配置步骤

#### 步骤 1：设置 Python 环境
```bash
# 添加项目路径到 PYTHONPATH
export PYTHONPATH="${PYTHONPATH}:${path_to_devbench}"
```

#### 步骤 2：设置 TMDB API Key
```bash
export TMDB_API_KEY="your_tmdb_api_key"
```

#### 步骤 3：设置 OpenAI API Key（用于 LLM）
```bash
export OPENAI_API_KEY="your_openai_api_key"
```

#### 步骤 4：开源模型配置（可选）
如使用开源模型，配置 `agent_system/baseline/open_source_config.json`：
```json
{
    "codellama-7b-instruct": "${model_ip_address}",
    "deepseek-coder-33b-instruct": "${model_ip_address}"
}
```

### 3.3 Windows 环境注意事项

在 Windows 上运行时，需要在 PowerShell 中设置环境变量：
```powershell
$env:PYTHONPATH = "${env:PYTHONPATH};E:\CodeBase\DevEval-java"
$env:TMDB_API_KEY = "your_tmdb_api_key"
$env:OPENAI_API_KEY = "your_openai_api_key"
```

---

## 4. 实验运行

### 4.1 运行 Agent 系统进行代码生成

```bash
cd agent_system/baseline

python run.py \
    --config Implementation \
    --input_path ../../benchmark_data/java/Actor_relationship_game/ \
    --model gpt-4-turbo-new \
    --model_source openai \
    --review execution \
    --evaluate
```

### 4.2 参数说明

| 参数 | 可选值 | 说明 |
|------|--------|------|
| `--config` | `SoftwareDesign`, `EnvironmentSetup`, `Implementation`, `AcceptanceTesting`, `UnitTesting` | 任务类型 |
| `--input_path` | 项目路径 | 输入项目目录 |
| `--model` | `gpt-3.5-turbo`, `gpt-4-turbo`, `deepseek-coder-33b` 等 | LLM 模型 |
| `--model_source` | `openai`, `open_source` | 模型来源 |
| `--review` | `none`, `normal`, `execution` | 代码审查模式 |
| `--evaluate` | - | 运行后执行评估 |

### 4.3 Review 模式说明

| 模式 | 描述 |
|------|------|
| `none` | 单次代码生成，无审查 |
| `normal` | 代码生成 + 审查交替，无执行反馈 |
| `execution` | 代码生成 + 审查交替，含执行反馈（推荐） |

### 4.4 任务类型说明

| 任务类型 | 说明 |
|----------|------|
| `SoftwareDesign` | 软件设计任务 |
| `EnvironmentSetup` | 环境配置任务 |
| `Implementation` | 代码实现任务（核心） |
| `AcceptanceTesting` | 验收测试生成 |
| `UnitTesting` | 单元测试生成 |

---

## 5. 实验输出与测试

### 5.1 输出位置

代码生成结果保存在：
```
agent_system/baseline/WareHouse_results/
└── Implementation_${model}/${review}/
    └── ${timestamp}_${project_name}/
        ├── src/main/java/...    # 生成的源代码
        ├── logs/                 # 运行日志
        └── ...
```

### 5.2 测试方法

#### 方法 1：通过 Agent 系统自动测试

使用 `--evaluate` 参数，系统会自动执行：
- 单元测试：`./gradlew test`
- 验收测试：`./gradlew acceptanceTest`

#### 方法 2：手动测试

```bash
# 进入生成代码目录
cd WareHouse_results/Implementation_xxx/

# 1. 构建项目
./gradlew build

# 2. 运行单元测试
./gradlew test

# 3. 运行验收测试
./gradlew acceptanceTest

# 4. 生成测试覆盖率报告
./gradlew jacocoTestReport
```

#### 方法 3：运行完整流程

```bash
# 1. 创建演员关系图
./gradlew runGraphCreation -PfileName="./actorGraph.ser"

# 2. 提取演员列表
./gradlew runActorGraphUtil -PgraphPath="./actorGraph.ser" -PfilePath="./actors_list.txt"

# 3. 查找演员连接
./gradlew runGameplayInterface -PgraphPath="./actorGraph.ser" -PactorPath="./actors_list.txt" -PfilePath="./results.txt"
```

### 5.3 测试验证点

#### 单元测试（7个测试文件）

| 测试文件 | 验证内容 |
|----------|----------|
| `ActorTest.java` | Actor 类属性（ID, name, movieIds） |
| `MovieTest.java` | Movie 类属性（ID, title, actorIds） |
| `ActorGraphTest.java` | 图操作（添加演员/电影、查找连接） |
| `ActorGraphUtilTest.java` | 图序列化/反序列化 |
| `GameplayInterfaceTest.java` | 游戏界面与图交互 |
| `GraphCreationTest.java` | 图创建和文件生成 |
| `TMDBApiTest.java` | TMDB API 集成 |

#### 验收测试（ActorRelationshipAcceptanceTest.java）

| 测试方法 | 验证内容 |
|----------|----------|
| `testGeneratedFiles()` | 文件生成存在性检查 |
| `testActorGraph()` | 序列化图文件一致性 |
| `testActorList()` | 演员列表准确性 |
| `testConnectionResults()` | 连接结果正确性 |

### 5.4 测试成功标准

1. **单元测试**：所有测试用例通过
   ```
   BUILD SUCCESSFUL
   Test Summary: 100% passed
   ```

2. **验收测试**：
   - `actorGraph.ser` 文件生成成功
   - `actors_list.txt` 包含有效演员列表
   - `actor_connection_results.txt` 包含正确的连接路径

3. **代码覆盖率**：通过 JaCoCo 生成报告
   ```
   ./gradlew jacocoTestReport
   # 查看 build/reports/jacoco/test/html/index.html
   ```

---

## 6. 完整复现流程

### 6.1 准备工作

```bash
# 1. 设置环境变量
export PYTHONPATH="${PYTHONPATH}:$(pwd)"
export TMDB_API_KEY="your_key"
export OPENAI_API_KEY="your_key"

# 2. 验证环境
java -version
gradle -version
python --version
```

### 6.2 执行代码生成

```bash
cd agent_system/baseline

# 方式一：直接实现任务
python run.py \
    --config Implementation \
    --input_path ../../benchmark_data/java/Actor_relationship_game/ \
    --model gpt-4-turbo-new \
    --model_source openai \
    --review execution \
    --evaluate

# 方式二：分阶段执行
# Step 1: 软件设计
python run.py --config SoftwareDesign --input_path ../../benchmark_data/java/Actor_relationship_game/ --model gpt-4-turbo-new --model_source openai

# Step 2: 环境配置
python run.py --config EnvironmentSetup --input_path ../../benchmark_data/java/Actor_relationship_game/ --model gpt-4-turbo-new --model_source openai

# Step 3: 代码实现
python run.py --config Implementation --input_path ../../benchmark_data/java/Actor_relationship_game/ --model gpt-4-turbo-new --model_source openai --review execution --evaluate
```

### 6.3 验证结果

```bash
# 进入输出目录
cd WareHouse_results/Implementation_gpt-4-turbo-new/execution/${timestamp}/

# 运行测试
./gradlew test acceptanceTest

# 检查测试报告
cat build/reports/tests/test/index.html
```

---

## 7. 常见问题

### Q1: TMDB API 调用失败
- 检查 `TMDB_API_KEY` 环境变量是否正确设置
- 确认 API Key 有效且有足够的调用配额

### Q2: Gradle 构建失败
- 检查 Java 版本是否为 1.8+
- 运行 `./gradlew clean build` 清理后重新构建

### Q3: 单元测试超时
- TMDB API 测试需要网络连接，可能需要增加超时时间
- 可考虑使用 Mock 数据替代真实 API 调用

### Q4: Windows 环境下脚本执行
- 使用 `gradlew.bat` 代替 `./gradlew`
- 或在 Git Bash / WSL 中运行

---

## 8. 参考链接

- [DevBench 论文](https://arxiv.org/abs/2403.08604)
- [TMDB API 文档](https://developer.themoviedb.org/docs/getting-started)
- [ChatDev 项目](https://github.com/OpenBMB/ChatDev)
