package br.com.fiap.techchallange.ordermanagement.bdd;


import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.InputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.util.OrderHelper;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class StepDefinition {

    private Response response;

    private OutputDataOrderDTO orderResponse;

    private InputDataOrderDTO inputDataOrderDTO;


    private SharedData sharedData;

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

        Thread.sleep(1000);
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
    public void atendente_clicar_pedido() {
        response = given()
                  .when()
                    .put(ENDPOINT_ORDERS + "/operation/preparation/" + sharedData.getNumber_order());
        assertEquals(202, (response.getStatusCode()));
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
        String accessKey = "YOUR_ACCESS_KEY";
        String secretKey = "YOUR_SECRET_KEY";
        String region = "us-east-1";
        String queueUrl = "http://localhost:4566/000000000000/payment-order-main";

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        String message =  "{'idOrder': '" + idOrder + "', 'statusPayment':" + status + " }";
        SendMessageRequest sendMsgRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);

        return sqs.sendMessage(sendMsgRequest);
    }

}
