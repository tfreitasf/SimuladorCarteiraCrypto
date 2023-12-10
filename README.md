# Simulador de Carteira de Criptomoedas V.1.1.0

Este projeto é um simulador de carteira de criptomoedas desenvolvido em Kotlin. Ele permite aos usuários simular transações de compra e venda de diferentes criptomoedas, gerenciar carteiras virtuais e acompanhar o desempenho de seus investimentos.

## 🎥 Demonstração
<p float="left">  
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/a5d06c5a-d243-4e72-902a-26724cc6cdc1" width="300" />
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/a61806ad-055a-497b-9892-1cf75b8ba0b9" width="300" />
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/ef2c74ff-4614-4408-9373-472861509e8e" width="300" />
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/721ab929-11fd-4752-a3f7-d50c7bc15b31" width="300" />
  <img src="https://github.com/tfreitasf/SimuladorCarteiraCrypto/assets/83042767/483e0ada-c74e-4991-ba2c-3a7291168bef" width="300" />
</p>




## ✔️ Funcionalidades
- Depósito e saques de dinheiro.
- Compra e venda de criptomoedas.
- Gestão de carteiras virtuais.
- Acompanhamento do desempenho de investimentos.

## 🌟 Novidades na versão 1.1.0
- Suporte para imagens SVG no carregamento de ícones de criptomoedas com Coil.
- Implementação de ActionBar em WalletDetailsActivity, com opções de edição de carteira.
- Capacidade de abrir detalhes de uma criptomoeda específica ao clicar em um item de cryptomoedas da carteira.
- Atualização da lógica de carregamento e exibição de criptomoedas para usar o modelo Crypto.
- Parcelização do modelo Wallet para facilitar o transporte de dados entre Activities.: Criptomoedas com saldo zero, resultantes de vendas ou outras transações, são automaticamente ocultadas da lista de criptomoedas na carteira, mantendo a interface limpa e focada nos ativos atuais.

## 🔨 Técnicas e tecnologias no projeto
- `Kotlin`: Linguagem de programação utilizada para desenvolver o aplicativo.
- `View Binding`: busca de views do layout de forma segura.
- `Coil`: carregar imagens via requisição HTTP.
- `Extension functions`: adicionar comportamentos em outras classes para reutilizá-los como funções de extensão para carregar imagens e formatar valores em moeda.
- `Room Database`: Persistência de dados com Room para armazenar os produtos localmente.
- `Flow`: Para lidar com sequências assíncronas de dados.
- `Retrofit`: para chamadas de API.

## 🚀 Futuras Implementações
- Implementação de autenticação de usuários e controle de acesso.
- Sincronização com a nuvem para backup.


## 📝 Uso
Para usar este aplicativo, siga estes passos:

- Abra o aplicativo.
- Siga as instruções na tela para criar sua carteira.
- Comece a simular transações de criptomoedas.

## 📝 Histórico de Versões

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

