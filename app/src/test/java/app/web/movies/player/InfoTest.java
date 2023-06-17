package app.web.movies.player;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;

public class InfoTest {

  @BeforeAll
  static public void init() {
    RestAssured.baseURI = "https://4km8nxaf60.execute-api.eu-north-1.amazonaws.com/prod";
  }

  @ParameterizedTest
  @CsvSource({
      "eneyida,2301-terminator,eneyida/base",
      "eneyida,6731-vonder-tut-i-tam,eneyida/with-path",
      // "anigato,https://anigato.ru/anime_ova/1794-vanpanchmen-ova.html,anigato/one-file-multiple-audio",
      // "anigato,https://anigato.ru/anime_ova/5556-van-pis-piratskie-koroli-bejsbola.html,anigato/one-file-one-audio",
      // "anigato,https://anigato.ru/anime/8763-semja-shpiona-tv-1.html,anigato/multiple-file-multiple-audio",
      // "animevost,https://animevost.org/tip/ona/2855-kakegurui-twin.html,",
      // "animedia,dorohedoro,",
      "animego,v-les-gde-mercayut-svetlyachki-810,animego/single",
      "animego,death-note-v2-95,animego/multiple",
      "anitubeua,3342-banana-fish,anitubeua/playlist",
      "anitubeua,2816-akademya-yunih-vdom-little-witch-academia-,anitubeua/playlist",
      "anitubeua,4304-suzume-no-tojimari,anitubeua/playlist",
      "anitubeua,4110-chainsaw-man,anitubeua/playlist",
      "uafilmtv,234-mertv-jak-ja,playerjs/hls-path",
      "uafilmtv,10191-pivdenniy-park-postkovid-kovid-povertayetsya,playerjs/hls",
      "uaserials,196-zoryana-brama-sezon-1,uaserials/tv-show",
      "uaserials,6573-nastupni-365-dniv,uaserials/movie",
      "uakinoclub,https://uakino.club/filmy/genre-action/15719-enola-golms-2.html,playerjs/hls",
      "uakinoclub,https://uakino.club/animeukr/anime-series/15231-kberpank-t-scho-bzhat-po-krayu-1-sezon.html,uakinoclub/v2",
      "gidonline,terminator,gidonline/movie",
      "gidonline,griffiny,gidonline/searial",
      "kinogo,5907-terminator-1984_21_45_54,kinogo/variant1", 
      "kinogo,25073-what-we-do-in-the-shadows_2014,kinogo/variant1", 
      "videocdn,imdb_id-tt6959064,playerjs/hls-path", 
      "videocdn,imdb_id-tt0098321,playerjs/hls", 
  })
  public void shouldReturnsValidsPlaylist(String provider, String id, String schema) throws UnsupportedEncodingException {
    if (schema == null) {
      schema = provider;
    }

    RestAssured
        .given()
        .pathParam("provider", provider)
        .pathParam("id", id)
        .queryParam("nocache", "true")
        .when()
        .log().all()
        .get("/api/trackers/{provider}/items/{id}")
        .then()
        .log().all()
        .statusCode(200)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/info/" + schema + ".json"))
        .body("id", is(URLEncoder.encode(id, "utf-8")))
        .body("provider", is(provider));
  }
}
