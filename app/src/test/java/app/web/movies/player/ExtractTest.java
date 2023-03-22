package app.web.movies.player;

import java.util.Map;
import java.util.stream.Stream;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.restassured.RestAssured;

import static org.hamcrest.Matchers.matchesRegex;

public class ExtractTest {
  private record ExtractorInput(
      String type,
      String url,
      Map<String, String> params,
      Matcher<String> matcher) {
  }

  @BeforeAll
  static public void init() {
    RestAssured.baseURI = "https://4km8nxaf60.execute-api.eu-north-1.amazonaws.com/prod";
  }

  @ParameterizedTest
  @MethodSource
  public void shoudlReturnValidLocation(ExtractorInput input) {
    var spec = RestAssured
        .given()
        .queryParam("type", input.type)
        .queryParam("url", input.url);

    if (input.params != null) {
      input.params.forEach((key, value) -> {
        spec.queryParams(key, value);
      });
    }

    spec.when()
        .redirects().follow(false)
        .log().all()
        .get("/api/extract")
        .then()
        .log().all()
        .statusCode(302)
        .header("Location", input.matcher);
  }

  public static Stream<ExtractorInput> shoudlReturnValidLocation() {
    return Stream.of(
        new ExtractorInput(
            "animego_kodik",
            "//kodik.info/seria/176323/2440f5eba2ac94913ed6ebe6183435cd/720p?translations=false&min_age=16",
            null,
            matchesRegex("^https://cloud\\.kodik\\-cdn\\.com/animetvseries(?:/[^/?#]+)+\\.m3u8$")),
        new ExtractorInput(
            "anigit",
            "",
            Map.of(
                "thash", "ebab6ace632a287b6e006add9324beae",
                "tid", "46583",
                "ttype", "video"),
            matchesRegex("^https://cloud\\.kodik\\-storage\\.com/useruploads(?:/[^/?#]+)+\\.m3u8$")),
        new ExtractorInput(
            "anigit",
            "747",
            Map.of(
                "thash", "4686dac6d584726ffe13845c62ca241e",
                "tid", "61627",
                "ttype", "season"),
            matchesRegex("^https://cloud\\.kodik\\-storage\\.com/useruploads(?:/[^/?#]+)+\\.m3u8$")),
        new ExtractorInput(
            "ashdi",
            "https://ashdi.vip/vod/62469",
            null,
            matchesRegex("^https://[a-z0-9\\.]+(?:/[^/?#]+)+\\.m3u8$")),
        new ExtractorInput(
            "tortuga",
            "https://tortuga.wtf/vod/79962",
            null,
            matchesRegex("^https://[a-z]+\\.tortuga\\.wtf/hls(?:/[^/?#]+)+/index\\.m3u8$")),
        new ExtractorInput(
            "kinogo",
            "https://vid1663085134.vb17123filippaaniketos.pw/serial/fc63a42a5faf9513fbd36eb9704bece94f0e85bbeb1fd144e76263e2e59d417e/iframe",
            Map.of(
                "file", "0,1,1"),
            matchesRegex("^https://[a-z0-9\\.\\-]+(?:/[^/?#]+)+\\.m3u8$")),
        new ExtractorInput(
          "m3u8proxy", 
            "https://tortuga.wtf/vod/27402", 
           null, 
           matchesRegex("^https://[a-z]+\\.tortuga\\.wtf/hls(?:/[^/?#]+)+/index\\.m3u8$"))
            
    );
  }
}
