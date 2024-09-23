<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Text Adventure</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #2b2b2b;
            color: #fff;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            text-align: center;
        }

        h1 {
            font-size: 2em;
            margin-bottom: 20px;
        }

        #scene-description {
            margin: 20px 0;
            font-size: 1.2em;
            width: 80%;
        }

        input[type="text"] {
            padding: 10px;
            font-size: 1em;
            width: 300px;
        }

        button {
            padding: 10px;
            font-size: 1em;
            margin-left: 10px;
            cursor: pointer;
        }

        #response {
            margin-top: 20px;
            font-size: 1.2em;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Text Adventure</h1>
    <div id="scene-description">
        <!-- Descrição da Cena 1 -->
        Você está diante da Caverna Encantada, uma formação grande com uma entrada na rocha coberta de musgo e trepadeiras. 
        A entrada possui desenhos esculpidos em padrões geométricos e símbolos enigmáticos. 
        Dentro da caverna, você vê uma <strong>ESTÁTUA ANTIGA</strong> de pedra com uma mão estendida, preparada para receber algo. 
        A <strong>PAREDE DE PEDRA</strong> ao fundo está coberta com várias pedras que brilham intensamente. 
        A inscrição na parede fornece uma pista enigmática: "Entre o brilho das pedras, a chave para a aventura está escondida."
    </div>
    
    <!-- Campo para o jogador digitar os comandos -->
    <input type="text" id="command-input" placeholder="Digite o comando">
    <button id="submit-command">Enviar</button>

    <div id="response"></div>
</div>

<script>
    document.getElementById('submit-command').addEventListener('click', function () {
        const command = document.getElementById('command-input').value.trim().toUpperCase();
        const responseDiv = document.getElementById('response');
        
        if (command === 'HELP') {
            // Exibe o menu de ajuda
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
        } else if (command.startsWith('CHECK')) {
    const itemName = command.split(' ')[1]; // Obtém o nome do item
    // Faz requisição à API para obter a descrição do item
    fetch(`http://localhost:8080/api/items?name=${encodeURIComponent(itemName)}`)
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
}else if (command.startsWith('GET')) {
    const itemName = command.split(' ')[1]; // Obtém o nome do item
    // Faz requisição à API para adicionar o item ao inventário
    fetch(`http://localhost:8080/api/inventory/add?name=${encodeURIComponent(itemName)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Item não encontrado ou não pode ser pego.');
            }
            return response.json();
        })
        .then(data => {
            responseDiv.textContent = data.message; // Mensagem de sucesso ou erro
        })
        .catch(error => {
            responseDiv.textContent = 'Erro ao adicionar item: ' + error.message;
        });
}else if (command.startsWith('USE')) {
    const parts = command.split(' ');
    const inventoryItem = parts[1]; // Item do inventário
    const sceneItem = parts[3]; // Item da cena
    // Faz requisição à API para usar o item
    fetch(`http://localhost:8080/api/use?inventoryItem=${encodeURIComponent(inventoryItem)}&sceneItem=${encodeURIComponent(sceneItem)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Ação não permitida.');
            }
            return response.json();
        })
        .then(data => {
            responseDiv.textContent = data.message; // Mensagem de sucesso ou erro
        })
        .catch(error => {
            responseDiv.textContent = 'Erro ao usar item: ' + error.message;
        });
} else if (command === 'INVENTORY') {
    fetch('http://localhost:8080/api/inventory?jogadorId=1') // Ajustar para o jogador correto
        .then(response => response.json())
        .then(data => {
            let inventoryList = "<strong>Inventário:</strong><br>";
            data.forEach(item => {
                inventoryList += `${item.nome} - ${item.quantidade}<br>`;
            });
            responseDiv.innerHTML = inventoryList;
        })
        .catch(error => {
            responseDiv.textContent = 'Erro ao carregar inventário: ' + error.message;
        });
}

    });
</script>


</body>
</html>
