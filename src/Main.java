import model.Cena;
import model.Item;
import repository.CenaDAO;
import repository.ItemDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Busca a cena por ID (ajuste o ID conforme necessário)
            Cena cena = CenaDAO.findCenaById(1);
            System.out.println(cena.toString());

            // Busca os itens relacionados à cena atual
            List<Item> itens = ItemDAO.findItensByScene(cena);

            // Exibe a lista de ações (itens) disponíveis
            System.out.println("Selecione uma das ações abaixo:");
            for (int i = 0; i < itens.size(); i++) {
                System.out.println((i + 1) + ". " + itens.get(i).getNome());
            }

            // Lê a escolha do usuário
            int escolha = scanner.nextInt();
            scanner.nextLine();  // Consumir a nova linha

            // Verifica se a escolha é válida
            if (escolha > 0 && escolha <= itens.size()) {
                Item itemEscolhido = itens.get(escolha - 1);
                System.out.println("Você escolheu: " + itemEscolhido.getNome());

                // Verifica se o item escolhido é o primeiro item
                if (itemEscolhido.getIdItem() == 1) {
                    System.out.println("Descrição do item: " + itemEscolhido.getDescricao());
                }

                // Aqui você pode adicionar lógica adicional para lidar com a escolha do usuário
            } else {
                System.out.println("Escolha inválida.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}