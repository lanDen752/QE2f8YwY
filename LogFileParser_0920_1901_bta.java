// 代码生成时间: 2025-09-20 19:01:19
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 日志文件解析工具类
 */
public class LogFileParser {

    /**
     * 解析日志文件并返回所有日志行
     *
     * @param logFilePath 日志文件路径
     * @return 日志行列表
     */
    public static List<String> parseLogFile(String logFilePath) {
        try {
            // 使用Files.readAllLines读取所有日志行
            return Files.readAllLines(Paths.get(logFilePath));
        } catch (IOException e) {
            // 处理文件读取异常
            System.err.println(