Simulador de Coleta de Lixo em Teresina
Este é um projeto de simulação para gerenciar a coleta de resíduos sólidos urbanos na cidade de Teresina, Piauí. O objetivo principal é otimizar a logística da coleta de lixo, simulando o funcionamento de caminhões em diferentes zonas da cidade e a operação de estações de transferência.

Funcionalidades Principais
Zonas da Cidade: A cidade é dividida em cinco zonas (Sul, Norte, Centro, Leste e Sudeste), cada uma com sua própria geração de lixo.
Caminhões de Coleta: Pequenos caminhões (2, 4, 8 e 10 toneladas de capacidade) coletam o lixo nas zonas.
Estações de Transferência: Caminhões grandes (20 toneladas de capacidade) transportam o lixo das estações de transferência para o aterro sanitário.
Eventos Discretos: A simulação funciona com base em eventos (ex: geração de lixo, coleta, chegada na estação), que acontecem em momentos específicos do tempo.
Parâmetros Configuráveis: É possível ajustar o número de caminhões, a capacidade, o número de viagens, os tempos de viagem (com horários de pico) e a geração de lixo.
Visualização Gráfica: Uma interface gráfica mostra o status das zonas, estações e caminhões em tempo real.
Tecnologias
Java: Linguagem de programação principal.
JavaFX: Para a interface gráfica do usuário.
Como Rodar o Projeto
Para executar o simulador, você precisará ter o Java Development Kit (JDK) 22 e o JavaFX SDK instalados.

1. Preparar o Ambiente
JDK: Verifique se você tem o JDK 22 instalado.
JavaFX SDK: Baixe o JavaFX SDK (versão 24.0.1 ou compatível) e descompacte-o em um local de fácil acesso no seu computador.
2. Abrir e Configurar na IDE (Ex: IntelliJ IDEA)
Abra o Projeto: No IntelliJ IDEA, selecione File > Open e navegue até a pasta raiz do projeto.
Configurar Bibliotecas JavaFX:
Vá em File > Project Structure > Libraries.
Clique no botão + e selecione Java.
Navegue até a pasta lib dentro do seu JavaFX SDK descompactado e selecione todos os arquivos .jar dentro dela. Clique em OK e depois em Apply.
Configurar VM Options para Execução:
Vá em Run > Edit Configurations....

Encontre a configuração para MainFX (se já existir) ou crie uma nova Application.

No campo VM options, adicione a seguinte linha, substituindo /caminho/para/seu/javafx-sdk-24.0.1 pelo caminho real da pasta do seu JavaFX SDK:

--module-path /caminho/para/seu/javafx-sdk-24.0.1/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics
Clique em Apply e OK.

3. Rodar a Simulação
Clique no botão Run (o triângulo verde) na sua IDE para executar a classe MainFX.
Uma janela da aplicação gráfica será aberta, permitindo que você ajuste os parâmetros da simulação (número de caminhões, horas a simular, etc.) e observe a dinâmica da coleta de lixo em Teresina.
