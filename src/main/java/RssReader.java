import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RssReader {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        List<URL> sources = new ArrayList<>();
        sources.add(new URL("https://www.agenda.ad/rss/que-fem-avui"));
        sources.add(new URL("https://www.agenda.ad/rss/que-fem-aquesta-setmana"));
        sources.add(new URL("https://www.agenda.ad/rss/musica"));
        sources.add(new URL("https://www.agenda.ad/rss/teatre-i-dansa"));
        sources.add(new URL("https://www.agenda.ad/rss/exposicions"));
        sources.add(new URL("https://www.agenda.ad/rss/lletres"));
        sources.add(new URL("https://www.agenda.ad/rss/conferencies"));
        sources.add(new URL("https://www.agenda.ad/rss/festes-i-tradicions"));
        sources.add(new URL("https://www.agenda.ad/rss/cinema"));
        sources.add(new URL("https://www.agenda.ad/rss/activitats-infantils"));
        sources.add(new URL("https://www.agenda.ad/rss/esdeveniments-esportius"));
        sources.add(new URL("https://www.agenda.ad/rss/mes-cultura"));
        sources.add(new URL("https://www.agenda.ad/rss/cursos-i-tallers"));
        sources.add(new URL("https://www.agenda.ad/rss/fires-i-mercats"));

        final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        final List<Future<List<News>>> futures = new ArrayList<>();
        for (URL site : sources) {
            futures.add(threadPool.submit(new GetNewsFromSite(site)));
        }

        final List<News> result = new ArrayList<>();
        for (Future<List<News>> future : futures) {
            result.addAll(future.get());
        }

        System.out.println(result.size());

        ElasticSearchAgent agent = new ElasticSearchAgent();
        agent.addNews(result);
        List<News> similar = agent.getSimilarNews(result.get(0), 5);
        System.out.println(similar);
        agent.shutDown();
    }
}
