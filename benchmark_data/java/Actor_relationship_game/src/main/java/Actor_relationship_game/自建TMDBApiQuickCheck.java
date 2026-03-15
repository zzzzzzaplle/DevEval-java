package Actor_relationship_game;

/**
 * 一个用于快速验证 TMDB_API_KEY 是否配置正确、TMDB API 是否可访问的简单工具类。
 *
 * 使用方式（Windows PowerShell 示例）：
 *
 * 1. 在项目根目录下进入当前子项目目录：
 *    cd E:\CodeBase\DevEval-java\benchmark_data\java\Actor_relationship_game
 *
 * 2. 设置环境变量（将 your_api_key_here 替换为你在 TMDB 上申请的 API Key）：
 *    $env:TMDB_API_KEY = "your_api_key_here"
 *
 * 3. 在 IDE 中右键运行本类的 main 方法，或配置一个运行配置：
 *    运行 TMDBApiQuickCheck.main()
 *
 * 程序会：
 * - 调用 searchPopularActors()，打印返回 JSON 的前一部分内容和长度
 * - 使用一个示例演员 ID 调用 getMoviesByActorId() 并打印返回内容的前一部分和长度
 *
 * 只要没有抛出 401/403 之类的错误，并且能看到正常的 JSON 输出，就说明你的 TMDB_API_KEY 是可用的。
 */
public class TMDBApiQuickCheck {

    public static void main(String[] args) {
        // 1. 检查环境变量是否设置
        String apiKey = System.getenv("TMDB_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("环境变量 TMDB_API_KEY 未设置，请先在终端或系统环境变量中设置后再运行。");
            return;
        } else {
            System.out.println("检测到 TMDB_API_KEY 已设置。");
        }

        TMDBApi api = new TMDBApi();

        try {
            // 2. 调用 searchPopularActors() 测试
            System.out.println("调用 TMDBApi.searchPopularActors()...");
            String popularJson = api.searchPopularActors();
            System.out.println("searchPopularActors 返回内容长度: " + popularJson.length());
            System.out.println("searchPopularActors 返回内容前 500 个字符：");
            System.out.println(popularJson.substring(0, Math.min(500, popularJson.length())));

            // 3. 调用 getMoviesByActorId() 测试
            // 这里使用一个示例 ID。实际中可以替换为你想测试的演员 ID。
            String exampleActorId = "500";
            System.out.println("\n调用 TMDBApi.getMoviesByActorId(\"" + exampleActorId + "\")...");
            String moviesJson = api.getMoviesByActorId(exampleActorId);
            System.out.println("getMoviesByActorId 返回内容长度: " + moviesJson.length());
            System.out.println("getMoviesByActorId 返回内容前 500 个字符：");
            System.out.println(moviesJson.substring(0, Math.min(500, moviesJson.length())));

            System.out.println("\nTMDBApiQuickCheck 运行完成。如果没有异常且输出为正常 JSON，则说明 TMDB_API_KEY 配置正确，TMDB API 可访问。");
        } catch (Exception e) {
            System.out.println("调用 TMDB API 过程中发生异常：");
            e.printStackTrace();
        }
    }
}


