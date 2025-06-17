import configsimulador.ParametrosSimulacao;
import configsimulador.Simulador;
import configsimulador.ConfiguracoesDoSimulador;
import eventos.Evento;
import eventos.GerenciadorAgenda;
import eventos.IEventoObserver;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import tads.Lista;
import zonas.Zonas;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MainFX extends Application implements IEventoObserver {

    private TextArea logArea;
    private Button iniciarBtn;
    private Button pausarBtn;
    private Button encerrarBtn;

    // --- CAMPOS DE TEXTO MOVIDOS PARA VARIÁVEIS DE INSTÂNCIA ---
    private TextField horasField;
    private TextField segPorHoraField;

    private Thread simuladorThread;
    private volatile boolean pausado = false;
    private volatile boolean encerrado = false;
    private Pane mapa;

    private int segundosPorHoraSimulada = 2;
    private int tempoEventoAnterior = 0;

    private RelogioSimulacao relogio;
    private PainelStatusCaminhoes painelStatusCaminhoes;

    private final Map<String, Node> caminhaoNodes = new HashMap<>();
    private final Map<Integer, Node> caminhaoGrandeNodes = new HashMap<>();

    private static final Map<String, Point2D> COORDENADAS = new HashMap<>() {{
        put("Norte", new Point2D(175, 100));
        put("Sul", new Point2D(175, 250));
        put("Centro", new Point2D(375, 120));
        put("Sudeste", new Point2D(375, 280));
        put("Leste", new Point2D(575, 200));
        put("Estação A", new Point2D(850, 150));
        put("Estação B", new Point2D(850, 250));
        put("Aterro Sanitário", new Point2D(1050, 200));
    }};

    @Override
    public void start(Stage primaryStage) {
        iniciarTelaPrincipal(primaryStage);
    }

    private void iniciarTelaPrincipal(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(1400, 800);

        Label titulo = new Label("Simulador de Coleta de Lixo - Teresina, PI");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(titulo, Pos.CENTER);

        // --- HBOX DE CONTROLES MODIFICADA ---
        HBox controles = new HBox(15); // Espaçamento ajustado
        controles.setPadding(new Insets(10));
        controles.setAlignment(Pos.CENTER);

        iniciarBtn = new Button("▶ Iniciar Simulação");
        pausarBtn = new Button("⏸ Pausar");
        encerrarBtn = new Button("⏹ Encerrar");
        pausarBtn.setDisable(true);
        encerrarBtn.setDisable(true);

        // Criação dos novos elementos de configuração
        Separator separator = new Separator(Orientation.VERTICAL);
        Label horasLabel = new Label("Horas a Simular:");
        horasField = new TextField("24");
        horasField.setPrefWidth(50); // Define uma largura menor

        Label segLabel = new Label("Segundos por Hora:");
        segPorHoraField = new TextField("1");
        segPorHoraField.setPrefWidth(50); // Define uma largura menor

        // Adiciona todos os elementos (botões e configs) na HBox superior
        controles.getChildren().addAll(
                iniciarBtn, pausarBtn, encerrarBtn,
                separator,
                horasLabel, horasField,
                segLabel, segPorHoraField
        );

        VBox topo = new VBox(10, titulo, controles);
        topo.setAlignment(Pos.CENTER);
        root.setTop(topo);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 14px;");
        logArea.setPrefHeight(300);
        root.setBottom(logArea);

        // --- VBOX DA ESQUERDA SIMPLIFICADA ---
        VBox configuracaoBox = new VBox(10);
        configuracaoBox.setPadding(new Insets(20));
        configuracaoBox.setAlignment(Pos.TOP_LEFT);
        configuracaoBox.setPrefWidth(250);

        Label configTitulo = new Label("Parâmetros dos Caminhões");
        configTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField c2Field = new TextField("2");
        TextField v2Field = new TextField("3");
        TextField c4Field = new TextField("1");
        TextField v4Field = new TextField("3");
        TextField c8Field = new TextField("1");
        TextField v8Field = new TextField("3");
        TextField c10Field = new TextField("1");
        TextField v10Field = new TextField("3");

        // Removemos os campos de config. da simulação daqui
        configuracaoBox.getChildren().addAll(configTitulo,
                new Label("Caminhões de 2t:"), c2Field, new Label("Viagens por caminhão 2t:"), v2Field,
                new Label("Caminhões de 4t:"), c4Field, new Label("Viagens por caminhão 4t:"), v4Field,
                new Label("Caminhões de 8t:"), c8Field, new Label("Viagens por caminhão 8t:"), v8Field,
                new Label("Caminhões de 10t:"), c10Field, new Label("Viagens por caminhão 10t:"), v10Field
        );
        root.setLeft(configuracaoBox);

        mapa = new Pane();
        mapa.setPrefSize(1200, 600);
        mapa.setStyle("-fx-background-color: #f2f2f2;");
        root.setCenter(mapa);

        inicializarElementosDoMapa(root);

        iniciarBtn.setOnAction(e -> {
            try {
                // A leitura dos campos agora usa as variáveis de instância
                this.segundosPorHoraSimulada = Integer.parseInt(segPorHoraField.getText().trim());
                if (this.segundosPorHoraSimulada <= 0) this.segundosPorHoraSimulada = 1;

                ParametrosSimulacao params = new ParametrosSimulacao(
                        Integer.parseInt(c2Field.getText().trim()), Integer.parseInt(v2Field.getText().trim()),
                        Integer.parseInt(c4Field.getText().trim()), Integer.parseInt(v4Field.getText().trim()),
                        Integer.parseInt(c8Field.getText().trim()), Integer.parseInt(v8Field.getText().trim()),
                        Integer.parseInt(c10Field.getText().trim()), Integer.parseInt(v10Field.getText().trim()),
                        Integer.parseInt(horasField.getText().trim())
                );
                iniciarSimulacao(params);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, insira apenas números inteiros válidos nos campos.");
                alert.showAndWait();
            }
        });

        pausarBtn.setOnAction(e -> {
            pausado = !pausado;
            pausarBtn.setText(pausado ? "▶ Continuar" : "⏸ Pausar");
        });

        encerrarBtn.setOnAction(e -> encerrarSimulacao());

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Simulador de Coleta de Lixo - Teresina");
        primaryStage.setOnCloseRequest(e -> {
            encerrarSimulacao();
            Platform.exit();
        });
        primaryStage.show();
    }

    // O resto da classe (métodos de simulação, atualização, etc.) permanece o mesmo.
    // ...
    private void iniciarSimulacao(ParametrosSimulacao params) {
        logArea.clear();
        this.tempoEventoAnterior = 0;
        caminhaoNodes.values().forEach(node -> mapa.getChildren().remove(node));
        caminhaoNodes.clear();
        caminhaoGrandeNodes.values().forEach(node -> mapa.getChildren().remove(node));
        caminhaoGrandeNodes.clear();

        pausado = false;
        encerrado = false;
        iniciarBtn.setDisable(true);
        pausarBtn.setDisable(false);
        encerrarBtn.setDisable(false);

        GerenciadorAgenda.adicionarObserver(this);
        zonas.DistanciaZonas.configurar(new estacoes.EstacaoDeTransferencia("Estação A"), new estacoes.EstacaoDeTransferencia("Estação B"));

        simuladorThread = new Thread(() -> {
            Simulador simulador = new Simulador();
            final Lista<Zonas> zonas = simulador.inicializar(params);
            final int tempoMaximoSimulacao = params.getHorasASimular() * 60;

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8)) {
                PrintStream oldOut = System.out;
                System.setOut(ps);

                while (!GerenciadorAgenda.estaVazia() && !Thread.currentThread().isInterrupted()) {
                    while (pausado) {
                        Thread.sleep(100);
                    }

                    Evento proximoEvento = GerenciadorAgenda.espiarProximoEvento();
                    if (proximoEvento.getTempo() > tempoMaximoSimulacao) {
                        System.out.println("[SIMULAÇÃO] Tempo limite de " + tempoMaximoSimulacao + " minutos atingido.");
                        break;
                    }

                    Evento eventoAtual = GerenciadorAgenda.proximoEvento();
                    if (eventoAtual != null) {
                        eventoAtual.executar();
                        GerenciadorAgenda.notificarObservers(eventoAtual);

                        long tempoSimuladoDecorrido = eventoAtual.getTempo() - tempoEventoAnterior;
                        if (tempoSimuladoDecorrido > 0) {
                            long delayMs = (tempoSimuladoDecorrido * this.segundosPorHoraSimulada * 1000L) / 60L;
                            Thread.sleep(delayMs);
                        }
                        this.tempoEventoAnterior = eventoAtual.getTempo();
                    }
                }

                simulador.exibirRelatorioFinal(zonas);
                System.setOut(oldOut);
                Platform.runLater(() -> logArea.setText(removerCoresANSI(baos.toString(StandardCharsets.UTF_8))));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[SIMULAÇÃO] Thread interrompida. Encerrando.");
            } catch (Exception ex) {
                Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Erro na simulação: " + ex.getMessage()).showAndWait());
                ex.printStackTrace();
            } finally {
                Platform.runLater(this::finalizarVisualizacao);
            }
        });

        simuladorThread.start();
    }
    private void finalizarVisualizacao() {
        logArea.appendText("\nSimulação concluída.\n");
        iniciarBtn.setDisable(false);
        pausarBtn.setDisable(true);
        encerrarBtn.setDisable(true);
        pausado = false;
        pausarBtn.setText("⏸ Pausar");
    }
    @Override
    public void onEvento(Evento evento) {
        Platform.runLater(() -> {
            if (evento instanceof eventos.ColetaLixo) {
                eventos.ColetaLixo eventoColeta = (eventos.ColetaLixo) evento;
                atualizarVisualZona(eventoColeta.getZona());
                atualizarPainelCaminhao(eventoColeta);
                Node caminhaoNode = getOrCreateCaminhaoNode(eventoColeta.getCaminhao());
                Point2D pos = COORDENADAS.get(eventoColeta.getZona().getNome());
                if (pos != null) {
                    resetarPosicao(caminhaoNode, pos);
                }
            } else if (evento instanceof eventos.GeracaoDeLixo) {
                eventos.GeracaoDeLixo eventoGeracao = (eventos.GeracaoDeLixo) evento;
                atualizarVisualZona(eventoGeracao.getZona());
            } else if (evento instanceof eventos.TransferenciaParaEstacao) {
                eventos.TransferenciaParaEstacao eventoTransf = (eventos.TransferenciaParaEstacao) evento;
                atualizarPainelCaminhao(eventoTransf);
                Node caminhaoNode = getOrCreateCaminhaoNode(eventoTransf.getCaminhao());
                estacoes.EstacaoDeTransferencia estacaoDestino = zonas.DistanciaZonas.getEstacaoPara(eventoTransf.getZona());
                Point2D posDestino = COORDENADAS.get(estacaoDestino.getNomeEstacao());
                int duracaoViagem = eventoTransf.getDuracaoViagem();
                animarNo(caminhaoNode, posDestino, duracaoViagem);
            } else if (evento instanceof eventos.EstacaoTransferencia) {
                eventos.EstacaoTransferencia eventoEstacao = (eventos.EstacaoTransferencia) evento;
                atualizarPainelCaminhao(eventoEstacao);
                atualizarVisualEstacao(eventoEstacao.getEstacao());
            } else if (evento instanceof eventos.GeracaoCaminhaoGrande) {
                eventos.GeracaoCaminhaoGrande eventoGeracao = (eventos.GeracaoCaminhaoGrande) evento;
                atualizarVisualEstacao(eventoGeracao.getEstacao());
            } else if (evento instanceof eventos.PartidaCaminhaoGrande) {
                eventos.PartidaCaminhaoGrande eventoPartida = (eventos.PartidaCaminhaoGrande) evento;
                Point2D posOrigem = COORDENADAS.get(eventoPartida.getEstacaoOrigem().getNomeEstacao());
                Node caminhaoNode = getOrCreateCaminhaoGrandeNode(eventoPartida.getCaminhaoGrande(), posOrigem);

                Point2D posDestino = COORDENADAS.get("Aterro Sanitário");
                animarNo(caminhaoNode, posDestino, ConfiguracoesDoSimulador.TEMPO_VIAGEM_ATERRO);

                TranslateTransition tt = (TranslateTransition) caminhaoNode.getProperties().get("animation");
                if (tt != null) {
                    tt.setOnFinished(e -> {
                        caminhaoNode.setVisible(false);
                    });
                }
            }
            relogio.atualizarTempo(evento.getTempo());
        });
    }
    private void inicializarElementosDoMapa(BorderPane root) {
        // Zonas
        String[][] zonas = {{"Norte", "#A5D6A7", "100", "50"}, {"Sul", "#90CAF9", "100", "200"},
                {"Centro", "#FFAB91", "300", "70"}, {"Sudeste", "#FFF59D", "300", "230"},
                {"Leste", "#CE93D8", "500", "150"}};

        for (String[] zona : zonas) {
            double x = Double.parseDouble(zona[2]);
            double y = Double.parseDouble(zona[3]);
            Rectangle rect = new Rectangle(150, 100, Color.web(zona[1]));
            rect.setArcWidth(12);
            rect.setArcHeight(12);
            rect.setLayoutX(x);
            rect.setLayoutY(y);
            Label label = new Label("Zona " + zona[0]);
            label.setLayoutX(x + 40);
            label.setLayoutY(y + 30);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            ProgressBar barraLixo = new ProgressBar(0);
            barraLixo.setPrefWidth(120);
            barraLixo.setLayoutX(x + 15);
            barraLixo.setLayoutY(y + 70);
            barraLixo.setStyle("-fx-accent: #8B4513;");
            barraLixo.setId("barra_" + zona[0]);
            Label valorLixo = new Label("0T");
            valorLixo.setLayoutX(x + 65);
            valorLixo.setLayoutY(y + 50);
            valorLixo.setId("lixo_" + zona[0]);
            mapa.getChildren().addAll(rect, label, barraLixo, valorLixo);
        }

        // Estações
        Rectangle estA = new Rectangle(100, 60, Color.LIGHTBLUE);
        estA.setLayoutX(800);
        estA.setLayoutY(120);
        Label labelA = new Label("Estação A");
        labelA.setLayoutX(810);
        labelA.setLayoutY(130);
        labelA.setStyle("-fx-font-weight: bold;");
        Rectangle estB = new Rectangle(100, 60, Color.LIGHTBLUE);
        estB.setLayoutX(800);
        estB.setLayoutY(220);
        Label labelB = new Label("Estação B");
        labelB.setLayoutX(810);
        labelB.setLayoutY(230);
        labelB.setStyle("-fx-font-weight: bold;");
        mapa.getChildren().addAll(estA, labelA, estB, labelB);

        // Barras de progresso para as estações
        ProgressBar barraEstacaoA = new ProgressBar(0);
        barraEstacaoA.setPrefWidth(80);
        barraEstacaoA.setLayoutX(810);
        barraEstacaoA.setLayoutY(160);
        barraEstacaoA.setStyle("-fx-accent: #4CAF50;"); // Cor verde
        barraEstacaoA.setId("barra_Estacao_A");

        ProgressBar barraEstacaoB = new ProgressBar(0);
        barraEstacaoB.setPrefWidth(80);
        barraEstacaoB.setLayoutX(810);
        barraEstacaoB.setLayoutY(260);
        barraEstacaoB.setStyle("-fx-accent: #4CAF50;");
        barraEstacaoB.setId("barra_Estacao_B");

        mapa.getChildren().addAll(barraEstacaoA, barraEstacaoB);
        // Aterro Sanitário
        Rectangle aterroRect = new Rectangle(120, 80, Color.web("#607D8B"));
        aterroRect.setArcWidth(12);
        aterroRect.setArcHeight(12);
        aterroRect.setLayoutX(1020);
        aterroRect.setLayoutY(180);
        Label aterroLabel = new Label("Aterro Sanitário");
        aterroLabel.setLayoutX(1025);
        aterroLabel.setLayoutY(210);
        aterroLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        mapa.getChildren().addAll(aterroRect, aterroLabel);


        // Componentes da UI
        relogio = new RelogioSimulacao();
        relogio.setLayoutX(1050);
        relogio.setLayoutY(20);
        mapa.getChildren().add(relogio);
        painelStatusCaminhoes = new PainelStatusCaminhoes(mapa);
    }
    private void encerrarSimulacao() {
        if (!encerrado) {
            encerrado = true;
            if (simuladorThread != null && simuladorThread.isAlive()) {
                simuladorThread.interrupt();
            }
        }
    }
    private String removerCoresANSI(String texto) {
        return texto.replaceAll("\u001B\\[[;\\d]*m", "");
    }
    private Node getOrCreateCaminhaoNode(caminhoes.CaminhaoPequeno caminhao) {
        String id = caminhao.getId();
        if (!caminhaoNodes.containsKey(id)) {
            Circle caminhaoVisual = new Circle(10, Color.DEEPSKYBLUE);
            caminhaoVisual.setStroke(Color.BLACK);
            caminhaoVisual.setLayoutX(-100);
            caminhaoVisual.setLayoutY(-100);
            caminhaoNodes.put(id, caminhaoVisual);
            mapa.getChildren().add(caminhaoVisual);
        }
        return caminhaoNodes.get(id);
    }
    private Node getOrCreateCaminhaoGrandeNode(caminhoes.CaminhaoGrande caminhao, Point2D posInicial) {
        int id = caminhao.getId();
        if (!caminhaoGrandeNodes.containsKey(id)) {
            Circle caminhaoVisual = new Circle(15, Color.web("#BF360C"));
            caminhaoVisual.setStroke(Color.WHITE);
            caminhaoVisual.setStrokeWidth(2);
            caminhaoVisual.setLayoutX(posInicial.getX());
            caminhaoVisual.setLayoutY(posInicial.getY());
            caminhaoGrandeNodes.put(id, caminhaoVisual);
            mapa.getChildren().add(caminhaoVisual);
        }
        return caminhaoGrandeNodes.get(id);
    }
    private void animarNo(Node node, Point2D destino, int duracaoSimulada) {
        if (node == null || destino == null) return;
        node.setVisible(true);
        double duracaoAnimacaoMs = duracaoSimulada * 50;
        TranslateTransition tt = new TranslateTransition(Duration.millis(duracaoAnimacaoMs), node);
        tt.setToX(destino.getX() - node.getLayoutX());
        tt.setToY(destino.getY() - node.getLayoutY());
        tt.setOnFinished(event -> {
            node.setLayoutX(node.getLayoutX() + node.getTranslateX());
            node.setLayoutY(node.getLayoutY() + node.getTranslateY());
            node.setTranslateX(0);
            node.setTranslateY(0);
        });
        node.getProperties().put("animation", tt);
        tt.play();
    }
    private void resetarPosicao(Node node, Point2D pos) {
        node.setTranslateX(0);
        node.setTranslateY(0);
        node.setLayoutX(pos.getX());
        node.setLayoutY(pos.getY());
    }
    private void atualizarVisualZona(zonas.Zonas zona) {
        String nomeZona = zona.getNome();
        ProgressBar barraLixo = (ProgressBar) mapa.lookup("#barra_" + nomeZona);
        Label valorLixo = (Label) mapa.lookup("#lixo_" + nomeZona);
        if (barraLixo != null && valorLixo != null) {
            double progresso = (double) zona.getLixoAcumulado() / zona.getLixoMax();
            barraLixo.setProgress(progresso);
            valorLixo.setText(zona.getLixoAcumulado() + "T");
        }
    }
    private void atualizarPainelCaminhao(eventos.ColetaLixo eventoColeta) {
        caminhoes.CaminhaoPequeno caminhao = eventoColeta.getCaminhao();
        String status = "Coletando em " + eventoColeta.getZona().getNome();
        String cor = "#FFC107";
        painelStatusCaminhoes.adicionarOuAtualizarCaminhao(caminhao.getId(), status, caminhao.getCargaAtual(), caminhao.getCapacidadeMaxima(), cor);
    }
    private void atualizarPainelCaminhao(eventos.TransferenciaParaEstacao eventoTransf) {
        caminhoes.CaminhaoPequeno caminhao = eventoTransf.getCaminhao();
        String status = "Indo para estação";
        String cor = "#03A9F4";
        painelStatusCaminhoes.adicionarOuAtualizarCaminhao(caminhao.getId(), status, caminhao.getCargaAtual(), caminhao.getCapacidadeMaxima(), cor);
    }
    private void atualizarPainelCaminhao(eventos.EstacaoTransferencia eventoEstacao) {
        caminhoes.CaminhaoPequeno caminhao = eventoEstacao.getCaminhao();
        String status = "Na " + eventoEstacao.getEstacao().getNomeEstacao();
        String cor = "#E91E63";
        int cargaParaExibir = caminhao.getCapacidadeMaxima();
        painelStatusCaminhoes.adicionarOuAtualizarCaminhao(caminhao.getId(), status, cargaParaExibir, caminhao.getCapacidadeMaxima(), cor);
    }
    private void atualizarVisualEstacao(estacoes.EstacaoDeTransferencia estacao) {
        String nomeEstacao = estacao.getNomeEstacao();
        String idEstacao = nomeEstacao.replace(" ", "_");
        for (Node node : mapa.getChildren()) {
            if (node instanceof Label && ((Label) node).getText().startsWith(nomeEstacao)) {
                Label labelEstacao = (Label) node;
                int tamanhoFila = estacao.getFilaCaminhoesPequeos().getTamanho();
                if (tamanhoFila > 0) {
                    labelEstacao.setText(nomeEstacao + " (Fila: " + tamanhoFila + ")");
                } else {
                    labelEstacao.setText(nomeEstacao);
                }
                break;
            }
        }
        ProgressBar barraEstacao = (ProgressBar) mapa.lookup("#barra_" + idEstacao);
        if (barraEstacao != null) {
            caminhoes.CaminhaoGrande caminhaoGrande = estacao.getCaminhaoGrande();
            if (caminhaoGrande != null) {
                double progresso = (double) caminhaoGrande.getCargaAtual() / caminhaoGrande.getCapacidadeMaxima();
                barraEstacao.setProgress(progresso);
            } else {
                barraEstacao.setProgress(0);
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
    public static class RelogioSimulacao extends StackPane {
        private final Label horaLabel;
        private final ProgressIndicator indicadorDia;

        public RelogioSimulacao() {
            setPrefSize(100, 100);
            setStyle("-fx-background-color: rgba(0,0,0,0.1); -fx-background-radius: 50;");
            indicadorDia = new ProgressIndicator(0);
            indicadorDia.setPrefSize(90, 90);
            horaLabel = new Label("07:00");
            horaLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            getChildren().addAll(indicadorDia, horaLabel);
        }

        public void atualizarTempo(int minutos) {
            int horaSimulada = 7 + (minutos / 60);
            int minutoSimulado = minutos % 60;
            String horaFormatada = String.format("%02d:%02d", horaSimulada, minutoSimulado);
            horaLabel.setText(horaFormatada);
        }
    }
    public static class PainelStatusCaminhoes {
        private final VBox container;
        private final Map<String, HBox> linhasCaminhoes = new HashMap<>();

        public PainelStatusCaminhoes(Pane parent) {
            container = new VBox(5);
            container.setPadding(new Insets(10));
            container.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 5;");
            container.setLayoutX(700);
            container.setLayoutY(350);
            container.setPrefWidth(250);
            Label titulo = new Label("Status dos Caminhões");
            titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            container.getChildren().add(titulo);
            parent.getChildren().add(container);
        }

        public void adicionarOuAtualizarCaminhao(String id, String status, int cargaAtual, int capacidade, String cor) {
            if (linhasCaminhoes.containsKey(id)) {
                atualizarLinhaCaminhao(id, status, cargaAtual, capacidade, cor);
            } else {
                criarLinhaCaminhao(id, status, cargaAtual, capacidade, cor);
            }
        }

        private void criarLinhaCaminhao(String id, String status, int cargaAtual, int capacidade, String cor) {
            HBox linha = new HBox(5);
            linha.setAlignment(Pos.CENTER_LEFT);
            Rectangle indicador = new Rectangle(10, 10, Color.web(cor));
            Label idLabel = new Label(id);
            idLabel.setMinWidth(40);
            idLabel.setStyle("-fx-font-weight: bold;");
            Label statusLabel = new Label(status);
            statusLabel.setPrefWidth(120);
            Label cargaLabel = new Label(cargaAtual + "/" + capacidade + "T");
            linha.getChildren().addAll(indicador, idLabel, statusLabel, cargaLabel);
            container.getChildren().add(linha);
            linhasCaminhoes.put(id, linha);
        }

        private void atualizarLinhaCaminhao(String id, String status, int cargaAtual, int capacidade, String cor) {
            HBox linha = linhasCaminhoes.get(id);
            ((Rectangle) linha.getChildren().get(0)).setFill(Color.web(cor));
            ((Label) linha.getChildren().get(2)).setText(status);
            ((Label) linha.getChildren().get(3)).setText(cargaAtual + "/" + capacidade + "T");
        }
    }
}