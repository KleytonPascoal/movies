# Movies

Uma aplicação Android cliente para OMDb API - The Open Movie Database

## Getting Started

Esta aplicação possui dois product flavors

1) mock: deve ser usado para execução dos testes. Ele será usado basicamente para simular as chamadas OMDb API e simulando os dados a serem entregues em cada uma das requisições disparadas. Ele tem irá simular os comportamentos da aplicação na presença de conexões ou não com a internet para os pontos onde se faz necessário o uso da mesma.

2) prod: versão de produção da aplicação com integração à OMDb API.

### Prerequisites

Java 8 e Android SDK setados no path da máquina.


### Testes

A aplicação possui dos grupos de testes:

1) app:androidTest: testes a serem aplicados afim de validar o comportamento de cada tela do app, analisando views, campos, intents lançados etc;
2) features-tests: module a parte contendo tests baseados em features do sistema. Seguindo práticas de [BDD](https://en.wikipedia.org/wiki/Behavior-driven_development).

Exemplo da feature testada:

```
Feature: On My Movies page show saved movies
    As user of Movies App I want see my movies as carousel, as list and as grid.

    @ScenarioId("F01")
    Scenario: Show my movies saved as carousel, as list and as grid
        Given I see My Movies page - F01
        When  I select navigation bottom to show movie as carousel
        Then  I want to see my movies saved as carousel
        When  I select navigation bottom to show movie as list
        Then  I want to see my movies saved as list
        When  I select navigation bottom to show movie as grid
        Then  I want to see my movies saved as grid
        When  I swipe to right my movies grid
        Then  I want to see my movies as list again
        When  I swipe to right my movies list
        Then  I want to see my movies as carousel again

```



### App de para o ambiente de testes

Para realizar os testes execute de regressão das UI:

1 - Build o app com o comando abaixo:

```
./gradlew assembleMock
```

2 - Agora instale o app:

```
./gradlew installMockDebugAndroidTest
```

3- Execute os testes no dispositivo:

```
adb shell am instrument -w -r   -e debug false -e class com.kleytonpascoal.movies.test.suite.UIRegressionTestSuite com.kleytonpascoal.movies.mock.test/android.support.test.runner.AndroidJUnitRunner
```

Após a execução os resultados dos testes poderão serem vistos em:

```
/<your-path>/app/build/reports/androidTests/connected/flavors/MOCK/index.html
```


Agora para testar a feature citada acima, execute

```
./gradlew features-tests:connectedCheck
```

Após a execução os resultados dos testes poderão serem vistos em:

```
/<your-path>/features-tests/build/reports/androidTests/connected/index.html
```


### App de para o ambiente de produção

Para realizar o build da aplicação do ambiente de produção execute o camando abaixo:

```
./gradlew assembleProdDebug
```

Agora realiza a instalação:

```
./gradlew installProdDebug
```

Enjoy ;)

