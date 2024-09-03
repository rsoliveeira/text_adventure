<?php
function sendToJavaServer($command) {
    $host = 'localhost';  // Endereço do servidor Java
    $port = 8080;        // Porta onde o servidor Java está escutando

    // Criando o socket
    $socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
    if ($socket === false) {
        echo "Erro ao criar socket: " . socket_strerror(socket_last_error()) . "\n";
        return null;
    }

    // Conectando ao servidor Java
    $result = socket_connect($socket, $host, $port);
    if ($result === false) {
        echo "Erro ao conectar ao servidor: " . socket_strerror(socket_last_error($socket)) . "\n";
        return null;
    }

    // Enviando o comando ao servidor Java
    socket_write($socket, $command, strlen($command));

    // Recebendo a resposta do servidor Java
    $response = '';
    while ($out = socket_read($socket, 2048)) {
        $response .= $out;
    }

    // Fechando o socket
    socket_close($socket);

    return $response;
}
?>
