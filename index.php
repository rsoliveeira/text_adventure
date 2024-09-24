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

    // Função para buscar o inventário
    function loadInventory() {
        fetch('http://localhost:8080/api/inventory/list')
            .then(response => response.json())
            .then(data => {
                let inventoryList = "<strong>Inventário:</strong><br>";
                data.forEach(item => {
                    inventoryList += `${item}<br>`;
                });
                document.getElementById('response').innerHTML = inventoryList;
            })
            .catch(error => {
                document.getElementById('response').textContent = 'Erro ao carregar inventário: ' + error.message;
            });
    }

    // Carrega a cena 1 quando a página é carregada
    window.onload = function() {
        loadScene(1); // Cena inicial
    };

    // Função principal para processar os comandos
    document.getElementById('submit-command').addEventListener('click', function () {
        const command = document.getElementById('command-input').value.trim().toUpperCase();
        const responseDiv = document.getElementById('response');

        if (command === 'HELP') {
            responseDiv.innerHTML = `
                <strong>Comandos disponíveis:</strong><br>
                <li><strong>O objetivo do text adventure é o usuário interagir com os objetos descritos na cena (identificados pelos nomes em letra maiúsculas) para avançar no jogo. Os comandos possíveis são:</li>
                <ul>
                    <li><strong>USE [ITEM]</strong>: Interage com o item da cena</li>
                    <li><strong>CHECK [ITEM]</strong>: Mostra a descrição do objeto na cena</li>
                    <li><strong>GET [ITEM]</strong>: Adiciona o item ao inventário, se possível</li>
                    <li><strong>INVENTORY</strong>: Mostra os itens que estão no inventário</li>
                    <li><strong>USE [INVENTORY_ITEM] WITH [SCENE_ITEM]</strong>: Realiza a ação utilizando um item do inventário com um item da cena</li>
                    <li><strong>SAVE</strong>: Salva o jogo</li>
                    <li><strong>LOAD</strong>: Carrega um jogo salvo</li>
                    <li><strong>RESTART</strong>: Reinicia o jogo</li>
                </ul>`;
        } else if (command.startsWith('CHECK ')) {
            const itemName = command.split(' ')[1];
            fetch(`http://localhost:8080/api/items?name=${encodeURIComponent(itemName)}&sceneId=${currentScene}`)
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
        } else if (command.startsWith('GET ')) {
            const itemName = command.split(' ')[1];
            fetch(`http://localhost:8080/api/inventory/add?name=${encodeURIComponent(itemName)}`, { method: 'POST' })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Item não encontrado ou não pode ser adicionado.');
                    }
                    return response.json();
                })
                .then(data => {
                    responseDiv.textContent = data.message;
                })
                .catch(error => {
                    responseDiv.textContent = 'Erro ao adicionar item: ' + error.message;
                });
        } else if (command === 'INVENTORY') {
            loadInventory();
        } else if (command === 'USE DIAMANTE WITH ESTÁTUA ANTIGA') {
            // Verifica se o jogador tem o DIAMANTE no inventário
            fetch(`http://localhost:8080/api/inventory/check?name=DIAMANTE`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Falha ao verificar inventário.');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.message.includes('tem DIAMANTE')) {
                        loadScene(2); // Avança para a cena 2
                        responseDiv.textContent = "Você usou o DIAMANTE na ESTÁTUA ANTIGA. A estátua revela um portal para uma nova área!";
                    } else {
                        responseDiv.textContent = "Você precisa pegar o DIAMANTE antes de usá-lo!";
                    }
                })
                .catch(error => {
                    responseDiv.textContent = 'Erro ao verificar o inventário: ' + error.message;
                });
        } else if (command === 'SAVE') {
            fetch('http://localhost:8080/api/save')
                .then(response => response.json())
                .then(data => {
                    responseDiv.textContent = data.message;
                })
                .catch(error => {
                    responseDiv.textContent = 'Erro ao salvar jogo: ' + error.message;
                });
        } else if (command === 'LOAD') {
            fetch('http://localhost:8080/api/load')
                .then(response => response.json())
                .then(data => {
                    loadScene(data.currentScene);
                    responseDiv.textContent = 'Jogo carregado.';
                })
                .catch(error => {
                    responseDiv.textContent = 'Erro ao carregar jogo: ' + error.message;
                });
        } else if (command === 'RESTART') {
            fetch('http://localhost:8080/api/inventory/reset', { method: 'POST' })
                .then(() => {
                    loadScene(1);
                    responseDiv.textContent = 'Jogo reiniciado e inventário limpo.';
                })
                .catch(error => {
                    responseDiv.textContent = 'Erro ao reiniciar o jogo: ' + error.message;
                });
        } else {
            responseDiv.textContent = 'Comando não reconhecido. Tente "HELP" para ver a lista de comandos.';
        }
    });
</script>

</body>
</html>
