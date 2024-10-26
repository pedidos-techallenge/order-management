package br.com.fiap.techchallange.ordermanagement.infrastructure.controller.integration;



import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.InputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.DisabledIf;

import static br.com.fiap.techchallange.ordermanagement.util.OrderHelper.generateOrderDTO;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisabledIf(expression = "#{environment['spring.database.sql.enabled'] == 'true'}")
public class OrderManagementITNoSQL {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    public class Atendimento{
        @Test
        public void deveCriarPedido(){
            InputDataOrderDTO inputDataOrderDTO = generateOrderDTO();
            OutputDataOrderDTO outputDataOrderDTO = new OutputDataOrderDTO(
                    inputDataOrderDTO.id(),
                    0,
                    "Open"
            );

            given()
                    .header("Content-Type", "application/json")
                    .body(inputDataOrderDTO)
            .when()
                    .post("/v1/order/checkout")
            .then()
                    .statusCode(HttpStatus.CREATED.value())
                            .body("id", equalTo(outputDataOrderDTO.id())
                    );
        }
    }

    @Nested
    public class Operacao{

        @Test
        public void deveColocarStatusPedidoEmPreparacao(){
            // GIVEN
            given()
                    .header("Content-Type", "application/json")
            .when()
                    .put("/v1/order/operation/preparation/10")
            .then()
                    .statusCode(HttpStatus.ACCEPTED.value());
        }

        @Test
        public void deveColocarStatusPedidoParaComidaPronta(){
            // GIVEN
            given()
                    .header("Content-Type", "application/json")
            .when()
                    .put("/v1/order/operation/done/11")
            .then()
                    .statusCode(HttpStatus.ACCEPTED.value());
        }

        @Test
        public void deveLancarExcecaoMudancaDeStatusNaoPermitida() {
            // GIVEN
            given()
                    .header("Content-Type", "application/json")
            .when()
                    .put("/v1/order/operation/done/11")
            .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("message", equalTo("Sequencia do status do pedido violado"));
        }
    }

    @Nested

    public class Finalizacao{

        @Test
        public void deveColocarStatusPedidoParaFinalizado()  {
            // GIVEN
            given()
                    .header("Content-Type", "application/json")
            .when()
                    .put("/v1/order/finished/12")
            .then()
                    .statusCode(HttpStatus.ACCEPTED.value());
        }

        @Test
        public void deveLancarExcecaoMudancaDeStatusNaoPermitida() {
            // GIVEN
            given()
                    .header("Content-Type", "application/json")
            .when()
                    .put("/v1/order/finished/10")
            .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("message", equalTo("Sequencia do status do pedido violado"));
        }
    }

    @Nested
    public class Rastreamento{

        @Test
        public void deveBuscarListaPedidosEmAtendimento() {
            // GIVEN
            given()
                    .header("Content-Type", "application/json")
            .when()
                    .get("/v1/order/tracker")
            .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", hasItems(
                            "defa318d-d6e5-4184-8c15-9c50c4475844",
                            "defa318d-d6e5-4184-8c15-9c50c4475843",
                            "defa318d-d6e5-4184-8c15-9c50c4475842"
                    ))
                    // Verifica os números dos pedidos
                    .body("number_order", hasItems(12, 11, 10))
                    // Verifica os status dos pedidos
                    .body("status", hasItems("Pronto", "EmPreparacao", "Recebido"));
        }
    }

}
