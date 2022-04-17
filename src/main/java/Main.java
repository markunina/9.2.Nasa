import com.google.gson.Gson;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class Main {

    public static final String REMOTE_SERVICE_URL = "https://api.nasa.gov/planetary/apod?api_key=U9X5cAwMJAfchimwKFGhak1enrClXHsHh6sPvStY";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
        CloseableHttpResponse response = httpClient.execute(request);

        String body = new String(response.getEntity().getContent().readAllBytes());
        Gson gson = new Gson();
        ApiNasa apiNasa = gson.fromJson(body, ApiNasa.class);

        String url = apiNasa.getHdurl();
        writeImage(url);

    }

    public static void writeImage(String urlName){
        BufferedImage image = null;
        String[] urlArray = urlName.split("/");
        String fileName = urlArray[urlArray.length - 1];
        try {
            URL url = new URL(urlName);
            image = ImageIO.read(url);
            ImageIO.write(image, "jpg",new File("C:\\Netology\\3.JavaCore\\9.2.Nasa\\"+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
