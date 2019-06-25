import entities.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Aplicação que simula a configuração e uso de uma urna eletrônica
 * utilizando um banco de dados MySQL.
 *
 * @author Maria Fernanda Silva Tutui
 */
public class principal {

    /**
     * Cria uma eleição com um nome e atribui o status de falso para apuração.
     * Possui a opção de inserir uma quetsão à eleição criada passando o parâmetro
     * idEleição.
     */
    public static void criarEleicao() {

        System.out.println("\n");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|------------------------- CRIAR ELEIÇÃO -----------------------------|");
        System.out.println("|---------------------------------------------------------------------|");

        System.out.println(" Insira o nome da eleição: ");

        Scanner criarEleicao = new Scanner(System.in);
        String nomeEleicao = criarEleicao.nextLine();

        boolean apuraçãoEleicao = false; // parâmetro not NULL no banco de dados

        Eleicao adicionaEleicao = new Eleicao(nomeEleicao, apuraçãoEleicao);
        EleicaoDAO.adicionaEleicao(adicionaEleicao);

        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|   1  - Inserir questões à eleição criada.                           |");
        System.out.println("|   2  - Inserir uma nova eleição.                                    |");
        System.out.println("|   3  - Retornar ao menu principal.                                  |");
        System.out.println("Escolha: ");

        int opcaoCriarEleicao;
        while(true) {
            try {
                opcaoCriarEleicao = criarEleicao.nextInt();
                break;
            } catch (Exception e) {
                criarEleicao.nextLine();
                System.out.println("Entre com uma das opções sugeridas!");
            }
        }

        if (opcaoCriarEleicao == 1) {

            Eleicao criaEleicaoQuestao = EleicaoDAO.retornaIdPorNome(nomeEleicao);
            int idEleicao = criaEleicaoQuestao.getIdEleicao();
            adicionarQuestao(idEleicao);

        } else if (opcaoCriarEleicao == 2) {
            criarEleicao();
        } else
            menu();
    }

    /**
     * Adiciona uma ou várias questões a uma uma eleição especícfica.
     * @param idEleicao Número único identificador de uma eleição.
     */
    public static void adicionarQuestao(int idEleicao) {

        System.out.println("\n");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|----------------------- ADICIONAR QUESTÃO ---------------------------|");
        System.out.println("|---------------------------------------------------------------------|");

        if (idEleicao == 0) {

            System.out.println("Escolha pelo 'idEleicao' a eleição para adicionar uma questão.");

            listarEleicaoSemEstado();
            System.out.println("Escolha: ");

            Scanner adicionaQuestao = new Scanner(System.in);
            idEleicao = adicionaQuestao.nextInt();
            adicionaQuestao.nextLine();
        }

        int controleQuestao = 0;

        while(controleQuestao == 0){

            System.out.println("Insira uma questão:");
            Scanner addQuestao = new Scanner(System.in);
            String pergunta = addQuestao.nextLine();

            System.out.println("Quantidade mínima de respostas: ");
            int minRespostas = addQuestao.nextInt();
            addQuestao.nextLine();

            System.out.println("Quantidade máxima de respostas: ");
            int maxRespostas = addQuestao.nextInt();
            addQuestao.nextLine();

            Pergunta p = new Pergunta(idEleicao, pergunta, minRespostas, maxRespostas);
            PerguntaDAO.adicionaPergunta(p);

            System.out.println("|   1  - Adicionar uma respostas para essa questão.                   |");
            System.out.println("|   2  - Adicionar uma nova questão.                                  |");
            System.out.println("|   3  - Retornar ao menu principal.                                  |");
            System.out.println("Escolha: ");

            while(true) {
                try {
                    controleQuestao = addQuestao.nextInt();
                    break;
                } catch (Exception e) {
                    addQuestao.nextLine();
                    System.out.println("Entre com uma das opções sugeridas!");
                }
            }

            if (controleQuestao == 1) {

                Pergunta q = PerguntaDAO.retornaIDPorPergunta(pergunta);
                int idPergunta = q.getIdPergunta();
                adicionarRespostas(idPergunta, idEleicao);

            } else if (controleQuestao == 2) {
                adicionarQuestao(idEleicao);
            } else {
                menu();
            }
        }
    }

