# Simulador de Carteira de Criptomoedas V.1.2.0

Este projeto é um simulador de carteira de criptomoedas desenvolvido em Kotlin. Ele permite aos usuários simular transações de compra e venda de diferentes criptomoedas, gerenciar carteiras virtuais e acompanhar o desempenho de seus investimentos.

## 🎥 Demonstração
<p float="left">  
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/9ab24b3e-2587-4865-bba9-50dbf6e42f94" width="300" />
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/a5d06c5a-d243-4e72-902a-26724cc6cdc1" width="300" />
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/a69fb18f-9785-48c9-a604-e16ce989343d" width="300" />
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/ef2c74ff-4614-4408-9373-472861509e8e" width="300" />
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/fde6ff77-3162-4c46-bee0-62ccfd9aec01" width="300" />
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/015a63f3-5347-48f9-a114-9dd9f09b5029" width="300" />
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/7ab00643-1e55-41be-b29c-4ff0f5ee6223" width="300" />
</p>



## ✔️ Funcionalidades
- Depósito e saques de dinheiro.
- Compra e venda de criptomoedas.
- Gestão de carteiras virtuais.
- Acompanhamento do desempenho de investimentos.
- Marcação de criptomoedas favoritas.
- Exibição da variação de preços das criptomoedas.
- Funcionalidade de pesquisa de criptomoedas na CryptoListActivity.

## 🌟  Novidades na versão 1.2.0
Funcionalidade de Favoritos: Adicionada a opção de marcar criptomoedas como favoritas na CryptoListActivity e CryptoDetailsActivity.
Exibição de Variação de Preços: Implementada a exibição da variação percentual do valor das criptomoedas nas últimas 24 horas na CryptoListActivity.
Melhorias na Interface do Usuário: Atualizações no layout e nas interfaces das Activities para melhorar a experiência do usuário.
Campo 'isFavorite' e 'change': Adição destes campos no modelo CryptoFromApi para suportar as novas funcionalidades.
Migrações do Banco de Dados: Implementadas para suportar alterações no esquema do banco de dados (versões 7 a 10).
Refatoração Geral: Incluindo a centralização da criação de instâncias do banco de dados na classe AppDatabase.


## 🔨 Técnicas e tecnologias no projeto
- `Kotlin`: Linguagem de programação utilizada para desenvolver o aplicativo.
- `View Binding`: busca de views do layout de forma segura.
- `Coil`: carregar imagens via requisição HTTP.
- `Extension functions`: adicionar comportamentos em outras classes para reutilizá-los como funções de extensão para carregar imagens e formatar valores em moeda.
- `Room Database`: Persistência de dados com Room para armazenar os produtos localmente.
- `Flow`: Para lidar com sequências assíncronas de dados.
- `Retrofit`: para chamadas de API.
- `BigDecimal`: Nova implementação para precisão de dados financeiros.

## 🚀 Futuras Implementações
- Implementação de autenticação de usuários e controle de acesso.
- Sincronização com a nuvem para backup.


## 📝 Uso
Para usar este aplicativo, siga estes passos:

- Abra o aplicativo.
- Siga as instruções na tela para criar sua carteira.
- Comece a simular transações de criptomoedas.

## 📝 Histórico de Versões

### Versão 1.2.0 (22/01/2024)
- Adição de funcionalidade de favoritos para criptomoedas.
- Exibição de variação percentual do valor das criptomoedas.
- Refatorações e melhorias na interface do usuário.
- Atualizações no modelo de dados e banco de dados.

### Versão 1.1.1 (15/01/2024)
- Refatoração para uso de BigDecimal em toda a aplicação.
- Melhorias nas atividades e adapters para suportar BigDecimal.
- Aprimoramento no tratamento de erros e exceções.
- Implementação de verificação de conectividade à Internet e feedback visual na MainActivity.
- Refatoração do CoinRepository para tratamento de erros e manipulação de dados.

### Versão 1.1.0 (09/12/2023)
- Adição de ActionBar e opções de edição em WalletDetailsActivity.
- Suporte para carregamento de imagens SVG com Coil.
- Implementação de detalhes de criptomoedas ao clicar em um item.
- Atualizações no modelo Crypto e Wallet para melhorar a funcionalidade.

### Versão 1.0.1 (08/12/2023)
- Ocultação da carteira de dinheiro na listagem de carteiras.
- Filtragem de criptomoedas com saldo zero na visualização de detalhes da carteira.

### Versão 1.0.0 (04/12/2023)
- Lançamento inicial do aplicativo.
- Funcionalidades principais incluem: gerenciamento de carteiras de criptomoedas e simulação de transações de compra e venda.

## 📧 Contato
- Para mais informações, entre em contato através de tfreitasf@gmail.com.

