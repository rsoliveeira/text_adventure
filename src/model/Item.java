package model;

public class Item {
    private int id;
    private String nome;
    private String descricao;

    // Construtor
    public Item(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    // Setters (se precisar alterar os valores depois de criar o objeto)
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Sobrescrever o método toString para exibir os detalhes do Item
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }

    // Sobrescrever equals() e hashCode() para comparar objetos corretamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (id != item.id) return false;
        if (!nome.equals(item.nome)) return false;
        return descricao.equals(item.descricao);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + nome.hashCode();
        result = 31 * result + descricao.hashCode();
        return result;
    }
}