    /**
     * Adiciona uma ou várias respostas a uma uma pergunta especícfica.
     * @param idPergunta Número único identificador de uma pergunta.
     * @param idEleicao Número único identificador de uma eleição.
     */
    public static void adicionarRespostas(int idPergunta, int idEleicao) {

        System.out.println("\n");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|---------------------- ADICIONAR RESPOSTAS --------------------------|");
        System.out.println("|---------------------------------------------------------------------|");

        if (idPergunta == 0) {

            System.out.println("Escolha pelo 'idEleicao' a eleição para visualizar as perguntas referentes a ela.");
            listarEleicaoSemEstado();
            System.out.println("Escolha: ");

            Scanner escolheEleicao = new Scanner(System.in);
            idEleicao = escolheEleicao.nextInt();
            escolheEleicao.nextLine();

            List<Pergunta> listaPerguntas = PerguntaDAO.listarPerguntaPorEleicao(idEleicao);

            if (listaPerguntas.size() == 0 ) {
                System.out.println("|A eleição selecionada não possui questões cadastradas.               |");
                System.out.println("|---------------------------------------------------------------------|");
                menu();
            }

            System.out.println("Escolha pelo 'idPergunta' a pergunta para adicionar uma resposta: ");
            System.out.println("|---------------------------------------------------------------------|");
            System.out.println(String.format("|%-10s | %-56s|", " idPergunta", " Pergunta"));
            System.out.println("|---------------------------------------------------------------------|");

            for (int i = 0; i < listaPerguntas.size(); i++) {
                System.out.println(String.format("| %-10d |%-56s| ",listaPerguntas.get(i).getIdPergunta(), listaPerguntas.get(i).getPergunta()));
            }
            System.out.println("|---------------------------------------------------------------------|");
            System.out.println("Escolha: ");

            idPergunta = escolheEleicao.nextInt();
            escolheEleicao.nextLine();
        }

        // Cria voto em branco
        String respostaBranco = "Branco";
        String respostaNulo = "Nulo";
        int votoBranco = 0;

        int contaBranco = 0;

        List<Resposta> verificaBranco = RespostaDAO.listaRespostaPorIdPergunta(idPergunta);
        for (int i = 0; i < verificaBranco.size(); i++) {
            if (verificaBranco.get(i).getResposta() == respostaBranco) {
                contaBranco = 1;
            }
        }

        if (contaBranco == 0) {
            Resposta b = new Resposta(idPergunta, idEleicao, respostaBranco, votoBranco);
            RespostaDAO.adicionaResposta(b);
            Resposta n = new Resposta(idPergunta, idEleicao, respostaNulo, votoBranco);
            RespostaDAO.adicionaResposta(n);
        }


        int controleResposta = 0;

        while(controleResposta == 0) {

            System.out.println("Insira a resposta: ");

            Scanner addResposta = new Scanner(System.in);
            String resposta = addResposta.nextLine();

            int votoResposta = 0;

            Resposta r = new Resposta(idPergunta, idEleicao, resposta, votoResposta);
            RespostaDAO.adicionaResposta(r);

            System.out.println("|   1  - Adicionar uma nova resposta.                                 |");
            System.out.println("|   2  - Retornar ao menu principal.                                  |");
            System.out.println("Escolha: ");

            controleResposta = addResposta.nextInt();
            addResposta.nextLine();

            if (controleResposta != 1){
                menu();
            } else {
                controleResposta = 0;
            }

        }
    }

