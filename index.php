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
        console.log('Carregando a cena:', sceneId);  // Log para verificar qual cena está sendo carregada
        fetch(`http://localhost:8080/api/cenas?id=${sceneId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Erro na requisição: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                const sceneDescriptionDiv = document.getElementById('scene-description');
                console.log('Descrição da cena carregada:', data.descricao);  // Log para exibir a descrição recebida
                sceneDescriptionDiv.innerHTML = data.descricao; // Exibe a descrição da cena
                currentScene = sceneId; // Atualiza a cena atual
                console.log('Cena atualizada para:', currentScene);  // Log para verificar se a cena foi atualizada corretamente
            })
            .catch(error => {
                console.log('Erro ao carregar a cena:', error.message);  // Log do erro
            });
    }

    // Carrega a cena inicial quando a página é carregada
    window.onload = function() {
        console.log('Carregando cena inicial');  // Log para verificar quando a cena inicial é carregada
        loadScene(1); // Cena inicial
    };

    // Função principal para processar os comandos
    document.getElementById('submit-command').addEventListener('click', function () {
        const command = document.getElementById('command-input').value.trim().toLowerCase();
        const responseDiv = document.getElementById('response');
        console.log('Comando recebido:', command);  // Log para exibir o comando recebido

        // Verifica se o comando é 'help', 'save' ou 'load'
        if (command === 'help') {
            responseDiv.innerHTML = `
                <strong>Comandos disponíveis:</strong><br>
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
        } else if (command === 'save') {
            // Faz a requisição para salvar o jogo
            console.log('Salvando jogo na cena:', currentScene);
            fetch(`http://localhost:8080/api/save?scene=${currentScene}`, { method: 'POST' })
                .then(response => response.json())
                .then(data => {
                    responseDiv.textContent = data.message;  // Exibe a mensagem do backend
                })
                .catch(error => {
                    console.log('Erro ao salvar o jogo:', error.message);
                    responseDiv.textContent = 'Erro ao salvar o jogo: ' + error.message;
                });

        } else if (command === 'load') {
            // Faz a requisição para carregar o jogo salvo
            console.log('Carregando jogo salvo');
            fetch('http://localhost:8080/api/load')
                .then(response => response.json())
                .then(data => {
                    console.log('Cena carregada:', data.currentScene);  // Log da cena carregada
                    responseDiv.textContent = data.message;  // Exibe a mensagem do backend
                    loadScene(data.currentScene);  // Carrega a cena salva
                })
                .catch(error => {
                    console.log('Erro ao carregar o jogo:', error.message);
                    responseDiv.textContent = 'Erro ao carregar o jogo: ' + error.message;
                });

        } else if (command === 'restart') {
            // Chama a API para reiniciar o jogo
            console.log('Reiniciando o jogo');
            fetch('http://localhost:8080/api/inventory/reset', { method: 'POST' })
                .then(response => response.json())
                .then(data => {
                    console.log('Resposta ao reiniciar jogo:', data.message);
                    responseDiv.textContent = 'Jogo reiniciado e inventário limpo.';
                    loadScene(1);  // Volta para a cena inicial
                })
                .catch(error => {
                    console.log('Erro ao reiniciar o jogo:', error.message);
                    responseDiv.textContent = 'Erro ao reiniciar o jogo: ' + error.message;
                });

        } else if (command.startsWith('get ')) {
            const itemName = command.split(' ')[1];
            console.log('Tentando pegar o item:', itemName);

            // Faz a requisição para adicionar o item ao inventário
            fetch('http://localhost:8080/api/inventory/add', { 
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `name=${encodeURIComponent(itemName)}&sceneId=${currentScene}`  
            })
            .then(response => response.json())
            .then(data => {
                responseDiv.textContent = data.message;
            })
            .catch(error => {
                console.log('Erro ao pegar o item:', error.message);
                responseDiv.textContent = 'Erro ao pegar o item: ' + error.message;
            });

        } else if (command.startsWith('use ')) {
            const parts = command.split(' ');
            const inventoryItem = parts[1];
            const sceneItem = command.slice(command.indexOf('with ') + 5);

            console.log('Tentando usar item:', inventoryItem, 'com:', sceneItem);

            // Verifica se o jogador tem o item no inventário
            fetch(`http://localhost:8080/api/inventory/check?name=${encodeURIComponent(inventoryItem)}`)
                .then(response => response.json())
                .then(data => {
                    console.log('Resposta do inventário:', data);

                    if (data.message.includes('Você tem ' + inventoryItem)) {
                        if (inventoryItem === 'diamante' && sceneItem === 'estátua antiga' && currentScene === 1) {
                            responseDiv.textContent = 'Você usou o DIAMANTE na ESTÁTUA ANTIGA. Avançando para a Cena 2.';
                            loadScene(2); // Avança para a cena 2
                        } else {
                            // Processa o uso do item normalmente
                            fetch('http://localhost:8080/api/actions', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                body: `inventoryItem=${encodeURIComponent(inventoryItem)}&sceneItem=${encodeURIComponent(sceneItem)}&sceneId=${currentScene}`
                            })
                            .then(response => response.json())
                            .then(data => {
                                responseDiv.textContent = data.message;
                            })
                            .catch(error => {
                                console.log('Erro ao processar ação:', error.message);
                                responseDiv.textContent = 'Erro ao processar ação: ' + error.message;
                            });
                        }
                    } else {
                        responseDiv.textContent = 'Você não tem o item necessário no inventário.';
                    }
                })
                .catch(error => {
                    console.log('Erro ao verificar o inventário:', error.message);
                    responseDiv.textContent = 'Erro ao verificar o inventário: ' + error.message;
                });

        } else if (command === 'inventory') {
            console.log('Verificando inventário');
            fetch('http://localhost:8080/api/inventory/list')
                .then(response => response.json())
                .then(data => {
                    responseDiv.innerHTML = `<strong>Inventário:</strong><br>${data.join(', ')}`;
                })
                .catch(error => {
                    console.log('Erro ao buscar o inventário:', error.message);
                    responseDiv.textContent = 'Erro ao buscar o inventário: ' + error.message;
                });

        } else {
            processCommand(command);  // Processa outros comandos
        }

        // Função para processar comandos gerais
        function processCommand(command) {
            console.log('Processando comando geral:', command);  // Log para exibir o comando geral
            fetch(`http://localhost:8080/api/actions`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `command=${encodeURIComponent(command)}&sceneId=${currentScene}`
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log('Resposta ao processar comando:', data.message);
                responseDiv.textContent = data.message;  // Exibe a resposta do backend
            })
            .catch(error => {
                console.log('Erro ao processar comando:', error.message);
                responseDiv.textContent = 'Erro ao processar ação: ' + error.message;
            });
        }
    });
</script>

</body>
</html>
