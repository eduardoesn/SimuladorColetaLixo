import configsimulador.ParametrosSimulacao;
import configsimulador.Simulador;
import eventos.Evento;
import eventos.GerenciadorAgenda;
import eventos.IEventoObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFX extends Application implements IEventoObserver {

    private TextArea logArea;
    private Slider velocidadeSlider;
    private Button iniciarBtn;
    private Button pausarBtn;
    private Button encerrarBtn;
    private Button estatisticasBtn;
    private Button exportarBtn;
    private Button debugBtn;

    private Thread simuladorThread;
    private volatile boolean pausado = false;
    private volatile boolean encerrado = false;
    private Pane mapa;

    // Componentes da UI
    private RelogioSimulacao relogio;
    private PainelEstatisticas painelEstatisticas;
    private PainelStatusCaminhoes painelStatusCaminhoes;
    private ModoDebug modoDebug;

    @Override
    public void start(Stage primaryStage) {
        iniciarTelaPrincipal(primaryStage);
    }

    private void iniciarTelaPrincipal(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(1400, 800);

        // --- TÍTULO ---
        Label titulo = new Label("Simulador de Coleta de Lixo - Teresina, PI");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(titulo, Pos.CENTER);

        // --- CONTROLES SUPERIORES ---
        HBox controles = new HBox(20);
        controles.setPadding(new Insets(10));
        controles.setAlignment(Pos.CENTER);

        iniciarBtn = new Button("▶ Iniciar Simulação");
        pausarBtn = new Button("⏸ Pausar");
        encerrarBtn = new Button("⏹ Encerrar");
        estatisticasBtn = new Button("📊 Estatísticas");
        exportarBtn = new Button("📋 Exportar Relatório");
        debugBtn = new Button("🔍 Modo Debug");

        velocidadeSlider = new Slider(1, 5, 3);
        velocidadeSlider.setMajorTickUnit(1);
        velocidadeSlider.setSnapToTicks(true);
        velocidadeSlider.setShowTickMarks(true);
        velocidadeSlider.setShowTickLabels(true);
        velocidadeSlider.setBlockIncrement(1);

        controles.getChildren().addAll(iniciarBtn, pausarBtn, encerrarBtn, new Label("Velocidade:"),
                velocidadeSlider, estatisticasBtn, exportarBtn, debugBtn);

        VBox topo = new VBox(10, titulo, controles);
        topo.setAlignment(Pos.CENTER);
        root.setTop(topo);

        // --- ÁREA DE LOG ---
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 14px;");
        logArea.setPrefHeight(300);
        root.setBottom(logArea);

        // --- PAINEL DE CONFIGURAÇÃO (ESQUERDA) ---
        VBox configuracaoBox = new VBox(10);
        configuracaoBox.setPadding(new Insets(20));
        configuracaoBox.setAlignment(Pos.TOP_LEFT);
        configuracaoBox.setPrefWidth(250);

        Label configTitulo = new Label("Parâmetros");
        configTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField c2Field = new TextField("2");
        TextField v2Field = new TextField("3");
        TextField c4Field = new TextField("1");
        TextField v4Field = new TextField("3");
        TextField c8Field = new TextField("1");
        TextField v8Field = new TextField("3");

        Button salvarConfigBtn = new Button("💾 Salvar Configuração");
        Button carregarConfigBtn = new Button("📂 Carregar Configuração");
        salvarConfigBtn.setMaxWidth(Double.MAX_VALUE);
        carregarConfigBtn.setMaxWidth(Double.MAX_VALUE);

        configuracaoBox.getChildren().addAll(configTitulo,
                new Label("Caminhões de 2t:"), c2Field, new Label("Viagens por caminhão 2t:"), v2Field,
                new Label("Caminhões de 4t:"), c4Field, new Label("Viagens por caminhão 4t:"), v4Field,
                new Label("Caminhões de 8t:"), c8Field, new Label("Viagens por caminhão 8t:"), v8Field,
                new Separator(), salvarConfigBtn, carregarConfigBtn);
        root.setLeft(configuracaoBox);

        // --- MAPA (CENTRO) ---
        mapa = new Pane();
        mapa.setPrefSize(1000, 600);
        mapa.setStyle("-fx-background-color: #f2f2f2;");
        root.setCenter(mapa);

        // Inicializa componentes da UI no mapa
        inicializarElementosDoMapa(root);

        // --- AÇÕES DOS BOTÕES ---
        iniciarBtn.setOnAction(e -> {
            try {
                ParametrosSimulacao params = new ParametrosSimulacao(
                        Integer.parseInt(c2Field.getText().trim()), Integer.parseInt(v2Field.getText().trim()),
                        Integer.parseInt(c4Field.getText().trim()), Integer.parseInt(v4Field.getText().trim()),
                        Integer.parseInt(c8Field.getText().trim()), Integer.parseInt(v8Field.getText().trim()));
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

        estatisticasBtn.setOnAction(e -> {
            if (painelEstatisticas.isVisible()) {
                painelEstatisticas.ocultar();
                estatisticasBtn.setText("📊 Mostrar Estatísticas");
            } else {
                painelEstatisticas.mostrar();
                estatisticasBtn.setText("📊 Ocultar Estatísticas");
            }
        });

        debugBtn.setOnAction(e -> {
            modoDebug.alternar();
            if (modoDebug.isAtivo()) {
                debugBtn.setText("🔍 Desativar Debug");
            } else {
                debugBtn.setText("🔍 Ativar Debug");
            }
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Simulador de Coleta de Lixo - Teresina");
        primaryStage.setOnCloseRequest(e -> {
            encerrarSimulacao();
            Platform.exit();
        });
        primaryStage.show();
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
            rect.setArcWidth(12); rect.setArcHeight(12); rect.setLayoutX(x); rect.setLayoutY(y);
            rect.setId("zona_" + zona[0]);
            Label label = new Label("Zona " + zona[0]);
            label.setLayoutX(x + 40); label.setLayoutY(y + 30);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            ProgressBar barraLixo = new ProgressBar(0);
            barraLixo.setPrefWidth(120); barraLixo.setLayoutX(x + 15); barraLixo.setLayoutY(y + 70);
            barraLixo.setStyle("-fx-accent: #8B4513;"); barraLixo.setId("barra_" + zona[0]);
            Label valorLixo = new Label("0T");
            valorLixo.setLayoutX(x + 65); valorLixo.setLayoutY(y + 50); valorLixo.setId("lixo_" + zona[0]);
            mapa.getChildren().addAll(rect, label, barraLixo, valorLixo);
        }

        // Estações
        Rectangle estA = new Rectangle(100, 60, Color.LIGHTBLUE); estA.setLayoutX(800); estA.setLayoutY(120);
        estA.setId("estacao_A");
        Label labelA = new Label("Estação A"); labelA.setLayoutX(810); labelA.setLayoutY(130);
        labelA.setStyle("-fx-font-weight: bold;");
        Rectangle estB = new Rectangle(100, 60, Color.LIGHTBLUE); estB.setLayoutX(800); estB.setLayoutY(220);
        estB.setId("estacao_B");
        Label labelB = new Label("Estação B"); labelB.setLayoutX(810); labelB.setLayoutY(230);
        labelB.setStyle("-fx-font-weight: bold;");
        mapa.getChildren().addAll(estA, labelA, estB, labelB);

        // Componentes da UI
        relogio = new RelogioSimulacao(); relogio.setLayoutX(1050); relogio.setLayoutY(20);
        mapa.getChildren().add(relogio);
        painelStatusCaminhoes = new PainelStatusCaminhoes(mapa);
        painelEstatisticas = new PainelEstatisticas(root);
        modoDebug = new ModoDebug(mapa);
    }

    private void iniciarSimulacao(ParametrosSimulacao params) {
        logArea.clear();
        encerrado = false;
        pausado = false;
        iniciarBtn.setDisable(true);

        GerenciadorAgenda.adicionarObserver(this);

        simuladorThread = new Thread(() -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
                PrintStream oldOut = System.out;
                System.setOut(ps);

                new Simulador().iniciar(params);

                System.setOut(oldOut);
                ps.flush();

                String logCompleto = baos.toString(StandardCharsets.UTF_8);
                Platform.runLater(() -> logArea.setText(removerCoresANSI(logCompleto)));

            } catch (Exception ex) {
                Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Erro na simulação: " + ex.getMessage()).showAndWait());
                ex.printStackTrace();
            } finally {
                Platform.runLater(() -> {
                    logArea.appendText("\nSimulação concluída.\n");
                    iniciarBtn.setDisable(false);
                    encerrarSimulacao();
                });
            }
        });

        simuladorThread.start();
    }

    @Override
    public void onEvento(Evento evento) {
        Platform.runLater(() -> {
            relogio.atualizarTempo(evento.getTempo(), 12 * 60);

            try {
                Thread.sleep(calcularDelay());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private int calcularDelay() {
        return (6 - (int) velocidadeSlider.getValue()) * 15;
    }

    private void encerrarSimulacao() {
        if (!encerrado) {
            encerrado = true;
            if (simuladorThread != null && simuladorThread.isAlive()) {
                simuladorThread.interrupt();
            }
            GerenciadorAgenda.removerObserver(this);
            iniciarBtn.setDisable(false);
        }
    }

    private String removerCoresANSI(String texto) {
        return texto.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    public static void main(String[] args) {
        launch(args);
    }

    // =================================================================================
    // CLASSES INTERNAS DA UI
    // =================================================================================

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

        public void atualizarTempo(int minutos, int totalMinutosDia) {
            int horaSimulada = 7 + (minutos / 60);
            int minutoSimulado = minutos % 60;
            String horaFormatada = String.format("%02d:%02d", horaSimulada, minutoSimulado);
            horaLabel.setText(horaFormatada);

            double progresso = (double) minutos / totalMinutosDia;
            indicadorDia.setProgress(progresso);

            if (minutos < 180) { // Manhã (7h-10h)
                setStyle("-fx-background-color: rgba(255,200,0,0.2); -fx-background-radius: 50;");
            } else if (minutos < 360) { // Meio-dia (10h-13h)
                setStyle("-fx-background-color: rgba(255,255,0,0.2); -fx-background-radius: 50;");
            } else if (minutos < 540) { // Tarde (13h-16h)
                setStyle("-fx-background-color: rgba(255,165,0,0.2); -fx-background-radius: 50;");
            } else { // Fim do dia (16h-19h)
                setStyle("-fx-background-color: rgba(255,69,0,0.2); -fx-background-radius: 50;");
            }
        }
    }

    public static class PainelEstatisticas {
        private final VBox container;
        private final LineChart<Number, Number> graficoLixo;
        private final XYChart.Series<Number, Number> serieGerado;
        private final XYChart.Series<Number, Number> serieColetado;
        private final PieChart graficoEficiencia;

        public PainelEstatisticas(Pane parent) {
            container = new VBox(10);
            container.setPrefSize(400, 500);
            container.setStyle("-fx-background-color: white; -fx-border-color: lightgrey; -fx-border-width: 1; -fx-background-radius: 5; -fx-padding: 10;");
            container.setLayoutX(500);
            container.setLayoutY(100);

            NumberAxis xAxis = new NumberAxis("Tempo (min)", 0, 720, 60);
            NumberAxis yAxis = new NumberAxis("Toneladas", 0, 100, 10);
            graficoLixo = new LineChart<>(xAxis, yAxis);
            graficoLixo.setTitle("Lixo Gerado vs Coletado");
            graficoLixo.setPrefHeight(200);

            serieGerado = new XYChart.Series<>();
            serieGerado.setName("Lixo Gerado");
            serieColetado = new XYChart.Series<>();
            serieColetado.setName("Lixo Coletado");
            graficoLixo.getData().addAll(serieGerado, serieColetado);

            graficoEficiencia = new PieChart();
            graficoEficiencia.setTitle("Eficiência por Tipo de Caminhão");
            graficoEficiencia.setPrefHeight(200);
            graficoEficiencia.setLabelsVisible(true);

            Button exportarBtn = new Button("Exportar Estatísticas");
            exportarBtn.setOnAction(e -> { /* Lógica de exportação */ });

            container.getChildren().addAll(graficoLixo, graficoEficiencia, exportarBtn);
            parent.getChildren().add(container);
            container.setVisible(false);
        }

        public void mostrar() { container.setVisible(true); }
        public void ocultar() { container.setVisible(false); }
        public boolean isVisible() { return container.isVisible(); }

        public void atualizarDadosLixo(int tempo, int lixoGeradoTotal, int lixoColetadoTotal) {
            serieGerado.getData().add(new XYChart.Data<>(tempo, lixoGeradoTotal));
            serieColetado.getData().add(new XYChart.Data<>(tempo, lixoColetadoTotal));
        }

        public void atualizarEficiencia(int eficiencia2t, int eficiencia4t, int eficiencia8t) {
            graficoEficiencia.getData().clear();
            graficoEficiencia.getData().addAll(
                    new PieChart.Data("Caminhões 2T", eficiencia2t),
                    new PieChart.Data("Caminhões 4T", eficiencia4t),
                    new PieChart.Data("Caminhões 8T", eficiencia8t)
            );
        }
    }

    public static class PainelStatusCaminhoes {
        private final VBox container;
        private final Map<String, HBox> linhasCaminhoes = new HashMap<>();

        public PainelStatusCaminhoes(Pane parent) {
            container = new VBox(5);
            container.setPadding(new Insets(10));
            container.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 5;");
            container.setLayoutX(10);
            container.setLayoutY(10);
            container.setPrefWidth(200);

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
            idLabel.setStyle("-fx-font-weight: bold;");
            Label statusLabel = new Label(status);
            statusLabel.setPrefWidth(100);
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

    public static class ModoDebug {
        private final Pane mapa;
        private final List<Node> debugNodes = new ArrayList<>();
        private boolean ativo = false;

        public ModoDebug(Pane mapa) {
            this.mapa = mapa;
        }

        public void alternar() {
            ativo = !ativo;
            if (ativo) {
                ativar();
            } else {
                desativar();
            }
        }

        public boolean isAtivo() {
            return ativo;
        }

        private void ativar() {
            // Exibir grades de coordenadas
            for (int x = 0; x < mapa.getPrefWidth(); x += 100) {
                Line linha = new Line(x, 0, x, mapa.getPrefHeight());
                linha.setStroke(Color.LIGHTGRAY);
                linha.getStrokeDashArray().addAll(5d, 5d);
                debugNodes.add(linha);
            }
            for (int y = 0; y < mapa.getPrefHeight(); y += 100) {
                Line linha = new Line(0, y, mapa.getPrefWidth(), y);
                linha.setStroke(Color.LIGHTGRAY);
                linha.getStrokeDashArray().addAll(5d, 5d);
                debugNodes.add(linha);
            }
            mapa.getChildren().addAll(debugNodes);
        }

        private void desativar() {
            mapa.getChildren().removeAll(debugNodes);
            debugNodes.clear();
        }
    }
}