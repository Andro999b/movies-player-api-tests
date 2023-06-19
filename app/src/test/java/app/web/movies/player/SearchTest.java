package app.web.movies.player;

import io.restassured.module.jsv.JsonSchemaValidator;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import io.restassured.RestAssured;

class SearchTest {

  @BeforeAll
  static public void init() {
    RestAssured.baseURI = "https://4km8nxaf60.execute-api.eu-north-1.amazonaws.com/prod";
  }

  @ParameterizedTest
  @CsvSource({
      "uafilmtv,Енола",
      "uakinoclub,Термінатор",
      "uaserials,Зоряна брама",
      "kinogo,Терминатор",
      // "kinovod,Терминатор",
      "eneyida,Гріфіни",
      "gidonline,гриффины",
      "gidonline,арчер",
      "videocdn,Терминатор",
      "videocdn,Время приключений",
      "videocdn,Топ гир",
      "animego,My Hero Academy",
      "anitubeua,Зошит смерті",
      "animelib,намбака",
      "animeuaclub,шпигун",
      // "anigato,Death Note",
      // "animevost,Тетрадь смерти",
      // "animedia,Дорохедоро",
      "rezka,Гріфіни"
  })
  void shouldReturnsValidsSearchResults(String provider, String query) {
    RestAssured
        .given()
        .pathParam("provider", provider)
        .queryParam("q", query)
        .when()
        .log().all()
        .get("/api/trackers/{provider}/search")
        .then()
        .log().all()
        .statusCode(200)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/search.json"))
        .body("provider", everyItem(is(provider)));
  }
}
