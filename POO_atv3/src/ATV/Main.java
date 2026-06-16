package ATV;

import java.util.Scanner;

import ATV.Exception.ArquivoInvalidoException;

public class Main {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        Scanner sc = new Scanner(System.in);

        // Carrega automaticamente oo iniciar
        try {
            biblioteca.carregarDados();
            System.out.println("[Sistema] Histórico e dados carregados automaticamente.");
        } catch (ArquivoInvalidoException e) {
            System.out.println("[Aviso] Iniciando sem registros prévios: " + e.getMessage());
        }

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===== MENU BIBLIOTECA =====");
            System.out.println("1. Cadastrar Livro");
            System.out.println("2. Cadastrar Usuário");
            System.out.println("3. Buscar Usuário por ID");
            System.out.println("4. Buscar Usuário por Nome");
            System.out.println("5. Buscar Livro por Código");
            System.out.println("6. Buscar Livro por Autor");
            System.out.println("7. Realizar Empréstimo");
            System.out.println("8. Realizar Devolução");
            System.out.println("9. Listar Todos os Livros");
            System.out.println("10. Listar Livros Disponíveis");
            System.out.println("11. Listar Livros Emprestados");
            System.out.println("12. Listar Livros Emprestados por Usuário");
            System.out.println("13. Gravar Dados Agora");
            System.out.println("0. Salvar e Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(sc.nextLine());
                switch (opcao) {
                    case 1 -> {
                        System.out.print("Código do Livro (int): "); int cod = Integer.parseInt(sc.nextLine());
                        System.out.print("Título: "); String tit = sc.nextLine();
                        System.out.print("Autor: "); String aut = sc.nextLine();
                        System.out.print("Ano de Publicação: "); int ano = Integer.parseInt(sc.nextLine());
                        biblioteca.cadastrarLivro(new Livro(cod, tit, aut, ano, true));
                    }
                    case 2 -> {
                        System.out.print("ID do Usuário (int): "); int id = Integer.parseInt(sc.nextLine());
                        System.out.print("Nome: "); String nome = sc.nextLine();
                        System.out.print("Email: "); String email = sc.nextLine();
                        biblioteca.cadastrarUsuario(new Usuario(id, nome, email));
                    }
                    case 3 -> {
                        System.out.print("Digite o ID do usuário: "); int id = Integer.parseInt(sc.nextLine());
                        System.out.println(biblioteca.buscarUsuarioPorId(id));
                    }
                    case 4 -> {
                        System.out.print("Digite o nome ou parte dele: "); String nome = sc.nextLine();
                        biblioteca.buscarUsuarioPorNome(nome);
                    }
                    case 5 -> {
                        System.out.print("Digite o código do livro: "); int cod = Integer.parseInt(sc.nextLine());
                        System.out.println(biblioteca.buscarLivroPorCodigo(cod));
                    }
                    case 6 -> {
                        System.out.print("Digite o autor ou parte dele: "); String autor = sc.nextLine();
                        biblioteca.buscarLivroPorAutor(autor);
                    }
                    case 7 -> {
                        System.out.print("Código do Livro: "); int codL = Integer.parseInt(sc.nextLine());
                        System.out.print("ID do Usuário: "); int idU = Integer.parseInt(sc.nextLine());
                        biblioteca.realizarEmprestimo(codL, idU);
                    }
                    case 8 -> {
                        System.out.print("Código do Livro para Devolução: "); int codL = Integer.parseInt(sc.nextLine());
                        biblioteca.realizarDevolucao(codL);
                    }
                    case 9 -> biblioteca.listarLivrosCadastrados();
                    case 10 -> biblioteca.listarLivrosDisponiveis();
                    case 11 -> biblioteca.listarLivrosEmprestados();
                    case 12 -> {
                        System.out.print("ID do Usuário: "); int idU = Integer.parseInt(sc.nextLine());
                        biblioteca.listarLivrosEmprestadosPorUsuario(idU);
                    }
                    case 13 -> biblioteca.gravarDados();
                    case 0 -> {
                        biblioteca.gravarDados(); // Salva tudo antes de sair
                        System.out.println("Sistema encerrado.");
                    }
                    default -> System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("[Erro] Por favor, insira um número válido para as opções/IDs.");
            } catch (Exception e) {
                // Captura as exceções personalizadas (LivroNaoEncontrado, etc.) e mostra na tela
                System.out.println("[Erro] " + e.getMessage());
            }
        }
        sc.close();
    }
}