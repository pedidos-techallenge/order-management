package br.com.fiap.techchallange.ordermanagement.bdd;

import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.InputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.util.OrderHelper;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class StepDefinition {

    private Response response;

    private InputDataOrderDTO inputDataOrderDTO;

    private SharedData sharedData;

    @Autowired
    private Environment env;

    public StepDefinition(){}

    @Autowired
    public StepDefinition(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    private String ENDPOINT_ORDERS = "http://localhost:8080/v1/order";

    @Quando("enviar o pedido para processamento")
    public Response enviar_o_pedido_para_processamento() {
        inputDataOrderDTO = OrderHelper.generateOrderDTO();
        this.sharedData.setIdOrder(inputDataOrderDTO.id());
        response = given()
                .header("Content-Type", "application/json")
                .body(inputDataOrderDTO)
                .when()
                .post(ENDPOINT_ORDERS + "/checkout");

        return response;
    }

    @Entao("o pedido deve ser registrado no sistema")
    public void pedido_deve_ser_registrado_no_sistema() {
        OrderViewModel orderViewModel = response.then().extract().as(OrderViewModel.class);
        assertTrue(orderViewModel.status().equals("Aberto"));
        response.then().statusCode(201);
    }

    @Quando("receber uma notificação de pagamento")
    public void receber_notificacao_de_pagamento() throws InterruptedException {
        SendMessageResult result = null;
        if (sharedData.getIdOrder() != null) {
             result = send_message(sharedData.getIdOrder(), "'PAID'");        }

        assertFalse(Objects.requireNonNull(result).getMessageId().isBlank());

        Thread.sleep(2000);
    }

    @Entao("o pedido deverá ser atualizado para Recebido")
    public void pedido_atualizado_recebido(){
        response = given()
                .when()
                .get(ENDPOINT_ORDERS + "/" + sharedData.getIdOrder() +"/tracker");

        OrderViewModel orderViewModel = response.then().extract().as(OrderViewModel.class);
        assertEquals("Recebido", (orderViewModel.status()));
        assertEquals(200, (response.getStatusCode()));
        this.sharedData.setNumber_order(orderViewModel.number_order());
    }

    @Quando("atendente clicar no pedido")
    public void atendente_clicar_pedido() throws InterruptedException {
        response = given()
                  .when()
                    .put(ENDPOINT_ORDERS + "/operation/preparation/" + sharedData.getNumber_order());
        assertEquals(202, (response.getStatusCode()));
        Thread.sleep(2000);
    }


    @Entao("sistema deverá colocar pedido para Em Preparação")
    public void sistema_deverá_colocar_pedido_para_em_preparacao() {
        response = given()
                .when()
                .get(ENDPOINT_ORDERS + "/" + sharedData.getIdOrder() +"/tracker");

        assertEquals("EmPreparacao", (response.then().extract().as(OrderViewModel.class).status()));
        assertEquals(200, (response.getStatusCode()));
    }

    @Quando("atendente informar término de preparação")
    public void atendente_informar_término_de_preparacao() {
        response = given()
                .when()
                .put(ENDPOINT_ORDERS + "/operation/done/" + sharedData.getNumber_order());
        assertEquals(202, (response.getStatusCode()));
    }

    @Entao("sistema deverá colocar pedido para Pronto")
    public void sistema_deverá_colocar_pedido_para_pronto() {
        response = given()
                .when()
                .get(ENDPOINT_ORDERS + "/" + sharedData.getIdOrder() +"/tracker");

        assertEquals("Pronto", (response.then().extract().as(OrderViewModel.class).status()));
        assertEquals(200, (response.getStatusCode()));
    }

    @Quando("atendente entregar produto para o cliente")
    public void atendente_entregar_produto_para_o_cliente() {
        response = given()
                .when()
                .put(ENDPOINT_ORDERS + "/finished/" + sharedData.getNumber_order());
        assertEquals(202, (response.getStatusCode()));
    }
    @Entao("sistema deverá colocar pedido para Finalizado")
    public void sistema_deverá_colocar_pedido_para_finalizado() {
        response = given()
                .when()
                .get(ENDPOINT_ORDERS + "/" + sharedData.getIdOrder() +"/tracker");

        assertEquals("Finalizado", (response.then().extract().as(OrderViewModel.class).status()));
        assertEquals(200, (response.getStatusCode()));
    }

    @Quando("receber uma notificação de pagamento negado")
    public void receber_uma_notificação_de_pagamento_negado() throws InterruptedException {
        response = enviar_o_pedido_para_processamento();
        OrderViewModel orderViewModel = response.then().extract().as(OrderViewModel.class);
        assertTrue(orderViewModel.status().equals("Aberto"));
        response.then().statusCode(201);

        SendMessageResult result = null;
        if (sharedData.getIdOrder() != null) {
            result = send_message(sharedData.getIdOrder(), "'DENIED'");
        }

        assertFalse(Objects.requireNonNull(result).getMessageId().isBlank());

        Thread.sleep(1000);


    }
    @Entao("o pedido deverá ser atualizado para Cancelado")
    public void o_pedido_deverá_ser_atualizado_para_cancelado() {
        response = given()
                .when()
                .get(ENDPOINT_ORDERS + "/" + sharedData.getIdOrder() +"/tracker");

        OrderViewModel orderViewModel = response.then().extract().as(OrderViewModel.class);
        assertEquals("Cancelado", (orderViewModel.status()));
        assertEquals(200, (response.getStatusCode()));
        this.sharedData.setNumber_order(orderViewModel.number_order());
    }

    private SendMessageResult send_message(String idOrder, String status){
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        String accessKey = System.getenv("AWS_ACCESS_KEY_ID") != null ?
                System.getenv("AWS_ACCESS_KEY_ID") :
                dotenv.get("AWS_ACCESS_KEY_ID");
                
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY") != null ?
                System.getenv("AWS_SECRET_ACCESS_KEY") :
                dotenv.get("AWS_SECRET_ACCESS_KEY");
                
        String sessionToken = System.getenv("AWS_SESSION_TOKEN") != null ? 
                System.getenv("AWS_SESSION_TOKEN") : 
                dotenv.get("AWS_SESSION_TOKEN");
                
        String region = System.getenv("AWS_REGION") != null ? 
                System.getenv("AWS_REGION") : 
                dotenv.get("AWS_REGION");
                
        String queueUrl = System.getenv("SQS_MAIN_QUEUE_URL") != null ? 
                System.getenv("SQS_MAIN_QUEUE_URL") : 
                dotenv.get("SQS_MAIN_QUEUE_URL");

        System.out.println("Region: " + region);
        
        if (region == null) {
            throw new IllegalStateException("AWS Region não pode ser null. Verifique a variável de ambiente AWS_REGION");
        }

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(
                    new BasicSessionCredentials(accessKey, secretKey, sessionToken)))
                .build();

        String message =  "{'idOrder': '" + idOrder + "', 'statusPayment':" + status + " }";
        SendMessageRequest sendMsgRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);

        return sqs.sendMessage(sendMsgRequest);
    }
}
