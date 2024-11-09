# language: pt

Funcionalidade: Pedido

  Cenario: Criar Pedido
    Quando enviar o pedido para processamento
    Entao o pedido deve ser registrado no sistema

  Cenario: Atualizar Pedido para Recebido
    Quando receber uma notificação de pagamento
    Entao o pedido deverá ser atualizado para Recebido

  Cenario: Atualizar Pedido para Em Preparação
    Quando atendente clicar no pedido
    Entao sistema deverá colocar pedido para Em Preparação

  Cenario: Atualizar Pedido para Pronto
    Quando atendente informar término de preparação
    Entao sistema deverá colocar pedido para Pronto

  Cenario: Atualizar Pedido para Finalizado
    Quando atendente entregar produto para o cliente
    Entao sistema deverá colocar pedido para Finalizado

  Cenario: Pagamento reprovado
    Quando receber uma notificação de pagamento negado
    Entao o pedido deverá ser atualizado para Cancelado