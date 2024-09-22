<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Text Adventure</title>
</head>
<body>
    <h1>Text Adventure - Cenas e Itens</h1>

    <!-- Exibir as Cenas -->
    <h2>Cenas</h2>
    <?php
    $cenasApiUrl = "http://localhost:8080/api/cenas";
    $cenas = file_get_contents($cenasApiUrl);
    $cenasArray = json_decode($cenas, true);

    if (!empty($cenasArray)) {
        foreach ($cenasArray as $cena) {
            echo "<div>";
            echo "<h3>" . $cena['titulo'] . "</h3>";
            echo "<p>" . $cena['descricao'] . "</p>";
            echo "</div>";
        }
    } else {
        echo "<p>Nenhuma cena encontrada.</p>";
    }
    ?>

    <!-- Exibir os Itens -->
    <h2>Itens</h2>
    <?php
    $itensApiUrl = "http://localhost:8080/api/items";
    $itens = file_get_contents($itensApiUrl);
    $itensArray = json_decode($itens, true);

    if (!empty($itensArray)) {
        foreach ($itensArray as $item) {
            echo "<div>";
            echo "<h3>" . $item['nome'] . "</h3>";
            echo "<p>" . $item['descricao'] . "</p>";
            echo "</div>";
        }
    } else {
        echo "<p>Nenhum item encontrado.</p>";
    }
    ?>
</body>
</html>
