<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Text Adventure</title>
    <link rel="stylesheet" href="style.css"> <!-- Importando o CSS -->
</head>
<body>

<div class="container">
    <h1>A Busca pelo Artefato Perdido</h1>
    <div id="scene-description">
        <!-- A descrição da cena será inserida aqui -->
    </div>
    
    <!-- Campo para o jogador digitar os comandos -->
    <input type="text" id="command-input" placeholder="Digite o comando">
    <button id="submit-command">Enviar</button>

    <div id="response"></div>
</div>

<script>
    let currentScene = 1;

    // Função para carregar a cena
    function loadScene(sceneId) {
        fetch(`http://localhost:8080/api/cenas?id=${sceneId}`)
            .then(response => response.json())
            .then(data => {
                const sceneDescriptionDiv = document.getElementById('scene-description');
                sceneDescriptionDiv.innerHTML = data.descricao; // Exibe a descrição da cena
                currentScene = sceneId; // Atualiza a cena atual
            })
            .catch(error => {
                console.log('Erro ao carregar a cena: ' + error.message);
            });
    }

    // Carrega a cena 1 quando a página é carregada
    window.onload = function() {
        loadScene(1); // Cena inicial
    };

    document.getElementById('submit-command').addEventListener('click', function () {
    const command = document.getElementById('command-input').value.trim().toUpperCase();
    const responseDiv = document.getElementById('response');

    if (currentScene === 1 && command === 'DIAMANTE') {
        loadScene(2); // Carregar Cena 2
        responseDiv.textContent = 'Você avançou para o Santuário de Luz.';
        currentScene = 2; // Atualiza a cena atual
    } else if (currentScene === 2 && command === 'LUZ DA VERDADE') {
        loadScene(3); // Carregar Cena 3
        responseDiv.textContent = 'Correto! Você avançou para o Labirinto de Sombras.';
        currentScene = 3; // Atualiza a cena atual
    } else if (currentScene === 3 && command === 'LUA') {
        loadScene(4); // Carregar Cena 4
        responseDiv.textContent = 'Você escolheu corretamente! Bem-vindo ao Jardim das Sombras.';
        currentScene = 4; // Atualiza a cena atual
    } else if (currentScene === 4 && command === 'LUZ') {
        loadScene(5); // Carregar Cena 5
        responseDiv.textContent = 'Você atravessou a Ponte de Luz e chegou ao Altar de Pedra.';
        currentScene = 5; // Atualiza a cena atual
    } else {
        // Caso o comando não corresponda à escolha correta, ou seja, um comando de interação genérico
        fetch(`http://localhost:8080/api/items?name=${encodeURIComponent(command)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Item não encontrado.');
                }
                return response.json();
            })
            .then(data => {
                responseDiv.textContent = data.descricao; // Mostra a descrição do item
            })
            .catch(error => {
                responseDiv.textContent = 'Erro ao buscar item: ' + error.message;
            });
    }
});

</script>

</body>
</html>
