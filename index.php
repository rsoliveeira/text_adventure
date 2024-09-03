<?php
require_once 'backend.php';  // Inclui o arquivo com a função sendToJavaServer

session_start();

if (!isset($_SESSION['cena_atual'])) {
    $_SESSION['cena_atual'] = 1; // Começa na cena 1
}

// Verifica se um comando foi enviado
if (isset($_POST['command'])) {
    $command = strtolower(trim($_POST['command']));

    // Processa o comando enviado
    switch ($command) {
        case 'parede de pedra':
            $response = sendToJavaServer($command);
            echo nl2br($response);  // Exibe a resposta do servidor Java
            break;

        case 'estátua antiga':
            $response = sendToJavaServer($command);
            echo nl2br($response);  // Exibe a resposta do servidor Java
            break;

        default:
            echo "INVALID COMMAND";
            break;
    }
} else {
    echo "Nenhum comando enviado.";
}
?>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Text Adventure</title>
    <style>
        body {
            background-color: #000;
            color: #00FF00;
            font-family: 'Courier New', Courier, monospace;
            padding: 20px;
        }
        #console {
            border: 1px solid #00FF00;
            padding: 10px;
            margin-bottom: 20px;
            height: 300px;
            overflow-y: auto;
            white-space: pre-wrap;
        }
        #inputLine {
            color: #00FF00;
            display: flex;
        }
        #command {
            background-color: #000;
            color: #00FF00;
            border: none;
            outline: none;
            flex: 1;
            font-family: inherit;
            font-size: inherit;
        }
        #prompt {
            padding-right: 10px;
        }
    </style>
</head>
<body>
    <h1>Bem-vindo ao Jornada digital: em busca do Artefado Perdido</h1>

    <form method="POST" action="">
        <label for="command">Digite um comando:</label><br>
        <input type="text" id="command" name="command" required><br><br>
        <input type="submit" value="Enviar Comando">
    </form>
</body>
</html>