    /**
     * Adiciona eleitores a uma eleição específica através de um arquivo de texto.
     */
    public static void adicionarEleitores(){

        System.out.println("\n");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|---------------------- ADICIONAR ELEITORES --------------------------|");
        System.out.println("|---------------------------------------------------------------------|");

        System.out.println("Escolha pelo 'idEleicao' a eleição para adicionar eleitores.");
        listarEleicaoSemEstado();
        System.out.println("Escolha: ");

        Scanner adicionarEleitores = new Scanner(System.in);
        int idEleicao = adicionarEleitores.nextInt();
        adicionarEleitores.nextLine();

        System.out.println("|Selecione o grupo de eleitores:                                      |");
        System.out.println("|   1  - Grupo1.                                                      |");
        System.out.println("|   2  - Grupo2.                                                      |");
        System.out.println("|   3  - Grupo3.                                                      |");
        System.out.println("|   4  - Retornar ao menu principal.                                  |");
        System.out.println("Escolha: ");

        int grupoEleitores = adicionarEleitores.nextInt();
        adicionarEleitores.nextLine();

        String arquivoEleitores = "";

        if (grupoEleitores == 1) {
            arquivoEleitores = "eleitores1.txt";
        }
        else if (grupoEleitores == 2) {
            arquivoEleitores = "eleitores2.txt";
        }
        else if(grupoEleitores == 3) {
            arquivoEleitores = "eleitores3.txt";
        }
        else {
            menu();
        }

        URL url = principal.class.getResource(arquivoEleitores);

        boolean statusEleitor = false;

        File arquivo = new File(url.getPath());
        try {
            Scanner loginEleitor = new Scanner((arquivo));
            while (loginEleitor.hasNextLine()) {
                Eleitor eleitor = new Eleitor(idEleicao,loginEleitor.nextLine(),statusEleitor);
                EleitorDAO.adicionaEleitor(eleitor);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        menu();
    }

    /**
     * Método que abre uma eleição e a torna capaz de receber votos.
     */
    public static void abrirEleicao(){

        System.out.println("\n");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|------------------------- ABRIR ELEIÇÃO -----------------------------|");
        System.out.println("|---------------------------------------------------------------------|");

        System.out.println("Escolha pelo 'idEleicao' a eleição que deseja abrir.");

        listarEleicaoSemEstado();
        System.out.println("Escolha: ");

        Scanner abrirEleicao = new Scanner(System.in);
        int idEleicao = abrirEleicao.nextInt();
        abrirEleicao.nextLine();

        List<Eleitor> eleitores = EleitorDAO.listarEleitores(idEleicao);
        if (eleitores.isEmpty()) {
            System.out.println("|---------------------------------------------------------------------|");
            System.out.println("|A eleição selecionada não possui eleitores cadastrados.              |");
            menu();
        }

        List<Pergunta> perguntas = PerguntaDAO.listarPerguntaPorEleicao(idEleicao);
        if (perguntas.size() == 0) {
            System.out.println("|---------------------------------------------------------------------|");
            System.out.println("|A eleição não possui perguntas cadastradas.                          |");
            menu();
        } else {
            for (int i = 0; i < perguntas.size(); i++) {
                int idPergunta  = perguntas.get(i).getIdPergunta();
                int maxRespostasTeoria = perguntas.get(i).getMaxRespostas();

                List<Resposta> respostas = RespostaDAO.listaRespostaPorIdPergunta(idPergunta);
                int maxRespostasPratica = respostas.size();

                if (maxRespostasPratica < maxRespostasTeoria) {
                    int respostasFaltando = (maxRespostasTeoria - maxRespostasPratica);
                    System.out.println("|---------------------------------------------------------------------|");
                    System.out.println("|Voce ainda deve cadastrar " + respostasFaltando + " respostas para a pergunta de id "+ idPergunta + ".      |");
                    System.out.println("|---------------------------------------------------------------------|");
                    menu();
                }
            }
        }

        String dataAtual = dataAtual();
        EleicaoDAO.abrirEleicao(idEleicao, dataAtual);

        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|Eleição aberta!                                                      |");
        System.out.println("|---------------------------------------------------------------------|");
        menu();
    }

    /**
     * Método que encerra uma eleição específica
     */
    public static void encerrarEleicao() {

        System.out.println("\n");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|----------------------- FINALIZAR ELEIÇÃO ---------------------------|");
        System.out.println("|---------------------------------------------------------------------|");

        System.out.println("Escolha pelo 'idEleicao' a eleição que deseja finalizar.");

        listarEleicaoAberta();
        System.out.println("idEleicao: ");

        Scanner fecharEleicao = new Scanner(System.in);
        int idEleicao = fecharEleicao.nextInt();
        fecharEleicao.nextLine();

        String dataAtual = dataAtual();
        EleicaoDAO.encerrarEleicao(idEleicao, dataAtual);

        menu();
    }

    /**
     * Apura uma eleição específica, desde que tenha sido encerrada.
     */
    public static void apurarEleicao() {

        System.out.println("\n");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|------------------------- APURAR ELEIÇÃO ----------------------------|");
        System.out.println("|---------------------------------------------------------------------|");

        System.out.println("Escolha pelo 'idEleicao' a eleição que deseja apurar.");

        List<Eleicao> eleicoes = EleicaoDAO.listarEleicaoEncerrada();

        formataPlot();

        for (int i = 0; i < eleicoes.size(); i++) {
            if (eleicoes.get(i).getEstadoEleicao() == false) {
                System.out.println(String.format("| %-10d |%-56s| ",eleicoes.get(i).getIdEleicao() , eleicoes.get(i).getNomeEleicao()));
            }
        }
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("Escolha : ");

        Scanner apurarEleicao = new Scanner(System.in);
        int idEleicao = apurarEleicao.nextInt();
        apurarEleicao.nextLine();

        EleicaoDAO.apurarEleicao(idEleicao);

        System.out.println("|Deseja visualizar o resultado dessa eleição?                         |");
        System.out.println("|   1  - Sim                                                          |");
        System.out.println("|   2  - Não, retornar ao menu principal                              |");
        System.out.println("Escolha: ");

        int opcao = apurarEleicao.nextInt();
        apurarEleicao.nextLine();

        if (opcao == 1) {
            resultadosEleicao(idEleicao);
        } else {
            menu();
        }
    }

    /**
     * Método que permite o usuário depois de logado ao sistema,
     * votar em uma eleição.
     */
    public static void votar() {

        System.out.println("\n");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|------------------------------ VOTAR --------------------------------|");
        System.out.println("|---------------------------------------------------------------------|");

        String loginPessoa = verificaLoginSenha();

        List<Eleitor> eleicaoDoEleitor = EleitorDAO.listarEleicaoDeEleitor(loginPessoa);
        List<Eleicao> eleicaoAberta = EleicaoDAO.listarEleicaoAberta();

        if (eleicaoDoEleitor.isEmpty()) {
            System.out.println("|Você não esta cadastrado para votar em nenhuma eleição.              |");
            menu();
        }

        if (eleicaoAberta.isEmpty()) {
            System.out.println("|Nenhuma eleição aberta no momento, não é possível votar.             |");
            menu();
        }

        // Gera uma lista com os ids das eleições abertas que o eleitor en questão pode pode votar
        ArrayList<Integer> listaIdEleicao = new ArrayList();
        for (int i = 0; i < eleicaoDoEleitor.size(); i++) {
            int idEleicaoEleitor = eleicaoDoEleitor.get(i).getIdEleicao();
            for (int j = 0; j < eleicaoAberta.size(); j++) {
                int idEleicaoAberta = eleicaoAberta.get(j).getIdEleicao();
                if (idEleicaoAberta == idEleicaoEleitor) {
                    listaIdEleicao.add(idEleicaoAberta);
                }
            }
        }

        if (listaIdEleicao.isEmpty()) {
            System.out.println("|Nenhuma eleição aberta disponível para esse usuário.                 |");
            menu();
        }

        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|Escolha pelo 'idEleicao' a eleição que deseja votar.                 |");

        formataPlot();

        // Mostra as eleições abertas para o eleitor
        for (int n = 0; n < listaIdEleicao.size(); n++) {
            int idLista = listaIdEleicao.get(n);
            for (int q = 0; q < eleicaoAberta.size(); q++) {
                int idEleicaoAberta = eleicaoAberta.get(q).getIdEleicao();

                if (idLista == idEleicaoAberta) {
                    System.out.println(String.format("| %-10d |%-56s| ", eleicaoAberta.get(q).getIdEleicao(), eleicaoAberta.get(q).getNomeEleicao()));
                }
            }
        }

        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|Escolha:                                                             |");

        Scanner votacao = new Scanner(System.in);
        int idEleicao = votacao.nextInt();
        votacao.nextLine();

        if (EleitorDAO.verificarVotoEmEleicao(idEleicao, loginPessoa) == true) {
            System.out.println("|---------------------------------------------------------------------|");
            System.out.println("|Você já votou nessa eleição!                                         |");
            System.out.println("|---------------------------------------------------------------------|");
            menu();
        }

        List<Pergunta> listaPerguntas = PerguntaDAO.listarPerguntaPorEleicao(idEleicao);

        for (int s = 0; s < listaPerguntas.size(); s++) {

            int idPergunta = listaPerguntas.get(s).getIdPergunta();
            int minRespostas = listaPerguntas.get(s).getMinRespostas();
            int maxRespostas = listaPerguntas.get(s).getMaxRespostas();

            System.out.println("|---------------------------------------------------------------------|");
            System.out.println(String.format("|%-7s %-59s|", "Pergunta:", listaPerguntas.get(s).getPergunta()));
            System.out.println("|---------------------------------------------------------------------|");
            System.out.println("|Opções de resposta:                                                  |");
            System.out.println("|---------------------------------------------------------------------|");

            List<Resposta> listaRespostas = RespostaDAO.listaRespostaPorIdPergunta(listaPerguntas.get(s).getIdPergunta());

            System.out.println(String.format("| %-10s |%-56s| ", "Número", " Resposta"));
            System.out.println("|---------------------------------------------------------------------|");

            for (int j = 0; j < listaRespostas.size(); j++) {

                // Não exibe Nulo
                if (!listaRespostas.get(j).getResposta().equals("Nulo")) {
                    System.out.println(String.format("| %-10s | %-55s| ", "[ " + listaRespostas.get(j).getIdResposta() + " ]", listaRespostas.get(j).getResposta()));
                }
            }

            if (minRespostas == maxRespostas) {
                System.out.println("|Escolha um número da opções para votar.                              |");
                System.out.println("|Insira N para votar nulo.                                            |");
                System.out.println("|---------------------------------------------------------------------|");
                System.out.println("|Voto:                                                                |");

                Scanner escolheVoto = new Scanner(System.in);
                String voto = escolheVoto.nextLine();

                // Vefifica se votou Nulo
                if (voto.equals("N")) {
                    for (int k = 0; k < listaRespostas.size(); k++) {
                        if (listaRespostas.get(k).getResposta().equals("Nulo")) {
                            RespostaDAO.vota(listaRespostas.get(k).getIdResposta());
                        }
                    }
                } else {
                    int novoInt = Integer.parseInt(voto);
                    RespostaDAO.vota(novoInt);
                }

            } else {
                System.out.println("Escolha no mínimo " + minRespostas + " e no máximo " + maxRespostas + " números para votar.");
                System.out.println("Para votar basta inserir os números das perguntas separados por vígula.");
                System.out.println("|Insira N para votar nulo.                                            |");
                System.out.println("|Exemplo de voto válido: 1,2,3                                        |");
                System.out.println("|Exemplo de voto válido: 1,2,N                                        |");
                System.out.println("|---------------------------------------------------------------------|");
                System.out.println("|Voto:                                                                |");

                Scanner escolheVoto = new Scanner(System.in);
                String votoMulti = escolheVoto.nextLine();
                String[] votoUni = votoMulti.split(",");

                for (int l = 0; l < votoUni.length; l++) {
                    if (votoUni[l].equals("N")) {
                        for (int m = 0; m < listaRespostas.size(); m++) {
                            if (listaRespostas.get(m).getResposta() == "Nulo") {
                                RespostaDAO.vota(listaRespostas.get(m).getIdResposta());
                            }
                        }
                    } else {
                        RespostaDAO.vota(Integer.parseInt(votoUni[l]));
                    }
                }
            }
        }
        EleitorDAO.votaEmEleicao(idEleicao, loginPessoa);
        menu();
    }

    /** Método que mostra o resultado de uma eleição específica.
     * @param idEleicao Número único identificador de uma eleição.
     **/
    public static void resultadosEleicao(int idEleicao) {

        System.out.println("\n");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|------------------------ RESULTADO ELEIÇÃO --------------------------|");
        System.out.println("|---------------------------------------------------------------------|");

        if (idEleicao == 0) {

            System.out.println("Escolha pelo 'idEleicao' a eleição que que deseja observar os resultados.");


            List<Eleicao> eleicoes = EleicaoDAO.listarEleicaoApurada();

            if (eleicoes.isEmpty()) {
                System.out.println("|---------------------------------------------------------------------|");
                System.out.println("|Nenhuma eleição apurada.                                             |");
                System.out.println("|---------------------------------------------------------------------|");
            }

            formataPlot();

            for (int i = 0; i < eleicoes.size(); i++) {
                System.out.println(String.format("| %-10d |%-56s| ", eleicoes.get(i).getIdEleicao(), eleicoes.get(i).getNomeEleicao()));
            }

            System.out.println("|---------------------------------------------------------------------|");


            System.out.println("Escolha: ");

            Scanner escolheEleicao = new Scanner(System.in);
            idEleicao = escolheEleicao.nextInt();
            escolheEleicao.nextLine();
        }

        Eleicao eleicaoPorId = EleicaoDAO.retornaNomePorID(idEleicao);

        System.out.println("|---------------------------------------------------------------------|");
        System.out.println(String.format("|%-68s |", "Eleição: " + eleicaoPorId.getNomeEleicao()));

        List<Pergunta> perguntas = PerguntaDAO.listarPerguntaPorEleicao(idEleicao);


        for (int i = 0 ; i < perguntas.size(); i++) {

            System.out.println("|---------------------------------------------------------------------|");
            System.out.println(String.format("| %-67s | ", perguntas.get(i).getPergunta()));
            System.out.println("|---------------------------------------------------------------------|");
            System.out.println(String.format("|%-30s| %-36s |", "Resposta ", "Votos"));
            System.out.println("|---------------------------------------------------------------------|");


            List<Resposta> respostas = RespostaDAO.listaRespostaPorIdPergunta(perguntas.get(i).getIdPergunta());
            for (int j = 0; j < respostas.size(); j++) {
                System.out.println(String.format("|%-30s| %-36s |",respostas.get(j).getResposta(), respostas.get(j).getVotoRespota()));
            }
        }
        System.out.println("|---------------------------------------------------------------------|");
    }

    /**
     * Seleciona um método através do uso de um switch case.
     * @param option Número digitado pelo usuário que escolhe
     * uma opção do menu principal
     */
    public static void menuCase(int option) {

        int idEleicao = 0;
        int idPergunta = 0;

        switch (option){
            case 1: criarEleicao();
                break;
            case 2: adicionarQuestao(idEleicao);
                break;
            case 3: adicionarRespostas(idPergunta, idEleicao);
                break;
            case 4: adicionarEleitores();
                break;
            case 5: abrirEleicao();
                break;
            case 6: encerrarEleicao();
                break;
            case 7: apurarEleicao();
                break;
            case 8:
                resultadosEleicao(idEleicao);
                break;
            case 9: votar();
                break;
            default:
                break;
        }
    }

    /**
     * Plota o cabeçalo mostrando 'idEleicao' e 'Nome'.
     */
    public static void formataPlot() {
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println(String.format("|%-10s |%-56s|", " idEleicao ", " Nome "));
        System.out.println("|---------------------------------------------------------------------|");
    }

    /**
     * Lista e plota todas as eleições com estado nulo a fim de identificar
     * eleições para abrir.
     */
    public static void listarEleicaoSemEstado() {

        List<Eleicao> eleicoes = EleicaoDAO.listarEleicaoSemEstado();

        formataPlot();

        for (int i = 0; i < eleicoes.size(); i++) {
            System.out.println(String.format("| %-10d |%-56s| ",eleicoes.get(i).getIdEleicao() , eleicoes.get(i).getNomeEleicao()));
        }
        System.out.println("|---------------------------------------------------------------------|");
    }

    /**
     * Lista e plota todas as eleições já abertas.
     */
    public static void listarEleicaoAberta() {

        List<Eleicao> eleicoes = EleicaoDAO.listarEleicaoAberta();

        formataPlot();

        for (int i = 0; i < eleicoes.size(); i++) {

            System.out.println(String.format("| %-10d |%-56s| ",eleicoes.get(i).getIdEleicao() , eleicoes.get(i).getNomeEleicao()));
        }

        System.out.println("|---------------------------------------------------------------------|");
    }

    /**
     * Retorna a data atual no formado (yyyy-MM-dd).
     * @return String data atual.
     */
    public static String dataAtual() {
        Date data = new Date(System.currentTimeMillis());
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String dataAtual = formatDate.format(data);
        return dataAtual;
    }

    /**
     * Método utilzado para a confirmação de login e senha
     * afim de liberar o usuário para realizar uma votação.
     */
    public static String verificaLoginSenha() {

        System.out.println("Entre com o seu login: ");

        Scanner verifica = new Scanner(System.in);
        String loginPessoa = verifica.nextLine();

        while(!PessoaDAO.verificaLogin(loginPessoa)) {

            System.out.println("|Login incorreto, usuário não encontrado no sistema!                  |");
            System.out.println("|   1  - Tentar novamente.                                            |");
            System.out.println("|   2  - Retornar ao menu principal.                                  |");
            System.out.println("Escolha: ");

            int validaLogin = verifica.nextInt();
            verifica.nextLine();

            if (validaLogin != 1) {
                menu();
            } else {
                System.out.println("Entre com o seu login: ");
                loginPessoa = verifica.nextLine();
            }
        }

        System.out.println("Senha: ");
        String senhaPessoa = verifica.nextLine();

        while(!PessoaDAO.verificaLoginSenha(loginPessoa, senhaPessoa)) {

            System.out.println("|Senha incorreta!                                                     |");
            System.out.println("|   1  - Tentar novamente.                                            |");
            System.out.println("|   2  - Retornar ao menu principal.                                  |");
            System.out.println("Escolha: ");

            int validaSenha = verifica.nextInt();
            verifica.nextLine();

            if (validaSenha != 1) {
                menu();
            } else {
                System.out.println("Senha: ");
                senhaPessoa = verifica.nextLine();
            }
        }

        return loginPessoa;
    }

    /**
     * Método que apresenta o menu principal ao usuário.
     */
    public static void menu() {

        int option;

        System.out.println("\n");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|------------------------ URNA ELETRÔNICA ----------------------------|");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.println("|   1  - Criar eleição                                                |");
        System.out.println("|   2  - Adicionar questões                                           |");
        System.out.println("|   3  - Adicionar respostas                                          |");
        System.out.println("|   4  - Adiconar eleitores                                           |");
        System.out.println("|   5  - Abrir eleicao                                                |");
        System.out.println("|   6  - Fechar eleicao                                               |");
        System.out.println("|   7  - Apurar eleicao                                               |");
        System.out.println("|   8  - Exibir resultado de uma eleição                              |");
        System.out.println("|   9  - Votar em uma eleição                                         |");
        System.out.println("|   10 - Sair                                                         |");
        System.out.println("|---------------------------------------------------------------------|");
        System.out.print("\n");
        System.out.println("Escolha:");

        Scanner menu_options = new Scanner(System.in);
        option = menu_options.nextInt();
        menuCase(option);
    }

    /**
     * Método principal
     */
    public static void main(String[] args) {
        menu();
    }
}