import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class Test1 {
    public static void main(String[] args) {
        try {
            // 1. 读取网络上的文件并解析获取图片路径
            URL url = new URL("http://10.122.7.154/javaweb/data/images-url.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            List<String> imageUrls = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                imageUrls.add(line);
            }
            reader.close();

            // 2. 创建本地文件夹
            String directoryPath = "C:/images/";
            Files.createDirectories(Paths.get(directoryPath));

            // 3. 下载图片并存储到文件夹
            for (String imageUrl : imageUrls) {
                try {
                    String imagePath = convertUrlToImagePath(imageUrl);
                    URL imageUrlObj = new URL(imageUrl);
                    InputStream inputStream = imageUrlObj.openStream();
                    Files.copy(inputStream, Paths.get(directoryPath + imagePath), StandardCopyOption.REPLACE_EXISTING);
                    inputStream.close();
                } catch (IOException e) {
                    System.err.println("源文件不存在：" + imageUrl);
                }
            }

            // 4. 对图片按大小排序并写入排序结果文件
            List<File> imageFiles = getFilesInDirectory(directoryPath);
            Collections.sort(imageFiles, Comparator.comparingLong(File::length));
            PrintWriter writer = new PrintWriter("C:/images/images-sorted.txt");
            for (File imageFile : imageFiles) {
                writer.println(imageFile.length() + " " + imageFile.getAbsolutePath());
            }
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 将图片的URL地址转换为本地保存的文件路径
    private static String convertUrlToImagePath(String url) {
        String[] parts = url.split("/");
        StringBuilder imagePath = new StringBuilder();
        for (int i = 2; i < parts.length - 1; i++) {
            imagePath.append(parts[i]).append(File.separator);
        }
        imagePath.append(parts[parts.length - 1]);
        return imagePath.toString();
    }

    // 获取指定文件夹下的所有文件
    private static List<File> getFilesInDirectory(String directoryPath) {
        List<File> files = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] fileList = directory.listFiles();
        if (fileList != null) {
            files.addAll(Arrays.asList(fileList));
        }
        return files;
    }
}