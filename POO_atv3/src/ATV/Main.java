package ATV;

import java.util.Scanner;

import ATV.Exception.ArquivoInvalidoException;
import ATV.Exception.LivroIndisponivelException;
import ATV.Exception.LivroNaoEncontradoException;
import ATV.Exception.UsuarioNaoEncontradoException;

public class Main {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        Scanner sc = new Scanner(System.in);

        try {
            biblioteca.carregarDados();
            System.out.println("[Sistema] Histórico e dados carregados automaticamente.");
        } catch (ArquivoInvalidoException e) {
            System.out.println("[Aviso] Iniciando sem registros prévios: " + e.getMessage());
        }

        Usuario usuarioLogado = null;
        int opcaoSessao = -1;

        while (opcaoSessao != 0) {
            if (usuarioLogado == null) {
                System.out.println("\n===== BEM-VINDO À BIBLIOTECA =====");
                System.out.println("1. Fazer Login");
                System.out.println("2. Cadastrar Novo Usuário");
                System.out.println("3. Listar Livros Disponíveis");
                System.out.println("0. Salvar e Sair do Sistema");
                System.out.print("Escolha uma opção: ");

                try {
                    opcaoSessao = lerInt(sc);

                    switch (opcaoSessao) {
                        case 1 -> {
                            System.out.print("Digite seu ID de usuário: ");
                            int id = lerInt(sc);

                            try {
                                usuarioLogado = biblioteca.buscarUsuarioPorId(id);
                                System.out.println("Login realizado com sucesso! Bem-vindo, " + usuarioLogado.getNome());
                            } catch (UsuarioNaoEncontradoException e) {
                                System.out.println("[Erro] " + e.getMessage());
                            }
                        }

                        case 2 -> {
                            System.out.println("\n--- Tipo de Cadastro ---");
                            System.out.println("1. Estudante");
                            System.out.println("2. Bibliotecário");
                            System.out.print("Escolha o tipo: ");

                            int tipo = lerInt(sc);

                            if (tipo != 1 && tipo != 2) {
                                System.out.println("[Erro] Tipo inválido! Digite somente 1 para Estudante ou 2 para Bibliotecário.");
                            } else {
                                System.out.print("ID do Usuário (int): ");
                                int id = lerInt(sc);

                                System.out.print("Nome: ");
                                String nome = lerTexto(sc);

                                System.out.print("Email: ");
                                String email = lerTexto(sc);

                                if (tipo == 1) {
                                    biblioteca.cadastrarUsuario(new Estudante(id, nome, email));
                                } else {
                                    biblioteca.cadastrarUsuario(new Bibliotecario(id, nome, email));
                                }
                            }
                        }

                        case 3 -> biblioteca.listarLivrosDisponiveis();

                        case 0 -> {
                            biblioteca.gravarDados();
                            System.out.println("Sistema encerrado.");
                        }

                        default -> System.out.println("Opção inválida!");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("[Erro] Entrada inválida. Insira um número.");
                } catch (Exception e) {
                    System.out.println("[Erro] " + e.getMessage());
                }

            } else if (usuarioLogado instanceof Bibliotecario) {
                System.out.println("\n===== MENU BIBLIOTECÁRIO =====");
                System.out.println("1. Cadastrar Livro");
                System.out.println("2. Buscar Usuário por ID");
                System.out.println("3. Buscar Usuário por Nome");
                System.out.println("4. Realizar Empréstimo");
                System.out.println("5. Realizar Devolução");
                System.out.println("6. Listar Todos os Livros");
                System.out.println("7. Listar Livros Emprestados");
                System.out.println("8. Gravar Dados Agora");
                System.out.println("9. Logout (Trocar de Usuário)");
                System.out.println("0. Salvar e Sair");
                System.out.print("Escolha uma opção: ");

                try {
                    int op = lerInt(sc);

                    switch (op) {
                        case 1 -> {
                            System.out.print("Código do Livro (int): ");
                            int cod = lerInt(sc);

                            System.out.print("Título: ");
                            String tit = lerTexto(sc);

                            System.out.print("Autor: ");
                            String aut = lerTexto(sc);

                            System.out.print("Ano de Publicação: ");
                            int ano = lerInt(sc);

                            biblioteca.cadastrarLivro(new Livro(cod, tit, aut, ano, true));
                        }

                        case 2 -> {
                            System.out.print("Digite o ID do usuário: ");
                            int id = lerInt(sc);
                            System.out.println(biblioteca.buscarUsuarioPorId(id));
                        }

                        case 3 -> {
                            System.out.print("Digite o nome ou parte dele: ");
                            String nome = lerTexto(sc);
                            biblioteca.buscarUsuarioPorNome(nome);
                        }

                        case 4 -> {
                            System.out.print("Código do Livro: ");
                            int codL = lerInt(sc);

                            System.out.print("ID do Usuário: ");
                            int idU = lerInt(sc);

                            biblioteca.realizarEmprestimo(codL, idU);
                        }

                        case 5 -> {
                            System.out.print("Código do Livro para Devolução: ");
                            int codL = lerInt(sc);
                            biblioteca.realizarDevolucao(codL);
                        }

                        case 6 -> biblioteca.listarLivrosCadastrados();

                        case 7 -> biblioteca.listarLivrosEmprestados();

                        case 8 -> biblioteca.gravarDados();

                        case 9 -> {
                            usuarioLogado = null;
                            System.out.println("Logout efetuado.");
                        }

                        case 0 -> {
                            biblioteca.gravarDados();
                            opcaoSessao = 0;
                            System.out.println("Sistema encerrado.");
                        }

                        default -> System.out.println("Opção inválida!");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("[Erro] Entrada inválida.");
                } catch (UsuarioNaoEncontradoException | LivroNaoEncontradoException | LivroIndisponivelException e) {
                    System.out.println("[Erro Negócio] " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("[Erro Sistema] " + e.getMessage());
                }

            } else if (usuarioLogado instanceof Estudante) {
                System.out.println("\n===== MENU DISCENTE (ESTUDANTE) =====");
                System.out.println("1. Buscar Livro por Código");
                System.out.println("2. Buscar Livro por Autor");
                System.out.println("3. Listar Meus Livros Emprestados");
                System.out.println("4. Logout (Trocar de Usuário)");
                System.out.println("0. Salvar e Sair");
                System.out.print("Escolha uma opção: ");

                try {
                    int op = lerInt(sc);

                    switch (op) {
                        case 1 -> {
                            System.out.print("Digite o código do livro: ");
                            int cod = lerInt(sc);
                            System.out.println(biblioteca.buscarLivroPorCodigo(cod));
                        }

                        case 2 -> {
                            System.out.print("Digite o autor ou parte dele: ");
                            String autor = lerTexto(sc);
                            biblioteca.buscarLivroPorAutor(autor);
                        }

                        case 3 -> biblioteca.listarLivrosEmprestadosPorUsuario(usuarioLogado.getId());

                        case 4 -> {
                            usuarioLogado = null;
                            System.out.println("Logout efetuado.");
                        }

                        case 0 -> {
                            biblioteca.gravarDados();
                            opcaoSessao = 0;
                            System.out.println("Sistema encerrado.");
                        }

                        default -> System.out.println("Opção inválida!");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("[Erro] Entrada inválida.");
                } catch (LivroNaoEncontradoException e) {
                    System.out.println("[Erro Negócio] " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("[Erro Sistema] " + e.getMessage());
                }
            }
        }

        sc.close();
    }

    private static String lerTexto(Scanner sc) {
        return sc.nextLine().trim();
    }

    private static int lerInt(Scanner sc) {
        return Integer.parseInt(lerTexto(sc));
    }
}