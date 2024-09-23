<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Text Adventure</title>
    <!-- Importa o arquivo CSS externo -->
    <link rel="stylesheet" href="style.css">
</head>
<body>

<div class="container">
    <h1>Text Adventure</h1>
    <div id="scene-description">
        <!-- A descrição da cena será inserida aqui -->
    </div>
    
    <!-- Campo para o jogador digitar os comandos -->
    <input type="text" id="command-input" placeholder="Digite o comando">
    <button id="submit-command">Enviar</button>

    <div id="response"></div>
</div>

<script>
    // Função para carregar e exibir a cena 1 no início
    function loadScene() {
        fetch('http://localhost:8080/api/cenas?id=1')
            .then(response => response.json())
            .then(data => {
                const sceneDescriptionDiv = document.getElementById('scene-description');
                sceneDescriptionDiv.innerHTML = data.descricao; // Exibe a descrição da cena 1
            })
            .catch(error => {
                console.log('Erro ao carregar a cena: ' + error.message);
            });
    }

    // Carrega a cena 1 quando a página é carregada
    window.onload = loadScene;

    document.getElementById('submit-command').addEventListener('click', function () {
        const command = document.getElementById('command-input').value.trim().toUpperCase();
        const responseDiv = document.getElementById('response');

        // Remove o comando "CHECK" e permite busca direta pelo item
        if (command === 'HELP') {
            // Exibe o menu de ajuda
            responseDiv.innerHTML = `
                <strong>Comandos disponíveis:</strong><br>
                <ul>
                    <li><strong>USE [ITEM]</strong>: Interage com o item da cena</li>
                    <li><strong>GET [ITEM]</strong>: Adiciona o item ao inventário, se possível</li>
                    <li><strong>INVENTORY</strong>: Mostra os itens que estão no inventário</li>
                    <li><strong>USE [INVENTORY_ITEM] WITH [SCENE_ITEM]</strong>: Realiza a ação utilizando um item do inventário com um item da cena</li>
                    <li><strong>SAVE</strong>: Salva o jogo</li>
                    <li><strong>LOAD</strong>: Carrega um jogo salvo</li>
                    <li><strong>RESTART</strong>: Reinicia o jogo</li>
                </ul>`;
        } else if (command === 'INVENTORY') {
            // Buscar inventário e exibir
            fetch('http://localhost:8080/api/inventory')
                .then(response => response.json())
                .then(data => {
                    let inventoryList = "<strong>Inventário:</strong><br>";
                    data.items.forEach(item => {
                        inventoryList += `${item}<br>`;
                    });
                    responseDiv.innerHTML = inventoryList;
                })
                .catch(error => {
                    responseDiv.textContent = 'Erro ao carregar inventário: ' + error.message;
                });
        } else {
            // Faz requisição para buscar o item pelo nome digitado
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
