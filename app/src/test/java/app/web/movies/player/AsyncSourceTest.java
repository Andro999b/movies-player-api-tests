package app.web.movies.player;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;

public class AsyncSourceTest {

  private record SourceInput(
      String provider,
      String resultId,
      String sourceId,
      Map<String, String> params,
      String schema) {
  }

  @BeforeAll
  static public void init() {
    RestAssured.baseURI = "https://4km8nxaf60.execute-api.eu-north-1.amazonaws.com/prod";
  }

  @ParameterizedTest
  @MethodSource
  public void souldReturnFile(SourceInput input) {
    var spec = RestAssured
        .given()
        .pathParam("provider", input.provider)
        .pathParam("resultId", input.resultId)
        .pathParam("sourceId", input.sourceId);

    if (input.params != null) {
      input.params.forEach((key, value) -> {
        spec.queryParams(key, value);
      });
    }

    spec.when()
        .redirects().follow(false)
        .log().all()
        .get("/api/trackers/{provider}/items/{resultId}/source/{sourceId}")
        .then()
        .log().all()
        .statusCode(200)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/source/" + input.schema + ".json"));
  }

  public static Stream<SourceInput> souldReturnFile() {
    return Stream.of(
        new SourceInput(
            "animego",
            "plamennaya-brigada-pozharnyh-2-1586",
            "20983",
            Map.of("ep", "1"),
            "animego")
        // new SourceInput(
        //     "animevost",
        //     "https://animevost.org/tip/tv/468-full-metal-panic.html",
        //     "2147397791",
        //     null,
        //     "animevost")
      );
  }
}
